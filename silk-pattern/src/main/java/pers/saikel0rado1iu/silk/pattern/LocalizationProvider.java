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

package pers.saikel0rado1iu.silk.pattern;

import pers.saikel0rado1iu.silk.base.common.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkPattern;

/**
 * <h2 style="color:FFC800">国际化 Silk API: Pattern 提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class InternationalizationProvider extends I18nModInfoProvider {
	static final InternationalizationProvider EN_US = new InternationalizationProvider("en_us",
			"Silk API: Pattern",
			"Sub API for creating advanced screens.",
			"This system includes the Silk API for screen creation and design-related utilities, " +
					"and the system also provides a series of screen templates for mod creators to use.");
	static final InternationalizationProvider ZH_CN = new InternationalizationProvider("zh_cn",
			"丝绸开发库：图案",
			"用于创建高级屏幕的子开发库。",
			"此系统包含了丝绸开发库中用于屏幕创建与设计有关的实用程序，并且此系统同时提供一系列屏幕模板以供模组创作者使用。");
	static final InternationalizationProvider ZH_HK = new InternationalizationProvider("zh_hk",
			"絲綢開發庫：圖案",
			"用於創建高級屏幕嘅子開發庫。",
			"此系統包含了絲綢開發庫中用于屏幕创建与设计有关嘅实用程序，并且此系统同时提供一系列屏幕模板以供模组创作者使用。");
	static final InternationalizationProvider ZH_TW = new InternationalizationProvider("zh_tw",
			"絲綢開發庫：圖案",
			"用於建立高階螢幕的子開發庫。",
			"此系統包含了絲綢開發庫中用於螢幕建立與設計有關的實用程式，並且此系統同時提供一系列螢幕模板以供模組創作者使用。");
	
	private InternationalizationProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkPattern.DATA.getId(), name, summary, description);
	}
}
