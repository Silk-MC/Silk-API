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

package pers.saikel0rado1iu.silk.test.spinningjenny.world.gen;

import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.world.gen.TrunkPlacerTypeRegistry;

/**
 * Test {@link TrunkPlacerTypeRegistry}
 */
public interface TrunkPlacerTypeRegistryTest extends TrunkPlacerTypeRegistry {
	/**
	 * test_trunk_placer_type
	 */
	@SuppressWarnings("unused")
	TrunkPlacerType<TrunkPlacer> TEST_TRUNK_PLACER_TYPE = TrunkPlacerTypeRegistry.registrar(new TrunkPlacerType<>(TrunkPlacer.TYPE_CODEC))
			.register(SilkSpinningJenny.getInstance(), "test_trunk_placer_type");
}
