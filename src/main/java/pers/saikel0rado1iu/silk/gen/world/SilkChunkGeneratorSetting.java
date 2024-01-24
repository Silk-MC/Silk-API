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

package pers.saikel0rado1iu.silk.gen.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

/**
 * <h2 style="color:FFC800">用于创建、注册、生成区块生成器设置</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldGenerator} 中覆盖 {@link SilkWorldGenerator#chunkGeneratorSettings()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkChunkGeneratorSetting {
	@ApiStatus.Internal
	public static final SilkChunkGeneratorSetting EMPTY = new SilkChunkGeneratorSetting() {
		@Override
		public void bootstrap(Registerable<ChunkGeneratorSettings> chunkGenerationSettingsRegisterable) {
		}
	};
	
	protected static RegistryKey<ChunkGeneratorSettings> register(ModBasicData mod, String id) {
		return RegistryKey.of(RegistryKeys.CHUNK_GENERATOR_SETTINGS, new Identifier(mod.getId(), id));
	}
	
	/**
	 * 参考 {@link ChunkGeneratorSettings#bootstrap(Registerable)}
	 */
	@ApiStatus.OverrideOnly
	public abstract void bootstrap(Registerable<ChunkGeneratorSettings> chunkGenerationSettingsRegisterable);
	
	@SilkApi
	public static class SilkGenerationShapeConfig extends GenerationShapeConfig {
		@SilkApi
		public static final GenerationShapeConfig SURFACE = GenerationShapeConfig.SURFACE;
		@SilkApi
		public static final GenerationShapeConfig NETHER = GenerationShapeConfig.NETHER;
		@SilkApi
		public static final GenerationShapeConfig END = GenerationShapeConfig.END;
		@SilkApi
		public static final GenerationShapeConfig CAVES = GenerationShapeConfig.CAVES;
		@SilkApi
		public static final GenerationShapeConfig FLOATING_ISLANDS = GenerationShapeConfig.FLOATING_ISLANDS;
		
		public SilkGenerationShapeConfig(int minimumY, int height, int i, int j) {
			super(minimumY, height, i, j);
		}
	}
}
