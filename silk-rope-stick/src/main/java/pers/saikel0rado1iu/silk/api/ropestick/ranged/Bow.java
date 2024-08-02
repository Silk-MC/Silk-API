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

package pers.saikel0rado1iu.silk.api.ropestick.ranged;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import pers.saikel0rado1iu.silk.api.base.common.util.MathUtil;

import java.util.function.Predicate;

/**
 * <h2 style="color:FFC800">弓</h2>
 * 加强方法的弓物品，辅助弓的创建的数据直观和清晰
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 0.1.0
 */
public abstract class Bow extends BowItem implements BowExpansion {
	/**
	 * @param settings 物品设置
	 */
	public Bow(Settings settings) {
		super(settings);
	}
	
	/**
	 * 设置自定义的速度、伤害与弹药偏移<br>
	 * 重实现 {@link BowItem#onStoppedUsing(ItemStack, World, LivingEntity, int)}
	 */
	@Override
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		// 检查用户使用者是否为玩家
		if (!(user instanceof PlayerEntity player)) return;
		
		// 检查是否在创造模式或者拥有“无限”附魔
		boolean inCreateOrInfinity = player.getAbilities().creativeMode || EnchantmentHelper.getLevel(Enchantments.INFINITY, stack) > 0;
		// 获取弹丸
		ItemStack useProjectile = getProjectileType(user, stack);
		// 检查玩家是否有箭，如果没有箭但在创造模式或者拥有“无限”附魔则使用默认弹丸
		if (!useProjectile.isEmpty() || inCreateOrInfinity) if (useProjectile.isEmpty()) useProjectile = new ItemStack(defaultProjectile());
		if (useProjectile.isEmpty() && !inCreateOrInfinity) return;
		
		// 获取弓已使用游戏刻
		int usedTicks = getMaxUseTime(stack) - remainingUseTicks;
		// 获取弓拉弓进度
		float pullProgress = getUsingProgress(usedTicks, stack);
		// 如果拉弓进度小于 0.1
		if (pullProgress < 0.1) return;
		
		// 如果在创造模式或者拥有“无限”附魔以及弹丸是默认弹丸
		boolean andDefaultProjectile = inCreateOrInfinity && useProjectile.isOf(defaultProjectile());
		if (!world.isClient) {
			// 创建箭实体
			ArrowItem arrowItem = (ArrowItem) (useProjectile.getItem() instanceof ArrowItem ? useProjectile.getItem() : Items.ARROW);
			PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, useProjectile, player);
			// 设置速度速度
			persistentProjectileEntity.setVelocity(player, player.getPitch(), player.getYaw(), 0, pullProgress * maxProjectileSpeed(), firingError());
			// 设置基础伤害增加
			persistentProjectileEntity.setDamage(adjustedProjectileDamage());
			if (MathUtil.compareFloat(pullProgress, 1) == 0) persistentProjectileEntity.setCritical(true);
			
			// 设置“力量”效果
			int powerLevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
			if (powerLevel > 0)
				persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double) powerLevel * 0.5 + 0.5);
			
			// 设置“冲击”效果
			int punchLevel = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
			if (punchLevel > 0) persistentProjectileEntity.setPunch(punchLevel);
			
			// 设置“火矢”效果
			if (EnchantmentHelper.getLevel(Enchantments.FLAME, stack) > 0) persistentProjectileEntity.setOnFireFor(100);
			
			// 设置工具损伤
			stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
			if (andDefaultProjectile || player.getAbilities().creativeMode && (useProjectile.isOf(Items.SPECTRAL_ARROW) || useProjectile.isOf(Items.TIPPED_ARROW))) {
				persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
			}
			
			// 生成弹丸实体
			world.spawnEntity(persistentProjectileEntity);
			if (player instanceof ServerPlayerEntity serverPlayer) triggerCriteria(serverPlayer, stack, persistentProjectileEntity);
		}
		
		// 播放音效
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1, 1 / (world.getRandom().nextFloat() * 0.4F + 1.2F) + pullProgress * 0.5F);
		if (!andDefaultProjectile && !player.getAbilities().creativeMode) {
			useProjectile.decrement(1);
			if (useProjectile.isEmpty()) player.getInventory().removeOne(useProjectile);
		}
		
		player.incrementStat(Stats.USED.getOrCreateStat(this));
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		setProjectileIndex(stack, getProjectileType(user, stack));
		return super.use(world, user, hand);
	}
	
	@Override
	public Predicate<ItemStack> getProjectiles() {
		return stack -> launchableProjectiles().stream().anyMatch(stack::isOf);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return maxUseTicks();
	}
	
	@Override
	public boolean canStretchHud() {
		return false;
	}
}
