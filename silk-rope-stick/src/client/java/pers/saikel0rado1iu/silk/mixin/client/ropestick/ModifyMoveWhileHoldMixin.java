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

package pers.saikel0rado1iu.silk.mixin.client.ropestick;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.ropestick.tool.ModifyMoveWhileHold;

/**
 * <h2 style="color:FFC800">{@link ModifyMoveWhileHold} 混入</h2>
 * 设置在持有物品时的移动速度
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(ClientPlayerEntity.class)
abstract class ModifyMoveWhileHoldMixin extends AbstractClientPlayerEntity {
	@Shadow
	public Input input;
	
	public ModifyMoveWhileHoldMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", shift = At.Shift.BEFORE))
	private void applyMovementMultiple(CallbackInfo ci) {
		Item mainHandItem = getMainHandStack().getItem();
		Item offHandItem = getOffHandStack().getItem();
		if (mainHandItem instanceof ModifyMoveWhileHold multiplier) {
			if (!multiplier.canModifyMove(getMainHandStack())) return;
			input.movementSideways *= multiplier.moveSpeedMultiple();
			input.movementForward *= multiplier.moveSpeedMultiple();
		}
		if (offHandItem instanceof ModifyMoveWhileHold multiplier && !multiplier.isConflictItem(mainHandItem)) {
			if (!multiplier.canModifyMove(getOffHandStack())) return;
			input.movementSideways *= multiplier.moveSpeedMultiple();
			input.movementForward *= multiplier.moveSpeedMultiple();
		}
	}
}
