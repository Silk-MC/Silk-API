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

package pers.saikel0rado1iu.silk.util.config;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.moandjiezana.toml.Toml;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static pers.saikel0rado1iu.silk.util.config.ConfigData.CHARSET;
import static pers.saikel0rado1iu.silk.util.config.ConfigData.CONFIG_PATH;

/**
 * <p><b style="color:FFC800"><font size="+1">用于配置数据读取</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public final class ConfigReader {
	private static final ScheduledExecutorService CONFIG_LOAD_POOL = new ScheduledThreadPoolExecutor(0,
			new BasicThreadFactory.Builder().daemon(true).build());
	
	private final ConfigData configData;
	
	ConfigReader(ConfigData configData) {
		this.configData = configData;
	}
	
	/**
	 * 加载 Properties 配置
	 */
	private static ConfigData loadPropertiesConfigs(ConfigData configData, Properties ppt, String keyPrefix) {
		configData.configs.forEach((s, object) -> {
			if (object instanceof ConfigData cd) {
				configData.configs.put(s, loadPropertiesConfigs(cd, ppt, keyPrefix + s + "."));
				return;
			}
			if (ppt.getProperty(keyPrefix + s) == null) return;
			String value = ppt.getProperty(keyPrefix + s);
			if (object instanceof Boolean && ("true".equals(value) || "false".equals(value))) {
				configData.setConfig(s, Boolean.parseBoolean(value));
				return;
			} else if (object instanceof Enum<?> e) {
				try {
					var data = Enum.valueOf(e.getDeclaringClass(), value);
					configData.setConfig(s, data);
					return;
				} catch (IllegalArgumentException ignored) {
				}
			} else if (object instanceof List<?> list) {
				try {
					if (list.get(2) instanceof Integer) configData.setConfig(s, Integer.parseInt(value));
					else configData.setConfig(s, Float.parseFloat(value));
					return;
				} catch (NumberFormatException ignored) {
				}
			}
			configData.mod.logger().warn("Illegal data error occurred while loading configuration file!" + Silk.DATA.getInfo());
		});
		return configData;
	}
	
	/**
	 * 加载 JSON 配置
	 */
	private static ConfigData loadJsonConfigs(ConfigData configData, String configs) {
		Gson gson = new Gson();
		LinkedHashMap<?, ?> configMap = gson.fromJson(configs, LinkedHashMap.class);
		configData.configs.forEach((s, object) -> {
			if (configMap.get(s) == null) return;
			if (object instanceof Boolean && configMap.get(s) instanceof Boolean bool) {
				configData.setConfig(s, bool);
				return;
			} else if (object instanceof Enum<?> e && configMap.get(s) instanceof String str) {
				try {
					var data = Enum.valueOf(e.getDeclaringClass(), str);
					configData.setConfig(s, data);
					return;
				} catch (IllegalArgumentException ignored) {
				}
			} else if (object instanceof List<?> list && configMap.get(s) instanceof Double d) {
				if (list.get(2) instanceof Integer) configData.setConfig(s, d.intValue());
				else configData.setConfig(s, d.floatValue());
				return;
			} else if (object instanceof ConfigData cd && configMap.get(s) instanceof LinkedTreeMap<?, ?> map) {
				configData.configs.put(s, loadJsonConfigs(cd, gson.toJson(map)));
				return;
			}
			configData.mod.logger().warn("Illegal data error occurred while loading configuration file!" + Silk.DATA.getInfo());
		});
		return configData;
	}
	
	/**
	 * 加载 TOML 配置
	 */
	private static ConfigData loadTomlConfigs(ConfigData configData, Toml toml) {
		configData.configs.forEach((s, object) -> {
			try {
				if (object instanceof Boolean) {
					var data = toml.getBoolean(s);
					if (data != null) configData.setConfig(s, data);
				} else if (object instanceof Enum<?> e) {
					try {
						var data = Enum.valueOf(e.getDeclaringClass(), toml.getString(s));
						configData.setConfig(s, data);
					} catch (IllegalArgumentException ignored) {
					}
				} else if (object instanceof List<?> list) {
					if (list.get(2) instanceof Integer) {
						var data = toml.getLong(s);
						if (data != null) configData.setConfig(s, data.intValue());
					} else {
						var data = toml.getDouble(s);
						if (data != null) configData.setConfig(s, data.floatValue());
					}
				} else if (object instanceof ConfigData cd) {
					var data = toml.getTable(s);
					if (data != null) configData.configs.put(s, loadTomlConfigs(cd, data));
				}
			} catch (Exception e) {
				configData.mod.logger().warn("Illegal data error occurred while loading configuration file!" + Silk.DATA.getInfo());
			}
		});
		return configData;
	}
	
	/**
	 * 加载默认路径中以模组 ID 为名的配置文件
	 */
	@SilkApi
	public void load() {
		load(CONFIG_PATH);
	}
	
	/**
	 * 加载在自定义保存路径中以模组 ID 为名的配置文件
	 */
	@SilkApi
	public void load(Path customPath) {
		switch (configData.mode) {
			case PROPERTIES -> load(customPath, configData.mod.getId() + ".properties");
			case XML -> load(customPath, configData.mod.getId() + ".xml");
			case JSON -> load(customPath, configData.mod.getId() + ".json");
			case TOML -> load(customPath, configData.mod.getId() + ".toml");
		}
	}
	
	/**
	 * 加载在自定义保存路径中特定名称的配置文件
	 */
	@SilkApi
	public void load(Path customPath, String fileName) {
		CONFIG_LOAD_POOL.schedule(new ConfigLoadThread(configData, customPath, fileName), 0, TimeUnit.SECONDS);
	}
	
	/**
	 * 解析 XML 配置文件
	 */
	private static final class SaxParse extends DefaultHandler {
		private final LinkedHashMap<String, ConfigData> superCdList = Maps.newLinkedHashMapWithExpectedSize(8);
		private ConfigData configData;
		private String key;
		
		private SaxParse(ConfigData configData) {
			this.configData = configData;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (qName.equals(configData.mod.getId())) return;
			key = qName;
			if (configData.configs.get(key) instanceof ConfigData cd) {
				superCdList.put(key, configData);
				configData = cd;
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (superCdList.isEmpty()) return;
			String s = (String) superCdList.keySet().toArray()[superCdList.keySet().size() - 1];
			if (qName.equals(s)) {
				configData = superCdList.get(s);
				superCdList.remove(s);
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			String value = new String(ch, start, length).trim();
			if (value.isEmpty()) return;
			Object object = configData.configs.get(key);
			if (object instanceof Boolean && ("true".equals(value) || "false".equals(value))) {
				configData.setConfig(key, Boolean.parseBoolean(value));
				return;
			} else if (object instanceof Enum<?> e) {
				try {
					var data = Enum.valueOf(e.getDeclaringClass(), value);
					configData.setConfig(key, data);
					return;
				} catch (IllegalArgumentException ignored) {
				}
			} else if (object instanceof List<?> list) {
				try {
					if (list.get(2) instanceof Integer) configData.setConfig(key, Integer.parseInt(value));
					else configData.setConfig(key, Float.parseFloat(value));
					return;
				} catch (NumberFormatException ignored) {
				}
			}
			configData.mod.logger().warn("Illegal data error occurred while loading configuration file!" + Silk.DATA.getInfo());
		}
	}
	
	/**
	 * 单独开辟线程减少主线程损耗
	 */
	private static class ConfigLoadThread extends Thread {
		private final ConfigData configData;
		private final Path customPath;
		private final String fileName;
		
		private ConfigLoadThread(ConfigData configData, Path customPath, String fileName) {
			this.configData = configData;
			this.customPath = customPath;
			this.fileName = fileName;
		}
		
		@Override
		public void run() {
			try {
				Path file = Paths.get(customPath.toString(), fileName);
				switch (configData.mode) {
					case PROPERTIES -> {
						Properties data = new Properties();
						data.load(Files.newInputStream(file));
						loadPropertiesConfigs(configData, data, "");
					}
					case XML -> {
						SAXParserFactory spf = SAXParserFactory.newInstance();
						SAXParser sp = spf.newSAXParser();
						SaxParse sax = new SaxParse(configData);
						sp.parse(Files.newInputStream(file), sax);
					}
					case JSON -> {
						String data = Files.readString(file, CHARSET);
						loadJsonConfigs(configData, data);
					}
					case TOML -> {
						Toml toml = new Toml().read(file.toFile());
						loadTomlConfigs(configData, toml);
					}
				}
			} catch (Exception e) {
				configData.writer().save();
			}
		}
	}
}
