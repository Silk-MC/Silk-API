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
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">用于模组所有物品与注册</h2>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#items()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkItem {
	protected static <I extends Item> Builder<I> builder(I item) {
		return new Builder<>(item);
	}
	
	/**
	 * 你需要在 client 模块中重新创建继承 {@link SilkItem} 类来创建一个专用于客户端的注册类并覆盖 ModClient.items()方法
	 */
	@SilkApi
	@Environment(EnvType.CLIENT)
	protected static <I extends Item> void clientRegister(I item, Consumer<I> clientRegister) {
		clientRegister.accept(item);
	}
	
	@SilkApi
	public static final class Builder<I extends Item> {
		private final I item;
		
		@SilkApi
		private Builder(I item) {
			this.item = item;
		}
		
		@SilkApi
		@SafeVarargs
		public final Builder<I> group(RegistryKey<ItemGroup>... groups) {
			Arrays.stream(groups).forEach(group -> ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item)));
			return this;
		}
		
		@SilkApi
		public Builder<I> otherRegister(Consumer<I> itemRegister) {
			itemRegister.accept(item);
			return this;
		}
		
		@SilkApi
		public I build(ModBasicData mod, String id) {
			Registry.register(Registries.ITEM, new Identifier(mod.getId(), id), item);
			return item;
		}
	}
}
