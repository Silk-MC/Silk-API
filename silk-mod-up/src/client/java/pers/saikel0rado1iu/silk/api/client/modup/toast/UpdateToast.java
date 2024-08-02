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

package pers.saikel0rado1iu.silk.api.client.modup.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.base.common.util.TickUtil;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.api.modup.UpdateData;
import pers.saikel0rado1iu.silk.api.client.modup.ClientUpdateManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <h2 style="color:FFC800">更新提示框</h2>
 * 基础更新提示类，所有更新提示书签都在此基础上扩展
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class UpdateToast implements Toast {
	private static final Identifier TEXTURE = new Identifier("toast/advancement");
	private static final int SHOW_SEC = 45;
	private static final int SHOW_TITLE_SEC = SHOW_SEC / 3;
	private static final int SHOW_TEXT_SEC = SHOW_SEC - SHOW_TITLE_SEC;
	protected final Text title;
	protected final Text message;
	protected final UpdateData updateData;
	protected final ClientUpdateManager updateManager;
	protected int time;
	
	protected UpdateToast(Text title, UpdateData updateData, ClientUpdateManager updateManager, Text message) {
		this.title = title.copy().formatted(Formatting.WHITE);
		this.updateData = updateData;
		this.updateManager = updateManager;
		this.message = message.copy().formatted(Formatting.WHITE);
		this.updateManager.setCanShowUpdateNotify(false);
	}
	
	/**
	 * 设置提示框
	 *
	 * @param updateToast 更新提示框
	 */
	public static void setToast(UpdateToast updateToast) {
		ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
		UpdateToast haveToast = toastManager.getToast(updateToast.getClass(), Toast.TYPE);
		if (haveToast == null) toastManager.add(updateToast);
	}
	
	@Override
	public Visibility draw(DrawContext context, ToastManager manager, long startTime) {
		time++;
		if (TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < 5) return Visibility.SHOW;
		RenderSystem.setShader(GameRenderer::getPositionTexProgram);
		context.drawGuiTexture(TEXTURE, -28, 0, getWidth(), getHeight());
		context.drawGuiTexture(TEXTURE, 0, 0, getWidth(), getHeight());
		RenderSystem.setShaderTexture(0, updateData.modData().icon().orElse(SilkModUp.getInstance().icon().orElseThrow()));
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		context.drawTexture(updateData.modData().icon().orElse(SilkModUp.getInstance().icon().orElseThrow()), -30 + 6, 4, 0, 0, 24, 24, 24, 24);
		TextRenderer textRenderer = manager.getClient().textRenderer;
		if (TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < SHOW_TITLE_SEC) {
			List<OrderedText> list = manager.getClient().textRenderer.wrapLines(title, getWidth() - 12);
			int perSec = SHOW_TITLE_SEC / list.size();
			OrderedText text = list.get(Math.min(list.size() - 1, (int) (TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) / perSec)));
			context.drawText(textRenderer, text, (getWidth() - textRenderer.getWidth(text)) / 2, (getHeight() - textRenderer.fontHeight) / 2, updateData.modData().themeColor(), false);
		} else {
			List<OrderedText> list = manager.getClient().textRenderer.wrapLines(message, getWidth() - 12);
			int perSec = SHOW_TEXT_SEC / list.size();
			OrderedText text = list.get(Math.min(list.size() - 1, (int) ((TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) - SHOW_TITLE_SEC) / perSec)));
			context.drawText(textRenderer, text, (getWidth() - textRenderer.getWidth(text)) / 2, (getHeight() - textRenderer.fontHeight) / 2, updateData.modData().themeColor(), false);
		}
		return TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < SHOW_SEC ? Visibility.SHOW : Visibility.HIDE;
	}
}
