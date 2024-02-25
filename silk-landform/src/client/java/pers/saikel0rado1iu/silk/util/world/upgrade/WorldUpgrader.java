/*
 * This worldFile is part of Silk API.
 * Copyright (C) 2023 Saikel Orado Liu
 *
 * Silk API is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Silk API is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Silk API. If not, see <https://www.gnu.org/licenses/>.
 */

package pers.saikel0rado1iu.silk.util.world.upgrade;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import it.unimi.dsi.fastutil.objects.Reference2FloatMaps;
import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.SaveLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.storage.RegionFile;
import net.minecraft.world.storage.StorageKey;
import net.minecraft.world.storage.VersionedChunkStorage;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.gen.world.chunk.UpgradeChunkGenerator;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.world.upgrade.data.ChunkStorageData;
import pers.saikel0rado1iu.silk.util.world.upgrade.data.WorldUpgradeData;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.ModWorldInfo;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.minecraft.world.ChunkSerializer.*;
import static pers.saikel0rado1iu.silk.util.world.upgrade.data.ChunkStorageData.chunkToBlockCoord;
import static pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary.TEXT_PREFIX;

/**
 * <h2 style="color:FFC800">世界升级器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@ApiStatus.Internal
public final class WorldUpgrader {
	private static final ThreadFactory UPDATE_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).build();
	private static final Pattern REGION_FILE_PATTERN = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");
	private final boolean eraseCache;
	private final Thread upgradeThread;
	private final SaveLoader saveLoader;
	private final UpgradeLevelSummary summary;
	private final LevelStorage.Session session;
	private final Set<RegistryKey<World>> worldKeys;
	private final Reference2FloatMap<RegistryKey<World>> dimensionProgress = Reference2FloatMaps.synchronize(new Reference2FloatOpenHashMap<>());
	private volatile boolean done;
	private volatile float progress;
	private volatile int totalChunkCount = 0;
	private volatile int deletedChunkCount = 0;
	private volatile int handledChunkCount = 0;
	private volatile boolean keepUpgradingChunks = true;
	private volatile Text status = Text.translatable("optimizeWorld.stage.counting");
	
	public WorldUpgrader(UpgradeLevelSummary summary, LevelStorage.Session session, SaveLoader saveLoader, Registry<DimensionOptions> dimensionOptionsRegistry, boolean eraseCache) {
		this.summary = summary;
		this.session = session;
		this.saveLoader = saveLoader;
		this.worldKeys = dimensionOptionsRegistry.getKeys().stream().map(RegistryKeys::toWorldKey).collect(Collectors.toUnmodifiableSet());
		this.eraseCache = eraseCache;
		this.upgradeThread = UPDATE_THREAD_FACTORY.newThread(this::upgradeWorld);
		this.upgradeThread.setUncaughtExceptionHandler((thread, throwable) -> {
			this.summary.getModWorldInfo().mod().logger().error("Error upgrading world", throwable);
			this.status = Text.translatable("optimizeWorld.stage.failed");
			this.done = true;
		});
		this.upgradeThread.start();
	}
	
	private static boolean canDeleteChunk(int type, SaveLoader saveLoader, LevelStorage.Session session, ChunkPos pos, NbtCompound nbt, UpgradeChunkGenerator generator, ModWorldInfo modWorldInfo) {
		final int chunkSize = 16;
		final int granularity = 4;
		ChunkStorageData chunk = ChunkStorageData.deserialize(saveLoader, session, pos, nbt);
		BlockPos blockPos = new BlockPos(chunkToBlockCoord(pos.x), 0, chunkToBlockCoord(pos.z));
		for (int offsetX = 0; offsetX < chunkSize; offsetX += granularity) {
			for (int offsetZ = 0; offsetZ < chunkSize; offsetZ += granularity) {
				for (int y = 0; y < chunk.getTotalHeight(); y += granularity) {
					BlockPos bPos = new BlockPos(blockPos.getX() + offsetX, y, blockPos.getZ() + offsetZ);
					if (switch (type) {
						case 1 -> generator.canUpgradeChunk(bPos, chunk, modWorldInfo.version());
						case -1 -> generator.canDowngradeChunk(bPos, chunk, modWorldInfo.version());
						default -> generator.canFlushChunk(bPos, chunk, modWorldInfo.version());
					}) return true;
				}
			}
		}
		return false;
	}
	
	private static List<ChunkPos> getChunkPositions(StorageKey key, Path regionDirectory) {
		File[] mcaFiles = regionDirectory.toFile().listFiles((file, name) -> name.endsWith(".mca"));
		if (mcaFiles == null) return ImmutableList.of();
		ArrayList<ChunkPos> list = Lists.newArrayList();
		for (File mcaFile : mcaFiles) {
			Matcher matcher = REGION_FILE_PATTERN.matcher(mcaFile.getName());
			if (!matcher.matches()) continue;
			int offsetX = Integer.parseInt(matcher.group(1)) << 5;
			int offsetZ = Integer.parseInt(matcher.group(2)) << 5;
			try (RegionFile region = new RegionFile(key, mcaFile.toPath(), regionDirectory, true)) {
				for (int x = 0; x < 32; ++x) {
					for (int z = 0; z < 32; ++z) {
						ChunkPos chunkPos = new ChunkPos(x + offsetX, z + offsetZ);
						if (!region.isChunkValid(chunkPos)) continue;
						list.add(chunkPos);
					}
				}
			} catch (Throwable ignored) {
			}
		}
		return list;
	}
	
	private VersionedChunkStorage openStorage(StorageKey storageKey, Path path) {
		return new VersionedChunkStorage(storageKey, path, null, true);
	}
	
	private <T extends ChunkGenerator & UpgradeChunkGenerator> void upgradeWorld() {
		// 获取 ChunkPos 与 ChunkStorage 的数据以供使用
		ImmutableMap.Builder<RegistryKey<World>, List<ChunkPos>> chunkPosMapBuilder = ImmutableMap.builder();
		ImmutableMap.Builder<RegistryKey<World>, VersionedChunkStorage> chunkStorageMapBuilder = ImmutableMap.builder();
		for (RegistryKey<World> world : worldKeys) {
			int temp = totalChunkCount;
			StorageKey storageKey = new StorageKey(session.getDirectoryName(), world, "chunk");
			Path path = session.getWorldDirectory(world).resolve("region");
			List<ChunkPos> list = getChunkPositions(storageKey, path);
			chunkStorageMapBuilder.put(world, openStorage(storageKey, path));
			chunkPosMapBuilder.put(world, list);
			totalChunkCount = temp + list.size();
		}
		ImmutableMap<RegistryKey<World>, List<ChunkPos>> chunkPosMap = chunkPosMapBuilder.build();
		ImmutableMap<RegistryKey<World>, VersionedChunkStorage> chunkStorageMap = chunkStorageMapBuilder.build();
		// 如果世界没有区块则完成升级
		if (totalChunkCount == 0) {
			done = true;
			return;
		}
		// 遍历每个维度世界进行判断
		long startTime = Util.getMeasuringTimeMs();
		if (summary.shouldUpgradeWorld()) status = Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "upgrading"));
		else if (summary.shouldDowngradeWorld()) status = Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "downgrading"));
		else status = Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "flushing"));
		for (RegistryKey<World> world : worldKeys) {
			if (!keepUpgradingChunks) return;
			progress = (float) (deletedChunkCount + handledChunkCount) / totalChunkCount;
			// 判断是否需要升级此维度并获取注册的区块生成器
			boolean quit = true;
			T chunkGenerator = null;
			for (WorldUpgradeData<?> worldUpgradeData : WorldUpgradeSystem.getWorldUpgradeData()) {
				if (worldUpgradeData.dimension != RegistryKeys.toDimensionKey(world)) continue;
				chunkGenerator = (T) worldUpgradeData.getGenerator(WorldUpgradeData.getRegistryManager());
				if (!Objects.equals(Registries.CHUNK_GENERATOR.getId(chunkGenerator.codec()), summary.getModWorldInfo().generatorId())) continue;
				quit = false;
			}
			if (quit) {
				int temp = handledChunkCount;
				handledChunkCount = temp + Objects.requireNonNullElse(chunkPosMap.get(world), List.of()).size();
				continue;
			}
			// 获取此维度区块数据
			List<ChunkPos> chunkPosList = chunkPosMap.get(world);
			VersionedChunkStorage chunkStorage = chunkStorageMap.get(world);
			if (null == chunkPosList || null == chunkStorage) continue;
			// 遍历每一个在此维度中的区块进行判断
			for (ChunkPos pos : chunkPosList) {
				if (!keepUpgradingChunks) return;
				// 获取区块 NBT
				NbtCompound nbt = chunkStorage.getNbt(pos).join().orElse(null);
				if (null == nbt) {
					int temp = handledChunkCount;
					handledChunkCount = temp + 1;
					continue;
				}
				// 判断 ChunkPos 是否和在 NBT 中储存的数据一致
				ChunkPos posInNbt = new ChunkPos(nbt.getInt(X_POS_KEY), nbt.getInt(Z_POS_KEY));
				if (!posInNbt.equals(pos)) Silk.DATA.logger().warn("Chunk {} has invalid position {}", pos, posInNbt);
				// 判断是否可以删除区块
				boolean canDelete;
				if (summary.shouldDowngradeWorld()) canDelete = canDeleteChunk(-1, saveLoader, session, pos, nbt, chunkGenerator, summary.getModWorldInfo());
				else if (summary.shouldUpgradeWorld()) canDelete = canDeleteChunk(1, saveLoader, session, pos, nbt, chunkGenerator, summary.getModWorldInfo());
				else canDelete = canDeleteChunk(0, saveLoader, session, pos, nbt, chunkGenerator, summary.getModWorldInfo());
				if (canDelete) {
					int temp = deletedChunkCount;
					deletedChunkCount = temp + 1;
					// 如果可以删除写入 null
					chunkStorage.setNbt(pos, null);
				} else {
					int temp = handledChunkCount;
					handledChunkCount = temp + 1;
					// 清空缓存数据
					if (eraseCache) {
						nbt.remove(HEIGHTMAPS_KEY);
						nbt.remove(IS_LIGHT_ON_KEY);
						NbtList nbtList = nbt.getList(SECTIONS_KEY, NbtElement.COMPOUND_TYPE);
						for (int count = 0; count < nbtList.size(); ++count) {
							NbtCompound lightNbt = nbtList.getCompound(count);
							lightNbt.remove(BLOCK_LIGHT_KEY);
							lightNbt.remove(SKY_LIGHT_KEY);
						}
					}
					VersionedChunkStorage.saveContextToNbt(nbt, world, chunkGenerator.getCodecKey());
					chunkStorage.setNbt(pos, nbt);
				}
				progress = (float) (deletedChunkCount + handledChunkCount) / totalChunkCount;
				dimensionProgress.put(world, (dimensionProgress.getFloat(world) * chunkPosList.size() + 1) / chunkPosList.size());
			}
		}
		// 结束升级
		if (!keepUpgradingChunks) return;
		status = Text.translatable("optimizeWorld.stage.finished");
		try {
			for (VersionedChunkStorage storage : chunkStorageMap.values()) storage.close();
			session.save(summary.getLevelInfo().getLevelName());
		} catch (IOException ignored) {
			Silk.DATA.logger().error("save failed");
		}
		Silk.DATA.logger().info("World upgrade finished after {} ms", Util.getMeasuringTimeMs() - startTime);
		done = true;
	}
	
	public void cancel() {
		keepUpgradingChunks = false;
		try {
			upgradeThread.join();
		} catch (InterruptedException ignored) {
		}
	}
	
	public boolean isDone() {
		return done;
	}
	
	public Set<RegistryKey<World>> getWorlds() {
		return worldKeys;
	}
	
	public float getProgress(RegistryKey<World> world) {
		return dimensionProgress.getFloat(world);
	}
	
	public float getProgress() {
		return progress;
	}
	
	public int getTotalChunkCount() {
		return totalChunkCount;
	}
	
	public int getDeletedChunkCount() {
		return deletedChunkCount;
	}
	
	public int getHandledChunkCount() {
		return handledChunkCount;
	}
	
	public Text getStatus() {
		return status;
	}
}
