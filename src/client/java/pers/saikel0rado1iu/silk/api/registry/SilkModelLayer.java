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

package pers.saikel0rado1iu.silk.api.registry;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModClient;

import java.util.Set;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">用于模组所有模型图层组成模型图层集与模型图层注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModClient} 中覆盖 {@link ModClient#modelLayers()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkModelLayer {
	public static final Set<EntityModelLayer> ALL_MODEL_LAYERS = Sets.newLinkedHashSetWithExpectedSize(8);
	
	protected static <L extends EntityModelLayer> Builder<L> builder(L layer) {
		return new Builder<>(layer);
	}
	
	@SilkApi
	public static final class Builder<L extends EntityModelLayer> {
		private final L layer;
		
		@SilkApi
		private Builder(L layer) {
			ALL_MODEL_LAYERS.add(this.layer = layer);
		}
		
		@SilkApi
		public Builder<L> put(Set<EntityModelLayer> layers) {
			layers.add(layer);
			return this;
		}
		
		@SilkApi
		public Builder<L> otherRegister(Consumer<L> layerRegister) {
			layerRegister.accept(layer);
			return this;
		}
		
		@SilkApi
		public L build(EntityModelLayerRegistry.TexturedModelDataProvider provider) {
			EntityModelLayerRegistry.registerModelLayer(layer, provider);
			return layer;
		}
	}
}
