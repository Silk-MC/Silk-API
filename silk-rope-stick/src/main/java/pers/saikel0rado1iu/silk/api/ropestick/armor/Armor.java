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

package pers.saikel0rado1iu.silk.api.ropestick.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">盔甲</h2>
 * 辅助盔甲的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface Armor {
	/**
	 * 击退抗性比例
	 */
	int KR_RATIO = 10;
	
	/**
	 * 注册盔甲材料
	 *
	 * @param armor 盔甲接口
	 * @return 盔甲材料注册项
	 */
	static RegistryEntry<ArmorMaterial> registerMaterial(Armor armor) {
		return RegistryEntry.of(Registry.register(
				Registries.ARMOR_MATERIAL,
				armor.id(),
				new ArmorMaterial(
						armor.defense(),
						armor.enchantability(),
						armor.equipSound(),
						armor.repairIngredient(),
						armor.layers(),
						armor.toughness(),
						armor.knockbackResistance() / KR_RATIO)));
	}
	
	/**
	 * 创建头盔
	 *
	 * @param settings 物品设置
	 * @return 头盔物品
	 */
	default ArmorItem createHelmet(Item.Settings settings) {
		return new ArmorItem(material().get(), ArmorItem.Type.HELMET, settings);
	}
	
	/**
	 * 创建胸甲
	 *
	 * @param settings 物品设置
	 * @return 头盔胸甲
	 */
	default ArmorItem createChestplate(Item.Settings settings) {
		return new ArmorItem(material().get(), ArmorItem.Type.CHESTPLATE, settings);
	}
	
	/**
	 * 创建护腿
	 *
	 * @param settings 物品设置
	 * @return 头盔护腿
	 */
	default ArmorItem createLeggings(Item.Settings settings) {
		return new ArmorItem(material().get(), ArmorItem.Type.LEGGINGS, settings);
	}
	
	/**
	 * 创建靴子
	 *
	 * @param settings 物品设置
	 * @return 头盔靴子
	 */
	default ArmorItem createBoots(Item.Settings settings) {
		return new ArmorItem(material().get(), ArmorItem.Type.BOOTS, settings);
	}
	
	/**
	 * 盔甲的唯一标识符
	 *
	 * @return 标识符
	 */
	Identifier id();
	
	/**
	 * 盔甲防御属性
	 *
	 * @return 盔甲防御图表
	 */
	Map<ArmorItem.Type, Integer> defense();
	
	/**
	 * 附魔能力属性
	 *
	 * @return 附魔能力
	 */
	int enchantability();
	
	/**
	 * 装备音效属性
	 *
	 * @return 装备音效注册项
	 */
	RegistryEntry<SoundEvent> equipSound();
	
	/**
	 * 修复成分属性
	 *
	 * @return 修复程序提供器
	 */
	Supplier<Ingredient> repairIngredient();
	
	/**
	 * 盔甲图层属性
	 *
	 * @return 盔甲图层列表
	 */
	default List<ArmorMaterial.Layer> layers() {
		return List.of(new ArmorMaterial.Layer(id()));
	}
	
	/**
	 * 韧性属性
	 *
	 * @return 盔甲韧性
	 */
	float toughness();
	
	/**
	 * 击退抗性属性
	 *
	 * @return 击退抗性
	 */
	float knockbackResistance();
	
	/**
	 * 盔甲材料注册项属性
	 *
	 * @return 盔甲材料注册项提供器
	 */
	Supplier<RegistryEntry<ArmorMaterial>> material();
}
