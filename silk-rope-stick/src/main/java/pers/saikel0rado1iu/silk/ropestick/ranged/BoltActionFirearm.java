/*
 * This file is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.ropestick.ranged;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">栓动式枪械</h2>
 * 用于创建一个有装填容量的，需要一颗一颗装填发射物，并且一颗一颗发射的远程武器<br>
 * 如需要，请手动实现如多重射击、快速装填等附魔效果
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class BoltActionFirearm extends Crossbow implements ProjectileContainer, ShootExpansion {
	protected int maxUseTicks = 0;
	protected int loadableAmount = 0;
	private int tempUseTicks = 0;
	
	/**
	 * @param settings 物品设置
	 */
	public BoltActionFirearm(Settings settings) {
		super(settings);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return maxUseTicks;
	}
	
	@Override
	public float getUsingProgress(int useTicks, ItemStack stack) {
		if (useTicks == 0) tempUseTicks = 0;
		return (Math.min(1, (tempUseTicks = Math.max(tempUseTicks, useTicks)) / (float) getMaxUseTime(stack)) * loadableAmount) % 1;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		setProjectileIndex(stack, user.getProjectileType(stack));
		ShootExpansion.resetShot(stack);
		loadableAmount = getLoadableAmount(stack, Optional.of(user));
		maxUseTicks = Math.round((float) maxUseTicks() * maxCapacity(stack) / maxCapacity(stack) * loadableAmount);
		// 如果已装填
		if (isCharged(stack)) {
			shoot(world, user, hand, stack, getMaxProjectileSpeed(stack), firingError());
			if (ProjectileContainer.getChargedAmount(stack) == 0) setCharged(stack, false);
			return TypedActionResult.pass(stack);
		}
		// 如果使用者有弹药
		if (!user.getProjectileType(stack).isEmpty()) {
			charged = false;
			loaded = false;
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		// 如果未装填
		return TypedActionResult.fail(stack);
	}
	
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient()) return;
		ShootExpansion.resetShot(stack);
		int level = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		double pullProgress = getUsingProgress(getMaxUseTime(stack) - remainingUseTicks, stack);
		if (pullProgress < 0.2) {
			charged = false;
			loaded = false;
		} else if (pullProgress > 0.3 && !charged) {
			charged = true;
			load(user, stack);
			world.playSound(null, user.getX(), user.getY(), user.getZ(), getQuickChargeSound(level), SoundCategory.PLAYERS, 1, 1);
		} else if (pullProgress > 0.9 && level == 0 && !loaded) {
			loaded = true;
			world.playSound(null, user.getX(), user.getY(), user.getZ(), loadingSound(), SoundCategory.PLAYERS, 1, 1);
		}
	}
	
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (ProjectileContainer.getChargedAmount(stack) > 0 && !isCharged(stack)) {
			// 设置已装填
			setCharged(stack, true);
			// 获取声音类别
			SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
			// 播放弩装填结束音效
			world.playSound(null, user.getX(), user.getY(), user.getZ(), loadedSound(), soundCategory, 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
		}
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		// 获取弹药
		List<ItemStack> projectiles = ProjectileContainer.getChargedProjectiles(stack);
		// 如果已装填且弹药不为空
		if (isCharged(stack) && projectiles.size() > 1) {
			MutableText text = (MutableText) tooltip.get(tooltip.size() - 1);
			tooltip.remove(text);
			tooltip.add(text.append(" x ").append(String.valueOf(ProjectileContainer.getChargedAmount(stack))));
		}
	}
	
	@Override
	protected boolean load(LivingEntity shooter, ItemStack crossbow) {
		// 如果实体为玩家且在创造模式
		boolean isPlayerAndInCreative = shooter instanceof PlayerEntity player && player.isCreative();
		// 获取弹药
		ItemStack projectile = shooter.getProjectileType(crossbow);
		// 如果没有弹药且在创造模式
		if (projectile.isEmpty() && isPlayerAndInCreative) {
			projectile = new ItemStack(defaultProjectile());
		}
		return load(shooter, crossbow, projectile, isPlayerAndInCreative, isPlayerAndInCreative);
	}
	
	@Override
	protected void shoot(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
		// 获取弹药
		ItemStack projectile = ProjectileContainer.popChargedProjectiles(stack);
		if (projectile.isEmpty()) return;
		// 如果实体为玩家且在创造模式
		boolean isPlayerAndInCreative = entity instanceof PlayerEntity player && player.isCreative();
		shootProjectile(world, entity, hand, stack, projectile, getSoundPitch(entity.getRandom().nextBoolean(), entity.getRandom()), isPlayerAndInCreative, speed, divergence, 0);
	}
	
	@Override
	protected void shootProjectile(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
		super.shootProjectile(world, shooter, hand, crossbow, projectile, soundPitch, creative, speed, divergence, simulated);
		postShot(world, shooter, crossbow);
	}
	
	@Override
	protected void postShot(World world, LivingEntity shooter, ItemStack stack) {
		if (shooter instanceof ServerPlayerEntity serverPlayerEntity) {
			if (!world.isClient) Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
			serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
		}
		if (shooter instanceof PlayerEntity player && shootingInterval() != 0) player.getItemCooldownManager().set(this, shootingInterval());
		if (shotState() || ProjectileContainer.getChargedAmount(stack) == 0) ShootExpansion.setShot(stack);
		renderShootingParticle(world, shooter);
	}
	
	protected boolean load(LivingEntity shooter, ItemStack crossbow, ItemStack projectile, boolean simulated, boolean creative) {
		// 如果没弹药
		if (projectile.isEmpty()) return false;
		// 如果不在创造模式且不是复制子弹则减少弹药
		if (!creative && !simulated) {
			putChargedProjectiles(crossbow, ImmutableList.of(projectile.split(1)));
			if (projectile.isEmpty() && shooter instanceof PlayerEntity player) player.getInventory().removeOne(projectile);
		} else {
			putChargedProjectiles(crossbow, ImmutableList.of(projectile.copy()));
		}
		return true;
	}
	
	/**
	 * 单颗发射物的最大使用刻数
	 *
	 * @return 最大使用刻数
	 */
	@Override
	public int maxUseTicks() {
		return super.maxUseTicks();
	}
}
