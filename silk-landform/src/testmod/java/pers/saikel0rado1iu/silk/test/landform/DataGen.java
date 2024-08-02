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

package pers.saikel0rado1iu.silk.test.landform;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import pers.saikel0rado1iu.silk.api.generate.DataGenerator;
import pers.saikel0rado1iu.silk.api.generate.DynamicDataEntry;
import pers.saikel0rado1iu.silk.api.modpass.ModData;

import java.util.Optional;
import java.util.Set;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * BaseDataGen
 */
public final class DataGen implements DataGenerator {
	@Override
	public void generate(FabricDataGenerator.Pack builtinPack, Optional<FabricDataGenerator.Pack> dataPack, Optional<FabricDataGenerator.Pack> resourcePack) {
		builtinPack.addProvider(WorldPresetTagProvider::new);
	}
	
	/**
	 * 动态数据条目表
	 *
	 * @return 列表动态数据集合
	 */
	@Override
	public Set<DynamicDataEntry<?>> dynamicDataEntries() {
		return ImmutableSet.of(ChunkGeneratorSettingEntries.INSTANCE, BiomeSourceParamLists.INSTANCE, WorldPresets.INSTANCE);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return MOD_PASS;
	}
}
