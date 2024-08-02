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

package pers.saikel0rado1iu.silk.api.landform.gen.chunk;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <h2 style="color:FFC800">被修改的区块生成器</h2>
 * 继承了 {@link NoiseChunkGenerator} 并实现了 {@link ChunkGeneratorCustom} 与 {@link ChunkGeneratorUpgradable} 的区块生成器，
 * 此区块生成器基于原版并在此基础上进行修改且可以进行升级
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class ModifiedChunkGenerator extends DefaultChunkGenerator implements ChunkGeneratorCustom, ChunkGeneratorUpgradable {
	private final List<FixedBiomeSource> additionalBiomeSources;
	private final String version;
	
	protected ModifiedChunkGenerator(BiomeSource biomeSource, List<FixedBiomeSource> additionalBiomeSources, RegistryEntry<ChunkGeneratorSettings> settings, String version) {
		super(biomeSource, settings);
		this.additionalBiomeSources = additionalBiomeSources;
		this.version = version;
	}
	
	protected static boolean isAdditionalBiomeSources(Predicate<RegistryEntry<Biome>> predicate, List<FixedBiomeSource> additionalBiomeSources) {
		for (FixedBiomeSource source : additionalBiomeSources) {
			for (RegistryEntry<Biome> entry : source.getBiomes()) {
				if (predicate.test(entry)) return false;
			}
		}
		return true;
	}
	
	protected static Optional<Pair<BlockPos, RegistryEntry<Biome>>> getLocateBiomePair(ModifiedChunkGenerator generator, BlockPos pos, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, ServerWorld world) {
		for (int y = world.getHeight(); y > world.getDimension().minY(); y--) {
			if (!world.getBlockState(new BlockPos(pos.getX(), y, pos.getZ())).isAir() && generator.getBiomeSource(pos) != generator.biomeSource
					&& !generator.getBiomeSource(pos).getBiomes().stream().filter(predicate).collect(Collectors.toUnmodifiableSet()).isEmpty()) {
				return Optional.of(Pair.of(new BlockPos(pos.getX(), y + 1, pos.getZ()), generator.getBiomeSource(pos).getBiome(pos.getX(), y + 1, pos.getZ(), noiseSampler)));
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Optional<Pair<BlockPos, RegistryEntry<Biome>>> locateBiome(BlockPos origin, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval, Predicate<RegistryEntry<Biome>> predicate, MultiNoiseUtil.MultiNoiseSampler noiseSampler, ServerWorld world) {
		if (isAdditionalBiomeSources(predicate, additionalBiomeSources)) {
			return ChunkGeneratorCustom.super.locateBiome(origin, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate, noiseSampler, world);
		}
		List<Integer> xs = new ArrayList<>();
		List<Integer> zs = new ArrayList<>();
		for (int radian = 0; radian < 360; radian++) {
			for (int count = 0; count < radius; count += horizontalBlockCheckInterval) {
				xs.add((int) (origin.getX() + count * Math.cos(radian)));
				zs.add((int) (origin.getZ() + count * Math.sin(radian)));
			}
		}
		for (int x : xs) for (int z : zs) return getLocateBiomePair(this, new BlockPos(x, 0, z), predicate, noiseSampler, world);
		return Optional.empty();
	}
	
	/**
	 * 获取附加生物群系源
	 *
	 * @return 附加生物群系源
	 */
	public List<FixedBiomeSource> additionalBiomeSources() {
		return additionalBiomeSources;
	}
	
	@Override
	public String version() {
		return version;
	}
	
	@Override
	public abstract Codec<? extends ChunkGenerator> getCodec();
}
