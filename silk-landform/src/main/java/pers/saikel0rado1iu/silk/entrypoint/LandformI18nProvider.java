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

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import pers.saikel0rado1iu.silk.api.base.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.api.codex.OptionTexts;
import pers.saikel0rado1iu.silk.api.generate.data.LinkedLanguageProvider;
import pers.saikel0rado1iu.silk.api.landform.WorldUpgradeSettings;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.impl.SilkLandform;

import java.util.Arrays;
import java.util.Iterator;

/**
 * <h2 style="color:FFC800">国际化 Silk API: Landform 提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class LandformI18nProvider extends I18nModInfoProvider {
	static final LandformI18nProvider EN_US = new LandformI18nProvider("en_us",
			"Silk API：Landform",
			"Sub-API for impl world generation functionality.",
			"This system includes Silk API used to implement vanilla noise chunk generator modifications, " +
					"terrain, biome modifications, and world upgrader and other functions related to world generation.", new String[]{
			"Text translation of the world upgrade system",
			"World Upgrade Settings",
			"Chunk Block Scan Granularity: Interval %1$s", """
			The granularity of block scanning to detect if a chunk needs to be upgraded.
			Larger granularity results in shorter scan time but lower accuracy.
			Smaller granularity results in longer scan time but higher accuracy.""",
			"Chunk Block Scan Granularity: Default", """
			The default granularity, balancing scan accuracy and speed.
			
			The granularity of block scanning to detect if a chunk needs to be upgraded.""",
			"Chunk Block Scan Granularity: Scan All Blocks", """
			Scanning all blocks results in extremely high accuracy but significantly affects scan speed. Please choose carefully.
			
			The granularity of block scanning to detect if a chunk needs to be upgraded.""",
			"Chunk Block Scan Granularity: Scan Once Only", """
			A chunk is scanned only once, resulting in very fast scan speed but low accuracy. Please choose carefully.
			
			The granularity of block scanning to detect if a chunk needs to be upgraded.""",
			"Occupied Time Deletion Threshold: %1$s minutes", """
			The total time all players spend in a chunk. If it's less than the threshold and deletion conditions are met, the chunk will be deleted.
			This is a combined time for all players, not individual. Please note!""",
			"Occupied Time Deletion Threshold: Default", """
			The default threshold is 60 minutes.
			
			The total time all players spend in a chunk. If it's less than the threshold and deletion conditions are met, the chunk will be deleted.
			This is a combined time for all players, not individual. Please note!""",
			"Occupied Time Deletion Threshold: Never Delete", """
			No chunks will be deleted. It is recommended to use this only when you do not want to upgrade the world while deleting chunks.
			
			The total time all players spend in a chunk. If it's less than the threshold and deletion conditions are met, the chunk will be deleted.
			This is a combined time for all players, not individual. Please note!""",
			"Occupied Time Deletion Threshold: Delete All", """
			All eligible chunks will be deleted. It is recommended to use this only when you want to completely refresh the entire world.
			
			The total time all players spend in a chunk. If it's less than the threshold and deletion conditions are met, the chunk will be deleted.
			This is a combined time for all players, not individual. Please note!""",
			"This world was created in an earlier version of '%1$s'. It is recommended to upgrade the world to better play '%1$s'.",
			"This world was created in an newer version of '%1$s'. Forcibly playing this world may have unexpected consequences. " +
					"You must downgrade the world or update the latest version of '%1$s' to play this world!",
			"Refresh World",
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
			"Refreshing chunks...",
			"Support for Silk API"
	});
	static final LandformI18nProvider ZH_CN = new LandformI18nProvider("zh_cn",
			"丝绸开发库：地貌",
			"用于实现世界生成功能的子开发库。",
			"此系统包含了丝绸开发库中用于实现原版噪声区块生成器修改，地形、生物群系修改以及世界升级器等与世界生成有关的功能。", new String[]{
			"世界升级系统的文本翻译",
			"世界升级设置",
			"区块方块扫描粒度：间隔 %1$s", """
			检测区块是否需要升级的方块扫描粒度
			粒度越大耗时越短、精准度越低
			粒度越小耗时越长、精准度越高""",
			"区块方块扫描粒度：默认", """
			默认的粒度，兼顾扫描的精准度与速度
			
			检测区块是否需要升级的方块扫描粒度""",
			"区块方块扫描粒度：扫描所有方块", """
			扫描所有方块，精准度极高，但是会极大的影响扫描速度，请慎重选择
			
			检测区块是否需要升级的方块扫描粒度""",
			"区块方块扫描粒度：只扫描一次", """
			一个区块只扫描一次，扫描速度极快，但精准度很低，请慎重选择
			
			检测区块是否需要升级的方块扫描粒度""",
			"占据时间删除阈值：%1$s 分钟", """
			玩家在区块中占据的时间，如果玩家在这个区块中占据的时间小于此阈值并满足删除条件则会删除此区块
			此时间为所有玩家在区块中所待时间的总和，非每位玩家的单独设置，请注意！""",
			"占据时间删除阈值：默认", """
			默认阈值为 60 分钟
			
			玩家在区块中占据的时间，如果玩家在这个区块中占据的时间小于此阈值并满足删除条件则会删除此区块
			此时间为所有玩家在区块中所待时间的总和，非每位玩家的单独设置，请注意！""",
			"占据时间删除阈值：从不删除", """
			不会删除任何一个区块，建议仅在不想要在删除区块的同时升级世界时使用
			
			玩家在区块中占据的时间，如果玩家在这个区块中占据的时间小于此阈值并满足删除条件则会删除此区块
			此时间为所有玩家在区块中所待时间的总和，非每位玩家的单独设置，请注意！""",
			"占据时间删除阈值：全部删除", """
			满足条件的区块全部删除，建议仅在需要让整个世界焕然一新时使用
			
			玩家在区块中占据的时间，如果玩家在这个区块中占据的时间小于此阈值并满足删除条件则会删除此区块
			此时间为所有玩家在区块中所待时间的总和，非每位玩家的单独设置，请注意！""",
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
			"刷新区块中…",
			"支持丝绸开发库"
	});
	static final LandformI18nProvider ZH_HK = new LandformI18nProvider("zh_hk",
			"絲綢開發庫：地貌",
			"用於實現世界生成功能嘅子開發庫。",
			"此系統包含了絲綢開發庫中用於實現原版噪聲區塊生成器修改，地形、生物羣落修改以及世界升級器等與世界生成有關嘅功能。", new String[]{
			"世界升級系統嘅文本翻譯",
			"世界升級設置",
			"區塊方塊掃描粒度：間隔 %1$s", """
			檢測區塊是否需要升級嘅方塊掃描粒度
			粒度越大耗時越短、精準度越低
			粒度越小耗時越長、精準度越高""",
			"區塊方塊掃描粒度：預設", """
			預設嘅粒度，兼顧掃描嘅精準度與速度
			
			檢測區塊是否需要升級嘅方塊掃描粒度""",
			"區塊方塊掃描粒度：掃描所有方塊", """
			掃描所有方塊，精準度極高，但是會極大嘅影響掃描速度，請慎重選擇
			
			檢測區塊是否需要升級嘅方塊掃描粒度""",
			"區塊方塊掃描粒度：只掃描一次", """
			一個區塊只掃描一次，掃描速度極快，但精準度很低，請慎重選擇
			
			檢測區塊是否需要升級嘅方塊掃描粒度""",
			"佔據時間刪除閾值：%1$s 分鐘", """
			玩家在區塊中佔據嘅時間，如果玩家在這個區塊中佔據嘅時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間嘅總和，非每位玩家嘅單獨設置，請注意！""",
			"佔據時間刪除閾值：預設", """
			預設閾值為 60 分鐘
			
			玩家在區塊中佔據嘅時間，如果玩家在這個區塊中佔據嘅時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間嘅總和，非每位玩家嘅單獨設置，請注意！""",
			"佔據時間刪除閾值：從不刪除", """
			不會刪除任何一個區塊，建議僅在不想要在刪除區塊嘅同時升級世界時使用
			
			玩家在區塊中佔據嘅時間，如果玩家在這個區塊中佔據嘅時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間嘅總和，非每位玩家嘅單獨設置，請注意！""",
			"佔據時間刪除閾值：全部刪除", """
			滿足條件嘅區塊全部刪除，建議僅在需要讓整個世界煥然一新時使用
			
			玩家在區塊中佔據嘅時間，如果玩家在這個區塊中佔據嘅時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間嘅總和，非每位玩家嘅單獨設置，請注意！""",
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
			"正在刷新區塊⋯⋯",
			"支持絲綢開發庫"
	});
	static final LandformI18nProvider ZH_TW = new LandformI18nProvider("zh_tw",
			"絲綢開發庫：地貌",
			"用於實現世界生成功能的子開發庫。",
			"此系統包含了絲綢開發庫中用於實現原版噪聲區塊生成器修改，地形、生態域修改以及世界升級器等與世界生成有關的功能。", new String[]{
			"世界升級系統的文字翻譯",
			"世界升級設定",
			"區塊方塊掃描粒度：間隔 %1$s", """
			檢測區塊是否需要升級的方塊掃描粒度
			粒度越大耗時越短、精準度越低
			粒度越小耗時越長、精準度越高""",
			"區塊方塊掃描粒度：預設", """
			預設的粒度，兼顧掃描的精準度與速度
			
			檢測區塊是否需要升級的方塊掃描粒度""",
			"區塊方塊掃描粒度：掃描所有方塊", """
			掃描所有方塊，精準度極高，但是會極大的影響掃描速度，請慎重選擇
			
			檢測區塊是否需要升級的方塊掃描粒度""",
			"區塊方塊掃描粒度：只掃描一次", """
			一個區塊只掃描一次，掃描速度極快，但精準度很低，請慎重選擇
			
			檢測區塊是否需要升級的方塊掃描粒度""",
			"佔據時間刪除閾值：%1$s 分鐘", """
			玩家在區塊中佔據的時間，如果玩家在這個區塊中佔據的時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間的總和，非每位玩家的單獨設定，請注意！""",
			"佔據時間刪除閾值：預設", """
			預設閾值為 60 分鐘
			
			玩家在區塊中佔據的時間，如果玩家在這個區塊中佔據的時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間的總和，非每位玩家的單獨設定，請注意！""",
			"佔據時間刪除閾值：從不刪除", """
			不會刪除任何一個區塊，建議僅在不想要在刪除區塊的同時升級世界時使用
			
			玩家在區塊中佔據的時間，如果玩家在這個區塊中佔據的時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間的總和，非每位玩家的單獨設定，請注意！""",
			"佔據時間刪除閾值：全部刪除", """
			滿足條件的區塊全部刪除，建議僅在需要讓整個世界煥然一新時使用
			
			玩家在區塊中佔據的時間，如果玩家在這個區塊中佔據的時間小於此閾值並滿足刪除條件則會刪除此區塊
			此時間為所有玩家在區塊中所待時間的總和，非每位玩家的單獨設定，請注意！""",
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
			"正在重新整理區塊...",
			"支援絲綢開發庫"
	});
	private final String[] allText;
	
	private LandformI18nProvider(String langCode, String name, String summary, String description, String[] allText) {
		super(langCode, SilkLandform.getInstance().id(), name, summary, description);
		this.allText = allText;
	}
	
	@Override
	public LinkedLanguageProvider provider(FabricDataOutput fabricDataOutput) {
		return new LinkedLanguageProvider(fabricDataOutput, langCode) {
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
		translationBuilder.add(LinkedLanguageProvider.comment("world_upgrade"), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.WORLD_UPGRADE_SETTINGS), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY, "default"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY, "default"), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY, "min"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY, "min"), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY, "max"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_BLOCK_SCAN_GRANULARITY, "max"), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME, "default"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME, "default"), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME, "min"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME, "min"), texts.next());
		translationBuilder.add(OptionTexts.textKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME, "max"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(WorldUpgradeSettings.CHUNK_DELETION_THRESHOLD_TIME, "max"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "upgrade.tooltip"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "downgrade.tooltip"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkLandform.getInstance(), "refresh"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkLandform.getInstance(), "upgrade"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkLandform.getInstance(), "downgrade"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "prompt"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkLandform.getInstance(), "warning"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "warning"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkLandform.getInstance(), "screen"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "deleted"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "handled"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "upgrading"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "downgrading"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "refreshing"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkLandform.getInstance(), "support"), texts.next());
	}
}
