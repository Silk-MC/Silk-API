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

package pers.saikel0rado1iu.silk.api.codex;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

/**
 * <h2 style="color:FFC800">选项文本</h2>
 * 有关选项文本的提供方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface OptionTexts {
	/**
	 * 提供设置数据根的文本
	 *
	 * @param settingData 设置数据
	 * @return 可翻译文本
	 */
	static MutableText root(SettingData settingData) {
		return Text.translatable(rootKey(settingData));
	}
	
	/**
	 * 提供设置数据根的文本键
	 *
	 * @param settingData 设置数据
	 * @return 文本翻译键
	 */
	static String rootKey(SettingData settingData) {
		return String.format("%s.option.root.text", settingData.modData().id());
	}
	
	/**
	 * 提供选项文本
	 *
	 * @param option 设置选项
	 * @return 可翻译文本
	 */
	static MutableText text(SettingOption<?> option) {
		return Text.translatable(textKey(option));
	}
	
	/**
	 * 提供选项文本键
	 *
	 * @param option 设置选项
	 * @return 文本翻译键
	 */
	static String textKey(SettingOption<?> option) {
		return String.format("%s.option.%s.text", option.id().getNamespace(), option.id().getPath());
	}
	
	/**
	 * 提供选项提示
	 *
	 * @param option 设置选项
	 * @return 可翻译文本
	 */
	static MutableText tip(SettingOption<?> option) {
		return Text.translatable(tipKey(option));
	}
	
	/**
	 * 提供选项提示键
	 *
	 * @param option 设置选项
	 * @return 文本翻译键
	 */
	static String tipKey(SettingOption<?> option) {
		return String.format("%s.option.%s.tip", option.id().getNamespace(), option.id().getPath());
	}
	
	/**
	 * 提供选项文本
	 *
	 * @param option    设置选项
	 * @param keySuffix 键的后缀
	 * @return 可翻译文本
	 */
	static MutableText text(SettingOption<?> option, String keySuffix) {
		return Text.translatable(textKey(option, keySuffix));
	}
	
	/**
	 * 提供选项文本键
	 *
	 * @param option    设置选项
	 * @param keySuffix 键的后缀
	 * @return 文本翻译键
	 */
	static String textKey(SettingOption<?> option, String keySuffix) {
		return String.format("%s.option.%s.%s.text", option.id().getNamespace(), option.id().getPath(), keySuffix);
	}
	
	/**
	 * 提供选项提示
	 *
	 * @param option    设置选项
	 * @param keySuffix 键的后缀
	 * @return 可翻译文本
	 */
	static MutableText tip(SettingOption<?> option, String keySuffix) {
		return Text.translatable(tipKey(option, keySuffix));
	}
	
	/**
	 * 提供选项提示键
	 *
	 * @param option    设置选项
	 * @param keySuffix 键的后缀
	 * @return 文本翻译键
	 */
	static String tipKey(SettingOption<?> option, String keySuffix) {
		return String.format("%s.option.%s.%s.tip", option.id().getNamespace(), option.id().getPath(), keySuffix);
	}
}
