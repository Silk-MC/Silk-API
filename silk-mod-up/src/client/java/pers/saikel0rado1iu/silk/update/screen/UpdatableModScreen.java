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

package pers.saikel0rado1iu.silk.update.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.pattern.screen.ModScreen;
import pers.saikel0rado1iu.silk.pattern.tab.ScreenTab;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.update.ClientUpdateManager;

/**
 * <h2 style="color:FFC800">可更新模组屏幕</h2>
 * 用于创建一个统一原版风格的模组主页，但是此模组屏幕包含一个可检查更新的按钮
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class UpdatableModScreen extends ModScreen {
	protected final ClientUpdateManager manager;
	protected PressableTextWidget modVersion;
	
	/**
	 * @param parent  父屏幕
	 * @param manager 更新数据
	 * @param tab     选项卡
	 * @param tabs    后续选项卡
	 */
	public UpdatableModScreen(@Nullable Screen parent, ClientUpdateManager manager, ScreenTab tab, ScreenTab... tabs) {
		super(parent, tab, tabs);
		this.manager = manager;
	}
	
	/**
	 * @param parent       父屏幕
	 * @param manager      更新数据
	 * @param mainTabIndex 主选项卡索引
	 * @param tab          选项卡
	 * @param tabs         后续选项卡
	 */
	public UpdatableModScreen(@Nullable Screen parent, ClientUpdateManager manager, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, mainTabIndex, tab, tabs);
		this.manager = manager;
	}
	
	/**
	 * @param parent       父屏幕
	 * @param manager      更新数据
	 * @param background   屏幕背景
	 * @param mainTabIndex 主选项卡索引
	 * @param tab          选项卡
	 * @param tabs         后续选项卡
	 */
	public UpdatableModScreen(@Nullable Screen parent, ClientUpdateManager manager, Identifier background, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, background, mainTabIndex, tab, tabs);
		this.manager = manager;
	}
	
	@Override
	protected void init() {
		super.init();
		String modVerString = tabs.get(0).getModPass().modData().version();
		modVersion = new PressableTextWidget(0, height - 12, textRenderer.getWidth(modVerString), textRenderer.fontHeight,
				Text.of(modVerString), buttonWidget -> manager.setShowNow(), textRenderer);
		addDrawableChild(modVersion);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		modVersion.setPosition(0, height - 12);
		if (modVersion.isMouseOver(mouseX, mouseY)) {
			context.drawTextWithShadow(textRenderer, WidgetTexts.text(SilkModUp.getInstance(), "check"), mouseX, mouseY - textRenderer.fontHeight * 2, 0xCCCCCC);
		}
	}
}
