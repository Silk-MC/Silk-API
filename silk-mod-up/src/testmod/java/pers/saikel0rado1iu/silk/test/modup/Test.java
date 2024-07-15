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

package pers.saikel0rado1iu.silk.test.modup;

import com.google.common.collect.ImmutableSet;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;
import pers.saikel0rado1iu.silk.modpass.*;
import pers.saikel0rado1iu.silk.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.modpass.pack.ResourcePack;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;
import pers.saikel0rado1iu.silk.modup.UpdateData;

import java.util.Optional;
import java.util.Set;

/**
 * 测试
 */
public final class Test implements ModMain {
	/**
	 * MOD_MENU
	 */
	public static final ModDataExpansion MOD_MENU = new ModDataExpansion() {
		@Override
		public int themeColor() {
			return 0;
		}
		
		@Override
		public Text i18nName() {
			return Text.literal("modmenu");
		}
		
		@Override
		public Text i18nSummary() {
			return Text.empty();
		}
		
		@Override
		public Text i18nDescription() {
			return Text.empty();
		}
		
		@Override
		public Optional<DataPack> dataPack() {
			return Optional.empty();
		}
		
		@Override
		public Optional<ResourcePack> resourcePack() {
			return Optional.empty();
		}
		
		@Override
		public String id() {
			return "modmenu";
		}
	};
	/**
	 * UPDATE_SETTINGS
	 */
	public static final SettingData UPDATE_SETTINGS = SettingData.builder(SilkModUp.getInstance()).build();
	/**
	 * UPDATE_DATA_BUILDER
	 */
	public static final UpdateData.Builder UPDATE_DATA_BUILDER = UpdateData.builder(Test.MOD_MENU, Test.UPDATE_SETTINGS);
	
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
		return ImmutableSet.of(UpdateManagerRegistryTest.class);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return SilkRopeStick.getInstance();
	}
}
