package studio.robotmonkey.archcreatia.blocks;

import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.*;
import studio.robotmonkey.archcreatia.util.DirectionHelper;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.HashMap;

public abstract class AbstractManaTankBlock extends BlockWithEntity {
    public static final DirectionProperty FACING;

    protected AbstractManaTankBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.PASS;
        } else {
            this.openContainer(world, pos, player);
            return ActionResult.SUCCESS;
        }
    }

    protected abstract void openContainer(World world, BlockPos pos, PlayerEntity player);

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if(!world.isClient) {
            updateConnections(world, pos, state);
        }
        if (itemStack.hasCustomName()) {
            //TODO update this to mana furnace
            if (blockEntity instanceof AbstractManaTankEntity) {
                ((AbstractManaTankEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }

    }
    public void updateConnections(World world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ((AbstractManaTankEntity) blockEntity).fixInputs();
        //TODO fix output attaching for blocks located next to it. Only set output if Direction if proper.
        BlockPos right = DirectionHelper.getRightPos(state.get(FACING), pos);
        if (world.getBlockEntity(right) instanceof HasMana) {
//            System.out.println("On right");
//            System.out.println("Found Something with mana");
            if (blockEntity instanceof AbstractManaTankEntity) {
                ((AbstractManaTankEntity) blockEntity).setOutput((HasMana) world.getBlockEntity(right));
                ((HasMana) world.getBlockEntity(right)).addInput((HasMana) blockEntity);
            }
        }
        HashMap<String, BlockPos> inputs = DirectionHelper.getInputSides(state.get(FACING), pos);
        if (world.getBlockEntity(inputs.get("up")) instanceof HasMana) {
//            System.out.println("Above");
            if (world.getBlockState(inputs.get("up")).get(FACING) == BlockRotation.CLOCKWISE_90.rotate(state.get(FACING))) {
//                System.out.println(world.getBlockState(inputs.get("up")).get(FACING));
//                System.out.println(BlockRotation.CLOCKWISE_90.rotate(state.get(FACING)));

                ((HasMana) world.getBlockEntity(inputs.get("up"))).setOutput((HasMana) blockEntity);
                ((AbstractManaTankEntity) blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("up")));
            }
        }
        if (world.getBlockEntity(inputs.get("left")) instanceof HasMana) {
//            System.out.println("On left");
            if (world.getBlockState(inputs.get("left")).get(FACING) == state.get(FACING)) {
                ((HasMana) world.getBlockEntity(inputs.get("left"))).setOutput((HasMana) blockEntity);
                ((AbstractManaTankEntity) blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("left")));
            }
        }
        if (world.getBlockEntity(inputs.get("down")) instanceof HasMana) {
//            System.out.println("down");
            if (world.getBlockState(inputs.get("down")).get(FACING) == BlockRotation.COUNTERCLOCKWISE_90.rotate(state.get(FACING))) {
                ((HasMana) world.getBlockEntity(inputs.get("down"))).setOutput((HasMana) blockEntity);
                ((AbstractManaTankEntity) blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("down")));
            }
        }
    }

    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AbstractManaTankEntity) {
                ItemScatterer.spawn(world, pos, (AbstractManaTankEntity)blockEntity);
                ((AbstractManaTankEntity)blockEntity).fixInputs();
                world.updateHorizontalAdjacent(pos, this);
            }
            super.onBlockRemoved(state, world, pos, newState, moved);
        }
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate((Direction)state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
    }
}
