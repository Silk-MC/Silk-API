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

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;
import java.util.Set;

/**
 * <h2 style="color:FFC800">远程拓展</h2>
 * 用于扩展远程武器特性的方法接口
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface RangedExpansion {
	/**
	 * 用于模型谓词，拉弓中状态键
	 */
	String PULLING_KEY = "pulling";
	/**
	 * 用于模型谓词，拉弓状态键
	 */
	String PULL_KEY = "pull";
	/**
	 * 用于模型谓词，弹药状态键
	 */
	String PROJECTILE_INDEX_KEY = "projectile";
	/**
	 * 默认射击误差
	 */
	float DEFAULT_FIRING_ERROR = 1;
	
	/**
	 * 触发进度条件
	 *
	 * @param serverPlayer 服务端玩家
	 * @param ranged       远程武器物品堆栈
	 * @param projectile   发射物
	 */
	void triggerCriteria(ServerPlayerEntity serverPlayer, ItemStack ranged, ProjectileEntity projectile);
	
	/**
	 * 最大发射物速度
	 *
	 * @return 最大发射物速度
	 */
	float maxProjectileSpeed();
	
	/**
	 * 最大非暴击伤害
	 *
	 * @return 最大伤害
	 */
	float maxDamage();
	
	/**
	 * 最大使用刻数
	 *
	 * @return 最大使用刻数
	 */
	int maxUseTicks();
	
	/**
	 * 射击误差
	 *
	 * @return 射击误差
	 */
	float firingError();
	
	/**
	 * 修正的发射物基础伤害，使其能符合设置的最大伤害
	 *
	 * @return 修正的发射物伤害
	 */
	float adjustedProjectileDamage();
	
	/**
	 * 设置发射物索引以供 JSON 渲染使用
	 *
	 * @param stack         物品堆栈
	 * @param useProjectile 使用的发射物
	 */
	void setProjectileIndex(ItemStack stack, ItemStack useProjectile);
	
	/**
	 * 获取发射物索引以供 JSON 渲染使用<br>
	 * 如果 {@code projectile} 为 {@link Optional#empty()} 则需要通过 {@code stack} 中存储的发射物判断<br>
	 * 否则需要根据 {@code projectile} 去提供索引
	 *
	 * @param stack      物品堆栈
	 * @param projectile 可选的弹射物
	 * @return 索引
	 */
	float getProjectileIndex(ItemStack stack, Optional<Item> projectile);
	
	/**
	 * 获取发射物索引以供 JSON 渲染使用
	 *
	 * @param projectile 发射物
	 * @return 索引
	 */
	float getProjectileIndex(Item projectile);
	
	/**
	 * 默认的发射物
	 *
	 * @return 发射物物品堆栈
	 */
	Item defaultProjectile();
	
	/**
	 * 获取远程武器能发射的所有的发射物
	 *
	 * @return 发射物集合
	 */
	Set<Item> launchableProjectiles();
}
