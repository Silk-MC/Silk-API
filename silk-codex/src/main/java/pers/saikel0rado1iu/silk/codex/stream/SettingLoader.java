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

package pers.saikel0rado1iu.silk.codex.stream;

import pers.saikel0rado1iu.silk.codex.SettingData;

import java.nio.file.Path;

/**
 * <h2 style="color:FFC800">设置加载器</h2>
 * 用于加载配置
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class SettingLoader {
	private final SettingData settingData;
	
	private SettingLoader(SettingData settingData) {
		this.settingData = settingData;
	}
	
	/**
	 * 创建一个设置加载器
	 *
	 * @param settingData 设置数据
	 * @return 设置加载器
	 */
	public static SettingLoader create(SettingData settingData) {
		return new SettingLoader(settingData);
	}
	
	/**
	 * 默认的加载方法<br>
	 * 加载 Fabric 的默认配置目录下并使用模组标识符作为文件名的设置文件
	 */
	public void load() {
		load(settingData.modData().id());
	}
	
	/**
	 * 加载方法<br>
	 * 加载 Fabric 的默认配置目录下的设置文件
	 *
	 * @param fileBaseName 加载文件的基本名
	 */
	public void load(String fileBaseName) {
		switch (settingData.format()) {
			case PROPERTIES -> load(SettingData.SETTING_PATH, fileBaseName + ".properties");
			case XML -> load(SettingData.SETTING_PATH, fileBaseName + ".xml");
			case JSON -> load(SettingData.SETTING_PATH, fileBaseName + ".json");
			case TOML -> load(SettingData.SETTING_PATH, fileBaseName + ".toml");
		}
	}
	
	/**
	 * 加载方法<br>
	 * 使用模组标识符作为文件名
	 *
	 * @param path 加载的文件路径
	 */
	public void load(Path path) {
		switch (settingData.format()) {
			case PROPERTIES -> load(path, settingData.modData().id() + ".properties");
			case XML -> load(path, settingData.modData().id() + ".xml");
			case JSON -> load(path, settingData.modData().id() + ".json");
			case TOML -> load(path, settingData.modData().id() + ".toml");
		}
	}
	
	/**
	 * 加载方法
	 *
	 * @param path     加载的文件路径
	 * @param fileName 加载的文件名
	 */
	public void load(Path path, String fileName) {
		SettingReadThread.run(settingData, path, fileName);
	}
}
