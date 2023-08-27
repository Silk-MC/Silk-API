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

package pers.saikel0rado1iu.silk.util.screen.update;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import pers.saikel0rado1iu.silk.util.update.UpdateData;

import static pers.saikel0rado1iu.silk.util.update.UpdateData.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用于生成更新系统</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class UpdateSystem {
	private final UpdateData updateData;
	
	public UpdateSystem(UpdateData updateData) {
		this.updateData = updateData;
	}
	
	/**
	 * 设置更新屏幕
	 */
	public void setUpdateScreen(Screen parent) {
		if (updateData.getShowScreen()) {
			switch (updateData.getUpdateState()) {
				case NEW_MC_VER -> MinecraftClient.getInstance().setScreen(new NewMcVerNotifyScreen(parent, updateData, getLinkTrusted()));
				case THIS_MC_VER -> MinecraftClient.getInstance().setScreen(new ThisMcVerNotifyScreen(parent, updateData, getLinkTrusted()));
				case MOD_LOG -> {
					if (updateData.getConfigs().getConfig(SHOW_CHANGELOG, Boolean.class))
						MinecraftClient.getInstance().setScreen(new ShowChangelogScreen(parent, updateData, getLinkTrusted()));
					else UpdateToast.setToast(new ShowChangelogToast(updateData));
				}
				case STOP_UPDATE -> {
					if (updateData.getConfigs().getConfig(STOP_UPDATING_WARNING, Boolean.class))
						MinecraftClient.getInstance().setScreen(new StopUpdateWarningScreen(parent, updateData, getLinkTrusted()));
					else UpdateToast.setToast(new StopUpdateWarningToast(updateData));
				}
				case UPDATE_FAIL -> {
					if (updateData.getConfigs().getConfig(UPDATE_SYS_FAIL_WARNING, Boolean.class))
						MinecraftClient.getInstance().setScreen(new UpdateFailWarningScreen(parent, updateData, getLinkTrusted()));
					else UpdateToast.setToast(new UpdateFailWarningToast(updateData));
				}
			}
		}
	}
	
	/**
	 * 设置更新提示
	 */
	public void setUpdateToast() {
		if (updateData.getShowScreen()) {
			switch (updateData.getUpdateState()) {
				case NEW_MC_VER -> UpdateToast.setToast(new NewMcVerNotifyToast(updateData));
				case THIS_MC_VER -> UpdateToast.setToast(new ThisMcVerNotifyToast(updateData));
				case MOD_LOG -> UpdateToast.setToast(new ShowChangelogToast(updateData));
				case STOP_UPDATE -> UpdateToast.setToast(new StopUpdateWarningToast(updateData));
				case UPDATE_FAIL -> UpdateToast.setToast(new UpdateFailWarningToast(updateData));
			}
		}
	}
	
	/**
	 * 重写此方法以信任链接
	 */
	protected boolean getLinkTrusted() {
		return false;
	}
}
