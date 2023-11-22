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

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.callback.WorldPresetSetDefaultCallback;

import static pers.saikel0rado1iu.silk.api.callback.WorldPresetSetDefaultCallback.Data.worldCreator;

/**
 * <p><b style="color:FFC800"><font size="+1">设置默认世界预设回调</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
interface WorldPresetSetDefaultCallbackMixin {
	@Mixin(CreateWorldScreen.class)
	abstract class GetWorldCreator {
		@Inject(method = "init", at = @At("HEAD"))
		private void init(CallbackInfo ci) {
			worldCreator = ((CreateWorldScreen) (Object) this).getWorldCreator();
		}
	}
	
	@Mixin(targets = "net.minecraft.client.gui.screen.world.CreateWorldScreen$WorldTab")
	abstract class SetDefault {
		@SuppressWarnings("unchecked")
		@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/widget/CyclingButtonWidget;setValue(L java/lang/Object;)V"), index = 0)
		private <T> T set(T type) {
			worldCreator.setWorldType(WorldPresetSetDefaultCallback.EVENT.invoker().setDefault(worldCreator));
			return (T) worldCreator.getWorldType();
		}
	}
}
