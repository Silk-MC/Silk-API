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

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackFactory;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.api.event.registry.RegisterModResourcePackCallback;
import pers.saikel0rado1iu.silk.api.modpass.ModData;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <h2 style="color:FFC800">基础包</h2>
 * 包的基础数据
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface BasePack extends ModPass {
	/**
	 * 包的根目录，数据包目录为 {@code "resourcepacks/packRoot()/..."}
	 *
	 * @return 根目录名称
	 */
	String packRoot();
	
	/**
	 * 包激活类型，参考 {@link ResourcePackActivationType}
	 *
	 * @return 数据包激活类型
	 */
	ResourcePackActivationType type();
	
	/**
	 * 包的唯一标识符
	 *
	 * @return 标识符
	 */
	Identifier id();
	
	/**
	 * 包名称文本
	 *
	 * @return 名称文本
	 */
	Text name();
	
	/**
	 * 注册包
	 *
	 * @return 是否成功注册
	 */
	boolean registry();
	
	/**
	 * 资源包的简单实现
	 */
	abstract class Simple implements BasePack {
		protected final String packRoot;
		protected final ResourcePackActivationType type;
		protected final ModPass modPass;
		
		/**
		 * @param packRoot 包的根目录
		 * @param type     包激活类型
		 * @param modPass  所需的模组通
		 */
		protected Simple(String packRoot, ResourcePackActivationType type, ModPass modPass) {
			this.packRoot = packRoot;
			this.type = type;
			this.modPass = modPass;
		}
		
		@Override
		public String packRoot() {
			return packRoot;
		}
		
		@Override
		public ResourcePackActivationType type() {
			return type;
		}
		
		@Override
		public ModData modData() {
			return modPass.modData();
		}
		
		@Override
		public boolean registry() {
			return ResourceManagerHelper.registerBuiltinResourcePack(id(), modData().mod(), name(), type());
		}
	}
	
	/**
	 * 组资源包<br>
	 * 组资源包会把所有模组容器内统一路径下的包内容识别为一个资源包
	 */
	abstract class Group extends Simple implements BasePack {
		protected final String nameKey;
		protected final String descKey;
		protected final List<String> orderList;
		
		/**
		 * @param packRoot  包的根目录
		 * @param nameKey   名称翻译键
		 * @param descKey   描述翻译键
		 * @param orderList 排序列表
		 * @param type      包激活类型
		 * @param modPass   所需的模组数据
		 */
		protected Group(String packRoot, String nameKey, String descKey, List<String> orderList, ResourcePackActivationType type, ModPass modPass) {
			super(packRoot, type, modPass);
			this.nameKey = nameKey;
			this.descKey = descKey;
			this.orderList = orderList;
		}
		
		@Override
		public boolean registry() {
			AtomicBoolean flag = new AtomicBoolean(false);
			RegisterModResourcePackCallback.EVENT.register((type, consumer) -> {
				ResourcePackProfile profile = ResourcePackProfile.create(
						modData().id(),
						Text.translatable(nameKey),
						type() == ResourcePackActivationType.ALWAYS_ENABLED,
						new ModResourcePackFactory(pack),
						type,
						ResourcePackProfile.InsertionPosition.TOP,
						new GroupResourcePackSource(modData())
				);
				if (profile == null) return;
				consumer.accept(profile);
				flag.set(true);
			});
			return flag.get();
		}
	}
}
