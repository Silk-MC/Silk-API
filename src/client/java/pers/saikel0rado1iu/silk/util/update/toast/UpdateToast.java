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
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.util.TickUtil;
import pers.saikel0rado1iu.silk.util.update.UpdateShow;

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
        if (TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < 15) {
            context.drawText(manager.getClient().textRenderer, title, (getWidth() - manager.getClient().textRenderer.getWidth(title)) / 2, (getHeight() - manager.getClient().textRenderer.fontHeight) / 2, updateShow.getMod().getThemeColor(), false);
        } else {
            MultilineText.create(manager.getClient().textRenderer, message, getWidth() - 6).draw(context, 4, getHeight() / 2 - manager.getClient().textRenderer.fontHeight, 10, 0xFFFFFF);
        }
        return TickUtil.getTime(time, TickUtil.Type.NATURAL, TimeUnit.SECONDS) < 30 ? Visibility.SHOW : Visibility.HIDE;
    }
}
