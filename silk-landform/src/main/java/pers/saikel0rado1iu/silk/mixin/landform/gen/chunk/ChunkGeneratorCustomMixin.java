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

package pers.saikel0rado1iu.silk.mixin.landform.gen.chunk;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.command.LocateCommand;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeCoords;
import net.minecraft.world.biome.source.BiomeSupplier;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkNoiseSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorCustom;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">{@link ChunkGeneratorCustom} 混入</h2>
 * 设置可自定义区块生成器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
interface ChunkGeneratorCustomMixin {
	/**
	 * 获取地图种子
	 */
	interface GetSeed {
		@Mixin(ChunkStatus.class)
		abstract class Chunk {
			@ModifyArg(method = "<clinit>", at = @At(value = "INVOKE",
					target = "L net/minecraft/world/chunk/ChunkStatus;register(L java/lang/String;L net/minecraft/world/chunk/ChunkStatus;IZL java/util/EnumSet;L net/minecraft/world/chunk/ChunkStatus$ChunkType;L net/minecraft/world/chunk/ChunkStatus$GenerationTask;L net/minecraft/world/chunk/ChunkStatus$LoadTask;)L net/minecraft/world/chunk/ChunkStatus;",
					ordinal = 0), index = 6)
			private static ChunkStatus.GenerationTask empty(ChunkStatus.GenerationTask generationTask) {
				return (targetStatus, executor, world, generator, structureTemplateManager, lightingProvider, fullChunkConverter, chunks, chunk) -> {
					if (generator instanceof ChunkGeneratorCustom chunkGeneratorCustom) {
						ChunkGeneratorCustom.SEED_MAP.put(chunkGeneratorCustom, world.getSeed());
					}
					return generationTask.doWork(targetStatus, executor, world, generator, structureTemplateManager, lightingProvider, fullChunkConverter, chunks, chunk);
				};
			}
		}
		
		@Mixin(ServerWorld.class)
		abstract class Server extends World implements StructureWorldAccess {
			private Server(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
				super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
			}
			
			@Inject(method = "<init>", at = @At("TAIL"))
			private void init(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<?> worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List<?> spawners, boolean shouldTickTime, RandomSequencesState randomSequencesState, CallbackInfo ci) {
				ChunkGenerator generator = ((ServerWorld) (Object) this).getChunkManager().getChunkGenerator();
				if (generator instanceof ChunkGeneratorCustom chunkGeneratorCustom) {
					ChunkGeneratorCustom.SEED_MAP.put(chunkGeneratorCustom, seed);
				}
			}
		}
	}
	
	/**
	 * 修复结构生成
	 */
	@Mixin(Structure.class)
	abstract class RepairStructureGen {
		@Inject(method = "isBiomeValid", at = @At("HEAD"), cancellable = true)
		private static void isBiomeValid(Structure.StructurePosition result, Structure.Context context, CallbackInfoReturnable<Boolean> cir) {
			if (!(context.chunkGenerator() instanceof ChunkGeneratorCustom generator)) return;
			BlockPos pos = result.position();
			cir.setReturnValue(context.biomePredicate().test(generator.getBiomeSource(pos).getBiome(
					BiomeCoords.fromBlock(pos.getX()), BiomeCoords.fromBlock(pos.getY()), BiomeCoords.fromBlock(pos.getZ()),
					context.noiseConfig().getMultiNoiseSampler())));
		}
	}
	
