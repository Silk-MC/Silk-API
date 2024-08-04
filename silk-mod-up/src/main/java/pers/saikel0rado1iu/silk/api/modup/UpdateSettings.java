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

package pers.saikel0rado1iu.silk.api.modup;

import pers.saikel0rado1iu.silk.api.codex.OptionType;
import pers.saikel0rado1iu.silk.api.codex.SettingData;
import pers.saikel0rado1iu.silk.api.codex.SettingOption;
import pers.saikel0rado1iu.silk.api.codex.SettingType;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.impl.SilkModUp;

/**
 * <h2 style="color:FFC800">更新设置</h2>
 * 有关模组更新的一些设置选项
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface UpdateSettings {
	/**
	 * 更新设置
	 */
	SettingOption<SettingData> UPDATE_SETTINGS = SettingOption.of(SilkModUp.getInstance().ofId("update"), OptionType.SETTINGS);
	/**
	 * 是否开启更新提示
	 */
	SettingOption<Boolean> UPDATE_NOTIFY = SettingOption.of(SilkModUp.getInstance().ofId("update_notify"), OptionType.SWITCH);
	/**
	 * 是否显示更新日志
	 */
	SettingOption<Boolean> SHOW_CHANGELOG = SettingOption.of(SilkModUp.getInstance().ofId("show_changelog"), OptionType.SWITCH);
	/**
	 * 是否检查新 MC 版本模组更新
	 */
	SettingOption<Boolean> CHECK_NEW_MC_VER_MOD = SettingOption.of(SilkModUp.getInstance().ofId("check_new_mc_ver_mod"), OptionType.SWITCH);
	/**
	 * 是否关闭停止更新警告
	 */
	SettingOption<Boolean> STOP_UPDATING_WARNING = SettingOption.of(SilkModUp.getInstance().ofId("stop_updating_warning"), OptionType.SWITCH);
	/**
	 * 是否关闭更新系统失效警告
	 */
	SettingOption<Boolean> UPDATE_SYS_FAIL_WARNING = SettingOption.of(SilkModUp.getInstance().ofId("update_sys_fail_warning"), OptionType.SWITCH);
	/**
	 * 选择更新方式
	 */
	SettingOption<UpdateMode> UPDATE_MODE = SettingOption.of(SilkModUp.getInstance().ofId("update_mode"), OptionType.ofOption(UpdateMode.class, UpdateMode[].class));
	/**
	 * 选择更新通道
	 */
	SettingOption<UpdateChannel> UPDATE_CHANNEL = SettingOption.of(SilkModUp.getInstance().ofId("update_channel"), OptionType.ofOption(UpdateChannel.class, UpdateChannel[].class));
	/**
	 * 更新日志选项，用于取出与更新日志有关的开发选项
	 */
	SettingOption<SettingData> CHANGELOG = SettingOption.of(SilkModUp.getInstance().ofId("update_changelog"), OptionType.SETTINGS);
	/**
	 * 是否可以显示更新日志选项
	 */
	SettingOption<Boolean> CAN_SHOW = SettingOption.of(SilkModUp.getInstance().ofId("can_show"), OptionType.SWITCH);
	
	/**
	 * 创建一个更新设置数据
	 *
	 * @param modPass     模组通
	 * @param settingData 设置数据
	 * @return 更新设置数据
	 */
	static SettingData of(ModPass modPass, SettingData settingData) {
		return SettingData.builder(modPass)
				.loadSettings(loader -> settingData.load())
				.saveSettings(storage -> settingData.save())
				.addOption(UPDATE_NOTIFY, true)
				.addOption(SHOW_CHANGELOG, true)
				.addOption(CHECK_NEW_MC_VER_MOD, true)
				.addOption(STOP_UPDATING_WARNING, true)
				.addOption(UPDATE_SYS_FAIL_WARNING, true)
				.addOption(UPDATE_MODE, UpdateMode.MANUAL_DOWNLOAD)
				.addOption(UPDATE_CHANNEL, UpdateChannel.RELEASE)
				.addOption(CHANGELOG, SettingData.builder(SilkModUp.getInstance()).type(SettingType.DEVELOPMENT).addOption(CAN_SHOW, true).build())
				.build();
	}
}
