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

package pers.saikel0rado1iu.silk.api.event.modplus;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import pers.saikel0rado1iu.silk.api.landform.ChunkStorageData;
import pers.saikel0rado1iu.silk.api.landform.gen.chunk.ChunkGeneratorUpgradable;

import java.util.Map;

/**
 * <h2 style="color:FFC800">修改 {@link ChunkGeneratorUpgradable} 事件</h2>
 * 用于动态修改 {@link ChunkGeneratorUpgradable} 中生成规则的事件<br>
 * 需要模组作者自主在 {@link ChunkGeneratorUpgradable} 中实现事件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface ModifyChunkGeneratorUpgradableEvents {
	/**
	 * 此事件将 {@link ChunkGeneratorUpgradable} 修改方法 {@link ChunkGeneratorUpgradable#version()}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link String}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link String}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link String}<br>
	 */
	Event<ModifyVersion> MODIFY_VERSION = EventFactory.createArrayBacked(ModifyVersion.class, listeners -> (upgradable, version) -> {
		String ver = version;
		for (ModifyVersion event : listeners) {
			Map.Entry<ActionResult, String> entry = event.version(upgradable, ver);
			if (entry.getKey() != ActionResult.PASS) return entry;
			ver = entry.getValue();
		}
		record Entry(String version) implements Map.Entry<ActionResult, String> {
			@Override
			public ActionResult getKey() {
				return ActionResult.PASS;
			}
			
			@Override
			public String getValue() {
				return version;
			}
			
			@Override
			public String setValue(String value) {
				return value;
			}
		}
		return new Entry(ver);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorUpgradable} 修改方法 {@link ChunkGeneratorUpgradable#compare(String)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link Integer}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link Integer}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link Integer}<br>
	 */
	Event<ModifyCompare> MODIFY_COMPARE = EventFactory.createArrayBacked(ModifyCompare.class, listeners -> (upgradable, compare, savedVersion) -> {
		int result = compare;
		for (ModifyCompare event : listeners) {
			Map.Entry<ActionResult, Integer> entry = event.compare(upgradable, result, savedVersion);
			if (entry.getKey() != ActionResult.PASS) return entry;
			result = entry.getValue();
		}
		record Entry(int compare) implements Map.Entry<ActionResult, Integer> {
			@Override
			public ActionResult getKey() {
				return ActionResult.PASS;
			}
			
			@Override
			public Integer getValue() {
				return compare;
			}
			
			@Override
			public Integer setValue(Integer value) {
				return value;
			}
		}
		return new Entry(result);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorUpgradable} 修改方法 {@link ChunkGeneratorUpgradable#needRefresh(BlockPos, ChunkStorageData, String)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link Boolean}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link Boolean}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link Boolean}<br>
	 */
	Event<ModifyNeedRefresh> MODIFY_NEED_REFRESH = EventFactory.createArrayBacked(ModifyNeedRefresh.class, listeners -> (upgradable, needRefresh, pos, chunk, version) -> {
		Boolean result = needRefresh;
		for (ModifyNeedRefresh event : listeners) {
			Map.Entry<ActionResult, Boolean> entry = event.needRefresh(upgradable, result, pos, chunk, version);
			if (entry.getKey() != ActionResult.PASS) return entry;
			result = entry.getValue();
		}
		record Entry(boolean needRefresh) implements Map.Entry<ActionResult, Boolean> {
			@Override
			public ActionResult getKey() {
				return ActionResult.PASS;
			}
			
			@Override
			public Boolean getValue() {
				return needRefresh;
			}
			
			@Override
			public Boolean setValue(Boolean value) {
				return value;
			}
		}
		return new Entry(result);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorUpgradable} 修改方法 {@link ChunkGeneratorUpgradable#needUpgrade(BlockPos, ChunkStorageData, String)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link Boolean}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link Boolean}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link Boolean}<br>
	 */
	Event<ModifyNeedUpgrade> MODIFY_NEED_UPGRADE = EventFactory.createArrayBacked(ModifyNeedUpgrade.class, listeners -> (upgradable, needUpgrade, pos, chunk, version) -> {
		Boolean result = needUpgrade;
		for (ModifyNeedUpgrade event : listeners) {
			Map.Entry<ActionResult, Boolean> entry = event.needUpgrade(upgradable, result, pos, chunk, version);
			if (entry.getKey() != ActionResult.PASS) return entry;
			result = entry.getValue();
		}
		record Entry(boolean needUpgrade) implements Map.Entry<ActionResult, Boolean> {
			@Override
			public ActionResult getKey() {
				return ActionResult.PASS;
			}
			
			@Override
			public Boolean getValue() {
				return needUpgrade;
			}
			
			@Override
			public Boolean setValue(Boolean value) {
				return value;
			}
		}
		return new Entry(result);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorUpgradable} 修改方法 {@link ChunkGeneratorUpgradable#needDowngrade(BlockPos, ChunkStorageData, String)}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link Boolean}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link Boolean}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link Boolean}<br>
	 */
	Event<ModifyNeedDowngrade> MODIFY_NEED_DOWNGRADE = EventFactory.createArrayBacked(ModifyNeedDowngrade.class, listeners -> (upgradable, needDowngrade, pos, chunk, version) -> {
		Boolean result = needDowngrade;
		for (ModifyNeedDowngrade event : listeners) {
			Map.Entry<ActionResult, Boolean> entry = event.needDowngrade(upgradable, result, pos, chunk, version);
			if (entry.getKey() != ActionResult.PASS) return entry;
			result = entry.getValue();
		}
		record Entry(boolean needDowngrade) implements Map.Entry<ActionResult, Boolean> {
			@Override
			public ActionResult getKey() {
				return ActionResult.PASS;
			}
			
			@Override
			public Boolean getValue() {
				return needDowngrade;
			}
			
			@Override
			public Boolean setValue(Boolean value) {
				return value;
			}
		}
		return new Entry(result);
	});
	
	/**
	 * 此事件将 {@link ChunkGeneratorUpgradable} 修改方法 {@link ChunkGeneratorUpgradable#getCodec()}<br>
	 * <br>
	 * - {@link ActionResult#SUCCESS} 退出原始方法实现，模组作者应返回对应的 {@link Codec}<br>
	 * - {@link ActionResult#FAIL} 回落到原始方法实现，模组作者应返回原始方法的 {@link Codec}<br>
	 * - {@link ActionResult#PASS} 回落到原始方法实现，如果没有其他的监听器了，模组作者应返回原始方法的 {@link Codec}<br>
	 */
	Event<ModifyGetCodec> MODIFY_GET_CODEC = EventFactory.createArrayBacked(ModifyGetCodec.class, listeners -> (upgradable, codec) -> {
		Codec<? extends ChunkGenerator> c = codec;
		for (ModifyGetCodec event : listeners) {
			Map.Entry<ActionResult, Codec<? extends ChunkGenerator>> entry = event.getCodec(upgradable, c);
			if (entry.getKey() != ActionResult.PASS) return entry;
			c = entry.getValue();
		}
		record Entry(Codec<? extends ChunkGenerator> codec) implements Map.Entry<ActionResult, Codec<? extends ChunkGenerator>> {
			@Override
			public ActionResult getKey() {
				return ActionResult.PASS;
			}
			
			@Override
			public Codec<? extends ChunkGenerator> getValue() {
				return codec;
			}
			
			@Override
			public Codec<? extends ChunkGenerator> setValue(Codec<? extends ChunkGenerator> value) {
				return value;
			}
		}
		return new Entry(c);
	});
	
	/**
	 * 修改版本
	 */
	@FunctionalInterface
	interface ModifyVersion {
		Map.Entry<ActionResult, String> version(ChunkGeneratorUpgradable upgradable, String version);
	}
	
	/**
	 * 修改版本比较
	 */
	@FunctionalInterface
	interface ModifyCompare {
		Map.Entry<ActionResult, Integer> compare(ChunkGeneratorUpgradable upgradable, int compare, String savedVersion);
	}
	
	/**
	 * 修改是否需要刷新
	 */
	@FunctionalInterface
	interface ModifyNeedRefresh {
		Map.Entry<ActionResult, Boolean> needRefresh(ChunkGeneratorUpgradable upgradable, boolean needRefresh, BlockPos pos, ChunkStorageData chunk, String version);
	}
	
	/**
	 * 修改是否需要升级
	 */
	@FunctionalInterface
	interface ModifyNeedUpgrade {
		Map.Entry<ActionResult, Boolean> needUpgrade(ChunkGeneratorUpgradable upgradable, boolean needUpgrade, BlockPos pos, ChunkStorageData chunk, String version);
	}
	
	/**
	 * 修改是否需要降级
	 */
	@FunctionalInterface
	interface ModifyNeedDowngrade {
		Map.Entry<ActionResult, Boolean> needDowngrade(ChunkGeneratorUpgradable upgradable, boolean needDowngrade, BlockPos pos, ChunkStorageData chunk, String version);
	}
	
	/**
	 * 修改获取的解编码器
	 */
	@FunctionalInterface
	interface ModifyGetCodec {
		Map.Entry<ActionResult, Codec<? extends ChunkGenerator>> getCodec(ChunkGeneratorUpgradable upgradable, Codec<? extends ChunkGenerator> codec);
	}
}
