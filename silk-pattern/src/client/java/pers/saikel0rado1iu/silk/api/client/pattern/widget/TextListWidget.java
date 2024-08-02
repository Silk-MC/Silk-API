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

package pers.saikel0rado1iu.silk.api.client.pattern.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.*;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static net.minecraft.client.gui.screen.Screen.OPTIONS_BACKGROUND_TEXTURE;
import static net.minecraft.client.gui.screen.world.CreateWorldScreen.LIGHT_DIRT_BACKGROUND_TEXTURE;

/**
 * <h2 style="color:FFC800">文本列表控件</h2>
 * This code is partially referenced from <a href="https://github.com/TerraformersMC/ModMenu">ModMenu(github)</a>, Attached here is its open source license.
 * MIT License
 * <p>
 * Copyright (c) 2018-2020 Prospector
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class TextListWidget extends EntryListWidget<TextListWidget.TextEntry> implements CustomBackground {
	protected final String text;
	protected final TextRenderer textRenderer;
	
	/**
	 * @param client      客户端实例
	 * @param width       宽度
	 * @param top         顶部坐标
	 * @param bottom      底部坐标
	 * @param entryHeight 条目高度
	 * @param text        文本列表的文本
	 */
	public TextListWidget(MinecraftClient client, int width, int top, int bottom, int entryHeight, String text) {
		super(client, width, bottom - top, top, entryHeight);
		this.text = text;
		textRenderer = client.textRenderer;
	}
	
	@Override
	public TextEntry getSelectedOrNull() {
		return null;
	}
	
	@Override
	public int getRowWidth() {
		return width - 10;
	}
	
	@Override
	protected int getScrollbarPositionX() {
		return width - 6 + getX();
	}
	
	@Override
	public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		clearEntries();
		if (!text.isEmpty()) {
			int interval = 6;
			for (OrderedText line : textRenderer.wrapLines(Text.literal(text), getRowWidth() - interval))
				children().add(new TextEntry(line, this));
		}
		
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.setShaderTexture(0, background().orElse(OPTIONS_BACKGROUND_TEXTURE));
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		if (background().isPresent() && (background().get() == OPTIONS_BACKGROUND_TEXTURE || background().get() == LIGHT_DIRT_BACKGROUND_TEXTURE)) {
			bufferBuilder.vertex(getX(), getBottom(), 0).texture(getX() / 32F, (getBottom() + (int) getScrollAmount()) / 32F).color(64, 64, 64, 255).next();
			bufferBuilder.vertex(getRight(), getBottom(), 0).texture(getRight() / 32F, (getBottom() + (int) getScrollAmount()) / 32F).color(64, 64, 64, 255).next();
			bufferBuilder.vertex(getRight(), getY(), 0).texture(getRight() / 32F, (getY() + (int) getScrollAmount()) / 32F).color(64, 64, 64, 255).next();
			bufferBuilder.vertex(getX(), getY(), 0).texture(getX() / 32F, (getY() + (int) getScrollAmount()) / 32F).color(64, 64, 64, 255).next();
		} else if (background().isPresent()) {
			bufferBuilder.vertex(getX(), getBottom(), 0).texture(getX(), (getBottom() + (int) getScrollAmount())).color(64, 64, 64, 255).next();
			bufferBuilder.vertex(getRight(), getBottom(), 0).texture(getRight(), (getBottom() + (int) getScrollAmount())).color(64, 64, 64, 255).next();
			bufferBuilder.vertex(getRight(), getY(), 0).texture(getRight(), (getY() + (int) getScrollAmount())).color(64, 64, 64, 255).next();
			bufferBuilder.vertex(getX(), getY(), 0).texture(getX(), (getY() + (int) getScrollAmount())).color(64, 64, 64, 255).next();
		}
		tessellator.draw();
		
		RenderSystem.depthFunc(515);
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(getX(), (getY() + 4), 0).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(getRight(), (getY() + 4), 0).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(getRight(), getY(), 0).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(getX(), getY(), 0).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(getX(), getBottom(), 0).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(getRight(), getBottom(), 0).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(getRight(), (getBottom() - 4), 0).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(getX(), (getBottom() - 4), 0).color(0, 0, 0, 0).next();
		tessellator.draw();
		
		renderList(context, mouseX, mouseY, delta);
		renderScrollBar(bufferBuilder, tessellator);
		
		RenderSystem.disableBlend();
	}
	
	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}
	
	/**
	 * 渲染滚动条
	 *
	 * @param bufferBuilder 缓冲区生成器
	 * @param tessellator   镶嵌器
	 */
	public void renderScrollBar(BufferBuilder bufferBuilder, Tessellator tessellator) {
		int scrollbarStartX = getScrollbarPositionX();
		int scrollbarEndX = scrollbarStartX + 6;
		int maxScroll = getMaxScroll();
		if (maxScroll > 0) {
			int value = (int) ((float) ((getBottom() - getY()) * (getBottom() - getY())) / (float) getMaxPosition());
			value = MathHelper.clamp(value, 32, getBottom() - getY() - 8);
			int y = Math.max(getY(), (int) getScrollAmount() * (getBottom() - getY() - value) / maxScroll + getY());
			
			RenderSystem.setShader(GameRenderer::getPositionColorProgram);
			bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			bufferBuilder.vertex(scrollbarStartX, getBottom(), 0).color(0, 0, 0, 255).next();
			bufferBuilder.vertex(scrollbarEndX, getBottom(), 0).color(0, 0, 0, 255).next();
			bufferBuilder.vertex(scrollbarEndX, getY(), 0).color(0, 0, 0, 255).next();
			bufferBuilder.vertex(scrollbarStartX, getY(), 0).color(0, 0, 0, 255).next();
			bufferBuilder.vertex(scrollbarStartX, y + value, 0).color(128, 128, 128, 255).next();
			bufferBuilder.vertex(scrollbarEndX, y + value, 0).color(128, 128, 128, 255).next();
			bufferBuilder.vertex(scrollbarEndX, y, 0).color(128, 128, 128, 255).next();
			bufferBuilder.vertex(scrollbarStartX, y, 0).color(128, 128, 128, 255).next();
			bufferBuilder.vertex(scrollbarStartX, y + value - 1, 0).color(192, 192, 192, 255).next();
			bufferBuilder.vertex(scrollbarEndX - 1, y + value - 1, 0).color(192, 192, 192, 255).next();
			bufferBuilder.vertex(scrollbarEndX - 1, y, 0).color(192, 192, 192, 255).next();
			bufferBuilder.vertex(scrollbarStartX, y, 0).color(192, 192, 192, 255).next();
			tessellator.draw();
		}
	}
	
	@Override
	public Optional<Identifier> background() {
		return Optional.empty();
	}
	
	/**
	 * 文本条目
	 */
	@Environment(EnvType.CLIENT)
	public class TextEntry extends ElementListWidget.Entry<TextEntry> {
		protected final boolean updateTextEntry = false;
		protected final OrderedText text;
		protected final int indent;
		protected final TextListWidget widget;
		
		protected TextEntry(OrderedText text, TextListWidget widget, int indent) {
			this.text = text;
			this.widget = widget;
			this.indent = indent;
		}
		
		protected TextEntry(OrderedText text, TextListWidget widget) {
			this(text, widget, 0);
		}
		
		@Override
		public void render(DrawContext context, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
			if (widget.getY() > y || widget.getBottom() - textRenderer.fontHeight < y) return;
			context.drawTextWithShadow(textRenderer, text, updateTextEntry ? x + indent + 11 : x + indent, y, 0xFFFFFF);
		}
		
		@Override
		public List<? extends Element> children() {
			return Collections.emptyList();
		}
		
		@Override
		public List<? extends Selectable> selectableChildren() {
			return Collections.emptyList();
		}
	}
}
