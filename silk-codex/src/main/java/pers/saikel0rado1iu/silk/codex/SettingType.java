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

import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * <h2 style="color:FFC800">设置类型</h2>
 * 设置的类型会影响设置选项以何种方式呈现在设置屏幕或设置选项卡中
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public enum SettingType {
	/**
	 * 默认设置<br>
	 * 玩家可更改，会显示在设置屏幕上
	 */
	DEFAULT,
	/**
	 * 开发用设置<br>
	 * 不会显示在设置屏幕上
	 */
	DEVELOPMENT,
	/**
	 * 已经弃用了的设置<br>
	 * 会以灰色字体显示，并且玩家无法点击，只能通过模组或设置文件修改
	 */
	DEPRECATED,
	/**
	 * 实验性的设置<br>
	 * 玩家可更改，会以黄色斜体字体显示
	 */
	EXPERIMENTAL;
	
	/**
	 * 获取格式化文本
	 *
	 * @param text 需要格式化的文本
	 * @return 已格式化文本
	 */
	public Text getFormatText(Text text) {
		return switch (this) {
			case DEFAULT -> text;
			case DEVELOPMENT -> Text.empty();
			case DEPRECATED -> text.copy().formatted(Formatting.GRAY);
			case EXPERIMENTAL -> text.copy().formatted(Formatting.YELLOW, Formatting.ITALIC);
		};
	}
}
