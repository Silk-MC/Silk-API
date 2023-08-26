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
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组介绍选项卡</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class SynopsisTab extends ScreenTab {
	protected int interval = 6;
	protected int addInterval = 15;
	protected TextWidget introTitle;
	protected TextWidget visionTitle;
	protected MultilineTextWidget introText;
	protected MultilineTextWidget visionText;
	
	public SynopsisTab(ModBasicData mod) {
		super(mod, "synopsis");
	}
	
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		addWidget(introTitle = new TextWidget(Text.translatable(ScreenUtil.widgetTitle(mod, "tab.synopsis.intro")).formatted(Formatting.BOLD), textRenderer));
		addWidget(visionTitle = new TextWidget(Text.translatable(ScreenUtil.widgetTitle(mod, "tab.synopsis.vision")).formatted(Formatting.BOLD), textRenderer));
		addWidget(introText = new MultilineTextWidget(Text.translatable(ScreenUtil.widgetText(mod, "tab.synopsis.intro")), textRenderer));
		addWidget(visionText = new MultilineTextWidget(Text.translatable(ScreenUtil.widgetText(mod, "tab.synopsis.vision")), textRenderer));
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		introTitle.setPosition(interval, TAP_TOP + interval);
		visionTitle.setPosition(interval, height / 2);
		introText.setMaxWidth(width - (interval + addInterval) * 2).setMaxRows((height / 2 - (TAP_TOP + interval + introTitle.getHeight() + 5)) / textRenderer.fontHeight - 1)
				.setPosition(interval + addInterval, TAP_TOP + interval + introTitle.getHeight() + 5);
		visionText.setMaxWidth(width - (interval + addInterval) * 2).setMaxRows((height / 2 - (TAP_TOP + interval + introTitle.getHeight() + 5)) / textRenderer.fontHeight - 1)
				.setPosition(interval + addInterval, height / 2 + visionTitle.getHeight() + 5);
	}
}
