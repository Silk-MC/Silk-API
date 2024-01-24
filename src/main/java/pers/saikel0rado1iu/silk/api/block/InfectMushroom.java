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

package pers.saikel0rado1iu.silk.api.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">菌类成长感染</font></b></p>
 * <p style="color:FFC800">将菌类的生长后植株感染成你想要的植物，而不是感染替换蘑菇苗</p>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface InfectMushroom {
	@SilkApi
	boolean generate(ServerWorld world, BlockPos pos, BlockState state, Random random, MushroomPlantBlock mushroomPlant);
}
