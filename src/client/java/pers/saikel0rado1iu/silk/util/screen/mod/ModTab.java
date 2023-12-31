/*
 * This file is part of Silk API.
 * Copyright (C) buttonHeight23 Saikel Orado Liu
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
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.screen.widget.TextListWidget;

/**
 * <h2 style="color:FFC800">用于模组主页选项卡</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88interval31138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class ModTab extends ScreenTab {
	protected static final int LOGO_SIDE = 66;
	protected static final int INTERVAL = 8;
	protected TextListWidget changelogWidget;
	protected TextListWidget targetWidget;
	protected TextWidget logTitle;
	protected TextWidget targetTitle;
	
	public ModTab(ModBasicData mod) {
		super(mod, "mod");
	}
	
	@Override
	public void init(MinecraftClient client, TextRenderer textRenderer, int width, int height) {
		int iconAdd = 20;
		int buttonX = width / 30 + LOGO_SIDE + iconAdd;
		int buttonHeight = 20;
		addWidget(ButtonWidget.builder(Text.of(""), button -> {
		}).dimensions(width / 60 + iconAdd, TAP_TOP + INTERVAL, LOGO_SIDE, LOGO_SIDE).build());
		IconWidget iconWidget;
		addWidget(iconWidget = IconWidget.create(LOGO_SIDE - 2, LOGO_SIDE - 2, mod.getIcon().orElse(Silk.DATA.getIcon().orElseThrow()), LOGO_SIDE - 2, LOGO_SIDE - 2));
		iconWidget.setPosition(width / 60 + iconAdd + 1, TAP_TOP + INTERVAL + 1);
		addWidget(ScreenUtil.linkButton(parent, mod, ModBasicData.LinkType.HOMEPAGE, linkTrusted()).dimensions(buttonX, TAP_TOP + INTERVAL, width / 2 - width / 60 - buttonX - iconAdd, buttonHeight).build());
		addWidget(ScreenUtil.linkButton(parent, mod, ModBasicData.LinkType.SUPPORT, linkTrusted()).dimensions(buttonX, ((TAP_TOP + INTERVAL) + (TAP_TOP + INTERVAL + LOGO_SIDE - buttonHeight)) / 2, width / 2 - width / 60 - buttonX - iconAdd, buttonHeight).build());
		addWidget(ScreenUtil.linkButton(parent, mod, ModBasicData.LinkType.COMMUNITY, linkTrusted()).dimensions(buttonX, TAP_TOP + INTERVAL + LOGO_SIDE - buttonHeight, width / 2 - width / 60 - buttonX - iconAdd, buttonHeight).build());
		addWidget(logTitle = new TextWidget(Text.translatable(TextUtil.widgetTitle(mod, "changelog")), textRenderer));
		addWidget(targetTitle = new TextWidget(Text.translatable(TextUtil.widgetTitle(mod, "target")), textRenderer));
		addWidget(changelogWidget = new TextListWidget(client, width / 2 - width / 30, TAP_TOP + INTERVAL * 3, height - TAP_BOTTOM - INTERVAL, 12, ScreenUtil.readChangelog(mod)));
		addWidget(targetWidget = new TextListWidget(client, width / 2 - width / 30, TAP_TOP + INTERVAL * 4 + LOGO_SIDE, height - TAP_BOTTOM - INTERVAL, 12, Text.translatable(TextUtil.widgetText(mod, "target")).getString()));
		changelogWidget.setX(width / 2 + width / 60);
		targetWidget.setX(width / 60);
	}
	
	@Override
	public void render(MinecraftClient client, TextRenderer textRenderer, DrawContext context, int mouseX, int mouseY, float delta, int width, int height) {
		logTitle.setPosition(width - width / 4 - logTitle.getWidth() / 2, TAP_TOP + INTERVAL);
		targetTitle.setPosition(width / 4 - targetTitle.getWidth() / 2, TAP_TOP + INTERVAL + LOGO_SIDE + INTERVAL);
		changelogWidget.render(context, mouseX, mouseY, delta);
		targetWidget.render(context, mouseX, mouseY, delta);
	}
}
