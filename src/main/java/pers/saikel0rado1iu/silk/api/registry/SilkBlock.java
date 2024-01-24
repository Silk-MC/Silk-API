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

package pers.saikel0rado1iu.silk.api.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">用于模组所有方块与注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#blocks()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkBlock {
	protected static <B extends Block> Builder<B> builder(B block) {
		return new Builder<>(block);
	}
	
	/**
	 * 你需要在 client 模块中重新创建继承 {@link SilkBlock} 类来创建一个专用于客户端的注册类并覆盖 ModClient.blocks()方法
	 */
	@SilkApi
	@Environment(EnvType.CLIENT)
	protected static <B extends Block> void clientRegister(B block, Consumer<B> clientRegister) {
		clientRegister.accept(block);
	}
	
	@SilkApi
	public static final class Builder<B extends Block> {
		private final B block;
		
		@SilkApi
		private Builder(B block) {
			this.block = block;
		}
		
		@SilkApi
		public Builder<B> otherRegister(Consumer<B> blockRegister) {
			blockRegister.accept(block);
			return this;
		}
		
		@SilkApi
		public B build(ModBasicData mod, String id) {
			Registry.register(Registries.BLOCK, new Identifier(mod.getId(), id), block);
			return block;
		}
	}
}
