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

package pers.saikel0rado1iu.silk.ropestick.weapon;

import net.minecraft.entity.damage.DamageSource;

/**
 * <h2 style="color:FFC800">可破盾</h2>
 * 设置武器可以破盾
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface BreakingShield {
	/**
	 * 判断是否可破盾
	 *
	 * @param source 伤害源
	 * @return 是否可以判断
	 */
	boolean canBreaking(DamageSource source);
	
	/**
	 * 决定盾牌是否进入冷却状态
	 *
	 * @return 是否冷却
	 */
	boolean canCooling();
	
	/**
	 * 获取实体应受伤害
	 *
	 * @param originalDamage 原始伤害
	 * @return 实体应受伤害
	 */
	
	float entityDamage(float originalDamage);
	
	/**
	 * 获取盾牌应受损伤
	 *
	 * @param originalDamage 原始损伤
	 * @return 盾牌应受损伤
	 */
	float shieldDamage(float originalDamage);
}
