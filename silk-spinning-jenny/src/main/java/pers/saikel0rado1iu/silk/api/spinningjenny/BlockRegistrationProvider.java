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

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.api.annotation.ClientRegistration;
import pers.saikel0rado1iu.silk.api.annotation.ServerRegistration;
import pers.saikel0rado1iu.silk.api.modpass.registry.ClientRegistrationProvider;
import pers.saikel0rado1iu.silk.api.modpass.registry.MainRegistrationProvider;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">方块注册提供器</h2>
 * 用于整合方块并注册方块以供使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ApiStatus.OverrideOnly
@ServerRegistration(registrar = BlockRegistrationProvider.MainRegistrar.class, type = Block.class)
@ClientRegistration(registrar = BlockRegistrationProvider.ClientRegistrar.class, type = Block.class)
public interface BlockRegistrationProvider extends MainRegistrationProvider<Block>, ClientRegistrationProvider<Block> {
	/**
	 * 方块主注册器
	 *
	 * @param <T> 方块类型
	 */
	final class MainRegistrar<T extends Block> extends MainRegistrationProvider.Registrar<T, MainRegistrar<T>> {
		MainRegistrar(Supplier<T> type) {
			super(type);
		}
		
		@Override
		protected MainRegistrar<T> self() {
			return this;
		}
		
		@Override
		protected Optional<Registry<?>> registry() {
			return Optional.of(Registries.BLOCK);
		}
	}
	
	/**
	 * 方块客户端注册器
	 *
	 * @param <T> 方块类型
	 */
	final class ClientRegistrar<T extends Block> extends ClientRegistrationProvider.Registrar<T> {
		ClientRegistrar(Runnable run) {
			super(run);
		}
		
		@Override
		protected Identifier getIdentifier(T t) {
			return Registries.BLOCK.getId(t);
		}
	}
}
