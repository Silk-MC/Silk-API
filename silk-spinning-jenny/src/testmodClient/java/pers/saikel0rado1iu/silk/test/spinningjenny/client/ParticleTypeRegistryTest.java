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

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.SpitParticle;
import pers.saikel0rado1iu.silk.spinningjenny.ParticleTypeRegistry;

import static pers.saikel0rado1iu.silk.test.spinningjenny.ParticleTypeRegistryTest.TEST_PARTICLE_TYPE;

/**
 * Test {@link ParticleTypeRegistry}
 */
public abstract class ParticleTypeRegistryTest implements ParticleTypeRegistry {
	static {
		ParticleTypeRegistry.registrar((Runnable) () -> ParticleFactoryRegistry.getInstance().register(TEST_PARTICLE_TYPE, SpitParticle.Factory::new)).register(TEST_PARTICLE_TYPE);
	}
	
	private ParticleTypeRegistryTest() {
	}
}
