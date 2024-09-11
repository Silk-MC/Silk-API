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

package pers.saikel0rado1iu.silk.test.modplus;

/**
 * ItemOverwrite
 */

@SuppressWarnings("unused")
public interface ItemOverwrite {
/*	@MainRegistryOverwrite(
			pack = "pers.saikel0rado1iu.silk.test.modplus.mixin",
			registrar = Items.class,
			field = "TEST_ITEM1",
			registers = {
					@Register(
							method = "group",
							overwrite = "ITEM_GROUP",
							remap = true
					),
					@Register(
							method = "other",
							overwrite = "ITEM_CONSUMER"
					)})
	Item OVERWRITE = new BowItem(new Item.Settings());
	RegistryKey<?>[] ITEM_GROUP = {ItemGroups.BUILDING_BLOCKS};
	Consumer<Item> ITEM_CONSUMER = item -> SilkModPlus.getInstance().logger().warn("dog");*/
}
