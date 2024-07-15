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
import pers.saikel0rado1iu.silk.impl.SilkCodex;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * <h2 style="color:FFC800">设置存储器</h2>
 * 用于存储配置
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class SettingStorage {
	private final SettingData settingData;
	
	private SettingStorage(SettingData settingData) {
		this.settingData = settingData;
	}
	
	/**
	 * 创建一个设置存储器
	 *
	 * @param settingData 设置数据
	 * @return 设置存储器
	 */
	public static SettingStorage create(SettingData settingData) {
		return new SettingStorage(settingData);
	}
	
	/**
	 * 调试方法，会在日志中输出调试信息
	 */
	public void debug() {
		debug(settingData);
	}
	
	/**
	 * 调试方法，会在日志中输出调试信息
	 *
	 * @param modPass 要输出的模组通
	 */
	public void debug(ModPass modPass) {
		SettingWriteThread.run(settingData, Path.of("", ""), "", (path, data) -> modPass.modData().logger().info("\n— setting debug —\n" + String.join("\n", data)));
	}
	
	/**
	 * 默认的保存方法<br>
	 * 将设置文件保存到 Fabric 的默认配置目录下并使用模组标识符作为文件名
	 */
	public void save() {
		save(settingData.modData().id());
	}
	
	/**
	 * 保存方法<br>
	 * 将设置文件保存到 Fabric 的默认配置目录下
	 *
	 * @param fileBaseName 保存文件的基本名
	 */
	public void save(String fileBaseName) {
		switch (settingData.format()) {
			case PROPERTIES -> save(SettingData.SETTING_PATH, fileBaseName + ".properties");
			case XML -> save(SettingData.SETTING_PATH, fileBaseName + ".xml");
			case JSON -> save(SettingData.SETTING_PATH, fileBaseName + ".json");
			case TOML -> save(SettingData.SETTING_PATH, fileBaseName + ".toml");
		}
	}
	
	/**
	 * 保存方法<br>
	 * 使用模组标识符作为文件名
	 *
	 * @param path 保存的文件路径
	 */
	public void save(Path path) {
		switch (settingData.format()) {
			case PROPERTIES -> save(path, settingData.modData().id() + ".properties");
			case XML -> save(path, settingData.modData().id() + ".xml");
			case JSON -> save(path, settingData.modData().id() + ".json");
			case TOML -> save(path, settingData.modData().id() + ".toml");
		}
	}
	
	/**
	 * 保存方法
	 *
	 * @param path     保存的文件路径
	 * @param fileName 保存的文件名
	 */
	public void save(Path path, String fileName) {
		SettingWriteThread.run(settingData, path, fileName, (p, data) -> {
			try {
				if (Files.exists(p)) Files.write(p, data, SettingData.CHARSET, StandardOpenOption.WRITE);
				else Files.write(p, data, SettingData.CHARSET, StandardOpenOption.CREATE);
			} catch (IOException e) {
				String msg = String.format("Unable to save: Due to some unexpected issues, the %s settings cannot be saved to the %s path. Please refer to the error details!\n%s", settingData.modData().debugName(), p, e);
				SilkCodex.getInstance().logger().error(msg);
				throw new RuntimeException(msg);
			}
		});
	}
}
