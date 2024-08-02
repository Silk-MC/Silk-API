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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.Util;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.impl.SilkModUp;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * <h2 style="color:FFC800">模组更新器</h2>
 * 用于更新模组文件的线程
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public final class ModUpdater extends Thread {
	private static final ScheduledExecutorService MOD_UPDATER_POOL = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().daemon(true).build());
	private final UpdateData updateData;
	private volatile float updateProgress = 0;
	
	private ModUpdater(UpdateData updateData) {
		this.updateData = updateData;
	}
	
	/**
	 * 运行方法，进行模组更新
	 *
	 * @param updateData 更新数据
	 * @return 模组更新器
	 */
	public static ModUpdater update(UpdateData updateData) {
		ModUpdater modUpdater = new ModUpdater(updateData);
		MOD_UPDATER_POOL.schedule(modUpdater, 0, TimeUnit.SECONDS);
		return modUpdater;
	}
	
	/**
	 * 运行方法，获取下载链接
	 *
	 * @param updateData 更新数据
	 * @return 下载 URL
	 */
	public static Future<URL> downloadUrl(UpdateData updateData) {
		FutureTask<URL> futureTask = new FutureTask<>(() -> {
			ModUpdater modUpdater = new ModUpdater(updateData);
			UpdateMode mode = updateData.getValue(UpdateSettings.UPDATE_MODE);
			Optional<URL> downloadLink = switch (mode) {
				case MANUAL_DOWNLOAD -> modUpdater.getManualDownloadUpdateLink();
				case AUTO_DOWNLOAD -> modUpdater.getAutoDownloadUpdateLink();
				case AUTO_UPDATE -> modUpdater.getAutoUpdateLink();
			};
			return downloadLink.orElse(new URL(String.format("https://modrinth.com/mod/%s/version", updateData.modData().slug())));
		});
		MOD_UPDATER_POOL.schedule(() -> {
			if (!futureTask.isDone()) futureTask.run();
		}, 0, TimeUnit.SECONDS);
		return futureTask;
	}
	
	/**
	 * 获取批处理文件名称
	 *
	 * @param updateData 更新数据
	 * @return 批处理文件名称
	 */
	public static String getBatName(UpdateData updateData) {
		return updateData.modPass().id() + ".bat";
	}
	
	/**
	 * 获取更新进度
	 *
	 * @return 更新进度
	 */
	public synchronized String getProgress() {
		return String.format("%.2f", updateProgress);
	}
	
	private Path getBatPath() {
		return Paths.get(updateData.modPass().jarPath().toString(), getBatName(updateData));
	}
	
	private Optional<URL> getManualDownloadUpdateLink() {
		try {
			return Optional.of(new URL(String.format("https://modrinth.com/mod/%s/version/%s", updateData.modData().slug(), updateData.modVersion())));
		} catch (MalformedURLException e) {
			String msg = "URL Error: The update link you attempted to connect to does not exist. Please check if the slug provided by ModPass is correct.";
			SilkModUp.getInstance().logger().error(msg, e);
			return Optional.empty();
		}
	}
	
	private Optional<URL> getAutoDownloadUpdateLink() {
		try {
			URL updateLink = updateData.updateLink();
			updateLink.openConnection().setConnectTimeout(200);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			JsonObject data = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
			// 判断更新
			JsonObject jsonObject = (JsonObject) data.getAsJsonArray("files").get(0);
			String url = jsonObject.get("url").getAsString();
			return Optional.of(new URL(url));
		} catch (IOException e) {
			String msg = "URL Error: The update link you attempted to connect to does not exist. Please check if the slug provided by ModPass is correct.";
			SilkModUp.getInstance().logger().error(msg, e);
			return Optional.empty();
		}
	}
	
	private Optional<URL> getAutoUpdateLink() {
		return getAutoDownloadUpdateLink();
	}
	
	private void manualDownload(URL downloadLink) {
		// 使用默认浏览器打开模组官网
		Util.getOperatingSystem().open(downloadLink);
	}
	
	private void autoDownload(URL downloadLink) {
		download(downloadLink);
	}
	
	private void autoUpdate(URL downloadLink) {
		// 下载文件
		autoDownload(downloadLink);
		// 创建自动删除旧文件的批处理文件
		try (DirectoryStream<Path> children = Files.newDirectoryStream(updateData.modData().jarPath())) {
			boolean hasFile = false;
			// 遍历 config 文件夹查询
			for (Path child : children) {
				if (!child.equals(getBatPath())) continue;
				hasFile = true;
				break;
			}
			// 如果不存在配置文件则读取模组配置
			if (!hasFile) Files.createFile(getBatPath());
			
			try (BufferedWriter bufferedWriter = Files.newBufferedWriter(getBatPath(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
			     PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
				printWriter.println("chcp 65001");
				printWriter.println("@echo off");
				printWriter.println("");
				printWriter.println("cls");
				printWriter.println("echo.");
				printWriter.println("echo 执行 " + updateData.modData().name() + " 更新清理中……");
				printWriter.println("echo Performing " + updateData.modData().name() + " update cleaning...");
				printWriter.println("echo.");
				printWriter.println("");
				printWriter.println("for /l %%i in (1, 1, 50000) do (");
				printWriter.println("\tif exist \"" + updateData.modData().jarName() + "\" (");
				printWriter.println("\t\tdel /q \"" + updateData.modData().jarName() + "\"");
				printWriter.println("\t) else (");
				printWriter.println("\t\tcls");
				printWriter.println("\t\techo.");
				printWriter.println("\t\techo 更新 " + updateData.modData().name() + " 成功，请启动游戏开始游玩。（正在执行后续操作，请勿关闭此窗口）");
				printWriter.println("\t\techo Successfully updated " + updateData.modData().name() + ", please start the game and start playing.");
				printWriter.println("\t\techo ^(Executing follow-up operations, please do not close this window^)");
				printWriter.println("\t\techo.");
				printWriter.println("\t\tchoice /t 5 /d y /n >nul");
				printWriter.println("\t\tdel /q \"" + getBatName(updateData) + "\"");
				printWriter.println("\t\texit");
				printWriter.println("\t)");
				printWriter.println(")");
				printWriter.println("");
				printWriter.println("cls");
				printWriter.println("echo.");
				printWriter.println("echo 卸载旧版本 " + updateData.modData().name() + " 失败，请手动删除旧版本 " + updateData.modData().name());
				printWriter.println("echo Failed to uninstall the old " + updateData.modData().name() + " version. Please manually delete the old " + updateData.modData().name() + " version");
				printWriter.println("echo.");
				printWriter.println("pause");
				printWriter.println("start explorer \"" + getBatPath() + "\"");
				printWriter.println("del /q \"" + getBatName(updateData) + "\"");
				printWriter.println("exit");
			} catch (IOException e) {
				String msg = "Unexpected Error: Unable to create the batch file! This might be due to storage permissions. Please check your storage permissions.";
				SilkModUp.getInstance().logger().error(msg, e);
			}
		} catch (IOException e) {
			String msg = "Unexpected Error: Unable to open the mod path! This might be due to storage permissions. Please check your storage permissions.";
			SilkModUp.getInstance().logger().error(msg, e);
		}
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
			String fileName = updateData.modData().jarPath() + FileSystems.getDefault().getSeparator() + downloadLink.getPath().substring(downloadLink.getPath().lastIndexOf('/'));
			outputStream = Files.newOutputStream(Path.of(URLDecoder.decode(fileName, StandardCharsets.UTF_8)));
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
			String msg = "Download Error: Unable to connect to the file server or the connection was interrupted. Please check your network or storage permissions.";
			SilkModUp.getInstance().logger().error(msg, e);
		}
	}
	
	@Override
	public void run() {
		super.run();
		UpdateMode mode = updateData.getValue(UpdateSettings.UPDATE_MODE);
		Optional<URL> downloadLink = switch (mode) {
			case MANUAL_DOWNLOAD -> getManualDownloadUpdateLink();
			case AUTO_DOWNLOAD -> getAutoDownloadUpdateLink();
			case AUTO_UPDATE -> getAutoUpdateLink();
		};
		if (downloadLink.isEmpty()) return;
		switch (mode) {
			case MANUAL_DOWNLOAD -> manualDownload(downloadLink.get());
			case AUTO_DOWNLOAD -> autoDownload(downloadLink.get());
			case AUTO_UPDATE -> autoUpdate(downloadLink.get());
		}
	}
}
