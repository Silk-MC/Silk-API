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

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.minecraft.SharedConstants;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.resource.metadata.ResourceMetadataReader;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <h2 style="color:FFC800">组资源包</h2>
 * 表示一个组资源包，将多个资源包作为一个资源包保存。
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 版本
 */
public class GroupResourcePack implements ResourcePack {
	protected final ResourceType type;
	protected final BasePack.Group group;
	protected final List<String> orderList;
	protected final List<? extends ResourcePack> packs;
	protected final Map<String, List<ResourcePack>> namespacedPacks = new Object2ObjectOpenHashMap<>();
	
	public GroupResourcePack(ResourceType type, List<? extends ResourcePack> packs, List<String> orderList, Supplier<ModResourcePack> packSupplier, BasePack.Group group) {
		this.type = type;
		this.packs = packs;
		this.orderList = orderList;
		this.group = group;
		this.packs.forEach(pack -> pack.getNamespaces(this.type).forEach(namespace ->
				this.namespacedPacks.computeIfAbsent(namespace, value -> new ArrayList<>()).add(pack)));
	}
	
	private List<? extends ResourcePack> orderList(List<? extends ResourcePack> packList) {
		if (packList == null) return null;
		List<? extends ResourcePack> packs = Lists.newArrayList(packList);
		Map<String, Integer> orderMap = Maps.newHashMapWithExpectedSize(packs.size());
		for (int count = 0; count < orderList.size(); count++) orderMap.put(orderList.get(count), Integer.MAX_VALUE - count);
		packs.sort(Comparator.comparingInt(pack -> orderMap.getOrDefault(pack.getName(), 0)));
		Collections.reverse(packs);
		return packs;
	}
	
	@Nullable
	@Override
	public InputSupplier<InputStream> openRoot(String... segments) {
		String fileName = String.join("/", segments);
		if ("pack.mcmeta".equals(fileName)) {
			String pack = String.format("{\"pack\":{\"pack_format\":" + SharedConstants.getGameVersion().getResourceVersion(type) + ",\"description\":{\"translate\":\"%s\"}}}", group.descKey);
			return () -> IOUtils.toInputStream(pack, Charsets.UTF_8);
		}
		return packSupplier.get().openRoot(segments);
	}
	
	@Override
	public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
		List<? extends ResourcePack> packs = orderList(namespacedPacks.get(id.getNamespace()));
		if (packs == null) return null;
		InputSupplier<InputStream> inputSupplier = null;
		for (ResourcePack pack : packs) {
			InputSupplier<InputStream> supplier = pack.open(type, id);
			if (supplier != null) inputSupplier = supplier;
		}
		return inputSupplier;
	}
	
	@Override
	public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
		List<? extends ResourcePack> packs = orderList(namespacedPacks.get(namespace));
		if (packs == null) return;
		for (ResourcePack pack : packs) pack.findResources(type, namespace, prefix, consumer);
	}
	
	@Override
	public Set<String> getNamespaces(ResourceType type) {
		return namespacedPacks.keySet();
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
		return group.modData().id();
	}
	
	public void appendResources(ResourceType type, Identifier id, List<Resource> resources) {
		List<? extends ResourcePack> packs = orderList(namespacedPacks.get(id.getNamespace()));
		if (packs == null) return;
		Identifier metadataId = NamespaceResourceManager.getMetadataPath(id);
		for (ResourcePack pack : packs) {
			InputSupplier<InputStream> supplier = pack.open(type, id);
			if (supplier == null) continue;
			InputSupplier<ResourceMetadata> metadataSupplier = () -> {
				InputSupplier<InputStream> rawMetadataSupplier = pack.open(type, metadataId);
				return rawMetadataSupplier != null ? NamespaceResourceManager.loadMetadata(rawMetadataSupplier) : ResourceMetadata.NONE;
			};
			resources.add(new Resource(pack, supplier, metadataSupplier));
		}
	}
	
	public String getFullName() {
		return getName() + " (" + packs.stream().map(ResourcePack::getName).collect(Collectors.joining(", ")) + ")";
	}
	
	@Override
	public void close() {
		packs.forEach(ResourcePack::close);
	}
}
