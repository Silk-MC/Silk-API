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

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.item.PiglinIgnore;

import static net.minecraft.entity.player.PlayerInventory.MAIN_SIZE;

/**
 * <h2 style="color:FFC800">设置猪灵忽视此物品</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@Mixin(PiglinBrain.class)
abstract class PiglinIgnoreMixin {
	@Inject(method = "wearsGoldArmor", at = @At("RETURN"), cancellable = true)
	private static void hasPiglinIgnoreItem(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			Item slotItem = entity.getEquippedStack(slot).getItem();
			PiglinIgnore item = null;
			if (slotItem instanceof ArmorItem a && a.getMaterial() instanceof PiglinIgnore p) item = p;
			if (slotItem instanceof ToolItem t && t.getMaterial() instanceof PiglinIgnore p) item = p;
			if (slotItem instanceof PiglinIgnore p) item = p;
			if (item == null) continue;
			if (item.getEffectiveEquipmentSlot().isPresent() && item.getEffectiveEquipmentSlot().get().stream().allMatch(equipmentSlot -> equipmentSlot != slot))
				continue;
			cir.setReturnValue(true);
			return;
		}
		if (!(entity instanceof PlayerEntity player)) return;
		for (int count = 0; count < MAIN_SIZE; count++) {
			ItemStack stack = player.getInventory().getStack(count);
			Item slotItem = stack.getItem();
			PiglinIgnore item = null;
			if (slotItem instanceof ArmorItem a && a.getMaterial() instanceof PiglinIgnore p) item = p;
			if (slotItem instanceof ToolItem t && t.getMaterial() instanceof PiglinIgnore p) item = p;
			if (slotItem instanceof PiglinIgnore p) item = p;
			if (item == null) continue;
			if (item.getEffectiveEquipmentSlot().isPresent()) continue;
			cir.setReturnValue(true);
			return;
		}
	}
}
