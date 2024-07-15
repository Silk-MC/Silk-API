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

package pers.saikel0rado1iu.silk.landform.biome.source.util;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;

import java.util.List;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">默认生物群系参数</h2>
 * 用于继承原版的生物群系参数以创造在拓展原版主维度的模组维度
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class DefaultBiomeParameters extends VanillaBiomeParameters {
	/**
	 * 非原版生成的生物群系
	 *
	 * @return 非原版生成的生物群系列表
	 */
	public List<RegistryKey<Biome>> nonVanillaGeneratedBiome() {
		return ImmutableList.of();
	}
	
	@Override
	public void writeOverworldBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
		super.writeOverworldBiomeParameters(parameters);
		addNonVanillaGeneratedBiome(parameters);
	}
	
	protected void addNonVanillaGeneratedBiome(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
		nonVanillaGeneratedBiome().forEach(biomeRegistryKey -> parameters.accept(Pair.of(
				MultiNoiseUtil.createNoiseHypercube(9, 9, 9, 9, 9, 9, 9), biomeRegistryKey)));
	}
}
