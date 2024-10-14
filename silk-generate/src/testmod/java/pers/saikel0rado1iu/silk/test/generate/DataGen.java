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

package pers.saikel0rado1iu.silk.test.generate;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.api.generate.DataGenerator;
import pers.saikel0rado1iu.silk.api.generate.DynamicDataEntry;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;
import pers.saikel0rado1iu.silk.test.generate.data.*;
import pers.saikel0rado1iu.silk.test.generate.entity.damage.DamageTypeEntryTest;
import pers.saikel0rado1iu.silk.test.generate.world.*;

import java.util.Optional;
import java.util.Set;

/**
 * test
 */
public final class DataGen implements DataGenerator {
	/**
	 * 数据生成方法
	 *
	 * @param builtinPack  Fabric 模组的内置包
	 * @param dataPack     模组的数据包
	 * @param resourcePack 模组的资源包
	 */
	@Override
	public void generate(FabricDataGenerator.Pack builtinPack, Optional<FabricDataGenerator.Pack> dataPack, Optional<FabricDataGenerator.Pack> resourcePack) {
		dataPack.ifPresent(pack -> {
			pack.addProvider(AdvancementGenUtilTest::new);
			pack.addProvider(LootTableGenUtilTest.Block::new);
			pack.addProvider(LootTableGenUtilTest.Entity::new);
			pack.addProvider(RecipeGenUtilTest::new);
			pack.addProvider(TagGenUtilTest::new);
		});
		resourcePack.ifPresent(pack -> {
			pack.addProvider(ModelProvider::new);
			pack.addProvider(LinkedLanguageProviderTest::new);
		});
	}
	
	/**
	 * 动态数据条目表
	 *
	 * @return 列表动态数据集合
	 */
	@Override
	public Set<DynamicDataEntry<?>> dynamicDataEntries() {
		return ImmutableSet.of(
				DamageTypeEntryTest.INSTANCE,
				BiomeEntryTest.INSTANCE,
				ChunkGeneratorSettingEntryTest.INSTANCE,
				ConfiguredFeatureEntryTest.INSTANCE,
				MultiNoiseBiomeSourceParameterListEntryTest.INSTANCE,
				PlacedFeatureEntryTest.INSTANCE,
				WorldPresetEntryTest.INSTANCE);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return Main.MOD_PASS;
	}
	
	@Override
	public @Nullable String getEffectiveModId() {
		return SilkGenerate.getInstance().id();
	}
}
