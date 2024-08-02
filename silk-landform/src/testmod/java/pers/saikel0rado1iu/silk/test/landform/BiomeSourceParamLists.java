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

package pers.saikel0rado1iu.silk.test.landform;

import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import pers.saikel0rado1iu.silk.api.generate.world.MultiNoiseBiomeSourceParameterListEntry;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * BiomeSourceParamLists
 */
public interface BiomeSourceParamLists extends MultiNoiseBiomeSourceParameterListEntry {
	/**
	 * INSTANCE
	 */
	BiomeSourceParamLists INSTANCE = new BiomeSourceParamLists() {
	};
	/**
	 * test
	 */
	RegistryKey<MultiNoiseBiomeSourceParameterList> TEST = MultiNoiseBiomeSourceParameterListEntry.of(MOD_PASS, "test");
	
	@Override
	default RegistryBuilder.BootstrapFunction<MultiNoiseBiomeSourceParameterList> bootstrap() {
		return registerable -> {
			RegistryEntryLookup<Biome> registryEntryLookup = registerable.getRegistryLookup(RegistryKeys.BIOME);
			registerable.register(TEST, new MultiNoiseBiomeSourceParameterList(BiomeSourceParamListPresets.TEST, registryEntryLookup));
		};
	}
}
