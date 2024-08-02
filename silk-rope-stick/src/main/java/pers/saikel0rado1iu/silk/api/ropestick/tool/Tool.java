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

package pers.saikel0rado1iu.silk.api.ropestick.tool;

import com.google.common.collect.ImmutableSet;
import net.minecraft.item.*;
import pers.saikel0rado1iu.silk.api.ropestick.property.ItemProperties;
import pers.saikel0rado1iu.silk.api.ropestick.property.ItemProperty;

import java.util.Set;

/**
 * <h2 style="color:FFC800">工具</h2>
 * 辅助工具的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface Tool extends ToolMaterial {
	/**
	 * 基础伤害
	 */
	int BASE_DAMAGE = 1;
	/**
	 * 基础攻速
	 */
	int BASE_SPEED = 4;
	
	/**
	 * 获取材料伤害，即为 {@link ToolMaterial} 中的 {@link ToolMaterial#getAttackDamage()}
	 *
	 * @return 材料伤害
	 */
	float getMaterialDamage();
	
	/**
	 * 创建锹
	 *
	 * @param damage     伤害
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 锹物品
	 */
	default ShovelItem createShovel(float damage, Item.Settings settings, ItemProperty... properties) {
		ImmutableSet<ItemProperty> immutableSet = ImmutableSet.copyOf(properties);
		class ShovelItemEx extends ShovelItem implements ItemProperties {
			public ShovelItemEx(ToolMaterial material) {
				super(material, getDamage(damage), getSpeed(1), settings);
			}
			
			@Override
			public Set<ItemProperty> properties() {
				return immutableSet;
			}
		}
		return new ShovelItemEx(this);
	}
	
	/**
	 * 创建镐
	 *
	 * @param damage     伤害
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 镐物品
	 */
	default PickaxeItem createPickaxe(float damage, Item.Settings settings, ItemProperty... properties) {
		ImmutableSet<ItemProperty> immutableSet = ImmutableSet.copyOf(properties);
		class PickaxeItemEx extends PickaxeItem implements ItemProperties {
			public PickaxeItemEx(ToolMaterial material) {
				super(material, (int) getDamage(damage), getSpeed(1.2F), settings);
			}
			
			@Override
			public Set<ItemProperty> properties() {
				return immutableSet;
			}
		}
		return new PickaxeItemEx(this);
	}
	
	/**
	 * 创建斧
	 *
	 * @param damage     伤害
	 * @param speed      攻击速度
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 斧物品
	 */
	default AxeItem createAxe(float damage, float speed, Item.Settings settings, ItemProperty... properties) {
		ImmutableSet<ItemProperty> immutableSet = ImmutableSet.copyOf(properties);
		class AxeItemEx extends AxeItem implements ItemProperties {
			public AxeItemEx(ToolMaterial material) {
				super(material, getDamage(damage), getSpeed(speed), settings);
			}
			
			@Override
			public Set<ItemProperty> properties() {
				return immutableSet;
			}
		}
		return new AxeItemEx(this);
	}
	
	/**
	 * 创建锄
	 *
	 * @param speed      攻击速度
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 锄物品
	 */
	default HoeItem createHoe(float speed, Item.Settings settings, ItemProperty... properties) {
		ImmutableSet<ItemProperty> immutableSet = ImmutableSet.copyOf(properties);
		class HoeItemEx extends HoeItem implements ItemProperties {
			public HoeItemEx(ToolMaterial material) {
				super(material, (int) getDamage(1), getSpeed(speed), settings);
			}
			
			@Override
			public Set<ItemProperty> properties() {
				return immutableSet;
			}
		}
		return new HoeItemEx(this);
	}
	
	/**
	 * 创建剑
	 *
	 * @param damage     伤害
	 * @param settings   物品设置
	 * @param properties 物品属性
	 * @return 剑物品
	 */
	default SwordItem createSword(float damage, Item.Settings settings, ItemProperty... properties) {
		ImmutableSet<ItemProperty> immutableSet = ImmutableSet.copyOf(properties);
		class SwordItemEx extends SwordItem implements ItemProperties {
			public SwordItemEx(ToolMaterial material) {
				super(material, (int) getDamage(damage), getSpeed(1.6F), settings);
			}
			
			@Override
			public Set<ItemProperty> properties() {
				return immutableSet;
			}
		}
		return new SwordItemEx(this);
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
