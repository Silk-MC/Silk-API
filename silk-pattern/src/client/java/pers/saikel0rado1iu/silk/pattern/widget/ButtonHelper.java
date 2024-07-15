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

package pers.saikel0rado1iu.silk.pattern.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import pers.saikel0rado1iu.silk.impl.SilkPattern;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">按钮助手</h2>
 * 有关按钮创建的相关方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ButtonHelper {
	/**
	 * 返回按钮
	 *
	 * @param screen 添加的屏幕
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder back(Screen screen) {
		return ButtonWidget.builder(ScreenTexts.BACK, (button) -> screen.close());
	}
	
	/**
	 * 取消按钮
	 *
	 * @param screen 添加的屏幕
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder cancel(Screen screen) {
		return ButtonWidget.builder(ScreenTexts.CANCEL, (button) -> screen.close());
	}
	
	/**
	 * 完成按钮
	 *
	 * @param screen 添加的屏幕
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder done(Screen screen) {
		return ButtonWidget.builder(ScreenTexts.DONE, (button) -> screen.close());
	}
	
	/**
	 * 返回按钮
	 *
	 * @param screen 添加的屏幕
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder returnToGame(Screen screen) {
		return ButtonWidget.builder(Text.translatable("menu.returnToGame"), (button) -> screen.close());
	}
	
	/**
	 * 退出按钮
	 *
	 * @param client 客户端实例
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder quit(MinecraftClient client) {
		return ButtonWidget.builder(Text.translatable("menu.quit"), (button) -> client.scheduleStop());
	}
	
	/**
	 * 链接按钮
	 *
	 * @param parent 返回的父屏幕
	 * @param text   按钮文本
	 * @param url    链接
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder link(Screen parent, Text text, String url) {
		return link(parent, text, url, false);
	}
	
	/**
	 * 链接按钮
	 *
	 * @param parent   返回的父屏幕
	 * @param text     按钮文本
	 * @param url      链接
	 * @param canTrust 可以信任链接
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder link(Screen parent, Text text, String url, boolean canTrust) {
		return ButtonWidget.builder(text, button -> MinecraftClient.getInstance().setScreen(new ConfirmLinkScreen(confirmed -> {
			if (confirmed) Util.getOperatingSystem().open(url);
			MinecraftClient.getInstance().setScreen(parent);
		}, url, canTrust)));
	}
	
	/**
	 * 链接按钮
	 *
	 * @param parent   返回的父屏幕
	 * @param modPass  模组通
	 * @param linkType 链接类型
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder link(Screen parent, ModPass modPass, ModData.LinkType linkType) {
		return link(parent, modPass, linkType, false);
	}
	
	/**
	 * 链接按钮
	 *
	 * @param parent   返回的父屏幕
	 * @param modPass  模组通
	 * @param linkType 链接类型
	 * @param canTrust 可以信任链接
	 * @return 按钮组件构建器
	 */
	static ButtonWidget.Builder link(Screen parent, ModPass modPass, ModData.LinkType linkType, boolean canTrust) {
		Text text = WidgetTexts.text(modPass, linkType.toString().toLowerCase());
		return modPass.modData().link(linkType).isPresent() ? link(parent, text, modPass.modData().link(linkType).get().toString(), canTrust) : link(parent, text, SilkPattern.getInstance().link(linkType).orElseThrow().toString(), true);
	}
}
