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

import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModExpansionData;
import pers.saikel0rado1iu.silk.util.config.ConfigData;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组更新数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public final class UpdateData {
	/**
	 * 是否开启更新提示
	 */
	public static final String UPDATE_NOTIFY = "update_notify";
	/**
	 * 是否显示更新日志
	 */
	public static final String SHOW_CHANGELOG = "show_changelog";
	/**
	 * 是否检查新 MC 版本模组更新
	 */
	public static final String CHECK_NEW_MC_VER_MOD = "check_new_mc_ver_mod";
	/**
	 * 是否关闭停止更新警告
	 */
	public static final String STOP_UPDATING_WARNING = "stop_updating_warning";
	/**
	 * 是否关闭更新系统失效警告
	 */
	public static final String UPDATE_SYS_FAIL_WARNING = "update_sys_fail_warning";
	/**
	 * 选择更新方式：手动下载、自动下载、自动更新
	 */
	public static final String UPDATE_MODE = "update_mode";
	/**
	 * 选择更新通道：正式版、测试版、开发版
	 */
	public static final String UPDATE_CHANNEL = "update_channel";
	private final String key;
	private final ModExpansionData mod;
	private final ConfigData data;
	private final boolean updating;
	private final String batName;
	private final Path batPath;
	private final String updateBasicLink;
	
	@SilkApi
	public UpdateData(ModExpansionData mod, ConfigData data, String key) {
		this(mod, data, key, true);
	}
	
	@SilkApi
	public UpdateData(ModExpansionData mod, ConfigData data, String key, boolean updating) {
		this.mod = mod;
		this.data = data.addSwitch(UPDATE_NOTIFY, true)
				.addSwitch(SHOW_CHANGELOG, true)
				.addSwitch(CHECK_NEW_MC_VER_MOD, true)
				.addSwitch(STOP_UPDATING_WARNING, true)
				.addSwitch(UPDATE_SYS_FAIL_WARNING, true)
				.addOption(UPDATE_MODE, Mode.MANUAL_DOWNLOAD)
				.addOption(UPDATE_CHANNEL, Channel.RELEASE)
				.addSubConfigs("changelog", ConfigData.builder(mod).type(ConfigData.Type.DEV).build()
						.addSwitch("show", false));
		this.key = key;
		this.updating = updating;
		this.batName = mod.getId() + ".bat";
		this.batPath = Paths.get(mod.getPath().toString(), batName);
		this.updateBasicLink = "https://api.modrinth.com/v2/project/" + mod.getSlug() + "/version?loaders=[%22fabric%22]";
	}
	
	@ApiStatus.Internal
	public ConfigData getData() {
		return data;
	}
	
	@ApiStatus.Internal
	public void load() {
		data.getMainConfig().reader().load();
	}
	
	@ApiStatus.Internal
	public void save() {
		data.getMainConfig().writer().save();
	}
	
	@ApiStatus.Internal
	public ModExpansionData getMod() {
		return mod;
	}
	
	@ApiStatus.Internal
	public String getKey() {
		return key;
	}
	
	@ApiStatus.Internal
	public String getUpdateBasicLink() {
		return updateBasicLink;
	}
	
	@ApiStatus.Internal
	public boolean getUpdating() {
		return updating;
	}
	
	@ApiStatus.Internal
	public String getBatName() {
		return batName;
	}
	
	@ApiStatus.Internal
	public Path getBatPath() {
		return batPath;
	}
	
	@ApiStatus.Internal
	public boolean getCanShowChangelog() {
		return data.getConfig("changelog", ConfigData.class).getConfig("show", Boolean.class);
	}
	
	@ApiStatus.Internal
	public void setCanShowChangelog(boolean value) {
		data.getConfig("changelog", ConfigData.class).setConfig("show", value);
	}
	
	@ApiStatus.Internal
	public boolean getUpdateNotify() {
		return data.getConfig(UPDATE_NOTIFY, Boolean.class);
	}
	
	@ApiStatus.Internal
	public void setUpdateNotify(boolean value) {
		data.setConfig(UPDATE_NOTIFY, value);
	}
	
	@ApiStatus.Internal
	public boolean getShowChangelog() {
		return data.getConfig(SHOW_CHANGELOG, Boolean.class);
	}
	
	@ApiStatus.Internal
	public void setShowChangelog(boolean value) {
		data.setConfig(SHOW_CHANGELOG, value);
	}
	
	@ApiStatus.Internal
	public boolean getCheckNewMcVerMod() {
		return data.getConfig(CHECK_NEW_MC_VER_MOD, Boolean.class);
	}
	
	@ApiStatus.Internal
	public void setCheckNewMcVerMod(boolean value) {
		data.setConfig(CHECK_NEW_MC_VER_MOD, value);
	}
	
	@ApiStatus.Internal
	public boolean getStopUpdatingWarning() {
		return data.getConfig(STOP_UPDATING_WARNING, Boolean.class);
	}
	
	@ApiStatus.Internal
	public void setStopUpdatingWarning(boolean value) {
		data.setConfig(STOP_UPDATING_WARNING, value);
	}
	
	@ApiStatus.Internal
	public boolean getUpdateSysFailWarning() {
		return data.getConfig(UPDATE_SYS_FAIL_WARNING, Boolean.class);
	}
	
	@ApiStatus.Internal
	public void setUpdateSysFailWarning(boolean value) {
		data.setConfig(UPDATE_SYS_FAIL_WARNING, value);
	}
	
	@ApiStatus.Internal
	public Mode getUpdateMode() {
		return data.getConfig(UPDATE_MODE, Mode.class);
	}
	
	@ApiStatus.Internal
	public void setUpdateMode(Mode value) {
		data.setConfig(UPDATE_MODE, value);
	}
	
	@ApiStatus.Internal
	public Channel getUpdateChannel() {
		return data.getConfig(UPDATE_CHANNEL, Channel.class);
	}
	
	@ApiStatus.Internal
	public void setUpdateChannel(Channel value) {
		data.setConfig(UPDATE_CHANNEL, value);
	}
	
	public enum Mode {MANUAL_DOWNLOAD, AUTO_DOWNLOAD, AUTO_UPDATE}
	
	public enum Channel {RELEASE, BETA, ALPHA}
}
