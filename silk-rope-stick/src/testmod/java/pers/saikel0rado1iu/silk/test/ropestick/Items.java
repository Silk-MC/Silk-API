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

import net.minecraft.item.*;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;
import pers.saikel0rado1iu.silk.spinningjenny.ItemRegistry;

/**
 * 物品
 */
@SuppressWarnings("unused")
public interface Items extends ItemRegistry {
	/**
	 * test_item
	 */
	BreakingShieldTest TEST_ITEM = ItemRegistry.registrar(new BreakingShieldTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_item");
	/**
	 * test_bolt_action_firearm
	 */
	BoltActionFirearmTest TEST_BOLT_ACTION_FIREARM = ItemRegistry.registrar(new BoltActionFirearmTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_bolt_action_firearm");
	/**
	 * test_bolt_action_repeating_firearm
	 */
	BoltActionRepeatingFirearmTest TEST_BOLT_ACTION_REPEATING_FIREARM = ItemRegistry.registrar(new BoltActionRepeatingFirearmTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_bolt_action_repeating_firearm");
	/**
	 * test_semi_automatic_firearm
	 */
	SemiAutomaticFirearmTest TEST_SEMI_AUTOMATIC_FIREARM = ItemRegistry.registrar(new SemiAutomaticFirearmTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_semi_automatic_firearm");
	/**
	 * test_fully_automatic_firearm
	 */
	FullyAutomaticFirearmTest TEST_FULLY_AUTOMATIC_FIREARM = ItemRegistry.registrar(new FullyAutomaticFirearmTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_fully_automatic_firearm");
	/**
	 * test_bow
	 */
	BowTest TEST_BOW = ItemRegistry.registrar(new BowTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_bow");
	/**
	 * test_crossbow
	 */
	CrossbowTest TEST_CROSSBOW = ItemRegistry.registrar(new CrossbowTest(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_crossbow");
	/**
	 * test_shovel
	 */
	ShovelItem TEST_SHOVEL = ItemRegistry.registrar(ToolTest.MATERIAL.createShovel(4, new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_shovel");
	/**
	 * test_pickaxe
	 */
	PickaxeItem TEST_PICKAXE = ItemRegistry.registrar(ToolTest.MATERIAL.createPickaxe(4, new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_pickaxe");
	/**
	 * test_axe
	 */
	AxeItem TEST_AXE = ItemRegistry.registrar(ToolTest.MATERIAL.createAxe(12, 1.2F, new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_axe");
	/**
	 * test_hoe
	 */
	HoeItem TEST_HOE = ItemRegistry.registrar(ToolTest.MATERIAL.createHoe(4, new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_hoe");
	/**
	 * test_sword
	 */
	SwordItem TEST_SWORD = ItemRegistry.registrar(ToolTest.MATERIAL.createSword(8, new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_sword");
	/**
	 * test_helmet
	 */
	ArmorItem TEST_HELMET = ItemRegistry.registrar(ArmorTest.MATERIAL.createHelmet(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_helmet");
	/**
	 * test_chestplate
	 */
	ArmorItem TEST_CHESTPLATE = ItemRegistry.registrar(ArmorTest.MATERIAL.createChestplate(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_chestplate");
	/**
	 * test_leggings
	 */
	ArmorItem TEST_LEGGINGS = ItemRegistry.registrar(ArmorTest.MATERIAL.createLeggings(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_leggings");
	/**
	 * test_boots
	 */
	ArmorItem TEST_BOOTS = ItemRegistry.registrar(ArmorTest.MATERIAL.createBoots(new Item.Settings()))
			.group(ItemGroupCreatorTest.TEST_ITEM_GROUP1, ItemGroupCreatorTest.TEST_ITEM_GROUP2)
			.register(SilkRopeStick.getInstance(), "test_boots");
	
}
