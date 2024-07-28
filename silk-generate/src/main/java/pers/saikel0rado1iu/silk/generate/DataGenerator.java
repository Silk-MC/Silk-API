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

package pers.saikel0rado1iu.silk.generate;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.modpass.ModDataExpansion;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * <h2 style="color:FFC800">数据生成器主类</h2>
 * 继承自 {@link DataGeneratorEntrypoint}。所有数据生成由此开始
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface DataGenerator extends DataGeneratorEntrypoint, ModPass {
	@Override
	default void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
		FabricDataGenerator.Pack builtinPack = dataGenerator.createPack();
		Optional<FabricDataGenerator.Pack> dataPack = Optional.empty();
		Optional<FabricDataGenerator.Pack> resourcePack = Optional.empty();
		if (modData() instanceof ModDataExpansion modData) {
			if (modData.dataPack().isPresent()) dataPack = Optional.of(dataGenerator.createBuiltinResourcePack(modData.dataPack().get().id()));
			if (modData.resourcePack().isPresent()) resourcePack = Optional.of(dataGenerator.createBuiltinResourcePack(modData.resourcePack().get().id()));
		}
		if (dataPack.isPresent()) dataPack.get().addProvider(this::dynamicDataProvider);
		else builtinPack.addProvider(this::dynamicDataProvider);
		generate(builtinPack, dataPack, resourcePack);
	}
	
	/**
	 * 数据生成方法
	 *
	 * @param builtinPack  Fabric 模组的内置包
	 * @param dataPack     模组的数据包
	 * @param resourcePack 模组的资源包
	 */
	void generate(FabricDataGenerator.Pack builtinPack, Optional<FabricDataGenerator.Pack> dataPack, Optional<FabricDataGenerator.Pack> resourcePack);
	
	/**
	 * 动态注册表提供器
	 *
	 * @param output           Fabric 数据输出
	 * @param registriesFuture 注册管理器
	 * @return 动态数据提供器
	 */
	default DynamicDataProvider dynamicDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		return dynamicDataProvider(output, registriesFuture, this, dynamicDataEntries());
	}
	
	/**
	 * 动态注册表提供器
	 *
	 * @param output           Fabric 数据输出
	 * @param registriesFuture 注册管理器
	 * @param modPass          模组通
	 * @param dataEntries      数据条目
	 * @return 动态数据提供器
	 */
	default DynamicDataProvider dynamicDataProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, ModPass modPass, Set<DynamicDataEntry<?>> dataEntries) {
		return DynamicDataProvider.Simple.create(output, registriesFuture, modPass, dataEntries);
	}
	
	/**
	 * 动态数据条目表
	 *
	 * @return 列表动态数据集合
	 */
	Set<DynamicDataEntry<?>> dynamicDataEntries();
	
	@Override
	@Nullable
	default String getEffectiveModId() {
		return modData().id();
	}
	
	@Override
	default void buildRegistry(RegistryBuilder registryBuilder) {
		dynamicDataEntries().forEach(entry -> entry.register(registryBuilder));
	}
}
