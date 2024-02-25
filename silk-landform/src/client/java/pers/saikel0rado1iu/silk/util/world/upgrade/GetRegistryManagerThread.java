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

package pers.saikel0rado1iu.silk.util.world.upgrade;

import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.util.world.upgrade.data.WorldUpgradeData;

/**
 * <h2 style="color:FFC800">用于获取注册表管理器线程</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@ApiStatus.Internal
public final class GetRegistryManagerThread extends Thread {
	private static GetRegistryManagerThread thread;
	private final LevelStorage.LevelList levels;
	private final LevelStorage.Session session;
	
	@ApiStatus.Internal
	public GetRegistryManagerThread(LevelStorage.Session session) {
		this.session = session;
		this.levels = null;
	}
	
	@ApiStatus.Internal
	public GetRegistryManagerThread(LevelStorage.LevelList levels) {
		this.levels = levels;
		this.session = null;
	}
	
	@ApiStatus.Internal
	public static void startGetRegistryManager(LevelStorage.Session session) {
		if (thread != null) return;
		thread = new GetRegistryManagerThread(session);
		thread.start();
	}
	
	@ApiStatus.Internal
	public static void startGetRegistryManager(LevelStorage.LevelList levels) {
		if (thread != null) return;
		thread = new GetRegistryManagerThread(levels);
		thread.start();
	}
	
	@ApiStatus.Internal
	public static void joinGet() {
		try {
			if (thread != null) thread.join();
		} catch (InterruptedException ignored) {
		}
	}
	
	@Override
	public void run() {
		DynamicRegistryManager.Immutable registryManager = null;
		if (session != null) {
			IntegratedServerLoader integratedServerLoader = MinecraftClient.getInstance().createIntegratedServerLoader();
			try (SaveLoader saveLoader = integratedServerLoader.createSaveLoader(session, false)) {
				registryManager = saveLoader.combinedDynamicRegistries().getCombinedRegistryManager();
			} catch (Exception ignored) {
			}
		}
		if (levels != null) {
			IntegratedServerLoader integratedServerLoader = MinecraftClient.getInstance().createIntegratedServerLoader();
			try (LevelStorage.Session session = MinecraftClient.getInstance().getLevelStorage().createSession(levels.levels().get(0).getRootPath());
			     SaveLoader saveLoader = integratedServerLoader.createSaveLoader(session, false)) {
				registryManager = saveLoader.combinedDynamicRegistries().getCombinedRegistryManager();
			} catch (Exception ignored) {
			}
		}
		WorldUpgradeData.setRegistryManager(registryManager);
	}
}
