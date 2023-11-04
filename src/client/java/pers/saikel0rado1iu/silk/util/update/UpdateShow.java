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

package pers.saikel0rado1iu.silk.util.update;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModExtendedData;
import pers.saikel0rado1iu.silk.util.TextUtil;
import pers.saikel0rado1iu.silk.util.config.ConfigData;
import pers.saikel0rado1iu.silk.util.update.screen.*;
import pers.saikel0rado1iu.silk.util.update.toast.*;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * <p><b style="color:FFC800"><font size="+1">用于生成更新系统</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class UpdateShow {
	public static final ScheduledExecutorService UPDATE_THREAD_POOL = new ScheduledThreadPoolExecutor(0, new BasicThreadFactory.Builder().daemon(true).build());
	public static final String KEY = "update.";
	private final CheckUpdateThread updateThread;
	private final ModUpdateThread modUpdateThread;
	private boolean canShowScreen = true;
	
	public UpdateShow(CheckUpdateThread updateThread) {
		this.updateThread = updateThread;
		this.modUpdateThread = new ModUpdateThread(updateThread);
	}
	
	@ApiStatus.Internal
	public ModExtendedData getMod() {
		return updateThread.getMod();
	}
	
	@ApiStatus.Internal
	public UpdateData getUpdateData() {
		return updateThread.getData();
	}
	
	@ApiStatus.Internal
	public ConfigData getConfigData() {
		return updateThread.getData().getData();
	}
	
	@ApiStatus.Internal
	public Text getTitle(String key) {
		return Text.translatable(TextUtil.widgetTitle(Silk.DATA, KEY + key), updateThread.getMod().getLocalizedName()).setStyle(Style.EMPTY.withBold(true).withColor(updateThread.getMod().getThemeColor()));
	}
	
	@ApiStatus.Internal
	public Text getTitle(String key, String value) {
		return Text.translatable(TextUtil.widgetTitle(Silk.DATA, KEY + key), updateThread.getMod().getLocalizedName(), value).setStyle(Style.EMPTY.withBold(true).withColor(updateThread.getMod().getThemeColor()));
	}
	
	@ApiStatus.Internal
	public Text getToastText(String key) {
		return Text.translatable(TextUtil.widgetText(Silk.DATA, KEY + key), updateThread.getMod().getLocalizedName()).setStyle(Style.EMPTY);
	}
	
	@ApiStatus.Internal
	public Text getWarText(String key) {
		return Text.translatable(TextUtil.widgetText(Silk.DATA, KEY + key), updateThread.getMod().getLocalizedName()).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.RED));
	}
	
	@ApiStatus.Internal
	public Text getVerText(String key) {
		String ver = getUpdateThread().getUpdateModVer();
		return Text.translatable(TextUtil.widgetText(Silk.DATA, KEY + key), updateThread.getMod().getLocalizedName(), ver.substring(0, ver.indexOf("-")));
	}
	
	@ApiStatus.Internal
	public void runUpdateThread() {
		UPDATE_THREAD_POOL.scheduleAtFixedRate(updateThread, 0, 1, TimeUnit.MINUTES);
	}
	
	@ApiStatus.Internal
	public void showUpdate(Screen parent) {
		String str = "modmenu.nameTranslation." + Silk.DATA.getId();
		if (str.equals(Text.translatable(str).getString())) return;
		if (!canShowScreen) return;
		if (updateThread.getUpdateModVer() == null) return;
		if (updateThread.getData().getUpdateNotify()) showUpdateScreen(parent);
		else showUpdateToast();
	}
	
	@ApiStatus.Internal
	public CheckUpdateThread getUpdateThread() {
		return updateThread;
	}
	
	@ApiStatus.Internal
	public void setCanShowScreen(boolean canShowScreen) {
		this.canShowScreen = canShowScreen;
	}
	
	@ApiStatus.Internal
	public final void updateMod() {
		UPDATE_THREAD_POOL.execute(modUpdateThread);
	}
	
	@ApiStatus.Internal
	public String getUpdateProgress() {
		return String.format("%.2f", modUpdateThread.getUpdateProgress());
	}
	
	/**
	 * 设置更新屏幕
	 */
	private void showUpdateScreen(Screen parent) {
		switch (updateThread.getUpdateState()) {
			case NEW_MC_VER -> MinecraftClient.getInstance().setScreen(new NewMcVerNotifyScreen(parent, this, linkTrusted()));
			case THIS_MC_VER -> MinecraftClient.getInstance().setScreen(new ThisMcVerNotifyScreen(parent, this, linkTrusted()));
			case MOD_LOG -> {
				if (updateThread.getData().getShowChangelog()) MinecraftClient.getInstance().setScreen(new ShowChangelogScreen(parent, this, linkTrusted()));
				else UpdateToast.setToast(new ShowChangelogToast(this));
			}
			case STOP_UPDATE -> {
				if (updateThread.getData().getStopUpdatingWarning())
					MinecraftClient.getInstance().setScreen(new StopUpdateWarningScreen(parent, this, linkTrusted()));
				else UpdateToast.setToast(new StopUpdateWarningToast(this));
			}
			case UPDATE_FAIL -> {
				if (updateThread.getData().getUpdateSysFailWarning())
					MinecraftClient.getInstance().setScreen(new UpdateFailWarningScreen(parent, this, linkTrusted()));
				else UpdateToast.setToast(new UpdateFailWarningToast(this));
			}
			case NONE -> getMod().logger().info("Checking the new version of mod..." + Silk.DATA.getInfo());
		}
	}
	
	/**
	 * 设置更新提示
	 */
	private void showUpdateToast() {
		switch (updateThread.getUpdateState()) {
			case NEW_MC_VER -> UpdateToast.setToast(new NewMcVerNotifyToast(this));
			case THIS_MC_VER -> UpdateToast.setToast(new ThisMcVerNotifyToast(this));
			case MOD_LOG -> UpdateToast.setToast(new ShowChangelogToast(this));
			case STOP_UPDATE -> UpdateToast.setToast(new StopUpdateWarningToast(this));
			case UPDATE_FAIL -> UpdateToast.setToast(new UpdateFailWarningToast(this));
			case NONE -> getMod().logger().info("Checking the new version of mod..." + Silk.DATA.getInfo());
		}
	}
	
	/**
	 * 重写此方法以信任链接
	 */
	protected boolean linkTrusted() {
		return false;
	}
}
