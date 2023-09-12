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

package pers.saikel0rado1iu.silk.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.util.config.ConfigScreen;

import static pers.saikel0rado1iu.silk.Main.CONFIG_DATA;

/**
 * <p><b style="color:FFC800"><font size="+1">作用</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(TitleScreen.class)
abstract class CMixin extends Screen {
	protected CMixin(Text title) {
		super(title);
	}
	
	@Inject(method = "initWidgetsNormal", at = @At("TAIL"))
	private void initWidgetsNormal(int y, int spacingY, CallbackInfo ci) {
		// 添加 自然更替 按钮
		ConfigScreen configScreen = new ConfigScreen(this, CONFIG_DATA);
		addDrawableChild(ButtonWidget.builder(configScreen.getTitle(), (button) -> MinecraftClient.getInstance().setScreen(
				//new UpdateFailWarningScreen(this,new UpdateData(Silk.DATA,CONFIG_DATA),false)
/*				new ModScreen(this, Screen.OPTIONS_BACKGROUND_TEXTURE, 1,
						new SynopsisTab(Silk.DATA),
						new ConfigTab(Silk.DATA, CONFIG_DATA),
						new ModTab(Silk.DATA, Paths.get("dsa"))*/
						configScreen
				)).dimensions(width / 2 - 100, y - (spacingY * 2), 200, 20).build());
	}
}
