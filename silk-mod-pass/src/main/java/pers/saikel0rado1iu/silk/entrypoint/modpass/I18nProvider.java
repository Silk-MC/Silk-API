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

package pers.saikel0rado1iu.silk.entrypoint.modpass;

import pers.saikel0rado1iu.silk.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkModPass;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Mod Pass 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class I18nProvider extends I18nModInfoProvider {
	static final I18nProvider EN_US = new I18nProvider("en_us",
			"Silk API: Mod Pass",
			"Sub-API for manipulating mod data.",
			"This system includes a program interface for operating mod data in Silk API, making it easier to access mod data. Almost all advanced systems in the API are developed using this interface.");
	static final I18nProvider ZH_CN = new I18nProvider("zh_cn",
			"丝绸开发库：模组通",
			"用于操作模组数据的子开发库。",
			"此系统包含了丝绸开发库中操作模组数据的程序接口，使其可以更便捷地访问模组数据。开发库中几乎所有的高级系统都使用了此接口进行开发。");
	static final I18nProvider ZH_HK = new I18nProvider("zh_hk",
			"絲綢開發庫：模組通",
			"用於操作模組數據嘅子開發庫。",
			"此系統包含了絲綢開發庫中操作模組數據嘅程序接口，使其可以更便捷地訪問模組數據。開發庫中幾乎所有嘅高級系統都使用了此接口進行開發。");
	static final I18nProvider ZH_TW = new I18nProvider("zh_tw",
			"絲綢開發庫：模組通",
			"用於操作模組資料的子開發庫。",
			"此系統包含了絲綢開發庫中操作模組資料的程式介面，使其可以更便捷地訪問模組資料。開發庫中幾乎所有的高階系統都使用了此介面進行開發。");
	
	private I18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkModPass.getInstance().id(), name, summary, description);
	}
}
