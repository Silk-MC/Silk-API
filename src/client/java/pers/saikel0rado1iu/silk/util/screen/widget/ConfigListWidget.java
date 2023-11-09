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

package pers.saikel0rado1iu.silk.util.screen.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import static com.mojang.blaze3d.systems.RenderSystem.disableScissor;
import static net.minecraft.client.gui.screen.Screen.OPTIONS_BACKGROUND_TEXTURE;
import static net.minecraft.client.gui.screen.world.CreateWorldScreen.LIGHT_DIRT_BACKGROUND_TEXTURE;
import static pers.saikel0rado1iu.silk.util.screen.mod.ScreenTab.TAP_BOTTOM;

/**
 * <p><b style="color:FFC800"><font size="+1">用于自定义背景的列表控件</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class ConfigListWidget extends OptionListWidget implements CustomBackground {
	protected Identifier background;
	
	public ConfigListWidget(MinecraftClient minecraftClient, int width, int height, int top, int bottom, int itemHeight) {
		super(minecraftClient, width, height, top, bottom, itemHeight);
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		int scrollbarPositionX = getScrollbarPositionX();
		int posX = scrollbarPositionX + 6;
		RenderSystem.setShaderColor(0.25F, 0.25F, 0.25F, 1.0F);
		RenderSystem.setShaderTexture(0, getBackground() == null ? OPTIONS_BACKGROUND_TEXTURE : getBackground());
		if (getBackground() == OPTIONS_BACKGROUND_TEXTURE || getBackground() == LIGHT_DIRT_BACKGROUND_TEXTURE)
			context.drawTexture(getBackground(), 0, 0, 0, 0, 0, width, height - TAP_BOTTOM, 32, 32);
		else if (getBackground() != null) context.drawTexture(getBackground(), 0, 0, 0, 0, 0, width, height - TAP_BOTTOM, width, height);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		
		int rowLeft = getRowLeft();
		int scrollAmount = top + 4 - (int) getScrollAmount();
		enableScissor(context);
		
		renderHeader(context, rowLeft, scrollAmount);
		
		renderList(context, mouseX, mouseY, delta);
		disableScissor();
		RenderSystem.setShaderTexture(0, LIGHT_DIRT_BACKGROUND_TEXTURE);
		context.drawTexture(LIGHT_DIRT_BACKGROUND_TEXTURE, left, 0, 0.0F, 0.0F, width, top, 32, 32);
		context.fillGradient(left, top, right, top + 4, -16777216, 0);
		
		int maxScroll = getMaxScroll();
		if (maxScroll > 0) {
			int pos = (int) ((float) ((bottom - top) * (bottom - top)) / (float) getMaxPosition());
			pos = MathHelper.clamp(pos, 32, bottom - top - 8);
			int o = Math.max(top, (int) getScrollAmount() * (bottom - top - pos) / maxScroll + top);
			
			context.fill(scrollbarPositionX, top, posX, bottom, -16777216);
			context.fill(scrollbarPositionX, o, posX, o + pos, -8355712);
			context.fill(scrollbarPositionX, o, posX - 1, o + pos - 1, -4144960);
		}
		
		renderDecorations(context, mouseX, mouseY);
		RenderSystem.disableBlend();
	}
	
	@Override
	public @Nullable Identifier getBackground() {
		return background;
	}
	
	@Override
	public void setBackground(Identifier background) {
		this.background = background;
	}
}
