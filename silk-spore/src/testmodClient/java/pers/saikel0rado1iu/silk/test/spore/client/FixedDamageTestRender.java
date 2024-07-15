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

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import pers.saikel0rado1iu.silk.impl.SilkSpore;
import pers.saikel0rado1iu.silk.test.spore.FixedDamageTest;

/**
 * FixedDamageTestRender
 */
public class FixedDamageTestRender extends EntityRenderer<FixedDamageTest> {
	private static final Identifier TEXTURE = SilkSpore.getInstance().ofId("textures/entity/steel_arrow.png");
	
	/**
	 * @param context context
	 */
	public FixedDamageTestRender(EntityRendererFactory.Context context) {
		super(context);
	}
	
	@Override
	public void render(FixedDamageTest entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, entity.prevYaw, entity.getYaw()) - 90.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, entity.prevPitch, entity.getPitch())));
		float s = (float) entity.shake - g;
		if (s > 0) matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-MathHelper.sin(s * 3.0F) * s));
		
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0F));
		matrixStack.scale(0.075F, 0.075F, 0.075F);
		matrixStack.translate(-4.0F, 0.0F, 0.0F);
		VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(entity)));
		MatrixStack.Entry entry = matrixStack.peek();
		Matrix4f matrix4f = entry.getPositionMatrix();
		Matrix3f matrix3f = entry.getNormalMatrix();
		vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, i);
		vertex(matrix4f, matrix3f, vertexConsumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, i);
		
		for (int u = 0; u < 4; ++u) {
			matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
			vertex(matrix4f, matrix3f, vertexConsumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, i);
			vertex(matrix4f, matrix3f, vertexConsumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, i);
			vertex(matrix4f, matrix3f, vertexConsumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, i);
			vertex(matrix4f, matrix3f, vertexConsumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, i);
		}
		
		matrixStack.pop();
		super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
	}
	
	/**
	 * @param positionMatrix test
	 * @param normalMatrix   test
	 * @param vertexConsumer test
	 * @param x              test
	 * @param y              test
	 * @param z              test
	 * @param u              test
	 * @param v              test
	 * @param normalX        test
	 * @param normalZ        test
	 * @param normalY        test
	 * @param light          test
	 */
	public void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int light) {
		vertexConsumer.vertex(positionMatrix, (float) x, (float) y, (float) z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, (float) normalX, (float) normalY, (float) normalZ).next();
	}
	
	@Override
	public Identifier getTexture(FixedDamageTest spectralArrowEntity) {
		return TEXTURE;
	}
}