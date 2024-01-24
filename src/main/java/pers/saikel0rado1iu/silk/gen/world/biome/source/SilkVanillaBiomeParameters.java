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

package pers.saikel0rado1iu.silk.gen.world.biome.source;

import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.List;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">用于继承原版的生物群系参数以创造在拓展原版主维度的模组维度</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkVanillaBiomeParameters extends VanillaBiomeParameters {
	@SilkApi
	public List<RegistryKey<Biome>> getNonVanillaGeneratedBiome() {
		return List.of();
	}
	
	@Override
	public void writeOverworldBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
		super.writeOverworldBiomeParameters(parameters);
		addNonVanillaGeneratedBiome(parameters);
	}
	
	@ApiStatus.Internal
	public void addNonVanillaGeneratedBiome(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
		getNonVanillaGeneratedBiome().forEach(biomeRegistryKey -> parameters.accept(Pair.of(
				MultiNoiseUtil.createNoiseHypercube(9, 9, 9, 9, 9, 9, 9), biomeRegistryKey)));
	}
}
