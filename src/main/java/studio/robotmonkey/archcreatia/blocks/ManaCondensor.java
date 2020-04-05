package studio.robotmonkey.archcreatia.blocks;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaCondensorEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.WeakManaCondensorEntity;

public class ManaCondensor extends AbstractManaCondensor {
    public ManaCondensor(Settings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(BlockView view) {
        return new ManaCondensorEntity();
    }

    @Override
    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ManaCondensorEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier("archcreatia", "mana_condensor"), player, buf -> buf.writeBlockPos(pos));
        }
    }
}
