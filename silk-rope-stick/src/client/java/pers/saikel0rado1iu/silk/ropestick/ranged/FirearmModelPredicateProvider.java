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

package pers.saikel0rado1iu.silk.ropestick.ranged;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import static net.minecraft.item.CrossbowItem.CHARGED_PROJECTILES_KEY;

/**
 * <h2 style="color:FFC800">枪械模型谓词提供器</h2>
 * 用于枪械的模型谓词提供器
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public interface FirearmModelPredicateProvider {
	/**
	 * 注册模型谓词
	 *
	 * @param firearm 枪械
	 * @param <T>     枪械类型
	 */
	static <T extends Crossbow & ProjectileContainer & ShootExpansion> void register(T firearm) {
		ModelPredicateProviderRegistry.register(firearm, new Identifier(Crossbow.PULLING_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return entity.isUsingItem() && isActive(entity.getActiveItem(), stack) ? 1 : 0;
		});
		ModelPredicateProviderRegistry.register(firearm, new Identifier(Crossbow.PULL_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return !isActive(entity.getActiveItem(), stack) ? 0 : ((Crossbow) stack.getItem()).getUsingProgress(stack.getMaxUseTime() - entity.getItemUseTimeLeft() - 1, stack);
		});
		ModelPredicateProviderRegistry.register(firearm, new Identifier(Crossbow.CHARGED_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return CrossbowItem.isCharged(stack) ? 1 : 0;
		});
		ModelPredicateProviderRegistry.register(firearm, new Identifier(Crossbow.PROJECTILE_INDEX_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return ((Crossbow) stack.getItem()).getProjectileIndex(stack);
		});
		ShootExpansionModelPredicateProvider.register(firearm);
	}
	
	private static boolean isActive(ItemStack active, ItemStack stack) {
		NbtCompound nbt0 = active.getOrCreateNbt();
		NbtCompound nbt1 = stack.getOrCreateNbt();
		nbt0.remove(CHARGED_PROJECTILES_KEY);
		nbt1.remove(CHARGED_PROJECTILES_KEY);
		return active.isOf(stack.getItem()) && active.getCount() == stack.getCount() && nbt0.equals(nbt1);
	}
}
