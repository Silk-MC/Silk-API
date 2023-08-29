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

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p><b style="color:FFC800"><font size="+1">用于下载模组文件的线程</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
public class ModDownloadThread extends Thread {
	private static float updateProgress;
	private final URL downloadLink;
	private final UpdateData data;
	
	ModDownloadThread(UpdateData data, URL downloadLink) {
		this.data = data;
		this.downloadLink = downloadLink;
		updateProgress = 0;
	}
	
	public float getUpdateProgress() {
		return updateProgress;
	}
	
	@Override
	public void run() {
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
}
