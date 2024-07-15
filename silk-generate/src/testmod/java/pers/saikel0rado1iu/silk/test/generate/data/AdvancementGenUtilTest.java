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

package pers.saikel0rado1iu.silk.test.generate.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;
import pers.saikel0rado1iu.silk.generate.advancement.criterion.RangedKilledEntityCriterion;
import pers.saikel0rado1iu.silk.generate.advancement.criterion.ShotProjectileCriterion;
import pers.saikel0rado1iu.silk.generate.data.AdvancementGenUtil;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;
import pers.saikel0rado1iu.silk.spinningjenny.tag.EntityTypeTags;

import java.util.function.Consumer;

import static pers.saikel0rado1iu.silk.test.generate.Items.TEST_BOW;
import static pers.saikel0rado1iu.silk.test.generate.Items.TEST_CROSSBOW;

/**
 * Test {@link AdvancementGenUtil}
 */
public final class AdvancementGenUtilTest extends FabricAdvancementProvider {
	/**
	 * root
	 */
	public static final AdvancementEntry ROOT = AdvancementGenUtil.builder(SilkGenerate.getInstance(), "root")
			.display(Items.CRAFTING_TABLE,
					new Identifier("textures/block/andesite.png"),
					AdvancementFrame.TASK,
					true,
					true,
					false)
			.criterion(RecipeProvider.hasItem(Items.CRAFTING_TABLE), ShotProjectileCriterion.Conditions
					.ranged(TEST_BOW).projectile(EntityPredicate.Builder.create().type(EntityType.ARROW).build())
					.amount(NumberRange.IntRange.atLeast(10))
					.build().create())
			.build();
	/**
	 * test
	 */
	public static final AdvancementEntry TEST = AdvancementGenUtil.builder(SilkGenerate.getInstance(), "vanilla", "test")
			.parent(ROOT)
			.display(Items.CRAFTING_TABLE,
					new Identifier("textures/block/andesite.png"),
					AdvancementFrame.TASK,
					true,
					true,
					false)
			.criterion(RecipeProvider.hasItem(Items.CRAFTING_TABLE), RangedKilledEntityCriterion.Conditions
					.ranged(TEST_CROSSBOW).target(EntityPredicate.Builder.create().type(EntityTypeTags.MONSTERS).build())
					.projectile(EntityPredicate.Builder.create().type(EntityType.ARROW).build())
					.killed(NumberRange.IntRange.atLeast(10))
					.build().create())
			.build();
	/**
	 * test_2
	 */
	public static final AdvancementEntry TEST_1 = AdvancementGenUtil.builder(SilkGenerate.getInstance(), "vanilla", "test_1")
			.parent(ROOT)
			.display(Items.CRAFTING_TABLE,
					new Identifier("textures/block/andesite.png"),
					AdvancementFrame.TASK,
					true,
					true,
					false)
			.criterion(RecipeProvider.hasItem(Items.CRAFTING_TABLE), ShotProjectileCriterion.Conditions
					.ranged(TEST_CROSSBOW).projectile(EntityPredicate.Builder.create().type(EntityType.ARROW).build())
					.amount(NumberRange.IntRange.atLeast(10))
					.build().create())
			.build();
	
	/**
	 * @param output 数据输出
	 */
	public AdvancementGenUtilTest(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generateAdvancement(Consumer<AdvancementEntry> consumer) {
		consumer.accept(ROOT);
		consumer.accept(TEST);
		consumer.accept(TEST_1);
	}
}
