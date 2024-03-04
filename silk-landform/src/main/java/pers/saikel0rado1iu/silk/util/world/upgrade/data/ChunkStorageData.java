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

package pers.saikel0rado1iu.silk.util.world.upgrade.data;

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
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.ChunkSerializer.*;

/**
 * <h2 style="color:FFC800">区块储存数据</h2>
 * <p style="color:FFC800">
 * 此类用于解析被存储在 Anvil 文件格式 (*.mca) 中的区块数据而不需要使用 {@link ServerWorld}。
 * 并提供类似于 {@link ChunkSerializer} 中的解析方法，并提供一些包含在 {@link Chunk}
 * 中的部分基础方法以供判断或操控区块储存文件。
 * </p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@SilkApi
public final class ChunkStorageData {
	private final ChunkPos chunkPos;
	private final boolean isLightOn;
	private final long inhabitedTime;
	private final Map<Heightmap.Type, long[]> heightmaps;
	private final Map<ChunkSectionPos, ChunkSection> sections;
	private final List<NbtCompound> entities = Lists.newArrayList();
	private final Map<BlockPos, NbtCompound> blockEntityNbts = Maps.newHashMap();
	private final Map<Structure, StructureStart> structureStarts = Maps.newHashMap();
	private final Map<Structure, LongSet> structureReferences = Maps.newHashMap();
	
	private ChunkStorageData(ChunkPos chunkPos, Map<ChunkSectionPos, ChunkSection> sections, Map<Heightmap.Type, long[]> heightmaps,
	                         Map<Structure, StructureStart> structureStarts, Map<Structure, LongSet> structureReferences, List<NbtCompound> entities,
	                         Map<BlockPos, NbtCompound> blockEntityNbts, boolean isLightOn, long inhabitedTime) {
		this.chunkPos = chunkPos;
		this.sections = sections;
		this.heightmaps = heightmaps;
		this.structureStarts.putAll(structureStarts);
		this.structureReferences.putAll(structureReferences);
		this.entities.addAll(entities);
		this.blockEntityNbts.putAll(blockEntityNbts);
		this.isLightOn = isLightOn;
		this.inhabitedTime = inhabitedTime;
	}
	
	@SilkApi
	public static Builder builder(ChunkPos pos) {
		return new Builder(pos);
	}
	
	@SilkApi
	public static int blockToChunkCoord(int blockCoord) {
		return MaterialRules.MaterialRuleContext.blockToChunkCoord(blockCoord);
	}
	
	@SilkApi
	public static int chunkToBlockCoord(int chunkCoord) {
		return MaterialRules.MaterialRuleContext.chunkToBlockCoord(chunkCoord);
	}
	
	@SilkApi
	public static ChunkStorageData deserialize(SaveLoader saveLoader, LevelStorage.Session session, ChunkPos pos, NbtCompound nbt) {
		ChunkPos posInNbt = new ChunkPos(nbt.getInt(X_POS_KEY), nbt.getInt(Z_POS_KEY));
		if (!pos.equals(posInNbt)) Silk.DATA.logger().error("Chunk file at {} is in the wrong location; relocating. (Expected {}, got {})", pos, pos, posInNbt);
		NbtList sections = nbt.getList(SECTIONS_KEY, NbtElement.COMPOUND_TYPE);
		Registry<Biome> biomes = WorldUpgradeData.getRegistryManager().get(RegistryKeys.BIOME);
		Codec<ReadableContainer<RegistryEntry<Biome>>> biomeCodec = ChunkSerializer.createCodec(biomes);
		Map<ChunkSectionPos, ChunkSection> sectionMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (NbtElement element : sections) {
			NbtCompound section = (NbtCompound) element;
			PalettedContainer<BlockState> palettedContainer = section.contains("block_states", NbtElement.COMPOUND_TYPE)
					? CODEC.parse(NbtOps.INSTANCE, section.getCompound("block_states"))
					.promotePartial(errorMessage -> Silk.DATA.logger().error(errorMessage)).getOrThrow(false, Silk.DATA.logger()::error)
					: new PalettedContainer<>(Block.STATE_IDS, Blocks.AIR.getDefaultState(), PalettedContainer.PaletteProvider.BLOCK_STATE);
			ReadableContainer<RegistryEntry<Biome>> readableContainer = section.contains("biomes", NbtElement.COMPOUND_TYPE)
					? biomeCodec.parse(NbtOps.INSTANCE, section.getCompound("biomes"))
					.promotePartial(errorMessage -> Silk.DATA.logger().error(errorMessage)).getOrThrow(false, Silk.DATA.logger()::error)
					: new PalettedContainer<>(biomes.getIndexedEntries(), biomes.entryOf(BiomeKeys.PLAINS), PalettedContainer.PaletteProvider.BIOME);
			sectionMap.put(ChunkSectionPos.from(pos, section.getByte("Y")), new ChunkSection(palettedContainer, readableContainer));
		}
		NbtCompound heightmapsNbt = nbt.getCompound(HEIGHTMAPS_KEY);
		Map<Heightmap.Type, long[]> heightmaps = Maps.newHashMapWithExpectedSize(4);
		for (Heightmap.Type type : Heightmap.Type.values()) {
			String name = type.getName();
			if (heightmapsNbt.contains(name, NbtElement.LONG_ARRAY_TYPE)) heightmaps.put(type, heightmapsNbt.getLongArray(name));
		}
		NbtCompound structuresNbt = nbt.getCompound("structures");
		Map<Structure, StructureStart> structureStarts = ChunkSerializer.readStructureStarts(new StructureContext(WorldUpgradeData.getResourceManager(saveLoader),
				WorldUpgradeData.getRegistryManager(), WorldUpgradeData.getStructureTemplateManager(saveLoader, session)), structuresNbt, WorldUpgradeData.getSeed(saveLoader));
		Map<Structure, LongSet> structureReferences = ChunkSerializer.readStructureReferences(WorldUpgradeData.getRegistryManager(), pos, structuresNbt);
		List<NbtCompound> entities = Lists.newArrayList();
		for (NbtElement element : nbt.getList("entities", NbtElement.COMPOUND_TYPE))
			entities.add((NbtCompound) element);
		Map<BlockPos, NbtCompound> blockEntityNbts = Maps.newHashMap();
		for (NbtElement element : nbt.getList("block_entities", NbtElement.COMPOUND_TYPE))
			blockEntityNbts.put(BlockEntity.posFromNbt((NbtCompound) element), (NbtCompound) element);
		return builder(pos)
				.sections(sectionMap)
				.heightmaps(heightmaps)
				.structureStarts(structureStarts)
				.structureReferences(structureReferences)
				.entities(entities)
				.blockEntityNbts(blockEntityNbts)
				.isLightOn(nbt.getBoolean(IS_LIGHT_ON_KEY))
				.inhabitedTime(nbt.getLong("InhabitedTime"))
				.build();
	}
	
	@SilkApi
	public int getHeight() {
		final int chunkSize = 16;
		Map<Integer, ChunkSectionPos> sectionYMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (ChunkSectionPos sectionPos : sections.keySet()) sectionYMap.put(sectionPos.getSectionY(), sectionPos);
		int[] sectionYs = ArrayUtils.toPrimitive(sectionYMap.keySet().toArray(new Integer[0]));
		Arrays.sort(sectionYs);
		return (sectionYs[sectionYs.length - 1] * chunkSize) + 15;
	}
	
	@SilkApi
	public int getButton() {
		final int chunkSize = 16;
		Map<Integer, ChunkSectionPos> sectionYMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (ChunkSectionPos sectionPos : sections.keySet()) sectionYMap.put(sectionPos.getSectionY(), sectionPos);
		int[] sectionYs = ArrayUtils.toPrimitive(sectionYMap.keySet().toArray(new Integer[0]));
		Arrays.sort(sectionYs);
		return (sectionYs[0] * chunkSize) + 1;
	}
	
	@SilkApi
	public int getTotalHeight() {
		return getHeight() + Math.abs(getButton());
	}
	
	@SilkApi
	public ChunkSectionPos getSectionIndex(BlockPos pos) {
		final int chunkSize = 16;
		Map<Integer, ChunkSectionPos> sectionYMap = Maps.newHashMapWithExpectedSize(sections.size());
		for (ChunkSectionPos sectionPos : sections.keySet()) sectionYMap.put(sectionPos.getSectionY(), sectionPos);
		int[] sectionYs = ArrayUtils.toPrimitive(sectionYMap.keySet().toArray(new Integer[0]));
		Arrays.sort(sectionYs);
		return sectionYMap.get(pos.getY() / chunkSize < 0 ? (pos.getY() / chunkSize) - 1 : pos.getY() / chunkSize);
	}
	
	@SilkApi
	public @Nullable ChunkSection getSection(ChunkSectionPos pos) {
		return sections.get(pos);
	}
	
	@SilkApi
	public @Nullable RegistryEntry<Biome> getBiome(BlockPos pos) {
		ChunkSection section = getSection(getSectionIndex(pos));
		return null == section ? null : section.getBiome(
				BiomeCoords.fromBlock(pos.getX()) & 3,
				BiomeCoords.fromBlock(MathHelper.clamp(pos.getY(), getButton(), getHeight())) & 3,
				BiomeCoords.fromBlock(pos.getZ()) & 3);
	}
	
	@SilkApi
	@Nullable
	public StructureStart getStructureStart(Structure structure) {
		return structureStarts.get(structure);
	}
	
	@SilkApi
	public LongSet getStructureReferences(Structure structure) {
		return structureReferences.getOrDefault(structure, new LongOpenHashSet());
	}
	
	@SilkApi
	public long[] getHeightmap(Heightmap.Type type) {
		return heightmaps.get(type);
	}
	
	@SilkApi
	@Nullable
	public NbtCompound getBlockEntityNbt(BlockPos pos) {
		return blockEntityNbts.get(pos);
	}
	
	@SilkApi
	public ChunkPos getChunkPos() {
		return chunkPos;
	}
	
	@SilkApi
	public boolean getIsLightOn() {
		return isLightOn;
	}
	
	@SilkApi
	public long getInhabitedTime() {
		return inhabitedTime;
	}
	
	@SilkApi
	public List<NbtCompound> getEntities() {
		return entities;
	}
	
	
	@SilkApi
	public Map<Heightmap.Type, long[]> getHeightmaps() {
		return heightmaps;
	}
	
	@SilkApi
	public Map<BlockPos, NbtCompound> getBlockEntityNbts() {
		return blockEntityNbts;
	}
	
	@SilkApi
	public Map<ChunkSectionPos, ChunkSection> getSections() {
		return sections;
	}
	
	@SilkApi
	public Map<Structure, StructureStart> getStructureStarts() {
		return structureStarts;
	}
	
	@SilkApi
	public Map<Structure, LongSet> getStructureReferences() {
		return structureReferences;
	}
	
	public static final class Builder {
		private final ChunkPos pos;
		private List<NbtCompound> entities = Lists.newArrayList();
		private Map<BlockPos, NbtCompound> blockEntityNbts = Maps.newHashMap();
		private Map<Structure, StructureStart> structureStarts = Maps.newHashMap();
		private Map<Structure, LongSet> structureReferences = Maps.newHashMap();
		private Map<ChunkSectionPos, ChunkSection> sections;
		private Map<Heightmap.Type, long[]> heightmaps;
		private boolean isLightOn;
		private long inhabitedTime;
		
		private Builder(ChunkPos pos) {
			this.pos = pos;
		}
		
		private Builder sections(Map<ChunkSectionPos, ChunkSection> sections) {
			this.sections = sections;
			return this;
		}
		
		private Builder heightmaps(Map<Heightmap.Type, long[]> heightmaps) {
			this.heightmaps = heightmaps;
			return this;
		}
		
		private Builder structureStarts(Map<Structure, StructureStart> structureStarts) {
			this.structureStarts = structureStarts;
			return this;
		}
		
		private Builder structureReferences(Map<Structure, LongSet> structureReferences) {
			this.structureReferences = structureReferences;
			return this;
		}
		
		private Builder entities(List<NbtCompound> entities) {
			this.entities = entities;
			return this;
		}
		
		private Builder blockEntityNbts(Map<BlockPos, NbtCompound> blockEntityNbts) {
			this.blockEntityNbts = blockEntityNbts;
			return this;
		}
		
		private Builder isLightOn(boolean isLightOn) {
			this.isLightOn = isLightOn;
			return this;
		}
		
		private Builder inhabitedTime(long inhabitedTime) {
			this.inhabitedTime = inhabitedTime;
			return this;
		}
		
		private ChunkStorageData build() {
			return new ChunkStorageData(pos, sections, heightmaps, structureStarts, structureReferences,
					entities, blockEntityNbts, isLightOn, inhabitedTime);
		}
	}
}
