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

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyChunkGeneratorCustomEvents;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyChunkGeneratorUpgradableEvents;
import pers.saikel0rado1iu.silk.api.landform.ChunkStorageData;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ModifiedChunkGenerator;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * ModifiedChunkGeneratorTest
 */
@SuppressWarnings("DataFlowIssue")
public final class ModifiedChunkGeneratorTest extends ModifiedChunkGenerator {
	ModifiedChunkGeneratorTest(BiomeSource biomeSource, List<FixedBiomeSource> additionalBiomeSources, RegistryEntry<ChunkGeneratorSettings> settings, String version) {
		super(biomeSource, additionalBiomeSources, settings, version);
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
	
	@Override
	public BiomeSource getBiomeSource(BlockPos blockPos) {
		BiomeSource source = null;
		Map.Entry<ActionResult, BiomeSource> result = ModifyChunkGeneratorCustomEvents.MODIFY_GET_BIOME_SOURCE.invoker().getBiomeSource(this, source, blockPos);
		if (ActionResult.FAIL == result.getKey()) return source;
		return result.getValue();
	}
	
	@Override
	public BlockState getTerrainNoise(BlockPos blockPos, BlockState blockState, int i) {
		BlockState state = null;
		Map.Entry<ActionResult, BlockState> result = ModifyChunkGeneratorCustomEvents.MODIFY_GET_TERRAIN_NOISE.invoker().getTerrainNoise(this, state, blockPos, blockState, i);
		if (ActionResult.FAIL == result.getKey()) return state;
		return result.getValue();
	}
}
