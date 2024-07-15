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

import pers.saikel0rado1iu.silk.codex.*;
import pers.saikel0rado1iu.silk.codex.stream.SettingLoader;
import pers.saikel0rado1iu.silk.codex.stream.SettingStorage;
import pers.saikel0rado1iu.silk.impl.SilkCodex;

/**
 * Test {@link SettingData}
 */
public interface Settings {
	/**
	 * SWITCH_OPTION
	 */
	SettingOption<Boolean> SWITCH_OPTION = SettingOption.of(SilkCodex.getInstance().ofId("switch"), OptionType.SWITCH);
	/**
	 * OPTION_OPTION
	 */
	SettingOption<SettingType> OPTION_OPTION = SettingOption.of(SilkCodex.getInstance().ofId("option"), OptionType.ofOption(SettingType.class, SettingType[].class));
	/**
	 * INT_OPTION
	 */
	SettingOption<Integer> INT_OPTION = SettingOption.of(SilkCodex.getInstance().ofId("int"), OptionType.INT_SLIDER);
	/**
	 * DOUBLE_OPTION
	 */
	SettingOption<Double> DOUBLE_OPTION = SettingOption.of(SilkCodex.getInstance().ofId("double"), OptionType.DOUBLE_SLIDER);
	/**
	 * LINK_OPTION
	 */
	SettingOption<String> LINK_OPTION = SettingOption.of(SilkCodex.getInstance().ofId("link"), OptionType.LINK);
	/**
	 * SETTINGS_OPTION
	 */
	SettingOption<SettingData> SETTINGS_OPTION = SettingOption.of(SilkCodex.getInstance().ofId("settings"), OptionType.SETTINGS);
	/**
	 * PROPERTIES_SETTINGS
	 */
	SettingData PROPERTIES_SETTINGS = SettingData.builder(SilkCodex.getInstance())
			.format(SettingFormat.PROPERTIES)
			.addOption(SWITCH_OPTION, false)
			.addOption(SWITCH_OPTION, false)
			.addOption(OPTION_OPTION, SettingType.DEVELOPMENT)
			.addOption(INT_OPTION, 5, 0, 10)
			.addOption(DOUBLE_OPTION, 0.5, 0.0, 1.0)
			.addOption(LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
			.addOption(SETTINGS_OPTION, SettingData.builder(SilkCodex.getInstance())
					.addOption(SWITCH_OPTION, false)
					.addOption(OPTION_OPTION, SettingType.DEVELOPMENT)
					.addOption(INT_OPTION, 5, 0, 10)
					.addOption(DOUBLE_OPTION, 0.5, 0.0, 1.0)
					.addOption(LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
					.build())
			.build();
	/**
	 * XML_SETTINGS
	 */
	SettingData XML_SETTINGS = SettingData.builder(PROPERTIES_SETTINGS)
			.modPass(SilkCodex.getInstance())
			.format(SettingFormat.XML)
			.build();
	/**
	 * JSON_SETTINGS
	 */
	SettingData JSON_SETTINGS = SettingData.builder(SilkCodex.getInstance())
			.format(SettingFormat.JSON)
			.addOption(SWITCH_OPTION, false)
			.addOption(OPTION_OPTION, SettingType.DEVELOPMENT)
			.addOption(INT_OPTION, 5, 0, 10)
			.addOption(DOUBLE_OPTION, 0.5, 0.0, 1.0)
			.addOption(LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
			.addOption(SETTINGS_OPTION, SettingData.builder(SilkCodex.getInstance())
					.addOption(SWITCH_OPTION, false)
					.addOption(OPTION_OPTION, SettingType.DEVELOPMENT)
					.addOption(INT_OPTION, 5, 0, 10)
					.addOption(DOUBLE_OPTION, 0.5, 0.0, 1.0)
					.addOption(LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
					.build())
			.build();
	/**
	 * TOML_SETTINGS
	 */
	SettingData TOML_SETTINGS = SettingData.builder(SilkCodex.getInstance())
			.loadSettings(SettingLoader::load)
			.saveSettings(SettingStorage::save)
			.addOption(SWITCH_OPTION, false)
			.addOption(OPTION_OPTION, SettingType.DEVELOPMENT)
			.addOption(INT_OPTION, 5, 0, 10)
			.addOption(LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
			.addOption(SETTINGS_OPTION, SettingData.builder(SilkCodex.getInstance())
					.addOption(SWITCH_OPTION, false)
					.addOption(OPTION_OPTION, SettingType.DEVELOPMENT)
					.addOption(INT_OPTION, 5, 0, 10)
					.addOption(DOUBLE_OPTION, 0.5, 0.0, 1.0)
					.addOption(LINK_OPTION, "https://github.com/Silk-MC/Silk-API")
					.build())
			.build();
}
