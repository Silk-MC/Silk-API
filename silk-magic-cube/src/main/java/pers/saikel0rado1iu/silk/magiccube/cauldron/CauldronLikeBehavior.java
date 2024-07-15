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

package pers.saikel0rado1iu.silk.magiccube.cauldron;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">坩埚类行为</h2>
 * 用于专门创建一个与原版坩埚有相同功能的方块必要的行为
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.0.0
 */
public abstract class CauldronLikeBehavior {
	/**
	 * 坩埚行为图表
	 */
	public Map<String, CauldronBehavior.CauldronBehaviorMap> behaviorMaps = new Object2ObjectArrayMap<>();
	/**
	 * 空坩埚行为图表
	 */
	public CauldronBehavior.CauldronBehaviorMap emptyBehavior = createMap("empty");
	/**
	 * 含水坩埚行为图表
	 */
	public CauldronBehavior.CauldronBehaviorMap waterBehavior = createMap("water");
	/**
	 * 含熔岩坩埚行为图表
	 */
	public CauldronBehavior.CauldronBehaviorMap lavaBehavior = createMap("lava");
	/**
	 * 含细雪坩埚行为图表
	 */
	public CauldronBehavior.CauldronBehaviorMap powderSnowBehavior = createMap("powder_snow");
	/**
	 * 装满水行为
	 */
	public CauldronBehavior fillWithWater = fillWithWater();
	/**
	 * 装满熔岩行为
	 */
	public CauldronBehavior fillWithLava = fillWithLava();
	/**
	 * 装满细雪行为
	 */
	public CauldronBehavior fillWithPowderSnow = fillWithPowderSnow();
	/**
	 * 清洗旗帜行为
	 */
	public CauldronBehavior cleanBanner = cleanBanner();
	/**
	 * 清洗可染色物品行为
	 */
	public CauldronBehavior cleanDyeableItem = cleanDyeableItem();
	/**
	 * 清洗潜影盒行为
	 */
	public CauldronBehavior cleanShulkerBox = cleanShulkerBox();
	
