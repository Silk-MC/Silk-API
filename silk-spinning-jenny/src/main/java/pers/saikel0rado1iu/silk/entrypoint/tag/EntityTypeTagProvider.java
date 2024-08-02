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

package pers.saikel0rado1iu.silk.entrypoint.tag;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.spinningjenny.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

/**
 * <h2 style="color:FFC800">实体类型标签提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public final class EntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
	/**
	 * @param output           数据输出
	 * @param registriesFuture 注册管理器
	 */
	public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(output, registriesFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		FabricTagProvider<EntityType<?>>.FabricTagBuilder tagBuilder = getOrCreateTagBuilder(EntityTypeTags.MONSTERS);
		for (EntityType<?> entityType : Registries.ENTITY_TYPE) if (entityType.getSpawnGroup().equals(SpawnGroup.MONSTER)) tagBuilder.add(entityType);
	}
}
