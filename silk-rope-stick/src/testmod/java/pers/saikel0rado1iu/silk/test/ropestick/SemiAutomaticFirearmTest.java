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

package pers.saikel0rado1iu.silk.test.ropestick;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.common.util.TickUtil;
import pers.saikel0rado1iu.silk.ropestick.ranged.SemiAutomaticFirearm;
import pers.saikel0rado1iu.silk.ropestick.tool.AdjustFovWhileUse;

import java.util.Optional;
import java.util.Set;

/**
 * Test {@link SemiAutomaticFirearm}
 */
public final class SemiAutomaticFirearmTest extends SemiAutomaticFirearm {
	/**
	 * @param settings 物品设置
	 */
	public SemiAutomaticFirearmTest(Settings settings) {
		super(settings);
	}
	
	/**
	 * 触发进度条件
	 *
	 * @param serverPlayer 服务端玩家
	 * @param ranged       远程武器物品堆栈
	 * @param projectile   发射物
	 */
	@Override
	public void triggerCriteria(ServerPlayerEntity serverPlayer, ItemStack ranged, ProjectileEntity projectile) {
	}
	
	@Override
	public int maxUseTicks() {
		return TickUtil.getTick(1);
	}
	
	@Override
	public float maxDamage() {
		return 50;
	}
	
	@Override
	public float fovScaling() {
		return 0.8F;
	}
	
	@Override
	public Optional<Identifier> hudOverlay() {
		return Optional.of(AdjustFovWhileUse.POWDER_SNOW_OUTLINE);
	}
	
	@Override
	public boolean canStretchHud() {
		return super.canStretchHud();
	}
	
	@Override
	public float moveSpeedMultiple() {
		return 10;
	}
	
	/**
	 * 设置发射物索引以供 JSON 渲染使用
	 *
	 * @param stack         物品堆栈
	 * @param useProjectile 使用的发射物
	 */
	@Override
	public void setProjectileIndex(ItemStack stack, ItemStack useProjectile) {
	}
	
	/**
	 * 获取发射物索引以供 JSON 渲染使用
	 *
	 * @param stack 物品堆栈
	 * @return 索引
	 */
	@Override
	public float getProjectileIndex(ItemStack stack) {
		return 0;
	}
	
	/**
	 * 获取发射物索引以供 JSON 渲染使用
	 *
	 * @param projectile 发射物
	 * @return 索引
	 */
	@Override
	public float getProjectileIndex(Item projectile) {
		return 0;
	}
	
	/**
	 * 默认的发射物
	 *
	 * @return 发射物物品堆栈
	 */
	@Override
	public Item defaultProjectile() {
		return Items.ARROW;
	}
	
	/**
	 * 获取远程武器能发射的所有的发射物
	 *
	 * @return 发射物集合
	 */
	@Override
	public Set<Item> launchableProjectiles() {
		return Set.of();
	}
	
	/**
	 * 最大发射物容量
	 *
	 * @return 最大发射物容量
	 */
	@Override
	public int maxCapacity(ItemStack stack) {
		return 10;
	}
	
	/**
	 * 设置已射击状态
	 *
	 * @return 如果为 {@code true} 则每个发射物发射后都会设置已发射状态<br>
	 * 如果为 {@code false} 则只有当所有发射物都被发射后才会设置已发射状态
	 */
	@Override
	public boolean shotState() {
		return false;
	}
	
	/**
	 * 设置射击间隔
	 *
	 * @return 射击间隔，单位为游戏刻
	 */
	@Override
	public int shootingInterval() {
		return TickUtil.getTick(0.25F);
	}
	
	/**
	 * 渲染射击时的粒子效果
	 *
	 * @param world   存档世界
	 * @param shooter 射击者
	 */
	@Override
	public void renderShootingParticle(World world, LivingEntity shooter) {
		world.addParticle(ParticleTypes.EXPLOSION, shooter.getBlockX(), shooter.getBlockY(), shooter.getBlockZ(), 0, 0, 0);
	}
}
