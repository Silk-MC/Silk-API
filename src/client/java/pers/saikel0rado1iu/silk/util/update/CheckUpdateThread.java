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
import org.jetbrains.annotations.ApiStatus;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.api.ModExtendedData;
import pers.saikel0rado1iu.silk.util.Minecraft;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static pers.saikel0rado1iu.silk.util.update.CheckUpdateThread.State.*;

/**
 * <p><b style="color:FFC800"><font size="+1">用于检查模组更新的线程</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@ApiStatus.Internal
public final class CheckUpdateThread extends Thread {
	private final UpdateData data;
	private State updateState = NONE;
	private boolean updatingFail = false;
	private boolean canCheckUpdate = true;
	private URL updateLink;
	private String updateModVer;
	private String updateMcVer;
	
	CheckUpdateThread(UpdateData data) {
		this.data = data;
	}
	
	private static String getFileSha1(Path path) {
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
	
	@Override
	public void run() {
		if (canCheckUpdate) {
			State tempUpdateState = checkUpdate();
			setUpdateModVer();
			setUpdateMcVersion();
			updateState = tempUpdateState;
		}
	}
	
	@ApiStatus.Internal
	public boolean getUpdatingFail() {
		return updatingFail;
	}
	
	@ApiStatus.Internal
	public void setUpdatingFail(boolean value) {
		updatingFail = value;
	}
	
	@ApiStatus.Internal
	public URL getUpdateLink() {
		return updateLink;
	}
	
	@ApiStatus.Internal
	public String getUpdateMcVer() {
		return updateMcVer;
	}
	
	@ApiStatus.Internal
	public String getUpdateModVer() {
		return updateModVer;
	}
	
	@ApiStatus.Internal
	public void setCanCheckUpdate(boolean canCheckUpdate) {
		this.canCheckUpdate = canCheckUpdate;
	}
	
	@ApiStatus.Internal
	public CheckUpdateThread.State getUpdateState() {
		return updateState;
	}
	
	@ApiStatus.Internal
	public UpdateData getData() {
		return data;
	}
	
	@ApiStatus.Internal
	public ModExtendedData getMod() {
		return data.getMod();
	}
	
	@ApiStatus.Internal
	public State checkUpdate() {
		// 检查是否已停止更新
		if (!data.getUpdating()) return State.STOP_UPDATE;
		State state;
		return switch (data.getUpdateChannel()) {
			case ALPHA: {
				state = checkUpdate("alpha");
				if (state != UPDATE_FAIL) yield state;
			}
			case BETA: {
				state = checkUpdate("beta");
				if (state != UPDATE_FAIL) yield state;
			}
			case RELEASE: {
				state = checkUpdate("release");
				if (state != UPDATE_FAIL) updatingFail = false;
				yield state;
			}
		};
	}
	
	private void setUpdateModVer() {
		if (updateLink == null) {
			updateModVer = "";
			return;
		}
		try {
			updateLink.openConnection().setConnectTimeout(100);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			JsonObject webData = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
			updateModVer = webData.get("version_number").getAsString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void setUpdateMcVersion() {
		if (updateLink == null) {
			updateMcVer = "";
			return;
		}
		try {
			updateLink.openConnection().setConnectTimeout(100);
			// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
			InputStream is = updateLink.openStream();
			InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
			JsonObject webData = (JsonObject) JsonParser.parseReader(new BufferedReader(isr)).getAsJsonArray().get(0);
			updateMcVer = getLatestMcVer(webData);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private int compareMcVer(String originalVer, String compareVer) {
		originalVer = StringUtils.substringBefore(originalVer, "-");
		compareVer = StringUtils.substringBefore(compareVer, "-");
		if (originalVer.contains("w") || originalVer.contains("a") || compareVer.contains("w") || compareVer.contains("a")) return 0;
		
		ArrayList<String> originalVerList = new ArrayList<>(Arrays.asList(originalVer.split("\\.")));
		originalVer = originalVer.replaceAll("\\.", "");
		if (originalVerList.size() < 3) originalVer += "0";
		ArrayList<String> compareVerList = new ArrayList<>(Arrays.asList(compareVer.split("\\.")));
		compareVer = compareVer.replaceAll("\\.", "");
		if (compareVerList.size() < 3) compareVer += "0";
		
		return Integer.compare(Integer.parseInt(compareVer), Integer.parseInt(originalVer));
	}
	
	private String getLatestMcVer(JsonObject data) {
		List<String> verList = new ArrayList<>(4);
		JsonArray verArray = data.getAsJsonArray("game_versions");
		for (JsonElement object : verArray) verList.add(object.getAsString());
		String latestMinecraftVer = "";
		for (int count = 0; count < verList.size() - 1; count++) {
			latestMinecraftVer = compareMcVer(verList.get(count), verList.get(count + 1)) < 0 ? verList.get(count) : verList.get(count + 1);
		}
		return latestMinecraftVer;
	}
	
	private State checkUpdate(String value) {
		// 检查是否联网，如未联网则不进行更新检查
		try {
			URL testOnline = new URL("https://www.microsoft.com");
			HttpsURLConnection connection = (HttpsURLConnection) testOnline.openConnection();
			connection.setConnectTimeout(100);
			connection.connect();
		} catch (IOException unknownHostException) {
			return NONE;
		}
		
		// 判断是否有更新
		try {
			String checkUpdateBasic = data.getUpdateBasicLink() + "&version_type=" + value;
			URL checkUpdateLink;
			if (data.getCheckNewMcVerMod()) {
				updateLink = checkUpdateLink = new URL(checkUpdateBasic);
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				String s = new BufferedReader(new InputStreamReader(checkUpdateLink.openStream())).lines().collect(Collectors.joining(System.lineSeparator()));
				Silk.DATA.logger().error(s);
				if ("[]".equals(s)) return UPDATE_FAIL;
				JsonObject webData = (JsonObject) JsonParser.parseString(s).getAsJsonArray().get(0);
				// 判断更新
				String updateVer = getLatestMcVer(webData);
				JsonObject jsonObject = (JsonObject) webData.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (!getFileSha1(data.getMod().getJar()).equals(sha1Code)) {
					if (updateVer.equals(Minecraft.DATA.getVersion())) return THIS_MC_VER;
					else if (compareMcVer(Minecraft.DATA.getVersion(), updateVer) > 0) return NEW_MC_VER;
				}
			} else {
				updateLink = checkUpdateLink = new URL(checkUpdateBasic + "&game_versions=[%22" + Minecraft.DATA.getVersion() + "%22]");
				checkUpdateLink.openConnection().setConnectTimeout(100);
				// 通过 URL 的 openStream 方法获取 URL 对象所表示的自愿字节输入流
				String s = new BufferedReader(new InputStreamReader(checkUpdateLink.openStream())).lines().collect(Collectors.joining(System.lineSeparator()));
				if ("[]".equals(s)) return UPDATE_FAIL;
				JsonObject webData = (JsonObject) JsonParser.parseString(s).getAsJsonArray().get(0);
				// 判断更新
				JsonObject jsonObject = (JsonObject) webData.getAsJsonArray("files").get(0);
				String sha1Code = jsonObject.getAsJsonObject("hashes").get("sha1").getAsString();
				if (getFileSha1(data.getMod().getJar()).equals(sha1Code)) return THIS_MC_VER;
			}
			data.load();
			if (data.getShowChangelog() && data.getCanShowChangelog()) return MOD_LOG;
			return UPDATE_FAIL;
		} catch (IOException e) {
			return UPDATE_FAIL;
		}
	}
	
	public enum State {NONE, NEW_MC_VER, THIS_MC_VER, MOD_LOG, STOP_UPDATE, UPDATE_FAIL}
}
