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

package pers.saikel0rado1iu.silk.generate.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.WorldPresets;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import pers.saikel0rado1iu.silk.generate.DynamicDataEntry;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">世界预设条目</h2>
 * 用于创建、注册、生成世界预设
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface WorldPresetEntry extends DynamicDataEntry<WorldPreset> {
	/**
	 * 获取世界预设
	 *
	 * @param modPass 模组通
	 * @param id      世界预设 ID
	 * @return 世界预设
	 */
	static RegistryKey<WorldPreset> of(ModPass modPass, String id) {
		return RegistryKey.of(RegistryKeys.WORLD_PRESET, modPass.modData().ofId(id));
	}
	
	@Override
	default RegistryKey<? extends Registry<WorldPreset>> registryKey() {
		return RegistryKeys.WORLD_PRESET;
	}
	
	/**
	 * 访问加宽的 {@link WorldPresets.Registrar}
	 */
	class Registrar extends WorldPresets.Registrar {
		/**
		 * @param presetRegisterable 可注册世界预设
		 */
		public Registrar(Registerable<WorldPreset> presetRegisterable) {
			super(presetRegisterable);
		}
		
		@Override
		public DimensionOptions createOverworldOptions(ChunkGenerator chunkGenerator) {
			return super.createOverworldOptions(chunkGenerator);
		}
		
		@Override
		public DimensionOptions createOverworldOptions(BiomeSource biomeSource, RegistryEntry<ChunkGeneratorSettings> chunkGeneratorSettings) {
			return super.createOverworldOptions(biomeSource, chunkGeneratorSettings);
		}
		
		@Override
		public WorldPreset createPreset(DimensionOptions dimensionOptions) {
			return super.createPreset(dimensionOptions);
		}
		
		@Override
		public void register(RegistryKey<WorldPreset> key, DimensionOptions dimensionOptions) {
			super.register(key, dimensionOptions);
		}
	}
}
