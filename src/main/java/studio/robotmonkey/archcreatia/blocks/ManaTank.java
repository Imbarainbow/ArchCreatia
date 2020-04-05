package studio.robotmonkey.archcreatia.blocks;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaFurnaceEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaTankEntity;

public class ManaTank extends AbstractManaTankBlock {
    public ManaTank(Settings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(BlockView view) {
        return new ManaTankEntity();
    }

    @Override
    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ManaTankEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier("archcreatia", "mana_tank"), player, buf -> buf.writeBlockPos(pos));
        }
    }
}
