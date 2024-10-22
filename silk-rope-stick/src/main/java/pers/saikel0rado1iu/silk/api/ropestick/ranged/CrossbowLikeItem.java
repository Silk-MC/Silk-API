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

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">类弩物品</h2>
 * 加强方法的弩物品，辅助弩的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.2
 */
public abstract class CrossbowLikeItem extends CrossbowItem implements CrossbowExpansion {
	protected ItemStack tempStack;
	
	/**
	 * @param settings 物品设置
	 */
	public CrossbowLikeItem(Settings settings) {
		super(settings);
	}
	
	@Override
	protected void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
		super.shootAll(world, shooter, hand, tempStack = stack, projectiles, speed, divergence, critical, target);
		postShot(world, shooter, stack);
	}
	
	/**
	 * 设置自定义伤害
	 *
	 * @param shooter    射击实体
	 * @param projectile 射击发射物
	 * @param index      箭矢索引
	 * @param speed      出弹速度
	 * @param divergence 弹道散布
	 * @param yaw        箭矢偏转
	 * @param target     可选目标
	 */
	@Override
	protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		if (projectile instanceof PersistentProjectileEntity persistentProjectile) persistentProjectile.setDamage(adjustedProjectileDamage());
		Vector3f vector3f;
		if (target != null) {
			double d = target.getX() - shooter.getX();
			double e = target.getZ() - shooter.getZ();
			double f = Math.sqrt(d * d + e * e);
			double g = target.getBodyY(1.0 / 3.0) - projectile.getY() + f * 0.2;
			vector3f = calcVelocity(shooter, new Vec3d(d, g, e), yaw);
		} else {
			Vec3d oppositeRotationVector = shooter.getOppositeRotationVector(1);
			Quaternionf angleAxis = new Quaternionf().setAngleAxis(yaw * MathHelper.RADIANS_PER_DEGREE, oppositeRotationVector.x, oppositeRotationVector.y, oppositeRotationVector.z);
			Vec3d rotationVec = shooter.getRotationVec(1);
			vector3f = rotationVec.toVector3f().rotate(angleAxis);
		}
		projectile.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), speed, divergence);
		float soundPitch = getSoundPitch(shooter.getRandom(), index);
		shooter.getWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), shootSound(), shooter.getSoundCategory(), 1, soundPitch);
		if (shooter instanceof ServerPlayerEntity serverPlayer) triggerCriteria(serverPlayer, tempStack, projectile);
	}
	
	/**
	 * 发射后操作
	 *
	 * @param world   存档世界
	 * @param shooter 射击者
	 * @param stack   射击物品
	 */
	protected void postShot(World world, LivingEntity shooter, ItemStack stack) {
	}
	
	/**
	 * 装填发射物方法
	 *
	 * @param shooter  发射者
	 * @param crossbow 需要装填的弩物品
	 * @return 是否成功装填
	 */
	protected boolean load(LivingEntity shooter, ItemStack crossbow) {
		List<ItemStack> list = loadProjectile(crossbow, getProjectileType(shooter, crossbow), shooter);
		if (list.isEmpty()) return false;
		crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
		return true;
	}
	
	/**
	 * 装填发射物方法
	 *
	 * @param weapon     武器
	 * @param projectile 发射物
	 * @param shooter    射击者
	 * @return 发射物列表
	 */
	protected List<ItemStack> loadProjectile(ItemStack weapon, ItemStack projectile, LivingEntity shooter) {
		return load(weapon, projectile, shooter);
	}
	
	/**
	 * 设置上弹结束音效
	 */
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// 获取已使用游戏刻
		int usedTicks = getMaxUseTime(stack) - remainingUseTicks;
		// 获取张弩进度
		float progress = getUsingProgress(usedTicks, stack);
		// 如果张弩进度 ≥ 1 且未装填并装填所有弹药
		if (progress >= 1 && !isCharged(stack) && load(user, stack)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), loadedSound(), user.getSoundCategory(), 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
		}
	}
	
	/**
	 * 设置自定义的弹药速度与射击误差
	 */
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		ChargedProjectilesComponent component = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
		// 如果已装填
		if (component != null && !component.isEmpty()) {
			// 发射所有
			shootAll(world, user, hand, tempStack = stack, getMaxProjectileSpeed(stack), firingError(), null);
			return TypedActionResult.consume(stack);
		} else if (!getProjectileType(user, stack).isEmpty()) {
			charged = false;
			loaded = false;
			// 虽然方法名称为设置当前手, 但实际上这个方法是表明此物品可以进入使用状态
			user.setCurrentHand(hand);
			return TypedActionResult.consume(stack);
		} else {
			// 如果未装填
			return TypedActionResult.fail(stack);
		}
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
