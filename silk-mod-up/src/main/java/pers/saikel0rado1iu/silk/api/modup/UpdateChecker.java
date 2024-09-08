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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import pers.saikel0rado1iu.silk.api.base.common.util.JarUtil;
import pers.saikel0rado1iu.silk.impl.Minecraft;
import pers.saikel0rado1iu.silk.impl.SilkModUp;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * <h2 style="color:FFC800">更新检查器</h2>
 * 用于检查模组更新的线程
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public final class UpdateChecker implements Callable<UpdateData> {
	private static final ScheduledExecutorService UPDATE_CHECKER_POOL = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().daemon(true).build());
	private final UpdateData.Builder updateDataBuilder;
	private final String projectLink;
	private final String basicLink;
	private URL updateLink;
	
	private UpdateChecker(UpdateData.Builder updateDataBuilder) {
		this.updateDataBuilder = updateDataBuilder;
		this.projectLink = String.format("https://api.modrinth.com/v2/project/%s", updateDataBuilder.modData().slug());
		this.basicLink = projectLink + "/version?loaders=[%%22fabric%%22]";
	}
	
	/**
	 * 运行方法，检查是否需要更新
	 *
	 * @param updateDataBuilder 更新数据构建器
	 * @return 更新数据
	 */
	public static Future<UpdateData> check(UpdateData.Builder updateDataBuilder) {
		FutureTask<UpdateData> futureTask = new FutureTask<>(new UpdateChecker(updateDataBuilder));
		UPDATE_CHECKER_POOL.schedule(() -> {
			if (!futureTask.isDone()) futureTask.run();
		}, 0, TimeUnit.SECONDS);
		return futureTask;
	}
	
	/**
	 * 检查方法
	 *
	 * @return 更新状态
	 */
	public UpdateState check() {
		// 检查是否已停止更新
		if (updateDataBuilder.isArchived) return UpdateState.STOP_UPDATE;
		return switch (updateDataBuilder.updateSettings.getValue(UpdateSettings.UPDATE_CHANNEL)) {
			case ALPHA: {
				UpdateState state = check(UpdateChannel.ALPHA);
				if (state != UpdateState.UPDATE_FAIL) yield state;
			}
			case BETA: {
				UpdateState state = check(UpdateChannel.BETA);
				if (state != UpdateState.UPDATE_FAIL) yield state;
			}
			case RELEASE: {
				yield check(UpdateChannel.RELEASE);
			}
		};
	}
	
	private UpdateState check(UpdateChannel channel) {
		String value = channel.toString().toLowerCase();
		// 检查是否联网，如未联网则不进行更新检查
		try {
			URL testOnline = new URL("https://www.minecraft.net");
			HttpsURLConnection connection = (HttpsURLConnection) testOnline.openConnection();
			connection.setConnectTimeout(1000);
			connection.connect();
		} catch (IOException unknownHostException) {
			String msg = "Unable to update: Unable to connect to the internet.";
			SilkModUp.getInstance().logger().warn(msg);
			return UpdateState.NONE;
		}
		// 检查是否存在项目主页，如不存在项目主页则报错
		try {
			URL testOnline = new URL(projectLink);
			HttpsURLConnection connection = (HttpsURLConnection) testOnline.openConnection();
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			connection.connect();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) throw new IOException();
		} catch (IOException unknownHostException) {
			String msg = "URL Error: The update link you attempted to connect to does not exist. Please check if the slug provided by ModPass is correct.";
			SilkModUp.getInstance().logger().error(msg);
			return UpdateState.NONE;
		}
		// 判断是否有更新
		try {
			String checkUpdateBasic = String.format("%s&version_type=%s", basicLink, value);
			if (updateDataBuilder.updateSettings.getValue(UpdateSettings.CHECK_NEW_MC_VER_MOD)) {
				updateLink = new URL(checkUpdateBasic);
				updateLink.openConnection().setConnectTimeout(200);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				String s = new BufferedReader(new InputStreamReader(updateLink.openStream())).lines().collect(Collectors.joining(System.lineSeparator()));
				if ("[]".equals(s)) return UpdateState.UPDATE_FAIL;
				JsonObject webData = (JsonObject) JsonParser.parseString(s).getAsJsonArray().get(0);
				// 判断更新
				String updateVer = Minecraft.getLatestVersion(webData);
				JsonObject jsonObject = (JsonObject) webData.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (!JarUtil.getFileSha1(updateDataBuilder.modData().jar()).equals(sha1Code)) {
					if (updateVer.equals(Minecraft.getInstance().version())) return UpdateState.THIS_MC_VER;
					else if (Minecraft.getInstance().compareVersion(updateVer) > 0) return UpdateState.NEW_MC_VER;
				}
			} else {
				updateLink = new URL(String.format("%s&game_versions=[%%22%s%%22]", checkUpdateBasic, Minecraft.getInstance().version()));
				updateLink.openConnection().setConnectTimeout(200);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				String s = new BufferedReader(new InputStreamReader(updateLink.openStream())).lines().collect(Collectors.joining(System.lineSeparator()));
				if ("[]".equals(s)) return UpdateState.UPDATE_FAIL;
				JsonObject webData = (JsonObject) JsonParser.parseString(s).getAsJsonArray().get(0);
				// 判断更新
				JsonObject jsonObject = (JsonObject) webData.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (!JarUtil.getFileSha1(updateDataBuilder.modData().jar()).equals(sha1Code)) return UpdateState.THIS_MC_VER;
			}
			if (UpdateData.canShowChangelog(updateDataBuilder.updateSettings)) return UpdateState.MOD_LOG;
			return UpdateState.DONE;
		} catch (IOException e) {
			String msg = "URL Error: The update link you attempted to connect to does not exist.";
			SilkModUp.getInstance().logger().error(msg, e);
			return UpdateState.UPDATE_FAIL;
		}
	}
	
	private String getUpdateModVersion() throws Exception {
		if (updateLink == null) return "";
		updateLink.openConnection().setConnectTimeout(200);
		// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
		InputStream is = updateLink.openStream();
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		JsonElement jsonElement = JsonParser.parseReader(new BufferedReader(isr));
		JsonObject webData;
		if (jsonElement instanceof JsonArray jsonArray) webData = (JsonObject) jsonArray.get(0);
		else webData = (JsonObject) jsonElement.getAsJsonArray().get(0);
		return webData.get("version_number").getAsString();
	}
	
	private String getUpdateMinecraftVersion() throws Exception {
		if (updateLink == null) return "";
		updateLink.openConnection().setConnectTimeout(200);
		// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
		InputStream is = updateLink.openStream();
		InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
		JsonObject webData = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
		return Minecraft.getLatestVersion(webData);
	}
	
	@Override
	public UpdateData call() {
		UpdateState state = check();
		if (state == UpdateState.UPDATE_FAIL) return updateDataBuilder.updateState(UpdateState.UPDATE_FAIL).build();
		try {
			String modVersion = getUpdateModVersion();
			String minecraftVersion = getUpdateMinecraftVersion();
			return updateDataBuilder.modVersion(modVersion).minecraftVersion(minecraftVersion).updateLink(updateLink).updateState(state).build();
		} catch (Exception e) {
			String msg = "URL Error: The update link you attempted to connect to does not exist. Please check if the slug provided by ModPass is correct.";
			SilkModUp.getInstance().logger().error(msg, e);
			return updateDataBuilder.updateState(UpdateState.UPDATE_FAIL).build();
		}
	}
}
