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

package pers.saikel0rado1iu.silk.entrypoint.spinningjenny;

import pers.saikel0rado1iu.silk.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Spinning Jenny 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class I18nProvider extends I18nModInfoProvider {
	static final I18nProvider EN_US = new I18nProvider("en_us",
			"Silk API: Spinning Jenny",
			"Sub-API for improved mod content registration.",
			"This system includes an improved mod content registration system in the Silk API. At the same time, it absorbs the advantages of both Forge and Fabric and adds many new features.");
	static final I18nProvider ZH_CN = new I18nProvider("zh_cn",
			"丝绸开发库：珍妮机",
			"用于改进模组内容注册的子开发库。",
			"此系统包含了丝绸开发库中改进的模组内容注册系统。同时吸收了_Forge_与_Fabric_的两者优点并添加了众多新功能。");
	static final I18nProvider ZH_HK = new I18nProvider("zh_hk",
			"絲綢開發庫：珍妮機",
			"用於改進模組內容註冊嘅子開發庫。",
			"此系統包含了絲綢開發庫中改進嘅模組內容註冊系統。同時吸收了_Forge_與_Fabric_嘅兩者優點並添加了眾多新功能。");
	static final I18nProvider ZH_TW = new I18nProvider("zh_tw",
			"絲綢開發庫：珍妮機",
			"用於改進模組內容註冊的子開發庫。",
			"此系統包含了絲綢開發庫中改進的模組內容註冊系統。同時吸收了_Forge_與_Fabric_的兩者優點並新增了眾多新功能。");
	
	private I18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkSpinningJenny.getInstance().id(), name, summary, description);
	}
}
