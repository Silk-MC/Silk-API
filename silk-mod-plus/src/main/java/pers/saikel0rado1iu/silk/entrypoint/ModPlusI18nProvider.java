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
import pers.saikel0rado1iu.silk.impl.SilkModPlus;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Mod Plus 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
final class ModPlusI18nProvider extends I18nModInfoProvider {
	static final ModPlusI18nProvider EN_US = new ModPlusI18nProvider("en_us",
			"Silk API: Mod +",
			"Sub-API used to implement modify and features for additional mods.",
			"This system includes most of the functions needed for additional mod development in the Silk API," +
					" allowing mod authors to dynamically modify content across multiple mods without needing to use Mixin.");
	static final ModPlusI18nProvider ZH_CN = new ModPlusI18nProvider("zh_cn",
			"丝绸开发库：模组+",
			"用于实现附加模组的修改与功能的子开发库。",
			"此系统包含了丝绸开发库中针对模组进行附加模组开发所需的大部分功能，可以帮助模组作者不需要使用 Mixin 也能够做到多模组内容的动态修改。");
	static final ModPlusI18nProvider ZH_HK = new ModPlusI18nProvider("zh_hk",
			"絲綢開發庫：模組+",
			"用於實現附加模組嘅修改與功能嘅子開發庫。",
			"此系統包含咗絲綢開發庫中針對模組進行附加模組開發所需嘅大部分功能，可以幫助模組作者唔需要使用 Mixin 也能够做到多模組內容嘅動態修改。");
	static final ModPlusI18nProvider ZH_TW = new ModPlusI18nProvider("zh_tw",
			"絲綢開發庫：模組+",
			"用於實現附加模組的修改與功能的子開發庫。",
			"此系統包含了絲綢開發庫中針對模組進行附加模組開發所需的大部分功能，可以幫助模組作者不需要使用 Mixin 也能够做到多模組內容的動態修改。");
	
	private ModPlusI18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkModPlus.getInstance().id(), name, summary, description);
	}
}
