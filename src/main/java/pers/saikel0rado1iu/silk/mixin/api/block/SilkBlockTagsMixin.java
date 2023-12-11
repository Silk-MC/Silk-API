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

package pers.saikel0rado1iu.silk.mixin.api.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.block.SilkBlockTags;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">设置扩展标签的效果</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface SilkBlockTagsMixin {
	/**
	 * 设置 cobwebs 方块标签的破坏加速效果
	 */
	interface CobwebsMixin {
		float COBWEB_MINING_SPEED = 15;
		
		@Mixin(ShearsItem.class)
		abstract class ShearsBreak {
			/**
			 * 矿物测试，如果方块是 cobwebs 方块标签则返回 true
			 */
			@Inject(method = "postMine", at = @At("RETURN"), cancellable = true)
			public void postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
				cir.setReturnValue(state.isIn(SilkBlockTags.COBWEBS));
			}
			
			/**
			 * 适用测试，如果方块是 cobwebs 方块标签则返回 true
			 */
			@Inject(method = "isSuitableFor", at = @At("RETURN"), cancellable = true)
			public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
				if (state.isIn(SilkBlockTags.COBWEBS)) cir.setReturnValue(true);
			}
			
			/**
			 * 获取挖掘速度增幅，如果方块是 cobwebs 方块标签则返回增幅倍数
			 */
			@Inject(method = "getMiningSpeedMultiplier", at = @At("HEAD"), cancellable = true)
			public void getMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
				if (state.isIn(SilkBlockTags.COBWEBS)) cir.setReturnValue(COBWEB_MINING_SPEED);
			}
		}
		
		@Mixin(SwordItem.class)
		abstract class SwordBreak {
			/**
			 * 使用测试，如果方块是 cobwebs 方块标签则返回 true
			 */
			@Inject(method = "isSuitableFor", at = @At("RETURN"), cancellable = true)
			public void isSuitableFor(BlockState state, CallbackInfoReturnable<Boolean> cir) {
				if (state.isIn(SilkBlockTags.COBWEBS)) cir.setReturnValue(true);
			}
			
			/**
			 * 获取挖掘速度增幅，如果方块是 cobwebs 方块标签则返回增幅倍数
			 */
			@Inject(method = "getMiningSpeedMultiplier", at = @At("RETURN"), cancellable = true)
			public void getMiningSpeedMultiplier(ItemStack stack, BlockState state, CallbackInfoReturnable<Float> cir) {
				if (state.isIn(SilkBlockTags.COBWEBS)) cir.setReturnValue(COBWEB_MINING_SPEED);
			}
		}
	}
	
	/**
	 * 设置泥土标签的铲平效果
	 */
	@Mixin(ShovelItem.class)
	abstract class DirtMixin {
		@Unique
		private BlockState state;
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
		private BlockState getBlockState(BlockState state) {
			return this.state = state;
		}
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 1)
		private BlockState set(BlockState state) {
			return this.state.isIn(BlockTags.DIRT) ? Blocks.DIRT_PATH.getDefaultState() : state;
		}
	}
	
	/**
	 * 设置泥土变种的犁地效果
	 */
	@Mixin(HoeItem.class)
	abstract class DirtVarietyMixin {
		@Unique
		private ItemUsageContext context;
		
		@Inject(method = "useOnBlock", at = @At("HEAD"))
		private void set(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
			this.context = context;
		}
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
		private Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> set(Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair) {
			return context.getWorld().getBlockState(context.getBlockPos()).isIn(SilkBlockTags.DIRT_VARIETY)
					? Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.DIRT.getDefaultState()))
					: pair;
		}
	}
	
	/**
	 * 设置可犁地方块的犁地效果
	 */
	@Mixin(HoeItem.class)
	abstract class CanTillBlockMixin {
		@Unique
		private ItemUsageContext context;
		
		@Inject(method = "useOnBlock", at = @At("HEAD"))
		private void set(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
			this.context = context;
		}
		
		@ModifyVariable(method = "useOnBlock", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
		private Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> set(Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>> pair) {
			return context.getWorld().getBlockState(context.getBlockPos()).isIn(SilkBlockTags.CAN_TILL_BLOCK)
					? Pair.of(HoeItem::canTillFarmland, HoeItem.createTillAction(Blocks.FARMLAND.getDefaultState()))
					: pair;
		}
	}
}
