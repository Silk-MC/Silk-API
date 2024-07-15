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
import net.minecraft.world.dimension.DimensionOptions;
import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.landform.UpgradableWorldData;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModMain;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

import java.util.Set;

import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * 测试
 */
public final class Test implements ModMain {
	/**
	 * test
	 */
	public static final SettingData UPGRADABLE_WORLD_SETTINGS = SettingData.builder(SilkLandform.getInstance()).build();
	/**
	 * test
	 */
	public static final UpgradableWorldData<TestChunkGenerator> DATA = UpgradableWorldData.builder(SilkLandform.getInstance(), UPGRADABLE_WORLD_SETTINGS, DimensionOptions.OVERWORLD, TestChunkGenerator::getInstance).build();
	
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	@Override
	public void main(ModPass mod) {
	}
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	@Override
	public Set<Class<? extends MainRegistrationProvider<?>>> registry() {
		return ImmutableSet.of(ChunkGeneratorCodecs.class);
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
