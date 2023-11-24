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

package pers.saikel0rado1iu.silk.api.registry.gen.world;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.registry.SilkWorldData;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有树叶放置器类型与注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldData} 中覆盖 {@link SilkWorldData#foliagePlacerTypes()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkFoliagePlacerType {
	protected static <P extends FoliagePlacer> Builder<P> builder(FoliagePlacerType<P> foliagePlacerType) {
		return new Builder<>(foliagePlacerType);
	}
	
	@SilkApi
	public static final class Builder<P extends FoliagePlacer> {
		private final FoliagePlacerType<P> foliagePlacerType;
		
		@SilkApi
		private Builder(FoliagePlacerType<P> foliagePlacerType) {
			this.foliagePlacerType = foliagePlacerType;
		}
		
		@SilkApi
		public FoliagePlacerType<P> build(ModBasicData mod, String id) {
			Registry.register(Registries.FOLIAGE_PLACER_TYPE, new Identifier(mod.getId(), id), foliagePlacerType);
			return foliagePlacerType;
		}
	}
}
