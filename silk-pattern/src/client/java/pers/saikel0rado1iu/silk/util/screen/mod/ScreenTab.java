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

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.screen.LinkTrusted;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">用于创建基本的屏幕选项卡</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class ScreenTab extends GridScreenTab implements LinkTrusted {
	@SilkApi
	public static final int TAP_TOP = 23;
	@SilkApi
	public static final int TAP_BOTTOM = 37;
	protected final ModBasicData mod;
	protected final List<ClickableWidget> drawableWidgetList = new ArrayList<>(4);
	protected final List<Object> selectableWidgetList = new ArrayList<>(4);
	protected GameOptionsScreen parent;
	protected Identifier background;
	
	@SilkApi
	public ScreenTab(ModBasicData mod, String key) {
		super(Text.translatable(TextUtil.widgetText(mod, "tab." + key)));
		this.mod = mod;
	}
	
	@SilkApi
	protected <T extends ClickableWidget> T addWidget(T widget) {
		drawableWidgetList.add(widget);
		return widget;
	}
	
	@SilkApi
	protected <T extends Element & Selectable> T addWidget(T widget) {
		selectableWidgetList.add(widget);
		return widget;
	}
	
	@SilkApi
	public ScreenTab setBackground(Identifier background) {
		this.background = background;
		return this;
	}
	
	@SilkApi
	public ScreenTab setParent(GameOptionsScreen parent) {
		this.parent = parent;
		return this;
	}
	
	@SilkApi
	public abstract void init(MinecraftClient client, TextRenderer textRenderer, int width, int height);
	
	@SilkApi
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
	}
	
	@SilkApi
	public List<ClickableWidget> getDrawableWidgetList() {
		return drawableWidgetList;
	}
	
	@SilkApi
	public List<Object> getSelectableWidgetList() {
		return selectableWidgetList;
	}
	
	@SilkApi
	public void close() {
		drawableWidgetList.forEach(widget -> widget.visible = false);
	}
	
	@SilkApi
	public void open() {
		drawableWidgetList.forEach(widget -> widget.visible = true);
	}
}
