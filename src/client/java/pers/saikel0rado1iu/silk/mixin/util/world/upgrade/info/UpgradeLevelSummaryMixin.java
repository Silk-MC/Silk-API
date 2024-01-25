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

package pers.saikel0rado1iu.silk.mixin.util.world.upgrade.info;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.scanner.NbtScanQuery;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryOps;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.gen.world.chunk.UpgradeChunkGenerator;
import pers.saikel0rado1iu.silk.util.world.upgrade.GetRegistryManagerThread;
import pers.saikel0rado1iu.silk.util.world.upgrade.WorldUpgradeSystem;
import pers.saikel0rado1iu.silk.util.world.upgrade.data.WorldUpgradeData;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.ModWorldInfo;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.nbt.NbtHelper.DATA_KEY;

/**
 * <h2 style="color:FFC800">设置可升级关卡摘要混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
interface UpgradeLevelSummaryMixin {
	@Mixin(LevelSummary.class)
	abstract class CreateDetails {
		@Inject(method = "createDetails", at = @At("RETURN"), cancellable = true)
		private void createDetails(CallbackInfoReturnable<Text> cir) {
			if ((Object) this instanceof UpgradeLevelSummary upgradeLevelSummary)
				cir.setReturnValue(upgradeLevelSummary.addDetails((MutableText) cir.getReturnValue()));
		}
	}
	
	@Mixin(SelectWorldScreen.class)
	abstract class SetButtonActive {
		@Shadow
		private WorldListWidget levelList;
		
		@Shadow
		private ButtonWidget recreateButton;
		
		@Shadow
		private ButtonWidget selectButton;
		
		@Inject(method = "worldSelected", at = @At("TAIL"))
		private void worldSelected(boolean buttonsActive, boolean deleteButtonActive, CallbackInfo ci) {
			if (levelList.getSelectedAsOptional().isEmpty()
					|| !(levelList.getSelectedAsOptional().get().level instanceof UpgradeLevelSummary summary)) return;
			selectButton.active = summary.isSelectable();
			recreateButton.active = summary.isRecreatable();
		}
	}
	
	@Mixin(WorldListWidget.class)
	abstract class FixSummaryShow extends AlwaysSelectedEntryListWidget<WorldListWidget.Entry> {
		@Shadow
		private @Nullable List<LevelSummary> levels;
		
		public FixSummaryShow(MinecraftClient minecraftClient, int i, int j, int k, int l, int m) {
			super(minecraftClient, i, j, k, l, m);
		}
		
		@Inject(method = "getRowWidth", at = @At("RETURN"), cancellable = true)
		private void getRowWidth(CallbackInfoReturnable<Integer> cir) {
			if (levels == null) return;
			int rowWidth = cir.getReturnValue();
			for (LevelSummary summary : levels) rowWidth = Math.max(rowWidth, client.textRenderer.getWidth(summary.getDetails()) + 40);
			cir.setReturnValue(rowWidth);
		}
		
		@Inject(method = "getScrollbarPositionX", at = @At("RETURN"), cancellable = true)
		private void getScrollbarPositionX(CallbackInfoReturnable<Integer> cir) {
			if (levels == null) return;
			cir.setReturnValue(Math.max(cir.getReturnValue(), (width + getRowWidth()) / 2));
		}
	}
	
	@Mixin(WorldListWidget.WorldEntry.class)
	abstract class AddWorldEntryWarning extends WorldListWidget.Entry {
		@Unique
		private static final Identifier ERROR_HIGHLIGHTED_TEXTURE = new Identifier("world_list/error_highlighted");
		@Unique
		private static final Identifier WARNING_HIGHLIGHTED_TEXTURE = new Identifier("world_list/warning_highlighted");
		@Unique
		private static final Identifier WARNING_TEXTURE = new Identifier("world_list/warning");
		@Unique
		private static final Identifier ERROR_TEXTURE = new Identifier("world_list/error");
		@Unique
		private static final Identifier MARKED_JOIN_HIGHLIGHTED_TEXTURE = new Identifier("world_list/marked_join_highlighted");
		@Unique
		private static final Identifier MARKED_JOIN_TEXTURE = new Identifier("world_list/marked_join");
		@Shadow
		@Final
		public LevelSummary level;
		@Shadow
		@Final
		private MinecraftClient client;
		@Shadow
		@Final
		private SelectWorldScreen screen;
		
		@Inject(method = "render", at = @At(value = "INVOKE", target = "L net/minecraft/client/gui/DrawContext;drawTexture(L net/minecraft/util/Identifier;I I F F I I I I)V", ordinal = 8), cancellable = true)
		private void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
			if (level instanceof UpgradeLevelSummary upgradeLevelSummary) {
				boolean focus = mouseX - x < 32;
				Identifier warningTexture = focus ? WARNING_HIGHLIGHTED_TEXTURE : WARNING_TEXTURE;
				Identifier errorTexture = focus ? ERROR_HIGHLIGHTED_TEXTURE : ERROR_TEXTURE;
				Identifier joinTexture = focus ? MARKED_JOIN_HIGHLIGHTED_TEXTURE : MARKED_JOIN_TEXTURE;
				if (upgradeLevelSummary.shouldUpgradeWorld()) {
					context.drawTexture(joinTexture, x, y, 32, 32, 256, 256);
					context.drawTexture(warningTexture, x, y, 32, 32, 256, 256);
					if (focus) screen.setTooltip(client.textRenderer.wrapLines(upgradeLevelSummary.getUpgradeTooltip(), 175));
					ci.cancel();
				} else if (upgradeLevelSummary.shouldDowngradeWorld()) {
					context.drawTexture(errorTexture, x, y, 32, 32, 256, 256);
					if (focus) screen.setTooltip(client.textRenderer.wrapLines(upgradeLevelSummary.getDowngradeTooltip(), 175));
					ci.cancel();
				}
			}
		}
	}
	
	@Mixin(LevelStorage.class)
	abstract class CreateSummary {
		@Unique
		private Dynamic<?> dynamic;
		
		@SuppressWarnings("InvalidInjectorMethodSignature")
		@ModifyArg(method = "loadCompactLevelData", at = @At(value = "INVOKE", target = "L net/minecraft/nbt/scanner/ExclusiveNbtCollector;<init>([L net/minecraft/nbt/scanner/NbtScanQuery;)V", ordinal = 0))
		private static NbtScanQuery[] loadCompactLevelData(NbtScanQuery... excludedQueries) {
			return new NbtScanQuery[]{new NbtScanQuery(DATA_KEY, NbtCompound.TYPE, "Player")};
		}
		
		@Inject(method = "loadSummaries", at = @At("HEAD"))
		private void loadSummaries(LevelStorage.LevelList levels, CallbackInfoReturnable<CompletableFuture<List<LevelSummary>>> cir) {
			GetRegistryManagerThread.startGetRegistryManager(levels);
		}
		
		@ModifyVariable(method = "method_29015", at = @At("STORE"), ordinal = 0)
		private Dynamic<?> getDynamic(Dynamic<?> dynamic) {
			return this.dynamic = dynamic;
		}
		
		@Inject(method = "method_29015", at = @At("RETURN"), cancellable = true)
		private void createLevelDataParser(LevelStorage.LevelSave levelSave, boolean bl, Path path, DataFixer dataFixer, CallbackInfoReturnable<LevelSummary> cir) {
			GetRegistryManagerThread.joinGet();
			DynamicRegistryManager.Immutable registryManager = WorldUpgradeData.getRegistryManager();
			for (WorldUpgradeData<?> worldUpgradeData : WorldUpgradeSystem.getWorldUpgradeData()) {
				UpgradeChunkGenerator generator = worldUpgradeData.getGenerator(registryManager);
				Dynamic<?> worldGenSettings = dynamic.get("WorldGenSettings").orElseEmptyMap();
				Dynamic<?> chunkGenerator = worldGenSettings.get("dimensions").get(worldUpgradeData.dimension.getValue().toString()).get("generator").orElseEmptyMap();
				if (chunkGenerator.equals(chunkGenerator.emptyMap())) continue;
				String version = chunkGenerator.get(UpgradeChunkGenerator.VERSION_FIELD).orElseEmptyMap().asString(UpgradeChunkGenerator.NON_VERSION);
				Identifier generatorId = Identifier.tryParse(chunkGenerator.get("type").orElseEmptyMap().asString("foo:bar"));
				if (generatorId != null && generatorId.equals(Registries.CHUNK_GENERATOR.getId(generator.getCodec()))) {
					LevelSummary summary = cir.getReturnValue();
					cir.setReturnValue(new UpgradeLevelSummary(summary.getLevelInfo(), summary.getVersionInfo(),
							new ModWorldInfo(worldUpgradeData.mod, generatorId, version, generator.compareVersion(version)),
							summary.getName(), summary.requiresConversion(), summary.isLocked(), summary.isExperimental(), summary.getIconPath()));
					return;
				}
			}
		}
	}
	
	@Mixin(LevelStorage.Session.class)
	abstract class SummaryUpdate {
		@Inject(method = "getLevelSummary", at = @At("RETURN"))
		private void getLevelSummary(CallbackInfoReturnable<LevelSummary> cir) {
			GetRegistryManagerThread.startGetRegistryManager((LevelStorage.Session) (Object) this);
		}
		
		@SuppressWarnings({"rawtypes", "unchecked"})
		@ModifyVariable(method = "save", at = @At("STORE"), ordinal = 1)
		private <T extends ChunkGenerator & UpgradeChunkGenerator> NbtCompound save(NbtCompound nbt) {
			GetRegistryManagerThread.joinGet();
			for (WorldUpgradeData<?> worldUpgradeData : WorldUpgradeSystem.getWorldUpgradeData()) {
				DynamicRegistryManager.Immutable registryManager = WorldUpgradeData.getRegistryManager();
				T generator = (T) worldUpgradeData.getGenerator(registryManager);
				NbtCompound dimensions = nbt.getCompound("Data").getCompound("WorldGenSettings").getCompound("dimensions");
				NbtCompound dimension;
				if ((dimension = dimensions.getCompound(worldUpgradeData.dimension.getValue().toString())).isEmpty()) continue;
				String generatorId = String.valueOf(Registries.CHUNK_GENERATOR.getId(generator.getCodec()));
				if (!dimension.getCompound("generator").getString("type").equals(generatorId)) continue;
				Codec codec = generator.getCodec();
				NbtCompound nbtCompound = (NbtCompound) Util.getResult(codec.encodeStart(RegistryOps.of(NbtOps.INSTANCE, registryManager), generator), IllegalStateException::new);
				nbtCompound.putString("type", generatorId);
				dimension.put("generator", nbtCompound);
			}
			return nbt;
		}
	}
}
