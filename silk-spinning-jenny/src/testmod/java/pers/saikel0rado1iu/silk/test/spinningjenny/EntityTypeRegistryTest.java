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

package pers.saikel0rado1iu.silk.test.spinningjenny;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.spinningjenny.EntityTypeRegistry;

/**
 * Test {@link EntityTypeRegistry}
 */
public interface EntityTypeRegistryTest extends EntityTypeRegistry {
	/**
	 * test_entity_type
	 */
	@SuppressWarnings("unused")
	EntityType<TestEntityType> TEST_ENTITY_TYPE = EntityTypeRegistry.registrar(FabricEntityTypeBuilder.create(SpawnGroup.MISC, TestEntityType::new).build())
			.register("test_entity_type");
	
	/**
	 * TestEntityType
	 */
	class TestEntityType extends ArrowEntity {
		TestEntityType(EntityType<? extends ArrowEntity> entityType, World world) {
			super(entityType, world);
		}
		
		@Override
		public void tick() {
			if (getWorld().isClient && !inGround) getWorld().addParticle(ParticleTypes.INSTANT_EFFECT, getX(), getY(), getZ(), 0, 0, 0);
			super.tick();
		}
	}
}
