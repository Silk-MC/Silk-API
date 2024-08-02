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

package pers.saikel0rado1iu.silk.api.generate.world;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.PlacedFeature;
import pers.saikel0rado1iu.silk.api.generate.DynamicDataEntry;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

/**
 * <h2 style="color:FFC800">已放置的地物条目</h2>
 * 用于创建、注册、生成已放置的地物
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface PlacedFeatureEntry extends DynamicDataEntry<PlacedFeature> {
	/**
	 * 获取已放置的地物
	 *
	 * @param modPass 模组通
	 * @param id      已放置的地物 ID
	 * @return 已放置的地物
	 */
	static RegistryKey<PlacedFeature> of(ModPass modPass, String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, modPass.modData().ofId(id));
	}
	
	@Override
	default RegistryKey<? extends Registry<PlacedFeature>> registryKey() {
		return RegistryKeys.PLACED_FEATURE;
	}
}
