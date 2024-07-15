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

package pers.saikel0rado1iu.silk.test.magiccube.infect;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeConfiguredFeatures;
import pers.saikel0rado1iu.silk.magiccube.PlantableBlock;
import pers.saikel0rado1iu.silk.magiccube.infect.InfectMushroom;
import pers.saikel0rado1iu.silk.spinningjenny.tag.ItemTags;

/**
 * Test {@link InfectMushroom}
 */
public final class InfectMushroomTest extends Block implements PlantableBlock, InfectMushroom {
	/**
	 * @param settings 方块设置
	 */
	public InfectMushroomTest(Settings settings) {
		super(settings);
	}
	
	/**
	 * 菌类的生长方法
	 *
	 * @param world         服务区存档世界
	 * @param pos           方块位置
	 * @param state         方块状态
	 * @param random        随机数
	 * @param mushroomPlant 蘑菇植株块
	 * @return 是否已生成
	 */
	@Override
	public boolean generate(ServerWorld world, BlockPos pos, BlockState state, Random random, MushroomPlantBlock mushroomPlant) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		ConfiguredFeature<?, ?> registryEntry = world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).getEntry(TreeConfiguredFeatures.CHERRY).orElseThrow().value();
		return registryEntry.generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult actionResult = onUse(world, pos, player, hand, hit, ItemTags.MUSHROOMS);
		return actionResult.equals(ActionResult.PASS) ? super.onUse(state, world, pos, player, hand, hit) : actionResult;
	}
}
