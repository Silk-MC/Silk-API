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

package pers.saikel0rado1iu.silk.util;

import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p><b style="color:FFC800"><font size="+1">有关屏幕的所有实用方法</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface ScreenUtil {
	@SilkApi
	static String configText(ModBasicData mod, String key) {
		return "config." + mod.getId() + '.' + key + ("".equals(key) ? "text" : ".text");
	}
	
	@SilkApi
	static String configTip(ModBasicData mod, String key) {
		return "config." + mod.getId() + '.' + key + ("".equals(key) ? "tip" : ".tip");
	}
	
	@SilkApi
	static String widgetTitle(ModBasicData mod, String key) {
		return "title." + mod.getId() + '.' + key;
	}
	
	@SilkApi
	static String widgetText(ModBasicData mod, String key) {
		return "text." + mod.getId() + '.' + key;
	}
	
	@SilkApi
	static ButtonWidget.Builder backButton(Screen screen) {
		return ButtonWidget.builder(ScreenTexts.BACK, (button) -> screen.close());
	}
	
	@SilkApi
	static ButtonWidget.Builder cancelButton(Screen screen) {
		return ButtonWidget.builder(ScreenTexts.CANCEL, (button) -> screen.close());
	}
	
	@SilkApi
	static ButtonWidget.Builder doneButton(Screen screen) {
		return ButtonWidget.builder(ScreenTexts.DONE, (button) -> screen.close());
	}
	
	@SilkApi
	static ButtonWidget.Builder linkButton(Screen parent, Text text, String url) {
		return linkButton(parent, text, url, false);
	}
	
	@SilkApi
	static ButtonWidget.Builder linkButton(Screen parent, Text text, String url, boolean canTrust) {
		return ButtonWidget.builder(text, ConfirmLinkScreen.opening(url, parent, canTrust));
	}
	
	@SilkApi
	static ButtonWidget.Builder linkButton(Screen parent, ModBasicData mod, ModBasicData.LinkType linkType) {
		return linkButton(parent, mod, linkType, false);
	}
	
	@SilkApi
	static ButtonWidget.Builder linkButton(Screen parent, ModBasicData mod, ModBasicData.LinkType linkType, boolean canTrust) {
		Text text = Text.translatable(configText(mod, linkType.toString().toLowerCase()));
		return mod.getLink(linkType).isPresent() ? linkButton(parent, text, mod.getLink(linkType).get().toString(), canTrust) : linkButton(parent, text, Silk.DATA.getLink(linkType).orElseThrow().toString(), true);
	}
	
	/**
	 * 读取的更新日志位置于资源包根目录下的 log 文件夹内
	 */
	@SilkApi
	static String readChangelog(ModBasicData mod) {
		try {
			URL path = ScreenUtil.class.getResource("/assets/" + mod.getId() + "/log/" + LocalizationUtil.getLanguage() + ".txt");
			if (path == null) return "Changelog does not exist!" + Silk.DATA.getInfo();
			Path logPath = Path.of(path.toURI());
			StringBuilder log = new StringBuilder().append(Files.readString(logPath, StandardCharsets.UTF_8));
			log = new StringBuilder(log.toString().replaceAll("\r", "\n"));
			log = new StringBuilder(log.toString().replaceAll("\n\n", "\n"));
			if (LocalizationUtil.isChinese()) log = new StringBuilder(log.toString().replaceAll("\t", "—").replaceAll(" ", "-"));
			else log = new StringBuilder(log.toString().replaceAll("\t", "  "));
			return log.toString();
		} catch (IOException | URISyntaxException e) {
			return "Changelog does not exist!" + Silk.DATA.getInfo();
		}
	}
}
