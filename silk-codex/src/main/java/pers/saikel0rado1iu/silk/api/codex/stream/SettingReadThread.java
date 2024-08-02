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

package pers.saikel0rado1iu.silk.api.codex.stream;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.json.JsonFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.xml.sax.SAXException;
import pers.saikel0rado1iu.silk.api.codex.OptionType;
import pers.saikel0rado1iu.silk.api.codex.SettingData;
import pers.saikel0rado1iu.silk.api.codex.SettingOption;
import pers.saikel0rado1iu.silk.impl.SilkCodex;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <h2 style="color:FFC800">设置读取线程</h2>
 * 以更不会影响玩家操作的方法读取设置文件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
final class SettingReadThread extends Thread {
	private static final ScheduledExecutorService SETTING_LOAD_POOL = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().daemon(true).build());
	private final SettingData settingData;
	private final Path path;
	
	private SettingReadThread(SettingData settingData, Path path) {
		this.settingData = settingData;
		this.path = path;
	}
	
	static void run(SettingData settingData, Path path, String fileName) {
		settingData.setModifying(true);
		SETTING_LOAD_POOL.schedule(new SettingReadThread(settingData, Path.of(path.toString(), fileName)), 0, TimeUnit.SECONDS);
	}
	
	private static void readFromProperties(SettingData settingData, Path path) throws IOException {
		Properties properties = new Properties();
		properties.load(Files.newInputStream(path));
		readFromProperties(settingData, properties, "");
	}
	
	private static void readFromProperties(SettingData settingData, Properties properties, String keyPrefix) {
		for (SettingOption<?> option : settingData.options().keySet()) {
			String fullKey = (keyPrefix.isEmpty() ? option.id().toString() : String.format("%s.%s", keyPrefix, option.id())).replaceAll(":", "|");
			String value = properties.getProperty(fullKey);
			if (value == null) continue;
			if (option.type() == OptionType.SETTINGS) {
				readFromProperties((SettingData) settingData.getValue(option), properties, fullKey);
			} else {
				settingData.setValue(option, value);
			}
		}
	}
	
	private static void readFromXml(SettingData settingData, Path path) throws IOException {
		try {
			SAXParserFactory.newInstance().newSAXParser().parse(Files.newInputStream(path), new SettingSaxParser(settingData));
		} catch (ParserConfigurationException | SAXException e) {
			SilkCodex.getInstance().logger().warn("Parsing failed: Unable to correctly parse the XML document while reading the settings for {}.", settingData.modData().debugName());
		}
	}
	
	private static void readFromJson(SettingData settingData, Path path) throws IOException {
		readFromDataMap(settingData, (new Gson()).fromJson(Files.readString(path, SettingData.CHARSET), new TypeToken<LinkedHashMap<String, Object>>() {
		}.getType()));
	}
	
	private static void readFromToml(SettingData settingData, Path path) throws IOException {
		Config.setInsertionOrderPreserved(true);
		try (FileConfig toml = FileConfig.of(path)) {
			toml.load();
			readFromDataMap(settingData, (new Gson()).fromJson(JsonFormat.fancyInstance().createWriter().writeToString(toml), new TypeToken<LinkedHashMap<String, Object>>() {
			}.getType()));
		} catch (Exception e) {
			SilkCodex.getInstance().logger().error("", e);
		}
	}
	
	private static void readFromDataMap(SettingData settingData, Map<String, Object> options) {
		for (SettingOption<?> option : settingData.options().keySet()) {
			String id = option.id().toString();
			Object value = options.get(id);
			if (value == null) continue;
			if (option.type() == OptionType.SETTINGS) {
				//noinspection unchecked
				readFromDataMap((SettingData) settingData.getValue(option), (Map<String, Object>) value);
			} else {
				settingData.setValue(option, value.toString());
			}
		}
	}
	
	@Override
	public void run() {
		super.run();
		try {
			if (Files.exists(path)) {
				switch (settingData.format()) {
					case PROPERTIES -> readFromProperties(settingData, path);
					case XML -> readFromXml(settingData, path);
					case JSON -> readFromJson(settingData, path);
					case TOML -> readFromToml(settingData, path);
				}
			}
		} catch (IOException e) {
			SilkCodex.getInstance().logger().error("Unable to read: Unable to open the settings file \"{}\" for {} due to an I/O exception.\n", path, settingData.modData().debugName(), e);
		}
		settingData.setModifying(false);
	}
}
