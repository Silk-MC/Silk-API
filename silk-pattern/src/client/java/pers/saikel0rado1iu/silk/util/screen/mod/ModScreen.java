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
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TabNavigationWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.ScreenUtil;

import java.util.ArrayList;

import static com.mojang.blaze3d.systems.RenderSystem.setShaderColor;
import static com.mojang.blaze3d.systems.RenderSystem.setShaderTexture;

/**
 * <h2 style="color:FFC800">用于创建一个统一原版风格的模组主页</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public class ModScreen extends GameOptionsScreen {
	protected final ImmutableList<ScreenTab> tabs;
	private final TabManager tabManager = new TabManager(this::addDrawableChild, this::remove);
	private final int mainTabIndex;
	private final Identifier background;
	private GridWidget grid;
	private TabNavigationWidget tabNavigation;
	private TextWidget verWidget;
	private TextWidget licenseWidget;
	private int tempIndex = -1;
	private int tempSize = -1;
	
	@SilkApi
	public ModScreen(Screen parent, ScreenTab tab, ScreenTab... tabs) {
		this(parent, 0, tab, tabs);
	}
	
	@SilkApi
	public ModScreen(Screen parent, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		this(parent, null, mainTabIndex, tab, tabs);
	}
	
	@SilkApi
	public ModScreen(Screen parent, @Nullable Identifier background, int mainTabIndex, ScreenTab tab, ScreenTab... tabs) {
		super(parent, null, Text.of(""));
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
		super.render(context, mouseX, mouseY, delta);
		for (int count = 0; count < tabs.size(); count++) {
			if (tabNavigation.getFocused() == null) {
				if (count == mainTabIndex) {
					if (tempIndex != count || tempSize != width * height) tabWidgetReset(tabs.get(count));
					tabs.get(count).render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
					tempIndex = count;
					tempSize = width * height;
				} else {
					tabs.get(count).drawableWidgetList.forEach(this::remove);
					tabs.get(count).selectableWidgetList.forEach(object -> remove((Element & Selectable) object));
				}
			} else {
				if (tabNavigation.children().get(count).equals(tabNavigation.getFocused())) {
					if (tempIndex != count || tempSize != width * height) tabWidgetReset(tabs.get(count));
					tabs.get(count).render(client, textRenderer, context, mouseX, mouseY, delta, width, height);
					tempIndex = count;
					tempSize = width * height;
				} else {
					tabs.get(count).drawableWidgetList.forEach(this::remove);
					tabs.get(count).selectableWidgetList.forEach(object -> remove((Element & Selectable) object));
				}
			}
		}
		tabNavigation.render(context, mouseX, mouseY, delta);
		String modLicense = new ArrayList<>(tabs.get(0).mod.getLicenses()).isEmpty() ? "ARR" : new ArrayList<>(tabs.get(0).mod.getLicenses()).get(0);
		verWidget.setPosition(0, height - 12);
		licenseWidget.setPosition(width - textRenderer.getWidth(modLicense), height - 12);
	}
	
	@Override
	protected void renderPanoramaBackground(DrawContext context, float delta) {
		if (null == background) {
			super.renderPanoramaBackground(context, delta);
		} else {
			setShaderTexture(0, background);
			context.drawTexture(background, 0, 0, 0, 0.0F, 0.0F, width, height, width, height);
			setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
	
	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		super.renderBackground(context, mouseX, mouseY, delta);
		RenderSystem.enableBlend();
		context.drawTexture(FOOTER_SEPARATOR_TEXTURE, 0, height - layout.getFooterHeight() - 2, 0, 0, width, 2, 32, 2);
		RenderSystem.disableBlend();
		if (null == background) {
			super.renderPanoramaBackground(context, delta);
		} else {
			setShaderTexture(0, background);
			setShaderColor(0.5F, 0.5F, 0.5F, 1.0F);
			context.drawTexture(background, 0, 0, 0, 0, 0, width, height - layout.getFooterHeight() - 2, width, height);
			setShaderColor(1, 1, 1, 1);
		}
	}
	
	@Override
	protected void init() {
		tabs.forEach(tab -> {
			tab.setParent(this);
			tab.drawableWidgetList.forEach(this::addDrawableChild);
		});
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
		String modVerString = tabs.get(0).mod.getVersion();
		addDrawableChild(verWidget = new TextWidget(0, height - 12, textRenderer.getWidth(modVerString), textRenderer.fontHeight, Text.of(modVerString), textRenderer));
		String modLicense = new ArrayList<>(tabs.get(0).mod.getLicenses()).isEmpty() ? "ARR" : new ArrayList<>(tabs.get(0).mod.getLicenses()).get(0);
		addDrawableChild(licenseWidget = new TextWidget(width - textRenderer.getWidth(modLicense), height - 12, textRenderer.getWidth(modLicense), textRenderer.fontHeight, Text.of(modLicense), textRenderer));
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
	
	@Override
	public void close() {
		if (client == null) return;
		client.setScreen(parent);
	}
}
