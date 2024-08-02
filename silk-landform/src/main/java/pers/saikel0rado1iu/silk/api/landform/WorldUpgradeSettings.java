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

package pers.saikel0rado1iu.silk.api.landform;

import pers.saikel0rado1iu.silk.api.codex.OptionType;
import pers.saikel0rado1iu.silk.api.codex.SettingData;
import pers.saikel0rado1iu.silk.api.codex.SettingOption;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

/**
 * <h2 style="color:FFC800">世界升级设置</h2>
 * 有关世界升级的一些设置选项
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface WorldUpgradeSettings {
	/**
	 * 世界升级设置
	 */
	SettingOption<SettingData> WORLD_UPGRADE_SETTINGS = SettingOption.of(SilkLandform.getInstance().ofId("world_upgrade"), OptionType.SETTINGS);
	/**
	 * 设置检测区块中方块的遍历粒度用于判断是否应该删除区块
	 */
	SettingOption<Integer> CHUNK_BLOCK_SCAN_GRANULARITY = SettingOption.of(SilkLandform.getInstance().ofId("chunk_block_scan_granularity"), OptionType.INT_SLIDER, value -> (int) Math.pow(2, value));
	/**
	 * 设置玩家在区块中所待的时间阈值用于判断是否应该删除区块
	 */
	SettingOption<Integer> CHUNK_DELETION_THRESHOLD_TIME = SettingOption.of(SilkLandform.getInstance().ofId("chunk_deletion_threshold_time"), OptionType.INT_SLIDER, value -> value * 15);
	
	/**
	 * 创建一个世界升级设置数据
	 *
	 * @param modPass     模组通
	 * @param settingData 设置数据
	 * @return 世界升级设置数据
	 */
	static SettingData of(ModPass modPass, SettingData settingData) {
		return SettingData.builder(modPass)
				.loadSettings(loader -> settingData.load())
				.saveSettings(storage -> settingData.save())
				.addOption(CHUNK_BLOCK_SCAN_GRANULARITY, 2, 0, 4)
				.addOption(CHUNK_DELETION_THRESHOLD_TIME, 4, 0, 200)
				.build();
	}
}
