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

package pers.saikel0rado1iu.silk.gen.data;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.gen.ModDataGeneration;

/**
 * <h2 style="color:FFC800">用于创建、注册、生成伤害类型</h2>
 * <p style="color:FFC800">模组作者需要在 {@link ModDataGeneration} 中覆盖 {@link ModDataGeneration#damageType()}方法</p>
 * <p style="color:FFC800">实例化</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SuppressWarnings("UnstableApiUsage")
@SilkApi
public abstract class SilkDamageType {
	@ApiStatus.Internal
	public static final SilkDamageType EMPTY = new SilkDamageType() {
		@Override
		public void bootstrap(Registerable<DamageType> featureRegisterable) {
		}
	};
	
	protected static RegistryKey<DamageType> register(ModBasicData mod, String id) {
		return RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(mod.getId(), id));
	}
	
	@ApiStatus.Internal
	public void configure(RegistryWrapper.WrapperLookup registries, FabricDynamicRegistryProvider.Entries entries) {
		entries.addAll(registries.getWrapperOrThrow(RegistryKeys.DAMAGE_TYPE));
	}
	
	/**
	 * 参考 {@link DamageTypes#bootstrap(Registerable)}
	 */
	@ApiStatus.OverrideOnly
	public abstract void bootstrap(Registerable<DamageType> featureRegisterable);
}
