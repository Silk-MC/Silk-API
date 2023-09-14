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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.gui.screen.ConfirmLinkScreen;
import org.jetbrains.annotations.ApiStatus;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用于更新模组文件的线程</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@ApiStatus.Internal
public final class ModUpdateThread extends Thread {
	private final CheckUpdateThread data;
	private float updateProgress;
	private boolean isUpdating;
	
	public ModUpdateThread(CheckUpdateThread data) {
		this.data = data;
		updateProgress = 0;
	}
	
	@Override
	public void run() {
		URL downloadLink = getUpdateLink();
		if (downloadLink == null) {
			data.setUpdatingFail(true);
			isUpdating = true;
			return;
		}
		switch (data.getData().getUpdateMode()) {
			case MANUAL_DOWNLOAD -> isUpdating = manualDownload(downloadLink);
			case AUTO_DOWNLOAD -> isUpdating = autoDownload(downloadLink);
			case AUTO_UPDATE -> isUpdating = autoUpdate(downloadLink);
		}
	}
	
	@ApiStatus.Internal
	public boolean isUpdating() {
		return isUpdating;
	}
	
	@ApiStatus.Internal
	public float getUpdateProgress() {
		return updateProgress;
	}
	
	private void download(URL downloadLink) {
		InputStream inputStream;
		OutputStream outputStream;
		try {
			// 打开连接
			HttpsURLConnection con = (HttpsURLConnection) downloadLink.openConnection();
			// 请求超时:5s
			con.setConnectTimeout(5 * 1000);
			
			con.setRequestMethod("GET");
			long size = con.getContentLength();
			
			inputStream = con.getInputStream();
			byte[] bytes = new byte[1024];
			// 读取到的数据长度
			int length;
			outputStream = Files.newOutputStream(Path.of(data.getMod().getPath() + FileSystems.getDefault().getSeparator() + downloadLink.getPath().substring(downloadLink.getPath().lastIndexOf('/'))));
			// 读取
			int fileSize = 0;
			while ((length = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, length);
				updateProgress = (fileSize += 1024) / size < 1 ? (float) fileSize / size * 100 : 99.99F;
			}
			updateProgress = 100;
			
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			data.setUpdatingFail(true);
		}
	}
	
	private URL getUpdateLink() {
		return switch (data.getData().getUpdateMode()) {
			case MANUAL_DOWNLOAD -> getManualDownloadUpdateLink();
			case AUTO_DOWNLOAD -> getAutoDownloadUpdateLink();
			case AUTO_UPDATE -> getAutoUpdateLink();
		};
	}
	
	private URL getManualDownloadUpdateLink() {
		try {
			return new URL("https://modrinth.com/mod/" + data.getMod().getSlug() + "/version/" + data.getUpdateModVer());
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	private URL getAutoDownloadUpdateLink() {
		try {
			URL updateLink = data.getUpdateLink();
			updateLink.openConnection().setConnectTimeout(100);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
			// 判断更新
			JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
			String url = jsonObject.get("url").getAsString();
			return new URL(url);
		} catch (IOException e) {
			return null;
		}
	}
	
	private URL getAutoUpdateLink() {
		return getAutoDownloadUpdateLink();
	}
	
	private boolean manualDownload(URL downloadLink) {
		// 使用默认浏览器打开模组官网
		//noinspection ResultOfMethodCallIgnored
		ConfirmLinkScreen.opening(downloadLink.toString(), null, true);
		// 返回 false 表示不下载
		return false;
	}
	
	private boolean autoDownload(URL downloadLink) {
		download(downloadLink);
		
		return true;
	}
	
	private boolean autoUpdate(URL downloadLink) {
		// 下载文件
		autoDownload(downloadLink);
		// 创建自动删除旧文件的批处理文件
		try (DirectoryStream<Path> children = Files.newDirectoryStream(data.getMod().getPath())) {
			boolean hasFile = false;
			// 遍历 config 文件夹查询
			for (Path child : children) {
				if (child.equals(data.getData().getBatPath())) {
					hasFile = true;
					break;
				}
			}
			// 如果不存在配置文件则读取模组配置
			if (!hasFile) Files.createFile(data.getData().getBatPath());
			
			try (BufferedWriter bufferedWriter = Files.newBufferedWriter(data.getData().getBatPath(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
			     PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
				printWriter.println("chcp 65001");
				printWriter.println("@echo off");
				printWriter.println("");
				printWriter.println("cls");
				printWriter.println("echo.");
				printWriter.println("echo 执行 " + data.getMod().getName() + " 更新清理中……");
				printWriter.println("echo Performing " + data.getMod().getName() + " update cleaning...");
				printWriter.println("echo.");
				printWriter.println("");
				printWriter.println("for /l %%i in (1, 1, 50000) do (");
				printWriter.println("\tif exist \"" + data.getMod().getJarName() + "\" (");
				printWriter.println("\t\tdel /q \"" + data.getMod().getJarName() + "\"");
				printWriter.println("\t) else (");
				printWriter.println("\t\tcls");
				printWriter.println("\t\techo.");
				printWriter.println("\t\techo 更新 " + data.getMod().getName() + " 成功，请启动游戏开始游玩");
				printWriter.println("\t\techo Successfully updated " + data.getMod().getName() + ", please start the game and start playing");
				printWriter.println("\t\techo.");
				printWriter.println("\t\tchoice /t 5 /d y /n >nul");
				printWriter.println("\t\tdel /q \"" + data.getData().getBatName() + "\"");
				printWriter.println("\t\texit");
				printWriter.println("\t)");
				printWriter.println(")");
				printWriter.println("");
				printWriter.println("cls");
				printWriter.println("echo.");
				printWriter.println("echo 卸载旧版本 " + data.getMod().getName() + " 失败，请手动删除旧版本 " + data.getMod().getName());
				printWriter.println("echo Failed to uninstall the old " + data.getMod().getName() + " version. Please manually delete the old " + data.getMod().getName() + " version");
				printWriter.println("echo.");
				printWriter.println("pause");
				printWriter.println("start explorer \"" + data.getData().getBatPath() + "\"");
				printWriter.println("del /q \"" + data.getData().getBatName() + "\"");
				printWriter.println("exit");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}
}
