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

package pers.saikel0rado1iu.silk.common.api;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

/**
 * <h2 style="color:FFC800">国际化模组信息提供器</h2>
 * 用于国际化模组的基本信息，如模组名称、摘要和简介的提供器<br>
 * 国际化词条基于 ModMenu 模组
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class I18nModInfoProvider {
	/**
	 * ModMenu 全球化名称键前缀
	 */
	public static final String MOD_MENU_I18N_NAME_KEY_PREFIX = "modmenu.nameTranslation.";
	/**
	 * ModMenu 全球化摘要键前缀
	 */
	public static final String MOD_MENU_I18N_SUMMARY_KEY_PREFIX = "modmenu.summaryTranslation.";
	/**
	 * ModMenu 全球化简介键前缀
	 */
	public static final String MOD_MENU_I18N_DESC_KEY_PREFIX = "modmenu.descriptionTranslation.";
	/**
	 * 语言代码，如 en_us
	 */
	protected final String langCode;
	/**
	 * 模组的唯一标识符
	 */
	protected final String modId;
	/**
	 * 模组在此语言代码中的翻译名称
	 */
	protected final String name;
	/**
	 * 模组在此语言代码中的翻译摘要
	 */
	protected final String summary;
	/**
	 * 模组在此语言代码中的翻译简介
	 */
	protected final String description;
	
	/**
	 * @param langCode    语言代码
	 * @param modId       模组 ID
	 * @param name        模组在此语言代码中的翻译名称
	 * @param summary     模组在此语言代码中的翻译摘要
	 * @param description 模组在此语言代码中的翻译简介
	 */
	public I18nModInfoProvider(String langCode, String modId, String name, String summary, String description) {
		this.langCode = langCode;
		this.modId = modId;
		this.name = name;
		this.summary = summary;
		this.description = description;
	}
	
	/**
	 * 本地化数据生成
	 *
	 * @param translationBuilder {@link FabricLanguageProvider.TranslationBuilder}
	 */
	protected void generate(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(MOD_MENU_I18N_NAME_KEY_PREFIX + modId, name);
		translationBuilder.add(MOD_MENU_I18N_SUMMARY_KEY_PREFIX + modId, summary);
		translationBuilder.add(MOD_MENU_I18N_DESC_KEY_PREFIX + modId, description);
	}
	
	/**
	 * 用于 {@link net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack#addProvider(FabricDataGenerator.Pack.Factory)} 注册 <br>
	 * 覆盖 {@link I18nModInfoProvider#generate(FabricLanguageProvider.TranslationBuilder)} 以修改全球化数据生成
	 *
	 * @param fabricDataOutput {@link FabricDataOutput}
	 * @return {@link FabricLanguageProvider}
	 */
	public FabricLanguageProvider provider(FabricDataOutput fabricDataOutput) {
		return new FabricLanguageProvider(fabricDataOutput, langCode) {
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				generate(translationBuilder);
			}
		};
	}
}
