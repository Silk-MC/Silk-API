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

package pers.saikel0rado1iu.silk.ropestick;

import pers.saikel0rado1iu.silk.api.ModBasicData;
import pers.saikel0rado1iu.silk.api.ModMain;
import pers.saikel0rado1iu.silk.api.registry.SilkDataComponentType;
import pers.saikel0rado1iu.silk.impl.SilkRopeStick;

/**
 * <h2 style="color:FFC800">主类</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.1
 */
public final class Main extends ModMain {
	public Main() {
		super(SilkRopeStick.DATA);
	}
	
	@Override
	public void main(ModBasicData modBasicData) {
	}
	
	@Override
	public SilkDataComponentType dataComponentTypes() {
		return new DataComponentTypes();
	}
}
