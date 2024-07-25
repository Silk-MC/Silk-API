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
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.ropestick.tool.AdjustFovWhileHold;
import pers.saikel0rado1iu.silk.ropestick.tool.ModifyMoveWhileHold;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">弩拓展</h2>
 * 用于扩展弩特性的方法接口
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface CrossbowExpansion extends RangedExpansion, ModifyMoveWhileHold, AdjustFovWhileHold {
	/**
	 * 用于模型谓词，已装填状态键
	 */
	String CHARGED_KEY = "charged";
	/**
	 * 弩最大使用刻数
	 */
	int CROSSBOW_MAX_USE_TICKS = 25;
	/**
	 * 弩最大发射物速度
	 */
	float CROSSBOW_MAX_PROJECTILE_SPEED = 3.15F;
	/**
	 * 弩的最大非暴击伤害
	 */
	float CROSSBOW_MAX_DAMAGE = 7;
	
	/**
	 * 获取使用进度
	 *
	 * @param useTicks 使用刻数
	 * @param stack    物品堆栈
	 * @return 使用进度
	 */
	default float getUsingProgress(int useTicks, ItemStack stack) {
		return Math.min(1, useTicks / (float) getMaxPullTicks(stack));
	}
	
	/**
	 * 获取最大拉弓刻数
	 *
	 * @param stack 物品堆栈
	 * @return 最大拉弓刻数
	 */
	default int getMaxPullTicks(ItemStack stack) {
		// 设置“快速装填”效果
		int quickChargeLevel = EnchantmentHelper.getLevel(Enchantments.QUICK_CHARGE, stack);
		return quickChargeLevel == 0 ? maxUseTicks() : maxUseTicks() - maxUseTicks() / 5 * quickChargeLevel;
	}
	
	/**
	 * 获取最大发射物速度
	 *
	 * @param stack 物品堆栈
	 * @return 最大发射物速度
	 */
	default float getMaxProjectileSpeed(ItemStack stack) {
		return CrossbowItem.hasProjectile(stack, Items.FIREWORK_ROCKET) ? maxProjectileSpeed() / 2 : maxProjectileSpeed();
	}
	
	@Override
	default float maxProjectileSpeed() {
		return CROSSBOW_MAX_PROJECTILE_SPEED;
	}
	
	@Override
	default float maxDamage() {
		return CROSSBOW_MAX_DAMAGE;
	}
	
	@Override
	default float firingError() {
		return DEFAULT_FIRING_ERROR;
	}
	
	@Override
	default int maxUseTicks() {
		return CROSSBOW_MAX_USE_TICKS;
	}
	
	@Override
	default float adjustedProjectileDamage() {
		return maxDamage() / maxProjectileSpeed();
	}
	
	@Override
	default float fovScaling() {
		return 1;
	}
	
	@Override
	default float moveSpeedMultiple() {
		return 1;
	}
	
	@Override
	default boolean isConflictItem(Item checkItem) {
		return checkItem instanceof ModifyMoveWhileHold;
	}
	
	@Override
	default Optional<Identifier> hudOverlay() {
		return Optional.empty();
	}
	
	@Override
	default boolean onlyFirstPerson() {
		return false;
	}
}

