package studio.robotmonkey.archcreatia.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.*;
import studio.robotmonkey.archcreatia.util.DirectionHelper;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.HashMap;

public abstract class AbstractManaPipe extends BlockWithEntity {

    public static final DirectionProperty FACING;

    protected AbstractManaPipe(Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

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
        ((AbstractManaPipeEntity)blockEntity).fixInputs();
        //TODO fix output attaching for blocks located next to it. Only set output if Direction if proper.
        BlockPos right = DirectionHelper.getRightPos(state.get(FACING), pos);
        if(world.getBlockEntity(right) instanceof HasMana) {
//            System.out.println("On right");
//            System.out.println("Found Something with mana");
            if (blockEntity instanceof ManaPipeEntity) {
                ((ManaPipeEntity) blockEntity).setOutput((HasMana) world.getBlockEntity(right));
                ((HasMana) world.getBlockEntity(right)).addInput((HasMana) blockEntity);
            }
        }
        HashMap<String, BlockPos> inputs = DirectionHelper.getInputSides(state.get(FACING), pos);
        if(world.getBlockEntity(inputs.get("up")) instanceof HasMana) {
//            System.out.println("Above");
            if(world.getBlockState(inputs.get("up")).get(FACING) == BlockRotation.CLOCKWISE_90.rotate(state.get(FACING))) {
//                System.out.println(world.getBlockState(inputs.get("up")).get(FACING));
//                System.out.println(BlockRotation.CLOCKWISE_90.rotate(state.get(FACING)));

                ((HasMana) world.getBlockEntity(inputs.get("up"))).setOutput((HasMana) blockEntity);
                ((AbstractManaPipeEntity)blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("up")));
            }
        }
        if(world.getBlockEntity(inputs.get("left")) instanceof HasMana) {
//            System.out.println("On left");
            if(world.getBlockState(inputs.get("left")).get(FACING) == state.get(FACING)) {
                ((HasMana) world.getBlockEntity(inputs.get("left"))).setOutput((HasMana) blockEntity);
                ((AbstractManaPipeEntity)blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("left")));
            }
        }
        if(world.getBlockEntity(inputs.get("down")) instanceof HasMana) {
//            System.out.println("down");
            if(world.getBlockState(inputs.get("down")).get(FACING) == BlockRotation.COUNTERCLOCKWISE_90.rotate(state.get(FACING))) {
                ((HasMana) world.getBlockEntity(inputs.get("down"))).setOutput((HasMana) blockEntity);
                ((AbstractManaPipeEntity)blockEntity).addInput((HasMana) world.getBlockEntity(inputs.get("down")));
            }
        }
//
//        if(state.get(FACING).asString().equals("north")) {
//            BlockPos right = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            BlockPos left = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            right = right.offset(Direction.WEST);
//            left = left.offset(Direction.EAST);
//
//            if(world.getBlockEntity(right) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if(blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).setOutput((HasMana)world.getBlockEntity(right));
//                    ((HasMana) world.getBlockEntity(right)).addInput((HasMana)blockEntity);
//                }
//            }
//            if(world.getBlockEntity(left) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if (blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).addInput((HasMana) world.getBlockEntity(left));
//                    if(world.getBlockState(left).get(FACING) == state.get(FACING)) {
//                        ((HasMana) world.getBlockEntity(left)).setOutput((HasMana) blockEntity);
//                    }
//                }
//            }
//        } else if(state.get(FACING).asString().equals("south")) {
//            BlockPos right = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            BlockPos left = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            right = right.offset(Direction.EAST);
//            left = left.offset(Direction.WEST);
//
//            if(world.getBlockEntity(right) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if(blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).setOutput((HasMana)world.getBlockEntity(right));
//                    ((HasMana) world.getBlockEntity(right)).addInput((HasMana)blockEntity);
//                }
//            }
//            if(world.getBlockEntity(left) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if (blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).addInput((HasMana) world.getBlockEntity(left));
//                    if(world.getBlockState(left).get(FACING) == state.get(FACING)) {
//                        ((HasMana) world.getBlockEntity(left)).setOutput((HasMana) blockEntity);
//                    }
//                }
//            }
//        } else if(state.get(FACING).asString().equals("east")) {
//            BlockPos right = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            BlockPos left = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            right = right.offset(Direction.NORTH);
//            left = left.offset(Direction.SOUTH);
//
//            if(world.getBlockEntity(right) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if(blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).setOutput((HasMana)world.getBlockEntity(right));
//                    ((HasMana) world.getBlockEntity(right)).addInput((HasMana)blockEntity);
//                }
//            }
//            if(world.getBlockEntity(left) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if (blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).addInput((HasMana) world.getBlockEntity(left));
//                    if(world.getBlockState(left).get(FACING) == state.get(FACING)) {
//                        ((HasMana) world.getBlockEntity(left)).setOutput((HasMana) blockEntity);
//                    }
//                }
//            }
//        } else {
//            BlockPos right = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            BlockPos left = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
//            right = right.offset(Direction.SOUTH);
//            left = left.offset(Direction.NORTH);
//
//            if(world.getBlockEntity(right) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if(blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).setOutput((HasMana)world.getBlockEntity(right));
//                    ((HasMana) world.getBlockEntity(right)).addInput((HasMana)blockEntity);
//                }
//            }
//            if(world.getBlockEntity(left) instanceof HasMana) {
//                System.out.println("Found Something with mana");
//                if (blockEntity instanceof ManaPipeEntity) {
//                    ((ManaPipeEntity) blockEntity).addInput((HasMana) world.getBlockEntity(left));
//                    if(world.getBlockState(left).get(FACING) == state.get(FACING)) {
//                        ((HasMana) world.getBlockEntity(left)).setOutput((HasMana) blockEntity);
//                    }
//                }
//            }
//        }
    }

    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof AbstractManaPipeEntity) {
//                ItemScatterer.spawn(world, pos, (AbstractManaTankEntity)blockEntity);
                ((AbstractManaPipeEntity)blockEntity).fixInputs();
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


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ctx) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case NORTH:
//                System.out.println("NORTH");
                return VoxelShapes.cuboid(0, 0.25, 0.25, 1.0f, 0.75, 0.75);
            case SOUTH:
                return VoxelShapes.cuboid(0, 0.25, 0.25, 1.0f, 0.75, 0.75);
            case EAST:
                return VoxelShapes.cuboid(0.25, 0.25f, 0, 0.75f, 0.75f, 1f);
            case WEST:
                return VoxelShapes.cuboid(0.25, 0.25f, 0, 0.75f, 0.75f, 1f);
            default:
                return VoxelShapes.fullCube();
        }
    }

}
