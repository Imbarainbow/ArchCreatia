package studio.robotmonkey.archcreatia.containers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;

public class AbstractManaTankContainer extends Container {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;

    protected AbstractManaTankContainer(ContainerType<?> containerType, int syncId, PlayerInventory playerInventory) {
        this(containerType, syncId, playerInventory, new BasicInventory(0), new ArrayPropertyDelegate(2));
    }

    protected AbstractManaTankContainer(ContainerType<?> containerType, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(containerType, syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;
//        this.addSlot(new Slot(inventory, 0, 40, 17));
//        this.addSlot(new Slot(inventory, 1, 40, 53));
//        this.addSlot(new FurnaceFuelSlot(this, inventory, 1, 56, 53));
//        this.addSlot(new FurnaceOutputSlot(playerInventory.player, inventory, 2, 116, 35));

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addProperties(propertyDelegate);

        checkContainerSize(inventory, 0);
        checkContainerDataCount(propertyDelegate, 2);
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUseInv(player);
    }


    @Environment(EnvType.CLIENT)
    public int getManaCount() {
        if(getMaxMana() < this.propertyDelegate.get(1)) {
            return 0;
        } else {
            return this.propertyDelegate.get(1);
        }

    }

    @Environment(EnvType.CLIENT)
    public int getMaxMana() {
        return this.propertyDelegate.get(0) - 1;
    }

    @Environment(EnvType.CLIENT)
    public float getManaPercent() {
        return (((float)getManaCount() / (float)getMaxMana()));
    }



}