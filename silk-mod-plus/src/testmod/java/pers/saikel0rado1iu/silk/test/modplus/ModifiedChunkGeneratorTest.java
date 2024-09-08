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

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyChunkGeneratorCustomEvents;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyChunkGeneratorInstanceEvents;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyChunkGeneratorUpgradableEvents;
import pers.saikel0rado1iu.silk.api.generate.world.WorldPresetEntry;
import pers.saikel0rado1iu.silk.api.landform.ChunkStorageData;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ModifiedChunkGenerator;
import pers.saikel0rado1iu.silk.impl.SilkModPlus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * ModifiedChunkGeneratorTest
 */
@SuppressWarnings("DataFlowIssue")
public final class ModifiedChunkGeneratorTest extends ModifiedChunkGenerator {
	private static final String VERSION = "1";
	
	ModifiedChunkGeneratorTest(BiomeSource biomeSource, List<FixedBiomeSource> additionalBiomeSources, RegistryEntry<ChunkGeneratorSettings> settings, String version) {
		super(biomeSource, additionalBiomeSources, settings, version);
	}
	
	@SuppressWarnings("unused")
	public static ModifiedChunkGeneratorTest getInstance(DynamicRegistryManager registryManager) {
		RegistryEntry<MultiNoiseBiomeSourceParameterList> parameters = registryManager.get(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
				.getEntry(MultiNoiseBiomeSourceParameterLists.OVERWORLD).orElseThrow();
		RegistryEntry<ChunkGeneratorSettings> settings = registryManager.get(RegistryKeys.CHUNK_GENERATOR_SETTINGS).getEntry(ChunkGeneratorSettings.OVERWORLD).orElseThrow();
		return ModifyChunkGeneratorInstanceEvents.MODIFY_DATA_GEN_INSTANCE.invoker().getInstance(new ModifiedChunkGeneratorTest(MultiNoiseBiomeSource.create(parameters), ImmutableList.of(), settings, VERSION), registryManager);
	}
	
	private static ModifiedChunkGeneratorTest getInstance(Registerable<WorldPreset> registerable, WorldPresetEntry.Registrar registrar) {
		RegistryEntry<MultiNoiseBiomeSourceParameterList> parameters = registrar.multiNoisePresetLookup.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD);
		RegistryEntry<ChunkGeneratorSettings> settings = registrar.chunkGeneratorSettingsLookup.getOrThrow(ChunkGeneratorSettings.OVERWORLD);
		return ModifyChunkGeneratorInstanceEvents.MODIFY_REGISTER_INSTANCE.invoker().getInstance(new ModifiedChunkGeneratorTest(MultiNoiseBiomeSource.create(parameters), ImmutableList.of(), settings, VERSION), registerable, registrar);
	}
	
	@SuppressWarnings("unused")
	public static void register(RegistryKey<WorldPreset> worldPreset, Registerable<WorldPreset> registerable) {
		WorldPresetEntry.Registrar registrar = new WorldPresetEntry.Registrar(registerable);
		registrar.register(worldPreset, new DimensionOptions(registerable.getRegistryLookup(RegistryKeys.DIMENSION_TYPE).getOrThrow(DimensionTypes.OVERWORLD), getInstance(registerable, registrar)));
	}
	
	public static void test() {
		ModifiedChunkGeneratorTest modifiedChunkGeneratorTest = new ModifiedChunkGeneratorTest(null, List.of(), null, "0");
		ModifyChunkGeneratorUpgradableEvents.MODIFY_VERSION.register((upgradable, version) -> {
			if (upgradable != modifiedChunkGeneratorTest) return Map.entry(ActionResult.PASS, version);
			return Map.entry(ActionResult.PASS, version + "-mod-plus");
		});
		SilkModPlus.getInstance().logger().error(modifiedChunkGeneratorTest.version());
	}
	
	@Override
	public String version() {
		String version = super.version();
		Map.Entry<ActionResult, String> result = ModifyChunkGeneratorUpgradableEvents.MODIFY_VERSION.invoker().version(this, version);
		if (ActionResult.FAIL == result.getKey()) return version;
		return result.getValue();
	}
	
