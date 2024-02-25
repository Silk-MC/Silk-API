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

package pers.saikel0rado1iu.silk.mixin.gen.world.chunk;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.gen.world.chunk.CustomChunkGenerator;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">设置可自定义区块生成器混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
interface CustomChunkGeneratorMixin {
	@Mixin(ChunkStatus.class)
	abstract class GetSeed {
		@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE",
				target = "L net/minecraft/world/chunk/ChunkStatus;register(L java/lang/String;L net/minecraft/world/chunk/ChunkStatus;IZL java/util/EnumSet;L net/minecraft/world/chunk/ChunkStatus$ChunkType;L net/minecraft/world/chunk/ChunkStatus$GenerationTask;L net/minecraft/world/chunk/ChunkStatus$LoadTask;)L net/minecraft/world/chunk/ChunkStatus;",
				ordinal = 0), index = 6)
		private static ChunkStatus.GenerationTask empty(ChunkStatus.GenerationTask generationTask) {
			return (targetStatus, executor, world, generator, structureTemplateManager, lightingProvider, fullChunkConverter, chunks, chunk) -> {
				if (generator instanceof CustomChunkGenerator) CustomChunkGenerator.Data.seed = world.getSeed();
				return generationTask.doWork(targetStatus, executor, world, generator, structureTemplateManager, lightingProvider, fullChunkConverter, chunks, chunk);
			};
		}
	}
	
	@Mixin(Structure.class)
	abstract class RepairStructureGen {
		@Inject(method = "isBiomeValid", at = @At("HEAD"), cancellable = true)
		private static void isBiomeValid(Structure.StructurePosition result, Structure.Context context, CallbackInfoReturnable<Boolean> cir) {
			if (!(context.chunkGenerator() instanceof CustomChunkGenerator generator)) return;
			BlockPos pos = result.position();
			cir.setReturnValue(context.biomePredicate().test(generator.getBiomeSource(pos).getBiome(
					BiomeCoords.fromBlock(pos.getX()), BiomeCoords.fromBlock(pos.getY()), BiomeCoords.fromBlock(pos.getZ()),
					context.noiseConfig().getMultiNoiseSampler())));
		}
	}
	
	@Mixin(NoiseChunkGenerator.class)
	abstract class PopulateBiomes {
		@ModifyArg(method = "populateBiomes(L net/minecraft/world/gen/chunk/Blender;L net/minecraft/world/gen/noise/NoiseConfig;L net/minecraft/world/gen/StructureAccessor;L net/minecraft/world/chunk/Chunk;)V",
				at = @At(value = "INVOKE", target = "L net/minecraft/world/chunk/Chunk;populateBiomes(L net/minecraft/world/biome/source/BiomeSupplier;L net/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;)V"),
				index = 0)
		private BiomeSupplier populateBiomes(BiomeSupplier biomeSupplier) {
			Function<CustomChunkGenerator, BiomeSupplier> supplier = (generator) -> (x, y, z, noise) -> {
				BlockPos pos = new BlockPos(BiomeCoords.toBlock(x), BiomeCoords.toBlock(y), BiomeCoords.toBlock(z));
				if (generator.getBiomeSource(pos) instanceof FixedBiomeSource fixedBiomeSource)
					return fixedBiomeSource.getBiomes().stream().toList().get(0);
				return biomeSupplier.getBiome(x, y, z, noise);
			};
			return (Object) (this) instanceof CustomChunkGenerator silkNoiseChunkGenerator
					? supplier.apply(silkNoiseChunkGenerator)
					: biomeSupplier;
		}
	}
	
	@Mixin(LocateCommand.class)
	abstract class LocateBiome {
		@Redirect(method = "executeLocateBiome", at = @At(value = "INVOKE", target = "L net/minecraft/server/world/ServerWorld;locateBiome(L java/util/function/Predicate;L net/minecraft/util/math/BlockPos;III)L com/mojang/datafixers/util/Pair;"))
		private static Pair<BlockPos, RegistryEntry<Biome>> executeLocateBiome(ServerWorld world, Predicate<RegistryEntry<Biome>> predicate, BlockPos pos, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval) {
			ServerChunkManager chunkManager = world.getChunkManager();
			if (chunkManager.getChunkGenerator() instanceof CustomChunkGenerator generator) {
				return generator.locateBiome(pos, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate,
						chunkManager.getNoiseConfig().getMultiNoiseSampler(), world);
			}
			return world.locateBiome(predicate, pos, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval);
		}
	}
	
	@Mixin(NoiseChunkGenerator.class)
	abstract class PopulateNoise {
		@Redirect(method = "populateNoise(L net/minecraft/world/gen/chunk/Blender;L net/minecraft/world/gen/StructureAccessor;L net/minecraft/world/gen/noise/NoiseConfig;L net/minecraft/world/chunk/Chunk;II)L net/minecraft/world/chunk/Chunk;",
				at = @At(value = "INVOKE", target = "L net/minecraft/world/gen/chunk/ChunkNoiseSampler;sampleBlockState()L net/minecraft/block/BlockState;"))
		private BlockState populateNoise(ChunkNoiseSampler instance) {
			BlockState state = instance.sampleBlockState();
			if (!(((Object) this) instanceof CustomChunkGenerator generator)) return state;
			return generator.setTerrainNoise(new BlockPos(instance.blockX(), instance.blockY(), instance.blockZ()),
					state, instance.estimateSurfaceHeight(instance.blockX(), instance.blockZ()));
		}
		
		
		@Redirect(method = "sampleHeightmap", at = @At(value = "INVOKE", target = "L net/minecraft/world/gen/chunk/ChunkNoiseSampler;sampleBlockState()L net/minecraft/block/BlockState;"))
		private BlockState sampleHeightmap(ChunkNoiseSampler instance) {
			BlockState state = instance.sampleBlockState();
			if (!(((Object) this) instanceof CustomChunkGenerator generator)) return state;
			return generator.setTerrainNoise(new BlockPos(instance.blockX(), instance.blockY(), instance.blockZ()),
					state, instance.estimateSurfaceHeight(instance.blockX(), instance.blockZ()));
		}
	}
}
