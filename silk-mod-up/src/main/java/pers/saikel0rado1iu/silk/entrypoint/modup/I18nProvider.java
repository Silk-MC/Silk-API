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

package pers.saikel0rado1iu.silk.entrypoint.modup;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import pers.saikel0rado1iu.silk.codex.OptionTexts;
import pers.saikel0rado1iu.silk.common.api.I18nModInfoProvider;
import pers.saikel0rado1iu.silk.generate.data.LinkedLanguageProvider;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.modup.*;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;

import java.util.Arrays;
import java.util.Iterator;


/**
 * <h2 style="color:FFC800">国际化提供器</h2>
 * 国际化 Silk API: Mod Up 提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
final class I18nProvider extends I18nModInfoProvider {
	static final I18nProvider EN_US = new I18nProvider("en_us",
			"Silk API：Mod ↑",
			"Sub-API used to implement mod update function.",
			"This system includes tools in the Silk API for detecting mod updates, displaying update prompts, and implementing automatic mod updates. " +
					"Mods use this system to conveniently implement update functions.", new String[]{
			"Text translation of the update system",
			"Update Settings",
			"Settings options for adjusting mod updates",
			"Update Notify",
			"If mod can be updated, an update prompt screen that occupies the entire game will pop up directly.",
			"A screen that occupies the entire game will not pop up, but updates will still be prompted in the game.",
			"Show Changelog",
			"If you start a new version of mod for the first time, an changelog screen that occupies the entire game will pop up to make it easy to see the new features.",
			"The changelog screen no longer pops up, and the changelog cannot be viewed unless otherwise provided by the mod author.",
			"Check New Minecraft Version Mod",
			"If mod adapts to a new version of Minecraft, it will also prompt you to update.",
			"Do not prompt for mod updates on the new version of Minecraft.",
			"Stop Updating Warning",
			"If mod does not continue to update on this Minecraft version, players will be warned every time the game is started.",
			"Even if mod does not continue to update on this Minecraft version, it will not prompt the player.",
			"Update System Fail Warning",
			"If the update system cannot get the update information when it is online, a warning will be displayed.",
			"Players are not warned even if the update system fails.",
			"Update Mode",
			"Update Mode: %1$s",
			"Manual Download",
			"Updates will be redirected to mod's modrinth website. You need to download the new version of mod yourself.",
			"Auto Download", """
						§c§l§o!Be careful! Mod dependencies are not automatically downloaded! §r
						Updates will automatically download the latest mod version files to the mods folder. You need to delete the old version mod files manually.""",
			"Auto Update", """
						§6§l§oCan only be used in Windows systems! §r
						§c§l§o!Be careful! Mod dependencies are not automatically updated! §r
						The system will automatically complete all the work needed for the update, you just need to open the game after the mod update is completed!""",
			"Update Channel",
			"Release", """
						§6Official/Release version of mod. §r
						Get updates in the release channel in modrinth.""",
			"Snapshot", """
						§6Preview/Test/Beta version of mod to experience the new content that is not perfect in advance.§r
						Get updates in the beta channel in modrinth.""",
			"Development", """
						§6Developing/Alpha version of mod, the version under development, is very unstable.§r
						Get updates in the alpha channel in modrinth.""",
			"update",
			"Click to visit the mod homepage.",
			"Go to download the mod.",
			"The mod %1$s version %2$s now supports Minecraft %3$s. Please %4$s as soon as possible for the best experience!",
			"The mod %1$s has been updated to version %2$s. Please %3$s as soon as possible for the best experience!",
			"Mod %1$s Changelog：\n%2$s",
			"The mod %1$s has stopped updating for the current Minecraft version. Please check the mod's official website for any new version updates.",
			"The mod %1$s update failed because the update system is no longer functioning and cannot process update requests. Please check if the new Silk-API has resolved this issue.",
			"Check For Updates",
			"Update System Fail Warning",
			"  The update system is invalid and the update request cannot be obtained. Please update the silk-api version in time.",
			"%1$s Stop Update Warning",
			"  %1$s is no longer updated in the current Minecraft version, please update the Minecraft version in time for the best experience.",
			"Changelog: %1$s",
			"  Has been updated to the latest version, please check the changelog.",
			"%1$s Has Update",
			"  %1$s has been updated to version %2$s. Please update as soon as possible for the best experience!",
			"%1$s Now Supports %2$s",
			"  %1$s has been updated to version %2$s. Please update as soon as possible for the best experience!",
			"Update Now",
			"Not Yet",
			"  In order to protect the interests of mod authors, they will jump to the mod website before automatic download to ensure that the authors get the advertising revenue they deserve.",
			"Confirm Update",
			"Downloading %1$s: %2$s%%",
			"  %1$s has been installed in the mods folder. Please delete the old version quickly.",
			"Delete Files",
			"  %1$s has been installed in the mods folder. Please close the game to clean the files.",
			"Clean Update"
	});
	static final I18nProvider ZH_CN = new I18nProvider("zh_cn",
			"丝绸开发库：模组↑",
			"用于实现模组更新功能的子开发库。",
			"此系统包含了丝绸开发库中用于检测模组更新、显示更新提示与实现模组自动更新的工具。模组用此系统来便捷的实现更新功能。", new String[]{
			"更新系统的文本翻译",
			"更新设置",
			"用于调整模组更新的设置选项",
			"更新通知",
			"如果模组有更新则会直接弹出一个占据整个游戏的更新界面。",
			"不会弹出一个占据整个游戏的界面，但是依然会在游戏中通知更新。",
			"显示更新日志",
			"如果第一次启动新版本模组则会弹出一个占据整个游戏的更新日志界面以方便查看新特性。",
			"不会再弹出更新日志界面。除非模组作者另有说明，否则无法查看更新日志。",
			"检查新版本 MC 模组",
			"如果模组适配了新版本的 Minecraft 也会提示更新。",
			"不提示在新版本 Minecraft 上的模组更新。",
			"停止更新警告",
			"如果模组在此 Minecraft 版本上不再继续更新则每次启动游戏则会警告玩家。",
			"即使模组在此 Minecraft 版本上不再继续更新也不提示玩家。",
			"更新系统失效警告",
			"如果更新系统在联网状态下无法获取更新信息则会显示警告。",
			"即使更新系统失效也不警告玩家。",
			"更新模式",
			"更新模式: %1$s",
			"手动下载",
			"更新会跳转到模组的 Modrinth 页面，需要自行下载更新模组",
			"自动下载", """
						§c§l§o！注意！不会自动下载前置模组！§r
						更新会自动下载最新的模组版本文件到模组文件夹，需要手动删除旧版本模组文件。""",
			"自动更新", """
						§6§l§o只能在 Windows 系统中使用！§r
						§c§l§o！注意！不会自动更新前置模组！§r
						模组会自动完成更新所需的一切工作，你只要在模组更新完成后打开游戏即可！""",
			"更新通道",
			"发行版", """
						§6模组的正式/发行版本。§r
						在 Modrinth 中的 Release 通道中获取更新。""",
			"快照版", """
						§6模组的预览/测试/公测版本，预先体验还未完善的更新内容。§r
						在 Modrinth 中的 Beta 通道中获取更新。""",
			"开发版", """
						§6模组的开发/内测版本，开发中的版本，非常不稳定。§r
						在 Modrinth 中的 Alpha 通道中获取更新。""",
			"更新",
			"点击前往模组主页",
			"前往下载模组",
			"模组 %1$s 版本 %2$s 已支持 Minecraft %3$s, 请尽快%4$s以获取最佳体验！",
			"模组 %1$s 已更新至版本 %2$s, 请尽快%3$s以获取最佳体验！",
			"模组 %1$s 更新日志：\n%2$s",
			"模组 %1$s 在当前 Minecraft 版本已停止更新, 请在模组官网检查是否有新版本更新。",
			"模组 %1$s 更新失败, 因为更新系统已失效, 无法获取更新请求。请检查新版丝绸开发库是否解决了此问题。",
			"检查更新",
			"更新系统失效警告",
			"　　更新系统已失效，无法获取更新请求，请及时更新丝绸开发库版本。",
			"%1$s·停止更新警告",
			"　　%1$s·在当前·Minecraft·版本已停止更新，请及时更新·Minecraft·版本获取最佳体验。",
			"更新日志：%1$s",
			"已更新至最新版本，请查看更新日志了解最新改动与特性。",
			"%1$s·现已更新",
			"　　%1$s·已更新至·%2$s·版本，请尽快更新以获取最佳体验！",
			"%1$s·现已支持·%2$s",
			"　　%1$s·已更新至·%2$s·版本，请尽快更新以获取最佳体验！",
			"立即更新",
			"暂时不用",
			"　　为了维护模组作者的利益，在自动下载前会跳转到模组官网以确保作者获得应有的广告收益。",
			"确认更新",
			"%1$s·下载中: %2$s%%",
			"　　%1$s·已安装至·mods·文件夹，请尽快删除旧版本文件。",
			"删除文件",
			"　　%1$s·已安装至·mods·文件夹，请关闭游戏进行文件清理。",
			"清理更新"
	});
	static final I18nProvider ZH_HK = new I18nProvider("zh_hk",
			"絲綢開發庫：模組↑",
			"用於實現模組更新功能嘅子開發庫。",
			"此系統包含了絲綢開發庫中用於檢測模組更新、顯示更新提示與實現模組自動更新嘅工具。模組用此系統來便捷嘅實現更新功能。", new String[]{
			"更新系統嘅文本翻譯",
			"更新設置",
			"用於調整模組更新嘅設置選項",
			"更新通知",
			"如果模組有更新則會直接彈出一個佔據整個遊戲嘅更新界面。",
			"不會彈出一個佔據整個遊戲嘅界面，但是依然會在遊戲中通知更新。",
			"顯示更新日誌",
			"如果第一次啓動新版本模組則會彈出一個佔據整個遊戲嘅更新日誌界面以方便查看新特性。",
			"不會再彈出更新日誌界面。除非模組作者另有説明，否則無法查看更新日誌。",
			"檢查新版本 MC 模組",
			"如果模組適配了新版本嘅 Minecraft 也會提示更新。",
			"不提示在新版本 Minecraft 上嘅模組更新。",
			"停止更新警告",
			"如果模組在此 Minecraft 版本上不再繼續更新則每次啓動遊戲則會警告玩家。",
			"即使模組在此 Minecraft 版本上不再繼續更新也不提示玩家。",
			"更新系統失效警告",
			"如果更新系統在聯網狀態下無法獲取更新信息則會顯示警告。",
			"即使更新系統失效也不警告玩家。",
			"更新模式",
			"更新模式: %1$s",
			"手動下載",
			"更新會跳轉到模組嘅 Modrinth 頁面，需要自行下載更新模組",
			"自動下載", """
						§c§l§o！注意！不會自動下載前置模組！§r
						更新會自動下載最新嘅模組版本文件到模組文件夾，需要手動刪除舊版本模組文件。""",
			"自動更新", """
						§6§l§o只能在 Windows 系統中使用！§r
						§c§l§o！注意！不會自動更新前置模組！§r
						模組會自動完成更新所需嘅一切工作，你只要在模組更新完成後打開遊戲即可！""",
			"更新通道",
			"發行版", """
						§6模組嘅正式/發行版本。§r
						在 Modrinth 中嘅 Release 通道中獲取更新。""",
			"快照版", """
						§6模組嘅預覽/測試/公測版本，預先體驗還未完善嘅更新內容。§r
						在 Modrinth 中嘅 Beta 通道中獲取更新。""",
			"開發版", """
						§6模組嘅開發/內測版本，開發中嘅版本，非常不穩定。§r
						在 Modrinth 中嘅 Alpha 通道中獲取更新。""",
			"更新",
			"點擊前往模組主頁",
			"前往下載模組",
			"模組 %1$s 版本 %2$s 已支持 Minecraft %3$s, 請儘快%4$s以獲取最佳體驗！",
			"模組 %1$s 已更新至版本 %2$s, 請儘快%3$s以獲取最佳體驗！",
			"模組 %1$s 更新日誌：\n%2$s",
			"模組 %1$s 在當前 Minecraft 版本已停止更新, 請在模組官網檢查是否有新版本更新。",
			"模組 %1$s 更新失敗, 因為更新系統已失效, 無法獲取更新請求。請檢查新版絲綢開發庫是否解決了此問題。",
			"檢查更新",
			"更新系統失效警告",
			"　　更新系統已失效，無法獲取更新請求，請及時更新絲綢開發庫版本。",
			"%1$s·停止更新警告",
			"　　%1$s·在當前·Minecraft·版本已停止更新，請及時更新·Minecraft·版本獲取最佳體驗。",
			"更新日誌：%1$s",
			"已更新至最新版本，請查看更新日誌瞭解最新改動與特性。",
			"%1$s·現已更新",
			"　　%1$s·已更新至·%2$s·版本，請儘快更新以獲取最佳體驗！",
			"%1$s·現已支持·%2$s",
			"　　%1$s·已更新至·%2$s·版本，請儘快更新以獲取最佳體驗！",
			"立即更新",
			"暫時不用",
			"　　為了維護模組作者嘅利益，在自動下載前會跳轉到模組官網以確保作者獲得應有嘅廣告收益。",
			"確認更新",
			"%1$s·下載中: %2$s%%",
			"　　%1$s·已安装至·mods·文件夹，请尽快删除旧版本文件。",
			"刪除文件",
			"　　%1$s·已安裝至·mods·文件夾，請關閉遊戲進行文件清理。",
			"清理更新"
	});
	static final I18nProvider ZH_TW = new I18nProvider("zh_tw",
			"絲綢開發庫：模組↑",
			"用於實現模組更新功能的子開發庫。",
			"此系統包含了絲綢開發庫中用於檢測模組更新、顯示更新提示與實現模組自動更新的工具。模組用此系統來便捷的實現更新功能。", new String[]{
			"更新系統的文字翻譯",
			"更新設定",
			"用於調整模組更新的設定選項",
			"更新通知",
			"如果模組有更新則會直接彈出一個佔據整個遊戲的更新介面。",
			"不會彈出一個佔據整個遊戲的介面，但是依然會在遊戲中通知更新。",
			"顯示更新日誌",
			"如果第一次啟動新版本模組則會彈出一個佔據整個遊戲的更新日誌介面以方便檢視新特性。",
			"不會再彈出更新日誌介面。除非模組作者另有說明，否則無法檢視更新日誌。",
			"檢查新版本 MC 模組",
			"如果模組適配了新版本的 Minecraft 也會提示更新。",
			"不提示在新版本 Minecraft 上的模組更新。",
			"停止更新警告",
			"如果模組在此 Minecraft 版本上不再繼續更新則每次啟動遊戲則會警告玩家。",
			"即使模組在此 Minecraft 版本上不再繼續更新也不提示玩家。",
			"更新系統失效警告",
			"如果更新系統在聯網狀態下無法獲取更新資訊則會顯示警告。",
			"即使更新系統失效也不警告玩家。",
			"更新模式",
			"更新模式: %1$s",
			"手動下載",
			"更新會跳轉到模組的 Modrinth 頁面，需要自行下載更新模組",
			"自動下載", """
						§c§l§o！注意！不會自動下載前置模組！§r
						更新會自動下載最新的模組版本檔案到模組資料夾，需要手動刪除舊版本模組檔案。""",
			"自動更新", """
						§6§l§o只能在 Windows 系統中使用！§r
						§c§l§o！注意！不會自動更新前置模組！§r
						模組會自動完成更新所需的一切工作，你只要在模組更新完成後開啟遊戲即可！""",
			"更新通道",
			"釋出版", """
						§6模組的正式/釋出版本。§r
						在 Modrinth 中的 Release 通道中獲取更新。""",
			"快照版", """
						§6模組的預覽/測試/公測版本，預先體驗還未完善的更新內容。§r
						在 Modrinth 中的 Beta 通道中獲取更新。""",
			"開發版", """
						§6模組的開發/內測版本，開發中的版本，非常不穩定。§r
						在 Modrinth 中的 Alpha 通道中獲取更新。""",
			"更新",
			"點選前往模組主頁",
			"前往下載模組",
			"模組 %1$s 版本 %2$s 已支援 Minecraft %3$s, 請儘快%4$s以獲取最佳體驗！",
			"模組 %1$s 已更新至版本 %2$s, 請儘快%3$s以獲取最佳體驗！",
			"模組 %1$s 更新日誌：\n%2$s",
			"模組 %1$s 在當前 Minecraft 版本已停止更新, 請在模組官網檢查是否有新版本更新。",
			"模組 %1$s 更新失敗, 因為更新系統已失效, 無法獲取更新請求。請檢查新版絲綢開發庫是否解決了此問題。",
			"檢查更新",
			"更新系統失效警告",
			"　　更新系統已失效，無法獲取更新請求，請及時更新絲綢開發庫版本。",
			"%1$s·停止更新警告",
			"　　%1$s·在當前·Minecraft·版本已停止更新，請及時更新·Minecraft·版本獲取最佳體驗。",
			"更新日誌：%1$s",
			"已更新至最新版本，請檢視更新日誌瞭解最新改動與特性。",
			"%1$s·現已更新",
			"　　%1$s·已更新至·%2$s·版本，請儘快更新以獲取最佳體驗！",
			"%1$s·現已支援·%2$s",
			"　　%1$s·已更新至·%2$s·版本，請儘快更新以獲取最佳體驗！",
			"立即更新",
			"暫時不用",
			"　　為了維護模組作者的利益，在自動下載前會跳轉到模組官網以確保作者獲得應有的廣告收益。",
			"確認更新",
			"%1$s·下載中: %2$s%%",
			"　　%1$s·已安装至·mods·檔案夹，请尽快删除旧版本檔案。",
			"刪除檔案",
			"　　%1$s·已安裝至·mods·資料夾，請關閉遊戲進行檔案清理。",
			"清理更新"
	});
	private final String[] allText;
	
	private I18nProvider(String langCode, String name, String summary, String description, String[] allText) {
		super(langCode, SilkModUp.getInstance().id(), name, summary, description);
		this.allText = allText;
	}
	
	@Override
	public LinkedLanguageProvider provider(FabricDataOutput fabricDataOutput) {
		return new LinkedLanguageProvider(fabricDataOutput, langCode) {
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
		translationBuilder.add(LinkedLanguageProvider.comment("update"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_SETTINGS), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_SETTINGS), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_NOTIFY), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_NOTIFY, "on"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_NOTIFY, "off"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.SHOW_CHANGELOG), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.SHOW_CHANGELOG, "on"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.SHOW_CHANGELOG, "off"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.CHECK_NEW_MC_VER_MOD), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.CHECK_NEW_MC_VER_MOD, "on"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.CHECK_NEW_MC_VER_MOD, "off"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.STOP_UPDATING_WARNING), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.STOP_UPDATING_WARNING, "on"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.STOP_UPDATING_WARNING, "off"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_SYS_FAIL_WARNING), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_SYS_FAIL_WARNING, "on"), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_SYS_FAIL_WARNING, "off"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_MODE), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_MODE, "_"), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_MODE, UpdateMode.MANUAL_DOWNLOAD.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_MODE, UpdateMode.MANUAL_DOWNLOAD.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_MODE, UpdateMode.AUTO_DOWNLOAD.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_MODE, UpdateMode.AUTO_DOWNLOAD.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_MODE, UpdateMode.AUTO_UPDATE.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_MODE, UpdateMode.AUTO_UPDATE.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_CHANNEL), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_CHANNEL, UpdateChannel.RELEASE.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_CHANNEL, UpdateChannel.RELEASE.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_CHANNEL, UpdateChannel.BETA.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_CHANNEL, UpdateChannel.BETA.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.textKey(UpdateSettings.UPDATE_CHANNEL, UpdateChannel.ALPHA.toString().toLowerCase()), texts.next());
		translationBuilder.add(OptionTexts.tipKey(UpdateSettings.UPDATE_CHANNEL, UpdateChannel.ALPHA.toString().toLowerCase()), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "serverNotify.update"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "serverNotify.modName.hover"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "serverNotify.update.hover"), texts.next());
		translationBuilder.add(ServerUpdateManager.updateTextKey(UpdateState.NEW_MC_VER), texts.next());
		translationBuilder.add(ServerUpdateManager.updateTextKey(UpdateState.THIS_MC_VER), texts.next());
		translationBuilder.add(ServerUpdateManager.updateTextKey(UpdateState.MOD_LOG), texts.next());
		translationBuilder.add(ServerUpdateManager.updateTextKey(UpdateState.STOP_UPDATE), texts.next());
		translationBuilder.add(ServerUpdateManager.updateTextKey(UpdateState.UPDATE_FAIL), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "check_update"), texts.next());
		translationBuilder.add(UpdateState.UPDATE_FAIL.title(), texts.next());
		translationBuilder.add(UpdateState.UPDATE_FAIL.text(), texts.next());
		translationBuilder.add(UpdateState.STOP_UPDATE.title(), texts.next());
		translationBuilder.add(UpdateState.STOP_UPDATE.text(), texts.next());
		translationBuilder.add(UpdateState.MOD_LOG.title(), texts.next());
		translationBuilder.add(UpdateState.MOD_LOG.text(), texts.next());
		translationBuilder.add(UpdateState.THIS_MC_VER.title(), texts.next());
		translationBuilder.add(UpdateState.THIS_MC_VER.text(), texts.next());
		translationBuilder.add(UpdateState.NEW_MC_VER.title(), texts.next());
		translationBuilder.add(UpdateState.NEW_MC_VER.text(), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "update_now"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "not_update"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "confirm_update"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "update"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "downloading"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkModUp.getInstance(), "downloaded"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "downloaded"), texts.next());
		translationBuilder.add(WidgetTexts.titleKey(SilkModUp.getInstance(), "updated"), texts.next());
		translationBuilder.add(WidgetTexts.textKey(SilkModUp.getInstance(), "updated"), texts.next());
	}
}
