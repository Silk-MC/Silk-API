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

package pers.saikel0rado1iu.silk.api.registry.datagen.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">射击弹药标准</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
@SilkApi
public class ShotProjectileCriterion extends AbstractCriterion<ShotProjectileCriterion.Conditions> {
	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, Optional<LootContextPredicate> lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Optional<ItemPredicate> ranged = ItemPredicate.fromJson(jsonObject.get("ranged"));
		Optional<EntityPredicate> projectile = EntityPredicate.fromJson(jsonObject.get("projectile"));
		NumberRange.IntRange count = NumberRange.IntRange.fromJson(jsonObject.get("count"));
		return new Conditions(lootContextPredicate, ranged, projectile, count);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, ItemStack ranged, Entity projectile) {
		trigger(player, ranged, projectile, 1);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, ItemStack ranged, Entity projectile, int count) {
		trigger(player, conditions -> conditions.matches(player, ranged, projectile, count));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Optional<ItemPredicate> ranged;
		private Optional<EntityPredicate> projectile;
		private NumberRange.IntRange count;
		private int counts = 0;
		
		public Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> ranged, Optional<EntityPredicate> projectile, NumberRange.IntRange count) {
			super(player);
			this.ranged = ranged;
			this.projectile = projectile;
			this.count = count;
		}
		
		@SilkApi
		public static Conditions ranged(Optional<ItemPredicate> ranged) {
			return new Conditions(Optional.empty(), ranged, Optional.empty(), NumberRange.IntRange.ANY);
		}
		
		@SilkApi
		public static Conditions ranged(ItemConvertible ranged) {
			Optional<ItemPredicate> itemPredicates = Optional.of(ItemPredicate.Builder.create().items(ranged.asItem()).build());
			return ranged(itemPredicates);
		}
		
		@SilkApi
		public AdvancementCriterion<Conditions> create() {
			return SilkCriteria.SHOT_PROJECTILE_CRITERION.create(this);
		}
		
		@SilkApi
		public Conditions projectile(Optional<EntityPredicate> projectile) {
			this.projectile = projectile;
			return this;
		}
		
		@SilkApi
		public Conditions count(NumberRange.IntRange count) {
			this.count = count;
			return this;
		}
		
		@Override
		public JsonObject toJson() {
			JsonObject jsonObject = super.toJson();
			ranged.ifPresent(itemPredicate -> jsonObject.add("ranged", itemPredicate.toJson()));
			projectile.ifPresent(entityPredicate -> jsonObject.add("projectile", entityPredicate.toJson()));
			jsonObject.add("count", count.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerPlayerEntity player, ItemStack ranged, Entity projectile, int count) {
			boolean hasRanged = this.ranged.isPresent() && this.ranged.get().test(ranged);
			if (!hasRanged) return false;
			if (this.projectile.isPresent() && !this.projectile.get().test(player, projectile)) return false;
			return this.count.test(counts += count);
		}
	}
}
