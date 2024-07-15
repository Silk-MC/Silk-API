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

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtString;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import pers.saikel0rado1iu.silk.common.util.TickUtil;
import pers.saikel0rado1iu.silk.generate.data.RecipeGenUtil;
import pers.saikel0rado1iu.silk.generate.data.family.EquipFamily;
import pers.saikel0rado1iu.silk.generate.recipe.NbtShapedRecipeJsonBuilder;
import pers.saikel0rado1iu.silk.impl.SilkGenerate;

/**
 * Test {@link RecipeGenUtil}
 */
public final class RecipeGenUtilTest extends FabricRecipeProvider {
	/**
	 * @param output 数据输出
	 */
	public RecipeGenUtilTest(FabricDataOutput output) {
		super(output);
	}
	
	@Override
	public void generate(RecipeExporter exporter) {
		RecipeGenUtil.generateFamily(exporter, Ingredient.ofItems(Items.DIAMOND), EquipFamily.builder()
				.shovel(Items.DIAMOND_SHOVEL).pickaxe(Items.DIAMOND_PICKAXE).axe(Items.DIAMOND_AXE).hoe(Items.DIAMOND_HOE).sword(Items.DIAMOND_SWORD)
				.helmet(Items.DIAMOND_HELMET).chestplate(Items.DIAMOND_CHESTPLATE).leggings(Items.DIAMOND_LEGGINGS).boots(Items.DIAMOND_BOOTS).build());
		RecipeGenUtil.offer2x2CompactingRecipeWithRecipeGroup(exporter, RecipeCategory.MISC, Items.DIAMOND, Items.DIAMOND_BLOCK);
		RecipeGenUtil.offer2x2CrossCompactingRecipe(exporter, RecipeCategory.MISC, Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.GOLD_INGOT), Items.NETHER_BRICK);
		RecipeGenUtil.offerCrossCompactingRecipe(exporter, RecipeCategory.MISC, Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.GOLD_INGOT), Items.NETHERITE_BLOCK);
		RecipeGenUtil.offerReversibleSmithingRecipe(exporter, RecipeCategory.MISC, Ingredient.ofItems(Items.DIAMOND), Ingredient.ofItems(Items.GOLD_INGOT), Items.NETHER_BRICK);
		RecipeGenUtil.offerSmelting(exporter, ImmutableSet.of(Items.GOLDEN_SHOVEL, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS),
				Items.GOLD_NUGGET, 0.15F, TickUtil.getTick(20), getItemPath(Items.GOLD_NUGGET));
		RecipeGenUtil.offerBlasting(exporter, ImmutableSet.of(Items.GOLDEN_SHOVEL, Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS),
				Items.GOLD_NUGGET, 0.15F, TickUtil.getTick(10), getItemPath(Items.GOLD_NUGGET));
		ItemStack out1 = new ItemStack(Items.TIPPED_ARROW, 4);
		out1.setSubNbt("Potion", NbtString.of("poison"));
		NbtShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, out1).group(getItemPath(out1.getItem())).input('#', Items.STRING).input('X', Items.STICK).input('@', Items.FEATHER)
				.pattern("#")
				.pattern("X")
				.pattern("@")
				.criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
				.criterion(hasItem(Items.STICK), conditionsFromItem(Items.STICK))
				.criterion(hasItem(Items.FEATHER), conditionsFromItem(Items.FEATHER))
				.offerTo(exporter, SilkGenerate.getInstance().ofId("poison_tipped_arrow_with_spider_fang"));
	}
}
