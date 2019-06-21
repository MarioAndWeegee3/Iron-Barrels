package marioandweegee3.ironbarrels.blocks;

import java.util.Random;

import marioandweegee3.ironbarrels.blocks.entities.BigBarrelEntity;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.Container;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BigBarrel extends BlockWithEntity implements InventoryProvider {
    private String material;
    private int numRows;

    interface PropertyRetriever<T> {
        public T getFromBarrel(BigBarrelEntity barrelEntity);
    }

    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;

    private static final PropertyRetriever<SidedInventory> INVENTORY_RETRIEVER = new PropertyRetriever<SidedInventory>() {
        @Override
        public SidedInventory getFromBarrel(BigBarrelEntity barrelEntity) {
            return barrelEntity;
        }
    };

    private static final PropertyRetriever<Component> NAME_RETRIEVER = new PropertyRetriever<Component>() {

        @Override
        public Component getFromBarrel(BigBarrelEntity barrelEntity) {
            return barrelEntity.getDisplayName();
        }

    };

    public BigBarrel(Settings settings, String material, int numRows) {
        super(settings);
        this.material = material;
        this.numRows = numRows;
        setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState blockState_1) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> stateFactory) {
        stateFactory.add(FACING, OPEN);
    }

    @Override
    public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
        BlockEntity entity = world.getBlockEntity(pos);
        if(entity instanceof BigBarrelEntity){
            ((BigBarrelEntity)entity).tick();
        }
    }

    @Override
    public boolean hasComparatorOutput(BlockState blockState_1) {
        return true;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return Container.calculateComparatorOutput(getInventory(state, world, pos));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public SidedInventory getInventory(BlockState state, IWorld world, BlockPos pos) {
        return retrieve(state, world, pos, INVENTORY_RETRIEVER);
    }

    private Stat<Identifier> getOpenStat() {
        return Stats.CUSTOM.getOrCreateStat(Stats.OPEN_BARREL);
    }

    public static SidedInventory getInventoryStatic(BlockState state, IWorld world, BlockPos pos) {
        return retrieve(state, world, pos, INVENTORY_RETRIEVER);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction d1 = context.getPlayerLookDirection().getOpposite();
        return getDefaultState().with(FACING, d1).with(OPEN, false);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
        if (stack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BigBarrelEntity) {
                ((BigBarrelEntity) blockEntity).setCustomName(stack.getCustomName());
            }
        }
    }

    @Override
    public void onBlockRemoved(BlockState state1, World world, BlockPos pos, BlockState state2, boolean flag) {
        if (state1.getBlock() != state2.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Inventory) {
                ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
                world.updateHorizontalAdjacent(pos, this);
            }
            super.onBlockRemoved(state1, world, pos, state2, flag);
        }
    }

    @Override
    public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hitResult) {
        if (world.isClient)
            return true;
        openContainer(state, world, pos, player, hand, hitResult);
        player.incrementStat(getOpenStat());
        return true;
    }

    private void openContainer(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
            BlockHitResult hitResult) {
        Component containerName = retrieve(state, world, pos, NAME_RETRIEVER);
        if (containerName == null)
            return;
        ContainerProviderRegistry.INSTANCE.openContainer(new Identifier("ironbarrels", "scrollbarrel"), player,
                (packetByteBuf -> {
                    packetByteBuf.writeBlockPos(pos);
                    packetByteBuf.writeTextComponent(containerName);
                }));

    }

    private static <T> T retrieve(BlockState state, IWorld world, BlockPos pos, PropertyRetriever<T> retriever) {
        BlockEntity blockEntity1 = world.getBlockEntity(pos);
        if (!(blockEntity1 instanceof BigBarrelEntity))
            return null;
        BigBarrelEntity barrelEntity = (BigBarrelEntity) blockEntity1;
        return retriever.getFromBarrel(barrelEntity);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView arg0) {
        return new BigBarrelEntity(material, numRows);
    }
}