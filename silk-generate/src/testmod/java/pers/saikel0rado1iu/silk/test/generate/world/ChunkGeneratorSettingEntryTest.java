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
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import pers.saikel0rado1iu.silk.generate.world.ChunkGeneratorSettingEntry;
import pers.saikel0rado1iu.silk.generate.world.chunk.ChunkGenerationShapeConfig;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;
import pers.saikel0rado1iu.silk.test.generate.Main;

/**
 * Test {@link ChunkGeneratorSettingEntry}
 */
public interface ChunkGeneratorSettingEntryTest extends ChunkGeneratorSettingEntry {
	/**
	 * INSTANCE
	 */
	ChunkGeneratorSettingEntryTest INSTANCE = new ChunkGeneratorSettingEntryTest() {
	};
	/**
	 * test
	 */
	RegistryKey<ChunkGeneratorSettings> TEST = ChunkGeneratorSettingEntry.of(Main.MOD_PASS, "test");
	
	@Override
	default RegistryBuilder.BootstrapFunction<ChunkGeneratorSettings> bootstrap() {
		return registerable -> {
			SilkGenerate.getInstance().logger().error(ChunkGenerationShapeConfig.SURFACE + "");
			SilkGenerate.getInstance().logger().error(ChunkGenerationShapeConfig.NETHER + "");
			SilkGenerate.getInstance().logger().error(ChunkGenerationShapeConfig.END + "");
			SilkGenerate.getInstance().logger().error(ChunkGenerationShapeConfig.CAVES + "");
			SilkGenerate.getInstance().logger().error(ChunkGenerationShapeConfig.FLOATING_ISLANDS + "");
			SilkGenerate.getInstance().logger().error(TEST + "");
		};
	}
}
