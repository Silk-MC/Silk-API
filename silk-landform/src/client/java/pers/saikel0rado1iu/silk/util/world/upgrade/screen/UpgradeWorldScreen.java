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

package pers.saikel0rado1iu.silk.util.world.upgrade.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.SaveLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.screen.BaseScreen;
import pers.saikel0rado1iu.silk.util.world.upgrade.WorldUpgrader;
import pers.saikel0rado1iu.silk.util.world.upgrade.data.WorldUpgradeData;
import pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary;

import java.util.function.ToIntFunction;

import static pers.saikel0rado1iu.silk.util.world.upgrade.info.UpgradeLevelSummary.TEXT_PREFIX;

/**
 * <h2 style="color:FFC800">升级世界屏幕</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
@ApiStatus.Internal
public class UpgradeWorldScreen extends BaseScreen {
	private static final ToIntFunction<RegistryKey<World>> DIMENSION_COLORS = Util.make(new Reference2IntOpenHashMap<>(), map -> {
		map.put(World.OVERWORLD, -13408734);
		map.put(World.NETHER, -10075085);
		map.put(World.END, -8943531);
		map.defaultReturnValue(-2236963);
	});
	private final WorldUpgrader upgrader;
	private final BooleanConsumer callback;
	
	public UpgradeWorldScreen(Text parentTitle, BooleanConsumer callback, LevelInfo levelInfo, UpgradeLevelSummary summary, LevelStorage.Session session, SaveLoader saveLoader, Registry<DimensionOptions> dimensionOptionsRegistry, boolean eraseCache) {
		super(null, Text.translatable(TextUtil.widgetTitle(Silk.DATA, TEXT_PREFIX + "screen"), parentTitle, levelInfo.getLevelName()));
		this.callback = callback;
		this.upgrader = new WorldUpgrader(summary, session, saveLoader, dimensionOptionsRegistry, eraseCache);
	}
	
	@ApiStatus.Internal
	public static UpgradeWorldScreen create(MinecraftClient client, Text parentTitle, BooleanConsumer callback, UpgradeLevelSummary summary, LevelStorage.Session storageSession, boolean eraseCache) {
		try {
			SaveLoader saveLoader = client.createIntegratedServerLoader().createSaveLoader(storageSession, false);
			SaveProperties saveProperties = saveLoader.saveProperties();
			return new UpgradeWorldScreen(parentTitle, callback, saveProperties.getLevelInfo(), summary, storageSession, saveLoader, WorldUpgradeData.getRegistryManager().get(RegistryKeys.DIMENSION), eraseCache);
		} catch (Exception e) {
			Silk.DATA.logger().warn("Failed to load datapacks, can't upgrade world", e);
			return null;
		}
	}
	
	@Override
	protected void init() {
		super.init();
		addDrawableChild(ButtonWidget.builder(ScreenTexts.CANCEL, (button) -> {
			upgrader.cancel();
			callback.accept(false);
		}).dimensions(width / 2 - 100, height / 4 + 150, 200, 20).build());
	}
	
	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context);
		int widgetStartX = width / 2 - 150;
		int widgetEndX = width / 2 + 150;
		int widgetStartY = height / 4 + 100;
		int widgetEndY = widgetStartY + 10;
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, upgrader.getStatus(), width / 2, widgetStartY - textRenderer.fontHeight - 2, 0xA0A0A0);
		if (upgrader.getTotalChunkCount() > 0) {
			context.fill(widgetStartX - 1, widgetStartY - 1, widgetEndX + 1, widgetEndY + 1, -16777216);
			context.drawTextWithShadow(textRenderer, Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "deleted"), upgrader.getDeletedChunkCount()), widgetStartX, 40, 0xA0A0A0);
			context.drawTextWithShadow(textRenderer, Text.translatable(TextUtil.widgetText(Silk.DATA, TEXT_PREFIX + "handled"), upgrader.getHandledChunkCount()), widgetStartX, 40 + textRenderer.fontHeight + 3, 0xA0A0A0);
			context.drawTextWithShadow(textRenderer, Text.translatable("optimizeWorld.info.total", upgrader.getTotalChunkCount()), widgetStartX, 40 + (textRenderer.fontHeight + 3) * 2, 0xA0A0A0);
			for (RegistryKey<World> registryKey : upgrader.getWorlds()) {
				int BarLength = MathHelper.floor(upgrader.getProgress(registryKey) * (float) (widgetEndX - widgetStartX));
				context.fill(widgetStartX, widgetStartY, widgetStartX + BarLength, widgetEndY, DIMENSION_COLORS.applyAsInt(registryKey));
			}
			context.drawCenteredTextWithShadow(textRenderer, (upgrader.getDeletedChunkCount() + upgrader.getHandledChunkCount()) + " / " + upgrader.getTotalChunkCount(),
					width / 2, widgetStartY + 2 * textRenderer.fontHeight + 2, 0xA0A0A0);
			context.drawCenteredTextWithShadow(textRenderer, MathHelper.floor(upgrader.getProgress() * 100.0f) + "%",
					width / 2, widgetStartY + (widgetEndY - widgetStartY) / 2 - textRenderer.fontHeight / 2, 0xA0A0A0);
			super.render(context, mouseX, mouseY, delta);
		}
	}
	
	@Override
	public void tick() {
		if (upgrader.isDone() && client != null) callback.accept(true);
	}
	
	@Override
	public void close() {
		if (client != null) callback.accept(false);
	}
	
	@Override
	public void removed() {
		upgrader.cancel();
	}
}
