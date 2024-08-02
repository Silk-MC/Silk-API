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
import pers.saikel0rado1iu.silk.impl.SilkSpore;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Spore 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class SporeI18nProvider extends I18nModInfoProvider {
	static final SporeI18nProvider EN_US = new SporeI18nProvider("en_us",
			"Silk API: Spore",
			"Sub-API for creating advanced entities.",
			"This system includes parts of the Silk API related to modifying entity behavior, " +
					"customizing entity models, creating entity animations, and other aspects related to advanced entity creation.");
	static final SporeI18nProvider ZH_CN = new SporeI18nProvider("zh_cn",
			"丝绸开发库：孢子",
			"用于创建高级实体的子开发库。",
			"此系统包含了丝绸开发库中有关修改实体行为、自定义实体模型、制作实体动画等与高级实体创建有关的部分。");
	static final SporeI18nProvider ZH_HK = new SporeI18nProvider("zh_hk",
			"絲綢開發庫：孢子",
			"用於創建高級實體嘅子開發庫。",
			"此系統包含了絲綢開發庫中有關修改實體行為、自定義實體模型、製作實體動畫等與高級實體創建有關嘅部分。");
	static final SporeI18nProvider ZH_TW = new SporeI18nProvider("zh_tw",
			"絲綢開發庫：孢子",
			"用於建立高階實體的子開發庫。",
			"此系統包含了絲綢開發庫中有關修改實體行為、自定義實體模型、製作實體動畫等與高階實體建立有關的部分。");
	
	private SporeI18nProvider(String langCode, String name, String summary, String description) {
		super(langCode, SilkSpore.getInstance().id(), name, summary, description);
	}
}
