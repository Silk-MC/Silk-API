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

import net.minecraft.datafixer.Schemas;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceManager;
import net.minecraft.server.SaveLoader;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.modpass.ModDataExpansion;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">可升级世界数据</h2>
 * 用于世界更新数据
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public record UpgradableWorldData<T extends ChunkGenerator & ChunkGeneratorUpgradable>
		(ModDataExpansion modPass, SettingData settings, RegistryKey<DimensionOptions> dimension,
		 Function<DynamicRegistryManager, T> getChunkGeneratorFunction) {
	/**
	 * @param modPass                   模组通
	 * @param worldUpgradeSettings      世界升级设置
	 * @param dimension                 维度
	 * @param getChunkGeneratorFunction 获取区块生成器函数
	 * @param <T>                       区块生成器类型
	 * @return 构建器
	 */
	public static <T extends ChunkGenerator & ChunkGeneratorUpgradable> Builder<T> builder(ModDataExpansion modPass, SettingData worldUpgradeSettings, RegistryKey<DimensionOptions> dimension, Function<DynamicRegistryManager, T> getChunkGeneratorFunction) {
		return new Builder<>(modPass, worldUpgradeSettings, dimension, getChunkGeneratorFunction);
	}
	
	/**
	 * 获取资源管理器
	 *
	 * @param saveLoader 存档加载器
	 * @return 资源管理器
	 */
	public static ResourceManager getResourceManager(SaveLoader saveLoader) {
		return saveLoader.resourceManager();
	}
	
	/**
	 * 获取结构模板管理器
	 *
	 * @param saveLoader 存档加载器
	 * @param session    关卡存储器会话
	 * @return 结构模板管理器
	 */
	public static StructureTemplateManager getStructureTemplateManager(SaveLoader saveLoader, LevelStorage.Session session) {
		return new StructureTemplateManager(getResourceManager(saveLoader), session, Schemas.getFixer(), UpgradableWorldManager.registryManager().createRegistryLookup().getOrThrow(RegistryKeys.BLOCK));
	}
	
	/**
	 * 获取存档种子
	 *
	 * @param saveLoader 存档加载器
	 * @return 存档种子
	 */
	public static long getSeed(SaveLoader saveLoader) {
		return saveLoader.saveProperties().getGeneratorOptions().getSeed();
	}
	
	/**
	 * 获取生成器
	 *
	 * @param registryManager 动态注册表管理器
	 * @return 区块生成器
	 */
	public T getGenerator(DynamicRegistryManager registryManager) {
		return getChunkGeneratorFunction.apply(registryManager);
	}
	
	/**
	 * 可升级世界数据的构建器
	 */
	public static final class Builder<T extends ChunkGenerator & ChunkGeneratorUpgradable> {
		private final ModDataExpansion modPass;
		private final SettingData worldUpgradeSettings;
		private final RegistryKey<DimensionOptions> dimension;
		private final Function<DynamicRegistryManager, T> getChunkGeneratorFunction;
		
		private Builder(ModDataExpansion modPass, SettingData worldUpgradeSettings, RegistryKey<DimensionOptions> dimension, Function<DynamicRegistryManager, T> getChunkGeneratorFunction) {
			SettingData worldUpgradeData = WorldUpgradeSettings.of(modPass, worldUpgradeSettings);
			worldUpgradeSettings.addOption(WorldUpgradeSettings.WORLD_UPGRADE_SETTINGS, worldUpgradeData);
			this.modPass = modPass;
			this.worldUpgradeSettings = worldUpgradeData;
			this.dimension = dimension;
			this.getChunkGeneratorFunction = getChunkGeneratorFunction;
		}
		
		/**
		 * 构建可升级世界数据
		 *
		 * @return 可升级世界数据
		 */
		public UpgradableWorldData<T> build() {
			return new UpgradableWorldData<>(modPass, worldUpgradeSettings, dimension, getChunkGeneratorFunction);
		}
	}
}
