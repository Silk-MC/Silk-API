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
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Set;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有方块实体类型组成方块实体类型集与方块实体类型注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#blockEntities()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkBlockEntity {
	public static final Set<BlockEntityType<?>> ALL_MOD_BLOCK_ENTITY_TYPES = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static <E extends BlockEntity> Builder<E> builder(BlockEntityType<E> blockEntityType) {
		return new Builder<>(blockEntityType);
	}
	
	@SilkApi
	public static final class Builder<E extends BlockEntity> {
		private final BlockEntityType<E> blockEntityType;
		
		@SilkApi
		private Builder(BlockEntityType<E> blockEntityType) {
			ALL_MOD_BLOCK_ENTITY_TYPES.add(this.blockEntityType = blockEntityType);
		}
		
		@SilkApi
		public Builder<E> put(Set<BlockEntityType<?>> blockEntityType) {
			blockEntityType.add(this.blockEntityType);
			return this;
		}
		
		@SilkApi
		public BlockEntityType<E> build(ModBasicData mod, String id) {
			Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(mod.getId(), id), blockEntityType);
			return blockEntityType;
		}
	}
}