	/**
	 * 注册行为
	 */
	public void registerBehavior() {
		registerBucketBehavior(emptyBehavior.map());
		emptyBehavior.map().put(Items.POTION, (state, world, pos, player, hand, stack) -> {
			if (PotionUtil.getPotion(stack) != Potions.WATER) {
				return ActionResult.PASS;
			} else {
				if (!world.isClient) {
					if (cantUse(world, pos)) return ActionResult.PASS;
					Item item = stack.getItem();
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(item));
					world.setBlockState(pos, waterCauldronLikeBlock().getDefaultState());
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				
				return ActionResult.success(world.isClient);
			}
		});
		registerBucketBehavior(waterBehavior.map());
		waterBehavior.map().put(Items.BUCKET, (state, world, pos, player, hand, stack) -> emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Items.WATER_BUCKET), (blockState) -> blockState.get(leveledCauldronLikeBlock().level()) == 3, SoundEvents.ITEM_BUCKET_FILL));
		waterBehavior.map().put(Items.GLASS_BOTTLE, (state, world, pos, player, hand, stack) -> {
			if (!world.isClient) {
				if (cantUse(world, pos)) return ActionResult.PASS;
				Item item = stack.getItem();
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(item));
				leveledCauldronLikeBlock().decrementFluidLevel(world, state, pos);
				world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			
			return ActionResult.success(world.isClient);
		});
		waterBehavior.map().put(Items.POTION, (state, world, pos, player, hand, stack) -> {
			if (state.get(leveledCauldronLikeBlock().level()) != 3 && PotionUtil.getPotion(stack) == Potions.WATER) {
				if (!world.isClient) {
					if (cantUse(world, pos)) return ActionResult.PASS;
					player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE)));
					player.incrementStat(Stats.USE_CAULDRON);
					player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
					world.setBlockState(pos, state.cycle(leveledCauldronLikeBlock().level()));
					world.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
					world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
				}
				
				return ActionResult.success(world.isClient);
			} else {
				return ActionResult.PASS;
			}
		});
		waterBehavior.map().put(Items.LEATHER_BOOTS, cleanDyeableItem);
		waterBehavior.map().put(Items.LEATHER_LEGGINGS, cleanDyeableItem);
		waterBehavior.map().put(Items.LEATHER_CHESTPLATE, cleanDyeableItem);
		waterBehavior.map().put(Items.LEATHER_HELMET, cleanDyeableItem);
		waterBehavior.map().put(Items.LEATHER_HORSE_ARMOR, cleanDyeableItem);
		waterBehavior.map().put(Items.WHITE_BANNER, cleanBanner);
		waterBehavior.map().put(Items.GRAY_BANNER, cleanBanner);
		waterBehavior.map().put(Items.BLACK_BANNER, cleanBanner);
		waterBehavior.map().put(Items.BLUE_BANNER, cleanBanner);
		waterBehavior.map().put(Items.BROWN_BANNER, cleanBanner);
		waterBehavior.map().put(Items.CYAN_BANNER, cleanBanner);
		waterBehavior.map().put(Items.GREEN_BANNER, cleanBanner);
		waterBehavior.map().put(Items.LIGHT_BLUE_BANNER, cleanBanner);
		waterBehavior.map().put(Items.LIGHT_GRAY_BANNER, cleanBanner);
		waterBehavior.map().put(Items.LIME_BANNER, cleanBanner);
		waterBehavior.map().put(Items.MAGENTA_BANNER, cleanBanner);
		waterBehavior.map().put(Items.ORANGE_BANNER, cleanBanner);
		waterBehavior.map().put(Items.PINK_BANNER, cleanBanner);
		waterBehavior.map().put(Items.PURPLE_BANNER, cleanBanner);
		waterBehavior.map().put(Items.RED_BANNER, cleanBanner);
		waterBehavior.map().put(Items.YELLOW_BANNER, cleanBanner);
		waterBehavior.map().put(Items.WHITE_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.GRAY_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.BLACK_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.BLUE_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.BROWN_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.CYAN_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.GREEN_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.LIGHT_BLUE_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.LIGHT_GRAY_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.LIME_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.MAGENTA_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.ORANGE_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.PINK_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.PURPLE_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.RED_SHULKER_BOX, cleanShulkerBox);
		waterBehavior.map().put(Items.YELLOW_SHULKER_BOX, cleanShulkerBox);
		lavaBehavior.map().put(Items.BUCKET, (state, world, pos, player, hand, stack) -> emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Items.LAVA_BUCKET), (blockState) -> true, SoundEvents.ITEM_BUCKET_FILL_LAVA));
		registerBucketBehavior(lavaBehavior.map());
		powderSnowBehavior.map().put(Items.BUCKET, (state, world, pos, player, hand, stack) -> emptyCauldron(state, world, pos, player, hand, stack, new ItemStack(Items.POWDER_SNOW_BUCKET), (blockState) -> blockState.get(leveledCauldronLikeBlock().level()) == 3, SoundEvents.ITEM_BUCKET_FILL_POWDER_SNOW));
		registerBucketBehavior(powderSnowBehavior.map());
	}
	
	protected CauldronBehavior.CauldronBehaviorMap createMap(String name) {
		Object2ObjectOpenHashMap<Item, CauldronBehavior> object2ObjectOpenHashMap = new Object2ObjectOpenHashMap<>();
		object2ObjectOpenHashMap.defaultReturnValue((state, world, pos, player, hand, stack) -> ActionResult.PASS);
		CauldronBehavior.CauldronBehaviorMap cauldronBehaviorMap = new CauldronBehavior.CauldronBehaviorMap(name, object2ObjectOpenHashMap);
		behaviorMaps.put(name, cauldronBehaviorMap);
		return cauldronBehaviorMap;
	}
	
	protected ActionResult emptyCauldron(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, ItemStack output, Predicate<BlockState> fullPredicate, SoundEvent soundEvent) {
		if (!fullPredicate.test(state)) {
			return ActionResult.PASS;
		} else {
			if (!world.isClient) {
				if (cantUse(world, pos)) return ActionResult.PASS;
				Item item = stack.getItem();
				player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, output));
				player.incrementStat(Stats.USE_CAULDRON);
				player.incrementStat(Stats.USED.getOrCreateStat(item));
				world.setBlockState(pos, emptyCauldronLikeBlock().getDefaultState());
				world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
				world.emitGameEvent(null, GameEvent.FLUID_PICKUP, pos);
			}
			
			return ActionResult.success(world.isClient);
		}
	}
	
	protected ActionResult fillCauldron(World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, BlockState state, SoundEvent soundEvent) {
		if (!world.isClient) {
			if (cantUse(world, pos)) return ActionResult.PASS;
			Item item = stack.getItem();
			player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, new ItemStack(Items.BUCKET)));
			player.incrementStat(Stats.FILL_CAULDRON);
			player.incrementStat(Stats.USED.getOrCreateStat(item));
			world.setBlockState(pos, state);
			world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
		}
		
		return ActionResult.success(world.isClient);
	}
	
	protected boolean cantUse(World world, BlockPos pos) {
		return !((world.getBlockState(pos.down()).isSideSolidFullSquare(world, pos.down(), Direction.UP)
				|| world.getBlockState(pos.down()).isIn(BlockTags.CAULDRONS)
				|| world.getBlockState(pos.down()).isIn(sameCauldronBlockTag()))
				&& (!world.getBlockState(pos.up()).isSideSolidFullSquare(world, pos.up(), Direction.DOWN)
				&& !world.getBlockState(pos.up()).isIn(sameCauldronBlockTag())));
	}
	
	protected void registerBucketBehavior(Map<Item, CauldronBehavior> behavior) {
		behavior.put(Items.LAVA_BUCKET, fillWithLava);
		behavior.put(Items.WATER_BUCKET, fillWithWater);
		behavior.put(Items.POWDER_SNOW_BUCKET, fillWithPowderSnow);
	}
	
	protected CauldronBehavior cleanDyeableItem() {
		return (state, world, pos, player, hand, stack) -> {
			Item item = stack.getItem();
			if (!(item instanceof DyeableItem dyeableItem)) {
				return ActionResult.PASS;
			} else if (!dyeableItem.hasColor(stack)) {
				return ActionResult.PASS;
			} else {
				if (!world.isClient) {
					if (cantUse(world, pos)) return ActionResult.PASS;
					dyeableItem.removeColor(stack);
					player.incrementStat(Stats.CLEAN_ARMOR);
					leveledCauldronLikeBlock().decrementFluidLevel(world, state, pos);
				}
				
				return ActionResult.success(world.isClient);
			}
		};
	}
	
	protected CauldronBehavior cleanBanner() {
		return (state, world, pos, player, hand, stack) -> {
			if (BannerBlockEntity.getPatternCount(stack) <= 0) {
				return ActionResult.PASS;
			} else {
				if (!world.isClient) {
					if (cantUse(world, pos)) return ActionResult.PASS;
					ItemStack itemStack = stack.copyWithCount(1);
					BannerBlockEntity.loadFromItemStack(itemStack);
					if (!player.getAbilities().creativeMode) {
						stack.decrement(1);
					}
					
					if (stack.isEmpty()) {
						player.setStackInHand(hand, itemStack);
					} else if (player.getInventory().insertStack(itemStack)) {
						player.playerScreenHandler.syncState();
					} else {
						player.dropItem(itemStack, false);
					}
					
					player.incrementStat(Stats.CLEAN_BANNER);
					leveledCauldronLikeBlock().decrementFluidLevel(world, state, pos);
				}
				
				return ActionResult.success(world.isClient);
			}
		};
	}
	
	protected CauldronBehavior cleanShulkerBox() {
		return (state, world, pos, player, hand, stack) -> {
			Block block = Block.getBlockFromItem(stack.getItem());
			if (!(block instanceof ShulkerBoxBlock)) {
				return ActionResult.PASS;
			} else {
				if (!world.isClient) {
					if (cantUse(world, pos)) return ActionResult.PASS;
					ItemStack itemStack = new ItemStack(Blocks.SHULKER_BOX);
					if (stack.hasNbt()) {
						itemStack.setNbt(Objects.requireNonNull(stack.getNbt()).copy());
					}
					
					player.setStackInHand(hand, itemStack);
					player.incrementStat(Stats.CLEAN_SHULKER_BOX);
					leveledCauldronLikeBlock().decrementFluidLevel(world, state, pos);
				}
				
				return ActionResult.success(world.isClient);
			}
		};
	}
	
	protected CauldronBehavior fillWithPowderSnow() {
		return (state, world, pos, player, hand, stack) -> fillCauldron(world, pos, player, hand, stack, powderSnowCauldronLikeBlock().getDefaultState().with(leveledCauldronLikeBlock().level(), 3), SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW);
	}
	
	protected CauldronBehavior fillWithLava() {
		return (state, world, pos, player, hand, stack) -> fillCauldron(world, pos, player, hand, stack, lavaCauldronLikeBlock().getDefaultState(), SoundEvents.ITEM_BUCKET_EMPTY_LAVA);
	}
	
	protected CauldronBehavior fillWithWater() {
		return (state, world, pos, player, hand, stack) -> fillCauldron(world, pos, player, hand, stack, waterCauldronLikeBlock().getDefaultState().with(leveledCauldronLikeBlock().level(), 3), SoundEvents.ITEM_BUCKET_EMPTY);
	}
	
	/**
	 * 水平面坩埚类方块
	 *
	 * @return 水平面坩埚类方块
	 */
	public abstract LeveledCauldronLikeBlock leveledCauldronLikeBlock();
	
	/**
	 * 空坩埚类方块
	 *
	 * @return 空坩埚类方块
	 */
	public abstract AbstractCauldronBlock emptyCauldronLikeBlock();
	
	/**
	 * 含水坩埚类方块
	 *
	 * @return 含水坩埚类方块
	 */
	public abstract AbstractCauldronBlock waterCauldronLikeBlock();
	
	/**
	 * 含熔岩坩埚类方块
	 *
	 * @return 含熔岩坩埚类方块
	 */
	public abstract AbstractCauldronBlock lavaCauldronLikeBlock();
	
	/**
	 * 含细雪坩埚类方块
	 *
	 * @return 含细雪坩埚类方块
	 */
	public abstract AbstractCauldronBlock powderSnowCauldronLikeBlock();
	
	/**
	 * 同种坩埚方块标签，在标签内的方块被认为是同一种坩埚类方块的不同变体
	 *
	 * @return 方块标签
	 */
	public abstract TagKey<Block> sameCauldronBlockTag();
}
