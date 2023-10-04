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

package pers.saikel0rado1iu.silk.datagen;

import net.minecraft.advancement.Advancement;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.function.Consumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于构建属于自己模组的进度，而不是依附在 minecraft 命名空间下</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public interface SilkAdvancement {
	@SilkApi
	static Advancement create(Consumer<Advancement> consumer, Advancement.Builder builder, Identifier id) {
		Advancement advancement = builder.build(id);
		consumer.accept(advancement);
		return advancement;
	}
	
	@SilkApi
	static Text getAdvancementTitle(ModBasicData mod, String path) {
		return Text.translatable("advancements." + mod.getId() + '.' + path + ".title");
	}
	
	@SilkApi
	static Text getAdvancementDescription(ModBasicData mod, String path) {
		return Text.translatable("advancements." + mod.getId() + '.' + path + ".description");
	}
}
