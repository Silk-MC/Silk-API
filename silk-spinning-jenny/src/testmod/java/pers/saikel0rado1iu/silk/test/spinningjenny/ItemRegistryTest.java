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

package pers.saikel0rado1iu.silk.test.spinningjenny;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistry;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;

/**
 * Test {@link ItemRegistry}
 */
public interface ItemRegistryTest extends ItemRegistry {
	/**
	 * test_item
	 */
	Item TEST_ITEM = ItemRegistry.registrar(() -> new Item(new Item.Settings().component(DataComponentTypeRegistryTest.TEST_DATA_COMPONENT_TYPE, 99)))
			.group(ItemGroups.BUILDING_BLOCKS)
			.other(item -> SilkSpinningJenny.getInstance().logger().info("other"))
			.register();
	/**
	 * test_block
	 */
	@SuppressWarnings("unused")
	BlockItem TEST_BLOCK = ItemRegistry.registrar(() -> new BlockItem(BlockRegistryTest.TEST_BLOCK, new Item.Settings()))
			.group(ItemGroups.BUILDING_BLOCKS)
			.register(SilkSpinningJenny.getInstance().ofId("test_block"));
}
