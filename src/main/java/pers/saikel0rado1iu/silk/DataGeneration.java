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

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import pers.saikel0rado1iu.silk.datagen.SilkLanguageProvider;

/**
 * <p><b style="color:FFC800"><font size="+1">用于 Silk-API 的数据生成器</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public final class DataGeneration implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(LocalizationGenerator.EnUs::new);
		pack.addProvider(LocalizationGenerator.ZhCn::new);
		pack.addProvider(LocalizationGenerator.ZhHk::new);
		pack.addProvider(LocalizationGenerator.ZhTw::new);
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
				translationBuilder.add("config.silk-api.support.text", "Support for Silk API");
				translationBuilder.add(getCommentKey("update"), "Text translation of the update system");
				translationBuilder.add("title.silk-api.update.update_fail", "Update System Fail Warning");
				translationBuilder.add("text.silk-api.update.update_fail", "  The update system is invalid and the update request cannot be obtained. Please update the silk-api version in time.");
				translationBuilder.add("title.silk-api.update.stop_update", "%s Stop Update Warning");
				translationBuilder.add("text.silk-api.update.stop_update", "  %s is no longer updated in the current Minecraft version, please update the Minecraft version in time for the best experience.");
				translationBuilder.add("title.silk-api.update.mod_log", "Changelog: %s");
				translationBuilder.add("text.silk-api.update.mod_log", "  Has been updated to the latest version, please check the changelog.");
				translationBuilder.add("title.silk-api.update.this_mc_ver", "%s Has Update");
				translationBuilder.add("text.silk-api.update.this_mc_ver", "  %s has been updated to %s, please update quickly!");
				translationBuilder.add("title.silk-api.update.new_mc_ver", "%s Now Supports %s");
				translationBuilder.add("text.silk-api.update.new_mc_ver", "  %s has been updated to %s, please update quickly!");
				translationBuilder.add("text.silk-api.confirm_update", "  In order to protect the interests of mod authors, they will jump to the mod website before automatic download to ensure that the authors get the advertising revenue they deserve.");
				translationBuilder.add("text.silk-api.downloading", "Downloading %s: %s%%");
				translationBuilder.add("text.silk-api.downloaded", "  %s has been installed in the mods folder. Please delete the old version quickly.");
				translationBuilder.add("text.silk-api.updated", "  %s has been installed in the mods folder. Please close the game to clean the files.");
				translationBuilder.add("config.silk-api.update_now.text", "Update Now");
				translationBuilder.add("config.silk-api.not_update.text", "Not Yet");
				translationBuilder.add("config.silk-api.confirm_update.text", "Confirm Update");
				translationBuilder.add("config.silk-api.downloaded.text", "Delete Files");
				translationBuilder.add("config.silk-api.updated.text", "Clean Update");
				translationBuilder.add("config.silk-api.update.text", "Update Settings");
				translationBuilder.add("config.silk-api.update.update_notify.text", "Update Notify");
				translationBuilder.add("config.silk-api.update.update_notify.on.tip", "If mod can be updated, an update prompt screen that occupies the entire game will pop up directly.");
				translationBuilder.add("config.silk-api.update.update_notify.off.tip", "A screen that occupies the entire game will not pop up, but updates will still be prompted in the game.");
				translationBuilder.add("config.silk-api.update.show_changelog.text", "Show Changelog");
				translationBuilder.add("config.silk-api.update.show_changelog.on.tip", "If you start a new version of mod for the first time, an changelog screen that occupies the entire game will pop up to make it easy to see the new features.");
				translationBuilder.add("config.silk-api.update.show_changelog.off.tip", "The changelog screen no longer pops up, and the changelog cannot be viewed unless otherwise provided by the mod author.");
				translationBuilder.add("config.silk-api.update.check_new_mc_ver_mod.text", "Check New Minecraft Version Mod");
				translationBuilder.add("config.silk-api.update.check_new_mc_ver_mod.on.tip", "If mod adapts to a new version of Minecraft, it will also prompt you to update.");
				translationBuilder.add("config.silk-api.update.check_new_mc_ver_mod.off.tip", "Do not prompt for mod updates on the new version of Minecraft.");
				translationBuilder.add("config.silk-api.update.stop_updating_warning.text", "Stop Updating Warning");
				translationBuilder.add("config.silk-api.update.stop_updating_warning.on.tip", "If mod does not continue to update on this Minecraft version, players will be warned every time the game is started.");
				translationBuilder.add("config.silk-api.update.stop_updating_warning.off.tip", "Even if mod does not continue to update on this Minecraft version, it will not prompt the player.");
				translationBuilder.add("config.silk-api.update.update_sys_fail_warning.text", "Update System Fail Warning");
				translationBuilder.add("config.silk-api.update.update_sys_fail_warning.on.tip", "If the update system cannot get the update information when it is online, a warning will be displayed.");
				translationBuilder.add("config.silk-api.update.update_sys_fail_warning.off.tip", "Players are not warned even if the update system fails.");
				translationBuilder.add("config.silk-api.update.update_mode.text", "Update Mode");
				translationBuilder.add("config.silk-api.update.update_mode_.text", "Update Mode: %s");
				translationBuilder.add("config.silk-api.update.update_mode.manual_download.text", "Manual Download");
				translationBuilder.add("config.silk-api.update.update_mode.manual_download.tip", "Updates will be redirected to mod's modrinth website. You need to download the new version of mod yourself.");
				translationBuilder.add("config.silk-api.update.update_mode.auto_download.text", "Auto Download");
				translationBuilder.add("config.silk-api.update.update_mode.auto_download.tip", "§c§l§o!Be careful! Mod dependencies are not automatically downloaded! §r\nUpdates will automatically download the latest mod version files to the mods folder. You need to delete the old version mod files manually.");
				translationBuilder.add("config.silk-api.update.update_mode.auto_update.text", "Auto Update");
				translationBuilder.add("config.silk-api.update.update_mode.auto_update.tip", "§6§l§oCan only be used in Windows systems! §r\n§c§l§o!Be careful! Mod dependencies are not automatically updated! §r\nThe system will automatically complete all the work needed for the update, you just need to open the game after the mod update is completed!");
				translationBuilder.add("config.silk-api.update.update_channel.text", "Update Channel");
				translationBuilder.add("config.silk-api.update.update_channel.release.text", "Release");
				translationBuilder.add("config.silk-api.update.update_channel.release.tip", "§6Official/Release version of mod. §r\nGet updates in the release channel in modrinth.");
				translationBuilder.add("config.silk-api.update.update_channel.beta.text", "Snapshot");
				translationBuilder.add("config.silk-api.update.update_channel.beta.tip", "§6Preview/Test/Beta version of mod to experience the new content that is not perfect in advance.§r\nGet updates in the beta channel in modrinth.");
				translationBuilder.add("config.silk-api.update.update_channel.alpha.text", "Development");
				translationBuilder.add("config.silk-api.update.update_channel.alpha.tip", "§6Developing/Alpha version of mod, the version under development, is very unstable.§r\nGet updates in the alpha channel in modrinth.");
			}
		}
		
		final class ZhCn extends SilkLanguageProvider {
			ZhCn(FabricDataOutput dataOutput) {
				super(dataOutput, "zh_cn");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
			}
		}
		
		final class ZhHk extends SilkLanguageProvider {
			ZhHk(FabricDataOutput dataOutput) {
				super(dataOutput, "zh_hk");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
			}
		}
		
		final class ZhTw extends SilkLanguageProvider {
			ZhTw(FabricDataOutput dataOutput) {
				super(dataOutput, "zh_tw");
			}
			
			@Override
			public void generateTranslations(TranslationBuilder translationBuilder) {
			}
		}
	}
}
