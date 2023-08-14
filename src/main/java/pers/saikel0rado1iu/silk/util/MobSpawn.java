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

package pers.saikel0rado1iu.silk.util;

import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.List;
import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">控制生物的特殊生成方式</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface MobSpawn {
	/**
	 * 如果不在生物群系则只在夜晚生成
	 *
	 * @param world    世界访问
	 * @param entity   生成实体
	 * @param biomeTag 生物群系标签
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifNotBiomeOnlySpawnInNight(WorldAccess world, Entity entity, TagKey<Biome> biomeTag) {
		if (entity.getEntityWorld().isDay())
			return world.getBiome(entity.getBlockPos()).isIn(biomeTag);
		else return true;
	}
	
	/**
	 * 是否只在夜晚生成
	 *
	 * @param entity 生成实体
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean onlySpawnInNight(Entity entity) {
		return entity.getEntityWorld().isNight();
	}
	
	/**
	 * 是否只在白天生成
	 *
	 * @param entity 生成实体
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean onlySpawnInDay(Entity entity) {
		return entity.getEntityWorld().isDay();
	}
	
	/**
	 * 如果不在生物群系则只在夜晚生成
	 *
	 * @param world     世界访问
	 * @param entity    生成实体
	 * @param biomeList 生物群系 ID 判断列表
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifNotBiomeOnlySpawnInNight(WorldAccess world, Entity entity, List<Identifier> biomeList) {
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (entity.getEntityWorld().isDay())
				return biomeList.contains(spawnBiomeKey.get().getValue());
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果不在生物群系则只在白天生成
	 *
	 * @param world     世界访问
	 * @param entity    生成实体
	 * @param biomeList 生物群系 ID 判断列表
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifNotBiomeOnlySpawnInDay(WorldAccess world, Entity entity, List<Identifier> biomeList) {
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (entity.getEntityWorld().isNight())
				return biomeList.contains(spawnBiomeKey.get().getValue());
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果不在生物群系则只在白天生成
	 *
	 * @param world    世界访问
	 * @param entity   生成实体
	 * @param biomeTag 生物群系标签
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifNotBiomeOnlySpawnInDay(WorldAccess world, Entity entity, TagKey<Biome> biomeTag) {
		if (entity.getEntityWorld().isNight())
			return world.getBiome(entity.getBlockPos()).isIn(biomeTag);
		else return true;
	}
	
	/**
	 * 如果在生物群系则只在夜晚生成
	 *
	 * @param world     世界访问
	 * @param entity    生成实体
	 * @param biomeList 生物群系 ID 判断列表
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifInBiomeOnlySpawnInNight(WorldAccess world, Entity entity, List<Identifier> biomeList) {
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (entity.getEntityWorld().isDay())
				return !biomeList.contains(spawnBiomeKey.get().getValue());
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果在生物群系则只在夜晚生成
	 *
	 * @param world    世界访问
	 * @param entity   生成实体
	 * @param biomeTag 生物群系标签
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifInBiomeOnlySpawnInNight(WorldAccess world, Entity entity, TagKey<Biome> biomeTag) {
		if (entity.getEntityWorld().isDay())
			return !world.getBiome(entity.getBlockPos()).isIn(biomeTag);
		else return true;
	}
	
	/**
	 * 如果在生物群系则只在白天生成
	 *
	 * @param world     世界访问
	 * @param entity    生成实体
	 * @param biomeList 生物群系 ID 判断列表
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifInBiomeOnlySpawnInDay(WorldAccess world, Entity entity, List<Identifier> biomeList) {
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (entity.getEntityWorld().isNight())
				return !biomeList.contains(spawnBiomeKey.get().getValue());
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果在生物群系则只在白天生成
	 *
	 * @param world    世界访问
	 * @param entity   生成实体
	 * @param biomeTag 生物群系标签
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifInBiomeOnlySpawnInDay(WorldAccess world, Entity entity, TagKey<Biome> biomeTag) {
		if (entity.getEntityWorld().isNight())
			return !world.getBiome(entity.getBlockPos()).isIn(biomeTag);
		else return true;
	}
	
	/**
	 * 是否只在特殊时间段生成
	 *
	 * @param entity  生成实体
	 * @param minTime 最小时间，游戏时间刻
	 * @param maxTime 最大时间，游戏时间刻
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean onlySpawnInTime(Entity entity, int minTime, int maxTime) {
		long time = entity.getEntityWorld().getLunarTime();
		return time > minTime || time < maxTime;
	}
	
	/**
	 * 如果不在生物群系则只在特殊时间段生成
	 *
	 * @param world     世界访问
	 * @param entity    生成实体
	 * @param biomeList 生物群系 ID 判断列表
	 * @param minTime   最小时间，游戏时间刻
	 * @param maxTime   最大时间，游戏时间刻
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifNotBiomeOnlySpawnInTime(WorldAccess world, Entity entity, List<Identifier> biomeList, int minTime, int maxTime) {
		long time = entity.getEntityWorld().getLunarTime();
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (!(time > minTime || time < maxTime))
				return biomeList.contains(spawnBiomeKey.get().getValue());
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果不在生物群系则只在特殊时间段生成
	 *
	 * @param world    世界访问
	 * @param entity   生成实体
	 * @param biomeTag 生物群系标签
	 * @param minTime  最小时间，游戏时间刻
	 * @param maxTime  最大时间，游戏时间刻
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifNotBiomeOnlySpawnInTime(WorldAccess world, Entity entity, TagKey<Biome> biomeTag, int minTime, int maxTime) {
		long time = entity.getEntityWorld().getLunarTime();
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (!(time > minTime || time < maxTime))
				return world.getBiome(entity.getBlockPos()).isIn(biomeTag);
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果在生物群系则只在特殊时间段生成
	 *
	 * @param world     世界访问
	 * @param entity    生成实体
	 * @param biomeList 生物群系 ID 判断列表
	 * @param minTime   最小时间，游戏时间刻
	 * @param maxTime   最大时间，游戏时间刻
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifInBiomeOnlySpawnInTime(WorldAccess world, Entity entity, List<Identifier> biomeList, int minTime, int maxTime) {
		long time = entity.getEntityWorld().getLunarTime();
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (!(time > minTime || time < maxTime))
				return !biomeList.contains(spawnBiomeKey.get().getValue());
			else return true;
		}
		return false;
	}
	
	/**
	 * 如果在生物群系则只在特殊时间段生成
	 *
	 * @param world    世界访问
	 * @param entity   生成实体
	 * @param biomeTag 生物群系标签
	 * @param minTime  最小时间，游戏时间刻
	 * @param maxTime  最大时间，游戏时间刻
	 * @return 是否生成实体
	 */
	@SilkApi
	static boolean ifInBiomeOnlySpawnInTime(WorldAccess world, Entity entity, TagKey<Biome> biomeTag, int minTime, int maxTime) {
		long time = entity.getEntityWorld().getLunarTime();
		Optional<RegistryKey<Biome>> spawnBiomeKey = world.getBiome(entity.getBlockPos()).getKey();
		if (spawnBiomeKey.isPresent()) {
			if (!(time > minTime || time < maxTime))
				return !world.getBiome(entity.getBlockPos()).isIn(biomeTag);
			else return true;
		}
		return false;
	}
}
