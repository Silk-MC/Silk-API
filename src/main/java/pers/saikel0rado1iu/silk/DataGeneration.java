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

package pers.saikel0rado1iu.silk;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.api.entity.SilkEntityTypeTags;
import pers.saikel0rado1iu.silk.datagen.ModDataGeneration;
import pers.saikel0rado1iu.silk.datagen.SilkLanguageProvider;
import pers.saikel0rado1iu.silk.util.Minecraft;

import java.util.concurrent.CompletableFuture;

import static pers.saikel0rado1iu.silk.datagen.SilkTagGenerator.putSpawnGroupEntityInTags;
import static pers.saikel0rado1iu.silk.util.TextUtil.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用于 Silk-API 的数据生成器</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public final class DataGeneration extends ModDataGeneration {
	@Override
	public void datagen(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(TagGeneration.EntityType::new);
		pack.addProvider(LocalizationGenerator.EnUs::new);
		pack.addProvider(LocalizationGenerator.ZhCn::new);
		pack.addProvider(LocalizationGenerator.ZhHk::new);
		pack.addProvider(LocalizationGenerator.ZhTw::new);
	}
	
	
	private interface TagGeneration {
		final class EntityType extends FabricTagProvider.EntityTypeTagProvider {
			public EntityType(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
				super(output, completableFuture);
			}
			
			@Override
			protected void configure(RegistryWrapper.WrapperLookup arg) {
				putSpawnGroupEntityInTags(this::getOrCreateTagBuilder, SilkEntityTypeTags.MONSTERS, SpawnGroup.MONSTER, Minecraft.DATA);
			}
		}
	}
	
	private interface LocalizationGenerator {
		
		final class EnUs extends SilkLanguageProvider {
			EnUs(FabricDataOutput dataOutput) {
				super(dataOutput, "en_us");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				translationBuilder.add(getCommentKey("title"), "Origin Language is Simplified Chinese(zh_cn)");
				translationBuilder.add(getCommentKey("modmenu"), "Translatable Descriptions and Summaries in Mod Menu");
				translationBuilder.add(getLocalizationNameKey(Silk.DATA), "Silk API");
				translationBuilder.add(getLocalizationSummaryKey(Silk.DATA), "Fabric API extensions for making fancy functionality.");
				translationBuilder.add(getLocalizationDescriptionKey(Silk.DATA), "Advanced Fabric API extensions for adding fancy mod items, blocks, mobs and features. The goal is to make it easy for fabric mod developers to produce a variety of advanced operations.");
				translationBuilder.add("modmenu.support.afdian", "Support: Afdian");
				translationBuilder.add("modmenu.support.patreon", "Support: Patreon");
				translationBuilder.add(getCommentKey("update"), "Text translation of the update system");
				translationBuilder.add(widgetText(Silk.DATA, "support"), "Support for Silk API");
				translationBuilder.add(widgetText(Silk.DATA, "check_update"), "Check For Updates");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.update_fail"), "Update System Fail Warning");
				translationBuilder.add(widgetText(Silk.DATA, "update.update_fail"), "  The update system is invalid and the update request cannot be obtained. Please update the silk-api version in time.");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%s Stop Update Warning");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "  %s is no longer updated in the current Minecraft version, please update the Minecraft version in time for the best experience.");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "Changelog: %s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "  Has been updated to the latest version, please check the changelog.");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%s Has Update");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "  %s has been updated to %s, please update quickly!");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%s Now Supports %s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "  %s has been updated to %s, please update quickly!");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "  In order to protect the interests of mod authors, they will jump to the mod website before automatic download to ensure that the authors get the advertising revenue they deserve.");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "Downloading %s: %s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "  %s has been installed in the mods folder. Please delete the old version quickly.");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "  %s has been installed in the mods folder. Please close the game to clean the files.");
				translationBuilder.add(configText(Silk.DATA, "update_now"), "Update Now");
				translationBuilder.add(configText(Silk.DATA, "not_update"), "Not Yet");
				translationBuilder.add(configText(Silk.DATA, "confirm_update"), "Confirm Update");
				translationBuilder.add(configText(Silk.DATA, "downloaded"), "Delete Files");
				translationBuilder.add(configText(Silk.DATA, "updated"), "Clean Update");
				translationBuilder.add(configText(Silk.DATA, "update"), "Update Settings");
				translationBuilder.add(configText(Silk.DATA, "update.update_notify"), "Update Notify");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.on"), "If mod can be updated, an update prompt screen that occupies the entire game will pop up directly.");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.off"), "A screen that occupies the entire game will not pop up, but updates will still be prompted in the game.");
				translationBuilder.add(configText(Silk.DATA, "update.show_changelog"), "Show Changelog");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.on"), "If you start a new version of mod for the first time, an changelog screen that occupies the entire game will pop up to make it easy to see the new features.");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.off"), "The changelog screen no longer pops up, and the changelog cannot be viewed unless otherwise provided by the mod author.");
				translationBuilder.add(configText(Silk.DATA, "update.check_new_mc_ver_mod"), "Check New Minecraft Version Mod");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.on"), "If mod adapts to a new version of Minecraft, it will also prompt you to update.");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.off"), "Do not prompt for mod updates on the new version of Minecraft.");
				translationBuilder.add(configText(Silk.DATA, "update.stop_updating_warning"), "Stop Updating Warning");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.on"), "If mod does not continue to update on this Minecraft version, players will be warned every time the game is started.");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.off"), "Even if mod does not continue to update on this Minecraft version, it will not prompt the player.");
				translationBuilder.add(configText(Silk.DATA, "update.update_sys_fail_warning"), "Update System Fail Warning");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.on"), "If the update system cannot get the update information when it is online, a warning will be displayed.");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.off"), "Players are not warned even if the update system fails.");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode"), "Update Mode");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "Update Mode: %s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "Manual Download");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "Updates will be redirected to mod's modrinth website. You need to download the new version of mod yourself.");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "Auto Download");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), "§c§l§o!Be careful! Mod dependencies are not automatically downloaded! §r\nUpdates will automatically download the latest mod version files to the mods folder. You need to delete the old version mod files manually.");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "Auto Update");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), "§6§l§oCan only be used in Windows systems! §r\n§c§l§o!Be careful! Mod dependencies are not automatically updated! §r\nThe system will automatically complete all the work needed for the update, you just need to open the game after the mod update is completed!");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "Update Channel");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "Release");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), "§6Official/Release version of mod. §r\nGet updates in the release channel in modrinth.");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "Snapshot");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), "§6Preview/Test/Beta version of mod to experience the new content that is not perfect in advance.§r\nGet updates in the beta channel in modrinth.");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "Development");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), "§6Developing/Alpha version of mod, the version under development, is very unstable.§r\nGet updates in the alpha channel in modrinth.");
			}
		}
		
		final class ZhCn extends SilkLanguageProvider {
			ZhCn(FabricDataOutput dataOutput) {
				super(dataOutput, "zh_cn");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				translationBuilder.add(getCommentKey("title"), "原生语言");
				translationBuilder.add(getCommentKey("modmenu"), "Mod Menu 上可翻译的模组描述与摘要");
				translationBuilder.add(getLocalizationNameKey(Silk.DATA), "丝绸开发库");
				translationBuilder.add(getLocalizationSummaryKey(Silk.DATA), "用于制作具有牛逼哄哄功能的 Fabric API 扩展");
				translationBuilder.add(getLocalizationDescriptionKey(Silk.DATA), "用于添加牛逼哄哄的模组物品、方块、实体与特性的高级·Fabric·API·扩展。其目的在于方便·fabric·模组开发者进行各种高级操作");
				translationBuilder.add("modmenu.support.afdian", "支持作者: 爱发电");
				translationBuilder.add("modmenu.support.patreon", "支持作者: Patreon");
				translationBuilder.add(getCommentKey("update"), "更新系统的文本翻译");
				translationBuilder.add(widgetText(Silk.DATA, "support"), "支持丝绸开发库");
				translationBuilder.add(widgetText(Silk.DATA, "check_update"), "检查更新");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.update_fail"), "更新系统失效警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.update_fail"), "　　更新系统已失效，无法获取更新请求，请及时更新丝绸开发库版本。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%s停止更新警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "　　%s在当前·Minecraft·版本已停止更新，请及时更新·Minecraft·版本获取最佳体验。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "更新日志：%s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "　　已更新至最新版本，请查看更新日志了解最新改动与特性。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%s现已更新");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "　　%s已更新至%s版本，请尽快更新以获取最佳体验！");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%s现已支持%s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "　　%s已更新至%s版本，请尽快更新以获取最佳体验！");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "　　为了维护模组作者的利益，在自动下载前会跳转到模组官网以确保作者获得应有的广告收益。");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "%s下载中: %s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "　　%s·已安装至·mods·文件夹，请尽快删除旧版本文件。");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "　　%s·已安装至·mods·文件夹，请关闭游戏进行文件清理。");
				translationBuilder.add(configText(Silk.DATA, "update_now"), "立即更新");
				translationBuilder.add(configText(Silk.DATA, "not_update"), "暂时不用");
				translationBuilder.add(configText(Silk.DATA, "confirm_update"), "确认更新");
				translationBuilder.add(configText(Silk.DATA, "downloaded"), "删除文件");
				translationBuilder.add(configText(Silk.DATA, "updated"), "清理更新");
				translationBuilder.add(configText(Silk.DATA, "update"), "更新设置");
				translationBuilder.add(configText(Silk.DATA, "update.update_notify"), "更新通知");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.on"), "如果模组有更新则会直接弹出一个占据整个游戏的更新界面。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.off"), "不会弹出一个占据整个游戏的界面，但是依然会在游戏中通知更新。");
				translationBuilder.add(configText(Silk.DATA, "update.show_changelog"), "显示更新日志");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.on"), "如果第一次启动新版本模组则会弹出一个占据整个游戏的更新日志界面以方便查看新特性。");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.off"), "不会再弹出更新日志界面。除非模组作者另有说明，否则无法查看更新日志。");
				translationBuilder.add(configText(Silk.DATA, "update.check_new_mc_ver_mod"), "检查新 MC 版本模组");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.on"), "如果模组适配了新版本的 Minecraft 也会提示更新。");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.off"), "不提示在新版本 Minecraft 上的模组更新。");
				translationBuilder.add(configText(Silk.DATA, "update.stop_updating_warning"), "停止更新警告");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.on"), "如果模组在此 Minecraft 版本上不再继续更新则每次启动游戏则会警告玩家。");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.off"), "即使模组在此 Minecraft 版本上不再继续更新也不提示玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_sys_fail_warning"), "更新系统失效警告");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.on"), "如果更新系统在联网状态下无法获取更新信息则会显示警告。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.off"), "即使更新系统失效也不警告玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode"), "更新模式");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "更新模式: %s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "手动下载");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "更新会跳转到模组的 Modrinth 页面，需要自行下载更新模组");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "自动下载");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), "§c§l§o！注意！不会自动下载前置模组！§r\n更新会自动下载最新的模组版本文件到模组文件夹，需要手动删除旧版本模组文件。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "自动更新");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), "§6§l§o只能在 Windows 系统中使用！§r\n§c§l§o！注意！不会自动更新前置模组！§r\n模组会自动完成更新所需的一切工作，你只要在模组更新完成后打开游戏即可！");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "更新通道");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "发行版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), "§6模组的正式/发行版本。§r\n在 Modrinth 中的 Release 通道中获取更新。");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "快照版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), "§6模组的预览/测试/公测版本，预先体验还未完善的更新内容。§r\n在 Modrinth 中的 Beta 通道中获取更新。");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "开发版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), "§6模组的开发/内测版本，开发中的版本，非常不稳定。§r\n在 Modrinth 中的 Alpha 通道中获取更新。");
			}
		}
		
		final class ZhHk extends SilkLanguageProvider {
			ZhHk(FabricDataOutput dataOutput) {
				super(dataOutput, "zh_hk");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				translationBuilder.add(getCommentKey("title"), "原生語言為簡體中文(zh_cn)");
				translationBuilder.add(getCommentKey("modmenu"), "Mod Menu 上可翻譯嘅模組描述與摘要");
				translationBuilder.add(getLocalizationNameKey(Silk.DATA), "絲綢開發庫");
				translationBuilder.add(getLocalizationSummaryKey(Silk.DATA), "用於製作具有牛逼哄哄功能嘅 Fabric API 擴展");
				translationBuilder.add(getLocalizationDescriptionKey(Silk.DATA), "用於添加牛逼哄哄嘅模組物品、方塊、實體與特性嘅高級·Fabric·API·擴展。其目嘅在於方便·fabric·模組開發者進行各種高級操作");
				translationBuilder.add("modmenu.support.afdian", "支持作者: 愛發電");
				translationBuilder.add("modmenu.support.patreon", "支持作者: Patreon");
				translationBuilder.add(getCommentKey("update"), "更新系統的文本翻譯");
				translationBuilder.add(widgetText(Silk.DATA, "support"), "支持絲綢開發庫");
				translationBuilder.add(widgetText(Silk.DATA, "check_update"), "檢查更新");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.update_fail"), "更新系統失效警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.update_fail"), "　　更新系統已失效，無法獲取更新請求，請及時更新絲綢開發庫版本。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%s停止更新警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "　　%s在當前·Minecraft·版本已停止更新，請及時更新·Minecraft·版本獲取最佳體驗。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "更新日誌：%s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "　　已更新至最新版本，請查看更新日誌瞭解最新改動與特性。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%s現已更新");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "　　%s已更新至%s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%s現已支持%s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "　　%s已更新至%s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "　　為了維護模組作者的利益，在自動下載前會跳轉到模組官網以確保作者獲得應有的廣告收益。");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "%s下載中: %s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "　　%s·已安装至·mods·文件夹，请尽快删除旧版本文件。");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "　　%s·已安裝至·mods·文件夾，請關閉遊戲進行文件清理。");
				translationBuilder.add(configText(Silk.DATA, "update_now"), "立即更新");
				translationBuilder.add(configText(Silk.DATA, "not_update"), "暫時不用");
				translationBuilder.add(configText(Silk.DATA, "confirm_update"), "確認更新");
				translationBuilder.add(configText(Silk.DATA, "downloaded"), "刪除文件");
				translationBuilder.add(configText(Silk.DATA, "updated"), "清理更新");
				translationBuilder.add(configText(Silk.DATA, "update"), "更新設置");
				translationBuilder.add(configText(Silk.DATA, "update.update_notify"), "更新通知");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.on"), "如果模組有更新則會直接彈出一個佔據整個遊戲的更新界面。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.off"), "不會彈出一個佔據整個遊戲的界面，但是依然會在遊戲中通知更新。");
				translationBuilder.add(configText(Silk.DATA, "update.show_changelog"), "顯示更新日誌");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.on"), "如果第一次啓動新版本模組則會彈出一個佔據整個遊戲的更新日誌界面以方便查看新特性。");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.off"), "不會再彈出更新日誌界面。除非模組作者另有説明，否則無法查看更新日誌。");
				translationBuilder.add(configText(Silk.DATA, "update.check_new_mc_ver_mod"), "檢查新 MC 版本模組");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.on"), "如果模組適配了新版本的 Minecraft 也會提示更新。");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.off"), "不提示在新版本 Minecraft 上的模組更新。");
				translationBuilder.add(configText(Silk.DATA, "update.stop_updating_warning"), "停止更新警告");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.on"), "如果模組在此 Minecraft 版本上不再繼續更新則每次啓動遊戲則會警告玩家。");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.off"), "即使模組在此 Minecraft 版本上不再繼續更新也不提示玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_sys_fail_warning"), "更新系統失效警告");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.on"), "如果更新系統在聯網狀態下無法獲取更新信息則會顯示警告。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.off"), "即使更新系統失效也不警告玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode"), "更新模式");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "更新模式: %s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "手動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "更新會跳轉到模組的 Modrinth 頁面，需要自行下載更新模組");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "自動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), "§c§l§o！注意！不會自動下載前置模組！§r\n更新會自動下載最新的模組版本文件到模組文件夾，需要手動刪除舊版本模組文件。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "自動更新");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), "§6§l§o只能在 Windows 系統中使用！§r\n§c§l§o！注意！不會自動更新前置模組！§r\n模組會自動完成更新所需的一切工作，你只要在模組更新完成後打開遊戲即可！");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "更新通道");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "發行版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), "§6模組的正式/發行版本。§r\n在 Modrinth 中的 Release 通道中獲取更新。");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "快照版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), "§6模組的預覽/測試/公測版本，預先體驗還未完善的更新內容。§r\n在 Modrinth 中的 Beta 通道中獲取更新。");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "開發版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), "§6模組的開發/內測版本，開發中的版本，非常不穩定。§r\n在 Modrinth 中的 Alpha 通道中獲取更新。");
			}
		}
		
		final class ZhTw extends SilkLanguageProvider {
			ZhTw(FabricDataOutput dataOutput) {
				super(dataOutput, "zh_tw");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
				translationBuilder.add(getCommentKey("title"), "原生語言為簡體中文(zh_cn)");
				translationBuilder.add(getCommentKey("modmenu"), "Mod Menu 上可翻譯的模組描述與摘要");
				translationBuilder.add(getLocalizationNameKey(Silk.DATA), "絲綢開發庫");
				translationBuilder.add(getLocalizationSummaryKey(Silk.DATA), "用於製作具有牛逼哄哄功能的 Fabric API 擴充套件");
				translationBuilder.add(getLocalizationDescriptionKey(Silk.DATA), "用於新增牛逼哄哄的模組物品、方塊、實體與特性的高階·Fabric·API·擴充套件。其目的在於方便·fabric·模組開發者進行各種高階操作");
				translationBuilder.add("modmenu.support.afdian", "支援作者: 愛發電");
				translationBuilder.add("modmenu.support.patreon", "支援作者: Patreon");
				translationBuilder.add(getCommentKey("update"), "更新系統的文字翻譯");
				translationBuilder.add(widgetText(Silk.DATA, "support"), "支援絲綢開發庫");
				translationBuilder.add(widgetText(Silk.DATA, "check_update"), "檢查更新");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.update_fail"), "更新系統失效警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.update_fail"), "　　更新系統已失效，無法獲取更新請求，請及時更新絲綢開發庫版本。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%s停止更新警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "　　%s在當前·Minecraft·版本已停止更新，請及時更新·Minecraft·版本獲取最佳體驗。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "更新日誌：%s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "　　已更新至最新版本，請檢視更新日誌瞭解最新改動與特性。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%s現已更新");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "　　%s已更新至%s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%s現已支援%s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "　　%s已更新至%s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "　　為了維護模組作者的利益，在自動下載前會跳轉到模組官網以確保作者獲得應有的廣告收益。");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "%s下載中: %s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "　　%s·已安装至·mods·檔案夹，请尽快删除旧版本檔案。");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "　　%s·已安裝至·mods·資料夾，請關閉遊戲進行檔案清理。");
				translationBuilder.add(configText(Silk.DATA, "update_now"), "立即更新");
				translationBuilder.add(configText(Silk.DATA, "not_update"), "暫時不用");
				translationBuilder.add(configText(Silk.DATA, "confirm_update"), "確認更新");
				translationBuilder.add(configText(Silk.DATA, "downloaded"), "刪除檔案");
				translationBuilder.add(configText(Silk.DATA, "updated"), "清理更新");
				translationBuilder.add(configText(Silk.DATA, "update"), "更新設定");
				translationBuilder.add(configText(Silk.DATA, "update.update_notify"), "更新通知");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.on"), "如果模組有更新則會直接彈出一個佔據整個遊戲的更新介面。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.off"), "不會彈出一個佔據整個遊戲的介面，但是依然會在遊戲中通知更新。");
				translationBuilder.add(configText(Silk.DATA, "update.show_changelog"), "顯示更新日誌");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.on"), "如果第一次啟動新版本模組則會彈出一個佔據整個遊戲的更新日誌介面以方便檢視新特性。");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.off"), "不會再彈出更新日誌介面。除非模組作者另有說明，否則無法檢視更新日誌。");
				translationBuilder.add(configText(Silk.DATA, "update.check_new_mc_ver_mod"), "檢查新 MC 版本模組");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.on"), "如果模組適配了新版本的 Minecraft 也會提示更新。");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.off"), "不提示在新版本 Minecraft 上的模組更新。");
				translationBuilder.add(configText(Silk.DATA, "update.stop_updating_warning"), "停止更新警告");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.on"), "如果模組在此 Minecraft 版本上不再繼續更新則每次啟動遊戲則會警告玩家。");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.off"), "即使模組在此 Minecraft 版本上不再繼續更新也不提示玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_sys_fail_warning"), "更新系統失效警告");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.on"), "如果更新系統在聯網狀態下無法獲取更新資訊則會顯示警告。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.off"), "即使更新系統失效也不警告玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode"), "更新模式");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "更新模式: %s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "手動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "更新會跳轉到模組的 Modrinth 頁面，需要自行下載更新模組");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "自動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), "§c§l§o！注意！不會自動下載前置模組！§r\n更新會自動下載最新的模組版本檔案到模組資料夾，需要手動刪除舊版本模組檔案。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "自動更新");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), "§6§l§o只能在 Windows 系統中使用！§r\n§c§l§o！注意！不會自動更新前置模組！§r\n模組會自動完成更新所需的一切工作，你只要在模組更新完成後開啟遊戲即可！");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "更新通道");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "釋出版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), "§6模組的正式/釋出版本。§r\n在 Modrinth 中的 Release 通道中獲取更新。");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "快照版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), "§6模組的預覽/測試/公測版本，預先體驗還未完善的更新內容。§r\n在 Modrinth 中的 Beta 通道中獲取更新。");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "開發版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), "§6模組的開發/內測版本，開發中的版本，非常不穩定。§r\n在 Modrinth 中的 Alpha 通道中獲取更新。");
			}
		}
	}
}
