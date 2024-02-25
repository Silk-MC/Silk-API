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

package pers.saikel0rado1iu.silk.gen.data;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">用于提供某些标签构建的方便方法</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface SilkTagGenerator {
	@SilkApi
	static void putSpawnGroupEntityInTags(Function<TagKey<EntityType<?>>, FabricTagProvider<EntityType<?>>.FabricTagBuilder> function, TagKey<EntityType<?>> tagKey, SpawnGroup spawnGroup, ModBasicData mod) {
		FabricTagProvider<EntityType<?>>.FabricTagBuilder tagBuilder = function.apply(tagKey);
		for (EntityType<?> entityType : Registries.ENTITY_TYPE) {
			if (mod.getId().equals(Registries.ENTITY_TYPE.getId(entityType).getNamespace()) && entityType.getSpawnGroup().equals(spawnGroup))
				tagBuilder.add(entityType);
		}
	}
}
