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

import com.google.common.collect.ImmutableSet;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import pers.saikel0rado1iu.silk.api.ropestick.property.CustomEnchantment;
import pers.saikel0rado1iu.silk.api.ropestick.property.EffectiveItemSlot;
import pers.saikel0rado1iu.silk.api.ropestick.property.InherentStatusEffect;
import pers.saikel0rado1iu.silk.api.ropestick.property.ItemProperty;
import pers.saikel0rado1iu.silk.api.ropestick.tool.Tool;

/**
 * Test {@link Tool}
 */
public interface ToolTest extends Tool {
	/**
	 * 材料
	 */
	ToolTest MATERIAL = new ToolTest() {
	};
	
	/**
	 * 物品属性集合
	 *
	 * @return 物品属性集合
	 */
	static ItemProperty[] properties() {
		return new ItemProperty[]{
				new CustomEnchantment(Enchantments.UNBREAKING, Enchantments.VANISHING_CURSE, Enchantments.MENDING),
				new InherentStatusEffect(
						new InherentStatusEffect.Property(StatusEffects.HEALTH_BOOST, 10, 5, 1,
								() -> ImmutableSet.of(Items.TEST_SHOVEL, Items.TEST_PICKAXE, Items.TEST_AXE, Items.TEST_HOE, Items.TEST_SWORD),
								1, EffectiveItemSlot.HAND))
		};
	}
	
	/**
	 * 创建锹
	 *
	 * @param damage   伤害
	 * @param settings 物品设置
	 * @return 锹物品
	 */
	default ShovelItem createShovel(float damage, Item.Settings settings) {
		return createShovel(damage, settings, properties());
	}
	
	/**
	 * 创建镐
	 *
	 * @param damage   伤害
	 * @param settings 物品设置
	 * @return 镐物品
	 */
	default PickaxeItem createPickaxe(float damage, Item.Settings settings) {
		return createPickaxe(damage, settings, properties());
	}
	
	/**
	 * 创建斧
	 *
	 * @param damage   伤害
	 * @param speed    攻击速度
	 * @param settings 物品设置
	 * @return 斧物品
	 */
	default AxeItem createAxe(float damage, float speed, Item.Settings settings) {
		return createAxe(damage, speed, settings, properties());
	}
	
	/**
	 * 创建锄
	 *
	 * @param speed    攻击速度
	 * @param settings 物品设置
	 * @return 锄物品
	 */
	default HoeItem createHoe(float speed, Item.Settings settings) {
		return createHoe(speed, settings, properties());
	}
	
	/**
	 * 创建剑
	 *
	 * @param damage   伤害
	 * @param settings 物品设置
	 * @return 剑物品
	 */
	default SwordItem createSword(float damage, Item.Settings settings) {
		return createSword(damage, settings, properties());
	}
	
	@Override
	default float getMaterialDamage() {
		return 2;
	}
	
	@Override
	default int getDurability() {
		return 15;
	}
	
	@Override
	default float getMiningSpeedMultiplier() {
		return 15;
	}
	
	@Override
	default int getMiningLevel() {
		return 3;
	}
	
	@Override
	default int getEnchantability() {
		return 15;
	}
	
	@Override
	default Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.TEST_ITEM);
	}
}
