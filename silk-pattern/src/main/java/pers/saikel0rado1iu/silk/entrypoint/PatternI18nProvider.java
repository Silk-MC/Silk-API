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

package pers.saikel0rado1iu.silk.entrypoint;

import pers.saikel0rado1iu.silk.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkPattern;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Pattern 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class PatternI18nProvider extends I18nModInfoProvider {
	static final PatternI18nProvider EN_US = new PatternI18nProvider("en_us",
			"Silk API: Pattern",
			"Sub API for creating advanced screens.",
			"This system includes the Silk API for screen creation and design-related utilities, " +
					"and the system also provides a series of screen templates for mod creators to use.");
	static final PatternI18nProvider ZH_CN = new PatternI18nProvider("zh_cn",
			"丝绸开发库：图案",
			"用于创建高级屏幕的子开发库。",
			"此系统包含了丝绸开发库中用于屏幕创建与设计有关的实用程序，并且此系统同时提供一系列屏幕模板以供模组创作者使用。");
	static final PatternI18nProvider ZH_HK = new PatternI18nProvider("zh_hk",
			"絲綢開發庫：圖案",
			"用於創建高級屏幕嘅子開發庫。",
			"此系統包含了絲綢開發庫中用于屏幕创建与设计有关嘅实用程序，并且此系统同时提供一系列屏幕模板以供模组创作者使用。");
	static final PatternI18nProvider ZH_TW = new PatternI18nProvider("zh_tw",
			"絲綢開發庫：圖案",
			"用於建立高階螢幕的子開發庫。",
			"此系統包含了絲綢開發庫中用於螢幕建立與設計有關的實用程式，並且此系統同時提供一系列螢幕模板以供模組創作者使用。");
	
	private PatternI18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkPattern.getInstance().id(), name, summary, description);
	}
}
