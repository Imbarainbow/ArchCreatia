package studio.robotmonkey.archcreatia.blocks;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaFurnaceEntity;

public class ManaFurnace extends AbstractManaFurnaceBlock {
    public ManaFurnace(Settings settings) {
        super(settings);
    }

    public BlockEntity createBlockEntity(BlockView view) {
        return new ManaFurnaceEntity();
    }

    @Override
    protected void openContainer(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ManaFurnaceEntity) {
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier("archcreatia", "mana_furnace"), player, buf -> buf.writeBlockPos(pos));
        }

    }

}
