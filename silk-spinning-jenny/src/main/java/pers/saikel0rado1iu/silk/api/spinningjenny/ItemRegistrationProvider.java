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

package pers.saikel0rado1iu.silk.api.spinningjenny;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.base.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.base.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;

import java.util.Arrays;
import java.util.Optional;

/**
 * <h2 style="color:FFC800">物品注册提供器</h2>
 * 用于整合物品并注册物品以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ServerRegistration(registrar = ItemRegistrationProvider.SERVER_REGISTRAR, type = ItemRegistrationProvider.TYPE)
@ClientRegistration(registrar = ItemRegistrationProvider.CLIENT_REGISTRAR, type = ItemRegistrationProvider.TYPE)
interface ItemRegistrationProvider extends MainRegistrationProvider<Item>, ClientRegistrationProvider<Item> {
	String SERVER_REGISTRAR = "pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistrationProvider.MainRegistrar";
	String CLIENT_REGISTRAR = "pers.saikel0rado1iu.silk.api.spinningjenny.ItemRegistrationProvider.ClientRegistrar";
	String TYPE = "net.minecraft.item.Item";
	
	/**
	 * 物品主注册器
	 *
	 * @param <T> 物品类型
	 */
	final class MainRegistrar<T extends Item> extends MainRegistrationProvider.Registrar<T, MainRegistrar<T>> {
		MainRegistrar(T type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Optional<Registry<?>> registry() {
			return Optional.of(Registries.ITEM);
		}
		
		/**
		 * 注册物品组
		 *
		 * @param groups 物品组
		 * @return 注册器
		 */
		@SafeVarargs
		public final MainRegistrar<T> group(RegistryKey<ItemGroup>... groups) {
			Arrays.stream(groups).forEach(group -> ItemGroupEvents.modifyEntriesEvent(group).register(content -> content.add(type)));
			return this;
		}
		
	}
	
	/**
	 * 物品客户端注册器
	 *
	 * @param <T> 物品类型
	 */
	final class ClientRegistrar<T extends Item> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Runnable run) {
			super(run);
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return Registries.ITEM.getId(t);
		}
	}
}
