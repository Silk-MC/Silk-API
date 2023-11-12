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

package pers.saikel0rado1iu.silk.util.screen.mod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

/**
 * <p><b style="color:FFC800"><font size="+1">用于创建一个统一原版风格的模组主页并提供简单更新按钮</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class CanUpdateModScreen extends ModScreen {
	protected final UpdateShow updateShow;
	protected PressableTextWidget modVersion;
	
	@SilkApi
	public CanUpdateModScreen(Screen parent, UpdateShow updateShow, ScreenTab tab, ScreenTab... tabs) {
		super(parent, tab, tabs);
		this.updateShow = updateShow;
	}
	
	@SilkApi
	public CanUpdateModScreen(Screen parent, UpdateShow updateShow, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, mainTabIndex, tab, tabs);
		this.updateShow = updateShow;
	}
	
	@SilkApi
	public CanUpdateModScreen(Screen parent, UpdateShow updateShow, Identifier background, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, background, mainTabIndex, tab, tabs);
		this.updateShow = updateShow;
	}
	
	@Override
	protected void init() {
		super.init();
		String modVerString = tabs.get(0).mod.getVersion();
		modVersion = new PressableTextWidget(0, height - 12, textRenderer.getWidth(modVerString), textRenderer.fontHeight,
				Text.of(modVerString), buttonWidget -> updateShow.showUpdate(this), textRenderer);
		addDrawableChild(modVersion);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (modVersion.isMouseOver(mouseX, mouseY)) {
			context.drawTextWithShadow(textRenderer, Text.translatable(TextUtil.widgetText(updateShow.getMod(), "check_update")),
					mouseX, mouseY - textRenderer.fontHeight * 2, 0xCCCCCC);
		}
	}
}
