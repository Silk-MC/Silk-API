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

package pers.saikel0rado1iu.silk.mixin.client.event.pattern;

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
import pers.saikel0rado1iu.silk.api.client.event.pattern.AddButtonInGameMenuCallback;

/**
 * <h2 style="color:FFC800">{@link AddButtonInGameMenuCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(GameMenuScreen.class)
abstract class AddButtonInGameMenuCallbackMixin extends Screen {
	@Unique
	private GridWidget.Adder adder;
	
	private AddButtonInGameMenuCallbackMixin(Text title) {
		super(title);
	}
	
	@ModifyVariable(method = "initWidgets", at = @At("STORE"), ordinal = 0)
	private GridWidget.Adder initWidgets(GridWidget.Adder adder) {
		return this.adder = adder;
	}
	
	@Inject(method = "initWidgets", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/screen/GameMenuScreen;createButton(L net/minecraft/text/Text;L java/util/function/Supplier;)L net/minecraft/client/gui/widget/ButtonWidget;", ordinal = 0))
	private void initWidgets(CallbackInfo ci) {
		if (client != null) AddButtonInGameMenuCallback.EVENT.invoker().add(client, this, adder);
	}
}
