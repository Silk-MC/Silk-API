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

package pers.saikel0rado1iu.silk.update;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import pers.saikel0rado1iu.silk.impl.SilkModUp;
import pers.saikel0rado1iu.silk.modup.UpdateData;
import pers.saikel0rado1iu.silk.modup.UpdateManager;
import pers.saikel0rado1iu.silk.modup.UpdateSettings;
import pers.saikel0rado1iu.silk.pattern.screen.LinkTrusted;
import pers.saikel0rado1iu.silk.update.screen.*;
import pers.saikel0rado1iu.silk.update.toast.*;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * <h2 style="color:FFC800">客户端更新管理器</h2>
 * 客户端更新管理器通过弹出屏幕通知用户需要进行模组更新
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public class ClientUpdateManager extends UpdateManager implements LinkTrusted {
	private long titleScreenTime = System.currentTimeMillis() - 5000;
	private Future<UpdateData> titleScreenUpdateDataFuture;
	
	/**
	 * @param updateDataBuilder 更新数据构建器
	 */
	public ClientUpdateManager(UpdateData.Builder updateDataBuilder) {
		super(updateDataBuilder);
	}
	
	private static void update(ClientUpdateManager manager, Supplier<Future<UpdateData>> updateDataSupplier, MinecraftClient client, Optional<Screen> parent) {
		if (System.currentTimeMillis() - manager.titleScreenTime < 5000) return;
		if (!manager.canShowUpdateNotify) return;
		try {
			if (manager.titleScreenUpdateDataFuture == null) manager.titleScreenUpdateDataFuture = updateDataSupplier.get();
			if (!manager.titleScreenUpdateDataFuture.isDone()) return;
			UpdateData updateData = manager.titleScreenUpdateDataFuture.get();
			manager.titleScreenUpdateDataFuture = updateDataSupplier.get();
			manager.titleScreenTime = System.currentTimeMillis();
			if (manager.prevModVersion.equals(updateData.modVersion())) {
				manager.prevModVersion = updateData.modVersion();
				return;
			} else {
				manager.canShowUpdateNotify = false;
				manager.prevModVersion = updateData.modVersion();
			}
			if (updateData.getValue(UpdateSettings.UPDATE_NOTIFY)) {
				switch (updateData.getUpdateState()) {
					case NEW_MC_VER -> client.setScreen(new NewMcVerNotifyScreen(parent, updateData, manager, manager.linkTrusted()));
					case THIS_MC_VER -> client.setScreen(new ThisMcVerNotifyScreen(parent, updateData, manager, manager.linkTrusted()));
					case MOD_LOG -> {
						if (updateData.getValue(UpdateSettings.SHOW_CHANGELOG)) {
							MinecraftClient.getInstance().setScreen(new ShowChangelogScreen(Optional.empty(), updateData, manager, manager.linkTrusted()));
						} else {
							UpdateToast.setToast(new ShowChangelogToast(updateData, manager));
						}
					}
					case STOP_UPDATE -> {
						if (updateData.getValue(UpdateSettings.STOP_UPDATING_WARNING)) {
							MinecraftClient.getInstance().setScreen(new StopUpdateWarningScreen(Optional.empty(), updateData, manager, manager.linkTrusted()));
						} else {
							UpdateToast.setToast(new StopUpdateWarningToast(updateData, manager));
						}
					}
					case UPDATE_FAIL -> {
						if (updateData.getValue(UpdateSettings.UPDATE_SYS_FAIL_WARNING)) {
							MinecraftClient.getInstance().setScreen(new UpdateFailWarningScreen(Optional.empty(), updateData, manager, manager.linkTrusted()));
						} else {
							UpdateToast.setToast(new UpdateFailWarningToast(updateData, manager));
						}
					}
					case DONE -> manager.canShowUpdateNotify = true;
					case NONE -> {
					}
				}
			} else {
				switch (updateData.getUpdateState()) {
					case NEW_MC_VER -> UpdateToast.setToast(new NewMcVerNotifyToast(updateData, manager));
					case THIS_MC_VER -> UpdateToast.setToast(new ThisMcVerNotifyToast(updateData, manager));
					case MOD_LOG -> UpdateToast.setToast(new ShowChangelogToast(updateData, manager));
					case STOP_UPDATE -> UpdateToast.setToast(new StopUpdateWarningToast(updateData, manager));
					case UPDATE_FAIL -> UpdateToast.setToast(new UpdateFailWarningToast(updateData, manager));
					case DONE -> manager.canShowUpdateNotify = true;
					case NONE -> {
					}
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			manager.canShowUpdateNotify = false;
			String msg = "Computation Error: An unexpected issue occurred during the update check computation process. Please check the specific error details.";
			SilkModUp.getInstance().logger().error(msg, e);
		}
	}
	
	/**
	 * 设置可以显示更新提示
	 *
	 * @param canShowUpdateNotify 可以显示更新提示
	 */
	public void setCanShowUpdateNotify(boolean canShowUpdateNotify) {
		this.canShowUpdateNotify = canShowUpdateNotify;
	}
	
	/**
	 * 更新操作
	 *
	 * @param updateDataSupplier 更新数据提供器
	 */
	@Override
	public void operation(Supplier<Future<UpdateData>> updateDataSupplier) {
		ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (!(screen instanceof TitleScreen)) return;
			ScreenEvents.beforeTick(screen).register(titleScreen -> update(this, updateDataSupplier, client, Optional.of(titleScreen)));
		});
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> update(this, updateDataSupplier, MinecraftClient.getInstance(), Optional.empty()));
	}
}
