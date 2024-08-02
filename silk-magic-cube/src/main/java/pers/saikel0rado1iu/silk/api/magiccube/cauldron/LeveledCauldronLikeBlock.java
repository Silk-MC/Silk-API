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

package pers.saikel0rado1iu.silk.api.magiccube.cauldron;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

/**
 * <h2 style="color:FFC800">水平面坩埚类方块</h2>
 * 创建一个拥有水平面的坩埚类方块
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class LeveledCauldronLikeBlock extends LeveledCauldronBlock {
	protected LeveledCauldronLikeBlock(Biome.Precipitation precipitation, CauldronBehavior.CauldronBehaviorMap behaviorMap, Settings settings) {
		super(precipitation, behaviorMap, settings);
	}
	
	/**
	 * 液位属性
	 *
	 * @return 整数属性
	 */
	public abstract IntProperty level();
	
	/**
	 * 递减液位方法
	 *
	 * @param world 存档世界
	 * @param state 方块状态
	 * @param pos   方块位置
	 */
	public abstract void decrementFluidLevel(World world, BlockState state, BlockPos pos);
}
