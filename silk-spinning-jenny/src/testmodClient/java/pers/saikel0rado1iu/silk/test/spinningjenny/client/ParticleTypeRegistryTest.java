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
import net.minecraft.particle.ParticleType;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.ParticleTypeRegistry;

import java.util.List;
import java.util.function.Supplier;

/**
 * Test {@link ParticleTypeRegistry}
 */
public interface ParticleTypeRegistryTest extends ParticleTypeRegistry {
	/**
	 * 客户端方块注册
	 */
	@SuppressWarnings("unused")
	List<ParticleType<?>> CLIENT = ParticleTypeRegistry.registrar((Supplier<List<ParticleType<?>>>) () -> {
		ParticleFactoryRegistry.getInstance().register(pers.saikel0rado1iu.silk.test.spinningjenny.ParticleTypeRegistryTest.TEST_PARTICLE_TYPE, SpitParticle.Factory::new);
		return List.of(pers.saikel0rado1iu.silk.test.spinningjenny.ParticleTypeRegistryTest.TEST_PARTICLE_TYPE);
	}).register(SilkSpinningJenny.getInstance());
}
