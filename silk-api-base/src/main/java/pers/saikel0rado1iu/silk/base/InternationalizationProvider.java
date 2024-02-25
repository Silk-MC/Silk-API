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

package pers.saikel0rado1iu.silk.base;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import pers.saikel0rado1iu.silk.base.common.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkId;

/**
 * <h2 style="color:FFC800">国际化 Silk API 提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class InternationalizationProvider extends I18nModInfoProvider {
	static final InternationalizationProvider EN_US = new InternationalizationProvider("en_us",
			"Silk API",
			"Fabric API extensions for making fancy functionality.",
			"Advanced Fabric API extensions for adding fancy mod items, blocks, mobs and features. " +
					"The goal is to make it easy for fabric mod developers to produce a variety of advanced operations.",
			"Silk API: Base",
			"Sub-API for providing basic utils.",
			"This system contains the most commonly used and basic interfaces and practical utils in Silk API, " +
					"which are used by other systems in Silk API.",
			"Support: Afdian",
			"Support: Patreon");
	static final InternationalizationProvider ZH_CN = new InternationalizationProvider("zh_cn",
			"丝绸开发库",
			"用于制作具有牛逼哄哄功能的 Fabric API 扩展。",
			"用于添加牛逼哄哄的模组物品、方块、实体与特性的高级_Fabric_API_扩展。其目的在于方便_Fabric_模组开发者进行各种高级操作。",
			"丝绸开发库：基础",
			"用于提供基础实用程序的子开发库。",
			"此系统包含了丝绸开发库中最常用、最基础的接口与实用工具，用于给丝绸开发库中的其他系统使用。",
			"支持作者：爱发电",
			"支持作者：Patreon");
	static final InternationalizationProvider ZH_HK = new InternationalizationProvider("zh_hk",
			"絲綢開發庫",
			"用於製作具有牛逼哄哄功能嘅 Fabric API 擴展。",
			"用於添加牛逼哄哄嘅模組物品、方塊、實體與特性嘅高級_Fabric_API_擴展。其目嘅在於方便_Fabric_模組開發者進行各種高級操作。",
			"絲綢開發庫：基礎",
			"用於提供基礎實用程序嘅子開發庫。",
			"此系統包含了絲綢開發庫中最常用、最基礎嘅接口與實用工具，用於給絲綢開發庫中嘅其他系統使用。",
			"支持作者：愛發電",
			"支持作者：Patreon");
	static final InternationalizationProvider ZH_TW = new InternationalizationProvider("zh_tw",
			"絲綢開發庫",
			"用於製作具有牛逼哄哄功能的 Fabric API 擴充套件。",
			"用於新增牛逼哄哄的模組物品、方塊、實體與特性的高階_Fabric_API_擴充套件。其目的在於方便_Fabric_模組開發者進行各種高階操作。",
			"絲綢開發庫：基礎",
			"用於提供基礎實用程式的子開發庫。",
			"此系統包含了絲綢開發庫中最常用、最基礎的介面與實用工具，用於給絲綢開發庫中的其他系統使用。",
			"支援作者：愛發電",
			"支援作者：Patreon");
	private final String apiName;
	private final String apiSummary;
	private final String apiDesc;
	private final String afdian;
	private final String patreon;
	
	private InternationalizationProvider(String langCode, String apiName, String apiSummary, String apiDesc, String name, String summary, String description, String afdian, String patreon) {
		super(langCode, SilkId.SILK_API_BASE, name, summary, description);
		this.apiName = apiName;
		this.apiSummary = apiSummary;
		this.apiDesc = apiDesc;
		this.afdian = afdian;
		this.patreon = patreon;
	}
	
	@Override
	protected void generate(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(NAME_KEY + SilkId.SILK_API, apiName);
		translationBuilder.add(SUMMARY_KEY + SilkId.SILK_API, apiSummary);
		translationBuilder.add(DESC_KEY + SilkId.SILK_API, apiDesc);
		super.generate(translationBuilder);
		translationBuilder.add("modmenu.support.afdian", afdian);
		translationBuilder.add("modmenu.support.patreon", patreon);
	}
}
