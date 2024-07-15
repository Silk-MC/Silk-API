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

package pers.saikel0rado1iu.silk.codex;

import com.google.common.collect.Maps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.codex.stream.SettingLoader;
import pers.saikel0rado1iu.silk.codex.stream.SettingStorage;
import pers.saikel0rado1iu.silk.impl.SilkCodex;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">设置数据</h2>
 * 用于创建模组设置数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class SettingData implements ModPass {
	/**
	 * 设置路径
	 */
	public static final Path SETTING_PATH = FabricLoader.getInstance().getConfigDir();
	/**
	 * 设置文件所使用的字符集
	 */
	public static final Charset CHARSET = StandardCharsets.UTF_8;
	private static final String OVERWRITTEN_OPTION_WARN = "Overwritten option: Option '{}' has been added repeatedly, which may lead to unexpected consequences.";
	private final LinkedHashMap<SettingOption<?>, Object[]> options;
	private final ModPass modPass;
	private final SettingType type;
	private final SettingFormat format;
	private final SettingLoader loader;
	private final SettingStorage storage;
	private final Runnable loadSettings;
	private final Runnable saveSettings;
	private final AtomicBoolean modifying = new AtomicBoolean(false);
	
	private SettingData(ModPass modPass, LinkedHashMap<SettingOption<?>, Object[]> options, SettingType type, SettingFormat format, Consumer<SettingLoader> loadSettings, Consumer<SettingStorage> saveSettings) {
		this.modPass = modPass;
		this.options = options;
		this.type = type;
		this.format = format;
		this.loader = SettingLoader.create(this);
		this.storage = SettingStorage.create(this);
		this.loadSettings = () -> loadSettings.accept(this.loader);
		this.saveSettings = () -> saveSettings.accept(this.storage);
		load();
	}
	
	/**
	 * 设置数据的构造器
	 *
	 * @param modPass 设置数据所在的模组通
	 * @return 构造器
	 */
	public static Builder builder(ModPass modPass) {
		return new Builder(modPass);
	}
	
	/**
	 * 设置数据的构造器
	 *
	 * @param settingData 设置数据中储存的基础选项
	 * @return 构造器
	 */
	public static Builder builder(SettingData settingData) {
		return new Builder(settingData);
	}
	
	private static <T> T[] createValues(Class<T[]> clazz, T defaultValue) {
		@SuppressWarnings("unchecked")
		T[] values = (T[]) Array.newInstance(clazz.getComponentType(), OptionValueIndex.values().length);
		values[OptionValueIndex.CURRENT.index()] = defaultValue;
		values[OptionValueIndex.DEFAULT.index()] = defaultValue;
		return values;
	}
	
	private static <T> T[] createValues(Class<T[]> clazz, T defaultValue, T min, T max) {
		@SuppressWarnings("unchecked")
		T[] values = (T[]) Array.newInstance(clazz.getComponentType(), OptionValueIndex.values().length);
		values[OptionValueIndex.CURRENT.index()] = defaultValue;
		values[OptionValueIndex.DEFAULT.index()] = defaultValue;
		values[OptionValueIndex.MIN.index()] = min;
		values[OptionValueIndex.MAX.index()] = max;
		return values;
	}
	
	/**
	 * 添加选项
	 *
	 * @param option       设置选项
	 * @param defaultValue 选项的默认值
	 * @param <T>          选项类型中存储的参数类型
	 */
	public synchronized <T> void addOption(SettingOption<T> option, T defaultValue) {
		if (options.keySet().stream().anyMatch(key -> key.id().equals(option.id()))) {
			SilkCodex.getInstance().logger().warn(OVERWRITTEN_OPTION_WARN, option.id());
		}
		if (!option.type().isSlider()) {
			options.put(option, createValues(option.type().arrayClass(), defaultValue));
		} else if (option.type() == OptionType.INT_SLIDER) {
			options.put(option, createValues(option.type().arrayClass(),
					option.type().clazz().cast(defaultValue),
					option.type().clazz().cast(0),
					option.type().clazz().cast(100)));
		} else if (option.type() == OptionType.DOUBLE_SLIDER) {
			options.put(option, createValues(option.type().arrayClass(),
					option.type().clazz().cast(defaultValue),
					option.type().clazz().cast(0F),
					option.type().clazz().cast(1F)));
		}
	}
	
	/**
	 * 添加滑块选项
	 *
	 * @param option       设置选项
	 * @param defaultValue 选项的默认值
	 * @param min          选项的最小值
	 * @param max          选项的最大值
	 * @param <T>          选项类型中存储的参数类型
	 */
	public synchronized <T extends Number> void addOption(SettingOption<T> option, T defaultValue, T min, T max) {
		if (options.keySet().stream().anyMatch(key -> key.id().equals(option.id()))) {
			SilkCodex.getInstance().logger().warn(OVERWRITTEN_OPTION_WARN, option.id());
		}
		options.put(option, createValues(option.type().arrayClass(), defaultValue, min, max));
	}
	
	/**
	 * 获取设置选项
	 *
	 * @param id 设置选项的标识符
	 * @return 设置选项
	 */
	public synchronized SettingOption<?> getOption(Identifier id) {
		Optional<SettingOption<?>> settingOption = options.keySet().stream().filter(option -> option.id().equals(id)).findFirst();
		return settingOption.orElseThrow(() -> {
			String msg = String.format("Nonexistent option: In the settings of %s, there is no option with the identifier %s.", modPass.modData().debugName(), id);
			SilkCodex.getInstance().logger().error(msg);
			return new RuntimeException(msg);
		});
	}
	
	/**
	 * 设定选项的当前值
	 *
	 * @param option 设置选项
	 * @param value  设置值的字符串形式
	 * @param <T>    选项类型中存储的参数类型
	 */
	public synchronized <T> void setValue(SettingOption<T> option, String value) {
		T t = option.type().parse(value);
		try {
			if (t != null) setValue(option, t);
		} catch (Exception e) {
			String msg = String.format("Parsing failed: An illegal data error occurred while reading the setting option '%s'.", option.id());
			SilkCodex.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
		if (t == null) {
			String msg = String.format("Parsing failed: The parsing method provided by the option ‘%s’‘s type returns a null value. Please recheck the parsing method.", option.id());
			SilkCodex.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	/**
	 * 设定选项的当前值
	 *
	 * @param option 设置选项
	 * @param value  设置的值
	 * @param <T>    选项类型中存储的参数类型
	 */
	public synchronized <T> void setValue(SettingOption<T> option, T value) {
		Object[] values = options.get(option);
		values[OptionValueIndex.CURRENT.index()] = value;
		options.put(option, values);
	}
	
	/**
	 * 获取设置选项中的当前的值
	 *
	 * @param option 设置选项
	 * @param <T>    选项类型中存储的参数类型
	 * @return 选项的值
	 */
	public synchronized <T> T getValue(SettingOption<T> option) {
		return getValue(option, OptionValueIndex.CURRENT);
	}
	
	/**
	 * 获取设置选项中的值
	 *
	 * @param option 设置选项
	 * @param index  选项数据索引
	 * @param <T>    选项类型中存储的参数类型
	 * @return 选项的值
	 */
	public synchronized <T> T getValue(SettingOption<T> option, OptionValueIndex index) {
		try {
			Object value = options.get(option)[index.index()];
			return option.type().clazz().cast(value);
		} catch (ClassCastException e) {
			String msg = String.format("Nonexistent value: Option '%s' does not have a %s value. Please check the index parameters.", option.id(), index.key());
			SilkCodex.getInstance().logger().error(msg);
			throw new RuntimeException(msg);
		}
	}
	
	/**
	 * 读取设置文件方法
	 */
	public void load() {
		loadSettings.run();
	}
	
	/**
	 * 保存设置文件方法
	 */
	public void save() {
		saveSettings.run();
	}
	
	/**
	 * 获取设置类型
	 *
	 * @return 设置类型
	 */
	public SettingType type() {
		return type;
	}
	
	/**
	 * 获取设置格式
	 *
	 * @return 设置格式
	 */
	public SettingFormat format() {
		return format;
	}
	
	/**
	 * 获取设置读取器
	 *
	 * @return 设置读取器
	 */
	public SettingLoader loader() {
		return loader;
	}
	
	/**
	 * 获取设置写入器
	 *
	 * @return 设置写入器
	 */
	public SettingStorage storage() {
		return storage;
	}
	
	/**
	 * 获取所有选项
	 *
	 * @return 所有选项
	 */
	@ApiStatus.Internal
	public synchronized LinkedHashMap<SettingOption<?>, Object[]> options() {
		return options;
	}
	
	/**
	 * 是否在修改
	 *
	 * @return 是否在修改
	 */
	public synchronized boolean getModifying() {
		return modifying.get();
	}
	
	/**
	 * 设置正在修改
	 *
	 * @param value 是否正在修改
	 */
	@ApiStatus.Internal
	public synchronized void setModifying(boolean value) {
		modifying.set(value);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModData modData() {
		return modPass.modData();
	}
	
	/**
	 * 设置数据的构造器
	 */
	public static final class Builder {
		private final LinkedHashMap<SettingOption<?>, Object[]> options;
		private ModPass modPass;
		private SettingType type;
		private SettingFormat format;
		private Consumer<SettingLoader> loadSettings;
		private Consumer<SettingStorage> saveSettings;
		
		private Builder(ModPass modPass) {
			this.modPass = modPass;
			this.type = SettingType.DEFAULT;
			this.format = SettingFormat.TOML;
			this.options = Maps.newLinkedHashMapWithExpectedSize(10);
			this.loadSettings = SettingLoader::load;
			this.saveSettings = SettingStorage::save;
		}
		
		private Builder(SettingData settingData) {
			this.modPass = settingData;
			this.type = SettingType.DEFAULT;
			this.format = SettingFormat.TOML;
			this.options = settingData.options;
			this.loadSettings = SettingLoader::load;
			this.saveSettings = SettingStorage::save;
		}
		
		/**
		 * @param modPass 模组通
		 * @return 构建器
		 */
		public Builder modPass(ModPass modPass) {
			this.modPass = modPass;
			return this;
		}
		
		/**
		 * @param type 设置类型
		 * @return 构建器
		 */
		public Builder type(SettingType type) {
			this.type = type;
			return this;
		}
		
		/**
		 * @param format 设置格式
		 * @return 构建器
		 */
		public Builder format(SettingFormat format) {
			this.format = format;
			return this;
		}
		
		/**
		 * @param loadSettings 加载设置
		 * @return 构建器
		 */
		public Builder loadSettings(Consumer<SettingLoader> loadSettings) {
			this.loadSettings = loadSettings;
			return this;
		}
		
		/**
		 * @param saveSettings 保存设置
		 * @return 构建器
		 */
		public Builder saveSettings(Consumer<SettingStorage> saveSettings) {
			this.saveSettings = saveSettings;
			return this;
		}
		
		/**
		 * 添加选项
		 *
		 * @param option       设置选项
		 * @param defaultValue 选项的默认值
		 * @param <T>          选项类型中存储的参数类型
		 * @return 构建器
		 */
		public <T> Builder addOption(SettingOption<T> option, T defaultValue) {
			if (options.keySet().stream().anyMatch(key -> key.id().equals(option.id()))) {
				SilkCodex.getInstance().logger().warn(OVERWRITTEN_OPTION_WARN, option.id());
			}
			if (!option.type().isSlider()) {
				options.put(option, createValues(option.type().arrayClass(), defaultValue));
			} else if (option.type() == OptionType.INT_SLIDER) {
				options.put(option, createValues(option.type().arrayClass(),
						option.type().clazz().cast(defaultValue),
						option.type().clazz().cast(0),
						option.type().clazz().cast(100)));
			} else if (option.type() == OptionType.DOUBLE_SLIDER) {
				options.put(option, createValues(option.type().arrayClass(),
						option.type().clazz().cast(defaultValue),
						option.type().clazz().cast(0.0),
						option.type().clazz().cast(1.0)));
			}
			return this;
		}
		
		/**
		 * 添加滑块选项
		 *
		 * @param option       设置选项
		 * @param defaultValue 选项的默认值
		 * @param min          选项的最小值
		 * @param max          选项的最大值
		 * @param <T>          选项类型中存储的参数类型
		 * @return 构建器
		 */
		public <T extends Number> Builder addOption(SettingOption<T> option, T defaultValue, T min, T max) {
			if (options.keySet().stream().anyMatch(key -> key.id().equals(option.id()))) {
				SilkCodex.getInstance().logger().warn(OVERWRITTEN_OPTION_WARN, option.id());
			}
			options.put(option, createValues(option.type().arrayClass(), defaultValue, min, max));
			return this;
		}
		
		/**
		 * 构造设置数据
		 *
		 * @return 设置数据
		 */
		public SettingData build() {
			return new SettingData(modPass, options, type, format, loadSettings, saveSettings);
		}
	}
}
