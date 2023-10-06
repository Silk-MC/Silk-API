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

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Set;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有方块组成方块集与方块注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#blocks()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkBlock {
	public static final Set<Block> ALL_MOD_BLOCKS = Sets.newLinkedHashSetWithExpectedSize(8);
	@ApiStatus.Internal
	public static final Set<Runnable> ALL_MOD_BLOCK_CLIENT_REGISTERS = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static Builder builder(Block block) {
		return new Builder(block);
	}
	
	@SilkApi
	public static final class Builder {
		private final Block block;
		
		@SilkApi
		private Builder(Block block) {
			ALL_MOD_BLOCKS.add(this.block = block);
		}
		
		@SilkApi
		public Builder put(Set<Block> blocks) {
			blocks.add(block);
			return this;
		}
		
		@SilkApi
		public Builder otherRegister(Consumer<Block> blockRegister) {
			blockRegister.accept(block);
			return this;
		}
		
		@SilkApi
		public Builder clientRegister(Consumer<Block> clientRegister) {
			ALL_MOD_BLOCK_CLIENT_REGISTERS.add(() -> clientRegister.accept(block));
			return this;
		}
		
		@SilkApi
		public Block build(ModBasicData mod, String id) {
			Registry.register(Registries.BLOCK, new Identifier(mod.getId(), id), block);
			return block;
		}
	}
}
