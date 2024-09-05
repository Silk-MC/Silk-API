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

package pers.saikel0rado1iu.silk.api.event.modplus;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorCustom;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">修改 {@link ChunkGeneratorCustom} 事件</h2>
 * 用于动态修改 {@link ChunkGeneratorCustom} 中生成规则的事件<br>
 * 需要模组作者自主在 {@link ChunkGeneratorCustom} 中实现事件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyChunkGeneratorCustomEvents {
	/**
	 * 此事件将 {@link ChunkGeneratorCustom} 修改方法 {@link ChunkGeneratorCustom#locateBiome(BlockPos, int, int, int, Predicate, MultiNoiseUtil.MultiNoiseSampler, ServerWorld)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link Pair}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link Pair}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link Pair}<br>
	 */
	Event<ModifyLocateBiome> MODIFY_LOCATE_BIOME = EventFactory.createArrayBacked(ModifyLocateBiome.class, listeners -> (custom, pair, origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world) -> {
		Optional<Pair<BlockPos, RegistryEntry<Biome>>> optionalPair = pair;
		for (ModifyLocateBiome event : listeners) {
			Map.Entry<ActionResult, Optional<Pair<BlockPos, RegistryEntry<Biome>>>> entry = event.locateBiome(custom, optionalPair, origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world);
			if (entry.getKey() != ActionResult.PASS) return entry;
			optionalPair = entry.getValue();
		}
		return Map.entry(ActionResult.PASS, optionalPair);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorCustom} 修改方法 {@link ChunkGeneratorCustom#getBiomeSource(BlockPos)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link BiomeSource}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link BiomeSource}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link BiomeSource}<br>
	 */
	Event<ModifyGetBiomeSource> MODIFY_GET_BIOME_SOURCE = EventFactory.createArrayBacked(ModifyGetBiomeSource.class, listeners -> (custom, source, pos) -> {
		BiomeSource biomeSource = source;
		for (ModifyGetBiomeSource event : listeners) {
			Map.Entry<ActionResult, BiomeSource> entry = event.getBiomeSource(custom, biomeSource, pos);
			if (entry.getKey() != ActionResult.PASS) return entry;
			biomeSource = entry.getValue();
		}
		return Map.entry(ActionResult.PASS, biomeSource);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorCustom} 修改方法 {@link ChunkGeneratorCustom#getTerrainNoise(BlockPos, BlockState, int)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link BlockState}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link BlockState}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link BlockState}<br>
	 */
	Event<ModifyGetTerrainNoise> MODIFY_GET_TERRAIN_NOISE = EventFactory.createArrayBacked(ModifyGetTerrainNoise.class, listeners -> (custom, state, pos, originBlock, estimateSurfaceHeight) -> {
		BlockState blockState = state;
		for (ModifyGetTerrainNoise event : listeners) {
			Map.Entry<ActionResult, BlockState> entry = event.getTerrainNoise(custom, blockState, pos, originBlock, estimateSurfaceHeight);
			if (entry.getKey() != ActionResult.PASS) return entry;
			blockState = entry.getValue();
		}
		return Map.entry(ActionResult.PASS, blockState);
	});
	
	/**
	 * 修改定位生物群系
	 */
	@FunctionalInterface
	interface ModifyLocateBiome {
		Map.Entry<ActionResult, Optional<Pair<BlockPos, RegistryEntry<Biome>>>> locateBiome(ChunkGeneratorCustom custom, Optional<Pair<BlockPos, RegistryEntry<Biome>>> pair, BlockPos origin, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, ServerWorld world);
	}
	
	/**
	 * 修改获取生物群系源
	 */
	@FunctionalInterface
	interface ModifyGetBiomeSource {
		Map.Entry<ActionResult, BiomeSource> getBiomeSource(ChunkGeneratorCustom custom, BiomeSource source, BlockPos pos);
	}
	
	/**
	 * 修改获取地形噪声
	 */
	@FunctionalInterface
	interface ModifyGetTerrainNoise {
		Map.Entry<ActionResult, BlockState> getTerrainNoise(ChunkGeneratorCustom custom, BlockState state, BlockPos pos, BlockState originBlock, int estimateSurfaceHeight);
	}
}
