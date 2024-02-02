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

package pers.saikel0rado1iu.silk.api.item.armor;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.EnumMap;
import java.util.List;

/**
 * <h2 style="color:FFC800">辅助盔甲的创建的数据直观和清晰</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface Armor {
	/**
	 * 击退抗性比例
	 */
	@SilkApi
	int KR_RATIO = 10;
	
	@SilkApi
	@NotNull String getId();
	
	@SilkApi
	int getDurability();
	
	@SilkApi
	int[] getProtection();
	
	@SilkApi
	float getKnockBackResistance();
	
	int getEnchantability();
	
	RegistryEntry<SoundEvent> getEquipSound();
	
	Ingredient getRepairIngredient();
	
	float getToughness();
	
	/**
	 * 获取装备的纹理层
	 *
	 * @return 默认的单个纹理层
	 */
	default List<ArmorMaterial.Layer> getLayers() {
		return List.of(new ArmorMaterial.Layer(new Identifier(getId())));
	}
	
	default RegistryEntry<ArmorMaterial> register() {
		return Registry.registerReference(Registries.ARMOR_MATERIAL,
				new Identifier(getId()),
				new ArmorMaterial(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
					map.put(ArmorItem.Type.HELMET, getProtection(ArmorItem.Type.HELMET));
					map.put(ArmorItem.Type.CHESTPLATE, getProtection(ArmorItem.Type.CHESTPLATE));
					map.put(ArmorItem.Type.LEGGINGS, getProtection(ArmorItem.Type.LEGGINGS));
					map.put(ArmorItem.Type.BOOTS, getProtection(ArmorItem.Type.BOOTS));
					map.put(ArmorItem.Type.BODY, getProtection(ArmorItem.Type.BODY));
				}), getEnchantability(),
						getEquipSound(),
						this::getRepairIngredient,
						getLayers(),
						getToughness(),
						getKnockBackResistance(getKnockBackResistance())));
	}
	
	@SilkApi
	default ArmorItem createHelmet(Item.Settings settings) {
		return new ArmorItem(register(), ArmorItem.Type.HELMET, settings.maxDamageIfAbsent(ArmorItem.Type.HELMET.getMaxDamage(getDurability())));
	}
	
	@SilkApi
	default ArmorItem createChestplate(Item.Settings settings) {
		return new ArmorItem(register(), ArmorItem.Type.CHESTPLATE, settings.maxDamageIfAbsent(ArmorItem.Type.CHESTPLATE.getMaxDamage(getDurability())));
	}
	
	@SilkApi
	default ArmorItem createLeggings(Item.Settings settings) {
		return new ArmorItem(register(), ArmorItem.Type.LEGGINGS, settings.maxDamageIfAbsent(ArmorItem.Type.LEGGINGS.getMaxDamage(getDurability())));
	}
	
	@SilkApi
	default ArmorItem createBoots(Item.Settings settings) {
		return new ArmorItem(register(), ArmorItem.Type.BOOTS, settings.maxDamageIfAbsent(ArmorItem.Type.BOOTS.getMaxDamage(getDurability())));
	}
	
	@SilkApi
	default ArmorItem createBody(Item.Settings settings) {
		return new ArmorItem(register(), ArmorItem.Type.BODY, settings.maxDamageIfAbsent(ArmorItem.Type.BODY.getMaxDamage(getDurability())));
	}
	
	/**
	 * 获取击退抗性
	 *
	 * @param kr 击退抗性
	 * @return 击退抗性
	 */
	private float getKnockBackResistance(float kr) {
		return kr / KR_RATIO;
	}
	
	/**
	 * 获取装备护甲值
	 *
	 * @param type 装备槽
	 * @return 实际护甲值
	 */
	private int getProtection(ArmorItem.Type type) {
		int[] protections = getProtection();
		if (protections.length == 4) protections = new int[]{
				protections[0], protections[1], protections[2], protections[3], protections[1]};
		return switch (type) {
			case HELMET -> protections[0];
			case CHESTPLATE -> protections[1];
			case LEGGINGS -> protections[2];
			case BOOTS -> protections[3];
			case BODY -> protections[4];
		};
	}
}
