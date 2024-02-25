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

package pers.saikel0rado1iu.silk.landform;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.base.common.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.gen.data.SilkLanguageProvider;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

import java.util.Arrays;
import java.util.Iterator;

import static pers.saikel0rado1iu.silk.gen.data.SilkLanguageProvider.getCommentKey;
import static pers.saikel0rado1iu.silk.util.TextUtil.widgetText;
import static pers.saikel0rado1iu.silk.util.TextUtil.widgetTitle;

/**
 * <h2 style="color:FFC800">国际化 Silk API: Landform 提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class InternationalizationProvider extends I18nModInfoProvider {
	static final InternationalizationProvider EN_US = new InternationalizationProvider("en_us",
			"Silk API：Landform",
			"Sub-API for impl world generation functionality.",
			"This system includes Silk API used to implement vanilla noise chunk generator modifications, " +
					"terrain, biome modifications, and world upgrader and other functions related to world generation.", new String[]{
			"Text translation of the world upgrade system",
			"This world was created in an earlier version of '%1$s'. It is recommended to upgrade the world to better play '%1$s'.",
			"This world was created in an newer version of '%1$s'. Forcibly playing this world may have unexpected consequences. " +
					"You must downgrade the world or update the latest version of '%1$s' to play this world!",
			"Flush World",
			"Upgrade World",
			"Downgrade World", """
						The world upgrader will detect the contents of each chunk to determine whether this chunk should be modified.
						All chunks that need to be modified will be §c§lDELETE§r for regeneration.
						Depending on the state of the world, you may §c§lLOSE§r data saved in certain chunks.
						If you are not sure whether this data is important, we recommend that you §6§lBACKUP§r your world before %1$s.""",
			"Need Upgrade World!", """
						This world is not the latest version and requires a world upgrade.
						If the world is not upgraded, the world will be forced to upgrade to the latest version, which may have unpredictable consequences.
						We recommend that you go to '%1$s' to upgrade the world so that you can play this world better.
						If you understand the consequences and want to continue playing, we recommend that you backup your world first just in case.""",
			"%1$s '%2$s'",
			"Deleted chunks: %1$s",
			"Handled chunks：%1$s",
			"Upgrading chunks...",
			"Downgrading chunks...",
			"Flushing chunks..."
	});
	static final InternationalizationProvider ZH_CN = new InternationalizationProvider("zh_cn",
			"丝绸开发库：地貌",
			"用于实现世界生成功能的子开发库。",
			"此系统包含了丝绸开发库中用于实现原版噪声区块生成器修改，地形、生物群系修改以及世界升级器等与世界生成有关的功能。", new String[]{
			"世界升级系统的文本翻译",
			"此世界是在更低版本的「%1$s」中创建的，建议进行世界升级以便更好的游玩「%1$s」。",
			"此世界是在更新版本的「%1$s」中创建的，如果强行进入此世界可能会有意想不到的后果，您必须要降级世界或者更新最新版「%1$s」才能进入此世界！",
			"刷新世界",
			"升级世界",
			"降级世界", """
						世界升级器会检测每个区块中的内容来判断是否应该修改此区块。
						所有需要修改的区块都会被§c§l删除§r以便重新生成。
						取决于世界的状况，你可能会§c§l丢失§r保存在某些区块中的数据。
						如果你不确定这些数据是否重要，我们建议你先§6§l备份§r再%1$s。""",
			"需要升级世界！", """
						此世界不为最新版本，需要进行世界升级。
						如果不进行世界升级，世界会被强制升级为最新版本，这可能会产生无法预测的后果。
						我们建议你前往「%1$s」进行世界升级，以便更好的游玩此世界。
						如果你已了解后果，想继续游戏的话，我们建议你先备份世界以防万一。""",
			"正在%1$s“%2$s”",
			"已删除的区块数：%1$s",
			"已处理的区块数：%1$s",
			"升级区块中…",
			"降级区块中…",
			"刷新区块中…"
	});
	static final InternationalizationProvider ZH_HK = new InternationalizationProvider("zh_hk",
			"絲綢開發庫：地貌",
			"用於實現世界生成功能嘅子開發庫。",
			"此系統包含了絲綢開發庫中用於實現原版噪聲區塊生成器修改，地形、生物羣落修改以及世界升級器等與世界生成有關嘅功能。", new String[]{
			"世界升級系統嘅文本翻譯",
			"此世界是在更低版本嘅「%1$s」中創建嘅，建議進行世界升級以便更好嘅遊玩「%1$s」。",
			"此世界是在更新版本嘅「%1$s」中創建嘅，如果強行進入此世界可能會有意想不到嘅後果，您必須要降級世界或者更新最新版「%1$s」才能進入此世界！",
			"刷新世界",
			"升級世界",
			"降級世界", """
						世界升級器會檢測每個區塊中嘅內容來判斷是否應該修改此區塊。
						所有需要修改嘅區塊都會被§c§l刪除§r以便重新生成。
						取決於世界嘅狀況，你可能會§c§l丟失§r保存在某些區塊中嘅數據。
						如果你不確定這些數據是否重要，我們建議你先§6§l備份§r再%1$s。""",
			"需要升級世界！", """
						此世界不為最新版本，需要進行世界升級。
						如果不進行世界升級，世界會被強制升級為最新版本，這可能會產生無法預測嘅後果。
						我們建議你前往「%1$s」進行世界升級，以便更好嘅遊玩此世界。
						如果你已瞭解後果，想繼續遊戲嘅話，我們建議你先備份世界以防萬一。""",
			"正在%1$s '%2$s'",
			"已刪除區塊數量：%1$s",
			"已處理區塊數量：%1$s",
			"正在升級區塊⋯⋯",
			"正在降級區塊⋯⋯",
			"正在刷新區塊⋯⋯"
	});
	static final InternationalizationProvider ZH_TW = new InternationalizationProvider("zh_tw",
			"絲綢開發庫：地貌",
			"用於實現世界生成功能的子開發庫。",
			"此系統包含了絲綢開發庫中用於實現原版噪聲區塊生成器修改，地形、生態域修改以及世界升級器等與世界生成有關的功能。", new String[]{
			"世界升級系統的文字翻譯",
			"此世界是在更低版本的「%1$s」中建立的，建議進行世界升級以便更好的遊玩「%1$s」。",
			"此世界是在更新版本的「%1$s」中建立的，如果強行進入此世界可能會有意想不到的後果，您必須要降級世界或者更新最新版「%1$s」才能進入此世界！",
			"重新整理世界",
			"升級世界",
			"降級世界", """
						世界升級器會檢測每個區塊中的內容來判斷是否應該修改此區塊。
						所有需要修改的區塊都會被§c§l刪除§r以便重新生成。
						取決於世界的狀況，你可能會§c§l丟失§r儲存在某些區塊中的資料。
						如果你不確定這些資料是否重要，我們建議你先§6§l備份§r再%1$s。""",
			"需要升級世界！", """
						此世界不為最新版本，需要進行世界升級。
						如果不進行世界升級，世界會被強制升級為最新版本，這可能會產生無法預測的後果。
						我們建議你前往「%1$s」進行世界升級，以便更好的遊玩此世界。
						如果你已瞭解後果，想繼續遊戲的話，我們建議你先備份世界以防萬一。""",
			"正在%1$s「%2$s」",
			"已刪除的區塊數：%1$s",
			"已處理的區塊數：%1$s",
			"正在升級區塊...",
			"正在降級區塊...",
			"正在重新整理區塊..."
	});
	private final String[] allText;
	
	private InternationalizationProvider(String langCode, String name, String summary, String description, String[] allText) {
		super(langCode, SilkLandform.DATA.getId(), name, summary, description);
		this.allText = allText;
	}
	
	@Override
	public SilkLanguageProvider provider(FabricDataOutput fabricDataOutput) {
		return new SilkLanguageProvider(fabricDataOutput, langCode) {
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				generate(translationBuilder);
			}
		};
	}
	
	@Override
	protected void generate(FabricLanguageProvider.TranslationBuilder translationBuilder) {
		super.generate(translationBuilder);
		Iterator<String> texts = Arrays.stream(allText).iterator();
		translationBuilder.add(getCommentKey("world_upgrade"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrade.tooltip"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrade.tooltip"), texts.next());
		translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.flush"), texts.next());
		translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.upgrade"), texts.next());
		translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.downgrade"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.prompt"), texts.next());
		translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.warning"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.warning"), texts.next());
		translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.screen"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.deleted"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.handled"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrading"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrading"), texts.next());
		translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.flushing"), texts.next());
	}
}
