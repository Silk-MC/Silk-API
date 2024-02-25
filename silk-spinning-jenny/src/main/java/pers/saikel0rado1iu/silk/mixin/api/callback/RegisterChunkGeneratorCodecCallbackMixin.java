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

package pers.saikel0rado1iu.silk.mixin.api.callback;

import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGenerators;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.callback.RegisterChunkGeneratorCodecCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2 style="color:FFC800">设置区块生成器解编码器的回调</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@Mixin(ChunkGenerators.class)
abstract class RegisterChunkGeneratorCodecCallbackMixin {
	@Inject(method = "registerAndGetDefault", at = @At("HEAD"))
	private static void registerAndGetDefault(Registry<Codec<? extends ChunkGenerator>> registry, CallbackInfoReturnable<Codec<? extends ChunkGenerator>> cir) {
		List<RegisterChunkGeneratorCodecCallback.Data> list = new ArrayList<>();
		RegisterChunkGeneratorCodecCallback.EVENT.invoker().register(list);
		for (RegisterChunkGeneratorCodecCallback.Data data : list) Registry.register(registry, new Identifier(data.mod().getId(), data.id()), data.codec());
	}
}
