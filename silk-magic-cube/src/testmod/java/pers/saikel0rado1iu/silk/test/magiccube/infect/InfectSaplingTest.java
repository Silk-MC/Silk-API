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
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
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
import pers.saikel0rado1iu.silk.magiccube.infect.InfectSapling;

/**
 * Test {@link InfectSapling}
 */
public final class InfectSaplingTest extends Block implements PlantableBlock, InfectSapling {
	/**
	 * @param settings 方块设置
	 */
	public InfectSaplingTest(Settings settings) {
		super(settings);
	}
	
	/**
	 * 树苗的生成方法
	 *
	 * @param world   服务区存档世界
	 * @param pos     方块位置
	 * @param state   方块状态
	 * @param random  随机数
	 * @param sapling 树苗方块
	 */
	@Override
	public void generate(ServerWorld world, BlockPos pos, BlockState state, Random random, SaplingBlock sapling) {
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
		ConfiguredFeature<?, ?> registryEntry = world.getRegistryManager().get(RegistryKeys.CONFIGURED_FEATURE).getEntry(TreeConfiguredFeatures.CHERRY).orElseThrow().value();
		registryEntry.generate(world, world.getChunkManager().getChunkGenerator(), random, pos);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult actionResult = onUse(world, pos, player, hand, hit, ItemTags.SAPLINGS);
		return actionResult.equals(ActionResult.PASS) ? super.onUse(state, world, pos, player, hand, hit) : actionResult;
	}
}
