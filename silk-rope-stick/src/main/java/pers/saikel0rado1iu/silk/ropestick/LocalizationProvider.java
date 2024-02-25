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

package pers.saikel0rado1iu.silk.ropestick;

import pers.saikel0rado1iu.silk.base.common.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

/**
 * <h2 style="color:FFC800">国际化 Silk API: Rope, Stick 提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class InternationalizationProvider extends I18nModInfoProvider {
	static final InternationalizationProvider EN_US = new InternationalizationProvider("en_us",
			"Silk API: Rope, Stick",
			"Sub-API for creating advanced items.",
			"This system includes all advanced interfaces related to items in the Silk API, such as tools, " +
					"weapons, crossbows, equipment, food, etc., and lets modders create unique items.");
	static final InternationalizationProvider ZH_CN = new InternationalizationProvider("zh_cn",
			"丝绸开发库：绳棒",
			"用于创建高级物品的子开发库。",
			"此系统包含了丝绸开发库中如工具、武器、弓弩、装备、食物等一切与物品有关的高级接口，供模组作者创建独具特色的物品。");
	static final InternationalizationProvider ZH_HK = new InternationalizationProvider("zh_hk",
			"絲綢開發庫：繩棒",
			"用於创建高级物品嘅子開發庫。",
			"此系統包含了絲綢開發庫中如工具、武器、弓弩、裝備、食物等一切與物品有關嘅高級接口，供模組作者創建獨具特色嘅物品。");
	static final InternationalizationProvider ZH_TW = new InternationalizationProvider("zh_tw",
			"絲綢開發庫：繩棒",
			"用於创建高级物品的子開發庫。",
			"此系統包含了絲綢開發庫中如工具、武器、弓弩、裝備、食物等一切與物品有關的高階介面，供模組作者建立獨具特色的物品。");
	
	private InternationalizationProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkRopeStick.DATA.getId(), name, summary, description);
	}
}
