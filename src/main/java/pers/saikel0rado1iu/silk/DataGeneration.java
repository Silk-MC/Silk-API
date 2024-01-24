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
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.api.entity.SilkEntityTypeTags;
import pers.saikel0rado1iu.silk.gen.ModDataGeneration;
import pers.saikel0rado1iu.silk.gen.data.SilkLanguageProvider;
import pers.saikel0rado1iu.silk.util.Minecraft;

import java.util.concurrent.CompletableFuture;

import static pers.saikel0rado1iu.silk.gen.data.SilkTagGenerator.putSpawnGroupEntityInTags;
import static pers.saikel0rado1iu.silk.util.TextUtil.*;

/**
 * <h2 style="color:FFC800">用于 Silk-API 的数据生成器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public final class DataGeneration extends ModDataGeneration {
	@Override
	public void datagen(FabricDataGenerator.Pack pack) {
		pack.addProvider(TagGeneration.EntityType::new);
		pack.addProvider(LocalizationGenerator.EnUs::new);
		pack.addProvider(LocalizationGenerator.ZhCn::new);
		pack.addProvider(LocalizationGenerator.ZhHk::new);
		pack.addProvider(LocalizationGenerator.ZhTw::new);
	}
	
	@Override
	public void dynamicRegistry(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
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
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%1$s Stop Update Warning");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "  %1$s is no longer updated in the current Minecraft version, please update the Minecraft version in time for the best experience.");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "Changelog: %1$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "  Has been updated to the latest version, please check the changelog.");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%1$s Has Update");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "  %1$s has been updated to %2$s, please update quickly!");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%1$s Now Supports %2$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "  %1$s has been updated to %2$s, please update quickly!");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "  In order to protect the interests of mod authors, they will jump to the mod website before automatic download to ensure that the authors get the advertising revenue they deserve.");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "Downloading %1$s: %2$s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "  %1$s has been installed in the mods folder. Please delete the old version quickly.");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "  %1$s has been installed in the mods folder. Please close the game to clean the files.");
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
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "Update Mode: %1$s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "Manual Download");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "Updates will be redirected to mod's modrinth website. You need to download the new version of mod yourself.");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "Auto Download");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), """
						§c§l§o!Be careful! Mod dependencies are not automatically downloaded! §r
						Updates will automatically download the latest mod version files to the mods folder. You need to delete the old version mod files manually.""");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "Auto Update");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), """
						§6§l§oCan only be used in Windows systems! §r
						§c§l§o!Be careful! Mod dependencies are not automatically updated! §r
						The system will automatically complete all the work needed for the update, you just need to open the game after the mod update is completed!""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "Update Channel");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "Release");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), """
						§6Official/Release version of mod. §r
						Get updates in the release channel in modrinth.""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "Snapshot");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), """
						§6Preview/Test/Beta version of mod to experience the new content that is not perfect in advance.§r
						Get updates in the beta channel in modrinth.""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "Development");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), """
						§6Developing/Alpha version of mod, the version under development, is very unstable.§r
						Get updates in the alpha channel in modrinth.""");
				translationBuilder.add(getCommentKey("world_upgrade"), "Text translation of the world upgrade system");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrade.tooltip"), "This world was created in an earlier version of '%1$s'. It is recommended to upgrade the world to better play '%1$s'.");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrade.tooltip"), "This world was created in an newer version of '%1$s'. Forcibly playing this world may have unexpected consequences. You must downgrade the world or update the latest version of '%1$s' to play this world!");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.flush"), "Flush World");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.upgrade"), "Upgrade World");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.downgrade"), "Downgrade World");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.prompt"), """
						The world upgrader will detect the contents of each chunk to determine whether this chunk should be modified.
						All chunks that need to be modified will be §c§lDELETE§r for regeneration.
						Depending on the state of the world, you may §c§lLOSE§r data saved in certain chunks.
						If you are not sure whether this data is important, we recommend that you §6§lBACKUP§r your world before %1$s.""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.warning"), "Need Upgrade World!");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.warning"), """
						This world is not the latest version and requires a world upgrade.
						If the world is not upgraded, the world will be forced to upgrade to the latest version, which may have unpredictable consequences.
						We recommend that you go to '%1$s' to upgrade the world so that you can play this world better.
						If you understand the consequences and want to continue playing, we recommend that you backup your world first just in case.""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.screen"), "%1$s '%2$s'");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.deleted"), "Deleted chunks: %1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.handled"), "Handled chunks：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrading"), "Upgrading chunks...");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrading"), "Downgrading chunks...");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.flushing"), "Flushing chunks...");
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
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%1$s停止更新警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "　　%1$s在当前·Minecraft·版本已停止更新，请及时更新·Minecraft·版本获取最佳体验。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "更新日志：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "　　已更新至最新版本，请查看更新日志了解最新改动与特性。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%1$s现已更新");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "　　%1$s已更新至%2$s版本，请尽快更新以获取最佳体验！");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%1$s现已支持%2$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "　　%1$s已更新至%2$s版本，请尽快更新以获取最佳体验！");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "　　为了维护模组作者的利益，在自动下载前会跳转到模组官网以确保作者获得应有的广告收益。");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "%1$s下载中: %2$s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "　　%1$s·已安装至·mods·文件夹，请尽快删除旧版本文件。");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "　　%1$s·已安装至·mods·文件夹，请关闭游戏进行文件清理。");
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
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "更新模式: %1$s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "手动下载");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "更新会跳转到模组的 Modrinth 页面，需要自行下载更新模组");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "自动下载");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), """
						§c§l§o！注意！不会自动下载前置模组！§r
						更新会自动下载最新的模组版本文件到模组文件夹，需要手动删除旧版本模组文件。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "自动更新");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), """
						§6§l§o只能在 Windows 系统中使用！§r
						§c§l§o！注意！不会自动更新前置模组！§r
						模组会自动完成更新所需的一切工作，你只要在模组更新完成后打开游戏即可！""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "更新通道");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "发行版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), """
						§6模组的正式/发行版本。§r
						在 Modrinth 中的 Release 通道中获取更新。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "快照版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), """
						§6模组的预览/测试/公测版本，预先体验还未完善的更新内容。§r
						在 Modrinth 中的 Beta 通道中获取更新。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "开发版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), """
						§6模组的开发/内测版本，开发中的版本，非常不稳定。§r
						在 Modrinth 中的 Alpha 通道中获取更新。""");
				translationBuilder.add(getCommentKey("world_upgrade"), "世界升级系统的文本翻译");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrade.tooltip"), "此世界是在更低版本的「%1$s」中创建的，建议进行世界升级以便更好的游玩「%1$s」。");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrade.tooltip"), "此世界是在更新版本的「%1$s」中创建的，如果强行进入此世界可能会有意想不到的后果，您必须要降级世界或者更新最新版「%1$s」才能进入此世界！");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.flush"), "刷新世界");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.upgrade"), "升级世界");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.downgrade"), "降级世界");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.prompt"), """
						世界升级器会检测每个区块中的内容来判断是否应该修改此区块。
						所有需要修改的区块都会被§c§l删除§r以便重新生成。
						取决于世界的状况，你可能会§c§l丢失§r保存在某些区块中的数据。
						如果你不确定这些数据是否重要，我们建议你先§6§l备份§r再%1$s。""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.warning"), "需要升级世界！");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.warning"), """
						此世界不为最新版本，需要进行世界升级。
						如果不进行世界升级，世界会被强制升级为最新版本，这可能会产生无法预测的后果。
						我们建议你前往「%1$s」进行世界升级，以便更好的游玩此世界。
						如果你已了解后果，想继续游戏的话，我们建议你先备份世界以防万一。""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.screen"), "正在%1$s“%2$s”");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.deleted"), "已删除的区块数：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.handled"), "已处理的区块数：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrading"), "升级区块中…");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrading"), "降级区块中…");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.flushing"), "刷新区块中…");
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
				translationBuilder.add(getCommentKey("update"), "更新系統嘅文本翻譯");
				translationBuilder.add(widgetText(Silk.DATA, "support"), "支持絲綢開發庫");
				translationBuilder.add(widgetText(Silk.DATA, "check_update"), "檢查更新");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.update_fail"), "更新系統失效警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.update_fail"), "　　更新系統已失效，無法獲取更新請求，請及時更新絲綢開發庫版本。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%1$s停止更新警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "　　%1$s在當前·Minecraft·版本已停止更新，請及時更新·Minecraft·版本獲取最佳體驗。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "更新日誌：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "　　已更新至最新版本，請查看更新日誌瞭解最新改動與特性。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%1$s現已更新");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "　　%1$s已更新至%2$s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%1$s現已支持%2$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "　　%1$s已更新至%2$s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "　　為了維護模組作者嘅利益，在自動下載前會跳轉到模組官網以確保作者獲得應有嘅廣告收益。");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "%1$s下載中: %2$s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "　　%1$s·已安装至·mods·文件夹，请尽快删除旧版本文件。");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "　　%1$s·已安裝至·mods·文件夾，請關閉遊戲進行文件清理。");
				translationBuilder.add(configText(Silk.DATA, "update_now"), "立即更新");
				translationBuilder.add(configText(Silk.DATA, "not_update"), "暫時不用");
				translationBuilder.add(configText(Silk.DATA, "confirm_update"), "確認更新");
				translationBuilder.add(configText(Silk.DATA, "downloaded"), "刪除文件");
				translationBuilder.add(configText(Silk.DATA, "updated"), "清理更新");
				translationBuilder.add(configText(Silk.DATA, "update"), "更新設置");
				translationBuilder.add(configText(Silk.DATA, "update.update_notify"), "更新通知");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.on"), "如果模組有更新則會直接彈出一個佔據整個遊戲嘅更新界面。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_notify.off"), "不會彈出一個佔據整個遊戲嘅界面，但是依然會在遊戲中通知更新。");
				translationBuilder.add(configText(Silk.DATA, "update.show_changelog"), "顯示更新日誌");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.on"), "如果第一次啓動新版本模組則會彈出一個佔據整個遊戲嘅更新日誌界面以方便查看新特性。");
				translationBuilder.add(configTip(Silk.DATA, "update.show_changelog.off"), "不會再彈出更新日誌界面。除非模組作者另有説明，否則無法查看更新日誌。");
				translationBuilder.add(configText(Silk.DATA, "update.check_new_mc_ver_mod"), "檢查新 MC 版本模組");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.on"), "如果模組適配了新版本嘅 Minecraft 也會提示更新。");
				translationBuilder.add(configTip(Silk.DATA, "update.check_new_mc_ver_mod.off"), "不提示在新版本 Minecraft 上嘅模組更新。");
				translationBuilder.add(configText(Silk.DATA, "update.stop_updating_warning"), "停止更新警告");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.on"), "如果模組在此 Minecraft 版本上不再繼續更新則每次啓動遊戲則會警告玩家。");
				translationBuilder.add(configTip(Silk.DATA, "update.stop_updating_warning.off"), "即使模組在此 Minecraft 版本上不再繼續更新也不提示玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_sys_fail_warning"), "更新系統失效警告");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.on"), "如果更新系統在聯網狀態下無法獲取更新信息則會顯示警告。");
				translationBuilder.add(configTip(Silk.DATA, "update.update_sys_fail_warning.off"), "即使更新系統失效也不警告玩家。");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode"), "更新模式");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "更新模式: %1$s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "手動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "更新會跳轉到模組嘅 Modrinth 頁面，需要自行下載更新模組");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "自動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), """
						§c§l§o！注意！不會自動下載前置模組！§r
						更新會自動下載最新嘅模組版本文件到模組文件夾，需要手動刪除舊版本模組文件。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "自動更新");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), """
						§6§l§o只能在 Windows 系統中使用！§r
						§c§l§o！注意！不會自動更新前置模組！§r
						模組會自動完成更新所需嘅一切工作，你只要在模組更新完成後打開遊戲即可！""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "更新通道");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "發行版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), """
						§6模組嘅正式/發行版本。§r
						在 Modrinth 中嘅 Release 通道中獲取更新。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "快照版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), """
						§6模組嘅預覽/測試/公測版本，預先體驗還未完善嘅更新內容。§r
						在 Modrinth 中嘅 Beta 通道中獲取更新。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "開發版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), """
						§6模組嘅開發/內測版本，開發中嘅版本，非常不穩定。§r
						在 Modrinth 中嘅 Alpha 通道中獲取更新。""");
				translationBuilder.add(getCommentKey("world_upgrade"), "世界升級系統嘅文本翻譯");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrade.tooltip"), "此世界是在更低版本嘅「%1$s」中創建嘅，建議進行世界升級以便更好嘅遊玩「%1$s」。");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrade.tooltip"), "此世界是在更新版本嘅「%1$s」中創建嘅，如果強行進入此世界可能會有意想不到嘅後果，您必須要降級世界或者更新最新版「%1$s」才能進入此世界！");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.flush"), "刷新世界");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.upgrade"), "升級世界");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.downgrade"), "降級世界");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.prompt"), """
						世界升級器會檢測每個區塊中的內容來判斷是否應該修改此區塊。
						所有需要修改的區塊都會被§c§l刪除§r以便重新生成。
						取決於世界的狀況，你可能會§c§l丟失§r保存在某些區塊中的數據。
						如果你不確定這些數據是否重要，我們建議你先§6§l備份§r再%1$s。""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.warning"), "需要升級世界！");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.warning"), """
						此世界不為最新版本，需要進行世界升級。
						如果不進行世界升級，世界會被強制升級為最新版本，這可能會產生無法預測的後果。
						我們建議你前往「%1$s」進行世界升級，以便更好的遊玩此世界。
						如果你已瞭解後果，想繼續遊戲的話，我們建議你先備份世界以防萬一。""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.screen"), "正在%1$s '%2$s'");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.deleted"), "已刪除區塊數量：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.handled"), "已處理區塊數量：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrading"), "正在升級區塊⋯⋯");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrading"), "正在降級區塊⋯⋯");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.flushing"), "正在刷新區塊⋯⋯");
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
				translationBuilder.add(widgetTitle(Silk.DATA, "update.stop_update"), "%1$s停止更新警告");
				translationBuilder.add(widgetText(Silk.DATA, "update.stop_update"), "　　%1$s在當前·Minecraft·版本已停止更新，請及時更新·Minecraft·版本獲取最佳體驗。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.mod_log"), "更新日誌：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.mod_log"), "　　已更新至最新版本，請檢視更新日誌瞭解最新改動與特性。");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.this_mc_ver"), "%1$s現已更新");
				translationBuilder.add(widgetText(Silk.DATA, "update.this_mc_ver"), "　　%1$s已更新至%2$s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetTitle(Silk.DATA, "update.new_mc_ver"), "%1$s現已支援%2$s");
				translationBuilder.add(widgetText(Silk.DATA, "update.new_mc_ver"), "　　%1$s已更新至%2$s版本，請儘快更新以獲取最佳體驗！");
				translationBuilder.add(widgetText(Silk.DATA, "confirm_update"), "　　為了維護模組作者的利益，在自動下載前會跳轉到模組官網以確保作者獲得應有的廣告收益。");
				translationBuilder.add(widgetText(Silk.DATA, "downloading"), "%1$s下載中: %2$s%%");
				translationBuilder.add(widgetText(Silk.DATA, "downloaded"), "　　%1$s·已安装至·mods·檔案夹，请尽快删除旧版本檔案。");
				translationBuilder.add(widgetText(Silk.DATA, "updated"), "　　%1$s·已安裝至·mods·資料夾，請關閉遊戲進行檔案清理。");
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
				translationBuilder.add(configText(Silk.DATA, "update.update_mode_"), "更新模式: %1$s");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.manual_download"), "手動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.manual_download"), "更新會跳轉到模組的 Modrinth 頁面，需要自行下載更新模組");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_download"), "自動下載");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_download"), """
						§c§l§o！注意！不會自動下載前置模組！§r
						更新會自動下載最新的模組版本檔案到模組資料夾，需要手動刪除舊版本模組檔案。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_mode.auto_update"), "自動更新");
				translationBuilder.add(configTip(Silk.DATA, "update.update_mode.auto_update"), """
						§6§l§o只能在 Windows 系統中使用！§r
						§c§l§o！注意！不會自動更新前置模組！§r
						模組會自動完成更新所需的一切工作，你只要在模組更新完成後開啟遊戲即可！""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel"), "更新通道");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.release"), "釋出版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.release"), """
						§6模組的正式/釋出版本。§r
						在 Modrinth 中的 Release 通道中獲取更新。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.beta"), "快照版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.beta"), """
						§6模組的預覽/測試/公測版本，預先體驗還未完善的更新內容。§r
						在 Modrinth 中的 Beta 通道中獲取更新。""");
				translationBuilder.add(configText(Silk.DATA, "update.update_channel.alpha"), "開發版");
				translationBuilder.add(configTip(Silk.DATA, "update.update_channel.alpha"), """
						§6模組的開發/內測版本，開發中的版本，非常不穩定。§r
						在 Modrinth 中的 Alpha 通道中獲取更新。""");
				translationBuilder.add(getCommentKey("world_upgrade"), "世界升級系統的文字翻譯");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrade.tooltip"), "此世界是在更低版本的「%1$s」中建立的，建議進行世界升級以便更好的遊玩「%1$s」。");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrade.tooltip"), "此世界是在更新版本的「%1$s」中建立的，如果強行進入此世界可能會有意想不到的後果，您必須要降級世界或者更新最新版「%1$s」才能進入此世界！");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.flush"), "重新整理世界");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.upgrade"), "升級世界");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.downgrade"), "降級世界");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.prompt"), """
						世界升級器會檢測每個區塊中的內容來判斷是否應該修改此區塊。
						所有需要修改的區塊都會被§c§l刪除§r以便重新生成。
						取決於世界的狀況，你可能會§c§l丟失§r儲存在某些區塊中的資料。
						如果你不確定這些資料是否重要，我們建議你先§6§l備份§r再%1$s。""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.warning"), "需要升級世界！");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.warning"), """
						此世界不為最新版本，需要進行世界升級。
						如果不進行世界升級，世界會被強制升級為最新版本，這可能會產生無法預測的後果。
						我們建議你前往「%1$s」進行世界升級，以便更好的遊玩此世界。
						如果你已瞭解後果，想繼續遊戲的話，我們建議你先備份世界以防萬一。""");
				translationBuilder.add(widgetTitle(Silk.DATA, "world_upgrade.screen"), "正在%1$s「%2$s」");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.deleted"), "已刪除的區塊數：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.handled"), "已處理的區塊數：%1$s");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.upgrading"), "正在升級區塊...");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.downgrading"), "正在降級區塊...");
				translationBuilder.add(widgetText(Silk.DATA, "world_upgrade.flushing"), "正在重新整理區塊...");
			}
		}
	}
}
