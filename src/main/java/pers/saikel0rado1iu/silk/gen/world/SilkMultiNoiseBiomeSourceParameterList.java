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

package pers.saikel0rado1iu.silk.gen.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.biome.source.MultiNoiseBiomeSourceParameterLists;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

/**
 * <h2 style="color:FFC800">用于创建、注册、生成在世界预设中使用的多重噪声生物群系源参数列表</h2>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldGenerator} 中覆盖 {@link SilkWorldGenerator#multiNoiseBiomeSourceParameterLists()}方法</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkMultiNoiseBiomeSourceParameterList {
	@ApiStatus.Internal
	public static final SilkMultiNoiseBiomeSourceParameterList EMPTY = new SilkMultiNoiseBiomeSourceParameterList() {
		@Override
		public void bootstrap(Registerable<MultiNoiseBiomeSourceParameterList> registry) {
		}
	};
	
	protected static RegistryKey<MultiNoiseBiomeSourceParameterList> register(ModBasicData mod, String id) {
		return RegistryKey.of(RegistryKeys.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, new Identifier(mod.getId(), id));
	}
	
	/**
	 * 参考 {@link MultiNoiseBiomeSourceParameterLists#bootstrap(Registerable)}
	 */
	@ApiStatus.OverrideOnly
	public abstract void bootstrap(Registerable<MultiNoiseBiomeSourceParameterList> registry);
	
	/**
	 * 加宽访问的 {@link MultiNoiseBiomeSourceParameterList.Preset.BiomeSourceFunction}
	 */
	@SilkApi
	public interface SilkBiomeSourceFunction extends MultiNoiseBiomeSourceParameterList.Preset.BiomeSourceFunction {
	}
}
