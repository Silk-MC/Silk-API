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
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.*;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionTypes;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import org.apache.commons.lang3.StringUtils;
import pers.saikel0rado1iu.silk.api.generate.world.WorldPresetEntry;
import pers.saikel0rado1iu.silk.api.landform.ChunkStorageData;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ModifiedChunkGenerator;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 测试
 */
public final class TestChunkGenerator extends ModifiedChunkGenerator {
	private static final int RANGE = 100;
	private static final String VERSION = "1";
	
	TestChunkGenerator(BiomeSource biomeSource, List<FixedBiomeSource> additionalBiomeSources, RegistryEntry<ChunkGeneratorSettings> settings, String version) {
		super(biomeSource, additionalBiomeSources, settings, version);
	}
	
	/**
	 * @param registryManager registryManager
	 * @return TestChunkGenerator
	 */
	public static TestChunkGenerator getInstance(DynamicRegistryManager registryManager) {
		RegistryEntry<MultiNoiseBiomeSourceParameterList> parameters = registryManager.get(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)
				.getEntry(MultiNoiseBiomeSourceParameterLists.OVERWORLD).orElseThrow();
		RegistryEntry<Biome> biome = registryManager.get(RegistryKeys.BIOME).getEntry(BiomeKeys.DESERT).orElseThrow();
		RegistryEntry<ChunkGeneratorSettings> settings = registryManager.get(RegistryKeys.CHUNK_GENERATOR_SETTINGS).getEntry(ChunkGeneratorSettings.OVERWORLD).orElseThrow();
		return new TestChunkGenerator(MultiNoiseBiomeSource.create(parameters), ImmutableList.of(new FixedBiomeSource(biome)), settings, VERSION);
	}
	
	private static TestChunkGenerator getInstance(Registerable<WorldPreset> registerable, WorldPresetEntry.Registrar registrar) {
		RegistryEntryLookup<Biome> biomeRegistrar = registerable.getRegistryLookup(RegistryKeys.BIOME);
		RegistryEntry<MultiNoiseBiomeSourceParameterList> parameters = registrar.multiNoisePresetLookup.getOrThrow(BiomeSourceParamLists.TEST);
		RegistryEntry<Biome> biome = biomeRegistrar.getOrThrow(BiomeKeys.DESERT);
		RegistryEntry<ChunkGeneratorSettings> settings = registrar.chunkGeneratorSettingsLookup.getOrThrow(ChunkGeneratorSettingEntries.TEST);
		return new TestChunkGenerator(MultiNoiseBiomeSource.create(parameters), ImmutableList.of(new FixedBiomeSource(biome)), settings, VERSION);
	}
	
	/**
	 * @param worldPreset  worldPreset
	 * @param registerable registerable
	 */
	public static void register(RegistryKey<WorldPreset> worldPreset, Registerable<WorldPreset> registerable) {
		WorldPresetEntry.Registrar registrar = new WorldPresetEntry.Registrar(registerable);
		registrar.register(worldPreset, new DimensionOptions(registerable.getRegistryLookup(RegistryKeys.DIMENSION_TYPE).getOrThrow(DimensionTypes.OVERWORLD), getInstance(registerable, registrar)));
	}
	
	private static boolean check(BlockPos pos) {
		return Math.abs(pos.getX()) < RANGE && Math.abs(pos.getZ()) < RANGE;
	}
	
	@Override
	public Optional<Pair<BlockPos, RegistryEntry<Biome>>> locateBiome(BlockPos origin, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, ServerWorld world) {
		SilkLandform.getInstance().logger().info("" + seed());
		// SilkLandform.getInstance().logger().info(ChunkStorageData.blockToChunkCoord(0) + "");
		return super.locateBiome(origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world);
	}
	
	/**
	 * 通过在世界中的块坐标判断生物群系，用于设置以特殊方法而非原版方法生成生物群系
	 *
	 * @param pos 块坐标
	 * @return 在坐标上应生成的生物群系源
	 */
	@Override
	public BiomeSource getBiomeSource(BlockPos pos) {
		return check(pos) ? getAdditionalBiomeSource(BiomeKeys.DESERT).orElse(null) : biomeSource;
	}
	
	/**
	 * 修改地形噪声的一种方法，用于设置以特殊方法而非原版方法生成地形噪声
	 *
	 * @param pos                   块坐标
	 * @param originBlock           原始块
	 * @param estimateSurfaceHeight 预期表面高度
	 * @return 应设置的噪声块
	 */
	@Override
	public Optional<BlockState> getTerrainNoise(BlockPos pos, Optional<BlockState> originBlock, int estimateSurfaceHeight) {
		if (check(pos) && pos.getY() == 200) return Optional.of(Blocks.GLASS.getDefaultState());
		return originBlock;
	}
	
	/**
	 * 用于判断生成器版本号的方法
	 *
	 * @param savedVersion 已保存在存档中的版本号
	 * @return -1 为小于；0 为等于；1 为大于
	 */
	@Override
	public int compare(String savedVersion) {
		if (!StringUtils.isNumeric(savedVersion)) return -1;
		return Integer.compare(Integer.parseInt(savedVersion), Integer.parseInt(VERSION));
	}
	
	/**
	 * 通过块坐标解析 {@link ChunkStorageData} 的数据进行判断是否可刷新坐标中的区块
	 *
	 * @param pos     需判断块坐标
	 * @param chunk   区块数据
	 * @param version 区块当前的生成器版本
	 * @return 是否需要刷新区块
	 */
	@Override
	public boolean needRefresh(BlockPos pos, ChunkStorageData chunk, String version) {
		return check(pos);
	}
	
	/**
	 * 通过块坐标解析 {@link ChunkStorageData} 的数据进行判断是否可升级坐标中的区块
	 *
	 * @param pos     需判断块坐标
	 * @param chunk   区块数据
	 * @param version 区块当前的生成器版本
	 * @return 是否需要升级区块
	 */
	@Override
	public boolean needUpgrade(BlockPos pos, ChunkStorageData chunk, String version) {
		return check(pos);
	}
	
	/**
	 * 通过块坐标解析 {@link ChunkStorageData} 的数据进行判断是否可降级坐标中的区块
	 *
	 * @param pos     需判断块坐标
	 * @param chunk   区块数据
	 * @param version 区块当前的生成器版本
	 * @return 是需要降级区块
	 */
	@Override
	public boolean needDowngrade(BlockPos pos, ChunkStorageData chunk, String version) {
		// SilkLandform.getInstance().logger().info(chunk.getBiome(pos) + "");
		// SilkLandform.getInstance().logger().info(chunk.getBlockEntityNbt(pos) + "");
		// SilkLandform.getInstance().logger().info(chunk.getStructureStart(null) + "");
		// SilkLandform.getInstance().logger().info(chunk.getStructureReferences(null) + "");
		// SilkLandform.getInstance().logger().info(Arrays.toString(chunk.getHeightmap(Heightmap.Type.MOTION_BLOCKING)));
		return check(pos);
	}
	
	/**
	 * 用于提供区块生成器的 {@link Codec}
	 *
	 * @return 区块生成器的 {@link Codec}
	 */
	@Override
	public Codec<? extends ChunkGenerator> getCodec() {
		return ChunkGeneratorCodecs.TEST;
	}
}
