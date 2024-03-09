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

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Item;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pers.saikel0rado1iu.silk.api.item.tool.HoldingFovZoom;

/**
 * <h2 style="color:FFC800">设置在持有物品时的视场角缩放</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface HoldingFovZoomMixin {
	/**
	 * 设置视角缩放
	 */
	@Mixin(GameRenderer.class)
	abstract class SetUsingFovZoom implements AutoCloseable {
		@Shadow
		@Final
		MinecraftClient client;
		@Shadow
		private float fovMultiplier;
		
		@Inject(method = "tick", at = @At(value = "INVOKE", target = "L net/minecraft/client/render/GameRenderer;updateFovMultiplier()V", shift = At.Shift.AFTER))
		private void setFovScale(CallbackInfo ci) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			Item mainHandItem = player.getMainHandStack().getItem();
			Item offHandItem = player.getOffHandStack().getItem();
			if (mainHandItem instanceof HoldingFovZoom fovZoom) {
				if (!fovZoom.canChangeFov(player.getMainHandStack())) return;
				if (fovZoom.onlyFirstPerson()) {
					if (client.options.getPerspective().isFirstPerson()) {
						float fovChangeAmount = 1 - fovZoom.getHoldingFovMultiple();
						fovMultiplier -= fovChangeAmount;
					}
				} else {
					float fovChangeAmount = 1 - fovZoom.getHoldingFovMultiple();
					fovMultiplier -= fovChangeAmount;
				}
			}
			if (offHandItem instanceof HoldingFovZoom fovZoom && !fovZoom.isConflictItems(mainHandItem)) {
				if (!fovZoom.canChangeFov(player.getOffHandStack())) return;
				if (fovZoom.onlyFirstPerson()) {
					if (client.options.getPerspective().isFirstPerson()) {
						float fovChangeAmount = 1 - fovZoom.getHoldingFovMultiple();
						fovMultiplier -= fovChangeAmount;
					}
				} else {
					float fovChangeAmount = 1 - fovZoom.getHoldingFovMultiple();
					fovMultiplier -= fovChangeAmount;
				}
			}
		}
	}
	
	/**
	 * 设置鼠标移动倍数
	 */
	@Mixin(Mouse.class)
	abstract class SetMouseMoveMultiplier {
		@Shadow
		@Final
		private MinecraftClient client;
		
		@ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "L net/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"))
		private void setMove(Args args) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			Item mainHandItem = player.getMainHandStack().getItem();
			Item offHandItem = player.getOffHandStack().getItem();
			if (mainHandItem instanceof HoldingFovZoom fovZoom) {
				if (!fovZoom.canChangeFov(player.getMainHandStack())) return;
				if (fovZoom.onlyFirstPerson()) {
					if (client.options.getPerspective().isFirstPerson()) {
						args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
						args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					}
				} else {
					args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
				}
			}
			if (offHandItem instanceof HoldingFovZoom fovZoom && !fovZoom.isConflictItems(mainHandItem)) {
				if (!fovZoom.canChangeFov(player.getOffHandStack())) return;
				if (fovZoom.onlyFirstPerson()) {
					if (client.options.getPerspective().isFirstPerson()) {
						args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
						args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					}
				} else {
					args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
				}
			}
		}
		
		@ModifyArgs(method = "updateMouse", at = @At(value = "INVOKE", target = "L net/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V"))
		private void setLookDirection(Args args) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			Item mainHandItem = player.getMainHandStack().getItem();
			Item offHandItem = player.getOffHandStack().getItem();
			if (mainHandItem instanceof HoldingFovZoom fovZoom) {
				if (!fovZoom.canChangeFov(player.getMainHandStack())) return;
				if (fovZoom.onlyFirstPerson()) {
					if (client.options.getPerspective().isFirstPerson()) {
						args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
						args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					}
				} else {
					args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
				}
			}
			if (offHandItem instanceof HoldingFovZoom fovZoom && !fovZoom.isConflictItems(mainHandItem)) {
				if (!fovZoom.canChangeFov(player.getOffHandStack())) return;
				if (fovZoom.onlyFirstPerson()) {
					if (client.options.getPerspective().isFirstPerson()) {
						args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
						args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					}
				} else {
					args.set(0, (double) args.get(0) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
					args.set(1, (double) args.get(1) * Math.pow(fovZoom.getHoldingFovMultiple(), 3));
				}
			}
		}
	}
	
	/**
	 * 设置缩放抬头显示
	 */
	@Mixin(InGameHud.class)
	abstract class RenderHudOverlay {
		@Final
		@Shadow
		private MinecraftClient client;
		
		@Unique
		private float hudScale = 0;
		
		@Shadow
		protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);
		
		@Unique
		private void renderHudOverlay(DrawContext context, Identifier texture, float scale) {
			int windowWidth = context.getScaledWindowWidth();
			int windowHeight = context.getScaledWindowHeight();
			int minWindowSize = Math.min(windowWidth, windowHeight);
			float scaleFactor = Math.min((float) windowWidth / minWindowSize, (float) windowHeight / minWindowSize) * scale;
			int scaledWidth = MathHelper.floor(minWindowSize * scaleFactor);
			int scaledHeight = MathHelper.floor(minWindowSize * scaleFactor);
			int offsetX = (windowWidth - scaledWidth) / 2;
			int offsetY = (windowHeight - scaledHeight) / 2;
			int endX = offsetX + scaledWidth;
			int endY = offsetY + scaledHeight;
			
			RenderSystem.enableBlend();
			context.drawTexture(texture, offsetX, offsetY, -90, 0, 0, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
			RenderSystem.disableBlend();
			
			// Fill overlay
			context.fill(RenderLayer.getGuiOverlay(), 0, endY, windowWidth, windowHeight, -90, Colors.BLACK);
			context.fill(RenderLayer.getGuiOverlay(), 0, 0, windowWidth, offsetY, -90, Colors.BLACK);
			context.fill(RenderLayer.getGuiOverlay(), 0, offsetY, offsetX, endY, -90, Colors.BLACK);
			context.fill(RenderLayer.getGuiOverlay(), endX, offsetY, windowWidth, endY, -90, Colors.BLACK);
		}
		
		@Inject(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "L net/minecraft/client/option/Perspective;isFirstPerson()Z", shift = At.Shift.BY))
		private void setRender(DrawContext context, float tickDelta, CallbackInfo ci) {
			ClientPlayerEntity player = client.player;
			if (player == null) return;
			Item mainHandItem = player.getMainHandStack().getItem();
			Item offHandItem = player.getOffHandStack().getItem();
			hudScale = MathHelper.lerp(0.5F * client.getLastFrameDuration(), hudScale, 1.125F);
			if (mainHandItem instanceof HoldingFovZoom fovZoom && fovZoom.getHubOverlay().isPresent()) {
				if (!fovZoom.canChangeFov(player.getMainHandStack())) return;
				if (client.options.getPerspective().isFirstPerson()) {
					if (fovZoom.isHubStretch()) renderOverlay(context, fovZoom.getHubOverlay().get(), 1.0f);
					else renderHudOverlay(context, fovZoom.getHubOverlay().get(), hudScale);
				}
			}
			if (offHandItem instanceof HoldingFovZoom fovZoom && fovZoom.getHubOverlay().isPresent()) {
				if (!fovZoom.canChangeFov(player.getOffHandStack())) return;
				if (client.options.getPerspective().isFirstPerson()) {
					if (fovZoom.isHubStretch()) renderOverlay(context, fovZoom.getHubOverlay().get(), 1.0f);
					else renderHudOverlay(context, fovZoom.getHubOverlay().get(), hudScale);
				}
			}
		}
	}
}
