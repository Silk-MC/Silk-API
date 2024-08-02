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

package pers.saikel0rado1iu.silk.api.magiccube;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TntBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.Explosion;
import pers.saikel0rado1iu.silk.api.spore.EntityUtil;
import pers.saikel0rado1iu.silk.api.spore.TntLikeEntity;

import java.util.Optional;

/**
 * <h2 style="color:FFC800">TNT 类方块</h2>
 * 用于创建类似于 TNT 或是扩展于 TNT 的方块
 *
 * @author <a href="https://github.com/Saikel-Orado-Liu"><img alt="author" src="https://avatars.githubusercontent.com/u/88531138?s=64&v=4"></a>
 * @since 1.1.0
 */
public abstract class TntLikeBlock extends Block {
	protected TntLikeBlock(Settings settings) {
		super(settings);
		setDefaultState(getDefaultState().with(TntBlock.UNSTABLE, false));
	}
	
	/**
	 * 引爆 TNT<br>
	 * 此方法用于生成一个 {@link TntLikeEntity} 实体并触发引燃事件
	 *
	 * @param world 游戏世界
	 * @param pos   块坐标
	 */
	public void primeTnt(World world, BlockPos pos) {
		primeTnt(world, pos, Optional.empty());
	}
	
	/**
	 * 引爆 TNT<br>
	 * 此方法用于生成一个 {@link TntLikeEntity} 实体并触发引燃事件
	 *
	 * @param world   游戏世界
	 * @param pos     块坐标
	 * @param igniter 引爆者
	 */
	protected void primeTnt(World world, BlockPos pos, Optional<LivingEntity> igniter) {
		if (world.isClient) return;
		TntLikeEntity tnt = createEntity(world, pos.getX() + EntityUtil.POS_SHIFTING, pos.getY(), pos.getZ() + EntityUtil.POS_SHIFTING, igniter);
		world.spawnEntity(tnt);
		world.playSound(null, tnt.getX(), tnt.getY(), tnt.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1, 1);
		world.emitGameEvent(igniter.orElse(null), GameEvent.PRIME_FUSE, pos);
	}
	
	/**
	 * 此方法用于处理是否能接受红石引爆
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (oldState.isOf(state.getBlock())) return;
		if (!world.isReceivingRedstonePower(pos)) return;
		primeTnt(world, pos);
		world.removeBlock(pos, false);
	}
	
	/**
	 * 此方法用于处理是否能接受红石引爆
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if (!world.isReceivingRedstonePower(pos)) return;
		primeTnt(world, pos);
		world.removeBlock(pos, false);
	}
	
	/**
	 * {@link TntLikeBlock#onUse(BlockState, World, BlockPos, PlayerEntity, Hand, BlockHitResult)} 的父类实现<br>
	 * 用于在需要覆盖 {@code onUse()} 时调用
	 */
	@SuppressWarnings("deprecation")
	protected ActionResult onUseSuper(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	/**
	 * 此方法用于处理是否能被玩家直接引燃
	 */
	@Override
	@SuppressWarnings("deprecation")
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (!(itemStack.isOf(Items.FLINT_AND_STEEL) || itemStack.isOf(Items.FIRE_CHARGE))) return onUseSuper(state, world, pos, player, hand, hit);
		primeTnt(world, pos, Optional.of(player));
		world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL_AND_REDRAW);
		if (!player.isCreative()) {
			if (!itemStack.isOf(Items.FLINT_AND_STEEL)) itemStack.decrement(1);
			else itemStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
		}
		player.incrementStat(Stats.USED.getOrCreateStat(itemStack.getItem()));
		return ActionResult.success(world.isClient);
	}
	
	/**
	 * 此方法用于处理是否能被火箭引燃
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
		if (world.isClient) return;
		BlockPos blockPos = hit.getBlockPos();
		Entity entity = projectile.getOwner();
		if (!projectile.isOnFire() || !projectile.canModifyAt(world, blockPos)) return;
		primeTnt(world, blockPos, entity instanceof LivingEntity ? Optional.of((LivingEntity) entity) : Optional.empty());
		world.removeBlock(blockPos, false);
	}
	
	/**
	 * {@link TntLikeBlock#onBreak(World, BlockPos, BlockState, PlayerEntity)} 的父类实现<br>
	 * 用于在需要覆盖 {@code onBreak()} 时调用
	 */
	protected BlockState onBreakSuper(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		return super.onBreak(world, pos, state, player);
	}
	
	/**
	 * 此方法用于处理在破坏方块时方块不稳定是否引爆
	 */
	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient() && !player.isCreative() && state.get(TntBlock.UNSTABLE)) primeTnt(world, pos);
		return onBreakSuper(world, pos, state, player);
	}
	
	/**
	 * 此方法用于处理在被爆炸摧毁时即刻引爆
	 */
	@Override
	public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
		if (world.isClient) return;
		TntLikeEntity tnt = createEntity(world, pos.getX() + EntityUtil.POS_SHIFTING, pos.getY(), pos.getZ() + EntityUtil.POS_SHIFTING, Optional.ofNullable(explosion.getCausingEntity()));
		int fuse = tnt.getFuse();
		tnt.setFuse((short) (world.random.nextInt(fuse / 4) + fuse / 8));
		world.spawnEntity(tnt);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(TntBlock.UNSTABLE);
	}
	
	@Override
	public boolean shouldDropItemsOnExplosion(Explosion explosion) {
		return false;
	}
	
	@Override
	protected abstract MapCodec<? extends TntLikeBlock> getCodec();
	
	protected abstract TntLikeEntity createEntity(World world, double x, double y, double z, Optional<LivingEntity> igniter);
}
