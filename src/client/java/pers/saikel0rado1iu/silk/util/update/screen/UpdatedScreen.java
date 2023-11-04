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

package pers.saikel0rado1iu.silk.util.update.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import java.io.IOException;

/**
 * <p><b style="color:FFC800"><font size="+1">更新完成屏幕</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class UpdatedScreen extends UpdateScreen {
	protected UpdatedScreen(Screen parent, UpdateShow updateShow, Text title) {
		super(parent, updateShow, title);
		this.updateShow.getUpdateData().setCanShowChangelog(true);
		this.updateShow.getUpdateData().save();
	}
	
	@Override
	protected void init() {
		super.init();
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0,
				Text.translatable(TextUtil.widgetText(Silk.DATA, "updated"), updateShow.getMod().getJarName()),
				textRenderer).setMaxWidth(screenWidth - INTERVAL);
		int height1 = (int) (height - (height - screenHeight) / 2 - BUTTON_SPACING * 1.5);
		int height2 = (height - screenHeight) / 2 + textRenderer.fontHeight + ICON_SIZE + INTERVAL * 2 - messageText.getHeight();
		int height3 = height1 - height2;
		messageText.setPosition((width - messageText.getWidth()) / 2, height2 + height3 / 2);
		addDrawableChild(messageText);
		int fullButtonWidth = screenWidth - INTERVAL;
		int fullButtonX = (width - (screenWidth - INTERVAL)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		addDrawableChild(ButtonWidget.builder(Text.translatable(TextUtil.configText(Silk.DATA, "updated")).setStyle(Style.EMPTY.withBold(true)), (button) -> {
			// 使用断言消除 setScreen NullPointerException警告
			try {
				Runtime.getRuntime().exec("cmd /A /C start \"\" /D \""
						+ updateShow.getMod().getPath() + "\" \"" + updateShow.getUpdateData().getBatName() + "\" CHCP 65001 ");
				if (client != null) client.scheduleStop();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}).dimensions(fullButtonX, (int) (buttonY - BUTTON_SPACING * 1.5), fullButtonWidth, BUTTON_HEIGHT).build());
	}
}
