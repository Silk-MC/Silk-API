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

package pers.saikel0rado1iu.silk.api.ropestick.ranged;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">类弓物品</h2>
 * 加强方法的 {@link BowLikeItem}，辅助弓的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.2
 */
public abstract class BowLikeItem extends BowItem implements BowExpansion {
	protected ItemStack tempStack;
	
	/**
	 * @param settings 物品设置
	 */
	public BowLikeItem(Settings settings) {
		super(settings);
	}
	
	/**
	 * 设置自定义的速度、伤害与弹药偏移<br>
	 * 重实现 {@link BowItem#onStoppedUsing(ItemStack, World, LivingEntity, int)}
	 */
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// 检查用户使用者是否为玩家
		if (!(user instanceof PlayerEntity player)) return;
		// 获取弹丸
		ItemStack projectile = player.getProjectileType(stack);
		if (projectile.isEmpty()) return;
		// 获取弓已使用游戏刻
		int usedTicks = getMaxUseTime(stack) - remainingUseTicks;
		// 获取弓拉弓进度
		float progress = getUsingProgress(usedTicks, stack);
		// 如果拉弓进度小于 0.1
		if (progress < 0.1) return;
		List<ItemStack> list = load(stack, projectile, player);
		if (!world.isClient() && !list.isEmpty()) {
			// 设置箭矢速度与设计误差
			shootAll(world, player, player.getActiveHand(), tempStack = stack, list, progress * maxProjectileSpeed(), firingError(), progress == 1, null);
		}
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1, 1 / (world.getRandom().nextFloat() * 0.4F + 1.2F) + progress * 0.5F);
		player.incrementStat(Stats.USED.getOrCreateStat(this));
	}
	
	@Override
	protected void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
		super.shootAll(world, shooter, hand, tempStack = stack, projectiles, speed, divergence, critical, target);
	}
	
	@Override
	protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		if (projectile instanceof PersistentProjectileEntity persistentProjectile) {
			persistentProjectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0, speed, divergence);
			// 设置基础伤害增加
			persistentProjectile.setDamage(adjustedProjectileDamage());
		} else {
			super.shoot(shooter, projectile, index, speed, divergence, yaw, target);
		}
		if (shooter instanceof ServerPlayerEntity serverPlayer) triggerCriteria(serverPlayer, tempStack, projectile);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		setProjectileIndex(stack, getProjectileType(user, stack));
		return super.use(world, user, hand);
	}
	
	@Override
	public Predicate<ItemStack> getProjectiles() {
		return stack -> launchableProjectiles().stream().anyMatch(stack::isOf);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return maxUseTicks();
	}
	
	@Override
	public boolean canStretchHud() {
		return false;
	}
}
