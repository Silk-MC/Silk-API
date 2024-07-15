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

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <h2 style="color:FFC800">可升级世界管理器</h2>
 * 通过管理器来注册有关可升级世界的相关操作
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class UpgradableWorldManager<T extends ChunkGenerator & ChunkGeneratorUpgradable> extends Thread implements ModPass {
	protected static final AtomicReference<DynamicRegistryManager> REGISTRY_MANAGER = new AtomicReference<>();
	protected static final String GET_MANAGER_ERROR_MSG = "Special Error: An issue occurred while obtaining the DynamicRegistryManager.";
	private static final ScheduledExecutorService WORLD_UPGRADE_MANAGER_POOL = new ScheduledThreadPoolExecutor(1,
			new BasicThreadFactory.Builder().daemon(true).build());
	private final UpgradableWorldData<T> upgradableWorldData;
	
	protected UpgradableWorldManager(UpgradableWorldData<T> upgradableWorldData) {
		this.upgradableWorldData = upgradableWorldData;
	}
	
	/**
	 * 运行方法<br>
	 * 启动可升级世界管理器
	 *
	 * @param manager 可升级世界管理器
	 */
	public static void run(UpgradableWorldManager<?> manager) {
		WORLD_UPGRADE_MANAGER_POOL.schedule(manager, 0, TimeUnit.SECONDS);
	}
	
	/**
	 * 获取注册表管理器
	 *
	 * @return 动态注册表管理器
	 */
	public static DynamicRegistryManager registryManager() {
		return REGISTRY_MANAGER.get();
	}
	
	/**
	 * 获取可升级世界数据
	 *
	 * @return 可升级世界数据
	 */
	public UpgradableWorldData<T> upgradableWorldData() {
		return upgradableWorldData;
	}
	
	/**
	 * 汇合获取方法
	 */
	public synchronized void joinGet() {
		while (REGISTRY_MANAGER.get() == null) Thread.yield();
	}
	
	@Override
	public void run() {
		super.run();
		operation();
	}
	
	@Override
	public ModData modData() {
		return upgradableWorldData.modPass();
	}
	
	protected abstract void operation();
}
