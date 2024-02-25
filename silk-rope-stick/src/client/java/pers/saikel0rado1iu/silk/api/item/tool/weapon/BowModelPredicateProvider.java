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

package pers.saikel0rado1iu.silk.api.item.tool.weapon;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.annotation.SilkApi;
import pers.saikel0rado1iu.silk.api.item.tool.weapon.ranged.Bow;

/**
 * <h2 style="color:FFC800">用于弓的模型谓词提供器</h2>
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
@SilkApi
public interface BowModelPredicateProvider {
	@SilkApi
	static void register(Bow bow) {
		ModelPredicateProviderRegistry.register(bow, new Identifier(Bow.PULLING_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return entity.isUsingItem() && entity.getActiveItem() == stack ? 1 : 0;
		});
		ModelPredicateProviderRegistry.register(bow, new Identifier(Bow.PULL_KEY), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return entity.getActiveItem() != stack ? 0 : ((Bow) stack.getItem()).getUsingProgress(stack.getMaxUseTime() - entity.getItemUseTimeLeft(), stack);
		});
		ModelPredicateProviderRegistry.register(bow, new Identifier(Bow.PROJECTILE_ID_KEY.toLowerCase()), (stack, world, entity, seed) -> {
			if (entity == null) return 0;
			return entity.getActiveItem() != stack ? 0 : ((Bow) stack.getItem()).getProjectileId(stack);
		});
	}
}
