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
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.util.Formatting;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;

/**
 * <h2 style="color:FFC800">概要选项卡</h2>
 * 用于模组介绍选项卡
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class SynopsisTab extends ScreenTab {
	protected static final int INTERVAL = 6;
	protected static final int ADD_INTERVAL = 15;
	protected TextWidget introTitle;
	protected TextWidget visionTitle;
	protected MultilineTextWidget introText;
	protected MultilineTextWidget visionText;
	
	/**
	 * @param modPass 模组通
	 */
	public SynopsisTab(ModPass modPass) {
		super(modPass, "synopsis");
	}
	
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		introTitle = addDrawableChild(new TextWidget(WidgetTexts.title(modPass, "tab.synopsis.intro").formatted(Formatting.BOLD), textRenderer));
		visionTitle = addDrawableChild(new TextWidget(WidgetTexts.title(modPass, "tab.synopsis.vision").formatted(Formatting.BOLD), textRenderer));
		introText = addDrawableChild(new MultilineTextWidget(WidgetTexts.text(modPass, "tab.synopsis.intro"), textRenderer));
		visionText = addDrawableChild(new MultilineTextWidget(WidgetTexts.text(modPass, "tab.synopsis.vision"), textRenderer));
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		introTitle.setPosition(INTERVAL, TAP_TOP + INTERVAL);
		visionTitle.setPosition(INTERVAL, height / 2);
		introText.setMaxWidth(width - (INTERVAL + ADD_INTERVAL) * 2).setMaxRows((height / 2 - (TAP_TOP + INTERVAL + introTitle.getHeight() + 5)) / textRenderer.fontHeight - 1)
				.setPosition(INTERVAL + ADD_INTERVAL, TAP_TOP + INTERVAL + introTitle.getHeight() + 5);
		visionText.setMaxWidth(width - (INTERVAL + ADD_INTERVAL) * 2).setMaxRows((height / 2 - (TAP_TOP + INTERVAL + introTitle.getHeight() + 5)) / textRenderer.fontHeight - 1)
				.setPosition(INTERVAL + ADD_INTERVAL, height / 2 + visionTitle.getHeight() + 5);
	}
}
