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

package pers.saikel0rado1iu.silk.mixin.api.callback;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.callback.GameMenuScreenAddButtonCallback;

/**
 * <p><b style="color:FFC800"><font size="+1">设置游戏菜单屏幕添加按钮回调</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(GameMenuScreen.class)
abstract class GameMenuScreenAddButtonCallbackMixin extends Screen {
	@Unique
	private GridWidget.Adder adder;
	
	private GameMenuScreenAddButtonCallbackMixin(Text title) {
		super(title);
	}
	
	@ModifyVariable(method = "initWidgets", at = @At("STORE"), ordinal = 0)
	private GridWidget.Adder initWidgets(GridWidget.Adder adder) {
		return this.adder = adder;
	}
	
	@Inject(method = "initWidgets", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/screen/GameMenuScreen;createButton(L net/minecraft/text/Text;L java/util/function/Supplier;)L net/minecraft/client/gui/widget/ButtonWidget;", ordinal = 0))
	private void initWidgets(CallbackInfo ci) {
		if (client != null) GameMenuScreenAddButtonCallback.EVENT.invoker().add(adder, client, this);
	}
}
