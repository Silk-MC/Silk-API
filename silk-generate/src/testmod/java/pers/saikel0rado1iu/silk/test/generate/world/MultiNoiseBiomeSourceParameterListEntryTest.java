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

package pers.saikel0rado1iu.silk.test.generate.world;

import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import pers.saikel0rado1iu.silk.api.generate.world.MultiNoiseBiomeSourceParameterListEntry;
import pers.saikel0rado1iu.silk.api.generate.world.biome.source.MultiNoiseBiomeSourceFunction;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;
import pers.saikel0rado1iu.silk.test.generate.Main;

import java.util.function.Function;

/**
 * Test {@link MultiNoiseBiomeSourceParameterListEntry}
 */
public interface MultiNoiseBiomeSourceParameterListEntryTest extends MultiNoiseBiomeSourceParameterListEntry {
	/**
	 * INSTANCE
	 */
	MultiNoiseBiomeSourceParameterListEntryTest INSTANCE = new MultiNoiseBiomeSourceParameterListEntryTest() {
	};
	/**
	 * test
	 */
	RegistryKey<MultiNoiseBiomeSourceParameterList> TEST = MultiNoiseBiomeSourceParameterListEntry.of(Main.MOD_PASS, "test");
	
	@Override
	default RegistryBuilder.BootstrapFunction<MultiNoiseBiomeSourceParameterList> bootstrap() {
		return registerable -> {
			MultiNoiseBiomeSourceFunction function = new MultiNoiseBiomeSourceFunction() {
				@Override
				public <T> MultiNoiseUtil.Entries<T> apply(Function<RegistryKey<Biome>, T> biomeEntryGetter) {
					return null;
				}
			};
			SilkGenerate.getInstance().logger().error(function + "");
			SilkGenerate.getInstance().logger().error(TEST + "");
		};
	}
}
