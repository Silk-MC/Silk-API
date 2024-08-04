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

package pers.saikel0rado1iu.silk.test.spore.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.client.spore.render.CustomEntityRenderer;
import pers.saikel0rado1iu.silk.impl.Minecraft;

/**
 * CustomEntityRendererTest
 */
public class CustomEntityRendererTest extends CustomEntityRenderer<CreeperEntity, CreeperEntityModel<CreeperEntity>> {
	CustomEntityRendererTest(EntityRendererFactory.Context ctx, CreeperEntityModel<CreeperEntity> model, float shadowRadius) {
		super(ctx, model, shadowRadius);
	}
	
	@Override
	protected void setupTransforms(CreeperEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta) {
		super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);
	}
	
	@Override
	protected float getAnimationCounter(CreeperEntity entity, float tickDelta) {
		return super.getAnimationCounter(entity, tickDelta);
	}
	
	@Override
	protected float getLyingAngle(CreeperEntity entity) {
		return super.getLyingAngle(entity);
	}
	
	@Override
	protected void scale(CreeperEntity entity, MatrixStack matrices, float amount) {
		super.scale(entity, matrices, amount);
		addFeature(null);
		addFeature(new FeatureRenderer<CreeperEntity, CreeperEntityModel<CreeperEntity>>(this) {
			@Override
			public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CreeperEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
			}
		});
	}
	
	@Override
	public Identifier getTexture(CreeperEntity entity) {
		return Minecraft.getInstance().ofId("textures/entity/creeper");
	}
}
