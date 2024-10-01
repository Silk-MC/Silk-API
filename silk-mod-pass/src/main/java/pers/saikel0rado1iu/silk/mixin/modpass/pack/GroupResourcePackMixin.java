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

package pers.saikel0rado1iu.silk.mixin.modpass.pack;


import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.modpass.pack.GroupResourcePack;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <h2 style="color:FFC800">{@link GroupResourcePack} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.2.2
 */
interface GroupResourcePackMixin {
	/**
	 * Patches getAllResources and method_41265 to work with GroupResourcePack.
	 */
	@Mixin(NamespaceResourceManager.class)
	abstract class FixNamespaceResourceManager {
		@Unique
		private final ThreadLocal<List<Resource>> fabric$getAllResources$resources = new ThreadLocal<>();
		
		@Inject(method = "getAllResources", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
		private void onGetAllResources(Identifier id, CallbackInfoReturnable<List<Resource>> cir, @Local List<Resource> resources) {
			this.fabric$getAllResources$resources.set(resources);
		}
		
		@Redirect(method = "getAllResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/resource/ResourcePack;open(Lnet/minecraft/resource/ResourceType;Lnet/minecraft/util/Identifier;)Lnet/minecraft/resource/InputSupplier;"))
		private InputSupplier<InputStream> onResourceAdd(ResourcePack pack, ResourceType type, Identifier id) {
			if (!(pack instanceof GroupResourcePack groupResourcePack)) return pack.open(type, id);
			groupResourcePack.appendResources(type, id, fabric$getAllResources$resources.get());
			return null;
		}
	}
	
	@Mixin(ReloadableResourceManagerImpl.class)
	abstract class FixReloadableResourceManagerImpl {
		// private static synthetic method_29491(Ljava/util/List;)Ljava/lang/Object;
		// Supplier lambda in beginMonitoredReload method.
		@Inject(method = "method_29491", at = @At("HEAD"), cancellable = true)
		private static void getResourcePackNames(List<ResourcePack> packs, CallbackInfoReturnable<String> cir) {
			cir.setReturnValue(packs.stream().map(pack -> {
				if (pack instanceof GroupResourcePack p) return p.getFullName();
				else return pack.getName();
			}).collect(Collectors.joining(", ")));
		}
	}
}
