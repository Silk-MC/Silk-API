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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
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
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource) {
		trigger(player, entity, damageSource, 1);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource, int killed) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		trigger(player, conditions -> conditions.matches(player, lootContext, damageSource.getSource(), killed));
	}
	
	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> target, Optional<ItemPredicate> ranged,
	                         Optional<EntityPredicate> projectile, NumberRange.IntRange killed, int[] count) implements AbstractCriterion.Conditions {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(Conditions::player),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "target").forGetter(Conditions::target),
						Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "ranged").forGetter(Conditions::ranged),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.CODEC, "projectile").forGetter(Conditions::projectile),
						Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "killed", NumberRange.IntRange.ANY).forGetter(Conditions::killed))
				.apply(instance, Conditions::new));
		
		public Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> target, Optional<ItemPredicate> ranged, Optional<EntityPredicate> projectile, NumberRange.IntRange killed) {
			this(player, target, ranged, projectile, killed, new int[]{0});
		}
		
		@SilkApi
		public static Builder ranged(Optional<ItemPredicate> ranged) {
			return new Builder(ranged);
		}
		
		@SilkApi
		public static Builder ranged(ItemConvertible ranged) {
			ItemPredicate itemPredicates = ItemPredicate.Builder.create().items(ranged.asItem()).build();
			return ranged(Optional.of(itemPredicates));
		}
		
		@Override
		public Optional<LootContextPredicate> player() {
			return player;
		}
		
		@SilkApi
		public AdvancementCriterion<Conditions> create() {
			return SilkCriteria.RANGED_KILLED_ENTITY_CRITERION.create(this);
		}
		
		public boolean matches(ServerPlayerEntity player, LootContext killedEntityContext, Entity projectile, int count) {
			if (target.isPresent() && !target.get().test(killedEntityContext)) return false;
			if (projectile == null) return false;
			NbtCompound nbtCompound = projectile.writeNbt(new NbtCompound()).getCompound("fromRanged");
			String[] id = nbtCompound.getString("id").split(":");
			ItemStack stack = new ItemStack("".equals(id[0]) ? Items.AIR : Registries.ITEM.get(new Identifier(id[0], id[1])));
			stack.setNbt(nbtCompound.getCompound("nbt"));
			boolean hasRanged = ranged.isPresent() && ranged.get().test(stack);
			if (!hasRanged) return false;
			if (this.projectile.isPresent() && !this.projectile.get().test(player, projectile)) return false;
			return killed.test(this.count[0] += count);
		}
		
		public static final class Builder {
			private final Optional<ItemPredicate> ranged;
			private Optional<LootContextPredicate> target;
			private Optional<EntityPredicate> projectile;
			private NumberRange.IntRange killed;
			
			private Builder(Optional<ItemPredicate> ranged) {
				this.ranged = ranged;
				this.target = Optional.empty();
				this.projectile = Optional.empty();
				this.killed = NumberRange.IntRange.ANY;
			}
			
			@SilkApi
			public Builder target(EntityPredicate target) {
				this.target = Optional.of(EntityPredicate.asLootContextPredicate(target));
				return this;
			}
			
			@SilkApi
			public Builder projectile(Optional<EntityPredicate> projectile) {
				this.projectile = projectile;
				return this;
			}
			
			@SilkApi
			public Builder killed(NumberRange.IntRange killed) {
				this.killed = killed;
				return this;
			}
			
			@SilkApi
			public Conditions build() {
				return new Conditions(Optional.empty(), target, ranged, projectile, killed);
			}
		}
	}
}
