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

package pers.saikel0rado1iu.silk.api.event.registry;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * <h2 style="color:FFC800">注册组资源包回调</h2>
 * 用于注册组资源包的事件
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
@FunctionalInterface
public interface RegisterGroupResourcePackCallback extends BiConsumer<ResourceType, Consumer<ResourcePackProfile>> {
	/**
	 * 事件
	 */
	Event<RegisterGroupResourcePackCallback> EVENT = EventFactory.createArrayBacked(RegisterGroupResourcePackCallback.class, listeners -> (type, consumer) -> {
		for (RegisterGroupResourcePackCallback event : listeners) event.accept(type, consumer);
	});
}
