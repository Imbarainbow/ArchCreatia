package studio.robotmonkey.archcreatia.containers;

import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

public class ManaCondensorContainer extends AbstractManaCondensorContainer {

    public ManaCondensorContainer(int syncId, PlayerInventory playerInventory) {
        super(null, syncId, playerInventory);
    }

    public ManaCondensorContainer(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(null, syncId, playerInventory, inventory, propertyDelegate);
    }
}
