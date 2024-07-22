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
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;
import pers.saikel0rado1iu.silk.ropestick.armor.Armor;
import pers.saikel0rado1iu.silk.ropestick.armor.CustomDyeableArmorItem;
import pers.saikel0rado1iu.silk.ropestick.property.EffectiveItemSlot;
import pers.saikel0rado1iu.silk.ropestick.property.InherentStatusEffect;
import pers.saikel0rado1iu.silk.ropestick.property.ItemProperty;
import pers.saikel0rado1iu.silk.ropestick.property.PiglinIgnore;

/**
 * Test {@link Armor}
 */
public interface ArmorTest extends Armor {
	/**
	 * 材料
	 */
	ArmorTest MATERIAL = new ArmorTest() {
	};
	
	/**
	 * 物品属性集合
	 *
	 * @return 物品属性集合
	 */
	static ItemProperty[] properties() {
		return new ItemProperty[]{
				new PiglinIgnore(EffectiveItemSlot.ALL),
				new InherentStatusEffect(
						new InherentStatusEffect.Property(StatusEffects.LUCK, 0, 5, 1,
								() -> ImmutableSet.of(Items.TEST_HELMET, Items.TEST_CHESTPLATE, Items.TEST_LEGGINGS, Items.TEST_BOOTS),
								0, EffectiveItemSlot.ARMOR),
						new InherentStatusEffect.Property(StatusEffects.UNLUCK, 1, 2, 0.5F,
								() -> ImmutableSet.of(Items.TEST_HELMET, Items.TEST_CHESTPLATE, Items.TEST_LEGGINGS, Items.TEST_BOOTS),
								2, EffectiveItemSlot.ARMOR))
		};
	}
	
	/**
	 * 创建头盔
	 *
	 * @param settings 物品设置
	 * @return 头盔物品
	 */
	default ArmorItem createHelmet(Item.Settings settings) {
		return createHelmet(settings, properties());
	}
	
	/**
	 * 创建胸甲
	 *
	 * @param settings 物品设置
	 * @return 头盔胸甲
	 */
	default ArmorItem createChestplate(Item.Settings settings) {
		return createChestplate(settings, properties());
	}
	
	/**
	 * 创建护腿
	 *
	 * @param settings 物品设置
	 * @return 头盔护腿
	 */
	default ArmorItem createLeggings(Item.Settings settings) {
		createLeggings(settings, properties());
		return new CustomDyeableArmorItem(this, ArmorItem.Type.LEGGINGS, settings, 0xFFFF00);
	}
	
	/**
	 * 创建靴子
	 *
	 * @param settings 物品设置
	 * @return 头盔靴子
	 */
	default ArmorItem createBoots(Item.Settings settings) {
		return createBoots(settings, properties());
	}
	
	@Override
	default Identifier getId() {
		return SilkRopeStick.getInstance().ofId("test");
	}
	
	@Override
	default int getDurability() {
		return 15;
	}
	
	@Override
	default int[] getProtections() {
		return new int[]{4, 6, 5, 5};
	}
	
	@Override
	default float getKnockBackResistance() {
		return 2;
	}
	
	@Override
	default int getEnchantability() {
		return 15;
	}
	
	@Override
	default SoundEvent getEquipSound() {
		return SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
	}
	
	@Override
	default Ingredient getRepairIngredient() {
		return Ingredient.ofItems(Items.TEST_ITEM);
	}
	
	@Override
	default float getToughness() {
		return 5;
	}
}
