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

package pers.saikel0rado1iu.silk.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <p><b style="color:FFC800"><font size="+1">用作模组数据生成主类，继承自 {@link DataGeneratorEntrypoint}。所有数据生成注册或操作由此开始</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public abstract class ModDataGeneration implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		datagen();
	}
	
	/**
	 * 最先运行的模组初始化函数
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public abstract void datagen();
	
	/**
	 * 提供来自模组的世界生成器以供注册、生成
	 */
	@SilkApi
	@ApiStatus.OverrideOnly
	public SilkWorldGenerator worldGen() {
		return null;
	}
	
	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		if (worldGen() != null) {
			registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, worldGen().configuredFeatures()::bootstrap);
			registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, worldGen().placedFeatures()::bootstrap);
		}
	}
}
