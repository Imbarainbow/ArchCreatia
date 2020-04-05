package studio.robotmonkey.archcreatia.containers;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.container.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.world.World;

public class AbstractManaCondensorContainer extends Container {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    protected final World world;

    protected AbstractManaCondensorContainer(ContainerType<?> containerType, int syncId, PlayerInventory playerInventory) {
        this(containerType, syncId, playerInventory, new BasicInventory(1), new ArrayPropertyDelegate(4));
    }

    protected AbstractManaCondensorContainer(ContainerType<?> containerType, int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(containerType, syncId);
        this.inventory = inventory;
        this.propertyDelegate = propertyDelegate;
        this.world = playerInventory.player.world;

        this.addSlot(new Slot(inventory, 0, 115, 34));
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

        checkContainerSize(inventory, 1);
        checkContainerDataCount(propertyDelegate, 4);
    }

    public void populateRecipeFinder(RecipeFinder recipeFinder) {
        if (this.inventory instanceof RecipeInputProvider) {
            ((RecipeInputProvider)this.inventory).provideRecipeInputs(recipeFinder);
        }

    }

    public void clearCraftingSlots() {
        this.inventory.clear();
    }

//    public void fillInputSlots(boolean bl, Recipe<?> recipe, ServerPlayerEntity serverPlayerEntity) {
//        (new FurnaceInputSlotFiller(this)).fillInputSlots(serverPlayerEntity, recipe, bl);
//    }

    public boolean matches(Recipe<? super Inventory> recipe) {
        return recipe.matches(this.inventory, this.world);
    }

    public int getCraftingResultSlotIndex() {
        return 2;
    }

    public int getCraftingWidth() {
        return 1;
    }

    public int getCraftingHeight() {
        return 1;
    }

    @Environment(EnvType.CLIENT)
    public int getCraftingSlotCount() {
        return 1;
    }

    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUseInv(player);
    }

    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(invSlot);
//        System.out.println(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot == 2) {
                if (!this.insertItem(itemStack2, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
            } else if (invSlot != 1 && invSlot != 0) {
                if (this.isSmeltable(itemStack2)) {
                    if (!this.insertItem(itemStack2, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.isFuel(itemStack2)) {
                    if (!this.insertItem(itemStack2, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot >= 3 && invSlot < 30) {
                    if (!this.insertItem(itemStack2, 30, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (invSlot >= 30 && invSlot < 39 && !this.insertItem(itemStack2, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(itemStack2, 1, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStack2);
        }

        return itemStack;
    }

    protected boolean isSmeltable(ItemStack itemStack) {
//        return this.world.getRecipeManager().getFirstMatch(this.recipeType, new BasicInventory(itemStack), this.world).isPresent();
        return false;
    }


    protected boolean isFuel(ItemStack itemStack) {
        return false;
    }

    @Environment(EnvType.CLIENT)
    public float getCookProgress() {
//        System.out.println(this.propertyDelegate.get(0));
//        System.out.println(this.propertyDelegate.get(1));

        return (float)this.propertyDelegate.get(0) / (float)this.propertyDelegate.get(1);
    }

    @Environment(EnvType.CLIENT)
    public int getManaCount() {
        if(getMaxMana() < this.propertyDelegate.get(3)) {
            return 0;
        } else {
            return this.propertyDelegate.get(3);
        }

    }

    @Environment(EnvType.CLIENT)
    public int getMaxMana() {
        return this.propertyDelegate.get(2) - 1;
    }

    @Environment(EnvType.CLIENT)
    public float getManaPercent() {
        return (((float)getManaCount() / (float)getMaxMana()));
    }

}
