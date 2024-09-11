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

package pers.saikel0rado1iu.silk.api.event.modplus;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import pers.saikel0rado1iu.silk.api.modpass.ModPass;
import pers.saikel0rado1iu.silk.api.modpass.pack.BasePack;
import pers.saikel0rado1iu.silk.api.modpass.pack.DataPack;
import pers.saikel0rado1iu.silk.api.modpass.pack.ResourcePack;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * <h2 style="color:FFC800">修改模组 {@link BasePack} 事件</h2>
 * 用于动态的修改模组之间的资源包覆盖关系或完全替换模组的资源部
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 版本
 */
public interface ModifyModResourcePackEvents {
	/**
	 * 此事件将 {@link DataPack.Group} 的排序替换为另外的排序方式
	 */
	Event<ModifyGroupDataPackOrder> MODIFY_GROUP_DATA_PACK_ORDER = EventFactory.createArrayBacked(ModifyGroupDataPackOrder.class, listeners -> (modPass, orderList) -> {
		Map.Entry<ModPass, List<String>> entry = Map.entry(modPass, orderList);
		for (ModifyGroupDataPackOrder event : listeners) entry = event.apply(entry.getKey(), entry.getValue());
		return entry;
	});
	/**
	 * 此事件将 {@link ResourcePack.Group} 的排序替换为另外的排序方式
	 */
	Event<ModifyGroupResourcePackOrder> MODIFY_GROUP_RESOURCE_PACK_ORDER = EventFactory.createArrayBacked(ModifyGroupResourcePackOrder.class, listeners -> (modPass, orderList) -> {
		Map.Entry<ModPass, List<String>> entry = Map.entry(modPass, orderList);
		for (ModifyGroupResourcePackOrder event : listeners) entry = event.apply(entry.getKey(), entry.getValue());
		return entry;
	});
	
	@FunctionalInterface
	interface ModifyGroupDataPackOrder extends BiFunction<ModPass, List<String>, Map.Entry<ModPass, List<String>>> {
	}
	
	@FunctionalInterface
	interface ModifyGroupResourcePackOrder extends BiFunction<ModPass, List<String>, Map.Entry<ModPass, List<String>>> {
	}
}
