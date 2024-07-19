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

package pers.saikel0rado1iu.silk.spinningjenny.world.gen.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

/**
 * <h2 style="color:FFC800">{@link Codec}{@code <? extends }{@link ChunkGenerator}{@code >} 注册表</h2>
 * 用于注册 {@link Codec}{@code <? extends }{@link ChunkGenerator}{@code >} 的注册表
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ChunkGeneratorCodecRegistry extends MainRegistrationProvider<Codec<? extends ChunkGenerator>> {
	/**
	 * 服务端注册方法<br>
	 * 此方法方法一个服务端注册器，注册器注册返回注册对象<br>
	 *
	 * @param ignored 仅用于标记泛型
	 * @param codec   注册对象
	 * @param <T>     区块生成器类型
	 * @return 服务端注册器
	 */
	static <T extends ChunkGenerator> ChunkGeneratorCodecRegistry.MainRegistrar<T> registrar(Class<T> ignored, Codec<T> codec) {
		return new ChunkGeneratorCodecRegistry.MainRegistrar<>(codec);
	}
	
	/**
	 * 区块生成器解编码器主注册器
	 *
	 * @param <T> 区块生成器解编码器
	 */
	final class MainRegistrar<T extends ChunkGenerator> extends Registrar<Codec<T>, MainRegistrar<T>> {
		MainRegistrar(Codec<T> type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Registry<?> registry() {
			return Registries.CHUNK_GENERATOR;
		}
		
	}
}
