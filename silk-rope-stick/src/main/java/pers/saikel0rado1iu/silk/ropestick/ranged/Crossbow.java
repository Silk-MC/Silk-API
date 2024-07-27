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

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pers.saikel0rado1iu.silk.common.util.MathUtil;

import java.util.List;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">弩</h2>
 * 加强方法的弩物品，辅助弩的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class Crossbow extends CrossbowItem implements CrossbowExpansion {
	/**
	 * @param settings 物品设置
	 */
	public Crossbow(Settings settings) {
		super(settings);
	}
	
	/**
	 * 设置自定义伤害
	 *
	 * @param world      存档世界
	 * @param shooter    射击实体
	 * @param hand       射击的主手还是副手
	 * @param crossbow   射击的弩
	 * @param projectile 射击发射物
	 * @param soundPitch 声音音高
	 * @param creative   是否为创造模式
	 * @param speed      出弹速度
	 * @param divergence 弹道散布
	 * @param simulated  弹道偏转
	 */
	protected void shootProjectile(World world, LivingEntity shooter, Hand hand, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean creative, float speed, float divergence, float simulated) {
		// 在服务端操作
		if (world.isClient) return;
		ProjectileEntity projectileEntity;
		// 如果弹药是烟花火箭
		boolean projectileIsFireworkRocket = projectile.isOf(Items.FIREWORK_ROCKET);
		// 构建弹丸实体
		if (projectileIsFireworkRocket) {
			projectileEntity = new FireworkRocketEntity(world, projectile, shooter, shooter.getX(), shooter.getEyeY() - 0.15, shooter.getZ(), true);
		} else {
			projectileEntity = createArrow(world, shooter, crossbow, projectile);
			// 设置基础伤害增加
			((PersistentProjectileEntity) projectileEntity).setDamage(adjustedProjectileDamage());
			// 如果在创造模式或不是'多重射击'弹药则设置为仅创造模式可拾起
			if (creative || MathUtil.compareFloat(simulated, 0) != 0) {
				((PersistentProjectileEntity) projectileEntity).pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
			}
		}
		// 如果使用者是弩使用者
		if (shooter instanceof CrossbowUser crossbowUser) {
			// 直接由弩使用者发射
			crossbowUser.shoot(crossbowUser.getTarget(), crossbow, projectileEntity, simulated);
		} else {
			// 设置弹药速度
			Vec3d vec3d = shooter.getOppositeRotationVector(1);
			Quaternionf quaternionf = new Quaternionf().setAngleAxis(simulated * ((float) Math.PI / 180), vec3d.x, vec3d.y, vec3d.z);
			Vec3d vec3d2 = shooter.getRotationVec(1);
			Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
			projectileEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
		}
		// 设置弩损伤
		crossbow.damage(projectileIsFireworkRocket ? 3 : 1, shooter, e -> e.sendToolBreakStatus(hand));
		// 生成弹药
		world.spawnEntity(projectileEntity);
		// 播放音效
		world.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), shootSound(), SoundCategory.PLAYERS, 1, soundPitch);
		if (shooter instanceof ServerPlayerEntity serverPlayer) triggerCriteria(serverPlayer, crossbow, projectileEntity);
	}
	
	/**
	 * 重新实现射击所有发射物以供使用
	 *
	 * @param world      存档世界
	 * @param entity     射击实体
	 * @param hand       射击的主手还是副手
	 * @param stack      射击的弩
	 * @param speed      出弹速度
	 * @param divergence 弹道散布
	 */
	protected void shoot(World world, LivingEntity entity, Hand hand, ItemStack stack, float speed, float divergence) {
		// 获取弹药
		List<ItemStack> projectiles = getProjectiles(stack);
		// 获取所有音高
		float[] soundPitches = getSoundPitches(entity.getRandom());
		
		// 发射所有弹药
		for (int count = 0; count < projectiles.size(); ++count) {
			ItemStack itemStack = projectiles.get(count);
			// 如果实体为玩家且在创造模式
			boolean isPlayerAndInCreative = entity instanceof PlayerEntity player && player.getAbilities().creativeMode;
			if (itemStack.isEmpty()) continue;
			// 设置“多重射击”的不同角度弹药发射
			switch (count) {
				case 0 -> shootProjectile(world, entity, hand, stack, itemStack, soundPitches[count], isPlayerAndInCreative, speed, divergence, 0);
				case 1 -> shootProjectile(world, entity, hand, stack, itemStack, soundPitches[count], isPlayerAndInCreative, speed, divergence, -10);
				case 2 -> shootProjectile(world, entity, hand, stack, itemStack, soundPitches[count], isPlayerAndInCreative, speed, divergence, 10);
			}
		}
		// 射击后操作
		postShot(world, entity, stack);
	}
	
	/**
	 * 发射后操作
	 *
	 * @param world   存档世界
	 * @param shooter 射击者
	 * @param stack   射击物品
	 */
	protected void postShot(World world, LivingEntity shooter, ItemStack stack) {
		postShoot(world, shooter, stack);
	}
	
	/**
	 * 装填发射物方法
	 *
	 * @param shooter  发射者
	 * @param crossbow 需要装填的弩物品
	 * @return 是否成功装填
	 */
	protected boolean load(LivingEntity shooter, ItemStack crossbow) {
		// 获取弹药数
		int projectilesNum = EnchantmentHelper.getLevel(Enchantments.MULTISHOT, crossbow) == 0 ? 1 : 3;
		// 如果实体为玩家且在创造模式
		boolean isPlayerAndInCreative = shooter instanceof PlayerEntity player && player.getAbilities().creativeMode;
		// 获取弹药
		ItemStack projectile = getProjectileType(shooter, crossbow);
		ItemStack projectileCopy = projectile.copy();
		for (int count = 0; count < projectilesNum; ++count) {
			// 如果有多重射击则多重射击弹药复制主弹药
			if (count > 0) projectile = projectileCopy.copy();
			// 如果没有弹药且在创造模式
			if (projectile.isEmpty() && isPlayerAndInCreative) {
				projectile = new ItemStack(defaultProjectile());
				projectileCopy = projectile.copy();
			}
			// 如果无法装填弹药
			if (!loadProjectile(shooter, crossbow, projectile, count > 0, isPlayerAndInCreative)) return false;
		}
		return true;
	}
	
	/**
	 * 设置上弹结束音效
	 */
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// 获取已使用游戏刻
		int usedTicks = getMaxUseTime(stack) - remainingUseTicks;
		// 获取张弩进度
		float pullProgress = getUsingProgress(usedTicks, stack);
		// 如果张弩进度 ≥ 1 且未装填并装填所有弹药
		if (pullProgress >= 1 && !isCharged(stack) && load(user, stack)) {
			// 设置已装填
			setCharged(stack, true);
			// 获取声音类别
			SoundCategory soundCategory = user instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
			// 播放弩装填结束音效
			world.playSound(null, user.getX(), user.getY(), user.getZ(), loadedSound(), soundCategory, 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
		}
	}
	
	/**
	 * 设置自定义的弹药速度与射击误差
	 */
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		setProjectileIndex(stack, getProjectileType(user, stack));
		// 如果已装填
		if (isCharged(stack)) {
			// 发射所有
			shoot(world, user, hand, stack, getMaxProjectileSpeed(stack), firingError());
			// 设置未装填
			setCharged(stack, false);
			return TypedActionResult.consume(stack);
		}
		// 如果使用者有弹药
		if (!getProjectileType(user, stack).isEmpty()) {
			charged = false;
			loaded = false;
			// 虽然方法名称为设置当前手, 但实际上这个方法是表明此物品可以进入使用状态
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		}
		// 如果未装填
		return TypedActionResult.fail(stack);
	}
	
	/**
	 * 设置装填音效和张弩进度
	 */
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient) return;
		// 设置“快速装填”音效
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		SoundEvent soundEvent = getQuickChargeSound(quickChargeLevel);
		SoundEvent soundEvent2 = quickChargeLevel == 0 ? loadingSound() : null;
		// 获取张弩进度
		float pullProgress = (float) (stack.getMaxUseTime() - remainingUseTicks) / getMaxPullTicks(stack);
		if (pullProgress < 0.2) {
			charged = false;
			loaded = false;
		} else if (pullProgress >= 0.2 && !charged) {
			charged = true;
			if (soundEvent != null) world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5F, 1);
		} else if (pullProgress >= 0.5 && soundEvent2 != null && !loaded) {
			loaded = true;
			world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5F, 1);
		}
	}
	
	/**
	 * 获取装填中音效
	 *
	 * @return 音效
	 */
	public SoundEvent loadingSound() {
		return SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE;
	}
	
	/**
	 * 获取装填结束音效
	 *
	 * @return 音效
	 */
	public SoundEvent loadedSound() {
		return SoundEvents.ITEM_CROSSBOW_LOADING_END;
	}
	
	/**
	 * 获取弩发射音效
	 *
	 * @return 音效
	 */
	public SoundEvent shootSound() {
		return SoundEvents.ITEM_CROSSBOW_SHOOT;
	}
	
	@Override
	public Predicate<ItemStack> getHeldProjectiles() {
		return stack -> launchableProjectiles().stream().anyMatch(stack::isOf);
	}
	
	@Override
	public Predicate<ItemStack> getProjectiles() {
		return stack -> launchableProjectiles().stream().anyMatch(stack::isOf);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getMaxPullTicks(stack);
	}
	
	@Override
	public int getEnchantability() {
		return 10;
	}
	
	@Override
	public boolean canStretchHud() {
		return false;
	}
	
	@Override
	public boolean canAdjustFov(ItemStack stack) {
		return isCharged(stack);
	}
	
	@Override
	public boolean canModifyMove(ItemStack stack) {
		return isCharged(stack);
	}
}
