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

package pers.saikel0rado1iu.silk.test.codex;

import com.google.common.collect.ImmutableSet;
import pers.saikel0rado1iu.silk.codex.OptionType;
import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.codex.SettingOption;
import pers.saikel0rado1iu.silk.codex.SettingType;
import pers.saikel0rado1iu.silk.impl.SilkCodex;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModMain;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.modpass.registry.MainRegistrationProvider;

import java.util.Set;

/**
 * 测试
 */
public final class Test implements ModMain {
	/**
	 * 模组主函数
	 *
	 * @param mod 提供的模组通
	 */
	@Override
	public void main(ModPass mod) {
		Settings.PROPERTIES_SETTINGS.loader().load(SettingData.SETTING_PATH);
		Settings.PROPERTIES_SETTINGS.storage().debug();
		Settings.PROPERTIES_SETTINGS.storage().save(SettingData.SETTING_PATH);
		Settings.XML_SETTINGS.loader().load();
		Settings.XML_SETTINGS.storage().debug();
		Settings.XML_SETTINGS.storage().save();
		Settings.JSON_SETTINGS.loader().load();
		Settings.JSON_SETTINGS.storage().debug();
		Settings.JSON_SETTINGS.storage().save();
		Settings.TOML_SETTINGS.addOption(Settings.DOUBLE_OPTION, 0.5, 0.0, 1.0);
		Settings.TOML_SETTINGS.addOption(SettingOption.of(SilkCodex.getInstance().ofId("dev"), OptionType.SETTINGS),
				SettingData.builder(SilkCodex.getInstance())
						.type(SettingType.DEVELOPMENT)
						.addOption(Settings.SWITCH_OPTION, false)
						.addOption(Settings.OPTION_OPTION, SettingType.DEVELOPMENT)
						.addOption(Settings.INT_OPTION, 5, 0, 10)
						.addOption(Settings.DOUBLE_OPTION, 0.5, 0.0, 1.0)
						.addOption(Settings.LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
						.build());
		Settings.TOML_SETTINGS.addOption(SettingOption.of(SilkCodex.getInstance().ofId("experimental"), OptionType.SETTINGS),
				SettingData.builder(SilkCodex.getInstance())
						.type(SettingType.EXPERIMENTAL)
						.addOption(Settings.SWITCH_OPTION, false)
						.addOption(Settings.OPTION_OPTION, SettingType.DEVELOPMENT)
						.addOption(Settings.INT_OPTION, 5, 0, 10)
						.addOption(Settings.DOUBLE_OPTION, 0.5, 0.0, 1.0)
						.addOption(Settings.LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
						.build());
		Settings.TOML_SETTINGS.addOption(SettingOption.of(SilkCodex.getInstance().ofId("deprecated"), OptionType.SETTINGS),
				SettingData.builder(SilkCodex.getInstance())
						.type(SettingType.DEPRECATED)
						.addOption(Settings.SWITCH_OPTION, false)
						.addOption(Settings.OPTION_OPTION, SettingType.DEVELOPMENT)
						.addOption(Settings.INT_OPTION, 5, 0, 10)
						.addOption(Settings.DOUBLE_OPTION, 0.5, 0.0, 1.0)
						.addOption(Settings.LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
						.build());
		Settings.TOML_SETTINGS.loader().load();
		Settings.TOML_SETTINGS.storage().debug();
		Settings.TOML_SETTINGS.storage().save();
	}
	
	/**
	 * 注册表方法，提供注册表以供注册
	 *
	 * @return 注册表的类型集合
	 */
	@Override
	public Set<Class<? extends MainRegistrationProvider<?>>> registry() {
		return ImmutableSet.of();
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return SilkCodex.getInstance();
	}
}
