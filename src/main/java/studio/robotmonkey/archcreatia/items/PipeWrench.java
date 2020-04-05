package studio.robotmonkey.archcreatia.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import studio.robotmonkey.archcreatia.blocks.AbstractManaPipe;
import studio.robotmonkey.archcreatia.blocks.ManaPipe;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaPipeEntity;

public class PipeWrench extends Item {

    public PipeWrench(Item.Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        BlockState block = context.getWorld().getBlockState(blockPos);
        BlockEntity be = context.getWorld().getBlockEntity(blockPos);
        if(be instanceof ManaPipeEntity) {
            block = block.rotate(BlockRotation.CLOCKWISE_90);
            ((ManaPipe)block.getBlock()).updateConnections(context.getWorld(), blockPos, block);
            context.getWorld().setBlockState( blockPos, block);

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
//        return super.useOnBlock(context);
    }


}
