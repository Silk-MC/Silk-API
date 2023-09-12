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
import org.jetbrains.annotations.NotNull;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
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
public final class ConfigData {
	public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir();
	public static final Charset CHARSET = StandardCharsets.UTF_8;
	final ModBasicData mod;
	final Mode mode;
	final Type type;
	final LinkedHashMap<String, Object> configs;
	final LinkedHashMap<String, Object> defaults;
	private final ConfigReader reader;
	private final ConfigWriter writer;
	private ConfigData mainConfig;
	
	/**
	 * 在 {@link ModBasicData} 模组中创建一个 {@link ConfigData} 模组配置文件的副本，并使用自定义的保存模式
	 */
	private ConfigData(ModBasicData mod, Type type, Mode mode, LinkedHashMap<String, Object> configs, LinkedHashMap<String, Object> defaults) {
		this.mod = mod;
		this.type = type;
		this.mode = mode;
		this.configs = configs;
		this.defaults = defaults;
		this.reader = new ConfigReader(this);
		this.writer = new ConfigWriter(this);
		this.mainConfig = this;
	}
	
	/**
	 * 创建一个 {@link ModBasicData} 模组的空配置
	 */
	@SilkApi
	public static Builder builder(@NotNull ModBasicData mod) {
		return new Builder(mod);
	}
	
	/**
	 * 使用 {@link ConfigData} 作为原始配置创建一份配置副本
	 */
	@SilkApi
	public static Builder builder(@NotNull ConfigData defaults) {
		return new Builder(defaults);
	}
	
	/**
	 * 添加一个开关配置，只能储存开或关
	 *
	 * @param id           配置 ID
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData addSwitch(String id, Boolean defaultValue) {
		if (configs.get(id) == null) {
			defaults.put(id, defaultValue);
			configs.put(id, defaultValue);
		} else {
			Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + id + "'!");
		}
		return this;
	}
	
	/**
	 * 添加一个选项配置，用于保存有多个状态的配置
	 *
	 * @param id           配置 ID
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData addOption(String id, Enum<?> defaultValue) {
		if (configs.get(id) == null) {
			defaults.put(id, defaultValue);
			configs.put(id, defaultValue);
		} else {
			Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + id + "'!");
		}
		return this;
	}
	
	/**
	 * 获取主配置用于读取或保存
	 */
	@SilkApi
	public ConfigData getMainConfig() {
		return mainConfig;
	}
	
	/**
	 * 设置主配置用于读取或保存
	 */
	@SilkApi
	private void setMainConfig(ConfigData mainConfig) {
		this.mainConfig = mainConfig;
	}
	
	/**
	 * 添加一个整数滑块配置，用于保存一个有范围的整数配置
	 *
	 * @param id           配置 ID
	 * @param minValue     可配置的最小值
	 * @param maxValue     可配置的最大值
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData addIntSlider(String id, Integer minValue, Integer maxValue, Integer defaultValue) {
		if (configs.get(id) == null) {
			defaults.put(id, defaultValue);
			configs.put(id, List.of(minValue, maxValue, defaultValue));
		} else {
			Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + id + "'!");
		}
		return this;
	}
	
	/**
	 * 添加一个浮点数滑块配置，用于保存一个有范围的浮点数配置
	 *
	 * @param id           配置 ID
	 * @param minValue     可配置的最小值
	 * @param maxValue     可配置的最大值
	 * @param defaultValue 默认值
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData addFloatSlider(String id, Float minValue, Float maxValue, Float defaultValue) {
		if (configs.get(id) == null) {
			defaults.put(id, defaultValue);
			configs.put(id, List.of(minValue, maxValue, defaultValue));
		} else {
			Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + id + "'!");
		}
		return this;
	}
	
	/**
	 * 添加一个子配置组，用于保存作为一个配置的子配置组
	 *
	 * @param id         配置 ID
	 * @param subConfigs 子配置组
	 * @return 返回自身以便可以连续添加配置
	 */
	@SilkApi
	public ConfigData addSubConfigs(String id, ConfigData subConfigs) {
		if (configs.get(id) == null) {
			defaults.put(id, subConfigs);
			configs.put(id, subConfigs);
			subConfigs.setMainConfig(getMainConfig());
		} else {
			Silk.DATA.logger().error("'" + mod.getId() + "' attempts to replace an existing configuration '" + id + "'!");
		}
		return this;
	}
	
