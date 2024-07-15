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

import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.densityfunction.DensityFunctions;
import net.minecraft.world.gen.noise.NoiseRouter;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;
import pers.saikel0rado1iu.silk.generate.world.ChunkGeneratorSettingEntry;
import pers.saikel0rado1iu.silk.generate.world.chunk.ChunkGenerationShapeConfig;
import pers.saikel0rado1iu.silk.landform.biome.source.util.DefaultBiomeParameters;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * ChunkGeneratorSettingEntries
 */
public interface ChunkGeneratorSettingEntries extends ChunkGeneratorSettingEntry {
	/**
	 * INSTANCE
	 */
	ChunkGeneratorSettingEntries INSTANCE = new ChunkGeneratorSettingEntries() {
	};
	/**
	 * test
	 */
	RegistryKey<ChunkGeneratorSettings> TEST = ChunkGeneratorSettingEntry.of(MOD_PASS, "test");
	
	private static ChunkGeneratorSettings createSurfaceSettings(Registerable<?> registerable) {
		return new ChunkGeneratorSettings(ChunkGenerationShapeConfig.SURFACE, Blocks.STONE.getDefaultState(), Blocks.WATER.getDefaultState(),
				DensityFunction.createSurfaceNoiseRouter(registerable.getRegistryLookup(RegistryKeys.DENSITY_FUNCTION), registerable.getRegistryLookup(RegistryKeys.NOISE_PARAMETERS), false, false),
				VanillaSurfaceRules.createOverworldSurfaceRule(), (new DefaultBiomeParameters() {
		}).getSpawnSuitabilityNoises(), 63, false, true, true, false);
	}
	
	@Override
	default RegistryBuilder.BootstrapFunction<ChunkGeneratorSettings> bootstrap() {
		return registerable -> registerable.register(TEST, ChunkGeneratorSettingEntries.createSurfaceSettings(registerable));
	}
	
	/**
	 * DensityFunction
	 */
	class DensityFunction extends DensityFunctions {
		/**
		 * @param densityFunctionLookup densityFunctionLookup
		 * @param noiseParametersLookup noiseParametersLookup
		 * @param largeBiomes           largeBiomes
		 * @param amplified             amplified
		 * @return return
		 */
		public static NoiseRouter createSurfaceNoiseRouter(RegistryEntryLookup<net.minecraft.world.gen.densityfunction.DensityFunction> densityFunctionLookup, RegistryEntryLookup<DoublePerlinNoiseSampler.NoiseParameters> noiseParametersLookup, boolean largeBiomes, boolean amplified) {
			return DensityFunctions.createSurfaceNoiseRouter(densityFunctionLookup, noiseParametersLookup, largeBiomes, amplified);
		}
	}
}
