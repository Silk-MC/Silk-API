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

package pers.saikel0rado1iu.silk.test.magiccube;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import pers.saikel0rado1iu.silk.impl.SilkMagicCube;
import pers.saikel0rado1iu.silk.spinningjenny.ItemRegistry;

/**
 * 物品
 */
@SuppressWarnings("unused")
public interface Items extends ItemRegistry {
	/**
	 * test_block
	 */
	BlockItem TEST_BLOCK = ItemRegistry.registrar(new BlockItem(Blocks.TEST_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkMagicCube.getInstance().ofId("test_block"));
	/**
	 * test_plantable_block
	 */
	BlockItem TEST_PLANTABLE_BLOCK = ItemRegistry.registrar(new BlockItem(Blocks.TEST_PLANTABLE_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkMagicCube.getInstance().ofId("test_plantable_block"));
	/**
	 * test_spreadable_soil_block
	 */
	BlockItem TEST_SPREADABLE_SOIL_BLOCK = ItemRegistry.registrar(new BlockItem(Blocks.TEST_SPREADABLE_SOIL_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkMagicCube.getInstance().ofId("test_spreadable_soil_block"));
	/**
	 * test_infect_sapling_block
	 */
	BlockItem TEST_INFECT_SAPLING_BLOCK = ItemRegistry.registrar(new BlockItem(Blocks.TEST_INFECT_SAPLING_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkMagicCube.getInstance().ofId("test_infect_sapling_block"));
	/**
	 * test_infect_mushroom_block
	 */
	BlockItem TEST_INFECT_MUSHROOM_BLOCK = ItemRegistry.registrar(new BlockItem(Blocks.TEST_INFECT_MUSHROOM_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkMagicCube.getInstance().ofId("test_infect_mushroom_block"));
	/**
	 * empty_pot_block
	 */
	BlockItem EMPTY_POT_BLOCK = ItemRegistry.registrar(new BlockItem(Blocks.EMPTY_POT_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkMagicCube.getInstance().ofId("empty_pot_block"));
}
