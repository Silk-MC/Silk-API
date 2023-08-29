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

package pers.saikel0rado1iu.silk.util.screen.update;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.screen.widget.TextListWidget;
import pers.saikel0rado1iu.silk.util.update.UpdateData;

/**
 * <p><b style="color:FFC800"><font size="+1">模组日志展示界面</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ShowChangelogScreen extends UpdateScreen {
	protected final boolean canTrust;
	protected TextListWidget changelogWidget;
	
	/**
	 * 构造更新屏幕类
	 */
	public ShowChangelogScreen(Screen parent, UpdateData data, boolean canTrust) {
		super(parent, data, Text.translatable(ScreenUtil.widgetText(data.getMod(), "log"))
				.setStyle(Style.EMPTY.withBold(true).withColor(data.getMod().getThemeColor())));
		this.canTrust = canTrust;
	}
	
	/**
	 * 配置函数, 所以要用到的按钮等都需要在此函数中注册
	 */
	@Override
	protected void init() {
		// 赞助按钮、官网按钮、关闭更新按钮、关闭新MC更新按钮、立即更新按钮、暂时不用按钮
		super.init();
		// 添加提示消息文本
		int fullButtonWidth = screenWidth - 6;
		int buttonHeight = 20;
		int buttonSpacing = buttonHeight + 3;
		int fullButtonX = (width - (screenWidth - 6)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		addSelectableChild(changelogWidget = new TextListWidget(client,
				screenWidth - 6, screenHeight - 6,
				(height - screenHeight) / 2 + 20 + 72 + buttonSpacing * 2 - buttonHeight,
				buttonY - buttonSpacing - 3,
				12, ""));
		changelogWidget.setLeftPos(fullButtonX);
		// 添加按钮
		addDrawableChild(ScreenUtil.linkButton(parent, data.getMod(), ModBasicData.LinkType.SUPPORT, canTrust)
				.dimensions(fullButtonX, (height - screenHeight) / 2 + 20 + 72 + buttonSpacing - buttonHeight, halfButtonWidth, buttonHeight).build());
		addDrawableChild(ScreenUtil.linkButton(parent, data.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
				.dimensions(halfButtonX, (height - screenHeight) / 2 + 20 + 72 + buttonSpacing - buttonHeight, halfButtonWidth, buttonHeight).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("menu.returnToGame"), (button) -> {
					data.setCanCheckUpdate(true);
					close();
				})
				.dimensions(fullButtonX, buttonY - buttonSpacing, fullButtonWidth, buttonHeight).build());
	}
	
	/**
	 * 渲染提供函数, 所需要使用的所有特性都要在此函数中注册
	 */
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		changelogWidget.render(context, mouseX, mouseY, delta);
	}
}
