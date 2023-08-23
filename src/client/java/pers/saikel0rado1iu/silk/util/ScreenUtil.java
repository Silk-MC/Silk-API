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
	static ButtonWidget.Builder linkButton(Screen parent, Text text, ModBasicData mod) {
		return linkButton(parent, text, mod, false);
	}
	
	@SilkApi
	static ButtonWidget.Builder linkButton(Screen parent, Text text, ModBasicData mod, boolean canTrust) {
		return ButtonWidget.builder(text, ConfirmLinkScreen.opening(mod.getLink(ModBasicData.LinkType.SUPPORT).orElseThrow().toString(), parent, canTrust));
	}
	
	@SilkApi
	static ButtonWidget.Builder supportButton(Screen parent, ModBasicData mod) {
		return supportButton(parent, mod, false);
	}
	
	@SilkApi
	static ButtonWidget.Builder supportButton(Screen parent, ModBasicData mod, boolean canTrust) {
		Text text = Text.translatable(configText(mod, ModBasicData.LinkType.SUPPORT.toString().toLowerCase()));
		return mod.getLink(ModBasicData.LinkType.SUPPORT).isPresent() ? linkButton(parent, text, mod, canTrust) : linkButton(parent, text, Silk.DATA, true);
	}
}
