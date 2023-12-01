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
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;

import java.util.Optional;

/**
 * <p><b style="color:FFC800"><font size="+1">远程武器击杀实体标准</font></b></p>
 * <p style="color:FFC800">远程武器是通过发射弹射物中储存的物品 NBT 来判断的，此 NBT 需要开发使用 {@link RangedKilledEntityCriterion#putRangedNbt(Entity, ItemStack)} 手动添加到弹射物中。{@link Entity#writeNbt(NbtCompound)} 已被注入，使可以记录参数中 NBT</p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@SilkApi
public class RangedKilledEntityCriterion extends AbstractCriterion<RangedKilledEntityCriterion.Conditions> {
	@SilkApi
	public static void putRangedNbt(Entity projectile, ItemStack ranged) {
		NbtCompound nbtCompound = new NbtCompound();
		NbtCompound nbt = new NbtCompound();
		nbt.putString("id", Registries.ITEM.getId(ranged.getItem()).toString());
		nbt.put("nbt", ranged.getNbt());
		nbtCompound.put("fromRanged", nbt);
		projectile.writeNbt(nbtCompound);
	}
	
	@Override
	protected Conditions conditionsFromJson(JsonObject jsonObject, Optional<LootContextPredicate> lootContextPredicate, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
		Optional<LootContextPredicate> target = EntityPredicate.contextPredicateFromJson(jsonObject, "target", advancementEntityPredicateDeserializer);
		Optional<ItemPredicate> ranged = ItemPredicate.fromJson(jsonObject.get("ranged"));
		Optional<EntityPredicate> projectile = EntityPredicate.fromJson(jsonObject.get("projectile"));
		NumberRange.IntRange killed = NumberRange.IntRange.fromJson(jsonObject.get("killed"));
		return new Conditions(lootContextPredicate, target, ranged, projectile, killed);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource) {
		trigger(player, entity, damageSource, 1);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource, int killed) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		trigger(player, conditions -> conditions.matches(player, lootContext, damageSource.getSource(), killed));
	}
	
	public static class Conditions extends AbstractCriterionConditions {
		private final Optional<ItemPredicate> ranged;
		private Optional<LootContextPredicate> target;
		private Optional<EntityPredicate> projectile;
		private NumberRange.IntRange killed;
		private int count = 0;
		
		public Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> target, Optional<ItemPredicate> ranged, Optional<EntityPredicate> projectile, NumberRange.IntRange killed) {
			super(player);
			this.target = target;
			this.ranged = ranged;
			this.projectile = projectile;
			this.killed = killed;
		}
		
		@SilkApi
		public static Conditions ranged(Optional<ItemPredicate> ranged) {
			return new Conditions(Optional.empty(), Optional.empty(), ranged, Optional.empty(), NumberRange.IntRange.ANY);
		}
		
		@SilkApi
		public static Conditions ranged(ItemConvertible ranged) {
			ItemPredicate itemPredicates = ItemPredicate.Builder.create().items(ranged.asItem()).build();
			return ranged(Optional.of(itemPredicates));
		}
		
		@SilkApi
		public Conditions target(EntityPredicate target) {
			this.target = Optional.of(EntityPredicate.asLootContextPredicate(target));
			return this;
		}
		
		@SilkApi
		public Conditions projectile(Optional<EntityPredicate> projectile) {
			this.projectile = projectile;
			return this;
		}
		
		@SilkApi
		public Conditions killed(NumberRange.IntRange killed) {
			this.killed = killed;
			return this;
		}
		
		@SilkApi
		public AdvancementCriterion<Conditions> create() {
			return SilkCriteria.RANGED_KILLED_ENTITY_CRITERION.create(this);
		}
		
		@Override
		public JsonObject toJson() {
			JsonObject jsonObject = super.toJson();
			target.ifPresent(lootContextPredicate -> jsonObject.add("target", lootContextPredicate.toJson()));
			ranged.ifPresent(itemPredicate -> jsonObject.add("ranged", itemPredicate.toJson()));
			projectile.ifPresent(entityPredicate -> jsonObject.add("projectile", entityPredicate.toJson()));
			jsonObject.add("killed", killed.toJson());
			return jsonObject;
		}
		
		public boolean matches(ServerPlayerEntity player, LootContext killedEntityContext, Entity projectile, int count) {
			if (target.isPresent() && !target.get().test(killedEntityContext)) return false;
			if (projectile == null) return false;
			NbtCompound nbtCompound = projectile.writeNbt(new NbtCompound()).getCompound("fromRanged");
			String[] id = nbtCompound.getString("id").split(":");
			ItemStack stack = new ItemStack(Registries.ITEM.get(new Identifier(id[0], id[1])));
			stack.setNbt(nbtCompound.getCompound("nbt"));
			boolean hasRanged = ranged.isPresent() && ranged.get().test(stack);
			if (!hasRanged) return false;
			if (this.projectile.isPresent() && !this.projectile.get().test(player, projectile)) return false;
			return killed.test(this.count += count);
		}
	}
}
