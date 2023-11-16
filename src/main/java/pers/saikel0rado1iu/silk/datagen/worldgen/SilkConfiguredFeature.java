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

package pers.saikel0rado1iu.silk.datagen.worldgen;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.datagen.SilkWorldGenerator;

/**
 * <p><b style="color:FFC800"><font size="+1">用于创建、注册、生成已配置的地物</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldGenerator} 中覆盖 {@link SilkWorldGenerator#configuredFeatures()}方法</p>
 * <p style="color:FFC800">实例化</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkConfiguredFeature {
	@ApiStatus.Internal
	public static final SilkConfiguredFeature EMPTY = new SilkConfiguredFeature() {
		@Override
		public void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable) {
		}
	};
	
	@SilkApi
	protected static RegistryKey<ConfiguredFeature<?, ?>> register(ModBasicData mod, String id) {
		return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(mod.getId(), id));
	}
	
	@SilkApi
	protected static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> registerable, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC config) {
		ConfiguredFeatures.register(registerable, key, feature, config);
	}
	
	@SilkApi
	protected static void register(Registerable<ConfiguredFeature<?, ?>> registerable, RegistryKey<ConfiguredFeature<?, ?>> key, Feature<DefaultFeatureConfig> feature) {
		ConfiguredFeatures.register(registerable, key, feature, FeatureConfig.DEFAULT);
	}
	
	/**
	 * 参考 {@link ConfiguredFeatures#bootstrap(Registerable)}
	 */
	@ApiStatus.OverrideOnly
	public abstract void bootstrap(Registerable<ConfiguredFeature<?, ?>> featureRegisterable);
}
