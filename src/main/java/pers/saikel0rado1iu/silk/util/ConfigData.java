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
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.Map;

/**
 * <p><b style="color:FFC800"><font size="+1">用于创建模组配置数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
final public class ConfigData<T extends ModBasicData> {
	private final T modData;
	private final Map<String, Object> configs;
	
	@SilkApi
	public ConfigData(T modData) {
		this(modData, null);
	}
	
	@SilkApi
	public ConfigData(ConfigData<T> defaults) {
		this(defaults.modData, defaults);
	}
	
	@SilkApi
	public ConfigData(T modData, ConfigData<?> defaults) {
		this.modData = modData;
		this.configs = defaults == null ? Maps.newHashMapWithExpectedSize(10) : defaults.configs;
	}
	
	public void add(String configId, Boolean object) {
		configs.put(configId, object);
	}
	
	public void load() {
	}
	
	/**
	 * {@link ConfigData#save(Type)} && ({@link Type} == {@link  Type#TOML})
	 */
	@SilkApi
	public void save() {
		save(Type.TOML);
	}
	
	@SilkApi
	public void save(Type saveMode) {
		switch (saveMode) {
			case PROPERTIES -> Silk.DATA.logger().info("ppt");
			case XML -> Silk.DATA.logger().info("xml");
			case JSON -> Silk.DATA.logger().info("json");
			case TOML -> Silk.DATA.logger().info("toml");
		}
	}
	
	public enum Type {
		PROPERTIES, XML, JSON, TOML
	}
}
