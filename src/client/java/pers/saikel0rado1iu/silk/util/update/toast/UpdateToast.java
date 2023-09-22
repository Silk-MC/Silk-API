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

package pers.saikel0rado1iu.silk.util.update.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.TickUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p><b style="color:FFC800"><font size="+1">基础更新提示类，所有更新提示书签都在此基础上扩展</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class UpdateToast implements Toast {
	private static final Identifier TEXTURE = new Identifier("toast/advancement");
	private static final int SHOW_SEC = 45;
	private static final int SHOW_TITLE_SEC = SHOW_SEC / 2;
	private static final int SHOW_TEXT_SEC = SHOW_SEC - SHOW_TITLE_SEC;
	protected final Text title;
	protected final Text message;
	protected final UpdateShow updateShow;
	protected int time;
	
	public UpdateToast(Text title, UpdateShow updateShow, Text message) {
		this.title = title;
		this.updateShow = updateShow;
		this.message = message;
		this.updateShow.getUpdateThread().setCanCheckUpdate(false);
		this.updateShow.setCanShowScreen(false);
	}
	
	@SilkApi
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
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		context.drawTexture(TEXTURE, -28, 0, 0, 0, getWidth(), getHeight());
		RenderSystem.setShaderTexture(0, TEXTURE);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		context.drawTexture(TEXTURE, 0, 0, 0, 0, getWidth(), getHeight());
		RenderSystem.setShaderTexture(0, updateShow.getMod().getIcon().orElse(Silk.DATA.getIcon().orElseThrow()));
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		context.drawTexture(updateShow.getMod().getIcon().orElse(Silk.DATA.getIcon().orElseThrow()), -30 + 6, 4, 0, 0, 24, 24, 24, 24);
		TextRenderer textRenderer = manager.getClient().textRenderer;
		if (TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < SHOW_TITLE_SEC) {
			List<OrderedText> list = manager.getClient().textRenderer.wrapLines(title, getWidth() - 6);
			int perSec = SHOW_TITLE_SEC / list.size();
			OrderedText text = list.get(Math.min(list.size() - 1, (int) (TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) / perSec)));
			context.drawText(textRenderer, text, (getWidth() - textRenderer.getWidth(text)) / 2, (getHeight() - textRenderer.fontHeight) / 2, updateShow.getMod().getThemeColor(), false);
		} else {
			List<OrderedText> list = manager.getClient().textRenderer.wrapLines(message, getWidth() - 6);
			int perSec = SHOW_TEXT_SEC / list.size();
			OrderedText text = list.get(Math.min(list.size() - 1, (int) ((TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) - SHOW_TITLE_SEC) / perSec)));
			context.drawText(textRenderer, text, (getWidth() - textRenderer.getWidth(text)) / 2, (getHeight() - textRenderer.fontHeight) / 2, updateShow.getMod().getThemeColor(), false);
		}
		return TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < SHOW_SEC ? Visibility.SHOW : Visibility.HIDE;
	}
}
