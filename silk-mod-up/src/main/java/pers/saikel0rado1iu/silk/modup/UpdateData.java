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

package pers.saikel0rado1iu.silk.modup;

import pers.saikel0rado1iu.silk.codex.SettingData;
import pers.saikel0rado1iu.silk.codex.SettingOption;
import pers.saikel0rado1iu.silk.modpass.ModDataExpansion;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.net.URL;

/**
 * <h2 style="color:FFC800">更新数据</h2>
 * 用于储存模组的更新数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public record UpdateData(ModDataExpansion modPass, SettingData updateSettings, UpdateState updateState, String minecraftVersion, String modVersion,
                         URL updateLink, boolean isArchived) implements ModPass {
	/**
	 * 构建方法
	 *
	 * @param modPass     拓展模组通
	 * @param settingData 设置数据
	 * @return 构建器
	 */
	public static Builder builder(ModDataExpansion modPass, SettingData settingData) {
		return builder(modPass, settingData, false);
	}
	
	/**
	 * 构建方法，设置模组是否存档
	 *
	 * @param modPass     拓展模组通
	 * @param settingData 设置数据
	 * @param isArchived  是否存档
	 * @return 构建器
	 */
	public static Builder builder(ModDataExpansion modPass, SettingData settingData, boolean isArchived) {
		return new Builder(modPass, settingData, isArchived);
	}
	
	/**
	 * 是否可以显示更新日志
	 *
	 * @param updateSettings 更新设置
	 * @return 是否可以显示
	 */
	public static boolean canShowChangelog(SettingData updateSettings) {
		return updateSettings.getValue(UpdateSettings.CHANGELOG).getValue(UpdateSettings.CAN_SHOW);
	}
	
	/**
	 * 设置是否可以显示更新日志
	 *
	 * @param updateSettings 更新设置
	 * @param canShow        是否可以显示
	 */
	public static void setCanShowChangelog(SettingData updateSettings, boolean canShow) {
		updateSettings.getValue(UpdateSettings.CHANGELOG).setValue(UpdateSettings.CAN_SHOW, canShow);
		updateSettings.save();
	}
	
	/**
	 * 获取正确的更新状态
	 *
	 * @return 正确的更新状态
	 */
	public UpdateState getUpdateState() {
		UpdateState state = updateState();
		if (state == UpdateState.MOD_LOG) {
			if (!getValue(UpdateSettings.SHOW_CHANGELOG)) state = UpdateState.DONE;
		} else if (state == UpdateState.STOP_UPDATE) {
			if (!getValue(UpdateSettings.STOP_UPDATING_WARNING)) state = UpdateState.DONE;
		} else if (state == UpdateState.UPDATE_FAIL) {
			if (!getValue(UpdateSettings.UPDATE_SYS_FAIL_WARNING)) state = UpdateState.DONE;
		}
		return state;
	}
	
	/**
	 * 获取设置数据的当前值
	 *
	 * @param option 设置选项
	 * @param <T>    选项值类型
	 * @return 选项的值
	 */
	public <T> T getValue(SettingOption<T> option) {
		return updateSettings.getValue(option);
	}
	
	/**
	 * 用于提供模组数据以基于模组数据实现功能
	 *
	 * @return 模组数据
	 */
	@Override
	public ModDataExpansion modData() {
		return modPass;
	}
	
	/**
	 * 更新数据的构建器
	 */
	public static final class Builder implements ModPass {
		/**
		 * 模组通
		 */
		public final ModDataExpansion modPass;
		/**
		 * 更新设置
		 */
		public final SettingData updateSettings;
		/**
		 * 是否存档
		 */
		public final boolean isArchived;
		private UpdateState updateState;
		private String minecraftVersion;
		private String modVersion;
		private URL updateLink;
		
		private Builder(ModDataExpansion modPass, SettingData updateSettings, boolean isArchived) {
			SettingData updateData = UpdateSettings.of(modPass, updateSettings);
			updateSettings.addOption(UpdateSettings.UPDATE_SETTINGS, updateData);
			this.modPass = modPass;
			this.updateSettings = updateData;
			this.updateState = UpdateState.NONE;
			this.isArchived = isArchived;
			this.minecraftVersion = "";
			this.modVersion = "";
			this.updateLink = null;
		}
		
		@Override
		public ModDataExpansion modData() {
			return modPass;
		}
		
		/**
		 * 设置更新状态
		 *
		 * @param updateState 更新状态
		 * @return 构建器
		 */
		public Builder updateState(UpdateState updateState) {
			this.updateState = updateState;
			return this;
		}
		
		/**
		 * 设置 Minecraft 版本
		 *
		 * @param minecraftVersion Minecraft 版本
		 * @return 构建器
		 */
		public Builder minecraftVersion(String minecraftVersion) {
			this.minecraftVersion = minecraftVersion;
			return this;
		}
		
		/**
		 * 设置模组版本
		 *
		 * @param modVersion 模组版本
		 * @return 构建器
		 */
		public Builder modVersion(String modVersion) {
			this.modVersion = modVersion;
			return this;
		}
		
		/**
		 * 设置更新链接
		 *
		 * @param updateLink 更新链接
		 * @return 构建器
		 */
		public Builder updateLink(URL updateLink) {
			this.updateLink = updateLink;
			return this;
		}
		
		/**
		 * 构建更新数据
		 *
		 * @return 更新数据
		 */
		public UpdateData build() {
			return new UpdateData(modPass, updateSettings, updateState, minecraftVersion, modVersion, updateLink, isArchived);
		}
	}
}
