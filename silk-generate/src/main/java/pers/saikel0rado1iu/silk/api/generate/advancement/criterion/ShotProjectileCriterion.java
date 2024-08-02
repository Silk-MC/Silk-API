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

package pers.saikel0rado1iu.silk.api.generate.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import pers.saikel0rado1iu.silk.impl.SilkApi;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * <h2 style="color:FFC800">射击发射物标准</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class ShotProjectileCriterion extends AbstractCriterion<ShotProjectileCriterion.Conditions> {
	/**
	 * 触发方法
	 *
	 * @param player     触发玩家
	 * @param ranged     远程物品
	 * @param projectile 发射物
	 */
	public void trigger(ServerPlayerEntity player, ItemStack ranged, Entity projectile) {
		trigger(player, ranged, projectile, 1);
	}
	
	/**
	 * 触发方法
	 *
	 * @param player     触发玩家
	 * @param ranged     远程物品
	 * @param projectile 发射物
	 * @param count      发射数量
	 */
	public void trigger(ServerPlayerEntity player, ItemStack ranged, Entity projectile, int count) {
		trigger(player, conditions -> conditions.matches(player, ranged, projectile, count));
	}
	
	@Override
	public Codec<ShotProjectileCriterion.Conditions> getConditionsCodec() {
		return ShotProjectileCriterion.Conditions.CODEC;
	}
	
	/**
	 * 射击发射物标准条件
	 *
	 * @param player     玩家
	 * @param ranged     远程武器
	 * @param projectile 发射物
	 * @param amount     发射数量
	 */
	public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> ranged, Optional<EntityPredicate> projectile,
	                         NumberRange.IntRange amount) implements AbstractCriterion.Conditions {
		/**
		 * 解编码器
		 */
		public static final Codec<ShotProjectileCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(ShotProjectileCriterion.Conditions::player),
						Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "stack").forGetter(ShotProjectileCriterion.Conditions::ranged),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.CODEC, "entity").forGetter(ShotProjectileCriterion.Conditions::projectile),
						Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "amount", NumberRange.IntRange.ANY).forGetter(ShotProjectileCriterion.Conditions::amount))
				.apply(instance, ShotProjectileCriterion.Conditions::new));
		
		/**
		 * 通过远程武器创建构建器
		 *
		 * @param ranged 远程武器物品谓词
		 * @return 构建器
		 */
		public static Builder ranged(Optional<ItemPredicate> ranged) {
			return new Builder(ranged);
		}
		
		/**
		 * 通过远程武器创建构建器
		 *
		 * @param ranged 远程武器物品
		 * @return 构建器
		 */
		public static Builder ranged(ItemConvertible ranged) {
			Optional<ItemPredicate> itemPredicates = Optional.of(ItemPredicate.Builder.create().items(ranged.asItem()).build());
			return ranged(itemPredicates);
		}
		
		/**
		 * 创建进度标准
		 *
		 * @return 进度标准
		 */
		public AdvancementCriterion<ShotProjectileCriterion.Conditions> create() {
			return Criteria.SHOT_PROJECTILE_CRITERION.create(this);
		}
		
		/**
		 * 比对方法
		 *
		 * @param player 玩家
		 * @param stack  远程武器
		 * @param entity 发射物
		 * @param count  发射计数
		 * @return 对比结果
		 */
		public boolean matches(ServerPlayerEntity player, ItemStack stack, Entity entity, int count) {
			if (ranged.isEmpty() || !ranged.get().test(stack)) return false;
			if (projectile.isPresent() && !projectile.get().test(player, entity)) return false;
			CountState.Data data = CountState.getPlayerState(player);
			String conditions = "" + ranged + projectile + amount;
			data.counts.put(conditions, Optional.ofNullable(data.counts.get(conditions)).orElse(0) + count);
			int countSum = Optional.ofNullable(data.counts.get(conditions)).orElse(0);
			if (!amount.test(countSum)) return false;
			data.counts.put(conditions, 0);
			return true;
		}
		
		@Override
		public Optional<LootContextPredicate> player() {
			return player;
		}
		
		/**
		 * 构建器
		 */
		public static final class Builder {
			private final Optional<ItemPredicate> ranged;
			private Optional<EntityPredicate> projectile;
			private NumberRange.IntRange amount;
			
			private Builder(Optional<ItemPredicate> ranged) {
				this.ranged = ranged;
				this.projectile = Optional.empty();
				this.amount = NumberRange.IntRange.ANY;
			}
			
			/**
			 * 设置发射物
			 *
			 * @param projectile 发射物
			 * @return 构建器
			 */
			public Builder projectile(EntityPredicate projectile) {
				this.projectile = Optional.of(projectile);
				return this;
			}
			
			/**
			 * 设置发射数量
			 *
			 * @param amount 发射数量
			 * @return 构建器
			 */
			public Builder amount(NumberRange.IntRange amount) {
				this.amount = amount;
				return this;
			}
			
			/**
			 * 构建方法
			 *
			 * @return 条件
			 */
			public ShotProjectileCriterion.Conditions build() {
				return new ShotProjectileCriterion.Conditions(Optional.empty(), ranged, projectile, amount);
			}
		}
	}
	
	private static class CountState extends PersistentState {
		public static final String STATE_ID = "shotProjectileCriterion";
		private static final Type<CountState> TYPE = new Type<>(CountState::new, CountState::createFromNbt, DataFixTypes.PLAYER);
		private final HashMap<UUID, Data> players = new HashMap<>();
		
		private static CountState createFromNbt(NbtCompound nbt) {
			CountState state = new CountState();
			NbtCompound playersNbt = nbt.getCompound(STATE_ID);
			playersNbt.getKeys().forEach(key -> {
				Data data = new Data();
				NbtCompound countsNbt = playersNbt.getCompound(key).getCompound("counts");
				countsNbt.getKeys().forEach(conditions -> data.counts.put(conditions, countsNbt.getInt(conditions)));
				UUID uuid = UUID.fromString(key);
				state.players.put(uuid, data);
			});
			return state;
		}
		
		private static CountState getServerState(MinecraftServer server) {
			PersistentStateManager persistentStateManager = server.getOverworld().getPersistentStateManager();
			CountState state = persistentStateManager.getOrCreate(TYPE, SilkApi.getInternal().id() + '.' + STATE_ID);
			state.markDirty();
			return state;
		}
		
		private static Data getPlayerState(LivingEntity player) {
			CountState serverState = getServerState(Optional.ofNullable(player.getWorld().getServer()).orElseThrow());
			return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new Data());
		}
		
		@Override
		public NbtCompound writeNbt(NbtCompound nbt) {
			NbtCompound playersNbt = new NbtCompound();
			players.forEach((uuid, data) -> {
				NbtCompound playerNbt = new NbtCompound();
				NbtCompound countsNbt = new NbtCompound();
				data.counts.forEach(countsNbt::putInt);
				playerNbt.put("counts", countsNbt);
				playersNbt.put(uuid.toString(), playerNbt);
			});
			nbt.put(STATE_ID, playersNbt);
			return nbt;
		}
		
		private static class Data {
			private final HashMap<String, Integer> counts = new HashMap<>();
		}
	}
}
