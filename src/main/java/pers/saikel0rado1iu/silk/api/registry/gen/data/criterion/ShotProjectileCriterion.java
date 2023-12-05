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
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;
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
	@SilkApi
	public void trigger(ServerPlayerEntity player, ItemStack ranged, Entity projectile) {
		trigger(player, ranged, projectile, 1);
	}
	
	@SilkApi
	public void trigger(ServerPlayerEntity player, ItemStack ranged, Entity projectile, int count) {
		trigger(player, conditions -> conditions.matches(player, ranged, projectile, count));
	}
	
	@Override
	public Codec<Conditions> getConditionsCodec() {
		return Conditions.CODEC;
	}
	
	public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> ranged, Optional<EntityPredicate> projectile,
	                         NumberRange.IntRange count, int[] counts) implements AbstractCriterion.Conditions {
		public static final Codec<Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(Conditions::player),
						Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "ranged").forGetter(Conditions::ranged),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.CODEC, "projectile").forGetter(Conditions::projectile),
						Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "count", NumberRange.IntRange.ANY).forGetter(Conditions::count))
				.apply(instance, Conditions::new));
		
		public Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> ranged, Optional<EntityPredicate> projectile, NumberRange.IntRange count) {
			this(player, ranged, projectile, count, new int[]{0});
		}
		
		@SilkApi
		public static Builder ranged(Optional<ItemPredicate> ranged) {
			return new Builder(ranged);
		}
		
		@SilkApi
		public static Builder ranged(ItemConvertible ranged) {
			Optional<ItemPredicate> itemPredicates = Optional.of(ItemPredicate.Builder.create().items(ranged.asItem()).build());
			return ranged(itemPredicates);
		}
		
		@Override
		public Optional<LootContextPredicate> player() {
			return player;
		}
		
		@SilkApi
		public AdvancementCriterion<Conditions> create() {
			return SilkCriteria.SHOT_PROJECTILE_CRITERION.create(this);
		}
		
		public boolean matches(ServerPlayerEntity player, ItemStack ranged, Entity projectile, int count) {
			boolean hasRanged = this.ranged.isPresent() && this.ranged.get().test(ranged);
			if (!hasRanged) return false;
			if (this.projectile.isPresent() && !this.projectile.get().test(player, projectile)) return false;
			return this.count.test(counts[0] += count);
		}
		
		public static final class Builder {
			private final Optional<ItemPredicate> ranged;
			private Optional<EntityPredicate> projectile;
			private NumberRange.IntRange count;
			
			private Builder(Optional<ItemPredicate> ranged) {
				this.ranged = ranged;
				this.projectile = Optional.empty();
				this.count = NumberRange.IntRange.ANY;
			}
			
			@SilkApi
			public Builder projectile(Optional<EntityPredicate> projectile) {
				this.projectile = projectile;
				return this;
			}
			
			@SilkApi
			public Builder count(NumberRange.IntRange count) {
				this.count = count;
				return this;
			}
			
			@SilkApi
			public Conditions build() {
				return new Conditions(Optional.empty(), ranged, projectile, count);
			}
		}
	}
}
