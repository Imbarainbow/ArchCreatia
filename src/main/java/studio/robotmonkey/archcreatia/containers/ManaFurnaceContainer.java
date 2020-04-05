package studio.robotmonkey.archcreatia.containers;

import net.minecraft.container.ContainerType;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.RecipeType;
import studio.robotmonkey.archcreatia.ArchCreatia;

public class ManaFurnaceContainer extends AbstractManaFurnaceContainer {

    public ManaFurnaceContainer(int syncId, PlayerInventory playerInventory) {
        super(null, RecipeType.SMELTING, syncId, playerInventory);
    }

    public ManaFurnaceContainer(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(null, RecipeType.SMELTING, syncId, playerInventory, inventory, propertyDelegate);
    }
}
