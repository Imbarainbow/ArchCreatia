package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.containers.ManaCondensorContainer;
import studio.robotmonkey.archcreatia.containers.ManaTankContainer;

public class WeakManaCondensorEntity extends AbstractManaCondensorEntity {
    public WeakManaCondensorEntity() {
        super(ArchCreatia.WEAK_MANA_CONDENSOR_ENTITY, 5001, 200, 1500, ArchCreatia.IMPURE_ICHOR);
    }

    protected Text getContainerName() {
        return new TranslatableText("container.archcreatia.weak_mana_condensor");
    }

    @Override
    public Container createContainer(int i, PlayerInventory playerInventory) {
        return new ManaCondensorContainer(i, playerInventory, this, this.propertyDelegate);
    }
}
