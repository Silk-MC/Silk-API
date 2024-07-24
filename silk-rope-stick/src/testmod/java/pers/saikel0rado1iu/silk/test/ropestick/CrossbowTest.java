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

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.ropestick.ranged.Crossbow;
import pers.saikel0rado1iu.silk.ropestick.tool.AdjustFovWhileUse;

import java.util.Optional;
import java.util.Set;

/**
 * Test {@link Crossbow}
 */
public final class CrossbowTest extends Crossbow {
	/**
	 * @param settings 物品设置
	 */
	public CrossbowTest(Settings settings) {
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
	public float getProjectileIndex(ItemStack stack, Optional<Item> projectile) {
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
}
