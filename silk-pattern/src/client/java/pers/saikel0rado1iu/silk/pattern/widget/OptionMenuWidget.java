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

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

import static net.minecraft.client.gui.screen.Screen.OPTIONS_BACKGROUND_TEXTURE;
import static net.minecraft.client.gui.screen.world.CreateWorldScreen.LIGHT_DIRT_BACKGROUND_TEXTURE;
import static pers.saikel0rado1iu.silk.pattern.tab.ScreenTab.TAP_BOTTOM;

/**
 * <h2 style="color:FFC800">选项菜单控件</h2>
 * 用于自定义背景的选项列表控件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class OptionMenuWidget extends OptionListWidget implements CustomBackground {
	/**
	 * @param minecraftClient 客户端实例
	 * @param width           宽度
	 * @param top             顶部坐标
	 * @param bottom          底部坐标
	 * @param itemHeight      文本高度
	 */
	public OptionMenuWidget(MinecraftClient minecraftClient, int width, int top, int bottom, int itemHeight) {
		super(minecraftClient, width, bottom - top, top, itemHeight);
	}
	
	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		int scrollbarPositionX = getScrollbarPositionX();
		int posX = scrollbarPositionX + 6;
		RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
		RenderSystem.setShaderTexture(0, background().orElse(OPTIONS_BACKGROUND_TEXTURE));
		if (background().isPresent() && (background().get() == OPTIONS_BACKGROUND_TEXTURE || background().get() == LIGHT_DIRT_BACKGROUND_TEXTURE)) {
			context.drawTexture(background().get(), 0, 0, 0, 0, 0, width, height - TAP_BOTTOM, 32, 32);
		} else if (background().isPresent()) {
			context.drawTexture(background().get(), 0, 0, 0, 0, 0, width, height - TAP_BOTTOM, width, height);
		}
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int rowLeft = getRowLeft();
		int scrollAmount = getY() + 4 - (int) getScrollAmount();
		enableScissor(context);
		
		renderHeader(context, rowLeft, scrollAmount);
		
		renderList(context, mouseX, mouseY, delta);
		context.disableScissor();
		RenderSystem.setShaderTexture(0, LIGHT_DIRT_BACKGROUND_TEXTURE);
		context.drawTexture(LIGHT_DIRT_BACKGROUND_TEXTURE, getX(), 0, 0.0F, 0.0F, width, getY(), 32, 32);
		context.fillGradient(getX(), getY(), getRight(), getY() + 4, -16777216, 0);
		
		int maxScroll = getMaxScroll();
		if (maxScroll > 0) {
			int pos = (int) ((float) ((getBottom() - getY()) * (getBottom() - getY())) / (float) getMaxPosition());
			pos = MathHelper.clamp(pos, 32, getBottom() - getY() - 8);
			int o = Math.max(getY(), (int) getScrollAmount() * (getBottom() - getY() - pos) / maxScroll + getY());
			
			context.fill(scrollbarPositionX, getY(), posX, getBottom(), -16777216);
			context.fill(scrollbarPositionX, o, posX, o + pos, -8355712);
			context.fill(scrollbarPositionX, o, posX - 1, o + pos - 1, -4144960);
		}
		
		renderDecorations(context, mouseX, mouseY);
		RenderSystem.disableBlend();
	}
	
	@Override
	public Optional<Identifier> background() {
		return Optional.empty();
	}
}
