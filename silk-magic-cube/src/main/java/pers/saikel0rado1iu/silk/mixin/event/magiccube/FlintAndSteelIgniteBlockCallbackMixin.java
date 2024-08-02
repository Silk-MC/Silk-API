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

package pers.saikel0rado1iu.silk.mixin.event.magiccube;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.event.magiccube.FlintAndSteelIgniteBlockCallback;

/**
 * <h2 style="color:FFC800">{@link FlintAndSteelIgniteBlockCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
@Mixin(targets = "net.minecraft.block.dispenser.DispenserBehavior$18")
abstract class FlintAndSteelIgniteBlockCallbackMixin extends FallibleItemDispenserBehavior {
	@Inject(method = "dispenseSilently", at = @At(value = "INVOKE", target = "L net/minecraft/block/dispenser/DispenserBehavior$18;setSuccess(Z)V", ordinal = 1, shift = At.Shift.AFTER))
	private void dispenseSilently(BlockPointer pointer, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
		ServerWorld world = pointer.world();
		Direction direction = pointer.state().get(DispenserBlock.FACING);
		BlockPos blockPos = pointer.pos().offset(direction);
		BlockState blockState = world.getBlockState(blockPos);
		if (FlintAndSteelIgniteBlockCallback.EVENT.invoker().igniteBlock(blockState, pointer, world, direction, blockPos, stack)) setSuccess(true);
	}
}
