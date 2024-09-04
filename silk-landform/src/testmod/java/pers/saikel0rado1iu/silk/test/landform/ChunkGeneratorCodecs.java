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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import pers.saikel0rado1iu.silk.api.spinningjenny.world.gen.chunk.ChunkGeneratorCodecRegistry;

import java.util.List;

import static pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable.NON_VERSION;
import static pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable.VERSION_KEY;
import static pers.saikel0rado1iu.silk.test.landform.Launch.MOD_PASS;

/**
 * ChunkGeneratorCodecs
 */
public interface ChunkGeneratorCodecs extends ChunkGeneratorCodecRegistry {
	/**
	 * test
	 */
	Codec<TestChunkGenerator> TEST = ChunkGeneratorCodecRegistry.registrar(TestChunkGenerator.class, () -> RecordCodecBuilder.create(instance -> instance.group(
							BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource),
							Codecs.createStrictOptionalFieldCodec(FixedBiomeSource.CODEC.listOf(), "fixed_biome_sources", List.of()).forGetter(TestChunkGenerator::additionalBiomeSources),
							ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter(NoiseChunkGenerator::getSettings),
							Codecs.createStrictOptionalFieldCodec(Codec.STRING, VERSION_KEY, NON_VERSION).forGetter(TestChunkGenerator::version))
					.apply(instance, instance.stable(TestChunkGenerator::new))))
			.register(MOD_PASS.ofId("test"));
}
