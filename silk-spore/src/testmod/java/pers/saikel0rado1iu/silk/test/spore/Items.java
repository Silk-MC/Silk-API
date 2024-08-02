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

package pers.saikel0rado1iu.silk.test.spore;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistry;
import pers.saikel0rado1iu.silk.impl.SilkSpore;

/**
 * Items
 */
public interface Items extends ItemRegistry {
	/**
	 * fixed_damage_test_item
	 */
	Item FIXED_DAMAGE_TEST_ITEM = ItemRegistry.registrar(new Item(new Item.Settings()))
			.group(ItemGroups.COMBAT).register(SilkSpore.getInstance().ofId("fixed_damage_test_item"));
}
