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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.registry.gen.data.criterion.SilkCriteria;

import java.util.List;

/**
 * <h2 style="color:FFC800">辅助弓的创建的数据直观和清晰</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class Bow extends BowItem implements SilkBowExtend {
	public Bow(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean isHubStretch() {
		return false;
	}
	
	/**
	 * 获取最大使用时间
	 */
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return getMaxUseTicks();
	}
	
	/**
	 * 添加注册弹药标准
	 */
	@Override
	protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
		ProjectileEntity projectile = super.createArrowEntity(world, shooter, weaponStack, projectileStack, critical);
		if (shooter instanceof ServerPlayerEntity serverPlayer) SilkCriteria.SHOT_PROJECTILE_CRITERION.trigger(serverPlayer, weaponStack, projectile, 1);
		return projectile;
	}
	
	/**
	 * <p>设置自定义的拉弓速度</p>
	 * 重实现 {@link BowItem#onStoppedUsing(ItemStack, World, LivingEntity, int)}
	 */
	public void onStoppedUsing(ItemStack usingStack, World world, LivingEntity user, int remainingUseTicks) {
		// 如果使用物品的实体不是玩家，直接返回
		if (!(user instanceof PlayerEntity playerEntity)) return;
		
		// 获取玩家使用的射击类型物品
		ItemStack projectileStack = playerEntity.getProjectileType(usingStack);
		// 如果射击类型物品为空，直接返回
		if (projectileStack.isEmpty()) return;
		
		// 计算已使用时间
		int elapsedTime = getMaxUseTime(usingStack) - remainingUseTicks;
		// 计算拉弓的程度
		float pullProgress = getUsingProgress(elapsedTime, usingStack);
		// 如果拉弓程度小于0.1，直接返回
		if (pullProgress < 0.1) return;
		
		// 载入射击物品的列表
		List<ItemStack> projectileList = load(usingStack, projectileStack, playerEntity);
		// 如果是客户端或者列表为空，直接返回
		if (!world.isClient() && !projectileList.isEmpty()) {
			// 射出所有已装填的射击物品
			shootAll(world, playerEntity, playerEntity.getActiveHand(), usingStack, projectileList, pullProgress, 1, pullProgress == 1, null);
		}
		// 播放射击音效
		world.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1, 1 / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
		// 增加统计数据
		playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
	}
	
	/**
	 * <p>设置自定义的速度、伤害与弹药偏移</p>
	 * 重实现 {@link BowItem#shoot(LivingEntity, ProjectileEntity, int, float, float, float, LivingEntity)}
	 */
	@Override
	protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
		// 设置速度速度
		projectile.setVelocity(shooter, shooter.getPitch(), shooter.getYaw() + yaw, 0, speed * getMaxProjectileSpeed(), getFiringError());
		if (!(projectile instanceof PersistentProjectileEntity persistentProjectile)) return;
		// 设置基础伤害增加
		persistentProjectile.setDamage(persistentProjectile.getDamage() * getDamageMultiple());
	}
}
