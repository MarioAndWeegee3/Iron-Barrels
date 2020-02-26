package marioandweegee3.ironbarrels2.block;

import marioandweegee3.ironbarrels2.IronBarrels;
import marioandweegee3.ironbarrels2.block.entity.BarrelEntities;
import marioandweegee3.ironbarrels2.block.entity.BigBarrelEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tools.FabricToolTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import ninjaphenix.containerlib.ContainerLibrary;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BigBarrelBlock extends Block implements BlockEntityProvider, InventoryProvider {
    public static final DirectionProperty FACING = BarrelBlock.FACING;
    public static final BooleanProperty OPEN = BarrelBlock.OPEN;

    public static final BigBarrelBlock[] BARRELS = {
        // Iron Barrel
        new BigBarrelBlock(6, 0),
        // Gold Barrel
        new BigBarrelBlock(9, 1),
        // Diamond Barrel
        new BigBarrelBlock(12, 2),
        // Obsidian Barrel
        new BigBarrelBlock(15, 3),
        // Copper Barrel
        new BigBarrelBlock(5, 4),
        // Silver Barrel
        new BigBarrelBlock(8, 5)
    };

    protected final int rows;
    public final int index;

    protected BigBarrelBlock(int rows, int index) {
        super(FabricBlockSettings
            .of(Material.METAL)
            .breakByTool(FabricToolTags.PICKAXES)
            .breakByHand(true)
            .strength(1, 2)
            .build()
        );
        this.rows = rows;
        this.index = index;
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    public int getRows(){
        return rows;
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> stateManager) {
        stateManager.add(FACING, OPEN);
    }

    @NotNull
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return BarrelEntities.barrel(index);
    }

    @Override
    public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(blockEntity instanceof BigBarrelEntity){
            return (BigBarrelEntity) blockEntity;
        } else {
            if (blockEntity != null) {
                blockEntity.markInvalid();
            }
            return null;
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (stack.hasCustomName()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof BigBarrelEntity) {
                ((BigBarrelEntity) be).setName(stack.getName());
            }
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState_1) {
        return true;
    }

    @Override
    public boolean hasBlockEntity() {
        return true;
    }

    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Container.calculateComparatorOutput(world.getBlockEntity(pos));
    }

    public BlockState rotate(BlockState blockState_1, BlockRotation blockRotation_1) {
        return blockState_1.with(FACING, blockRotation_1.rotate(blockState_1.get(FACING)));
    }

    public BlockState mirror(BlockState blockState_1, BlockMirror blockMirror_1) {
        return blockState_1.rotate(blockMirror_1.getRotation(blockState_1.get(FACING)));
    }

    public void onBlockRemoved(BlockState blockState_1, World world_1, BlockPos blockPos_1, BlockState blockState_2,
            boolean boolean_1) {
        if (blockState_1.getBlock() != blockState_2.getBlock()) {
            BlockEntity blockEntity_1 = world_1.getBlockEntity(blockPos_1);
            if (blockEntity_1 instanceof Inventory) {
                ItemScatterer.spawn(world_1, blockPos_1, (Inventory) blockEntity_1);
                world_1.updateHorizontalAdjacent(blockPos_1, this);
            }

            super.onBlockRemoved(blockState_1, world_1, blockPos_1, blockState_2, boolean_1);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player,
            Hand hand, BlockHitResult blockHitResult_1) {
        if (!world.isClient) {
            if (!player.isSneaking()) {
                if (FabricLoader.getInstance().isDevelopmentEnvironment() && player.getStackInHand(hand).getItem() == IronBarrels.debug_item) {
                    LOGGER.info("Comparator Output: " + getComparatorOutput(state, world, pos));
                    return ActionResult.SUCCESS;
                }

                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof BigBarrelEntity) {
                    ContainerLibrary.openContainer(player, pos, ((BigBarrelEntity) be).getName());

                    player.incrementStat(Stats.OPEN_BARREL);
                }
            }

        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockEntity e = world.getBlockEntity(pos);
        if(e instanceof BigBarrelEntity){
            BigBarrelEntity barrel = (BigBarrelEntity) e;
            barrel.update();
            if(barrel.isOpen() && !state.get(OPEN)){
                world.setBlockState(pos, state.with(OPEN, true));
            } else if (!barrel.isOpen() && state.get(OPEN)) {
                world.setBlockState(pos, state.with(OPEN, false));
            }
        } else {
            if (e != null) {
                e.markInvalid();
            }
        }
    }

    

}