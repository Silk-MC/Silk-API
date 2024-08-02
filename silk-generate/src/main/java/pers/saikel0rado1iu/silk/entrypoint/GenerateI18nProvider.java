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

import pers.saikel0rado1iu.silk.api.base.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Generate 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class GenerateI18nProvider extends I18nModInfoProvider {
	static final GenerateI18nProvider EN_US = new GenerateI18nProvider("en_us",
			"Silk API: Generate",
			"Sub-API for improved data generation.",
			"This system includes a new and improved data generation system in the Silk API and a variety of " +
					"utilities that can help modders greatly speed up the production of generateable files.");
	static final GenerateI18nProvider ZH_CN = new GenerateI18nProvider("zh_cn",
			"丝绸开发库：生成",
			"用于改进数据生成的子开发库。",
			"此系统包含了丝绸开发库中改进的新数据生成系统与多种实用程序，可以帮助模组作者极大地加速可生成文件的制作。");
	static final GenerateI18nProvider ZH_HK = new GenerateI18nProvider("zh_hk",
			"絲綢開發庫：生成",
			"用於改進數據生成嘅子開發庫。",
			"此系統包含了絲綢開發庫中改進嘅新數據生成系統與多種實用程序，可以幫助模組作者極大地加速可生成文件嘅製作。");
	static final GenerateI18nProvider ZH_TW = new GenerateI18nProvider("zh_tw",
			"絲綢開發庫：生成",
			"用於改進資料生成的子開發庫。",
			"此系統包含了絲綢開發庫中改進的新資料生成系統與多種實用程式，可以幫助模組作者極大地加速可生成檔案的製作。");
	
	private GenerateI18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkGenerate.getInstance().id(), name, summary, description);
	}
}
