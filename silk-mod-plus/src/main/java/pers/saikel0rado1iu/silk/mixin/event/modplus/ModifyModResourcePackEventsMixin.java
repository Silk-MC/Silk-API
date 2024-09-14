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

package pers.saikel0rado1iu.silk.mixin.event.modplus;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pers.saikel0rado1iu.silk.api.event.modplus.ModifyModResourcePackEvents;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.modpass.pack.BasePack;
import pers.saikel0rado1iu.silk.api.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.api.modpass.pack.ResourcePack;

import java.util.List;

/**
 * <h2 style="color:FFC800">{@link ModifyModResourcePackEvents} 混入</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
interface ModifyModResourcePackEventsMixin {
	/**
	 * {@link ModifyModResourcePackEvents#MODIFY_GROUP_DATA_PACK_ORDER} 混入
	 */
	@Mixin(DataPack.class)
	interface ModifyGroupDataPackOrder extends BasePack {
		@ModifyArg(method = "createGroup(L java/lang/String;L java/util/List;L net/fabricmc/fabric/api/resource/ResourcePackActivationType;L pers/saikel0rado1iu/silk/api/modpass/ModPass;)L pers/saikel0rado1iu/silk/api/modpass/pack/DataPack$Group;",
				at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/modpass/pack/DataPack$Group;<init>(L java/lang/String;L java/util/List;L net/fabricmc/fabric/api/resource/ResourcePackActivationType;L pers/saikel0rado1iu/silk/api/modpass/ModPass;)V"),
				index = 1,
				remap = false)
		private static List<String> modify(List<String> orderList, @Local(argsOnly = true) ModPass modPass) {
			return ModifyModResourcePackEvents.MODIFY_GROUP_DATA_PACK_ORDER.invoker().apply(modPass, orderList).getValue();
		}
	}
	
	/**
	 * {@link ModifyModResourcePackEvents#MODIFY_GROUP_RESOURCE_PACK_ORDER} 混入
	 */
	@Mixin(ResourcePack.class)
	interface ModifyGroupResourcePackOrder extends BasePack {
		@ModifyArg(method = "createGroup(L java/lang/String;L java/util/List;L net/fabricmc/fabric/api/resource/ResourcePackActivationType;L pers/saikel0rado1iu/silk/api/modpass/ModPass;)L pers/saikel0rado1iu/silk/api/modpass/pack/ResourcePack$Group;",
				at = @At(value = "INVOKE", target = "L pers/saikel0rado1iu/silk/api/modpass/pack/ResourcePack$Group;<init>(L java/lang/String;L java/util/List;L net/fabricmc/fabric/api/resource/ResourcePackActivationType;L pers/saikel0rado1iu/silk/api/modpass/ModPass;)V"),
				index = 1,
				remap = false)
		private static List<String> modify(List<String> orderList, @Local(argsOnly = true) ModPass modPass) {
			return ModifyModResourcePackEvents.MODIFY_GROUP_RESOURCE_PACK_ORDER.invoker().apply(modPass, orderList).getValue();
		}
	}
}
