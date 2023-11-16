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

package pers.saikel0rado1iu.silk.datagen.worldgen;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.biome.source.TheEndBiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.PlacedFeature;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.datagen.SilkWorldGenerator;

import java.util.Map;

/**
 * <p><b style="color:FFC800"><font size="+1">用于创建、注册、生成世界预设</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldGenerator} 中覆盖 {@link SilkWorldGenerator#worldPresets()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public abstract class SilkWorldPreset {
	@ApiStatus.Internal
	public static final SilkWorldPreset EMPTY = new SilkWorldPreset() {
		@Override
		public void bootstrap(Registerable<WorldPreset> presetRegisterable) {
		}
	};
	
	protected static RegistryKey<WorldPreset> register(ModBasicData mod, String id) {
		return RegistryKey.of(RegistryKeys.WORLD_PRESET, new Identifier(mod.getId(), id));
	}
	
	/**
	 * 参考 {@link WorldPresets#bootstrap(Registerable)}
	 */
	@ApiStatus.OverrideOnly
	public abstract void bootstrap(Registerable<WorldPreset> presetRegisterable);
	
	@SilkApi
	public static class Registrar {
		public final Registerable<WorldPreset> presetRegisterable;
		public final RegistryEntryLookup<ChunkGeneratorSettings> chunkGeneratorSettingsLookup;
		public final RegistryEntryLookup<Biome> biomeLookup;
		public final RegistryEntryLookup<PlacedFeature> featureLookup;
		public final RegistryEntryLookup<StructureSet> structureSetLookup;
		public final RegistryEntryLookup<MultiNoiseBiomeSourceParameterList> multiNoisePresetLookup;
		public final RegistryEntry<DimensionType> overworldDimensionType;
		public final DimensionOptions netherDimensionOptions;
		public final DimensionOptions endDimensionOptions;
		
		Registrar(Registerable<WorldPreset> presetRegisterable) {
			this.presetRegisterable = presetRegisterable;
			RegistryEntryLookup<DimensionType> registryEntryLookup = presetRegisterable.getRegistryLookup(RegistryKeys.DIMENSION_TYPE);
			chunkGeneratorSettingsLookup = presetRegisterable.getRegistryLookup(RegistryKeys.CHUNK_GENERATOR_SETTINGS);
			biomeLookup = presetRegisterable.getRegistryLookup(RegistryKeys.BIOME);
			featureLookup = presetRegisterable.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
			structureSetLookup = presetRegisterable.getRegistryLookup(RegistryKeys.STRUCTURE_SET);
			multiNoisePresetLookup = presetRegisterable.getRegistryLookup(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
			overworldDimensionType = registryEntryLookup.getOrThrow(DimensionTypes.OVERWORLD);
			RegistryEntry.Reference<DimensionType> registryEntry = registryEntryLookup.getOrThrow(DimensionTypes.THE_NETHER);
			RegistryEntry.Reference<ChunkGeneratorSettings> registryEntry2 = chunkGeneratorSettingsLookup.getOrThrow(ChunkGeneratorSettings.NETHER);
			RegistryEntry.Reference<MultiNoiseBiomeSourceParameterList> reference = multiNoisePresetLookup.getOrThrow(MultiNoiseBiomeSourceParameterLists.NETHER);
			netherDimensionOptions = new DimensionOptions(registryEntry, new NoiseChunkGenerator(MultiNoiseBiomeSource.create(reference), registryEntry2));
			RegistryEntry.Reference<DimensionType> registryEntry3 = registryEntryLookup.getOrThrow(DimensionTypes.THE_END);
			RegistryEntry.Reference<ChunkGeneratorSettings> registryEntry4 = chunkGeneratorSettingsLookup.getOrThrow(ChunkGeneratorSettings.END);
			endDimensionOptions = new DimensionOptions(registryEntry3, new NoiseChunkGenerator(TheEndBiomeSource.createVanilla(biomeLookup), registryEntry4));
		}
		
		public WorldPreset createPreset(DimensionOptions dimensionOptions) {
			return new WorldPreset(Map.of(DimensionOptions.OVERWORLD, dimensionOptions, DimensionOptions.NETHER, netherDimensionOptions, DimensionOptions.END, endDimensionOptions));
		}
		
		public void register(RegistryKey<WorldPreset> key, DimensionOptions dimensionOptions) {
			presetRegisterable.register(key, createPreset(dimensionOptions));
		}
	}
}
