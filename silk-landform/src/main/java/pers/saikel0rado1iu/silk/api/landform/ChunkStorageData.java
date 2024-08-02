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

package pers.saikel0rado1iu.silk.api.landform;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkSerializer;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.PalettedContainer;
import net.minecraft.world.chunk.ReadableContainer;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.commons.lang3.ArrayUtils;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.ChunkSerializer.*;

/**
 * <h2 style="color:FFC800">区块储存数据</h2>
 * 此类用于解析被存储在 Anvil 文件格式 (*.mca) 中的区块数据而不需要使用 {@link ServerWorld}。<br>
 * 并提供类似于 {@link ChunkSerializer} 中的解析方法，和一些包含在 {@link Chunk} 中的部分基础方法以供判断或操控区块储存文件。
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public record ChunkStorageData
		(ChunkPos chunkPos, Map<ChunkSectionPos, ChunkSection> sections, Map<Heightmap.Type, long[]> heightmaps,
		 Map<Structure, StructureStart> structureStarts, Map<Structure, LongSet> structureReferences, List<NbtCompound> entities,
		 Map<BlockPos, NbtCompound> blockEntityNbts, boolean isLightOn, long inhabitedTime) {
	/**
	 * 方块坐标到区块坐标方法
	 *
	 * @param blockCoord 方块坐标
	 * @return 区块坐标
	 */
	public static int blockToChunkCoord(int blockCoord) {
		return MaterialRules.MaterialRuleContext.blockToChunkCoord(blockCoord);
	}
	
	/**
	 * 区块坐标到方块坐标方法
	 *
	 * @param chunkCoord 区块坐标
	 * @return 方块坐标
	 */
	public static int chunkToBlockCoord(int chunkCoord) {
		return MaterialRules.MaterialRuleContext.chunkToBlockCoord(chunkCoord);
	}
	
	/**
	 * 反序列化区块储存数据
	 *
	 * @param saveLoader 存档加载器
	 * @param session    关卡存储会话
	 * @param pos        区块坐标
	 * @param nbt        区块 NBT
	 * @return 区块储存数据
	 */
	public static ChunkStorageData deserialize(SaveLoader saveLoader, LevelStorage.Session session, ChunkPos pos, NbtCompound nbt) {
		ChunkPos posInNbt = new ChunkPos(nbt.getInt(X_POS_KEY), nbt.getInt(Z_POS_KEY));
		if (!pos.equals(posInNbt)) {
			SilkLandform.getInstance().logger().error("Deserialize error: Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", pos, pos, posInNbt);
		}
		NbtList sections = nbt.getList(SECTIONS_KEY, NbtElement.COMPOUND_TYPE);
		Registry<Biome> biomes = UpgradableWorldManager.registryManager().get(RegistryKeys.BIOME);
		Codec<ReadableContainer<RegistryEntry<Biome>>> biomeCodec = ChunkSerializer.createCodec(biomes);
		Map<ChunkSectionPos, ChunkSection> sectionMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (NbtElement element : sections) {
			NbtCompound section = (NbtCompound) element;
			PalettedContainer<BlockState> palettedContainer = section.contains("block_states", NbtElement.COMPOUND_TYPE)
					? CODEC.parse(NbtOps.INSTANCE, section.getCompound("block_states"))
					.promotePartial(errorMessage -> SilkLandform.getInstance().logger().error(errorMessage)).getOrThrow(false, SilkLandform.getInstance().logger()::error)
					: new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE);
			ReadableContainer<RegistryEntry<Biome>> readableContainer = section.contains("biomes", NbtElement.COMPOUND_TYPE)
					? biomeCodec.parse(NbtOps.INSTANCE, section.getCompound("biomes"))
					.promotePartial(errorMessage -> SilkLandform.getInstance().logger().error(errorMessage)).getOrThrow(false, SilkLandform.getInstance().logger()::error)
					: new PalettedContainer<>(biomes.getIndexedEntries(), biomes.entryOf(BiomeKeys.PLAINS), PalettedContainer.PaletteProvider.BIOME);
			sectionMap.put(ChunkSectionPos.from(pos, section.getByte("Y")), new ChunkSection(palettedContainer, readableContainer));
		}
		NbtCompound heightmapsNbt = nbt.getCompound(HEIGHTMAPS_KEY);
		Map<Heightmap.Type, long[]> heightmaps = Maps.newHashMapWithExpectedSize(4);
		for (Heightmap.Type type : Heightmap.Type.values()) {
			if (heightmapsNbt.contains(type.getName(), NbtElement.LONG_ARRAY_TYPE)) heightmaps.put(type, heightmapsNbt.getLongArray(type.getName()));
		}
		NbtCompound structuresNbt = nbt.getCompound("structures");
		Map<Structure, StructureStart> structureStarts = ChunkSerializer.readStructureStarts(
				new StructureContext(
						UpgradableWorldData.getResourceManager(saveLoader),
						UpgradableWorldManager.registryManager(),
						UpgradableWorldData.getStructureTemplateManager(saveLoader, session)),
				structuresNbt, UpgradableWorldData.getSeed(saveLoader));
		Map<Structure, LongSet> structureReferences = ChunkSerializer.readStructureReferences(UpgradableWorldManager.registryManager(), pos, structuresNbt);
		List<NbtCompound> entities = Lists.newArrayList();
		for (NbtElement element : nbt.getList("entities", NbtElement.COMPOUND_TYPE)) entities.add((NbtCompound) element);
		Map<BlockPos, NbtCompound> blockEntityNbts = Maps.newHashMap();
		for (NbtElement element : nbt.getList("block_entities", NbtElement.COMPOUND_TYPE)) {
			blockEntityNbts.put(BlockEntity.posFromNbt((NbtCompound) element), (NbtCompound) element);
		}
		return new ChunkStorageData(pos, sectionMap, heightmaps, structureStarts, structureReferences, entities, blockEntityNbts, nbt.getBoolean(IS_LIGHT_ON_KEY), nbt.getLong("InhabitedTime"));
	}
	
	/**
	 * 获取世界高度
	 *
	 * @return 世界高度
	 */
	public int height() {
		final int chunkSize = 16;
		Map<Integer, ChunkSectionPos> sectionYMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (ChunkSectionPos sectionPos : sections.keySet()) sectionYMap.put(sectionPos.getSectionY(), sectionPos);
		int[] sectionYs = ArrayUtils.toPrimitive(sectionYMap.keySet().toArray(new Integer[0]));
		Arrays.sort(sectionYs);
		return (sectionYs[sectionYs.length - 1] * chunkSize) + 15;
	}
	
	/**
	 * 获取世界地底坐标
	 *
	 * @return 世界地底坐标
	 */
	public int button() {
		final int chunkSize = 16;
		Map<Integer, ChunkSectionPos> sectionYMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (ChunkSectionPos sectionPos : sections.keySet()) sectionYMap.put(sectionPos.getSectionY(), sectionPos);
		int[] sectionYs = ArrayUtils.toPrimitive(sectionYMap.keySet().toArray(new Integer[0]));
		Arrays.sort(sectionYs);
		return (sectionYs[0] * chunkSize) + 1;
	}
	
	/**
	 * 获取世界总高度
	 *
	 * @return 世界总高度
	 */
	public int totalHeight() {
		return height() + Math.abs(button());
	}
	
	/**
	 * 获取区块部分索引
	 *
	 * @param pos 方块坐标
	 * @return 区块部分坐标
	 */
	public ChunkSectionPos getSectionIndex(BlockPos pos) {
		final int chunkSize = 16;
		Map<Integer, ChunkSectionPos> sectionYMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (ChunkSectionPos sectionPos : sections.keySet()) sectionYMap.put(sectionPos.getSectionY(), sectionPos);
		int[] sectionYs = ArrayUtils.toPrimitive(sectionYMap.keySet().toArray(new Integer[0]));
		Arrays.sort(sectionYs);
		return sectionYMap.get(pos.getY() / chunkSize < 0 ? (pos.getY() / chunkSize) - 1 : pos.getY() / chunkSize);
	}
	
	/**
	 * 获取区块部分
	 *
	 * @param pos 区块部分坐标
	 * @return 区块部分
	 */
	public Optional<ChunkSection> getSection(ChunkSectionPos pos) {
		return Optional.ofNullable(sections.get(pos));
	}
	
	/**
	 * 获取生物群系
	 *
	 * @param pos 方块坐标
	 * @return 生物群系注册表项
	 */
	public Optional<RegistryEntry<Biome>> getBiome(BlockPos pos) {
		Optional<ChunkSection> section = getSection(getSectionIndex(pos));
		return section.map(chunkSection -> chunkSection.getBiome(
				BiomeCoords.fromBlock(pos.getX()) & 3,
				BiomeCoords.fromBlock(MathHelper.clamp(pos.getY(), button(), height())) & 3,
				BiomeCoords.fromBlock(pos.getZ()) & 3));
	}
	
	/**
	 * 获取结构开始
	 *
	 * @param structure 结构
	 * @return 结构开始
	 */
	public Optional<StructureStart> getStructureStart(Structure structure) {
		return Optional.ofNullable(structureStarts.get(structure));
	}
	
	/**
	 * 获取结构参考
	 *
	 * @param structure 结构
	 * @return 结构参考
	 */
	public LongSet getStructureReferences(Structure structure) {
		return structureReferences.getOrDefault(structure, new LongOpenHashSet());
	}
	
	/**
	 * 获取高度图
	 *
	 * @param type 高度图类型
	 * @return 高度图
	 */
	public long[] getHeightmap(Heightmap.Type type) {
		return heightmaps.get(type);
	}
	
	/**
	 * 获取方块实体 NBT
	 *
	 * @param pos 方块坐标
	 * @return 方块实体 NBT
	 */
	public Optional<NbtCompound> getBlockEntityNbt(BlockPos pos) {
		return Optional.ofNullable(blockEntityNbts.get(pos));
	}
}
