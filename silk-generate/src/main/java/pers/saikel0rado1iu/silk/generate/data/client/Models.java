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

package pers.saikel0rado1iu.silk.generate.data.client;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import pers.saikel0rado1iu.silk.impl.SilkApi;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">模型</h2>
 * 用于补充原版的模型以便在模组中使用
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface Models {
	/**
	 * 立体物品模型
	 */
	Model STEREOSCOPIC_ITEM = item(SilkApi.getInternal(), "stereoscopic_item", TextureKeys.STEREOSCOPIC_ITEM_TEXTURE_KEYS.toArray(new TextureKey[]{}));
	
	/**
	 * 创建物品模型
	 *
	 * @param modPass             模组通
	 * @param parent              父模型
	 * @param requiredTextureKeys 所需的纹理键
	 * @return 模型
	 */
	static Model item(ModPass modPass, String parent, TextureKey... requiredTextureKeys) {
		return new Model(Optional.of(modPass.modData().ofId("item/" + parent)), Optional.empty(), requiredTextureKeys);
	}
}
