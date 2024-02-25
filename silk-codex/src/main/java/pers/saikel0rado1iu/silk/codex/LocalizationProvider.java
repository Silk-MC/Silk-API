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

import pers.saikel0rado1iu.silk.base.common.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkCodex;

/**
 * <h2 style="color:FFC800">国际化 Silk API: Codex 提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class InternationalizationProvider extends I18nModInfoProvider {
	static final InternationalizationProvider EN_US = new InternationalizationProvider("en_us",
			"Silk API: Codex",
			"Sub-API for creating mod configurations.",
			"This system includes related utilities in Silk API for creating, " +
					"reading, writing mod configurations and switching configuration modes.");
	static final InternationalizationProvider ZH_CN = new InternationalizationProvider("zh_cn",
			"丝绸开发库：法典",
			"用于创建模组配置的子开发库。",
			"此系统包含了丝绸开发库中用于创建、读取、写入模组配置与切换配置模式的相关工具。");
	static final InternationalizationProvider ZH_HK = new InternationalizationProvider("zh_hk",
			"絲綢開發庫：法典",
			"用於創建模組配置嘅子開發庫。",
			"此系統包含了絲綢開發庫中用於創建、讀取、寫入模組配置與切換配置模式嘅相關工具。");
	static final InternationalizationProvider ZH_TW = new InternationalizationProvider("zh_tw",
			"絲綢開發庫：法典",
			"用於建立模組配置的子開發庫。",
			"此系統包含了絲綢開發庫中用於建立、讀取、寫入模組配置與切換配置模式的相關工具。");
	
	private InternationalizationProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkCodex.DATA.getId(), name, summary, description);
	}
}
