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

import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.callback.RegisterMultiNoiseBiomeSourceParameterListPresetCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">设置注册多重噪声生物群系源参数列表预设的回调</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(MultiNoiseBiomeSourceParameterList.Preset.class)
abstract class RegisterMultiNoiseBiomeSourceParameterListPresetCallbackMixin {
	@SuppressWarnings("unchecked")
	@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "L java/util/stream/Stream;of([L java/lang/Object;)L java/util/stream/Stream;"))
	private static <T> T[] register(T[] byId) {
		List<T> presets = new ArrayList<>(List.of(byId));
		RegisterMultiNoiseBiomeSourceParameterListPresetCallback.EVENT.invoker().register((List<MultiNoiseBiomeSourceParameterList.Preset>) presets);
		return (T[]) presets.toArray(new Object[0]);
	}
}
