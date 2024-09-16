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

package pers.saikel0rado1iu.silk.mixin.event.modplus;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyDefaultBiomeParametersEvents;
import pers.saikel0rado1iu.silk.api.landform.biome.source.util.DefaultBiomeParameters;

import java.util.List;

/**
 * <h2 style="color:FFC800">{@link ModifyDefaultBiomeParametersEvents} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyDefaultBiomeParametersEventsMixin {
	/**
	 * {@link ModifyDefaultBiomeParametersEvents#MODIFY_NON_VANILLA_GENERATED_BIOME} 混入
	 */
	@Mixin(DefaultBiomeParameters.class)
	abstract class ModifyNonVanillaGeneratedBiome {
		@Inject(method = "nonVanillaGeneratedBiome", at = @At("RETURN"), cancellable = true, remap = false)
		private void nonVanillaGeneratedBiome(CallbackInfoReturnable<List<RegistryKey<Biome>>> cir) {
			cir.setReturnValue(ModifyDefaultBiomeParametersEvents.MODIFY_NON_VANILLA_GENERATED_BIOME.invoker()
					.nonVanillaGeneratedBiome(((DefaultBiomeParameters) (Object) this), cir.getReturnValue()).getValue());
		}
	}
}
