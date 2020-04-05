package studio.robotmonkey.archcreatia.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Tickable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import studio.robotmonkey.archcreatia.blocks.blockEntities.TestBlockEntity;
import studio.robotmonkey.archcreatia.util.InventorySystem;

import java.util.Random;

public class TestBlock extends Block implements BlockEntityProvider {

    public static final BooleanProperty CLICKED = BooleanProperty.of("clicked");
    public TestBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(CLICKED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(CLICKED);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (world.isClient) return ActionResult.PASS;
        TestBlockEntity blockEntity = (TestBlockEntity) world.getBlockEntity(pos);


        if (!player.getStackInHand(hand).isEmpty()) {
            // Check what is the first open slot and put an item from the player's hand there
            if (blockEntity.getInvStack(0).isEmpty()) {
                // Put the stack the player is holding into the inventory
                blockEntity.setInvStack(0, player.getStackInHand(hand).copy());
                // Remove the stack from the player's hand
                player.getStackInHand(hand).setCount(0);
//                world.setBlockState(pos, state.with(CLICKED, true));
            } else if (blockEntity.getInvStack(1).isEmpty()) {
                blockEntity.setInvStack(1, player.getStackInHand(hand).copy());
                player.getStackInHand(hand).setCount(0);

            } else {
                // If the inventory is full we'll print it's contents
//                System.out.println("The first slot holds "
//                        + blockEntity.getInvStack(0) + " and the second slot holds " + blockEntity.getInvStack(1));
            }
        } else {
            // If the player is not holding anything we'll get give him the items in the block entity one by one

            // Find the first slot that has an item and give it to the player
            if (!blockEntity.getInvStack(1).isEmpty()) {
                // Give the player the stack in the inventory
                player.inventory.offerOrDrop(world, blockEntity.getInvStack(1));
                // Remove the stack from the inventory
                blockEntity.removeInvStack(1);

            } else if (!blockEntity.getInvStack(0).isEmpty()) {
                player.inventory.offerOrDrop(world, blockEntity.getInvStack(0));
                blockEntity.removeInvStack(0);

            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockView) {
        return new TestBlockEntity();
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
//        if (world.isClient) return;
        TestBlockEntity blockEntity = (TestBlockEntity) world.getBlockEntity(pos);

//        System.out.println(blockEntity.getNumber());
//        System.out.println(blockEntity.getInvStack(0).getCount());
        if (!blockEntity.isInvEmpty()) {
            world.setBlockState(pos, state.with(CLICKED, false));
//            System.out.println("Its not empty!");
        } else {
            world.setBlockState(pos, state.with(CLICKED, true));
//            System.out.println("Its empty!");
        }
//        System.out.println("The first slot holds "
//                + blockEntity.getItem() + " and the second slot holds " + blockEntity.getInvStack(1));
    }


}
