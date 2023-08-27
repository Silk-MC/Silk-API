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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModExpansionData;
import pers.saikel0rado1iu.silk.util.Minecraft;
import pers.saikel0rado1iu.silk.util.config.ConfigData;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * <p><b style="color:FFC800"><font size="+1">用于模组更新数据</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
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
	/**
	 * 更新配置子配置
	 */
	public static final String UPDATE_CONFIG = "update";
	public final String BAT_NAME;
	public final ScheduledExecutorService UPDATE_THREAD_POOL = new ScheduledThreadPoolExecutor(0,
			new BasicThreadFactory.Builder().daemon(true).build());
	public final Path BAT_PATH;
	private final String UPDATE_BASIC_LINK;
	private final ModExpansionData mod;
	private final ConfigData configs;
	private final boolean stopUpdating;
	boolean updatingFail = false;
	State state = State.UPDATE_FAIL;
	private boolean showScreen = true;
	boolean canCheckUpdate = true;
	private ModDownloadThread thread;
	private URL updateBasicLink;
	private String updateModVer;
	private String updateMinecraftVer;
	
	public UpdateData(ModExpansionData mod, ConfigData configs) {
		this(mod, configs, false);
	}
	
	public UpdateData(ModExpansionData mod, ConfigData configs, boolean stopUpdating) {
		this.mod = mod;
		this.configs = ConfigData.builder(mod).build()
				.addSwitch(UPDATE_NOTIFY, true)
				.addSwitch(SHOW_CHANGELOG, true)
				.addSwitch(CHECK_NEW_MC_VER_MOD, true)
				.addSwitch(STOP_UPDATING_WARNING, false)
				.addSwitch(UPDATE_SYS_FAIL_WARNING, false)
				.addOption(UPDATE_MODE, Mode.MANUAL_DOWNLOAD)
				.addOption(UPDATE_CHANNEL, Channel.RELEASE)
				.addSubConfigs("changelog", ConfigData.builder(mod).type(ConfigData.Type.DEV).build()
						.addSwitch("show", false));
		configs.addSubConfigs("", this.configs);
		this.stopUpdating = stopUpdating;
		BAT_NAME = mod.getName() + ".bat";
		BAT_PATH = Paths.get(mod.getPath().toString(), BAT_NAME);
		UPDATE_BASIC_LINK = "https://api.modrinth.com/v2/project/" + mod.getSlug() + "/version?loaders=[%22fabric%22]";
	}
	
	public static String getFileSha1(Path path) {
		try (InputStream in = Files.newInputStream(path)) {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] buffer = new byte[1024 * 1024 * 10];
			
			int len;
			while ((len = in.read(buffer)) > 0) digest.update(buffer, 0, len);
			StringBuilder sha1 = new StringBuilder(new BigInteger(1, digest.digest()).toString(16));
			int length = 40 - sha1.length();
			if (length > 0) for (int i = 0; i < length; i++) sha1.insert(0, "0");
			return sha1.toString();
		} catch (IOException | NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ModExpansionData getMod() {
		return mod;
	}
	
	public ConfigData getConfigs() {
		return configs;
	}
	
	/**
	 * 重置能检查更新
	 */
	public void resetCanCheckUpdate() {
		canCheckUpdate = true;
	}
	
	/**
	 * 设置能检查更新
	 */
	public void setCanCheckUpdate() {
		canCheckUpdate = false;
	}
	
	/**
	 * 获取更新模组版本
	 */
	public String getUpdateModVersion() {
		return updateModVer;
	}
	
	/**
	 * 获取更新 MC 版本
	 */
	public String getUpdateMinecraftVersion() {
		return updateMinecraftVer;
	}
	
	/**
	 * 获取更新进度
	 */
	public float getUpdateProgress() {
		return thread != null ? thread.getUpdateProgress() : 100;
	}
	
	/**
	 * 获取更新状态
	 */
	public State getUpdateState() {
		return state;
	}
	
	/**
	 * 获取是否更新失败
	 */
	public boolean getUpdatingFail() {
		return updatingFail;
	}
	
	/**
	 * 获取更新链接
	 */
	public URL getUpdateLink() {
		return switch (configs.getConfig(UPDATE_MODE, Mode.class)) {
			case MANUAL_DOWNLOAD -> getManualDownloadUpdateLink();
			case AUTO_DOWNLOAD -> getAutoDownloadUpdateLink();
			case AUTO_UPDATE -> getAutoUpdateLink();
		};
	}
	
	/**
	 * 更新模组函数
	 */
	public boolean updateMod(URL downloadLink) {
		if (downloadLink == null) {
			updatingFail = true;
			return true;
		}
		return switch (configs.getConfig(UPDATE_MODE, Mode.class)) {
			case MANUAL_DOWNLOAD -> manualDownload(downloadLink);
			case AUTO_DOWNLOAD -> autoDownload(downloadLink);
			case AUTO_UPDATE -> autoUpdate(downloadLink);
		};
	}
	
	/**
	 * 读取更新日志
	 *
	 * @return 用于显示日志的文本列表组件
	 */
	@SilkApi
	public String readChangelog(Path logPath, boolean isChinese) {
		try {
			StringBuilder log = new StringBuilder().append(Files.readString(logPath, StandardCharsets.UTF_8));
			log = new StringBuilder(log.toString().replaceAll("\r", "\n"));
			log = new StringBuilder(log.toString().replaceAll("\n\n", "\n"));
			if (isChinese) log = new StringBuilder(log.toString().replaceAll("\t", "—"));
			else log = new StringBuilder(log.toString().replaceAll("\t", "  "));
			return log.toString();
		} catch (IOException e) {
			return "Changelog does not exist! -- by " + Silk.DATA.getName();
		}
	}
	
	/**
	 * 设置已经显示更新屏幕
	 */
	public void setShowScreen() {
		showScreen = false;
	}
	
	/**
	 * 复位已经显示更新屏幕
	 */
	public void resetShowScreen() {
		showScreen = true;
	}
	
	/**
	 * 获取是否显示更新屏幕
	 */
	public boolean getShowScreen() {
		return showScreen;
	}
	
	private int compareMcVer(String originalVer, String compareVer) {
		originalVer = StringUtils.substringBefore(originalVer, "-");
		compareVer = StringUtils.substringBefore(compareVer, "-");
		if (originalVer.contains("w") || originalVer.contains("a") || compareVer.contains("w") || compareVer.contains("a"))
			return 0;
		
		ArrayList<String> originalVerList = new ArrayList<>(Arrays.asList(originalVer.split("\\.")));
		originalVer = originalVer.replaceAll("\\.", "");
		if (originalVerList.size() < 3) originalVer += "0";
		ArrayList<String> compareVerList = new ArrayList<>(Arrays.asList(compareVer.split("\\.")));
		compareVer = compareVer.replaceAll("\\.", "");
		if (compareVerList.size() < 3) compareVer += "0";
		
		return Integer.compare(Integer.parseInt(compareVer), Integer.parseInt(originalVer));
	}
	
	private String getLatestMcVer(JsonObject data) {
		List<String> VerList = new ArrayList<>(4);
		JsonArray VerArray = data.getAsJsonArray("game_versions");
		for (JsonElement object : VerArray) VerList.add(object.getAsString());
		String latestMinecraftVer = "";
		for (int count = 0; count < VerList.size() - 1; count++) {
			latestMinecraftVer = compareMcVer(VerList.get(count), VerList.get(count + 1)) < 0 ?
					VerList.get(count) : VerList.get(count + 1);
		}
		return latestMinecraftVer;
	}
	
	State checkUpdate() {
		// 检查是否已停止更新
		if (stopUpdating) return State.STOP_UPDATE;
		State state;
		return switch (configs.getConfig(UPDATE_CHANNEL, Channel.class)) {
			case ALPHA: {
				state = checkAlphaUpdate();
				if (state != State.UPDATE_FAIL)
					yield state;
			}
			case BETA: {
				state = checkBetaUpdate();
				if (state != State.UPDATE_FAIL)
					yield state;
			}
			case RELEASE: {
				state = checkReleaseUpdate();
				if (state != State.UPDATE_FAIL)
					updatingFail = false;
				yield state;
			}
		};
	}
	
	private State checkReleaseUpdate() {
		// 检查是否联网，如未联网则不进行更新检查
		try {
			URL testOnline = new URL("https://www.minecraft.net");
			HttpsURLConnection connection = (HttpsURLConnection) testOnline.openConnection();
			connection.setConnectTimeout(100);
			connection.getResponseCode();
		} catch (IOException unknownHostException) {
			return State.UPDATE_FAIL;
		}
		
		// 判断是否有更新
		try {
			String checkUpdateBasic = UPDATE_BASIC_LINK + "&version_type=release";
			URL checkUpdateLink;
			if (configs.getConfig(CHECK_NEW_MC_VER_MOD, Boolean.class)) {
				checkUpdateLink = new URL(checkUpdateBasic);
				updateBasicLink = checkUpdateLink;
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				InputStream is = checkUpdateLink.openStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
				// 判断更新
				String updateVer = getLatestMcVer(data);
				JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (compareMcVer(Minecraft.DATA.getVersion(), updateVer) > 0) return State.NEW_MC_VER;
				if (compareMcVer(Minecraft.DATA.getVersion(), updateVer) > 0) return State.NEW_MC_VER;
				if (getFileSha1(mod.getJar()).equals(sha1Code)) {
					if (updateVer.equals(Minecraft.DATA.getVersion()))
						return State.THIS_MC_VER;
					return State.NEW_MC_VER;
				}
			} else {
				checkUpdateLink = new URL(checkUpdateBasic + "&game_versions=[%22" + Minecraft.DATA.getVersion() + "%22]");
				updateBasicLink = checkUpdateLink;
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				InputStream is = checkUpdateLink.openStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
				// 判断更新
				JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (getFileSha1(mod.getJar()).equals(sha1Code))
					return State.THIS_MC_VER;
			}
			configs.reader().load();
			if (configs.getConfig("changelog", ConfigData.class).getConfig("show", Boolean.class)) {
				configs.getConfig("changelog", ConfigData.class).setConfig("show", false);
				configs.writer().save();
				return State.MOD_LOG;
			}
			return State.UPDATE_FAIL;
		} catch (IOException e) {
			return State.UPDATE_FAIL;
		}
	}
	
	private State checkBetaUpdate() {
		// 检查是否联网，如未联网则不进行更新检查
		try {
			URL testOnline = new URL("https://www.minecraft.net");
			HttpsURLConnection connection = (HttpsURLConnection) testOnline.openConnection();
			connection.setConnectTimeout(100);
			connection.getResponseCode();
		} catch (IOException unknownHostException) {
			return State.UPDATE_FAIL;
		}
		
		// 判断是否有更新
		try {
			String checkUpdateBasic = UPDATE_BASIC_LINK + "&version_type=beta";
			URL checkUpdateLink;
			if (configs.getConfig(CHECK_NEW_MC_VER_MOD, Boolean.class)) {
				checkUpdateLink = new URL(checkUpdateBasic);
				updateBasicLink = checkUpdateLink;
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				InputStream is = checkUpdateLink.openStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
				// 判断更新
				String updateVer = getLatestMcVer(data);
				JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (compareMcVer(Minecraft.DATA.getVersion(), updateVer) > 0) return State.NEW_MC_VER;
				if (getFileSha1(mod.getJar()).equals(sha1Code)) {
					if (updateVer.equals(Minecraft.DATA.getVersion()))
						return State.THIS_MC_VER;
					return State.NEW_MC_VER;
				}
			} else {
				checkUpdateLink = new URL(checkUpdateBasic + "&game_versions=[%22" + Minecraft.DATA.getVersion() + "%22]");
				updateBasicLink = checkUpdateLink;
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				InputStream is = checkUpdateLink.openStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
				// 判断更新
				JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (getFileSha1(mod.getJar()).equals(sha1Code))
					return State.THIS_MC_VER;
			}
			configs.reader().load();
			if (configs.getConfig("changelog", ConfigData.class).getConfig("show", Boolean.class)) {
				configs.getConfig("changelog", ConfigData.class).setConfig("show", false);
				configs.writer().save();
				return State.MOD_LOG;
			}
			return State.UPDATE_FAIL;
		} catch (IOException e) {
			return State.UPDATE_FAIL;
		}
	}
	
	private State checkAlphaUpdate() {
		// 检查是否联网，如未联网则不进行更新检查
		try {
			URL testOnline = new URL("https://www.minecraft.net");
			HttpsURLConnection connection = (HttpsURLConnection) testOnline.openConnection();
			connection.setConnectTimeout(100);
			connection.getResponseCode();
		} catch (IOException unknownHostException) {
			return State.UPDATE_FAIL;
		}
		
		// 判断是否有更新
		try {
			String checkUpdateBasic = UPDATE_BASIC_LINK + "&version_type=alpha";
			URL checkUpdateLink;
			if (configs.getConfig(CHECK_NEW_MC_VER_MOD, Boolean.class)) {
				checkUpdateLink = new URL(checkUpdateBasic);
				updateBasicLink = checkUpdateLink;
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				InputStream is = checkUpdateLink.openStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
				// 判断更新
				String updateVer = getLatestMcVer(data);
				JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (compareMcVer(Minecraft.DATA.getVersion(), updateVer) > 0) return State.NEW_MC_VER;
				if (getFileSha1(mod.getJar()).equals(sha1Code)) {
					if (updateVer.equals(Minecraft.DATA.getVersion()))
						return State.THIS_MC_VER;
					return State.NEW_MC_VER;
				}
			} else {
				checkUpdateLink = new URL(checkUpdateBasic + "&game_versions=[%22" + Minecraft.DATA.getVersion() + "%22]");
				updateBasicLink = checkUpdateLink;
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				InputStream is = checkUpdateLink.openStream();
				InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
				// 判断更新
				JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (getFileSha1(mod.getJar()).equals(sha1Code))
					return State.THIS_MC_VER;
			}
			configs.reader().load();
			if (configs.getConfig("changelog", ConfigData.class).getConfig("show", Boolean.class)) {
				configs.getConfig("changelog", ConfigData.class).setConfig("show", false);
				configs.writer().save();
				return State.MOD_LOG;
			}
			return State.UPDATE_FAIL;
		} catch (IOException e) {
			return State.UPDATE_FAIL;
		}
	}
	
	private URL getManualDownloadUpdateLink() {
		try {
			return new URL("https://modrinth.com/mod/" + mod.getSlug() + "/version/" + getUpdateModVersion());
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	private URL getAutoDownloadUpdateLink() {
		try {
			updateBasicLink.openConnection().setConnectTimeout(100);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateBasicLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			// 为字符输入流添加缓冲
			BufferedReader br = new BufferedReader(isr);
			String data = br.readLine();//读取数据
			
			String[] strings = data.split(",");
			for (String string : strings) {
				if (string.contains("\"url\"")) {
					data = string;
					break;
				}
			}
			
			data = StringUtils.substringAfter(data, ":");
			data = data.replaceAll("\"", "");
			return new URL(data);
		} catch (IOException e) {
			return null;
		}
	}
	
	private URL getAutoUpdateLink() {
		return getAutoDownloadUpdateLink();
	}
	
	private boolean manualDownload(URL downloadLink) {
		// 使用默认浏览器打开模组官网
		// ConfirmLinkScreen.opening(downloadLink.toString(), null, true);
		// 返回 false 表示不下载
		return false;
	}
	
	private boolean autoDownload(URL downloadLink) {
		UPDATE_THREAD_POOL.execute(thread = new ModDownloadThread(this, downloadLink));
		
		return true;
	}
	
	private boolean autoUpdate(URL downloadLink) {
		// 下载文件
		autoDownload(downloadLink);
		// 创建自动删除旧文件的批处理文件
		try (DirectoryStream<Path> children = Files.newDirectoryStream(mod.getPath())) {
			boolean hasFile = false;
			// 遍历 config 文件夹查询
			for (Path child : children) {
				if (child.equals(BAT_PATH)) {
					hasFile = true;
					break;
				}
			}
			// 如果不存在配置文件则读取模组配置
			if (!hasFile) Files.createFile(BAT_PATH);
			
			try (BufferedWriter bufferedWriter = Files.newBufferedWriter(BAT_PATH, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
			     PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
				printWriter.println("chcp 65001");
				printWriter.println("@echo off");
				printWriter.println("");
				printWriter.println("cls");
				printWriter.println("echo.");
				printWriter.println("echo 执行 " + mod.getName() + " 更新清理中……");
				printWriter.println("echo Performing Spontaneous-Replace update cleaning...");
				printWriter.println("echo.");
				printWriter.println("");
				printWriter.println("for /l %%i in (1, 1, 50000) do (");
				printWriter.println("\tif exist \"" + mod.getJarName() + "\" (");
				printWriter.println("\t\tdel /q \"" + mod.getJarName() + "\"");
				printWriter.println("\t) else (");
				printWriter.println("\t\tcls");
				printWriter.println("\t\techo.");
				printWriter.println("\t\techo 更新 " + mod.getName() + " 成功，请启动游戏开始游玩");
				printWriter.println("\t\techo Successfully updated Spontaneous-Replace, please start the game and start playing");
				printWriter.println("\t\techo.");
				printWriter.println("\t\tchoice /t 5 /d y /n >nul");
				printWriter.println("\t\tdel /q \"" + BAT_NAME + "\"");
				printWriter.println("\t\texit");
				printWriter.println("\t)");
				printWriter.println(")");
				printWriter.println("");
				printWriter.println("cls");
				printWriter.println("echo.");
				printWriter.println("echo 卸载旧版本 " + mod.getName() + " 失败，请手动删除旧版本 " + mod.getName());
				printWriter.println("echo Failed to uninstall the old Spontaneous-Replace version. Please manually delete the old Spontaneous-Replace version");
				printWriter.println("echo.");
				printWriter.println("pause");
				printWriter.println("start explorer \"" + mod.getPath() + "\"");
				printWriter.println("del /q \"" + BAT_NAME + "\"");
				printWriter.println("exit");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		return true;
	}
	
	void setUpdateModVersion() {
		if (updateBasicLink == null) {
			updateModVer = "";
			return;
		}
		try {
			updateBasicLink.openConnection().setConnectTimeout(100);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateBasicLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			// 为字符输入流添加缓冲
			BufferedReader br = new BufferedReader(isr);
			String data = br.readLine();//读取数据
			
			String[] strings = data.split(",");
			for (String string : strings) {
				if (string.contains("\"version_number\"")) {
					data = string;
					break;
				}
			}
			
			data = StringUtils.substringAfter(data, ":");
			data = data.replaceAll("\"", "");
			updateModVer = data;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	void setUpdateMinecraftVersion() {
		if (updateBasicLink == null) {
			updateMinecraftVer = "";
			return;
		}
		try {
			updateBasicLink.openConnection().setConnectTimeout(100);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateBasicLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
			updateMinecraftVer = getLatestMcVer(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public enum Mode {MANUAL_DOWNLOAD, AUTO_DOWNLOAD, AUTO_UPDATE}
	
	public enum Channel {RELEASE, BETA, ALPHA}
	
	public enum State {NEW_MC_VER, THIS_MC_VER, MOD_LOG, STOP_UPDATE, UPDATE_FAIL}
}
