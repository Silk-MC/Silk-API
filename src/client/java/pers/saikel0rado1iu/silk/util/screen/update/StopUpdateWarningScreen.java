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

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateData;

/**
 * <p><b style="color:FFC800"><font size="+1">停止更新警告界面</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class StopUpdateWarningScreen extends UpdateScreen {
	protected final boolean canTrust;
	
	/**
	 * 构造更新屏幕类
	 */
	public StopUpdateWarningScreen(Screen parent, UpdateData data, boolean canTrust) {
		super(parent, data, Text.translatable("title.spontaneous_replace.stop_update_warning")
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
		messageText = MultilineText.create(textRenderer, Text.translatable("text.spontaneous_replace.stop_update_warning")
				.setStyle(Style.EMPTY.withBold(true).withColor(Formatting.RED)), screenWidth - 6);
		int fullButtonWidth = screenWidth - 6;
		int buttonHeight = 20;
		int buttonSpacing = buttonHeight + 3;
		int fullButtonX = (width - (screenWidth - 6)) / 2;
		int buttonY = (height - (height - screenHeight) / 2);
		int halfButtonWidth = fullButtonWidth / 2 - 1;
		int halfButtonX = fullButtonX + halfButtonWidth + 2;
		// 添加按钮
		addDrawableChild(ScreenUtil.linkButton(parent, data.getMod(), ModBasicData.LinkType.HOMEPAGE, canTrust)
				.dimensions(fullButtonX, (height - screenHeight) / 2 + 20 + 72 + buttonSpacing - buttonHeight, halfButtonWidth, buttonHeight).build());
		addDrawableChild(ScreenUtil.linkButton(parent, data.getMod(), ModBasicData.LinkType.COMMUNITY, canTrust)
				.dimensions(halfButtonX, (height - screenHeight) / 2 + 20 + 72 + buttonSpacing - buttonHeight, halfButtonWidth, buttonHeight).build());
		addDrawableChild(ButtonWidget.builder(Text.translatable("menu.returnToGame"), (button) -> {
					data.resetCanCheckUpdate();
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
		messageText.drawCenterWithShadow(context,
				width / 2,
				(height - screenHeight) / 2 + 20 + 72 + (height - (height - screenHeight + 20 + 72)) / 2 - 6);
	}
}
