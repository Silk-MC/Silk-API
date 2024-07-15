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

package pers.saikel0rado1iu.silk.common.util;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;

import java.util.function.Function;

/**
 * <h2 style="color:FFC800">生成实用工具</h2>
 * 此方法主要用于类似怪物类生物的生成，而不是动物类生物的生成。<br>
 * 如果要实现动物类的生成要在 {@link Builder#otherChecker(SpawnRestriction.SpawnPredicate)} 中手动添加检测器<br>
 * ！注意！在大部分情况下，此方法可能看起来失效。这是因为在 {@link SpawnSettings.Builder#spawn(SpawnGroup, SpawnSettings.SpawnEntry)}
 * 中的设置的生成组规则大过自定义规则。此时将 {@link MobEntity#canSpawn(WorldView)} 与 {@link MobEntity#canSpawn(WorldAccess, SpawnReason)}
 * 返回 true 则可以修复大部分怪物生成问题，生物会照样在区块生成时生成。虽无法预测此操作可能带来的完整效果，但经过测试，绝大部分情况下并不会破坏生成规则
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface SpawnUtil {
	/**
	 * 设置 {@link SpawnRestriction.SpawnPredicate} 的生成方法构建器
	 *
	 * @param ignore {@link MobEntity} 的类
	 * @param <T>    设置的 {@link MobEntity}
	 * @return 构建器
	 */
	static <T extends MobEntity> Builder<T> builder(Class<T> ignore) {
		return new Builder<>();
	}
	
	/**
	 * {@link SpawnRestriction.SpawnPredicate} 的生成方法构建器
	 *
	 * @param <T> {@link MobEntity}
	 */
	final class Builder<T extends MobEntity> {
		private boolean isDay = false;
		private boolean isNight = false;
		private boolean isMonster = false;
		private Function<Long, Boolean> timeChecker = aLong -> true;
		private Function<Integer, Boolean> lightChecker = aInt -> true;
		private Function<RegistryEntry<Biome>, Boolean> biomeChecker = biomeRegistryEntry -> true;
		private SpawnRestriction.SpawnPredicate<T> otherChecker = (type, world, spawnReason, pos, random) -> true;
		
		private Builder() {
		}
		
		/**
		 * 如果与 {@link Builder#night()} 一起使用则很可能无法生成生物
		 *
		 * @return {@link Builder}
		 */
		public Builder<T> day() {
			isDay = true;
			return this;
		}
		
		/**
		 * 如果与 {@link Builder#day()} 一起使用则很可能无法生成生物
		 *
		 * @return {@link Builder}
		 */
		public Builder<T> night() {
			isNight = true;
			return this;
		}
		
		/**
		 * 是否为怪物
		 *
		 * @return {@link Builder}
		 */
		public Builder<T> monster() {
			this.isMonster = true;
			return this;
		}
		
		/**
		 * 设置特定时间
		 *
		 * @param timeChecker 时间检测函数
		 * @return {@link Builder}
		 */
		public Builder<T> time(Function<Long, Boolean> timeChecker) {
			this.timeChecker = timeChecker;
			return this;
		}
		
		/**
		 * 此光照等级是从当前位置选取天空光照与方块光照中最高的作为参数<br>
		 * 如果要进行更精细的控制可以使用 {@link Builder#otherChecker(SpawnRestriction.SpawnPredicate)}
		 *
		 * @param lightChecker 光照检测函数
		 * @return {@link Builder}
		 */
		public Builder<T> light(Function<Integer, Boolean> lightChecker) {
			this.lightChecker = lightChecker;
			return this;
		}
		
		/**
		 * 设置特定生物群系
		 *
		 * @param biomeChecker 生物群系检测函数
		 * @return {@link Builder}
		 */
		public Builder<T> biome(Function<RegistryEntry<Biome>, Boolean> biomeChecker) {
			this.biomeChecker = biomeChecker;
			return this;
		}
		
		/**
		 * 设置其他检测器
		 *
		 * @param otherChecker 其他生成检测器
		 * @return {@link Builder}
		 */
		public Builder<T> otherChecker(SpawnRestriction.SpawnPredicate<T> otherChecker) {
			this.otherChecker = otherChecker;
			return this;
		}
		
		/**
		 * 构建检测器
		 *
		 * @return 生成检测器
		 */
		public SpawnRestriction.SpawnPredicate<T> build() {
			return (type, world, spawnReason, pos, random) -> MobEntity.canMobSpawn(type, world, spawnReason, pos, random)
					&& biomeChecker.apply(world.getBiome(pos))
					&& timeChecker.apply(world.getLunarTime())
					&& (!isDay || world.toServerWorld().isDay())
					&& (!isNight || world.toServerWorld().isNight())
					&& (!isMonster || world.getDifficulty() != Difficulty.PEACEFUL)
					&& lightChecker.apply(Math.max(world.getLightLevel(LightType.SKY, pos), world.getLightLevel(LightType.BLOCK, pos)))
					&& otherChecker.test(type, world, spawnReason, pos, random);
		}
	}
}
