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

package pers.saikel0rado1iu.silk.modpass.pack;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModPass;

/**
 * <h2 style="color:FFC800">数据包</h2>
 * 用于数据包的构建
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface DataPack extends BasePack {
	/**
	 * 创建一个数据包
	 *
	 * @param packRoot 包的根目录
	 * @param type     包激活类型
	 * @param modPass  所需的模组数据
	 * @return 数据包
	 */
	static DataPack create(String packRoot, ResourcePackActivationType type, ModPass modPass) {
		return new Simple(packRoot, type, modPass);
	}
	
	/**
	 * 获取数据包名称键
	 *
	 * @param modPass 模组通
	 * @return 数据包名称键
	 */
	static String getNameKey(ModPass modPass) {
		return String.format("dataPack.%s.name", modPass.modData().id());
	}
	
	/**
	 * 获取数据包描述键
	 *
	 * @param modPass 模组通
	 * @return 数据包描述键
	 */
	static String getDescKey(ModPass modPass) {
		return String.format("dataPack.%s.description", modPass.modData().id());
	}
	
	/**
	 * 数据包名称文本
	 *
	 * @return 名称文本
	 */
	default Text name() {
		return Text.translatable(getNameKey(modData()));
	}
	
	@Override
	default Identifier id() {
		return modData().ofId(packRoot());
	}
	
	@Override
	default boolean registry() {
		return ResourceManagerHelper.registerBuiltinResourcePack(id(), modData().mod(), name(), type());
	}
	
	/**
	 * 数据包的简单实现
	 *
	 * @param packRoot 包的根目录
	 * @param type     包激活类型
	 * @param modData  所需的模组数据
	 */
	record Simple(String packRoot, ResourcePackActivationType type, ModData modData) implements DataPack {
		private Simple(String packRoot, ResourcePackActivationType type, ModPass modPass) {
			this(packRoot, type, modPass.modData());
		}
	}
}
