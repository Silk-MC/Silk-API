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

package pers.saikel0rado1iu.silk.api.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有状态效果与注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link ModMain} 中覆盖 {@link ModMain#statusEffects()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkStatusEffect {
	protected static <E extends StatusEffect> Builder<E> builder(E statusEffect) {
		return new Builder<>(statusEffect);
	}
	
	@SilkApi
	public static final class Builder<E extends StatusEffect> {
		private final E statusEffect;
		
		@SilkApi
		private Builder(E statusEffect) {
			this.statusEffect = statusEffect;
		}
		
		@SilkApi
		public E build(ModBasicData mod, String id) {
			Registry.register(Registries.STATUS_EFFECT, new Identifier(mod.getId(), id), statusEffect);
			return statusEffect;
		}
	}
}
