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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.screen.BaseScreen;

import static com.mojang.blaze3d.systems.RenderSystem.setShaderColor;
import static com.mojang.blaze3d.systems.RenderSystem.setShaderTexture;
import static net.minecraft.client.gui.screen.world.CreateWorldScreen.FOOTER_SEPARATOR_TEXTURE;
import static net.minecraft.client.gui.screen.world.CreateWorldScreen.LIGHT_DIRT_BACKGROUND_TEXTURE;

/**
 * <p><b style="color:FFC800"><font size="+1">用于创建一个统一原版风格的模组主页</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class ModScreen extends BaseScreen {
	private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
	private final ImmutableList<ScreenTab> tabs;
	private final int mainTabIndex;
	private final Identifier background;
	private GridWidget grid;
	private TabNavigationWidget tabNavigation;
	private int tempIndex = -1;
	
	@SilkApi
	public ModScreen(Screen parent, ScreenTab tab, ScreenTab... tabs) {
		this(parent, 0, tab, tabs);
	}
	
	@SilkApi
	public ModScreen(Screen parent, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		this(parent, LIGHT_DIRT_BACKGROUND_TEXTURE, mainTabIndex, tab, tabs);
	}
	
	@SilkApi
	public ModScreen(Screen parent, Identifier background, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, Text.of(""));
		this.background = background;
		this.mainTabIndex = mainTabIndex;
		this.tabs = ImmutableList.copyOf(Lists.asList(tab, tabs));
	}
	
	protected void tabWidgetReset(ScreenTab tab) {
		tab.drawableWidgetList.forEach(this::remove);
		tab.selectableWidgetList.forEach(object -> remove((Element & Selectable) object));
		tab.drawableWidgetList.clear();
		tab.selectableWidgetList.clear();
		tab.init(client, textRenderer, width, height);
		tab.drawableWidgetList.forEach(this::addDrawableChild);
		tab.selectableWidgetList.forEach(object -> addSelectableChild((Element & Selectable) object));
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		context.drawTexture(FOOTER_SEPARATOR_TEXTURE, 0, MathHelper.roundUpToMultiple(height - 36 - 2, 2), 0, 0, width, 2, 32, 2);
		for (int count = 0; count < tabs.size(); count++) {
			if (tabNavigation.getFocused() == null) {
				if (count == mainTabIndex) {
					if (tempIndex != count) tabWidgetReset(tabs.get(count));
					tabs.get(count).render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
					tempIndex = count;
				} else {
					tabs.get(count).drawableWidgetList.forEach(this::remove);
					tabs.get(count).selectableWidgetList.forEach(object -> remove((Element & Selectable) object));
				}
			} else {
				if (tabNavigation.children().get(count).equals(tabNavigation.getFocused())) {
					if (tempIndex != count) tabWidgetReset(tabs.get(count));
					tabs.get(count).render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
					tempIndex = count;
				} else {
					tabs.get(count).drawableWidgetList.forEach(this::remove);
					tabs.get(count).selectableWidgetList.forEach(object -> remove((Element & Selectable) object));
				}
			}
		}
		super.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	public void renderBackgroundTexture(DrawContext context) {
		if (background != OPTIONS_BACKGROUND_TEXTURE && background != LIGHT_DIRT_BACKGROUND_TEXTURE) {
			setShaderTexture(0, background);
			setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
			context.drawTexture(background, 0, 0, 0, 0.0F, 0.0F, width, height, width, height);
			setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			setShaderTexture(0, LIGHT_DIRT_BACKGROUND_TEXTURE);
			context.drawTexture(LIGHT_DIRT_BACKGROUND_TEXTURE, 0, height - 36, 0, 0, 0, width, height, 32, 32);
		} else {
			context.drawTexture(LIGHT_DIRT_BACKGROUND_TEXTURE, 0, 0, 0, 0, 0, width, height, 32, 32);
		}
	}
	
	@Override
	protected void init() {
		tabs.forEach(tab -> tab.setParent(this).setBackground(background).init(client, textRenderer, width, height));
		tabNavigation = TabNavigationWidget.builder(tabManager, width).tabs(tabs.toArray(new Tab[0])).build();
		addDrawableChild(tabNavigation);
		grid = new GridWidget().setColumnSpacing(10);
		GridWidget.Adder adder = grid.createAdder(1);
		adder.add(ScreenUtil.doneButton(this).build());
		grid.forEachChild(child -> {
			child.setNavigationOrder(1);
			addDrawableChild(child);
		});
		tabNavigation.selectTab(mainTabIndex, false);
		initTabNavigation();
	}
	
	@Override
	protected void initTabNavigation() {
		if (tabNavigation == null || grid == null) return;
		tabNavigation.setWidth(width);
		tabNavigation.init();
		grid.refreshPositions();
		SimplePositioningWidget.setPos(grid, 0, height - 36, width, 36);
		int otherAxis = tabNavigation.getNavigationFocus().getBottom();
		ScreenRect screenRect = new ScreenRect(0, otherAxis, width, grid.getY() - otherAxis);
		tabManager.setTabArea(screenRect);
	}
}
