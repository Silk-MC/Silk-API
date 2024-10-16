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

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.featuretoggle.FeatureSet;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.saikel0rado1iu.silk.api.item.CustomEnchantment;

import java.util.List;

/**
 * <h2 style="color:FFC800">设置自定义物品附魔</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
interface CustomEnchantmentMixin {
	/**
	 * 设置自定义附魔能被接受
	 */
	@Mixin(Enchantment.class)
	abstract class SetAcceptEnchantment {
		/**
		 * 如果物品为自定义物品判断此魔咒是否包含在自定义魔咒中，所以请忽略 'EqualsBetweenInconvertibleTypes' 警告
		 */
		@SuppressWarnings("EqualsBetweenInconvertibleTypes")
		@Inject(method = "isAcceptableItem", at = @At("RETURN"), cancellable = true)
		private void acceptEnchantment(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
			if (stack.getItem() instanceof CustomEnchantment item) {
				if (item.getEnchantments().stream().anyMatch(enchantment -> enchantment.equals(this))) cir.setReturnValue(true);
				else cir.setReturnValue(false);
			}
		}
	}
	
	/**
	 * 设置附魔台附魔能够附魔自定义魔咒
	 */
	@Mixin(EnchantmentHelper.class)
	@ApiStatus.Obsolete
	abstract class SetTableEnchantment {
		/**
		 * *已过时*
		 * <p>
		 * 设置附魔判断为 {@link Enchantment#isAcceptableItem(ItemStack)} 而不是 EnchantmentTarget#isAcceptableItem(Item)
		 */
		@Inject(method = "getPossibleEntries", at = @At("RETURN"))
		private static void setEnchantment(FeatureSet enabledFeatures, int level, ItemStack stack, boolean treasureAllowed, CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir) {
/*			ArrayList<EnchantmentLevelEntry> enchantments = Lists.newArrayList();
			nextEnchantment:
			for (Enchantment enchantment : Registries.ENCHANTMENT) {
				if (enchantment.isTreasure() && !treasureAllowed || !enchantment.isAvailableForRandomSelection()
						|| !enchantment.isAcceptableItem(stack) && !stack.isOf(Items.BOOK)) continue;
				for (int level = enchantment.getMaxLevel(); level > enchantment.getMinLevel() - 1; level--) {
					if (power < enchantment.getMinPower(level) || power > enchantment.getMaxPower(level)) continue;
					enchantments.add(new EnchantmentLevelEntry(enchantment, level));
					continue nextEnchantment;
				}
			}
			cir.setReturnValue(enchantments);*/
		}
	}
}
