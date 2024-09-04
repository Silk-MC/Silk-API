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

package pers.saikel0rado1iu.silk.test.magiccube.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import pers.saikel0rado1iu.silk.api.spinningjenny.EntityTypeRegistry;

/**
 * 实体类型
 */
public interface EntityTypes extends EntityTypeRegistry {
	/**
	 * tnt_like_entity
	 */
	EntityType<TntLikeEntityTest> TNT_LIKE_ENTITY = EntityTypeRegistry.registrar(() -> FabricEntityTypeBuilder.<TntLikeEntityTest>create(SpawnGroup.MISC, TntLikeEntityTest::new)
					.dimensions(EntityDimensions.fixed(1, 1)).fireImmune().build())
			.register("tnt_like_entity");
}
