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

package pers.saikel0rado1iu.silk.api.item.tool;

import net.minecraft.item.*;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">辅助工具的创建的数据直观和清晰</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface Tool extends ToolMaterial {
	/**
	 * 基础伤害
	 */
	@SilkApi
	int BASE_DAMAGE = 1;
	/**
	 * 基础攻速
	 */
	@SilkApi
	int BASE_SPEED = 4;
	
	@SilkApi
	float getMaterialDamage();
	
	@SilkApi
	default ShovelItem createShovel(float damage, Item.Settings settings) {
		return new ShovelItem(this, settings.attributeModifiers(ShovelItem.createAttributeModifiers(this, getDamage(damage), getSpeed(1))));
	}
	
	@SilkApi
	default PickaxeItem createPickaxe(float damage, Item.Settings settings) {
		return new PickaxeItem(this, settings.attributeModifiers(PickaxeItem.createAttributeModifiers(this, (int) getDamage(damage), getSpeed(1.2F))));
	}
	
	@SilkApi
	default AxeItem createAxe(float damage, float speed, Item.Settings settings) {
		return new AxeItem(this, settings.attributeModifiers(AxeItem.createAttributeModifiers(this, getDamage(damage), getSpeed(speed))));
	}
	
	@SilkApi
	default HoeItem createHoe(float speed, Item.Settings settings) {
		return new HoeItem(this, settings.attributeModifiers(HoeItem.createAttributeModifiers(this, (int) getDamage(1), getSpeed(speed))));
	}
	
	@SilkApi
	default SwordItem createSword(float damage, Item.Settings settings) {
		return new SwordItem(this, settings.attributeModifiers(SwordItem.createAttributeModifiers(this, (int) getDamage(damage), getSpeed(1.6F))));
	}
	
	@Override
	default float getAttackDamage() {
		return getMaterialDamage();
	}
	
	/**
	 * 获取伤害
	 *
	 * @param damage 攻击伤害
	 * @return 实际设置数据
	 */
	private float getDamage(float damage) {
		return damage - BASE_DAMAGE - getMaterialDamage();
	}
	
	/**
	 * 获取攻速
	 *
	 * @param speed 攻击速度
	 * @return 实际设置攻速
	 */
	private float getSpeed(float speed) {
		return speed - BASE_SPEED;
	}
}
