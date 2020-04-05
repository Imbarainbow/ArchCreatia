package studio.robotmonkey.archcreatia.blocks;


import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaPipeEntity;

public class ManaPipe extends AbstractManaPipe {

    public ManaPipe(Settings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(BlockView view) {
        return new ManaPipeEntity();
    }

}
