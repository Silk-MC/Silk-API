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

package pers.saikel0rado1iu.silk.mixin.api.item;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.item.tool.UsingMovementMultiplier;

import static pers.saikel0rado1iu.silk.api.item.tool.UsingMovementMultiplier.DEFAULT_MOVEMENT_MULTIPLE;


/**
 * <h2 style="color:FFC800">设置在使用物品时的移动速度</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(ClientPlayerEntity.class)
abstract class UsingMovementMultiplierMixin extends AbstractClientPlayerEntity {
	@Shadow
	public Input input;
	
	public UsingMovementMultiplierMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.AFTER))
	private void applyMovementMultiple(CallbackInfo ci) {
		if (isUsingItem() && getStackInHand(getActiveHand()).getItem() instanceof UsingMovementMultiplier multiplier) {
			input.movementSideways /= DEFAULT_MOVEMENT_MULTIPLE;
			input.movementSideways *= multiplier.getUsingMovementMultiple();
			input.movementForward /= DEFAULT_MOVEMENT_MULTIPLE;
			input.movementForward *= multiplier.getUsingMovementMultiple();
		}
	}
}
