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

package pers.saikel0rado1iu.silk.api.client.event.landform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.api.client.landform.ClientUpgradableWorldManager;
import pers.saikel0rado1iu.silk.api.client.landform.UpgradableLevelSummary;
import pers.saikel0rado1iu.silk.api.landform.UpgradableWorldData;
import pers.saikel0rado1iu.silk.api.landform.UpgradableWorldInfo;
import pers.saikel0rado1iu.silk.api.landform.UpgradableWorldManager;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">客户端可升级世界管理器回调</h2>
 * 用于进行和客户端可升级世界管理器有关的各种事件注册<br>
 * 此接口为内部开发接口，不应为模组开发者使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@ApiStatus.Internal
@FunctionalInterface
public interface ClientWorldUpgradeManagerCallback extends Supplier<ClientUpgradableWorldManager<?>> {
	/**
	 * 使用关卡列表获取资源管理器的事件
	 */
	Event<Consumer<LevelStorage.LevelList>> LEVEL_LIST_START_EVENT = EventFactory.createArrayBacked(Consumer.class, listeners -> (levels) -> {
		for (Consumer<LevelStorage.LevelList> event : listeners) event.accept(levels);
	});
	/**
	 * 使用关卡列表进行资源管理器操作的事件
	 */
	Event<BiFunction<Dynamic<?>, LevelSummary, LevelSummary>> LEVEL_LIST_OPERATION_EVENT = EventFactory.createArrayBacked(BiFunction.class, listeners -> (dynamic, summary) -> {
		for (BiFunction<Dynamic<?>, LevelSummary, LevelSummary> event : listeners) {
			LevelSummary levelSummary = event.apply(dynamic, summary);
			if (levelSummary != summary) return levelSummary;
		}
		return summary;
	});
	/**
	 * 使用关卡列表进行可升级世界管理器操作的事件
	 */
	Event<Supplier<ClientUpgradableWorldManager<?>>> LEVEL_LIST_EVENT = EventFactory.createArrayBacked(Supplier.class, listeners -> () -> {
		for (Supplier<ClientUpgradableWorldManager<?>> event : listeners) {
			ClientUpgradableWorldManager<?> manager = event.get();
			LEVEL_LIST_START_EVENT.register(manager::start);
			LEVEL_LIST_OPERATION_EVENT.register((dynamic, summary) -> {
				DynamicRegistryManager registryManager = UpgradableWorldManager.registryManager();
				UpgradableWorldData<?> upgradableWorldData = manager.upgradableWorldData();
				ChunkGeneratorUpgradable generator = upgradableWorldData.getGenerator(registryManager);
				Dynamic<?> worldGenSettings = dynamic.get("WorldGenSettings").orElseEmptyMap();
				Dynamic<?> chunkGenerator = worldGenSettings.get("dimensions").get(upgradableWorldData.dimension().getValue().toString()).get("generator").orElseEmptyMap();
				if (chunkGenerator.equals(chunkGenerator.emptyMap())) return summary;
				String version = chunkGenerator.get(ChunkGeneratorUpgradable.VERSION_KEY).orElseEmptyMap().asString(ChunkGeneratorUpgradable.NON_VERSION);
				Identifier generatorId = Identifier.tryParse(chunkGenerator.get("type").orElseEmptyMap().asString("foo:bar"));
				if (generatorId == null || !generatorId.equals(Registries.CHUNK_GENERATOR.getId(generator.getCodec()))) return summary;
				return new UpgradableLevelSummary<>(summary.getLevelInfo(), summary.getVersionInfo(),
						new UpgradableWorldInfo<>(upgradableWorldData.modPass(), manager.upgradableWorldData(), summary.getLevelInfo().getLevelName(), generatorId, version, generator.compare(version)),
						summary.getName(), summary.requiresConversion(), summary.isLocked(), summary.isExperimental(), summary.getIconPath());
			});
		}
		return null;
	});
	/**
	 * 使用关卡存储器会话获取资源管理器的事件
	 */
	Event<Consumer<LevelStorage.Session>> SESSION_START_EVENT = EventFactory.createArrayBacked(Consumer.class, listeners -> (session) -> {
		for (Consumer<LevelStorage.Session> event : listeners) event.accept(session);
	});
	/**
	 * 使用关卡存储器会话进行资源管理器操作的事件
	 */
	Event<Function<NbtCompound, NbtCompound>> SESSION_OPERATION_EVENT = EventFactory.createArrayBacked(Function.class, listeners -> (nbt) -> {
		for (Function<NbtCompound, NbtCompound> event : listeners) {
			NbtCompound nbtCompound = event.apply(nbt);
			if (!nbtCompound.equals(nbt)) return nbtCompound;
		}
		return nbt;
	});
	/**
	 * 使用关卡存储器会话进行可升级世界管理器操作的事件
	 */
	Event<Supplier<ClientUpgradableWorldManager<?>>> SESSION_EVENT = EventFactory.createArrayBacked(Supplier.class, listeners -> () -> {
		for (Supplier<ClientUpgradableWorldManager<?>> event : listeners) {
			ClientUpgradableWorldManager<?> manager = event.get();
			SESSION_START_EVENT.register(session -> {
				manager.start(session);
				UpgradableWorldManager.registryManager();
			});
			SESSION_OPERATION_EVENT.register(nbt -> {
				UpgradableWorldData<?> upgradableWorldData = manager.upgradableWorldData();
				if (!UpgradableWorldManager.haveRegistryManager()) return nbt;
				DynamicRegistryManager registryManager = UpgradableWorldManager.registryManager();
				ChunkGenerator chunkGenerator = upgradableWorldData.getGenerator(registryManager);
				ChunkGeneratorUpgradable chunkGeneratorUpgradable = (ChunkGeneratorUpgradable) chunkGenerator;
				NbtCompound dimensions = nbt.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions");
				NbtCompound dimension;
				if ((dimension = dimensions.getCompound(upgradableWorldData.dimension().getValue().toString())).isEmpty()) return nbt;
				Codec<? extends ChunkGenerator> codec = chunkGeneratorUpgradable.getCodec();
				String generatorId = String.valueOf(Registries.CHUNK_GENERATOR.getId(codec));
				if (!dimension.getCompound("generator").getString("type").equals(generatorId)) return nbt;
				@SuppressWarnings("unchecked")
				NbtCompound nbtCompound = (NbtCompound) Util.getResult(((Codec<ChunkGenerator>) codec).encodeStart(RegistryOps.of(NbtOps.INSTANCE, registryManager), chunkGenerator), IllegalStateException::new);
				nbtCompound.putString("type", generatorId);
				dimension.put("generator", nbtCompound);
				return nbt;
			});
		}
		return null;
	});
	/**
	 * 事件
	 */
	Event<ClientWorldUpgradeManagerCallback> EVENT = EventFactory.createArrayBacked(ClientWorldUpgradeManagerCallback.class, listeners -> () -> {
		for (ClientWorldUpgradeManagerCallback event : listeners) {
			ClientUpgradableWorldManager<?> manager = event.get();
			LEVEL_LIST_EVENT.register(() -> manager);
			SESSION_EVENT.register(() -> manager);
		}
		LEVEL_LIST_EVENT.invoker().get();
		SESSION_EVENT.invoker().get();
		return null;
	});
}
