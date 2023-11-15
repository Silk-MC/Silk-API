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

package pers.saikel0rado1iu.silk.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.datagen.worldgen.SilkBiomeKey;
import pers.saikel0rado1iu.silk.datagen.worldgen.SilkConfiguredFeature;
import pers.saikel0rado1iu.silk.datagen.worldgen.SilkPlacedFeature;

import java.util.concurrent.CompletableFuture;

/**
 * <p><b style="color:FFC800"><font size="+1">用于提供世界生成的方便方法</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModDataGeneration} 中覆盖 {@link ModDataGeneration#worldGen()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkWorldGenerator extends FabricDynamicRegistryProvider {
	public SilkWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.BIOME));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_CARVER));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DENSITY_FUNCTION));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.FLAT_LEVEL_GENERATOR_PRESET));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.NOISE_PARAMETERS));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PROCESSOR_LIST));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.STRUCTURE));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.STRUCTURE_SET));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL));
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.WORLD_PRESET));
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
	 * 提供来自模组的已放置的地物以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkPlacedFeature placedFeatures() {
		return SilkPlacedFeature.EMPTY;
	}
	
	/**
	 * 提供来自模组的生物群系以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkBiomeKey biomes() {
		return SilkBiomeKey.EMPTY;
	}
	
	public abstract ModBasicData getMod();
	
	@Override
	public String getName() {
		return getMod().getId();
	}
}
