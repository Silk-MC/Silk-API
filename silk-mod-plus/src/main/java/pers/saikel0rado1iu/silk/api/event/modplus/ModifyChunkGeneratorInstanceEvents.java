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

package pers.saikel0rado1iu.silk.api.event.modplus;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.world.gen.WorldPreset;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import pers.saikel0rado1iu.silk.api.generate.world.WorldPresetEntry;

/**
 * <h2 style="color:FFC800">修改区块生成器实例事件</h2>
 * 用于动态修改模组区块生成器的事件<br>
 * 需要模组作者自主在模组的区块生成器实例获取方法中实现事件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyChunkGeneratorInstanceEvents {
	/**
	 * 此事件将修改区块生成器中的数据生成实例方法
	 */
	Event<ModifyDataGenInstance> MODIFY_DATA_GEN_INSTANCE = EventFactory.createArrayBacked(ModifyDataGenInstance.class, listeners -> new ModifyDataGenInstance() {
		@Override
		public <T extends ChunkGenerator> T getInstance(T originGenerator, DynamicRegistryManager registryManager) {
			T generator = originGenerator;
			for (ModifyDataGenInstance event : listeners) generator = event.getInstance(generator, registryManager);
			return generator;
		}
	});
	
	/**
	 * 此事件将修改区块生成器中的数据生成实例方法
	 */
	Event<ModifyRegisterInstance> MODIFY_REGISTER_INSTANCE = EventFactory.createArrayBacked(ModifyRegisterInstance.class, listeners -> new ModifyRegisterInstance() {
		@Override
		public <T extends ChunkGenerator> T getInstance(T originGenerator, Registerable<WorldPreset> registerable, WorldPresetEntry.Registrar registrar) {
			T generator = originGenerator;
			for (ModifyRegisterInstance event : listeners) generator = event.getInstance(generator, registerable, registrar);
			return generator;
		}
	});
	
	/**
	 * 修改数据生成实例
	 */
	@FunctionalInterface
	interface ModifyDataGenInstance {
		<T extends ChunkGenerator> T getInstance(T originGenerator, DynamicRegistryManager registryManager);
	}
	
	/**
	 * 修改注册实例
	 */
	@FunctionalInterface
	interface ModifyRegisterInstance {
		<T extends ChunkGenerator> T getInstance(T originGenerator, Registerable<WorldPreset> registerable, WorldPresetEntry.Registrar registrar);
	}
}
