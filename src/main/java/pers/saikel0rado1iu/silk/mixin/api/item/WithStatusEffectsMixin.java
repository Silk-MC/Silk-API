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

package pers.saikel0rado1iu.silk.mixin.api.item;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.saikel0rado1iu.silk.api.item.WithStatusEffects;

import java.util.*;
import java.util.stream.StreamSupport;

import static net.minecraft.entity.effect.StatusEffectInstance.INFINITE;

/**
 * <p><b style="color:FFC800"><font size="+1">设置自带状态效果的物品</font></b></p>
 * <style="color:FFC800">
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"><p>
 * @since 0.1.0
 */
@Mixin(LivingEntity.class)
abstract class WithStatusEffectsMixin extends Entity implements Attackable {
	@Unique
	private final Set<StatusEffectInstance> instances = Sets.newHashSetWithExpectedSize(5);
	
	public WithStatusEffectsMixin(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot var1);
	
	@Override
	@Shadow
	public abstract Iterable<ItemStack> getArmorItems();
	
	@Shadow
	public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);
	
	@Shadow
	public abstract Collection<StatusEffectInstance> getStatusEffects();
	
	@Inject(method = "tick", at = @At("RETURN"))
	private void setStatusEffects(CallbackInfo ci) {
		// 清除所有物品自带状态效果
		instances.forEach(statusEffectInstance -> getStatusEffects().remove(statusEffectInstance));
		instances.clear();
		// 获取实体全部物品堆栈
		List<ItemStack> hasStack;
		if (((LivingEntity) (Object) this) instanceof PlayerEntity player) {
			hasStack = new ArrayList<>(player.getInventory().main.stream().toList());
			hasStack.addAll(player.getInventory().armor.stream().toList());
			hasStack.addAll(player.getInventory().offHand.stream().toList());
		} else {
			hasStack = StreamSupport.stream(getArmorItems().spliterator(), false).toList();
		}
		// 将所有堆栈转化为所有物品与物品个数
		Map<Item, Integer> hasItem = Maps.newHashMapWithExpectedSize(45);
		hasStack.forEach(stack -> {
			Item item = stack.getItem();
			Integer counts = hasItem.get(item);
			hasItem.put(item, counts == null ? stack.getCount() : counts + stack.getCount());
		});
		// 获取所有状态效果物品
		Set<WithStatusEffects> hasWith = Sets.newHashSetWithExpectedSize(45);
		hasItem.keySet().forEach(item -> {
			if (item instanceof WithStatusEffects with) hasWith.add(with);
		});
		if (hasWith.isEmpty()) return;
		// 获取所有套装效果组
		Map<Map<Item, Optional<Set<EquipmentSlot>>>, Integer> kitsMap = Maps.newHashMapWithExpectedSize(45);
		hasWith.forEach(with -> with.getStatusEffects().keySet().forEach(effect -> {
			Map<Item, Optional<Set<EquipmentSlot>>> map = with.getStatusEffectsKit().get(effect).orElse(new HashMap<>(1));
			map.put((Item) with, with.getEffectiveEquipmentSlot());
			kitsMap.put(map, 0);
		}));
		// 获取所有套装效果组触发物品数
		kitsMap.forEach((itemMap, integer) -> {
			final int[] triggerCount = {integer};
			hasStack.forEach(stack -> {
				Item item = stack.getItem();
				int num = stack.getCount();
				if (!itemMap.containsKey(item)) return;
				Set<EquipmentSlot> slots;
				if (item instanceof WithStatusEffects with) slots = with.getEffectiveEquipmentSlot().orElse(null);
				else slots = itemMap.get(item).orElse(null);
				triggerCount[0] += slots != null
						? (slots.stream().anyMatch(slot -> getEquippedStack(slot).getItem().equals(item))
						? num : 0) : num;
			});
			kitsMap.put(itemMap, triggerCount[0]);
		});
		// 转换为 kitsSet
		Map<Set<Item>, Integer> kitsSet = Maps.newHashMapWithExpectedSize(45);
		kitsMap.forEach((itemMap, integer) -> kitsSet.put(itemMap.keySet(), integer));
		// 设置状态效果
		hasWith.forEach(with -> {
			Map<StatusEffect, Integer> maxLevels = with.getStatusEffects();
			Map<StatusEffect, Float> stackingLevels = with.getStatusEffectsStackingLevel();
			Map<StatusEffect, Optional<Map<Item, Optional<Set<EquipmentSlot>>>>> kits = with.getStatusEffectsKit();
			Map<StatusEffect, Optional<Integer>> thresholds = with.getKitTriggerThreshold();
			for (StatusEffect effect : maxLevels.keySet()) {
				int maxLevel = maxLevels.get(effect);
				float stackingLevel = stackingLevels.get(effect);
				Set<Item> kit = kits.get(effect).isEmpty() ? new HashSet<>(1) : kits.get(effect).get().keySet();
				kit.add((Item) with);
				int threshold = thresholds.get(effect).isPresent()
						? (kit.size() == 1 ? Math.max(1, thresholds.get(effect).get()) : Math.min(kit.size(), Math.max(1, thresholds.get(effect).get())))
						: (kit.size() == 1 ? 1 : thresholds.get(effect).get());
				// 是否能触发效果
				if (kitsSet.get(kit) >= threshold) {
					int level = (int) Math.min(maxLevel, stackingLevel * (kitsSet.get(kit) - threshold));
					StatusEffectInstance e = new StatusEffectInstance(effect, INFINITE, level);
					StatusEffectInstance i = getStatusEffect(effect);
					if (i != null) {
						if (i.getAmplifier() >= level && i.getDuration() > INFINITE) return;
						removeStatusEffect(effect);
					}
					instances.add(e);
					addStatusEffect(e);
				} else {
					StatusEffectInstance i = getStatusEffect(effect);
					if (i != null) {
						if (i.getDuration() == INFINITE) getStatusEffects().remove(i);
					}
				}
			}
		});
	}
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
}
