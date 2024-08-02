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

package pers.saikel0rado1iu.silk.mixin.ropestick.tool;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.ropestick.ranged.Crossbow;

/**
 * <h2 style="color:FFC800">{@link Crossbow} 混入</h2>
 * 设置弩的物品渲染
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface CrossbowMixin {
	/**
	 * 弩的第一人称渲染
	 */
	@Mixin(HeldItemRenderer.class)
	abstract class FirstPersonRender {
		@Inject(method = "isChargedCrossbow", at = @At("RETURN"), cancellable = true)
		private static void isChargedCrossbow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
			cir.setReturnValue(stack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(stack));
		}
		
		@Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "L net/minecraft/item/ItemStack;isOf(L net/minecraft/item/Item;)Z", ordinal = 1))
		private boolean renderFirstPersonItem(ItemStack instance, Item item) {
			return instance.getItem() instanceof CrossbowItem;
		}
	}
	
	/**
	 * 弩的第三人称渲染
	 */
	@Mixin(PlayerEntityRenderer.class)
	abstract class ThirdPersonRender {
		@Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
		private static void getArmPose(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
			ItemStack itemStack = player.getStackInHand(hand);
			if (!itemStack.isEmpty() && !player.handSwinging && itemStack.getItem() instanceof CrossbowItem crossbow && CrossbowItem.isCharged(itemStack)) {
				BipedEntityModel.ArmPose armPose = BipedEntityModel.ArmPose.CROSSBOW_CHARGE;
				UseAction useAction = crossbow.getUseAction(itemStack);
				if (useAction == UseAction.BLOCK) armPose = BipedEntityModel.ArmPose.BLOCK;
				else if (useAction == UseAction.BOW) armPose = BipedEntityModel.ArmPose.BOW_AND_ARROW;
				else if (useAction == UseAction.SPEAR) armPose = BipedEntityModel.ArmPose.THROW_SPEAR;
				else if (useAction == UseAction.SPYGLASS) armPose = BipedEntityModel.ArmPose.SPYGLASS;
				else if (useAction == UseAction.TOOT_HORN) armPose = BipedEntityModel.ArmPose.TOOT_HORN;
				else if (useAction == UseAction.BRUSH) armPose = BipedEntityModel.ArmPose.BRUSH;
				cir.setReturnValue(armPose);
			}
		}
	}
}
