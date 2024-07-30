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

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.GroupResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import pers.saikel0rado1iu.silk.event.modpass.RegisterGroupResourcePackCallback;
import pers.saikel0rado1iu.silk.modpass.ModData;
import pers.saikel0rado1iu.silk.modpass.ModPass;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
	@SuppressWarnings("UnstableApiUsage")
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
		
		private GroupResourcePack createPack(ResourceType resourceType, List<ModResourcePack> packs) {
			return new GroupResourcePack(resourceType, packs) {
				@Nullable
				@Override
				public InputSupplier<InputStream> openRoot(String... segments) {
					String fileName = String.join("/", segments);
					if ("pack.mcmeta".equals(fileName)) {
						String pack = String.format("{\"pack\":{\"pack_format\":" + SharedConstants.getGameVersion().getResourceVersion(type) + ",\"description\":{\"translate\":\"%s\"}}}", descKey);
						return () -> IOUtils.toInputStream(pack, Charsets.UTF_8);
					}
					String subPath = ("resourcepacks/" + id().getPath()).replace("/", FileSystems.getDefault().getSeparator());
					try (ModNioResourcePack modPack = ModNioResourcePack.create(id().toString(), modData().mod(), subPath, resourceType, type())) {
						return modPack.openRoot(segments);
					}
				}
				
				@Override
				public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
					List<? extends ResourcePack> packs = namespacedPacks.get(id.getNamespace());
					if (packs == null) return null;
					Map<String, Integer> orderMap = Maps.newHashMapWithExpectedSize(packs.size());
					for (int count = 0; count < orderList.size(); count++) orderMap.put(orderList.get(count), Integer.MAX_VALUE - count);
					packs.sort(Comparator.comparingInt(pack -> orderMap.getOrDefault(pack.getName(), 0)));
					// 更改默认的顺序为从上到下，同时保证自身模组中包的内容永远会被优先覆盖，或者按照指定的覆盖顺序覆盖
					InputSupplier<InputStream> inputSupplier = null;
					for (int count = packs.size() - 1; count >= 0; count--) {
						ResourcePack pack = packs.get(count);
						InputSupplier<InputStream> supplier = pack.open(type, id);
						if (supplier != null) inputSupplier = supplier;
					}
					return inputSupplier;
				}
				
				@Nullable
				@Override
				public <T> T parseMetadata(ResourceMetadataReader<T> metaReader) throws IOException {
					InputSupplier<InputStream> inputSupplier = openRoot("pack.mcmeta");
					if (inputSupplier == null) return null;
					try (InputStream input = inputSupplier.get()) {
						return AbstractFileResourcePack.parseMetadata(metaReader, input);
					}
				}
				
				@Override
				public String getName() {
					return modData().id();
				}
			};
		}
		
		@Override
		public boolean registry() {
			RegisterGroupResourcePackCallback.EVENT.register((type, consumer) -> {
				List<ModResourcePack> packs = new ArrayList<>();
				ModResourcePackUtil.appendModResourcePacks(packs, type, "resourcepacks/" + id().getPath());
				if (packs.isEmpty()) return;
				ResourcePackProfile resourcePackProfile = ResourcePackProfile.create(modData().id(), Text.translatable(nameKey), type() == ResourcePackActivationType.ALWAYS_ENABLED, new ResourcePackProfile.PackFactory() {
					@Override
					public ResourcePack open(String name) {
						return createPack(type, packs);
					}
					
					@Override
					public ResourcePack openWithOverlays(String name, ResourcePackProfile.Metadata metadata) {
						final ResourcePack basePack = open(name);
						final List<String> overlays = metadata.overlays();
						if (overlays.isEmpty()) return basePack;
						final List<ResourcePack> overlayPacks = new ArrayList<>(overlays.size());
						for (String overlay : overlays) {
							List<ModResourcePack> innerPacks = new ArrayList<>();
							ModResourcePackUtil.appendModResourcePacks(innerPacks, type, overlay);
							overlayPacks.add(createPack(type, innerPacks));
						}
						return new OverlayResourcePack(basePack, overlayPacks);
					}
				}, type, ResourcePackProfile.InsertionPosition.TOP, new GroupResourcePackSource(modData()));
				if (resourcePackProfile != null) consumer.accept(resourcePackProfile);
			});
			return true;
		}
	}
}
