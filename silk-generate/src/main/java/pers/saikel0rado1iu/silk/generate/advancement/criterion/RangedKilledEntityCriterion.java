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

package pers.saikel0rado1iu.silk.generate.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import pers.saikel0rado1iu.silk.impl.SilkApi;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * <h2 style="color:FFC800">远程武器击杀实体标准</h2>
 * 远程武器是通过发射物的自定义名称中储存的物品标识符来判断的<br>
 * 需要使用 {@link RangedKilledEntityCriterion#setRangedWeapon(Entity, ItemStack)} 将远程武器手动添加到发射物中<br>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public class RangedKilledEntityCriterion extends AbstractCriterion<RangedKilledEntityCriterion.Conditions> {
	/**
	 * 设置远程武器
	 *
	 * @param projectile 发射物实体
	 * @param ranged     远程武器
	 */
	public static void setRangedWeapon(Entity projectile, ItemStack ranged) {
		projectile.setCustomName(Text.literal(Registries.ITEM.getId(ranged.getItem()).toString()));
	}
	
	/**
	 * 触发方法
	 *
	 * @param player       触发玩家
	 * @param entity       击杀实体
	 * @param damageSource 伤害来源
	 */
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource) {
		trigger(player, entity, damageSource, 1);
	}
	
	/**
	 * 触发方法
	 *
	 * @param player       触发玩家
	 * @param entity       击杀实体
	 * @param damageSource 伤害来源
	 * @param killed       击杀数量
	 */
	public void trigger(ServerPlayerEntity player, Entity entity, DamageSource damageSource, int killed) {
		LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
		trigger(player, conditions -> conditions.matches(player, lootContext, damageSource.getSource(), killed));
	}
	
	@Override
	public Codec<RangedKilledEntityCriterion.Conditions> getConditionsCodec() {
		return RangedKilledEntityCriterion.Conditions.CODEC;
	}
	
	/**
	 * 远程武器击杀实体标准条件
	 *
	 * @param player     玩家
	 * @param ranged     远程武器
	 * @param target     击杀目标
	 * @param projectile 发射物
	 * @param killed     击杀数量
	 */
	public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> ranged, Optional<LootContextPredicate> target,
	                         Optional<EntityPredicate> projectile, NumberRange.IntRange killed) implements AbstractCriterion.Conditions {
		/**
		 * 解编码器
		 */
		public static final Codec<RangedKilledEntityCriterion.Conditions> CODEC = RecordCodecBuilder.create(instance -> instance.group(
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(RangedKilledEntityCriterion.Conditions::player),
						Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "ranged").forGetter(RangedKilledEntityCriterion.Conditions::ranged),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "target").forGetter(RangedKilledEntityCriterion.Conditions::target),
						Codecs.createStrictOptionalFieldCodec(EntityPredicate.CODEC, "entity").forGetter(RangedKilledEntityCriterion.Conditions::projectile),
						Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "killed", NumberRange.IntRange.ANY).forGetter(RangedKilledEntityCriterion.Conditions::killed))
				.apply(instance, RangedKilledEntityCriterion.Conditions::new));
		
		/**
		 * 通过设置远程武器创建构建器
		 *
		 * @param ranged 远程武器物品谓词
		 * @return 构建器
		 */
		public static Builder ranged(Optional<ItemPredicate> ranged) {
			return new Builder(ranged);
		}
		
		/**
		 * 通过设置远程武器创建构建器
		 *
		 * @param ranged 远程武器物品
		 * @return 构建器
		 */
		public static Builder ranged(ItemConvertible ranged) {
			ItemPredicate itemPredicates = ItemPredicate.Builder.create().items(ranged.asItem()).build();
			return ranged(Optional.of(itemPredicates));
		}
		
		/**
		 * 创建进度标准
		 *
		 * @return 进度标准
		 */
		public AdvancementCriterion<RangedKilledEntityCriterion.Conditions> create() {
			return Criteria.RANGED_KILLED_ENTITY_CRITERION.create(this);
		}
		
		/**
		 * 比对方法
		 *
		 * @param player              玩家
		 * @param killedEntityContext 已被杀死的实体上下文
		 * @param entity              发射物
		 * @param count               击杀计数
		 * @return 对比结果
		 */
		public boolean matches(ServerPlayerEntity player, LootContext killedEntityContext, Entity entity, int count) {
			if (target.isPresent() && !target.get().test(killedEntityContext)) return false;
			if (entity == null) return false;
			String[] id = Optional.ofNullable(Optional.ofNullable(entity.getCustomName()).orElse(Text.literal("")).getLiteralString()).orElse("").split(":");
			ItemStack stack = new ItemStack("".equals(id[0]) ? Items.AIR : Registries.ITEM.get(new Identifier(id[0], id[1])));
			if (ranged.isEmpty() || !ranged.get().test(stack)) return false;
			if (projectile.isPresent() && !projectile.get().test(player, entity)) return false;
			CountState.Data data = CountState.getPlayerState(player);
			String targetString = target.isEmpty() ? Optional.empty().toString() : LootContextPredicate.CODEC.encodeStart(NbtOps.INSTANCE, target.get()).result().toString();
			String conditions = ranged + targetString + projectile + killed;
			data.counts.put(conditions, Optional.ofNullable(data.counts.get(conditions)).orElse(0) + count);
			int countSum = Optional.ofNullable(data.counts.get(conditions)).orElse(0);
			if (!killed.test(countSum)) return false;
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
			private Optional<LootContextPredicate> target;
			private Optional<EntityPredicate> projectile;
			private NumberRange.IntRange killed;
			
			private Builder(Optional<ItemPredicate> ranged) {
				this.ranged = ranged;
				this.target = Optional.empty();
				this.projectile = Optional.empty();
				this.killed = NumberRange.IntRange.ANY;
			}
			
			/**
			 * 设置击杀目标
			 *
			 * @param target 击杀目标
			 * @return 构建器
			 */
			public Builder target(EntityPredicate target) {
				this.target = Optional.of(EntityPredicate.asLootContextPredicate(target));
				return this;
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
			 * 设置击杀数量
			 *
			 * @param killed 击杀数量
			 * @return 构建器
			 */
			public Builder killed(NumberRange.IntRange killed) {
				this.killed = killed;
				return this;
			}
			
			/**
			 * 构建方法
			 *
			 * @return 条件
			 */
			public RangedKilledEntityCriterion.Conditions build() {
				return new RangedKilledEntityCriterion.Conditions(Optional.empty(), ranged, target, projectile, killed);
			}
		}
	}
	
	private static class CountState extends PersistentState {
		public static final String STATE_ID = "rangedKilledEntityCriterion";
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
