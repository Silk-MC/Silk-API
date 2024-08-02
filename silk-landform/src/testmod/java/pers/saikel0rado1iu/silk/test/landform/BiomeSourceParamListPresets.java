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

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import pers.saikel0rado1iu.silk.api.generate.world.biome.source.MultiNoiseBiomeSourceFunction;
import pers.saikel0rado1iu.silk.api.landform.biome.source.util.DefaultBiomeParameters;
import pers.saikel0rado1iu.silk.api.spinningjenny.world.biome.source.BiomeSourceParamListPresetRegistry;

import java.util.function.Function;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * BiomeSourceParamListPresets
 */
public interface BiomeSourceParamListPresets extends BiomeSourceParamListPresetRegistry {
	/**
	 * test
	 */
	MultiNoiseBiomeSourceParameterList.Preset TEST = BiomeSourceParamListPresetRegistry.registrar(new MultiNoiseBiomeSourceParameterList.Preset(MOD_PASS.ofId("test"), new MultiNoiseBiomeSourceFunction() {
		@Override
		public <T> MultiNoiseUtil.Entries<T> apply(Function<RegistryKey<Biome>, T> biomeEntryGetter) {
			ImmutableList.Builder<Pair<MultiNoiseUtil.NoiseHypercube, T>> builder = ImmutableList.builder();
			new DefaultBiomeParameters() {
			}.writeOverworldBiomeParameters(pair -> builder.add(pair.mapSecond(biomeEntryGetter)));
			return new MultiNoiseUtil.Entries<>(builder.build());
		}
	})).register(MOD_PASS, "test");
}
