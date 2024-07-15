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

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.ropestick.property.ItemProperty;
import pers.saikel0rado1iu.silk.ropestick.property.PiglinIgnore;

import java.util.Optional;
import java.util.Set;

import static net.minecraft.entity.player.PlayerInventory.MAIN_SIZE;

/**
 * <h2 style="color:FFC800">{@link PiglinIgnore} 混入</h2>
 * 设置猪灵忽视装备了此物品的实体
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(PiglinBrain.class)
abstract class PiglinIgnoreMixin {
	@Inject(method = "wearsGoldArmor", at = @At("RETURN"), cancellable = true)
	private static void hasPiglinIgnoreItem(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			Optional<PiglinIgnore> property = ItemProperty.get(entity.getEquippedStack(slot).getItem(), PiglinIgnore.class);
			if (property.isEmpty()) continue;
			Set<EquipmentSlot> slots = property.get().effectiveItemSlot().slots();
			if (slots.isEmpty() || slots.stream().anyMatch(equipmentSlot -> equipmentSlot == slot)) {
				cir.setReturnValue(true);
				return;
			}
		}
		if (!(entity instanceof PlayerEntity player)) return;
		for (int count = 0; count < MAIN_SIZE; count++) {
			Optional<PiglinIgnore> property = ItemProperty.get(player.getInventory().getStack(count).getItem(), PiglinIgnore.class);
			if (property.isEmpty()) continue;
			Set<EquipmentSlot> slots = property.get().effectiveItemSlot().slots();
			if (slots.isEmpty()) {
				cir.setReturnValue(true);
				return;
			}
		}
	}
}
