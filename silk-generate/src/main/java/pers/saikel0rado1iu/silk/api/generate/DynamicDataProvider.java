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

package pers.saikel0rado1iu.silk.api.generate;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * <h2 style="color:FFC800">动态数据提供器</h2>
 * 继承自 {@link FabricDynamicRegistryProvider}
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class DynamicDataProvider extends FabricDynamicRegistryProvider {
	private final ModPass modPass;
	private final Set<DynamicDataEntry<?>> dataEntries;
	
	/**
	 * @param output           Fabric 数据输出
	 * @param registriesFuture 注册管理器
	 * @param modPass          模组数据
	 * @param dataEntries      数据条目
	 */
	public DynamicDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, ModPass modPass, Set<DynamicDataEntry<?>> dataEntries) {
		super(output, registriesFuture);
		this.modPass = modPass;
		this.dataEntries = dataEntries;
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
		dynamicRegistry(registries, entries);
		dataEntries.forEach(entry -> entry.configure(registries, entries));
	}
	
	/**
	 * 声明需要动态注册的项目
	 */
	abstract void dynamicRegistry(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries);
	
	@Override
	public String getName() {
		return modPass.modData().id();
	}
	
	static final class Simple extends DynamicDataProvider {
		Simple(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, ModPass modPass, Set<DynamicDataEntry<?>> dataEntries) {
			super(output, registriesFuture, modPass, dataEntries);
		}
		
		static Simple create(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, ModPass modPass, Set<DynamicDataEntry<?>> dataEntries) {
			return new Simple(output, registriesFuture, modPass, dataEntries);
		}
		
		/**
		 * 声明需要动态注册的项目
		 */
		@Override
		void dynamicRegistry(RegistryWrapper.WrapperLookup registries, Entries entries) {
		}
	}
}
