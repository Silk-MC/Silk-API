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

package pers.saikel0rado1iu.silk.mixin.event.landform;

import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.scanner.NbtScanQuery;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.event.landform.ClientWorldUpgradeManagerCallback;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.nbt.NbtHelper.DATA_KEY;

/**
 * <h2 style="color:FFC800">{@link ClientWorldUpgradeManagerCallback} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
interface ClientWorldUpgradeManagerCallbackMixin {
	/**
	 * 关卡列表事件的混入
	 */
	@Mixin(LevelStorage.class)
	abstract class LevelListEvent {
		@ModifyArg(method = "loadCompactLevelData", at = @At(value = "INVOKE", target = "L net/minecraft/nbt/scanner/ExclusiveNbtCollector;<init>([L net/minecraft/nbt/scanner/NbtScanQuery;)V", ordinal = 0))
		private static NbtScanQuery[] loadCompactLevelData(NbtScanQuery... excludedQueries) {
			return new NbtScanQuery[]{new NbtScanQuery(DATA_KEY, NbtCompound.TYPE, "Player")};
		}
		
		@Inject(method = "loadSummaries", at = @At("HEAD"))
		private void loadSummaries(LevelStorage.LevelList levels, CallbackInfoReturnable<CompletableFuture<List<LevelSummary>>> cir) {
			ClientWorldUpgradeManagerCallback.LEVEL_LIST_START_EVENT.invoker().accept(levels);
		}
		
		@Inject(method = "parseSummary", at = @At("RETURN"), cancellable = true)
		private void parseSummary(Dynamic<?> dynamic, LevelStorage.LevelSave save, boolean locked, CallbackInfoReturnable<LevelSummary> cir) {
			cir.setReturnValue(ClientWorldUpgradeManagerCallback.LEVEL_LIST_OPERATION_EVENT.invoker().apply(dynamic, cir.getReturnValue()));
		}
	}
	
	/**
	 * 关卡存储器会话的混入
	 */
	@Mixin(LevelStorage.Session.class)
	abstract class SessionEvent {
		@Inject(method = "getLevelSummary", at = @At("RETURN"))
		private void getLevelSummary(Dynamic<?> dynamic, CallbackInfoReturnable<LevelSummary> cir) {
			ClientWorldUpgradeManagerCallback.SESSION_START_EVENT.invoker().accept((LevelStorage.Session) (Object) this);
		}
		
		@ModifyVariable(method = "save(L net/minecraft/nbt/NbtCompound;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
		private NbtCompound save(NbtCompound nbt) {
			return ClientWorldUpgradeManagerCallback.SESSION_OPERATION_EVENT.invoker().apply(nbt);
		}
	}
}
