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

package pers.saikel0rado1iu.silk.base.common;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

/**
 * <h2 style="color:FFC800">国际化模组信息提供器</h2>
 * <p style="color:FFC800">
 * 用于国际化模组的基本信息，如模组名称、摘要和简介的提供器。国际化词条基于 ModMenu 模组。
 * </p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class I18nModInfoProvider {
	public static final String NAME_KEY = "modmenu.nameTranslation.";
	public static final String SUMMARY_KEY = "modmenu.summaryTranslation.";
	public static final String DESC_KEY = "modmenu.descriptionTranslation.";
	protected final String langCode;
	protected final String modId;
	protected final String name;
	protected final String summary;
	protected final String description;
	
	public I18nModInfoProvider(String langCode, String modId, String name, String summary, String description) {
		this.langCode = langCode;
		this.modId = modId;
		this.name = name;
		this.summary = summary;
		this.description = description;
	}
	
	protected void generate(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		translationBuilder.add(NAME_KEY + modId, name);
		translationBuilder.add(SUMMARY_KEY + modId, summary);
		translationBuilder.add(DESC_KEY + modId, description);
	}
	
	public FabricLanguageProvider provider(FabricDataOutput fabricDataOutput) {
		return new FabricLanguageProvider(fabricDataOutput, langCode) {
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				generate(translationBuilder);
			}
		};
	}
}
