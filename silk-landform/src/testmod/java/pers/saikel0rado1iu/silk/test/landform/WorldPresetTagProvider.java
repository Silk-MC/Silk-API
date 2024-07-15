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

package pers.saikel0rado1iu.silk.test.landform;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.tag.TagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.WorldPresetTags;
import net.minecraft.world.gen.WorldPreset;

import java.util.concurrent.CompletableFuture;

/**
 * WorldPresetTagProvider
 */
public final class WorldPresetTagProvider extends TagProvider<WorldPreset> {
	WorldPresetTagProvider(DataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookupFuture) {
		super(output, RegistryKeys.WORLD_PRESET, registryLookupFuture);
	}
	
	@Override
	protected void configure(RegistryWrapper.WrapperLookup lookup) {
		getOrCreateTagBuilder(WorldPresetTags.NORMAL).addOptional(WorldPresets.TEST.getValue());
	}
}
