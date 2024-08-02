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

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.event.magiccube.FireIgniteBlockCallback;

/**
 * <h2 style="color:FFC800">{@link FireIgniteBlockCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
@Mixin(FireBlock.class)
abstract class FireIgniteBlockCallbackMixin {
	@Inject(method = "trySpreadingFire", at = @At(value = "INVOKE", target = "L net/minecraft/block/BlockState;getBlock()L net/minecraft/block/Block;"))
	private void trySpreadingFire(World world, BlockPos pos, int spreadFactor, Random random, int currentAge, CallbackInfo ci, @Local BlockState blockState) {
		FireIgniteBlockCallback.EVENT.invoker().igniteBlock(blockState, world, pos, spreadFactor, random, currentAge);
	}
}
