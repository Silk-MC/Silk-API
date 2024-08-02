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

package pers.saikel0rado1iu.silk.mixin.client.event.landform;

import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.client.event.landform.WorldPresetSetDefaultCallback;

/**
 * <h2 style="color:FFC800">{@link WorldPresetSetDefaultCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(targets = "net.minecraft.client.gui.screen.world.CreateWorldScreen$WorldTab")
abstract class WorldPresetSetDefaultCallbackMixin {
	@Shadow
	@Final
	CreateWorldScreen field_42182;
	
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/widget/CyclingButtonWidget;setValue(L java/lang/Object;)V"), index = 0)
	private Object set(Object type) {
		WorldCreator worldCreator = field_42182.getWorldCreator();
		worldCreator.setWorldType(WorldPresetSetDefaultCallback.EVENT.invoker().apply(worldCreator));
		return worldCreator.getWorldType();
	}
}
