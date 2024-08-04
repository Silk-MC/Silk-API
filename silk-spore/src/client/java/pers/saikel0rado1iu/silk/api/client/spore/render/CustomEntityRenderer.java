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

package pers.saikel0rado1iu.silk.api.client.spore.render;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * <h2 style="color:FFC800">自定义实体渲染器</h2>
 * 一个可以为 {@link Entity} 对象提供独立的模型与动画的渲染器
 *
 * @param <M> 实体模型
 * @param <T> 实体
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
public abstract class CustomEntityRenderer<T extends Entity, M extends EntityModel<T>> extends EntityRenderer<T> implements FeatureRendererContext<T, M> {
	protected final List<FeatureRenderer<T, M>> features = Lists.newArrayList();
	protected M model;
	
	protected CustomEntityRenderer(EntityRendererFactory.Context ctx, M model, float shadowRadius) {
		super(ctx);
		this.model = model;
		this.shadowRadius = shadowRadius;
	}
	
	private static boolean shouldFlipUpsideDown(Entity entity) {
		String string;
		if ((entity instanceof PlayerEntity || entity.hasCustomName()) && ("Dinnerbone".equals(string = Formatting.strip(entity.getName().getString())) || "Grumm".equals(string))) {
			return !(entity instanceof PlayerEntity) || ((PlayerEntity) entity).isPartVisible(PlayerModelPart.CAPE);
		}
		return false;
	}
	
	@Override
	public void render(T entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		// Initial setup
		matrices.push();
		EntityModel<T> entityModel = model;
		entityModel.riding = entity.hasVehicle();
		
		// Calculate body and head yaw
		float bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getBodyYaw());
		float headYaw = MathHelper.lerpAngleDegrees(tickDelta, entity.prevYaw, entity.getHeadYaw());
		float headYawDifference = headYaw - bodyYaw;
		
		// Adjust yaw if entity is riding another entity
		if (entity.hasVehicle() && entity.getVehicle() instanceof LivingEntity vehicleEntity) {
			bodyYaw = MathHelper.lerpAngleDegrees(tickDelta, vehicleEntity.prevBodyYaw, vehicleEntity.bodyYaw);
			headYawDifference = headYaw - bodyYaw;
			float clampedYawDifference = MathHelper.wrapDegrees(headYawDifference);
			clampedYawDifference = MathHelper.clamp(clampedYawDifference, -85.0f, 85.0f);
			bodyYaw = headYaw - clampedYawDifference;
			if (clampedYawDifference * clampedYawDifference > 2500.0f) bodyYaw += clampedYawDifference * 0.2f;
			headYawDifference = headYaw - bodyYaw;
		}
		
		// Calculate pitch and handle upside down
		float pitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
		if (shouldFlipUpsideDown(entity)) {
			pitch *= -1.0f;
			headYawDifference *= -1.0f;
		}
		
		// Setup transformations and scale
		float animationProgress = getAnimationProgress(entity, tickDelta);
		setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
		matrices.scale(-1.0f, -1.0f, 1.0f);
		scale(entity, matrices, tickDelta);
		matrices.translate(0.0f, -1.501f, 0.0f);
		
		// Animate model
		float limbSwing = 0.0f;
		float limbSwingAmount = 0.0f;
		if (!entity.hasVehicle() && entity.isAlive()) limbSwingAmount = MathHelper.clamp(limbSwingAmount, 0.0f, 1.0f);
		entityModel.animateModel(entity, limbSwing, limbSwingAmount, tickDelta);
		entityModel.setAngles(entity, limbSwing, limbSwingAmount, animationProgress, headYawDifference, pitch);
		
		// Render the entity
		MinecraftClient minecraftClient = MinecraftClient.getInstance();
		boolean isVisible = isVisible(entity);
		boolean isNotVisibleToPlayer = !isVisible && !entity.isInvisibleTo(minecraftClient.player);
		boolean hasOutline = minecraftClient.hasOutline(entity);
		RenderLayer renderLayer = getRenderLayer(entity, isVisible, isNotVisibleToPlayer, hasOutline);
		if (renderLayer != null) {
			VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
			int overlay = OverlayTexture.packUv(OverlayTexture.getU(getAnimationCounter(entity, tickDelta)), OverlayTexture.getV(false));
			entityModel.render(matrices, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, isNotVisibleToPlayer ? 0.15f : 1.0f);
		}
		
		// Render features
		if (!entity.isSpectator()) {
			for (FeatureRenderer<T, M> featureRenderer : features) {
				featureRenderer.render(matrices, vertexConsumers, light, entity, limbSwing, limbSwingAmount, tickDelta, animationProgress, headYawDifference, pitch);
			}
		}
		
		matrices.pop();
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
	}
	
	/**
	 * Gets the render layer appropriate for rendering the passed entity. Returns null if the entity should not be rendered.
	 */
	@Nullable
	protected RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline) {
		Identifier identifier = getTexture(entity);
		if (translucent) return RenderLayer.getItemEntityTranslucentCull(identifier);
		if (showBody) return model.getLayer(identifier);
		if (showOutline) return RenderLayer.getOutline(identifier);
		return null;
	}
	
	protected void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		if (isShaking(entity)) bodyYaw += (float) (Math.cos((double) (entity).age * 3.25) * Math.PI * (double) 0.4f);
		if (!entity.isInPose(EntityPose.SLEEPING)) matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - bodyYaw));
		if (entity.isInPose(EntityPose.SLEEPING)) {
			matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(getLyingAngle(entity)));
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(270.0f));
		}
	}
	
	protected float getAnimationCounter(T entity, float tickDelta) {
		return 0.0f;
	}
	
	/**
	 * This value is passed to other methods when calculating angles for animation.
	 * It's typically just the sum of the entity's age (in ticks) and the passed in tickDelta.
	 */
	protected float getAnimationProgress(T entity, float tickDelta) {
		return (float) entity.age + tickDelta;
	}
	
	/**
	 * {@return if this entity is shaking} Specifically, in the way a zombie villager,
	 * zombie, husk, or piglin undergoing conversion shakes.
	 */
	protected boolean isShaking(T entity) {
		return entity.isFrozen();
	}
	
	protected boolean isVisible(T entity) {
		return !entity.isInvisible();
	}
	
	protected float getLyingAngle(T entity) {
		return 90;
	}
	
	protected void scale(T entity, MatrixStack matrices, float amount) {
	}
	
	protected final boolean addFeature(FeatureRenderer<T, M> feature) {
		return features.add(feature);
	}
	
	@Override
	public M getModel() {
		return model;
	}
}
