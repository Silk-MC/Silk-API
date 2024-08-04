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

package pers.saikel0rado1iu.silk.api.client.landform;

import com.google.common.collect.Maps;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.SaveLoading;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.util.Util;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.api.client.event.landform.ClientWorldUpgradeManagerCallback;
import pers.saikel0rado1iu.silk.api.landform.UpgradableWorldData;
import pers.saikel0rado1iu.silk.api.landform.UpgradableWorldManager;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h2 style="color:FFC800">客户端可升级世界管理器</h2>
 * 客户端可升级世界管理器通过存档设置通知用户需要进行世界升级
 *
 * @param <T>区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class ClientUpgradableWorldManager<T extends ChunkGenerator & ChunkGeneratorUpgradable> extends UpgradableWorldManager<T> {
	private static final ScheduledExecutorService GET_MANAGER_POOL = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().daemon(true).build());
	private static final Map<String, DynamicRegistryManager.Immutable> SAVE_MANAGER_MAP = Maps.newHashMapWithExpectedSize(8);
	
	/**
	 * @param upgradableWorldData 可升级世界数据
	 */
	public ClientUpgradableWorldManager(UpgradableWorldData<T> upgradableWorldData) {
		super(upgradableWorldData);
	}
	
	protected static void logging(Exception e) {
		String msg = "The dependent parameters were not obtained before obtaining DynamicRegistryManager. This may be due to incorrect programming. Please report this to the Silk-API team.";
		SilkLandform.getInstance().logger().error(GET_MANAGER_ERROR_MSG, new IllegalStateException(msg, e));
	}
	
	/**
	 * 启动管理器获取
	 *
	 * @param levels 关卡列表
	 */
	public void start(LevelStorage.LevelList levels) {
		GET_MANAGER_POOL.schedule(new Thread(() -> {
			try (LevelStorage.Session session = MinecraftClient.getInstance().getLevelStorage().createSession(levels.levels().get(0).getRootPath())) {
				ResourcePackManager resourcePackManager = VanillaDataPackProvider.createManager(session);
				DataConfiguration dataConfiguration = new DataConfiguration(new DataPackSettings(new ArrayList<String>(resourcePackManager.getNames()), List.of()), FeatureFlags.FEATURE_MANAGER.getFeatureSet());
				SaveLoading.DataPacks dataPacks = new SaveLoading.DataPacks(resourcePackManager, dataConfiguration, false, true);
				SaveLoading.ServerConfig serverConfig = new SaveLoading.ServerConfig(dataPacks, CommandManager.RegistrationEnvironment.INTEGRATED, 2);
				REGISTRY_MANAGER_GETTER.set(SaveLoading.load(serverConfig, context -> new SaveLoading.LoadContext<>(null, context.dimensionsRegistryManager()),
						(resourceManager, dataPackContents, combinedDynamicRegistries, generatorOptions) -> {
							resourceManager.close();
							return combinedDynamicRegistries.getCombinedRegistryManager();
						}, Util.getMainWorkerExecutor(), (MinecraftClient.getInstance())));
				Objects.requireNonNull(REGISTRY_MANAGER_GETTER.get());
				MinecraftClient.getInstance().runTasks(REGISTRY_MANAGER_GETTER.get()::isDone);
			} catch (Exception e) {
				logging(e);
			}
		}), 0, TimeUnit.SECONDS);
	}
	
	/**
	 * 启动管理器获取
	 *
	 * @param session 关卡存储器会话
	 */
	public void start(LevelStorage.Session session) {
		REGISTRY_MANAGER_GETTER.set(null);
		if (SAVE_MANAGER_MAP.get(session.getDirectoryName()) != null) {
			REGISTRY_MANAGER_GETTER.set(CompletableFuture.completedFuture(SAVE_MANAGER_MAP.get(session.getDirectoryName())));
			return;
		}
		GET_MANAGER_POOL.schedule(new Thread(() -> {
			IntegratedServerLoader integratedServerLoader = MinecraftClient.getInstance().createIntegratedServerLoader();
			ResourcePackManager resourcePackManager = VanillaDataPackProvider.createManager(session);
			try (SaveLoader saveLoader = integratedServerLoader.load(session.readLevelProperties(), false, resourcePackManager)) {
				DynamicRegistryManager.Immutable manager = saveLoader.combinedDynamicRegistries().getCombinedRegistryManager();
				SAVE_MANAGER_MAP.put(session.getDirectoryName(), manager);
				REGISTRY_MANAGER_GETTER.set(CompletableFuture.completedFuture(manager));
			} catch (Exception e) {
				logging(e);
			}
		}), 0, TimeUnit.SECONDS);
	}
	
	@Override
	protected void operation() {
		ClientWorldUpgradeManagerCallback.EVENT.register(() -> this);
		ClientWorldUpgradeManagerCallback.EVENT.invoker().get();
	}
}
