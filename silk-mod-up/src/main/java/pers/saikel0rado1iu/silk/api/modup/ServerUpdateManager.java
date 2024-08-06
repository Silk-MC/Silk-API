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

package pers.saikel0rado1iu.silk.api.modup;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.api.modpass.log.Changelog;
import pers.saikel0rado1iu.silk.api.pattern.widget.WidgetTexts;
import pers.saikel0rado1iu.silk.impl.SilkModUp;

import java.net.URL;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">服务端更新管理器</h2>
 * 服务端更新管理器通过发送服务器消息通知用户需要进行模组更新
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class ServerUpdateManager extends UpdateManager {
	private static final ScheduledExecutorService SERVER_STARTED_UPDATE_POOL = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().daemon(true).build());
	private static final ScheduledExecutorService PLAYER_JOIN_UPDATE_POOL = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().daemon(true).build());
	protected String prevModVersion2 = prevModVersion;
	protected boolean canShowUpdateNotify2 = canShowUpdateNotify;
	
	/**
	 * @param updateDataBuilder 更新数据构建器
	 */
	public ServerUpdateManager(UpdateData.Builder updateDataBuilder) {
		super(updateDataBuilder);
	}
	
	/**
	 * 获取更新文本键
	 *
	 * @param updateState 更新状态
	 * @return 文本键
	 */
	public static String updateTextKey(UpdateState updateState) {
		return WidgetTexts.textKey(SilkModUp.getInstance(), "serverNotify." + updateState.toString().toLowerCase());
	}
	
	private static void serverStartedUpdate(ServerUpdateManager manager, Supplier<Future<UpdateData>> updateDataSupplier, MinecraftServer server) {
		SERVER_STARTED_UPDATE_POOL.scheduleAtFixedRate(() -> {
			if (!manager.canShowUpdateNotify) return;
			try {
				Future<UpdateData> updateDataFuture = updateDataSupplier.get();
				while (!updateDataFuture.isDone()) Thread.yield();
				UpdateData updateData = updateDataFuture.get();
				if (!manager.prevModVersion.equals(updateData.modVersion())) manager.canShowUpdateNotify = false;
				manager.prevModVersion = updateData.modVersion();
				Future<URL> urlFuture = ModUpdater.downloadUrl(updateData);
				while (!urlFuture.isDone() && !urlFuture.isCancelled()) Thread.yield();
				URL url = urlFuture.get();
				Text modName = updateData.modData().i18nName().copy().styled(style -> style
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://modrinth.com/mod/%s", updateData.modData().slug())))
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, WidgetTexts.text(SilkModUp.getInstance(), "serverNotify.modName.hover")))
						.withColor(Formatting.GREEN));
				Text updateText = WidgetTexts.text(SilkModUp.getInstance(), "serverNotify.update").styled(style -> style
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.toString()))
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, WidgetTexts.text(SilkModUp.getInstance(), "serverNotify.update.hover")))
						.withColor(Formatting.GREEN));
				switch (updateData.getUpdateState()) {
					case NEW_MC_VER -> server.sendMessage(
							Text.translatable(updateTextKey(UpdateState.NEW_MC_VER), modName, updateData.modVersion(), updateData.minecraftVersion(), updateText));
					case THIS_MC_VER -> server.sendMessage(
							Text.translatable(updateTextKey(UpdateState.THIS_MC_VER), modName, updateData.modVersion(), updateText));
					case MOD_LOG -> server.sendMessage(
							Text.translatable(updateTextKey(UpdateState.MOD_LOG), modName, Changelog.read(updateData, String.format("%s_%s", Locale.getDefault().getLanguage(), Locale.getDefault().getCountry().toLowerCase()))));
					case STOP_UPDATE -> server.sendMessage(
							Text.translatable(updateTextKey(UpdateState.STOP_UPDATE), modName));
					case UPDATE_FAIL -> server.sendMessage(
							Text.translatable(updateTextKey(UpdateState.UPDATE_FAIL), modName));
					case DONE -> manager.canShowUpdateNotify = true;
					case NONE -> {
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				manager.canShowUpdateNotify = false;
				String msg = "Computation Error: An unexpected issue occurred during the update check computation process. Please check the specific error details.";
				SilkModUp.getInstance().logger().error(msg, e);
			}
		}, 0, 1, TimeUnit.MINUTES);
	}
	
	private static void playerJoinUpdate(ServerUpdateManager manager, Supplier<Future<UpdateData>> updateDataSupplier, ServerPlayNetworkHandler handler) {
		PLAYER_JOIN_UPDATE_POOL.scheduleAtFixedRate(() -> {
			if (!manager.canShowUpdateNotify2) return;
			try {
				Future<UpdateData> updateDataFuture = updateDataSupplier.get();
				while (!updateDataFuture.isDone() && !updateDataFuture.isCancelled()) Thread.yield();
				UpdateData updateData = updateDataFuture.get();
				if (!manager.prevModVersion2.equals(updateData.modVersion())) manager.canShowUpdateNotify2 = false;
				manager.prevModVersion2 = updateData.modVersion();
				Future<URL> urlFuture = ModUpdater.downloadUrl(updateData);
				while (!urlFuture.isDone() && !urlFuture.isCancelled()) Thread.yield();
				URL url = urlFuture.get();
				Text modName = updateData.modData().i18nName().copy().styled(style -> style
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, String.format("https://modrinth.com/mod/%s", updateData.modData().slug())))
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, WidgetTexts.text(SilkModUp.getInstance(), "serverNotify.modName.hover")))
						.withColor(Formatting.GREEN));
				Text updateText = WidgetTexts.text(SilkModUp.getInstance(), "serverNotify.update").styled(style -> style
						.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url.toString()))
						.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, WidgetTexts.text(SilkModUp.getInstance(), "serverNotify.update.hover")))
						.withColor(Formatting.GREEN));
				switch (updateData.getUpdateState()) {
					case NEW_MC_VER -> handler.player.sendMessageToClient(
							Text.translatable(updateTextKey(UpdateState.NEW_MC_VER), modName, updateData.modVersion(), updateData.minecraftVersion(), updateText), false);
					case THIS_MC_VER -> handler.player.sendMessageToClient(
							Text.translatable(updateTextKey(UpdateState.THIS_MC_VER), modName, updateData.modVersion(), updateText), false);
					case STOP_UPDATE -> handler.player.sendMessageToClient(
							Text.translatable(updateTextKey(UpdateState.STOP_UPDATE), modName), false);
					case UPDATE_FAIL -> handler.player.sendMessageToClient(
							Text.translatable(updateTextKey(UpdateState.UPDATE_FAIL), modName), false);
					case DONE -> manager.canShowUpdateNotify2 = true;
					case MOD_LOG, NONE -> {
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				manager.canShowUpdateNotify2 = false;
				String msg = "Computation Error: An unexpected issue occurred during the update check computation process. Please check the specific error details.";
				SilkModUp.getInstance().logger().error(msg, e);
			}
		}, 0, 1, TimeUnit.MINUTES);
	}
	
	/**
	 * 更新操作
	 *
	 * @param updateDataSupplier 更新数据提供器
	 */
	@Override
	public void operation(Supplier<Future<UpdateData>> updateDataSupplier) {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> serverStartedUpdate(this, updateDataSupplier, server));
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> playerJoinUpdate(this, updateDataSupplier, handler));
	}
}
