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

package pers.saikel0rado1iu.silk.api.pattern.widget;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

/**
 * <h2 style="color:FFC800">控件文本</h2>
 * 有关控件文本的提供方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface WidgetTexts {
	/**
	 * 提供控件标题
	 *
	 * @param modPass 模组通
	 * @param key     文本键
	 * @return 可翻译文本
	 */
	static MutableText title(ModPass modPass, String key) {
		return Text.translatable(titleKey(modPass, key));
	}
	
	/**
	 * 提供控件标题键
	 *
	 * @param modPass 模组通
	 * @param key     文本键
	 * @return 文本翻译键
	 */
	static String titleKey(ModPass modPass, String key) {
		return String.format("%s.%s.title", modPass.modData().id(), key);
	}
	
	/**
	 * 提供控件文本
	 *
	 * @param modPass 模组通
	 * @param key     文本键
	 * @return 可翻译文本
	 */
	static MutableText text(ModPass modPass, String key) {
		return Text.translatable(textKey(modPass, key));
	}
	
	/**
	 * 提供控件文本键
	 *
	 * @param modPass 模组通
	 * @param key     文本键
	 * @return 文本翻译键
	 */
	static String textKey(ModPass modPass, String key) {
		return String.format("%s.%s.text", modPass.modData().id(), key);
	}
}
