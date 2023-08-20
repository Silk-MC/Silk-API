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
import net.fabricmc.loader.api.FabricLoader;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

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
	
	final T mod;
	final Type saveMode;
	final LinkedHashMap<String, Object> configs;
	final String baseConfigName;
	
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
	 * 保存配置文件保存格式
	 */
	public enum Type {
		PROPERTIES, XML, JSON, TOML
	}
}
