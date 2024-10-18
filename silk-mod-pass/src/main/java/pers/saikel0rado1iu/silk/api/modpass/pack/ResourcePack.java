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

package pers.saikel0rado1iu.silk.api.modpass.pack;

import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.List;

/**
 * <h2 style="color:FFC800">资源包</h2>
 * 用于资源包的构建
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public interface ResourcePack extends BasePack {
	/**
	 * 创建一个简单资源包
	 *
	 * @param packRoot 包的根目录
	 * @param type     包激活类型
	 * @param modPass  所需的模组数据
	 * @return 简单资源包
	 */
	static Simple createSimple(String packRoot, ResourcePackActivationType type, ModPass modPass) {
		return new Simple(packRoot, type, modPass);
	}
	
	/**
	 * 创建一个组数据包
	 *
	 * @param packRoot  包的根目录
	 * @param orderList 排序列表
	 * @param type      包激活类型
	 * @param modPass   所需的模组数据
	 * @return 组数据包
	 */
	static Group createGroup(String packRoot, List<String> orderList, ResourcePackActivationType type, ModPass modPass) {
		return new Group(packRoot, orderList, type, modPass);
	}
	
	/**
	 * 创建一个组资源包
	 *
	 * @param packRoot 包的根目录
	 * @param type     包激活类型
	 * @param modPass  所需的模组数据
	 * @return 组资源包
	 */
	static Group createGroup(String packRoot, ResourcePackActivationType type, ModPass modPass) {
		return createGroup(packRoot, List.of(modPass.modData().id()), type, modPass);
	}
	
	/**
	 * 获取资源包名称键
	 *
	 * @param modPass 模组通
	 * @return 资源包名称键
	 */
	static String getNameKey(ModPass modPass) {
		return String.format("resourcePack.%s.name", modPass.modData().id());
	}
	
	/**
	 * 获取资源包描述键
	 *
	 * @param modPass 模组通
	 * @return 资源包描述键
	 */
	static String getDescKey(ModPass modPass) {
		return String.format("resourcePack.%s.description", modPass.modData().id());
	}
	
	@Override
	default Text name() {
		return Text.translatable(getNameKey(modData()));
	}
	
	@Override
	default Identifier id() {
		return modData().ofId(packRoot());
	}
	
	/**
	 * 简单资源包
	 */
	final class Simple extends BasePack.Simple implements ResourcePack {
		Simple(String packRoot, ResourcePackActivationType type, ModPass modPass) {
			super(packRoot, type, modPass);
		}
	}
	
	/**
	 * 组资源包
	 */
	final class Group extends BasePack.Group implements ResourcePack {
		Group(String packRoot, List<String> orderList, ResourcePackActivationType type, ModPass modPass) {
			super(packRoot, getNameKey(modPass), getDescKey(modPass), orderList, type, modPass, ResourceType.CLIENT_RESOURCES);
		}
	}
}
