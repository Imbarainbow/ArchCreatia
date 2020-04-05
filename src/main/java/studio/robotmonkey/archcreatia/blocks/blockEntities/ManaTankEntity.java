package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.containers.ManaFurnaceContainer;
import studio.robotmonkey.archcreatia.containers.ManaTankContainer;

public class ManaTankEntity extends AbstractManaTankEntity {
    public ManaTankEntity() {
        super(ArchCreatia.MANA_TANK_ENTITY, 5001);
    }

    protected Text getContainerName() {
        return new TranslatableText("container.archcreatia.mana_tank");
    }

    @Override
    public Container createContainer(int i, PlayerInventory playerInventory) {
        return new ManaTankContainer(i, playerInventory, this, this.propertyDelegate);
    }
}