	/**
	 * 设置已保存配置数据的值，但不能设置子配置的值
	 *
	 * @param id    配置 ID
	 * @param value 配置值
	 */
	@SilkApi
	public void setConfig(String id, Object value) {
		if (value instanceof Boolean && configs.get(id) instanceof Boolean) {
			configs.put(id, value);
			return;
		} else if (value instanceof Enum<?> && configs.get(id).getClass().equals(value.getClass())) {
			configs.put(id, value);
			return;
		} else if (value instanceof Integer i && configs.get(id) instanceof List<?> list && list.get(2) instanceof Integer) {
			List<Integer> result = new ArrayList<>(3);
			list.forEach(obj -> result.add((Integer) obj));
			result.set(2, Math.max(result.get(0), Math.min(result.get(1), i)));
			configs.put(id, result);
			return;
		} else if (value instanceof Float f && configs.get(id) instanceof List<?> list && list.get(2) instanceof Float) {
			List<Float> result = new ArrayList<>(3);
			list.forEach(obj -> result.add((Float) obj));
			result.set(2, Math.max(result.get(0), Math.min(result.get(1), f)));
			configs.put(id, result);
			return;
		}
		mod.logger().warn("Illegal type error occurred while setting configuration file! — by " + Silk.DATA.getName());
	}
	
	/**
	 * 获取已添加的配置，返回 {@link O} 以便可保持通用
	 *
	 * @param id  配置 ID
	 * @param c   将要转换的类
	 * @param <O> 转换出的类类型
	 * @return 如果返回 {@link Optional#empty()} 则表明获取数值失败
	 */
	@SilkApi
	public <O> O getConfig(String id, Class<O> c) {
		if (configs.get(id) instanceof Boolean bool) return c.cast(bool);
		else if (configs.get(id) instanceof Enum<?> e) return c.cast(e);
		else if (configs.get(id) instanceof List<?> list) return c.cast(list.get(2));
		else if (configs.get(id) instanceof ConfigData data) return c.cast(data);
		else mod.logger().error("No configuration data was found with ID as '" + id + "'! — by " + Silk.DATA.getName());
		throw new RuntimeException("No configuration data was found with ID as '" + id + "'! — by " + Silk.DATA.getName());
	}
	
	/**
	 * @return 配置读取器
	 */
	@SilkApi
	public ConfigReader reader() {
		return reader;
	}
	
	/**
	 * @return 配置写入器
	 */
	@SilkApi
	public ConfigWriter writer() {
		return writer;
	}
	
	/**
	 * @return 配置类型
	 */
	@SilkApi
	public Type getType() {
		return type;
	}
	
	/**
	 * 配置文件类型
	 */
	public enum Type {
		/**
		 * 玩家可更改配置，会显示在配置屏幕上
		 */
		GAMER,
		/**
		 * 开发用配置选项，不会显示在配置屏幕上
		 */
		DEV,
		/**
		 * 已经弃用了的配置选项，会以黄色斜体字体显示，并且玩家无法点击，只能通过模组或配置文件修改
		 */
		DEPRECATED,
		/**
		 * 实验性的配置选项，会以红色字体显示在配置屏幕上
		 */
		EXPERIMENTAL
	}
	
	/**
	 * 保存配置文件保存格式
	 */
	public enum Mode {
		PROPERTIES, XML, JSON, TOML
	}
	
	/**
	 * 构建配置数据
	 */
	@SilkApi
	public static final class Builder {
		private ModBasicData mod;
		private Type type;
		private Mode mode;
		private LinkedHashMap<String, Object> configs;
		private LinkedHashMap<String, Object> defaults;
		
		private Builder(@NotNull ModBasicData mod) {
			this.mod = mod;
			this.type = Type.GAMER;
			this.mode = Mode.TOML;
			this.configs = Maps.newLinkedHashMapWithExpectedSize(10);
			this.defaults = Maps.newLinkedHashMapWithExpectedSize(10);
		}
		
		private Builder(@NotNull ConfigData defaults) {
			this.mod = defaults.mod;
			this.type = defaults.type;
			this.mode = defaults.mode;
			this.configs = defaults.configs;
			this.defaults = defaults.defaults;
		}
		
		@SilkApi
		public Builder mod(ModBasicData mod) {
			this.mod = mod;
			return this;
		}
		
		@SilkApi
		public Builder type(Type type) {
			this.type = type;
			return this;
		}
		
		@SilkApi
		public Builder mode(Mode mode) {
			this.mode = mode;
			return this;
		}
		
		@SilkApi
		public Builder defaults(ConfigData defaults) {
			this.configs = defaults.configs;
			this.defaults = defaults.defaults;
			return this;
		}
		
		@SilkApi
		public ConfigData build() {
			return new ConfigData(mod, type, mode, configs, defaults);
		}
	}
}
