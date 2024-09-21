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
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.ActionResult;
import net.minecraft.world.biome.Biome;
import pers.saikel0rado1iu.silk.api.landform.biome.source.util.DefaultBiomeParameters;

import java.util.List;
import java.util.Map;

/**
 * <h2 style="color:FFC800">修改 {@link DefaultBiomeParameters} 事件</h2>
 * 用于动态修改 {@link DefaultBiomeParameters} 中生成规则的事件<br>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyDefaultBiomeParametersEvents {
	/**
	 * 此事件将 {@link DefaultBiomeParameters} 修改方法 {@link DefaultBiomeParameters#nonVanillaGeneratedBiome()}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link List}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link List}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link List}<br>
	 */
	Event<ModifyNonVanillaGeneratedBiome> MODIFY_NON_VANILLA_GENERATED_BIOME = EventFactory.createArrayBacked(ModifyNonVanillaGeneratedBiome.class, listeners -> (parameters, biomes) -> {
		List<RegistryKey<Biome>> biomeList = biomes;
		for (ModifyNonVanillaGeneratedBiome event : listeners) {
			Map.Entry<ActionResult, List<RegistryKey<Biome>>> entry = event.nonVanillaGeneratedBiome(parameters, biomeList);
			if (entry.getKey() != ActionResult.PASS) return entry;
			biomeList = entry.getValue();
		}
		return Map.entry(ActionResult.PASS, biomeList);
	});
	
	/**
	 * 修改非原版生成生物群系
	 */
	@FunctionalInterface
	interface ModifyNonVanillaGeneratedBiome {
		Map.Entry<ActionResult, List<RegistryKey<Biome>>> nonVanillaGeneratedBiome(DefaultBiomeParameters parameters, List<RegistryKey<Biome>> biomes);
	}
}
