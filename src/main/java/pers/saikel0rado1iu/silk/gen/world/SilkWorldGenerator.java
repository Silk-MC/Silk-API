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

package pers.saikel0rado1iu.silk.gen.world;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.gen.ModDataGeneration;

/**
 * <h2 style="color:FFC800">用于提供世界生成的方便方法</h2>
 * <p style="color:FFC800">模组作者需要在 {@link ModDataGeneration} 中覆盖 {@link ModDataGeneration#worldGen()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkWorldGenerator {
	@ApiStatus.Internal
	public static final SilkWorldGenerator EMPTY = new SilkWorldGenerator() {
	};
	
	@ApiStatus.Internal
	public void configure(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.BIOME));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_CARVER));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DENSITY_FUNCTION));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.NOISE_PARAMETERS));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CHUNK_GENERATOR_SETTINGS));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PROCESSOR_LIST));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.STRUCTURE));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.STRUCTURE_SET));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.WORLD_PRESET));
	}
	
	/**
	 * 提供来自模组的生物群系以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkBiomeKey biomes() {
		return SilkBiomeKey.EMPTY;
	}
	
	/**
	 * 提供来自模组的已配置的地物以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkConfiguredFeature configuredFeatures() {
		return SilkConfiguredFeature.EMPTY;
	}
	
	/**
	 * 提供来自模组的多重噪声生物群系源参数列表以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkMultiNoiseBiomeSourceParameterList multiNoiseBiomeSourceParameterLists() {
		return SilkMultiNoiseBiomeSourceParameterList.EMPTY;
	}
	
	/**
	 * 提供来自模组的区块生成器设置以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkChunkGeneratorSetting chunkGeneratorSettings() {
		return SilkChunkGeneratorSetting.EMPTY;
	}
	
	/**
	 * 提供来自模组的已放置的地物以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkPlacedFeature placedFeatures() {
		return SilkPlacedFeature.EMPTY;
	}
	
	/**
	 * 提供来自模组的世界预设以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkWorldPreset worldPresets() {
		return SilkWorldPreset.EMPTY;
	}
}
