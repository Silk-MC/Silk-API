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

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import pers.saikel0rado1iu.silk.impl.SilkSpinningJenny;
import pers.saikel0rado1iu.silk.spinningjenny.EntityTypeRegistry;

import java.util.List;
import java.util.function.Supplier;

import static pers.saikel0rado1iu.silk.test.spore.EntityTypes.FIXED_DAMAGE_TEST;

/**
 * EntityTypes
 */
public interface EntityTypes extends EntityTypeRegistry {
	/**
	 * 客户端注册
	 */
	@SuppressWarnings("unused")
	List<EntityType<?>> CLIENT = EntityTypeRegistry.registrar((Supplier<List<EntityType<?>>>) () -> {
		EntityRendererRegistry.register(FIXED_DAMAGE_TEST, FixedDamageTestRender::new);
		return List.of(FIXED_DAMAGE_TEST);
	}).register(SilkSpinningJenny.getInstance());
}
