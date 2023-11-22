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
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateData;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

/**
 * <p><b style="color:FFC800"><font size="+1">下载中屏幕</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class DownloadingScreen extends UpdateScreen {
	protected MultilineTextWidget messageText;
	
	protected DownloadingScreen(Screen parent, UpdateShow updateShow, Text title) {
		super(parent, updateShow, title);
	}
	
	@Override
	protected void init() {
		super.init();
		int fullButtonWidth = screenWidth - INTERVAL;
		int fullButtonX = (width - (screenWidth - INTERVAL)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		// 添加信息
		messageText = new MultilineTextWidget(0, 0, Text.of(""), textRenderer).setMaxWidth(screenWidth - INTERVAL).setCentered(true);
		int height1 = height - (height - screenHeight) / 2 - BUTTON_SPACING;
		int height2 = (height - screenHeight) / 2 + textRenderer.fontHeight + ICON_SIZE + INTERVAL * 2 - messageText.getHeight();
		int height3 = height1 - height2;
		messageText.setPosition((width - messageText.getWidth()) / 2, height2 + height3 / 2);
		addDrawableChild(messageText);
		addDrawableChild(returnToGameButton().dimensions(fullButtonX, buttonY - BUTTON_SPACING, fullButtonWidth, BUTTON_HEIGHT).build());
		updateShow.updateMod();
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		messageText.setMessage(Text.translatable(TextUtil.widgetText(Silk.DATA, "downloading"), updateShow.getMod().getLocalizedName(), updateShow.getUpdateProgress()));
		messageText.setPosition((width - messageText.getWidth()) / 2, messageText.getY());
	}
	
	@Override
	public void tick() {
		super.tick();
		if (Double.parseDouble(updateShow.getUpdateProgress()) == 100) {
			if (updateShow.getUpdateData().getUpdateMode() == UpdateData.Mode.AUTO_DOWNLOAD)
				MinecraftClient.getInstance().setScreen(new DownloadedScreen(parent, updateShow, title));
			else MinecraftClient.getInstance().setScreen(new UpdatedScreen(parent, updateShow, title));
		}
	}
}
