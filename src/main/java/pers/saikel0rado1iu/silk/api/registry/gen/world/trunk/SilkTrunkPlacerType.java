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

package pers.saikel0rado1iu.silk.api.registry.gen.world.trunk;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.registry.SilkWorldData;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组所有树干放置器类型与注册</font></b></p>
 * <p style="color:FFC800">模组作者需要在 {@link SilkWorldData} 中覆盖 {@link SilkWorldData#foliagePlacerTypes()}方法</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public abstract class SilkTrunkPlacerType {
	protected static <P extends TrunkPlacer> Builder<P> builder(TrunkPlacerType<P> trunkPlacerType) {
		return new Builder<>(trunkPlacerType);
	}
	
	@SilkApi
	public static final class Builder<P extends TrunkPlacer> {
		private final TrunkPlacerType<P> trunkPlacerType;
		
		@SilkApi
		private Builder(TrunkPlacerType<P> trunkPlacerType) {
			this.trunkPlacerType = trunkPlacerType;
		}
		
		@SilkApi
		public TrunkPlacerType<P> build(ModBasicData mod, String id) {
			Registry.register(Registries.TRUNK_PLACER_TYPE, new Identifier(mod.getId(), id), trunkPlacerType);
			return trunkPlacerType;
		}
	}
}