	@Override
	public int compare(String s) {
		int compare = 0;
		Map.Entry<ActionResult, Integer> result = ModifyChunkGeneratorUpgradableEvents.MODIFY_COMPARE.invoker().compare(this, compare, s);
		if (ActionResult.FAIL == result.getKey()) return compare;
		return result.getValue();
	}
	
	@Override
	public boolean needRefresh(BlockPos blockPos, ChunkStorageData chunkStorageData, String s) {
		boolean needRefresh = false;
		Map.Entry<ActionResult, Boolean> result = ModifyChunkGeneratorUpgradableEvents.MODIFY_NEED_REFRESH.invoker().needRefresh(this, needRefresh, blockPos, chunkStorageData, s);
		if (ActionResult.FAIL == result.getKey()) return needRefresh;
		return result.getValue();
	}
	
	@Override
	public boolean needUpgrade(BlockPos blockPos, ChunkStorageData chunkStorageData, String s) {
		boolean needUpgrade = false;
		Map.Entry<ActionResult, Boolean> result = ModifyChunkGeneratorUpgradableEvents.MODIFY_NEED_UPGRADE.invoker().needUpgrade(this, needUpgrade, blockPos, chunkStorageData, s);
		if (ActionResult.FAIL == result.getKey()) return needUpgrade;
		return result.getValue();
	}
	
	@Override
	public boolean needDowngrade(BlockPos blockPos, ChunkStorageData chunkStorageData, String s) {
		boolean needDowngrade = false;
		Map.Entry<ActionResult, Boolean> result = ModifyChunkGeneratorUpgradableEvents.MODIFY_NEED_DOWNGRADE.invoker().needDowngrade(this, needDowngrade, blockPos, chunkStorageData, s);
		if (ActionResult.FAIL == result.getKey()) return needDowngrade;
		return result.getValue();
	}
	
	@SuppressWarnings("ConstantValue")
	@Override
	public Codec<? extends ChunkGenerator> getCodec() {
		Codec<? extends ChunkGenerator> codec = null;
		Map.Entry<ActionResult, Codec<? extends ChunkGenerator>> result = ModifyChunkGeneratorUpgradableEvents.MODIFY_GET_CODEC.invoker().getCodec(this, codec);
		if (ActionResult.FAIL == result.getKey()) return codec;
		return result.getValue();
	}
	
	@Override
	public Optional<Pair<BlockPos, RegistryEntry<Biome>>> locateBiome(BlockPos origin, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, ServerWorld world) {
		Optional<Pair<BlockPos, RegistryEntry<Biome>>> pair = super.locateBiome(origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world);
		Map.Entry<ActionResult, Optional<Pair<BlockPos, RegistryEntry<Biome>>>> result = ModifyChunkGeneratorCustomEvents.MODIFY_LOCATE_BIOME.invoker().locateBiome(this, pair, origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world);
		if (ActionResult.FAIL == result.getKey()) return pair;
		return result.getValue();
	}
	
	@SuppressWarnings("ConstantValue")
	@Override
	public BiomeSource getBiomeSource(BlockPos blockPos) {
		BiomeSource source = null;
		Map.Entry<ActionResult, BiomeSource> result = ModifyChunkGeneratorCustomEvents.MODIFY_GET_BIOME_SOURCE.invoker().getBiomeSource(this, source, blockPos);
		if (ActionResult.FAIL == result.getKey()) return source;
		return result.getValue();
	}
	
	@SuppressWarnings("UnnecessaryLocalVariable")
	@Override
	public Optional<BlockState> getTerrainNoise(BlockPos blockPos, Optional<BlockState> blockState, int i) {
		Optional<BlockState> state = blockState;
		Map.Entry<ActionResult, Optional<BlockState>> result = ModifyChunkGeneratorCustomEvents.MODIFY_GET_TERRAIN_NOISE.invoker().getTerrainNoise(this, state, blockPos, blockState, i);
		if (ActionResult.FAIL == result.getKey()) return state;
		return result.getValue();
	}
}
