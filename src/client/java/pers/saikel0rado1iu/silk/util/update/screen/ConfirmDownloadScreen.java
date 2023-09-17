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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

/**
 * <p><b style="color:FFC800"><font size="+1">下载模组时弹出的确认网页跳转屏幕</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ConfirmDownloadScreen extends UpdateScreen {
	private static final String KEY = "confirm_update";
	private int transColor = 0xFF0000;
	private ButtonWidget updateButton;
	
	protected ConfirmDownloadScreen(Screen parent, UpdateShow updateShow, Text title) {
		super(parent, updateShow, title);
	}
	
	@Override
	protected void init() {
		super.init();
		int fullButtonWidth = screenWidth - INTERVAL;
		int fullButtonX = (width - (screenWidth - INTERVAL)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0, Text.translatable(ScreenUtil.widgetText(Silk.DATA, KEY)), textRenderer)
				.setMaxWidth(screenWidth - INTERVAL);
		int height1 = height - (height - screenHeight) / 2 - BUTTON_SPACING * 2;
		int height2 = (height - screenHeight) / 2 + textRenderer.fontHeight + ICON_SIZE + INTERVAL * 2 - messageText.getHeight();
		int height3 = height1 - height2;
		messageText.setPosition((width - messageText.getWidth()) / 2, height2 + height3 / 2);
		addDrawableChild(messageText);
		// 添加按钮
		addDrawableChild(updateButton = ButtonWidget.builder(Text.translatable(ScreenUtil.configText(Silk.DATA, KEY)), button -> {
			close();
			Util.getOperatingSystem().open("https://modrinth.com/mod/" + updateShow.getMod().getSlug() + "/version/" + updateShow.getUpdateThread().getUpdateModVer());
			MinecraftClient.getInstance().setScreen(new DownloadingScreen(parent, updateShow, title));
		}).dimensions(fullButtonX, buttonY - BUTTON_SPACING * 2, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(returnToGameButton().dimensions(halfButtonX, buttonY - BUTTON_SPACING * 2, halfButtonWidth, BUTTON_HEIGHT).build());
	}
	
	@Override
	public void tick() {
		super.tick();
		transColor = ScreenUtil.colorCycling(transColor);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (updateButton != null) updateButton.setMessage(updateButton.getMessage().copy().setStyle(Style.EMPTY.withBold(true).withColor(transColor)));
		super.render(context, mouseX, mouseY, delta);
	}
}
