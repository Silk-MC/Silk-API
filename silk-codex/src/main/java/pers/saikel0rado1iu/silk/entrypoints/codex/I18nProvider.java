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

package pers.saikel0rado1iu.silk.entrypoints.codex;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import pers.saikel0rado1iu.silk.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.impl.SilkCodex;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;

import java.util.Arrays;
import java.util.Iterator;

/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Codex 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class I18nProvider extends I18nModInfoProvider {
	static final I18nProvider EN_US = new I18nProvider("en_us",
			"Silk API: Codex",
			"Sub-API for creating mod configurations.",
			"This system includes related utilities in Silk API for creating, " +
					"reading, writing mod configurations and switching configuration modes.", new String[]{
			"Support for Silk API"});
	static final I18nProvider ZH_CN = new I18nProvider("zh_cn",
			"丝绸开发库：法典",
			"用于创建模组配置的子开发库。",
			"此系统包含了丝绸开发库中用于创建、读取、写入模组配置与切换配置模式的相关工具。", new String[]{
			"支持丝绸开发库"});
	static final I18nProvider ZH_HK = new I18nProvider("zh_hk",
			"絲綢開發庫：法典",
			"用於創建模組配置嘅子開發庫。",
			"此系統包含了絲綢開發庫中用於創建、讀取、寫入模組配置與切換配置模式嘅相關工具。", new String[]{
			"支持絲綢開發庫"});
	static final I18nProvider ZH_TW = new I18nProvider("zh_tw",
			"絲綢開發庫：法典",
			"用於建立模組配置的子開發庫。",
			"此系統包含了絲綢開發庫中用於建立、讀取、寫入模組配置與切換配置模式的相關工具。", new String[]{
			"支援絲綢開發庫"});
	
	private final String[] allText;
	
	private I18nProvider(String langCode, String name, String summary, String description, String[] allText) {
		super(langCode, SilkCodex.getInstance().id(), name, summary, description);
		this.allText = allText;
	}
	
	@Override
	public FabricLanguageProvider provider(FabricDataOutput fabricDataOutput) {
		return new FabricLanguageProvider(fabricDataOutput, langCode) {
			@Override
			public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
				generate(translationBuilder);
			}
		};
	}
	
	@Override
	protected void generate(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		super.generate(translationBuilder);
		Iterator<String> texts = Arrays.stream(allText).iterator();
		translationBuilder.add(WidgetTexts.textKey(SilkCodex.getInstance(), "support"), texts.next());
	}
}
