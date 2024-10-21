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

import com.google.common.base.Suppliers;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.api.ropestick.armor.Armor;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Test {@link Armor}
 */
public enum ArmorTest implements Armor {
	MATERIAL("test", Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 2);
		map.put(ArmorItem.Type.LEGGINGS, 5);
		map.put(ArmorItem.Type.CHESTPLATE, 6);
		map.put(ArmorItem.Type.HELMET, 2);
		map.put(ArmorItem.Type.BODY, 5);
	}), 15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 5, 2, () -> Ingredient.ofItems(Items.TEST_ITEM));
	
	private final String name;
	private final Map<ArmorItem.Type, Integer> defense;
	private final int enchantability;
	private final RegistryEntry<SoundEvent> equipSound;
	private final float toughness;
	private final float knockbackResistance;
	private final Supplier<Ingredient> ingredient;
	private final Supplier<RegistryEntry<ArmorMaterial>> material;
	
	ArmorTest(String name, Map<ArmorItem.Type, Integer> defense, int enchantability, RegistryEntry<SoundEvent> equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> ingredient) {
		this.name = name;
		this.defense = defense;
		this.enchantability = enchantability;
		this.equipSound = equipSound;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.ingredient = Suppliers.memoize(ingredient::get);
		this.material = Suppliers.memoize(() -> Armor.registerMaterial(this));
	}
	
	@Override
	public Identifier id() {
		return SilkRopeStick.getInstance().ofId(name);
	}
	
	@Override
	public Map<ArmorItem.Type, Integer> defense() {
		return defense;
	}
	
	@Override
	public int enchantability() {
		return enchantability;
	}
	
	@Override
	public RegistryEntry<SoundEvent> equipSound() {
		return equipSound;
	}
	
	@Override
	public Supplier<Ingredient> repairIngredient() {
		return ingredient;
	}
	
	@Override
	public float toughness() {
		return toughness;
	}
	
	@Override
	public float knockbackResistance() {
		return knockbackResistance;
	}
	
	@Override
	public Supplier<RegistryEntry<ArmorMaterial>> material() {
		return material;
	}
}
