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

package pers.saikel0rado1iu.silk.mixin.spinningjenny.tag;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.spinningjenny.tag.BlockTags;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">{@link BlockTags} 混入</h2>
 * 设置扩展标签的效果
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface BlockTagsMixin {
	/**
	 * 设置 cobwebs 方块标签的破坏加速效果
	 */
	interface Cobweb {
		float COBWEB_MINING_SPEED = 15;
		
		/**
		 * 使用剪刀破坏
		 */
		@Mixin(ShearsItem.class)
		abstract class ShearsBreak {
			/**
			 * 矿物测试，如果方块是 cobwebs 方块标签则返回 true
			 */
			@Inject(method = "postMine", at = @At("RETURN"), cancellable = true)
			public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
				cir.setReturnValue(state.isIn(BlockTags.COBWEB));
			}
			
			/**
			 * 适用测试，如果方块是 cobwebs 方块标签则返回 true
			 */
			@Inject(method = "isSuitableFor", at = @At("RETURN"), cancellable = true)
			public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
				if (state.isIn(BlockTags.COBWEB)) cir.setReturnValue(true);
			}
			
			/**
			 * 获取挖掘速度增幅，如果方块是 cobwebs 方块标签则返回增幅倍数
			 */
			@Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
			public void getMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
				if (state.isIn(BlockTags.COBWEB)) cir.setReturnValue(COBWEB_MINING_SPEED);
			}
		}
		
		/**
		 * 使用剑破坏
		 */
		@Mixin(SwordItem.class)
		abstract class SwordBreak {
			/**
			 * 使用测试，如果方块是 cobwebs 方块标签则返回 true
			 */
			@Inject(method = "isSuitableFor", at = @At("RETURN"), cancellable = true)
			public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
				if (state.isIn(BlockTags.COBWEB)) cir.setReturnValue(true);
			}
			
			/**
			 * 获取挖掘速度增幅，如果方块是 cobwebs 方块标签则返回增幅倍数
			 */
			@Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
			public void getMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
				if (state.isIn(BlockTags.COBWEB)) cir.setReturnValue(COBWEB_MINING_SPEED);
			}
		}
	}
	
	/**
	 * 设置泥土标签的铲平效果
	 */
	@Mixin(ShovelItem.class)
	abstract class Dirt {
		@Unique
		private BlockState state;
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
		private BlockState getBlockState(BlockState state) {
			return this.state = state;
		}
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
		private BlockState set(BlockState state) {
			return this.state.isIn(net.minecraft.registry.tag.BlockTags.DIRT) ? Blocks.DIRT_PATH.getDefaultState() : state;
		}
	}
	
	/**
	 * 设置泥土变种的犁地效果
	 */
	@Mixin(HoeItem.class)
	abstract class Soil {
		@Unique
		private ItemUsageContext context;
		
		@Inject(method = "useOnBlock", at = @At("HEAD"))
		private void set(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
			this.context = context;
		}
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
		private Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> set(Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair) {
			return context.getWorld().getBlockState(context.getBlockPos()).isIn(BlockTags.SOIL)
					? Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.DIRT.getDefaultState()))
					: pair;
		}
	}
	
	/**
	 * 设置可犁地方块的犁地效果
	 */
	@Mixin(HoeItem.class)
	abstract class TillableBlock {
		@Unique
		private ItemUsageContext context;
		
		@Inject(method = "useOnBlock", at = @At("HEAD"))
		private void set(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
			this.context = context;
		}
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
		private Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> set(Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair) {
			return context.getWorld().getBlockState(context.getBlockPos()).isIn(BlockTags.TILLABLE_BLOCKS)
					? Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState()))
					: pair;
		}
	}
	
	/**
	 * 设置树苗种植块的可种植效果
	 */
	@Mixin(SaplingBlock.class)
	abstract class SaplingGrowBlock extends PlantBlock {
		private SaplingGrowBlock(Settings settings) {
			super(settings);
		}
		
		@Override
		protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
			return super.canPlantOnTop(floor, world, pos) || floor.isIn(BlockTags.SAPLING_GROW_BLOCK);
		}
	}
	
	/**
	 * 设置植株种植块的可种植效果
	 */
	@Mixin(PlantBlock.class)
	abstract class PlantGrowBlock {
		@Inject(method = "canPlantOnTop", at = @At("RETURN"), cancellable = true)
		private void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
			cir.setReturnValue(cir.getReturnValue() || floor.isIn(BlockTags.PLANT_GROW_BLOCK));
		}
	}
}
