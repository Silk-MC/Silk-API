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

package pers.saikel0rado1iu.silk.api.client.pattern.tab;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.tab.GridScreenTab;
import net.minecraft.client.gui.widget.ClickableWidget;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.LinkTrusted;
import pers.saikel0rado1iu.silk.api.client.pattern.screen.ModScreen;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">屏幕选项卡</h2>
 * 用于创建基本的屏幕选项卡
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class ScreenTab extends GridScreenTab implements LinkTrusted, ModPass {
	/**
	 * 选项卡顶部坐标
	 */
	public static final int TAP_TOP = 23;
	/**
	 * 选项卡底部坐标
	 */
	public static final int TAP_BOTTOM = 37;
	protected final ModPass modPass;
	protected final List<ClickableWidget> drawableWidgetList = new ArrayList<>(4);
	@SuppressWarnings("rawtypes")
	protected final List selectableWidgetList = new ArrayList<>(4);
	protected ModScreen parent;
	
	/**
	 * @param modPass 模组通
	 * @param key     选项卡唯一键
	 */
	public ScreenTab(ModPass modPass, String key) {
		super(WidgetTexts.text(modPass, "tab." + key));
		this.modPass = modPass;
	}
	
	/**
	 * 添加可绘制控件
	 *
	 * @param widget 可绘制控件
	 * @param <T>    可绘制控件类型
	 * @return 可绘制控件
	 */
	protected <T extends ClickableWidget> T addDrawableChild(T widget) {
		drawableWidgetList.add(widget);
		return widget;
	}
	
	/**
	 * 添加可选择控件
	 *
	 * @param widget 可选择控件
	 * @param <T>    可选择控件类型
	 * @return 可选择控件
	 */
	@SuppressWarnings({"unchecked", "UnusedReturnValue"})
	protected <T extends Element & Selectable> T addSelectableChild(T widget) {
		selectableWidgetList.add(widget);
		return widget;
	}
	
	/**
	 * 设置父屏幕
	 *
	 * @param parent 父屏幕
	 */
	public void setParent(ModScreen parent) {
		this.parent = parent;
	}
	
	/**
	 * 初始化方法
	 *
	 * @param client       客户端实例
	 * @param textRenderer 文本渲染器
	 * @param width        屏幕宽度
	 * @param height       屏幕高度
	 */
	public abstract void init(MinecraftClient client, TextRenderer textRenderer, int width, int height);
	
	/**
	 * 渲染方法
	 *
	 * @param client       客户端实例
	 * @param textRenderer 文本渲染器
	 * @param context      绘制上下文
	 * @param mouseX       指针坐标 X
	 * @param mouseY       指针坐标 Y
	 * @param delta        Δ
	 * @param width        屏幕宽度
	 * @param height       屏幕高度
	 */
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
	}
	
	/**
	 * 获取可绘制控件列表
	 *
	 * @return 可绘制控件列表
	 */
	public List<ClickableWidget> getDrawableWidgetList() {
		return drawableWidgetList;
	}
	
	/**
	 * 获取可选择控件列表
	 *
	 * @param <T> 可选择控件类型
	 * @return 可选择控件列表
	 */
	@SuppressWarnings("unchecked")
	public <T extends Element & Selectable> List<T> getSelectableWidgetList() {
		return selectableWidgetList;
	}
	
	@Override
	public ModData modData() {
		return modPass.modData();
	}
}
