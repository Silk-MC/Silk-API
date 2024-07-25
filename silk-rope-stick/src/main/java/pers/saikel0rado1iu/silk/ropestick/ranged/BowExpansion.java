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

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.ropestick.tool.AdjustFovWhileUse;
import pers.saikel0rado1iu.silk.ropestick.tool.ModifyMoveWhileUse;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">弓拓展</h2>
 * 用于扩展弓特性的方法接口
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface BowExpansion extends RangedExpansion, ModifyMoveWhileUse, AdjustFovWhileUse {
	/**
	 * 弓最大发射物速度
	 */
	float BOW_MAX_PROJECTILE_SPEED = 3;
	/**
	 * 弓最大使用刻数
	 */
	int BOW_MAX_USE_TICKS = 72000;
	/**
	 * 弓最大拉弓时间
	 */
	int BOW_MAX_PULL_TICKS = 20;
	/**
	 * 弓的最大非暴击伤害
	 */
	float BOW_MAX_DAMAGE = 6;
	/**
	 * 弓的视场角缩放值
	 */
	float BOW_FOV_SCALING = 1.08F;
	
	/**
	 * 获取最大拉弓时间
	 *
	 * @return 最大拉弓时间
	 */
	default int maxPullTicks() {
		return BOW_MAX_PULL_TICKS;
	}
	
	@Override
	default float maxProjectileSpeed() {
		return BOW_MAX_PROJECTILE_SPEED;
	}
	
	@Override
	default float maxDamage() {
		return BOW_MAX_DAMAGE;
	}
	
	@Override
	default float firingError() {
		return DEFAULT_FIRING_ERROR;
	}
	
	@Override
	default int maxUseTicks() {
		return BOW_MAX_USE_TICKS;
	}
	
	@Override
	default float adjustedProjectileDamage() {
		return maxDamage() / maxProjectileSpeed();
	}
	
	@Override
	default float getUsingProgress(int useTicks, ItemStack stack) {
		float progress = (float) useTicks / maxPullTicks();
		return Math.min(1, (progress * (progress + 2)) / 3);
	}
	
	@Override
	default float fovScaling() {
		return BOW_FOV_SCALING;
	}
	
	@Override
	default float moveSpeedMultiple() {
		return DEFAULT_SPEED_MULTIPLE;
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
