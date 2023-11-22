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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import static pers.saikel0rado1iu.silk.util.update.CheckUpdateThread.State.THIS_MC_VER;

/**
 * <p><b style="color:FFC800"><font size="+1">此 MC 版本模组更新提示界面</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ThisMcVerNotifyScreen extends UpdateScreen {
	private static final String KEY = THIS_MC_VER.toString().toLowerCase();
	protected final boolean canTrust;
	
	public ThisMcVerNotifyScreen(Screen parent, UpdateShow show, boolean canTrust) {
		super(parent, show, show.getTitle(KEY));
		this.canTrust = canTrust;
	}
	
	@Override
	protected void init() {
		super.init();
		if (updateShow.getUpdateThread().getUpdatingFail()) MinecraftClient.getInstance().setScreen(new UpdateFailWarningScreen(parent, updateShow, canTrust));
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0, updateShow.getVerText(KEY), textRenderer).setMaxWidth(screenWidth - INTERVAL);
		int height1 = height - (height - screenHeight) / 2 - BUTTON_SPACING * 4;
		int height2 = (height - screenHeight) / 2 + textRenderer.fontHeight + ICON_SIZE + INTERVAL * 2 - messageText.getHeight();
		int height3 = height1 - height2;
		messageText.setPosition((width - messageText.getWidth()) / 2, height2 + height3 / 2);
		addDrawableChild(messageText);
		// 按钮
		int fullButtonWidth = screenWidth - INTERVAL;
		int fullButtonX = (width - (screenWidth - INTERVAL)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		addDrawableChild(ScreenUtil.linkButton(this, updateShow.getMod(), ModBasicData.LinkType.SUPPORT, canTrust)
				.dimensions(fullButtonX, buttonY - BUTTON_SPACING * 4, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(ScreenUtil.linkButton(this, updateShow.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
				.dimensions(halfButtonX, buttonY - BUTTON_SPACING * 4, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(updateConfigButton().dimensions(fullButtonX, buttonY - BUTTON_SPACING * 3, fullButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(updateModeButton(fullButtonX, buttonY - BUTTON_SPACING * 2, fullButtonWidth, BUTTON_HEIGHT));
		ButtonWidget updateNowButton = updateNowButton();
		updateNowButton.setPosition(fullButtonX, buttonY - BUTTON_SPACING);
		updateNowButton.setWidth(halfButtonWidth);
		addDrawableChild(updateNowButton);
		addDrawableChild(notUpdateButton().dimensions(halfButtonX, buttonY - BUTTON_SPACING, halfButtonWidth, BUTTON_HEIGHT).build());
	}
}
