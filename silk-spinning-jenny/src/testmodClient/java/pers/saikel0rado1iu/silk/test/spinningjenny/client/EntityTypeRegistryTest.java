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

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import pers.saikel0rado1iu.silk.api.spinningjenny.EntityTypeRegistry;

import static pers.saikel0rado1iu.silk.test.spinningjenny.EntityTypeRegistryTest.TEST_ENTITY_TYPE;

/**
 * Test {@link EntityTypeRegistry}
 */
public abstract class EntityTypeRegistryTest implements EntityTypeRegistry {
	static {
		EntityTypeRegistry.registrar((Runnable) () -> EntityRendererRegistry.register(TEST_ENTITY_TYPE, ArrowEntityRenderer::new)).register(TEST_ENTITY_TYPE);
	}
	
	private EntityTypeRegistryTest() {
	}
}
