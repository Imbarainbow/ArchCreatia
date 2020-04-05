package studio.robotmonkey.archcreatia.ManaGeneration;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class AbstractManaRecipies {
    private ItemStack input;
    private int manaOutput;
    private int cookingTime;


    public AbstractManaRecipies(ItemStack input, int manaOutput, int cookingTime) {
        this.input = input;
        this.manaOutput = manaOutput;
        this.cookingTime = cookingTime;
    }
    public static AbstractManaRecipies defaultRecipe = new AbstractManaRecipies(ItemStack.EMPTY, 0, 0);
    public boolean matches(Inventory inv, World world) {
        return this.input.isItemEqual(inv.getInvStack(0));
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getManaOutput() {
        return manaOutput;
    }

    public Item getInputAsItem() {
        return this.input.getItem();
    }
}
