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

package pers.saikel0rado1iu.silk.mixin.ropestick.property;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.ropestick.property.EffectiveItemSlot;
import pers.saikel0rado1iu.silk.ropestick.property.InherentStatusEffect;
import pers.saikel0rado1iu.silk.ropestick.property.ItemProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.minecraft.entity.effect.StatusEffectInstance.INFINITE;

/**
 * <h2 style="color:FFC800">{@link InherentStatusEffect} 混入</h2>
 * 设置自带状态效果的物品
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><se>
 * @since 1.0.0
 */
@Mixin(LivingEntity.class)
abstract class InherentStatusEffectMixin extends Entity implements Attackable {
	@Unique
	private final Set<StatusEffectInstance> instances = Sets.newHashSetWithExpectedSize(5);
	
	public InherentStatusEffectMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Override
	@Shadow
	public abstract Iterable<ItemStack> getArmorItems();
	
	@Shadow
	public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);
	
	@Inject(method = "tick", at = @At("RETURN"))
	private void setStatusEffects(CallbackInfo ci) {
		// 清除所有物品自带状态效果
		instances.forEach(statusEffectInstance -> removeStatusEffect(statusEffectInstance.getEffectType()));
		instances.clear();
		// 获取实体全部物品堆栈
		ImmutableList.Builder<ItemStack> stackBuilder = ImmutableList.builder();
		if (((LivingEntity) (Object) this) instanceof PlayerEntity player) {
			stackBuilder.addAll(player.getInventory().main)
					.addAll(player.getInventory().armor)
					.addAll(player.getInventory().offHand);
		} else {
			stackBuilder.addAll(getArmorItems());
		}
		List<ItemStack> stacks = stackBuilder.build();
		// 将物品堆栈转换为物品与物品数量图表
		HashMap<Item, Integer> itemsBuilder = new HashMap<>();
		for (ItemStack stack : stacks) {
			Item item = stack.getItem();
			int count = stack.getCount();
			itemsBuilder.put(item, itemsBuilder.getOrDefault(item, 0) + count);
		}
		ImmutableMap<Item, Integer> items = ImmutableMap.copyOf(itemsBuilder);
		// 获取物品中所有的自带状态效果
		HashMap<InherentStatusEffect.Property, Item> effectItemsBuilder = Maps.newHashMapWithExpectedSize(5);
		for (Item item : items.keySet()) {
			Optional<InherentStatusEffect> property = ItemProperty.get(item, InherentStatusEffect.class);
			property.ifPresent(p -> p.inherentStatusEffects().forEach(effect -> effectItemsBuilder.put(effect, item)));
		}
		ImmutableMap<InherentStatusEffect.Property, Item> effectItems = ImmutableMap.copyOf(effectItemsBuilder);
		if (effectItems.isEmpty()) return;
		// 设置自带状态效果
		for (InherentStatusEffect.Property property : effectItems.keySet()) {
			StatusEffect effect = property.statusEffect();
			int baseLevel = Math.max(1, property.baseLevel());
			int maxLevel = Math.max(1, property.maxLevel());
			float stackingLevel = property.stackingLevel();
			Set<Item> kit = property.statusEffectKit().get();
			int threshold = Math.max(1, property.kitTriggerThreshold());
			EffectiveItemSlot slot = property.effectiveItemSlot();
			Integer itemCount = 0;
			if (kit.isEmpty()) {
				Item item = Optional.ofNullable(effectItems.get(property)).orElse(Items.AIR);
				itemCount = slot.isEffective(LivingEntity.class.cast(this), item)
						? (slot.slots().isEmpty() ? Optional.ofNullable(items.get(item)).orElse(0) : 1)
						: 0;
			} else {
				for (Item item : kit) {
					itemCount += slot.isEffective(LivingEntity.class.cast(this), item)
							? (slot.slots().isEmpty() ? Optional.ofNullable(items.get(item)).orElse(0) : 1)
							: 0;
				}
			}
			if (itemCount == null || itemCount < threshold) continue;
			int level = Math.min(maxLevel - 1, Math.round(Math.max(baseLevel - 1, stackingLevel * (itemCount - threshold))));
			StatusEffectInstance instance = new StatusEffectInstance(effect, INFINITE, level);
			StatusEffectInstance oldInstance = getStatusEffect(effect);
			if (oldInstance != null && (oldInstance.getAmplifier() > level || oldInstance.getDuration() != INFINITE)) continue;
			instances.add(instance);
		}
		instances.forEach(this::addStatusEffect);
	}
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
}
