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

package pers.saikel0rado1iu.silk.api.generate.data.client;

import com.google.common.collect.ImmutableSet;
import net.minecraft.data.client.TextureKey;

/**
 * <h2 style="color:FFC800">纹理键</h2>
 * 用于补充原版的纹理键以便在模组中使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface TextureKeys {
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 4 层纹理
	 */
	TextureKey LAYER3 = TextureKey.of("layer3");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 5 层纹理
	 */
	TextureKey LAYER4 = TextureKey.of("layer4");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 6 层纹理
	 */
	TextureKey LAYER5 = TextureKey.of("layer5");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 7 层纹理
	 */
	TextureKey LAYER6 = TextureKey.of("layer6");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 8 层纹理
	 */
	TextureKey LAYER7 = TextureKey.of("layer7");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 9 层纹理
	 */
	TextureKey LAYER8 = TextureKey.of("layer8");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 10 层纹理
	 */
	TextureKey LAYER9 = TextureKey.of("layer9");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 11 层纹理
	 */
	TextureKey LAYER10 = TextureKey.of("layer10");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 12 层纹理
	 */
	TextureKey LAYER11 = TextureKey.of("layer11");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 13 层纹理
	 */
	TextureKey LAYER12 = TextureKey.of("layer12");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 14 层纹理
	 */
	TextureKey LAYER13 = TextureKey.of("layer13");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 15 层纹理
	 */
	TextureKey LAYER14 = TextureKey.of("layer14");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的第 16 层纹理
	 */
	TextureKey LAYER15 = TextureKey.of("layer15");
	/**
	 * {@link Models#STEREOSCOPIC_ITEM} 的所有纹理键
	 */
	ImmutableSet<TextureKey> STEREOSCOPIC_ITEM_TEXTURE_KEYS = ImmutableSet.of(
			TextureKey.LAYER0, TextureKey.LAYER1, TextureKey.LAYER2, TextureKeys.LAYER3, TextureKeys.LAYER4, TextureKeys.LAYER5, TextureKeys.LAYER6, TextureKeys.LAYER7,
			TextureKeys.LAYER8, TextureKeys.LAYER9, TextureKeys.LAYER10, TextureKeys.LAYER11, TextureKeys.LAYER12, TextureKeys.LAYER13, TextureKeys.LAYER14, TextureKeys.LAYER15);
}
