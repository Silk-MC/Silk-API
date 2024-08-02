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

package pers.saikel0rado1iu.silk.api.base.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <h2 style="color:FFC800">Jar 包实用工具</h2>
 * 有关 Jar 包的所有实用方法
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface JarUtil {
	/**
	 * 获取文件的 SHA-1
	 *
	 * @param path 文件 {@link Path}
	 * @return SHA-1 值，如果出现解析错误则返回空字符串
	 */
	static String getFileSha1(Path path) {
		if (!path.toFile().isFile()) return "";
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
			return "";
		}
	}
}
