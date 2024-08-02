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
import pers.saikel0rado1iu.silk.impl.SilkMagicCube;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Magic Cube 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class MagicCubeI18nProvider extends I18nModInfoProvider {
	static final MagicCubeI18nProvider EN_US = new MagicCubeI18nProvider("en_us",
			"Silk API: Magic Cube",
			"Sub-API for creating advanced blocks.",
			"This system includes all advanced block-related interfaces in the Silk API such as building blocks, " +
					"redstone blocks, interactive blocks, block entities, etc., and lets modders create unique blocks.");
	static final MagicCubeI18nProvider ZH_CN = new MagicCubeI18nProvider("zh_cn",
			"丝绸开发库：魔方",
			"用于创建高级方块的子开发库。",
			"此系统包含了丝绸开发库中如建筑方块、红石方块、可互动块、方块实体等一切与方块有关的高级接口，供模组作者创建独具特色的方块。");
	static final MagicCubeI18nProvider ZH_HK = new MagicCubeI18nProvider("zh_hk",
			"絲綢開發庫：魔方",
			"用於創建高級方塊嘅子開發庫。",
			"此系統包含了絲綢開發庫中如建築方塊、紅石方塊、可互動塊、方塊實體等一切與方塊有關嘅高級接口，供模組作者創建獨具特色嘅方塊。");
	static final MagicCubeI18nProvider ZH_TW = new MagicCubeI18nProvider("zh_tw",
			"絲綢開發庫：魔方",
			"用於建立高階方塊的子開發庫。",
			"此系統包含了絲綢開發庫中如建築方塊、紅石方塊、可互動塊、方塊實體等一切與方塊有關的高階介面，供模組作者建立獨具特色的方塊。");
	
	private MagicCubeI18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkMagicCube.getInstance().id(), name, summary, description);
	}
}
