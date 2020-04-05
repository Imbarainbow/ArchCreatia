package studio.robotmonkey.archcreatia.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaBlockEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaFurnaceEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.TestBlockEntity;

public class ManaBlockTest extends AbstractManaFurnaceBlock {
    public ManaBlockTest(Settings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(BlockView view) {
        return new ManaFurnaceEntity();
    }

    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ManaFurnaceEntity) {
            player.openContainer((NameableContainerFactory)blockEntity);
            player.incrementStat(Stats.INTERACT_WITH_FURNACE);
        }

    }

//    @Override
//    public BlockEntity createBlockEntity(BlockView blockView) {
//        return new ManaBlockEntity();
//    }
//
//    @Override
//    @Environment(EnvType.CLIENT)
//    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//
//        if (world.isClient) return ActionResult.PASS;
//        ManaBlockEntity blockEntity = (ManaBlockEntity) world.getBlockEntity(pos);
//        blockEntity.addMana(1);
//        System.out.println(blockEntity.getCurrentMana());
//
//        return ActionResult.SUCCESS;
//    }
//
//    @Override
//    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
//
//    }
}
