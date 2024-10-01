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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.*;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h2 style="color:FFC800">组资源包</h2>
 * 用于替代原本 Fabric API 中的 GroupResourcePack
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.2.2
 */
public abstract class GroupResourcePack implements ResourcePack {
	protected final ResourceType type;
	protected final List<? extends ResourcePack> packs;
	protected final Map<String, List<ResourcePack>> namespacedPacks = new Object2ObjectOpenHashMap<>();
	
	public GroupResourcePack(ResourceType type, List<? extends ResourcePack> packs) {
		this.type = type;
		this.packs = packs;
		this.packs.forEach(pack -> pack.getNamespaces(this.type)
				.forEach(namespace -> this.namespacedPacks.computeIfAbsent(namespace, value -> new ArrayList<>())
						.add(pack)));
	}
	
	@Override
	public InputSupplier<InputStream> open(ResourceType type, Identifier id) {
		List<? extends ResourcePack> packs = this.namespacedPacks.get(id.getNamespace());
		
		if (packs != null) {
			// Last to first, since higher priority packs are at the end
			for (int i = packs.size() - 1; i >= 0; i--) {
				ResourcePack pack = packs.get(i);
				InputSupplier<InputStream> supplier = pack.open(type, id);
				
				if (supplier != null) {
					return supplier;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void findResources(ResourceType type, String namespace, String prefix, ResultConsumer consumer) {
		List<? extends ResourcePack> packs = this.namespacedPacks.get(namespace);
		
		if (packs == null) {
			return;
		}
		
		// First to last, since later calls override previously returned data
		for (ResourcePack pack : packs) {
			pack.findResources(type, namespace, prefix, consumer);
		}
	}
	
	@Override
	public Set<String> getNamespaces(ResourceType type) {
		return this.namespacedPacks.keySet();
	}
	
	public void appendResources(ResourceType type, Identifier id, List<Resource> resources) {
		List<? extends ResourcePack> packs = this.namespacedPacks.get(id.getNamespace());
		
		if (packs == null) {
			return;
		}
		
		Identifier metadataId = NamespaceResourceManager.getMetadataPath(id);
		
		// Last to first, since higher priority packs are at the end
		for (int i = packs.size() - 1; i >= 0; i--) {
			ResourcePack pack = packs.get(i);
			InputSupplier<InputStream> supplier = pack.open(type, id);
			
			if (supplier != null) {
				InputSupplier<ResourceMetadata> metadataSupplier = () -> {
					InputSupplier<InputStream> rawMetadataSupplier = pack.open(this.type, metadataId);
					return rawMetadataSupplier != null ? NamespaceResourceManager.loadMetadata(rawMetadataSupplier) : ResourceMetadata.NONE;
				};
				
				resources.add(new Resource(pack, supplier, metadataSupplier));
			}
		}
	}
	
	public String getFullName() {
		return this.getName() + " (" + this.packs.stream().map(ResourcePack::getName).collect(Collectors.joining(", ")) + ")";
	}
	
	@Override
	public void close() {
		this.packs.forEach(ResourcePack::close);
	}
}
