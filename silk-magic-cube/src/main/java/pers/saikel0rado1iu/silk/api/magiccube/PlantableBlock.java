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

package pers.saikel0rado1iu.silk.api.magiccube;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

/**
 * <h2 style="color:FFC800">可种植块</h2>
 * 树苗可放置在可种植快上成长
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface PlantableBlock {
	/**
	 * 设置可放置树苗
	 *
	 * @param world  存档世界
	 * @param pos    方块位置
	 * @param player 玩家
	 * @param hand   玩家的主副手
	 * @param hit    方块命中结果
	 * @param tag    可以放置的树苗标签
	 * @return 操作结果，如果为 {@link ActionResult#PASS} 则可以进行后续处理
	 */
	default ActionResult onUse(World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, TagKey<Item> tag) {
		ItemStack stack = player.getStackInHand(hand);
		Item item = stack.getItem();
		if (hit.getSide() == Direction.UP && stack.isIn(tag)) {
			world.playSound(null, pos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.setBlockState(pos.up(), ((BlockItem) item).getBlock().getDefaultState());
			if (!world.isClient) if (!player.isCreative()) stack.decrement(1);
			
			player.incrementStat(Stats.USED.getOrCreateStat(item));
			return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}
}
