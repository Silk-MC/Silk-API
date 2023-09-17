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

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.screen.widget.TextListWidget;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import static pers.saikel0rado1iu.silk.util.update.CheckUpdateThread.State.MOD_LOG;

/**
 * <p><b style="color:FFC800"><font size="+1">模组日志展示界面</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ShowChangelogScreen extends UpdateScreen {
	private static final String KEY = MOD_LOG.toString().toLowerCase();
	protected final boolean canTrust;
	protected TextListWidget changelogWidget;
	
	public ShowChangelogScreen(Screen parent, UpdateShow show, boolean canTrust) {
		super(parent, show, show.getTitle(KEY));
		this.canTrust = canTrust;
		this.updateShow.getUpdateData().setCanShowChangelog(false);
		this.updateShow.getUpdateData().save();
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
		addSelectableChild(changelogWidget = new TextListWidget(client,
				screenWidth - INTERVAL, screenHeight - INTERVAL,
				(height - screenHeight) / 2 + BUTTON_HEIGHT + ICON_SIZE + buttonSpacing * 2 - BUTTON_HEIGHT,
				buttonY - buttonSpacing - INTERVAL / 2,
				textRenderer.fontHeight, ScreenUtil.readChangelog(updateShow.getMod())));
		changelogWidget.setLeftPos(fullButtonX);
		// 添加按钮
		addDrawableChild(ScreenUtil.linkButton(this, updateShow.getMod(), ModBasicData.LinkType.SUPPORT, canTrust)
				.dimensions(fullButtonX, (height - screenHeight) / 2 + BUTTON_HEIGHT + ICON_SIZE + buttonSpacing - BUTTON_HEIGHT, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(ScreenUtil.linkButton(this, updateShow.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
				.dimensions(halfButtonX, (height - screenHeight) / 2 + BUTTON_HEIGHT + ICON_SIZE + buttonSpacing - BUTTON_HEIGHT, halfButtonWidth, BUTTON_HEIGHT).build());
		addDrawableChild(returnToGameButton().dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, BUTTON_HEIGHT).build());
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		changelogWidget.render(context, mouseX, mouseY, delta);
	}
}
