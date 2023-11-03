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

package pers.saikel0rado1iu.silk.api.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange.IntRange;
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
 * <p><b style="color:FFC800"><font size="+1">射击弹药标准</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 */
@SilkApi
public class ShotProjectileCriterion extends AbstractCriterion<ShotProjectileCriterion.Conditions> {
	private static final Identifier ID = new Identifier(Silk.DATA.getId(), "shot_projectile");
	
	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, LootContextPredicate lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		ItemPredicate rangedWeapons = ItemPredicate.fromJson(jsonObject.get("ranged_weapon"));
		EntityPredicate projectile = EntityPredicate.fromJson(jsonObject.get("projectile"));
		IntRange count = IntRange.fromJson(jsonObject.get("count"));
		return new Conditions(lootContextPredicate, rangedWeapons, projectile, count);
	}
	
	@Override
	public Identifier getId() {
		return ID;
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, ItemStack rangedWeapon, Entity projectile) {
		trigger(player, conditions -> conditions.matches(player, rangedWeapon, projectile, 0));
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, ItemStack rangedWeapon, Entity projectile, int count) {
		trigger(player, conditions -> conditions.matches(player, rangedWeapon, projectile, count));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final ItemPredicate rangedWeapon;
		private EntityPredicate projectile;
		private IntRange count;
		private int counts = 0;
		
		public Conditions(LootContextPredicate player, ItemPredicate rangedWeapon, EntityPredicate projectile, IntRange count) {
			super(ID, player);
			this.rangedWeapon = rangedWeapon;
			this.projectile = projectile;
			this.count = count;
		}
		
		@SilkApi
		public static Conditions rangedWeapons(ItemPredicate rangedWeapons) {
			return new Conditions(LootContextPredicate.EMPTY, rangedWeapons, EntityPredicate.ANY, IntRange.ANY);
		}
		
		@SilkApi
		public static Conditions rangedWeapons(ItemConvertible rangedWeapons) {
			ItemPredicate itemPredicates = ItemPredicate.Builder.create().items(rangedWeapons.asItem()).build();
			return rangedWeapons(itemPredicates);
		}
		
		@SilkApi
		public Conditions projectile(EntityPredicate projectile) {
			this.projectile = projectile;
			return this;
		}
		
		@SilkApi
		public Conditions count(IntRange count) {
			this.count = count;
			return this;
		}
		
		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
			JsonObject jsonObject = super.toJson(predicateSerializer);
			jsonObject.add("ranged_weapon", rangedWeapon.toJson());
			jsonObject.add("projectile", projectile.toJson());
			jsonObject.add("count", count.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerPlayerEntity player, ItemStack rangedWeapon, Entity projectile, int count) {
			boolean hasRanged = this.rangedWeapon.test(rangedWeapon);
			if (!hasRanged) return false;
			if (!this.projectile.test(player, projectile)) return false;
			counts += count;
			return this.count.test(counts);
		}
	}
}