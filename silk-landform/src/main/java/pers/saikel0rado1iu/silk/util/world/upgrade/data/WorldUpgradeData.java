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
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModExtendedData;
import pers.saikel0rado1iu.silk.gen.world.chunk.UpgradeChunkGenerator;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">用于世界更新数据</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@SilkApi
public final class WorldUpgradeData<T extends ChunkGenerator & UpgradeChunkGenerator> {
	@ApiStatus.Internal
	private static DynamicRegistryManager.Immutable registryManager;
	@ApiStatus.Internal
	public final ModExtendedData mod;
	@ApiStatus.Internal
	public final RegistryKey<DimensionOptions> dimension;
	private final Function<DynamicRegistryManager.Immutable, T> getChunkGeneratorFunction;
	
	public WorldUpgradeData(ModExtendedData mod, RegistryKey<DimensionOptions> dimension, Function<DynamicRegistryManager.Immutable, T> getChunkGeneratorFunction) {
		this.mod = mod;
		this.dimension = dimension;
		this.getChunkGeneratorFunction = getChunkGeneratorFunction;
	}
	
	@ApiStatus.Internal
	public static DynamicRegistryManager.Immutable getRegistryManager() {
		return registryManager;
	}
	
	@ApiStatus.Internal
	public static void setRegistryManager(DynamicRegistryManager.Immutable registryManager) {
		WorldUpgradeData.registryManager = registryManager;
	}
	
	@ApiStatus.Internal
	public static ResourceManager getResourceManager(SaveLoader saveLoader) {
		return saveLoader.resourceManager();
	}
	
	@ApiStatus.Internal
	public static StructureTemplateManager getStructureTemplateManager(SaveLoader saveLoader, LevelStorage.Session session) {
		return new StructureTemplateManager(getResourceManager(saveLoader), session, Schemas.getFixer(), registryManager.createRegistryLookup().getOrThrow(RegistryKeys.BLOCK));
	}
	
	@ApiStatus.Internal
	public static long getSeed(SaveLoader saveLoader) {
		return saveLoader.saveProperties().getGeneratorOptions().getSeed();
	}
	
	@ApiStatus.Internal
	public T getGenerator(DynamicRegistryManager.Immutable registryManager) {
		return getChunkGeneratorFunction.apply(registryManager);
	}
}
