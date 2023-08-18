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

package pers.saikel0rado1iu.silk.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.fabricmc.loader.api.FabricLoader;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用于创建模组配置数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public final class ConfigData<T extends ModBasicData> {
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir();
	public static final Charset CHARSET = StandardCharsets.UTF_8;
	
	private final T mod;
	private final Type saveMode;
	private final LinkedHashMap<String, Object> configs;
	private final String baseConfigName;
	
	/**
	 * 创建一个 {@link ModBasicData} 模组的空配置文件，默认的保存模式为 {@link Type#TOML}
	 */
	@SilkApi
	public ConfigData(T mod) {
		this(mod, Type.TOML, null);
	}
	
	/**
	 * 创建 {@link ConfigData<T>} 模组配置文件的副本
	 */
	@SilkApi
	public ConfigData(ConfigData<T> defaults) {
		this(defaults.mod, defaults);
	}
	
	/**
	 * 创建一个 {@link ModBasicData} 模组的空配置文件，通过自定义 {@link Type} 自定义保存模式
	 */
	@SilkApi
	public ConfigData(T mod, Type saveMode) {
		this(mod, saveMode, null);
	}
	
	/**
	 * 创建一个模组的配置文件的副本，但作为 {@link T} 模组的配置数据
	 */
	@SilkApi
	public ConfigData(T mod, ConfigData<?> defaults) {
		this(mod, defaults.saveMode, defaults);
	}
	
	/**
	 * 创建 {@link ConfigData<T>} 模组配置文件的副本，但使用自定义的保存模式
	 */
	@SilkApi
	public ConfigData(Type saveMode, ConfigData<T> defaults) {
		this(defaults.mod, saveMode, defaults);
	}
	
	/**
	 * 在 {@link ModBasicData} 模组中创建一个 {@link ConfigData} 模组配置文件的副本，并使用自定义的保存模式
	 */
	@SilkApi
	public ConfigData(T mod, Type saveMode, ConfigData<?> defaults) {
		this.mod = mod;
		this.saveMode = saveMode;
		this.baseConfigName = Paths.get(CONFIG_PATH.toString(), mod.getId()).toString();
		this.configs = defaults == null ? Maps.newLinkedHashMapWithExpectedSize(10) : defaults.configs;
	}
	
	/**
	 * 添加一个开关配置，只能储存开或关
	 *
	 * @param Id           配置 ID
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData<T> addSwitch(String Id, Boolean defaultValue) {
		if (configs.get(Id) == null) configs.put(Id, defaultValue);
		else Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + Id + "'!");
		return this;
	}
	
	/**
	 * 添加一个选项配置，用于保存有多个状态的配置
	 *
	 * @param Id           配置 ID
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData<T> addOption(String Id, Enum<?> defaultValue) {
		if (configs.get(Id) == null) configs.put(Id, defaultValue);
		else Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + Id + "'!");
		return this;
	}
	
	/**
	 * 添加一个整数滑块配置，用于保存一个有范围的整数配置
	 *
	 * @param Id           配置 ID
	 * @param minValue     可配置的最小值
	 * @param maxValue     可配置的最大值
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData<T> addIntSlider(String Id, Integer minValue, Integer maxValue, Integer defaultValue) {
		if (configs.get(Id) == null) configs.put(Id, List.of(minValue, maxValue, defaultValue));
		else Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + Id + "'!");
		return this;
	}
	
	/**
	 * 添加一个浮点数滑块配置，用于保存一个有范围的浮点数配置
	 *
	 * @param Id           配置 ID
	 * @param minValue     可配置的最小值
	 * @param maxValue     可配置的最大值
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData<T> addFloatSlider(String Id, Float minValue, Float maxValue, Float defaultValue) {
		if (configs.get(Id) == null) configs.put(Id, List.of(minValue, maxValue, defaultValue));
		else Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + Id + "'!");
		return this;
	}
	
	/**
	 * 添加一个子配置组，用于保存作为一个配置的子配置组
	 *
	 * @param Id         配置 ID
	 * @param subConfigs 子配置组
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData<T> addSubConfigs(String Id, ConfigData<T> subConfigs) {
		if (configs.get(Id) == null) configs.put(Id, subConfigs);
		else Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + Id + "'!");
		return this;
	}
	
	/**
	 * 设置已保存配置数据的值，但不能设置子配置的值
	 *
	 * @param Id    配置 ID
	 * @param value 配置值
	 */
	@SilkApi
	public void setConfig(String Id, Object value) {
		if (value instanceof Boolean && configs.get(Id) instanceof Boolean) {
			configs.put(Id, value);
			return;
		} else if (value instanceof Enum<?> && configs.get(Id).getClass().equals(value.getClass())) {
			configs.put(Id, value);
			return;
		} else if (value instanceof Integer i && configs.get(Id) instanceof List<?> list && list.get(2) instanceof Integer) {
			List<Integer> result = new ArrayList<>(3);
			list.forEach(obj -> result.add((Integer) obj));
			result.set(2, Math.max(result.get(0), Math.min(result.get(1), i)));
			configs.put(Id, result);
			return;
		} else if (value instanceof Float f && configs.get(Id) instanceof List<?> list && list.get(2) instanceof Float) {
			List<Float> result = new ArrayList<>(3);
			list.forEach(obj -> result.add((Float) obj));
			result.set(2, Math.max(result.get(0), Math.min(result.get(1), f)));
			configs.put(Id, result);
			return;
		}
		mod.logger().warn("Illegal type error occurred while setting configuration file! -- by " + Silk.DATA.getName());
	}
	
	/**
	 * 获取已添加的配置，返回 {@link O} 以便可保持通用
	 *
	 * @param Id  配置 ID
	 * @param c   将要转换的类
	 * @param <O> 转换出的类类型
	 * @return 如果返回 {@link Optional#empty()} 则表明获取数值失败
	 */
	@SilkApi
	public <O> Optional<O> getConfig(String Id, Class<O> c) {
		if (configs.get(Id) instanceof Boolean bool) return Optional.of(c.cast(bool));
		else if (configs.get(Id) instanceof Enum<?> e) return Optional.of(c.cast(e));
		else if (configs.get(Id) instanceof List<?> list) return Optional.of(c.cast(list.get(2)));
		else if (configs.get(Id) instanceof ConfigData<?> data) return Optional.of(c.cast(data));
		else mod.logger().warn("No configuration data was found with ID as '" + Id + "'! -- by " + Silk.DATA.getName());
		return Optional.empty();
	}
	
	/**
	 * 加载配置文件
	 */
	@SilkApi
	public void load() {
		try {
			switch (saveMode) {
				case PROPERTIES -> Silk.DATA.logger().info("ppt");
				case XML -> Silk.DATA.logger().info("xml");
				case JSON -> {
					Path file = Paths.get(baseConfigName + ".json");
					String data = Files.readString(file, CHARSET);
					Gson gson = new Gson();
				}
				case TOML -> {
					Path file = Paths.get(baseConfigName + ".toml");
					Toml toml = new Toml().read(file.toFile());
					loadTomlConfigs(toml);
				}
			}
		} catch (Exception e) {
			save();
		}
	}
	
	/**
	 * 保存配置文件
	 */
	@SilkApi
	public void save() {
		try {
			switch (saveMode) {
				case PROPERTIES -> Silk.DATA.logger().info("ppt");
				case XML -> Silk.DATA.logger().info("xml");
				case JSON -> {
					JsonObject jsonObject = JsonParser.parseString(new Gson().toJson(getSaveConfigs())).getAsJsonObject();
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					List<String> info = getAdditionalInfo();
					info.replaceAll(s -> "  \"//\": \"" + s + "\",");
					info.add("  ");
					List<String> data = new ArrayList<>(List.of(gson.toJson(jsonObject).split("\n")));
					data.addAll(1, info);
					Path file = Paths.get(baseConfigName + ".json");
					Files.write(file, data, CHARSET);
				}
				case TOML -> {
					TomlWriter tomlWriter = new TomlWriter.Builder().indentValuesBy(2).build();
					Path file = Paths.get(baseConfigName + ".toml");
					tomlWriter.write(getSaveConfigs(), file.toFile());
					List<String> data;
					List<String> info = getAdditionalInfo();
					info.replaceAll(s -> "# " + s);
					info.add("");
					data = Files.readAllLines(file, CHARSET);
					Files.write(file, info, CHARSET);
					Files.write(file, data, CHARSET, StandardOpenOption.APPEND);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 将此配置列表输出到调试列表，对调试有极大帮助
	 */
	@SilkApi
	public void debug() {
		mod.logger().info("-- configs debug --");
		switch (saveMode) {
			case PROPERTIES -> Silk.DATA.logger().info("ppt");
			case XML -> Silk.DATA.logger().info("xml");
			case JSON -> {
				JsonObject jsonObject = JsonParser.parseString(new Gson().toJson(getSaveConfigs())).getAsJsonObject();
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				List<String> info = getAdditionalInfo();
				info.replaceAll(s -> "  \"//\": \"" + s + "\",");
				info.add("  ");
				List<String> data = new ArrayList<>(List.of(gson.toJson(jsonObject).split("\n")));
				data.addAll(1, info);
				data.forEach(s -> mod.logger().info(s));
			}
			case TOML -> {
				TomlWriter tomlWriter = new TomlWriter.Builder().indentValuesBy(2).build();
				List<String> info = getAdditionalInfo();
				info.replaceAll(s -> "# " + s);
				info.add("");
				info.forEach(s -> mod.logger().info(s));
				Arrays.asList(tomlWriter.write(getSaveConfigs()).split("\n")).forEach(s -> mod.logger().info(s));
			}
		}
	}
	
	/**
	 * 加载 TOML 配置
	 */
	private ConfigData<?> loadTomlConfigs(Toml toml) {
		if (toml.isEmpty()) return new ConfigData<>(Silk.DATA);
		configs.forEach((s, object) -> {
			if (object instanceof Boolean) {
				var data = toml.getBoolean(s);
				if (data == null) return;
				setConfig(s, data);
			} else if (object instanceof Enum<?> e) {
				try {
					var data = Enum.valueOf(e.getDeclaringClass(), toml.getString(s));
					setConfig(s, data);
				} catch (IllegalArgumentException ignored) {
				}
			} else if (object instanceof List<?> list) {
				if (list.get(2) instanceof Integer) {
					var data = toml.getLong(s);
					if (data == null) return;
					setConfig(s, data);
				} else {
					var data = toml.getDouble(s);
					if (data == null) return;
					setConfig(s, data);
				}
			} else if (object instanceof ConfigData<?> cd) {
				var data = toml.getTable(s);
				if (data == null) return;
				configs.put(s, cd.loadTomlConfigs(data));
			} else {
				mod.logger().warn("Illegal type error occurred while loading configuration file! -- by " + Silk.DATA.getName());
			}
		});
		return this;
	}
	
	/**
	 * 获取保存配置应保存信息
	 */
	private LinkedHashMap<String, Object> getSaveConfigs() {
		LinkedHashMap<String, Object> saveConfigs = Maps.newLinkedHashMapWithExpectedSize(10);
		configs.forEach((s, object) -> {
			if (object instanceof ConfigData<?> data) saveConfigs.put(s, data.getSaveConfigs());
			else if (object instanceof List<?> list) saveConfigs.put(s, list.get(2));
			else saveConfigs.put(s, object);
		});
		return saveConfigs;
	}
	
	/**
	 * 用于添加附加信息
	 */
	private List<String> getAdditionalInfo() {
		List<String> list = new ArrayList<>(8);
		list.add("This configuration file is generated by '" + mod.getName() + "' calling Silk API.");
		String separator = File.separator.contains("\\") ? "\\\\" : File.separator;
		String[] paths = mod.getMod().getOrigin().getPaths().get(0).toString().split(separator);
		list.add("Mod Jar:      " + paths[paths.length - 1]);
		list.add("Mod Name:     " + mod.getName());
		list.add("Mod ID:       " + mod.getId());
		list.add("Mod Version:  " + mod.getVersion());
		list.add("Mod Authors:  " + String.join(", ", mod.getAuthors()));
		list.add("Mod Licenses: " + String.join(", ", mod.getLicenses()));
		if (mod.getLink(ModBasicData.LinkType.HOMEPAGE).isPresent()) list.add("Mod HomePage: " + mod.getLink(ModBasicData.LinkType.HOMEPAGE).get());
		list.add("Stored in " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd-HH:mm:ss")));
		return list;
	}
	
	/**
	 * 保存配置文件保存格式
	 */
	public enum Type {
		PROPERTIES, XML, JSON, TOML
	}
}
