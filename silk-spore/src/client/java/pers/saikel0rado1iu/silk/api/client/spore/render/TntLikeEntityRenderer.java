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

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import pers.saikel0rado1iu.silk.api.spore.TntLikeEntity;

/**
 * <h2 style="color:FFC800">TNT 类实体渲染器</h2>
 * 为 TNT 类实体提供一个默认的实体渲染器，以原版样式渲染
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
public class TntLikeEntityRenderer extends EntityRenderer<TntLikeEntity> {
	protected final BlockRenderManager blockRenderManager;
	
	/**
	 * @param context 实体渲染器工厂内容
	 */
	public TntLikeEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
		this.shadowRadius = 0.5F;
		this.blockRenderManager = context.getBlockRenderManager();
	}
	
	public void render(TntLikeEntity tntEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		matrixStack.push();
		matrixStack.translate(0, 0.5F, 0);
		int j = tntEntity.getFuse();
		if ((float) j - g + 1 < 10) {
			float h = 1 - ((float) j - g + 1) / 10;
			h = MathHelper.clamp(h, 0, 1);
			h *= h;
			h *= h;
			float k = 1 + h * 0.3F;
			matrixStack.scale(k, k, k);
		}
		
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90));
		matrixStack.translate(-0.5F, -0.5F, 0.5F);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
		TntMinecartEntityRenderer.renderFlashingBlock(blockRenderManager, tntEntity.getBlockState(), matrixStack, vertexConsumerProvider, i, j / 5 % 2 == 0);
		matrixStack.pop();
		super.render(tntEntity, f, g, matrixStack, vertexConsumerProvider, i);
	}
	
	public Identifier getTexture(TntLikeEntity tntEntity) {
		//noinspection deprecation
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
