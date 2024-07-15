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

package pers.saikel0rado1iu.silk.landform;

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
import net.minecraft.world.storage.VersionedChunkStorage;
import pers.saikel0rado1iu.silk.common.util.TickUtil;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static net.minecraft.world.ChunkSerializer.*;
import static pers.saikel0rado1iu.silk.landform.ChunkStorageData.chunkToBlockCoord;

/**
 * <h2 style="color:FFC800">世界升级器</h2>
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public final class WorldUpgrader<T extends ChunkGenerator & ChunkGeneratorUpgradable> {
	private static final ThreadFactory UPDATE_THREAD_FACTORY = new ThreadFactoryBuilder().setDaemon(true).build();
	private static final Pattern REGION_FILE_PATTERN = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");
	private final boolean eraseCache;
	private final Thread upgradeThread;
	private final SaveLoader saveLoader;
	private final UpgradableWorldInfo<T> info;
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
	
	/**
	 * @param info                     可升级世界信息
	 * @param session                  关卡存储器会话
	 * @param saveLoader               存档加载器
	 * @param dimensionOptionsRegistry 维度选项注册表
	 * @param eraseCache               是否需要擦除缓存
	 */
	public WorldUpgrader(UpgradableWorldInfo<T> info, LevelStorage.Session session, SaveLoader saveLoader, Registry<DimensionOptions> dimensionOptionsRegistry, boolean eraseCache) {
		this.info = info;
		this.session = session;
		this.saveLoader = saveLoader;
		this.worldKeys = dimensionOptionsRegistry.getKeys().stream().map(RegistryKeys::toWorldKey).collect(Collectors.toUnmodifiableSet());
		this.eraseCache = eraseCache;
		this.upgradeThread = UPDATE_THREAD_FACTORY.newThread(this::upgradeWorld);
		this.upgradeThread.setUncaughtExceptionHandler((thread, throwable) -> {
			this.info.mod().logger().error("Error upgrading world", throwable);
			this.status = Text.translatable("optimizeWorld.stage.failed");
			this.done = true;
		});
		this.upgradeThread.start();
	}
	
	private static boolean canDeleteChunk(int type, SaveLoader saveLoader, LevelStorage.Session session, ChunkPos pos, NbtCompound nbt, ChunkGeneratorUpgradable generator, UpgradableWorldInfo<?> info) {
		final int chunkSize = 16;
		final int granularity = WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY.getValue().apply(info.data().settings().getValue(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY));
		ChunkStorageData chunk = ChunkStorageData.deserialize(saveLoader, session, pos, nbt);
		if (chunk.inhabitedTime() >= TickUtil.getTick(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME.getValue().apply(info.data().settings().getValue(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME)), TickUtil.Type.NATURAL, TimeUnit.MINUTES)) {
			return false;
		}
		BlockPos blockPos = new BlockPos(chunkToBlockCoord(pos.x), 0, chunkToBlockCoord(pos.z));
		for (int offsetX = 0; offsetX < chunkSize; offsetX += granularity) {
			for (int offsetZ = 0; offsetZ < chunkSize; offsetZ += granularity) {
				for (int y = 0; y < chunk.totalHeight(); y += granularity) {
					BlockPos bPos = new BlockPos(blockPos.getX() + offsetX, y, blockPos.getZ() + offsetZ);
					if (switch (type) {
						case 1 -> generator.needUpgrade(bPos, chunk, info.version());
						case -1 -> generator.needDowngrade(bPos, chunk, info.version());
						default -> generator.needRefresh(bPos, chunk, info.version());
					}) return true;
				}
			}
		}
		return false;
	}
	
	private static List<ChunkPos> getChunkPositions(LevelStorage.Session session, RegistryKey<World> world) {
		File worldFile = session.getWorldDirectory(world).toFile();
		File regionFile = new File(worldFile, "region");
		File[] mcaFiles = regionFile.listFiles((directory, name) -> name.endsWith(".mca"));
		if (mcaFiles == null) return ImmutableList.of();
		ArrayList<ChunkPos> list = Lists.newArrayList();
		for (File mcaFile : mcaFiles) {
			Matcher matcher = REGION_FILE_PATTERN.matcher(mcaFile.getName());
			if (!matcher.matches()) continue;
			int offsetX = Integer.parseInt(matcher.group(1)) << 5;
			int offsetZ = Integer.parseInt(matcher.group(2)) << 5;
			try (RegionFile region = new RegionFile(mcaFile.toPath(), regionFile.toPath(), true)) {
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
	
	private void upgradeWorld() {
		// 获取 ChunkPos 与 ChunkStorage 的数据以供使用
		ImmutableMap.Builder<RegistryKey<World>, List<ChunkPos>> chunkPosMapBuilder = ImmutableMap.builder();
		ImmutableMap.Builder<RegistryKey<World>, VersionedChunkStorage> chunkStorageMapBuilder = ImmutableMap.builder();
		for (RegistryKey<World> world : worldKeys) {
			int temp = totalChunkCount;
			List<ChunkPos> list = getChunkPositions(session, world);
			chunkPosMapBuilder.put(world, list);
			Path path = session.getWorldDirectory(world);
			chunkStorageMapBuilder.put(world, new VersionedChunkStorage(path.resolve("region"), null, true));
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
		if (info.shouldUpgradeWorld()) status = WidgetTexts.text(SilkLandform.getInstance(), "upgrading");
		else if (info.shouldDowngradeWorld()) status = WidgetTexts.text(SilkLandform.getInstance(), "downgrading");
		else status = WidgetTexts.text(SilkLandform.getInstance(), "refreshing");
		for (RegistryKey<World> world : worldKeys) {
			if (!keepUpgradingChunks) return;
			progress = (float) (deletedChunkCount + handledChunkCount) / totalChunkCount;
			// 判断是否需要升级此维度并获取注册的区块生成器
			boolean quit = true;
			T chunkGenerator = info.data().getGenerator(UpgradableWorldManager.registryManager());
			if (info.generatorId().equals(Registries.CHUNK_GENERATOR.getId(chunkGenerator.getCodec()))) quit = false;
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
				if (!posInNbt.equals(pos)) SilkLandform.getInstance().logger().warn("Chunk {} has invalid position {}", pos, posInNbt);
				// 判断是否可以删除区块
				boolean canDelete;
				if (info.shouldDowngradeWorld()) canDelete = canDeleteChunk(-1, saveLoader, session, pos, nbt, chunkGenerator, info);
				else if (info.shouldUpgradeWorld()) canDelete = canDeleteChunk(1, saveLoader, session, pos, nbt, chunkGenerator, info);
				else canDelete = canDeleteChunk(0, saveLoader, session, pos, nbt, chunkGenerator, info);
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
			session.save(info.levelName());
		} catch (IOException ignored) {
			SilkLandform.getInstance().logger().error("save failed");
		}
		SilkLandform.getInstance().logger().info("World upgrade finished after {} ms", Util.getMeasuringTimeMs() - startTime);
		done = true;
	}
	
	/**
	 * 取消方法
	 */
	public void cancel() {
		keepUpgradingChunks = false;
		try {
			upgradeThread.join();
		} catch (InterruptedException ignored) {
		}
	}
	
	/**
	 * 是否完成
	 *
	 * @return 是否已完成
	 */
	public synchronized boolean isDone() {
		return done;
	}
	
	/**
	 * 获取所有世界
	 *
	 * @return 世界注册键集合
	 */
	public synchronized Set<RegistryKey<World>> worlds() {
		return worldKeys;
	}
	
	/**
	 * 获取世界的处理进度
	 *
	 * @param world 世界注册键
	 * @return 进度
	 */
	public synchronized float progress(RegistryKey<World> world) {
		return dimensionProgress.getFloat(world);
	}
	
	/**
	 * 获取总进度
	 *
	 * @return 总进度
	 */
	public synchronized float progress() {
		return progress;
	}
	
	/**
	 * 获取总区块计数
	 *
	 * @return 总区块计数
	 */
	public synchronized int totalChunkCount() {
		return totalChunkCount;
	}
	
	/**
	 * 获取已删除的区块计数
	 *
	 * @return 已删除的区块计数
	 */
	public synchronized int deletedChunkCount() {
		return deletedChunkCount;
	}
	
	/**
	 * 获取已处理的区块计数
	 *
	 * @return 已处理的区块计数
	 */
	public synchronized int handledChunkCount() {
		return handledChunkCount;
	}
	
	/**
	 * 获取状态
	 *
	 * @return 状态
	 */
	public synchronized Text status() {
		return status;
	}
}