	/**
	 * 填充生物群系
	 */
	@Mixin(NoiseChunkGenerator.class)
	abstract class PopulateBiomes {
		@ModifyArg(method = "populateBiomes(L net/minecraft/world/gen/chunk/Blender;L net/minecraft/world/gen/noise/NoiseConfig;L net/minecraft/world/gen/StructureAccessor;L net/minecraft/world/chunk/Chunk;)V",
				at = @At(value = "INVOKE", target = "L net/minecraft/world/chunk/Chunk;populateBiomes(L net/minecraft/world/biome/source/BiomeSupplier;L net/minecraft/world/biome/source/util/MultiNoiseUtil$MultiNoiseSampler;)V"),
				index = 0)
		private BiomeSupplier populateBiomes(BiomeSupplier biomeSupplier) {
			Function<ChunkGeneratorCustom, BiomeSupplier> supplier = (generator) -> (x, y, z, noise) -> {
				BlockPos pos = new BlockPos(BiomeCoords.toBlock(x), BiomeCoords.toBlock(y), BiomeCoords.toBlock(z));
				if (generator.getBiomeSource(pos) instanceof FixedBiomeSource fixedBiomeSource)
					return fixedBiomeSource.getBiomes().stream().toList().get(0);
				return biomeSupplier.getBiome(x, y, z, noise);
			};
			return (Object) (this) instanceof ChunkGeneratorCustom silkNoiseChunkGenerator
					? supplier.apply(silkNoiseChunkGenerator)
					: biomeSupplier;
		}
	}
	
	/**
	 * 定位生物群系
	 */
	@Mixin(LocateCommand.class)
	abstract class LocateBiome {
		@Redirect(method = "executeLocateBiome", at = @At(value = "INVOKE", target = "L net/minecraft/server/world/ServerWorld;locateBiome(L java/util/function/Predicate;L net/minecraft/util/math/BlockPos;III)L com/mojang/datafixers/util/Pair;"))
		private static Pair<BlockPos, RegistryEntry<Biome>> executeLocateBiome(ServerWorld world, Predicate<RegistryEntry<Biome>> predicate, BlockPos pos, int radius, int horizontalBlockCheckInterval, int verticalBlockCheckInterval) {
			ServerChunkManager chunkManager = world.getChunkManager();
			if (chunkManager.getChunkGenerator() instanceof ChunkGeneratorCustom generator) {
				return generator.locateBiome(pos, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval, predicate,
						chunkManager.getNoiseConfig().getMultiNoiseSampler(), world).orElse(null);
			}
			return world.locateBiome(predicate, pos, radius, horizontalBlockCheckInterval, verticalBlockCheckInterval);
		}
	}
	
	/**
	 * 填充地形噪声
	 */
	@Mixin(NoiseChunkGenerator.class)
	abstract class PopulateNoise {
		@Redirect(method = "populateNoise(L net/minecraft/world/gen/chunk/Blender;L net/minecraft/world/gen/StructureAccessor;L net/minecraft/world/gen/noise/NoiseConfig;L net/minecraft/world/chunk/Chunk;II)L net/minecraft/world/chunk/Chunk;",
				at = @At(value = "INVOKE", target = "L net/minecraft/world/gen/chunk/ChunkNoiseSampler;sampleBlockState()L net/minecraft/block/BlockState;"))
		private BlockState populateNoise(ChunkNoiseSampler instance) {
			BlockState state = instance.sampleBlockState();
			if (!(((Object) this) instanceof ChunkGeneratorCustom generator)) return state;
			return generator.getTerrainNoise(new BlockPos(instance.blockX(), instance.blockY(), instance.blockZ()),
					Optional.ofNullable(state), instance.estimateSurfaceHeight(instance.blockX(), instance.blockZ())).orElse(null);
		}
		
		@Redirect(method = "sampleHeightmap", at = @At(value = "INVOKE", target = "L net/minecraft/world/gen/chunk/ChunkNoiseSampler;sampleBlockState()L net/minecraft/block/BlockState;"))
		private BlockState sampleHeightmap(ChunkNoiseSampler instance) {
			BlockState state = instance.sampleBlockState();
			if (!(((Object) this) instanceof ChunkGeneratorCustom generator)) return state;
			return generator.getTerrainNoise(new BlockPos(instance.blockX(), instance.blockY(), instance.blockZ()),
					Optional.ofNullable(state), instance.estimateSurfaceHeight(instance.blockX(), instance.blockZ())).orElse(null);
		}
	}
}
