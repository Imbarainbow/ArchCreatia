package studio.robotmonkey.archcreatia.containers;

import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;

public class ManaTankContainer extends AbstractManaTankContainer {

    public ManaTankContainer(int syncId, PlayerInventory playerInventory) {
        super(null, syncId, playerInventory);
    }

    public ManaTankContainer(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(null, syncId, playerInventory, inventory, propertyDelegate);
    }
}
