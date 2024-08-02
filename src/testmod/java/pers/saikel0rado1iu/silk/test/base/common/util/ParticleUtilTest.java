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

package pers.saikel0rado1iu.silk.test.base.common.util;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.saikel0rado1iu.silk.api.base.common.util.ParticleUtil;

/**
 * Test {@link ParticleUtil}
 */
public interface ParticleUtilTest {
	/**
	 * 日志
	 */
	Logger LOGGER = LoggerFactory.getLogger("SilkApiBase/TestingParticleUtil");
	/**
	 * 测试方块
	 */
	ParticleBlock BLOCK = new ParticleBlock(AbstractBlock.Settings.create());
	/**
	 * 测试方块物品
	 */
	BlockItem BLOCK_ITEM = new BlockItem(BLOCK, new Item.Settings());
	
	/**
	 * 测试
	 */
	static void test() {
		Registry.register(Registries.BLOCK, new Identifier("test", "block"), BLOCK);
		Registry.register(Registries.ITEM, new Identifier("test", "block_item"), BLOCK_ITEM);
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(content -> content.add(BLOCK_ITEM));
	}
	
	/**
	 * 测试粒子方块
	 */
	final class ParticleBlock extends Block {
		private ParticleBlock(Settings settings) {
			super(settings);
		}
		
		@Override
		public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
			LOGGER.info("ParticleUtil.addEffectParticle(world, StatusEffects.FIRE_RESISTANCE, pos.getX(), pos.getY(), pos.getZ())");
			ParticleUtil.addEffectParticle(world, StatusEffects.FIRE_RESISTANCE, pos.getX(), pos.getY(), pos.getZ());
			return super.onBreak(world, pos, state, player);
		}
	}
}
