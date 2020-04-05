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
import studio.robotmonkey.archcreatia.blocks.blockEntities.AbstractManaFurnaceEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.AbstractManaPipeEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaFurnaceEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaPipeEntity;
import studio.robotmonkey.archcreatia.util.DirectionHelper;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.HashMap;

public abstract class AbstractManaFurnaceBlock extends BlockWithEntity {
    public static final DirectionProperty FACING;
    public static final BooleanProperty LIT;

    protected AbstractManaFurnaceBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)).with(LIT, false));
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
            if (blockEntity instanceof AbstractFurnaceBlockEntity) {
                ((AbstractFurnaceBlockEntity)blockEntity).setCustomName(itemStack.getName());
            }
        }

    }

    public void updateConnections(World world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        ((ManaFurnaceEntity) blockEntity).fixInputs();
        //TODO fix output attaching for blocks located next to it. Only set output if Direction if proper.
        BlockPos right = DirectionHelper.getRightPos(state.get(FACING), pos);
        if (world.getBlockEntity(right) instanceof HasMana) {
//            System.out.println("On right");
//            System.out.println("Found Something with mana");
            if (blockEntity instanceof AbstractManaFurnaceEntity) {
                ((AbstractManaFurnaceEntity) blockEntity).setOutput((HasMana) world.getBlockEntity(right));
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
                ((AbstractManaFurnaceEntity) blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("up")));
            }
        }
        if (world.getBlockEntity(inputs.get("left")) instanceof HasMana) {
//            System.out.println("On left");
            if (world.getBlockState(inputs.get("left")).get(FACING) == state.get(FACING)) {
                ((HasMana) world.getBlockEntity(inputs.get("left"))).setOutput((HasMana) blockEntity);
                ((AbstractManaFurnaceEntity) blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("left")));
            }
        }
        if (world.getBlockEntity(inputs.get("down")) instanceof HasMana) {
//            System.out.println("down");
            if (world.getBlockState(inputs.get("down")).get(FACING) == BlockRotation.COUNTERCLOCKWISE_90.rotate(state.get(FACING))) {
                ((HasMana) world.getBlockEntity(inputs.get("down"))).setOutput((HasMana) blockEntity);
                ((AbstractManaFurnaceEntity) blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("down")));
            }
        }
    }


    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AbstractManaFurnaceEntity) {
                //Clears each machine attached to this one.
                ((AbstractManaFurnaceEntity)blockEntity).fixInputs();
                ItemScatterer.spawn(world, pos, (AbstractManaFurnaceEntity)blockEntity);
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
        builder.add(new Property[]{FACING, LIT});
    }

    static {
        FACING = HorizontalFacingBlock.FACING;
        LIT = RedstoneTorchBlock.LIT;
    }
}
