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

package pers.saikel0rado1iu.silk.test.generate.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryWrapper;
import pers.saikel0rado1iu.silk.api.generate.data.TagGenUtil;
import pers.saikel0rado1iu.silk.api.spinningjenny.tag.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

/**
 * Test {@link TagGenUtil}
 */
public final class TagGenUtilTest extends FabricTagProvider.EntityTypeTagProvider {
	/**
	 * @param output            数据输出
	 * @param completableFuture 后续处理
	 */
	public TagGenUtilTest(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
		super(output, completableFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup arg) {
		TagGenUtil.addSpawnGroup(this::getOrCreateTagBuilder, EntityTypeTags.MONSTERS, SpawnGroup.MONSTER);
	}
}
