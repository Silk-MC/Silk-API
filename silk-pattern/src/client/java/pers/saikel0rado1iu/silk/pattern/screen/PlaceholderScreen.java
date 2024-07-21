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

package pers.saikel0rado1iu.silk.pattern.screen;

import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModPass;
import pers.saikel0rado1iu.silk.pattern.widget.ButtonHelper;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;

import java.util.Objects;

/**
 * <h2 style="color:FFC800">占位屏幕</h2>
 * 用于添加一个按钮的功能未添加时所用的占位屏幕
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class PlaceholderScreen extends BaseScreen implements LinkTrusted {
	protected final ModPass modPass;
	protected MultilineText messageText;
	
	/**
	 * @param parent  父屏幕
	 * @param modPass 模组通
	 */
	public PlaceholderScreen(@Nullable Screen parent, ModPass modPass) {
		super(parent, Text.of(""));
		this.modPass = modPass;
	}
	
	/**
	 * 获取文本键
	 *
	 * @param modPass 模组通
	 * @return 文本键
	 */
	public static String textKey(ModPass modPass) {
		return WidgetTexts.textKey(modPass, "placeholder");
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		messageText.drawCenterWithShadow(context, width / 2, height / 2 - 4 - 36, 8, Objects.requireNonNull(Formatting.GRAY.getColorValue()));
		super.render(context, mouseX, mouseY, delta);
	}
	
	@Override
	protected void init() {
		super.init();
		// 添加提示消息文本
		messageText = MultilineText.create(textRenderer, Text.translatable(textKey(modPass)), width - 100);
		int buttonWidth = 100;
		// 添加官网跳转按钮
		addDrawableChild(ButtonHelper.link(this, modPass, ModData.LinkType.HOMEPAGE, linkTrusted()).dimensions((int) (width / 2.0 - buttonWidth * 1.5), height / 2 + 12, buttonWidth - 4, 20).build());
		// 添加支持按钮
		addDrawableChild(ButtonHelper.link(this, modPass, ModData.LinkType.SUPPORT, linkTrusted()).dimensions((int) (width / 2.0 - buttonWidth * 0.5 + 2), height / 2 + 12, buttonWidth - 4, 20).build());
		// 添加返回按钮
		addDrawableChild(ButtonHelper.back(this).dimensions((int) (width / 2.0 + buttonWidth * 0.5 + 4), height / 2 + 12, buttonWidth - 4, 20).build());
	}
}
