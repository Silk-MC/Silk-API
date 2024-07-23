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

package pers.saikel0rado1iu.silk.test.spinningjenny.client;

import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SpiderEntityModel;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.EntityModelLayerRegistry;

import static net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry.registerModelLayer;

/**
 * Test {@link EntityModelLayerRegistry}
 */
public abstract class ModelLayerRegistryTest implements EntityModelLayerRegistry {
	/**
	 * test_entity_model_layer
	 */
	@SuppressWarnings("unused")
	public static final EntityModelLayer TEST_ENTITY_MODEL_LAYER = new EntityModelLayer(SilkSpinningJenny.getInstance().ofId("test_entity_model_layer"), "main");
	
	static {
		EntityModelLayerRegistry.registrar(() -> registerModelLayer(TEST_ENTITY_MODEL_LAYER, SpiderEntityModel::getTexturedModelData)).register(TEST_ENTITY_MODEL_LAYER);
	}
	
	private ModelLayerRegistryTest() {
	}
}
