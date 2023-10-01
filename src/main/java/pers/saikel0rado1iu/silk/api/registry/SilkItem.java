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
import java.util.Set;
import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有物品组成物品集与物品注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#items()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkItem {
	public static final Set<Item> ALL_MOD_ITEMS = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static Builder builder(Item item) {
		return new Builder(item);
	}
	
	@SilkApi
	public static final class Builder {
		private final Item item;
		
		@SilkApi
		private Builder(Item item) {
			ALL_MOD_ITEMS.add(this.item = item);
		}
		
		@SilkApi
		@SafeVarargs
		public final Builder group(RegistryKey<ItemGroup>... groups) {
			Arrays.stream(groups).forEach(group -> ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(item)));
			return this;
		}
		
		@SilkApi
		public Builder put(Set<Item> items) {
			items.add(item);
			return this;
		}
		
		@SilkApi
		public Builder otherRegister(Consumer<Item> blockConsumer) {
			blockConsumer.accept(item);
			return this;
		}
		
		@SilkApi
		public Item build(ModBasicData mod, String id) {
			Registry.register(Registries.ITEM, new Identifier(mod.getId(), id), item);
			return item;
		}
	}
}
