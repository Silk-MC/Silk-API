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

package pers.saikel0rado1iu.silk.util.screen.update;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.util.ScreenUtil;
import pers.saikel0rado1iu.silk.util.screen.BaseScreen;
import pers.saikel0rado1iu.silk.util.update.UpdateData;

import java.util.Objects;

/**
 * <p><b style="color:FFC800"><font size="+1">基础更新屏幕类，所有更新提示都在此基础上拓展</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public abstract class UpdateScreen extends BaseScreen {
	public final Text updateText;
	public final Text returnText;
	public final Text autoDownloadDoneText;
	public final Text autoUpdateDoneText;
	public final Text updatingFailText;
	protected final Text title;
	protected final UpdateData data;
	protected int screenWidth;
	protected int screenHeight;
	protected ButtonWidget updateButton;
	protected MultilineText messageText;
	
	protected UpdateScreen(Screen parent, UpdateData data, Text title) {
		super(parent, Text.of(""));
		this.data = data;
		this.title = title;
		updateText = Text.translatable(ScreenUtil.widgetText(data.getMod(), "update_now"));
		returnText = Text.translatable(ScreenUtil.widgetText(data.getMod(), "not_update"));
		autoDownloadDoneText = Text.translatable(ScreenUtil.widgetText(data.getMod(), "auto_download_done")).setStyle(Style.EMPTY.withBold(true));
		autoUpdateDoneText = Text.translatable(ScreenUtil.widgetText(data.getMod(), "auto_update_done")).setStyle(Style.EMPTY.withBold(true));
		updatingFailText = Text.translatable(ScreenUtil.widgetText(data.getMod(), "updating_fail")).setStyle(Style.EMPTY.withBold(true));
		data.setCanCheckUpdate(false);
		data.setShowScreen();
	}
	
	/**
	 * 提供函数, 注册背景屏幕大小标题与模组图标
	 */
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		context.drawTextWithShadow(textRenderer, title, (width - textRenderer.getWidth(title)) / 2, (height - screenHeight) / 2 + 5, 0xFFFFFF);
		RenderSystem.setShaderTexture(0, data.getMod().getIcon().orElse(Silk.DATA.getIcon().orElseThrow()));
		context.drawTexture(data.getMod().getIcon().orElse(Silk.DATA.getIcon().orElseThrow()), width / 2 - 36, (height - screenHeight) / 2 + 18, 0, 0.0F, 0.0F, 72, 72, 72, 72);
		super.render(context, mouseX, mouseY, delta);
	}
	
	protected void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		if (Objects.requireNonNull(client).world != null) context.fillGradient(0, 0, width, height, -1072689136, -804253680);
		renderBackgroundTexture(context, mouseX, mouseY, delta);
	}
	
	/**
	 * 渲染背景纹理
	 */
	protected void renderBackgroundTexture(DrawContext context, int mouseX, int mouseY, float delta) {
		ButtonWidget.builder(Text.of(""), (button) -> {
		}).dimensions((width - (screenWidth + 6)) / 2, (height - (screenHeight + 6)) / 2, screenWidth + 6, screenHeight + 6).build().render(context, mouseX, mouseY, delta);
		RenderSystem.setShaderTexture(0, OPTIONS_BACKGROUND_TEXTURE);
		RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
		context.drawTexture(OPTIONS_BACKGROUND_TEXTURE, (width - screenWidth) / 2, (height - screenHeight) / 2, 0, 0.0F, 0.0F, screenWidth, screenHeight, 32, 32);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	/**
	 * 配置函数, 设置屏幕大小参数
	 */
	@Override
	protected void init() {
		super.init();
		screenWidth = (int) (width / 2.5);
		screenHeight = (int) (height * 0.9);
	}
}
