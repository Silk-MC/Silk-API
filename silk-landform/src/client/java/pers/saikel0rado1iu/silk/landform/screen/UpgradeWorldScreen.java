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

package pers.saikel0rado1iu.silk.landform.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.SaveLoader;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.storage.LevelStorage;
import pers.saikel0rado1iu.silk.impl.SilkLandform;
import pers.saikel0rado1iu.silk.landform.UpgradableLevelSummary;
import pers.saikel0rado1iu.silk.landform.UpgradableWorldManager;
import pers.saikel0rado1iu.silk.landform.WorldUpgrader;
import pers.saikel0rado1iu.silk.landform.gen.chunk.ChunkGeneratorUpgradable;
import pers.saikel0rado1iu.silk.pattern.screen.BaseScreen;
import pers.saikel0rado1iu.silk.pattern.widget.WidgetTexts;

import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * <h2 style="color:FFC800">升级世界屏幕</h2>
 *
 * @param <T> 区块生成器类型
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.2.0
 */
public class UpgradeWorldScreen<T extends ChunkGenerator & ChunkGeneratorUpgradable> extends BaseScreen {
	private static final ToIntFunction<RegistryKey<World>> DIMENSION_COLORS = Util.make(new Reference2IntOpenHashMap<>(), map -> {
		map.put(World.OVERWORLD, -13408734);
		map.put(World.NETHER, -10075085);
		map.put(World.END, -8943531);
		map.defaultReturnValue(-2236963);
	});
	private final WorldUpgrader<T> upgrader;
	private final BooleanConsumer callback;
	
	private UpgradeWorldScreen(Text parentTitle, BooleanConsumer callback, LevelInfo levelInfo, UpgradableLevelSummary<T> summary, LevelStorage.Session session, SaveLoader saveLoader, Registry<DimensionOptions> dimensionOptionsRegistry, boolean eraseCache) {
		super(null, Text.translatable(WidgetTexts.titleKey(SilkLandform.getInstance(), "screen"), parentTitle, levelInfo.getLevelName()));
		this.callback = callback;
		this.upgrader = new WorldUpgrader<>(summary.info(), session, saveLoader, dimensionOptionsRegistry, eraseCache);
	}
	
	/**
	 * @param client         Minecraft 客户端
	 * @param parentTitle    父标题
	 * @param callback       运行回调
	 * @param summary        升级关卡摘要
	 * @param storageSession 关卡存储器会话
	 * @param eraseCache     是否擦除缓存
	 * @param <T>            区块生成器类型
	 * @return 升级世界屏幕
	 */
	public static <T extends ChunkGenerator & ChunkGeneratorUpgradable> Optional<UpgradeWorldScreen<T>> create(MinecraftClient client, Text parentTitle, BooleanConsumer callback, UpgradableLevelSummary<T> summary, LevelStorage.Session storageSession, boolean eraseCache) {
		try {
			SaveLoader saveLoader = client.createIntegratedServerLoader().load(storageSession.readLevelProperties(), false, VanillaDataPackProvider.createManager(storageSession));
			SaveProperties saveProperties = saveLoader.saveProperties();
			return Optional.of(new UpgradeWorldScreen<>(parentTitle, callback, saveProperties.getLevelInfo(), summary, storageSession, saveLoader, UpgradableWorldManager.registryManager().get(RegistryKeys.DIMENSION), eraseCache));
		} catch (Exception e) {
			SilkLandform.getInstance().logger().warn("Failed to load datapacks, can't upgrade world", e);
			return Optional.empty();
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
		super.render(context, mouseX, mouseY, delta);
		int widgetStartX = width / 2 - 150;
		int widgetEndX = width / 2 + 150;
		int widgetStartY = height / 4 + 100;
		int widgetEndY = widgetStartY + 10;
		context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 20, 0xFFFFFF);
		context.drawCenteredTextWithShadow(textRenderer, upgrader.status(), width / 2, widgetStartY - textRenderer.fontHeight - 2, 0xA0A0A0);
		if (upgrader.totalChunkCount() > 0) {
			context.fill(widgetStartX - 1, widgetStartY - 1, widgetEndX + 1, widgetEndY + 1, -16777216);
			context.drawTextWithShadow(textRenderer, Text.translatable(WidgetTexts.textKey(SilkLandform.getInstance(), "deleted"), upgrader.deletedChunkCount()), widgetStartX, 40, 0xA0A0A0);
			context.drawTextWithShadow(textRenderer, Text.translatable(WidgetTexts.textKey(SilkLandform.getInstance(), "handled"), upgrader.handledChunkCount()), widgetStartX, 40 + textRenderer.fontHeight + 3, 0xA0A0A0);
			context.drawTextWithShadow(textRenderer, Text.translatable("optimizeWorld.info.total", upgrader.totalChunkCount()), widgetStartX, 40 + (textRenderer.fontHeight + 3) * 2, 0xA0A0A0);
			for (RegistryKey<World> registryKey : upgrader.worlds()) {
				int BarLength = MathHelper.floor(upgrader.progress(registryKey) * (float) (widgetEndX - widgetStartX));
				context.fill(widgetStartX, widgetStartY, widgetStartX + BarLength, widgetEndY, DIMENSION_COLORS.applyAsInt(registryKey));
			}
			context.drawCenteredTextWithShadow(textRenderer, Text.translatable("optimizeWorld.progress.counter", upgrader.deletedChunkCount() + upgrader.handledChunkCount(),
					upgrader.totalChunkCount()), width / 2, widgetStartY + 2 * textRenderer.fontHeight + 2, 0xA0A0A0);
			context.drawCenteredTextWithShadow(textRenderer, Text.translatable("optimizeWorld.progress.percentage", MathHelper.floor(upgrader.progress() * 100.0f)),
					width / 2, widgetStartY + (widgetEndY - widgetStartY) / 2 - textRenderer.fontHeight / 2, 0xA0A0A0);
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
