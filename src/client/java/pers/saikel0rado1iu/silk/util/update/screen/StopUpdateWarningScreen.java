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
import net.minecraft.client.gui.widget.MultilineTextWidget;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import static pers.saikel0rado1iu.silk.util.update.CheckUpdateThread.State.STOP_UPDATE;

/**
 * <h2 style="color:FFC800">停止更新警告界面</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class StopUpdateWarningScreen extends UpdateScreen {
	private static final String KEY = STOP_UPDATE.toString().toLowerCase();
	protected final boolean canTrust;
	
	public StopUpdateWarningScreen(Screen parent, UpdateShow show, boolean canTrust) {
		super(parent, show, show.getTitle(KEY));
		this.canTrust = canTrust;
	}
	
	@Override
	protected void init() {
		// 赞助按钮、官网按钮、关闭更新按钮、关闭新MC更新按钮、立即更新按钮、暂时不用按钮
		super.init();
		// 添加提示消息文本
		int fullButtonWidth = screenWidth - INTERVAL;
		int buttonSpacing = BUTTON_HEIGHT + INTERVAL / 2;
		int fullButtonX = (width - (screenWidth - INTERVAL)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		// 添加按钮
		addDrawableChild(ScreenUtil.linkButton(this, updateShow.getMod(), ModBasicData.LinkType.HOMEPAGE, canTrust)
				.dimensions(fullButtonX, (height - screenHeight) / 2 + BUTTON_HEIGHT + ICON_SIZE + buttonSpacing - BUTTON_HEIGHT, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(ScreenUtil.linkButton(this, updateShow.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
				.dimensions(halfButtonX, (height - screenHeight) / 2 + BUTTON_HEIGHT + ICON_SIZE + buttonSpacing - BUTTON_HEIGHT, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(returnToGameButton().dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, BUTTON_HEIGHT).build());
		// 添加信息
		MultilineTextWidget messageText = new MultilineTextWidget(0, 0, updateShow.getWarText(KEY), textRenderer)
				.setMaxWidth(screenWidth - INTERVAL);
		int height1 = height - (height - screenHeight) / 2 - BUTTON_SPACING;
		int height2 = (height - screenHeight) / 2 + textRenderer.fontHeight + ICON_SIZE + INTERVAL * 2 + BUTTON_SPACING - messageText.getHeight();
		int height3 = height1 - height2;
		messageText.setPosition((width - messageText.getWidth()) / 2, height2 + height3 / 2);
		addDrawableChild(messageText);
	}
}
