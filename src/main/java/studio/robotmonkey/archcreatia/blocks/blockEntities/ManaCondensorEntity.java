package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.containers.ManaCondensorContainer;

public class ManaCondensorEntity extends AbstractManaCondensorEntity {
    public ManaCondensorEntity() {
        super(ArchCreatia.MANA_CONDENSOR_ENTITY, 8001, 350, 3000, ArchCreatia.MANA_CRYSTAL);
    }

    protected Text getContainerName() {
        return new TranslatableText("container.archcreatia.mana_condensor");
    }

    @Override
    public Container createContainer(int i, PlayerInventory playerInventory) {
        return new ManaCondensorContainer(i, playerInventory, this, this.propertyDelegate);
    }
}
