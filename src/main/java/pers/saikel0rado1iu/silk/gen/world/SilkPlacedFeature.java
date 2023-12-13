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
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.List;

/**
 * <h2 style="color:FFC800">用于创建、注册、生成已放置的地物</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldGenerator} 中覆盖 {@link SilkWorldGenerator#placedFeatures()}方法</p>
 * <p style="color:FFC800">实例化</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkPlacedFeature {
	@ApiStatus.Internal
	public static final SilkPlacedFeature EMPTY = new SilkPlacedFeature() {
		@Override
		public void bootstrap(Registerable<PlacedFeature> featureRegisterable) {
		}
	};
	
	@SilkApi
	protected static RegistryKey<PlacedFeature> register(ModBasicData mod, String id) {
		return RegistryKey.of(RegistryKeys.PLACED_FEATURE, new Identifier(mod.getId(), id));
	}
	
	@SilkApi
	protected static void register(Registerable<PlacedFeature> featureRegisterable, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> feature, List<PlacementModifier> modifiers) {
		featureRegisterable.register(key, new PlacedFeature(feature, List.copyOf(modifiers)));
	}
	
	@SilkApi
	protected static void register(Registerable<PlacedFeature> featureRegisterable, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		PlacedFeatures.register(featureRegisterable, key, feature, List.of(modifiers));
	}
	
	/**
	 * 参考 {@link PlacedFeatures#bootstrap(Registerable)}
	 */
	@ApiStatus.OverrideOnly
	public abstract void bootstrap(Registerable<PlacedFeature> featureRegisterable);
}
