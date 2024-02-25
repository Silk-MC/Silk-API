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

package pers.saikel0rado1iu.silk.pattern;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.gen.ModDataGeneration;

/**
 * <h2 style="color:FFC800">用于 Silk API: Pattern 的数据生成器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public final class DataGen extends ModDataGeneration {
	@Override
	public void datagen(FabricDataGenerator.Pack pack) {
		pack.addProvider(InternationalizationProvider.EN_US::provider);
		pack.addProvider(InternationalizationProvider.ZH_CN::provider);
		pack.addProvider(InternationalizationProvider.ZH_HK::provider);
		pack.addProvider(InternationalizationProvider.ZH_TW::provider);
	}
	
	@Override
	public void dynamicRegistry(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
	}
}
