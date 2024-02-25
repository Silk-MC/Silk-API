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

package pers.saikel0rado1iu.silk.api.registry.gen.data.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.Silk;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

/**
 * <h2 style="color:FFC800">射击弹药标准</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 */
@SilkApi
public class ShotProjectileCriterion extends AbstractCriterion<ShotProjectileCriterion.Conditions> {
	private static final Identifier ID = new Identifier(Silk.DATA.getId(), "shot_projectile");
	
	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate ranged = ItemPredicate.fromJson(jsonObject.get("ranged"));
		EntityPredicate projectile = EntityPredicate.fromJson(jsonObject.get("projectile"));
		NumberRange.IntRange count = NumberRange.IntRange.fromJson(jsonObject.get("count"));
		return new Conditions(lootContextPredicate, ranged, projectile, count);
	}
	
	@Override
	public Identifier getId() {
		return ID;
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
		private final ItemPredicate ranged;
		private EntityPredicate projectile;
		private NumberRange.IntRange count;
		private int counts = 0;
		
		public Conditions(LootContextPredicate player, ItemPredicate ranged, EntityPredicate projectile, NumberRange.IntRange count) {
			super(ID, player);
			this.ranged = ranged;
			this.projectile = projectile;
			this.count = count;
		}
		
		@SilkApi
		public static Conditions ranged(ItemPredicate ranged) {
			return new Conditions(LootContextPredicate.EMPTY, ranged, EntityPredicate.ANY, NumberRange.IntRange.ANY);
		}
		
		@SilkApi
		public static Conditions ranged(ItemConvertible ranged) {
			ItemPredicate itemPredicates = ItemPredicate.Builder.create().items(ranged.asItem()).build();
			return ranged(itemPredicates);
		}
		
		@SilkApi
		public Conditions projectile(EntityPredicate projectile) {
			this.projectile = projectile;
			return this;
		}
		
		@SilkApi
		public Conditions count(NumberRange.IntRange count) {
			this.count = count;
			return this;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("ranged", ranged.toJson());
			jsonObject.add("projectile", projectile.toJson());
			jsonObject.add("count", count.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerPlayerEntity player, ItemStack ranged, Entity projectile, int count) {
			boolean hasRanged = this.ranged.test(ranged);
			if (!hasRanged) return false;
			if (!this.projectile.test(player, projectile)) return false;
			return this.count.test(counts += count);
		}
	}
}
