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

import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTable;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.ModBasicData;

import java.util.function.BiConsumer;

/**
 * <p><b style="color:FFC800"><font size="+1">用于提供模组中常用但未提供更方便方法的战利品表方法</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
@SilkApi
public interface SilkLootTableGenerator {
	@SilkApi
	EntityPredicate.Builder NEEDS_ENTITY_ON_FIRE = EntityPredicate.Builder.create().flags(EntityFlagsPredicate.Builder.create().onFire(true).build());
	
	@SilkApi
	static void addEntityDrop(BiConsumer<Identifier, LootTable.Builder> exporter, EntityType<?> entity, LootTable.Builder builder) {
		exporter.accept(new Identifier(Registries.ENTITY_TYPE.getId(entity).getNamespace(), "entities/" + Registries.ENTITY_TYPE.getId(entity).getPath()), builder);
	}
	
	@SilkApi
	static void addArchaeologyDrop(BiConsumer<Identifier, LootTable.Builder> exporter, ModBasicData mod, String id, LootTable.Builder builder) {
		exporter.accept(new Identifier(mod.getId(), "archaeology/" + id), builder);
	}
	
	@SilkApi
	static void addChestDrop(BiConsumer<Identifier, LootTable.Builder> exporter, ModBasicData mod, String id, LootTable.Builder builder) {
		exporter.accept(new Identifier(mod.getId(), "chests/" + id), builder);
	}
	
	@SilkApi
	static void addGameplayDrop(BiConsumer<Identifier, LootTable.Builder> exporter, ModBasicData mod, String id, LootTable.Builder builder) {
		exporter.accept(new Identifier(mod.getId(), "gameplay/" + id), builder);
	}
}
