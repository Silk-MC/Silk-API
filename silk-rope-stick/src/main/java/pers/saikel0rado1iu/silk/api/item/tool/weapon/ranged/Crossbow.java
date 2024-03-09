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

package pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged;

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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.SilkCriteria;

import java.util.List;

/**
 * <h2 style="color:FFC800">辅助弩的创建的数据直观和清晰</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class Crossbow extends CrossbowItem implements SilkCrossbowExtend {
	protected boolean charged = false;
	protected boolean loaded = false;
	
	public Crossbow(Settings settings) {
		super(settings);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#getSoundPitch(Random, int)}
	 */
	@SuppressWarnings("JavadocReference")
	protected static float getSoundPitch(Random random, int index) {
		if (index == 0) return 1;
		return getSoundPitch((index & 1) == 1, random);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#getSoundPitch(boolean, Random)}
	 */
	@SuppressWarnings("JavadocReference")
	protected static float getSoundPitch(boolean flag, Random random) {
		float pitch = flag ? 0.63F : 0.43F;
		return 1 / (random.nextFloat() * 0.5F + 1.8F) + pitch;
	}
	
	/**
	 * 重实现 {@link CrossbowItem#calcVelocity(LivingEntity, Vec3d, float)}
	 */
	@SuppressWarnings("JavadocReference")
	protected static Vector3f calcVelocity(LivingEntity shooter, Vec3d direction, float yaw) {
		Vector3f vector3f = direction.toVector3f().normalize();
		Vector3f vector3f2 = new Vector3f(vector3f).cross(new Vector3f(0, 1, 0));
		if ((double) vector3f2.lengthSquared() <= 1.0E-7) {
			Vec3d vec3d = shooter.getOppositeRotationVector(1);
			vector3f2 = new Vector3f(vector3f).cross(vec3d.toVector3f());
		}
		Vector3f vector3f3 = new Vector3f(vector3f).rotateAxis(1.5707964f, vector3f2.x, vector3f2.y, vector3f2.z);
		return new Vector3f(vector3f).rotateAxis(yaw * ((float) Math.PI / 180), vector3f3.x, vector3f3.y, vector3f3.z);
	}
	
	/**
	 * 重实现 {@link CrossbowItem#loadProjectiles(LivingEntity, ItemStack)}
	 */
	@SuppressWarnings("JavadocReference")
	protected boolean loadAllProjectile(LivingEntity shooter, ItemStack crossbow) {
		List<ItemStack> list = CrossbowItem.load(crossbow, shooter.getProjectileType(crossbow), shooter);
		if (list.isEmpty()) return false;
		crossbow.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
		return true;
	}
	
	@Override
	public boolean isHubStretch() {
		return false;
	}
	
	@Override
	public boolean canChangeFov(ItemStack stack) {
		return isCharged(stack);
	}
	
	@Override
	public boolean canChangeMovement(ItemStack stack) {
		return isCharged(stack);
	}
	
	@Override
	public int getEnchantability() {
		return 10;
	}
	
	/**
	 * <p>设置自定义伤害、速度和射击音效</p>
	 * 重实现 {@link CrossbowItem#shoot(LivingEntity, ProjectileEntity, int, float, float, float, LivingEntity)}
	 */
	@Override
	protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		// 设置基础伤害增加
		if (projectile instanceof PersistentProjectileEntity persistentPro) persistentPro.setDamage(persistentPro.getDamage() * getDamageMultiple());
		Vector3f directionVector;
		// 如果有目标
		if (target != null) {
			// 计算目标位置与射击者位置之间的距离
			double deltaX = target.getX() - shooter.getX();
			double deltaZ = target.getZ() - shooter.getZ();
			double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
			// 计算目标位置的高度
			double deltaY = target.getBodyY((double) 1 / 3) - projectile.getY() + distance * 0.2;
			// 计算射击方向的速度向量
			directionVector = calcVelocity(shooter, new Vec3d(deltaX, deltaY, deltaZ), yaw);
		} else {
			// 如果没有目标，计算射击方向的速度向量
			Vec3d rotationVectorOpposite = shooter.getOppositeRotationVector(1);
			Quaternionf rotationQuaternion = new Quaternionf().setAngleAxis(yaw * ((float) Math.PI / 180), rotationVectorOpposite.x, rotationVectorOpposite.y, rotationVectorOpposite.z);
			Vec3d rotationVector = shooter.getRotationVec(1);
			directionVector = rotationVector.toVector3f().rotate(rotationQuaternion);
		}
		// 设置弹药的速度和发散度
		projectile.setVelocity(directionVector.x(), directionVector.y(), directionVector.z(), speed, divergence);
		// 获取射击音效的音调
		float soundPitch = getSoundPitch(shooter.getRandom(), index);
		// 播放射击音效
		shooter.playSound(getShootSound(), 1, soundPitch);
	}
	
	/**
	 * 添加射击弹药标准
	 */
	@Override
	protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
		ProjectileEntity projectile = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
		if (shooter instanceof ServerPlayerEntity serverPlayer) SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, weaponStack, projectile, 1);
		return projectile;
	}
	
	/**
	 * 更改装填结束音效
	 */
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// 获取已使用游戏刻
		int usedTicks = getMaxUseTime(stack) - remainingUseTicks;
		// 获取张弩进度
		float pullProgress = getUsingProgress(usedTicks, stack);
		if (pullProgress >= 1 && !CrossbowItem.isCharged(stack) && loadAllProjectile(user, stack)) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), getLoadingEndSound(), user.getSoundCategory(), 1, 1 / (world.getRandom().nextFloat() * 0.5F + 1) + 0.2F);
		}
	}
	
	/**
	 * <p>设置自定义的弹药速度与射击误差</p>
	 * 重实现 {@link CrossbowItem#use(World, PlayerEntity, Hand)}
	 */
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		// 获取玩家手中的物品堆
		ItemStack itemStack = user.getStackInHand(hand);
		
		// 获取物品堆中的ChargedProjectilesComponent组件
		ChargedProjectilesComponent chargedProjectilesComponent = itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
		
		// 如果ChargedProjectilesComponent组件不为空且不为空的话
		if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
			// 发射所有已充能的弹药
			shootAll(world, user, hand, itemStack, getMaxProjectileSpeed(chargedProjectilesComponent), getFiringError(), null);
			// 返回表示消耗了物品堆的TypedActionResult对象
			return TypedActionResult.consume(itemStack);
		}
		
		// 如果玩家手中的物品堆不为空的话
		if (!user.getProjectileType(itemStack).isEmpty()) {
			// 重置交叉弓的充能状态和装填状态
			charged = false;
			loaded = false;
			// 设置玩家当前的手为当前手
			user.setCurrentHand(hand);
			return TypedActionResult.consume(itemStack);
		}
		
		return TypedActionResult.fail(itemStack);
	}
	
	
	/**
	 * <p>更改快速装填音效</p>
	 * 重实现 {@link CrossbowItem#usageTick(World, LivingEntity, ItemStack, int)}
	 */
	@Override
	public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
		if (world.isClient) return;
		// 设置“快速装填”音效
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		SoundEvent soundEvent = getQuickChargeSound(quickChargeLevel);
		SoundEvent soundEvent2 = quickChargeLevel == 0 ? getLoadingSound() : null;
		// 获取张弩进度
		float pullProgress = (float) (stack.getMaxUseTime() - remainingUseTicks) / getMaxPullTicks(stack);
		if (pullProgress < 0.2F) {
			charged = false;
			loaded = false;
		} else if (pullProgress >= 0.2F && !charged) {
			charged = true;
			if (soundEvent != null) world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent, SoundCategory.PLAYERS, 0.5F, 1);
		} else if (pullProgress >= 0.5F && soundEvent2 != null && !loaded) {
			loaded = true;
			world.playSound(null, user.getX(), user.getY(), user.getZ(), soundEvent2, SoundCategory.PLAYERS, 0.5F, 1);
		}
	}
	
	/**
	 * <p>因私有字段问题</p>
	 * 重实现 {@link CrossbowItem#getQuickChargeSound(int)}
	 */
	@SuppressWarnings("JavadocReference")
	protected SoundEvent getQuickChargeSound(int stage) {
		return switch (stage) {
			case 1 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_1;
			case 2 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_2;
			case 3 -> SoundEvents.ITEM_CROSSBOW_QUICK_CHARGE_3;
			default -> SoundEvents.ITEM_CROSSBOW_LOADING_START;
		};
	}
	
	/**
	 * 获取装填中音效
	 */
	protected SoundEvent getLoadingSound() {
		return SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE;
	}
	
	/**
	 * 获取装填结束音效
	 */
	protected SoundEvent getLoadingEndSound() {
		return SoundEvents.ITEM_CROSSBOW_LOADING_END;
	}
	
	/**
	 * 获取弩发射音效
	 */
	protected SoundEvent getShootSound() {
		return SoundEvents.ITEM_CROSSBOW_SHOOT;
	}
	
	/**
	 * 获取最大使用时间
	 */
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getMaxPullTicks(stack);
	}
}
