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

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import pers.saikel0rado1iu.silk.ropestick.weapon.BreakingShield;

/**
 * Test {@link BreakingShield}
 */
public final class BreakingShieldTest extends Item implements BreakingShield {
	/**
	 * @param settings 物品设置
	 */
	public BreakingShieldTest(Settings settings) {
		super(settings);
	}
	
	/**
	 * 判断是否可破盾
	 *
	 * @param source 伤害源
	 * @return 是否可以判断
	 */
	@Override
	public boolean canBreaking(DamageSource source) {
		return true;
	}
	
	/**
	 * 决定盾牌是否进入冷却状态
	 *
	 * @return 是否冷却
	 */
	@Override
	public boolean canCooling() {
		return true;
	}
	
	/**
	 * 获取实体应受伤害
	 *
	 * @param originalDamage 原始伤害
	 * @return 实体应受伤害
	 */
	@Override
	public float entityDamage(float originalDamage) {
		return originalDamage / 2;
	}
	
	/**
	 * 获取盾牌应受损伤
	 *
	 * @param originalDamage 原始损伤
	 * @return 盾牌应受损伤
	 */
	@Override
	public float shieldDamage(float originalDamage) {
		return originalDamage / 2;
	}
}
