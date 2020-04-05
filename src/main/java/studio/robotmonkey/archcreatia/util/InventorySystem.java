package studio.robotmonkey.archcreatia.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public interface InventorySystem extends Inventory {
    DefaultedList<ItemStack> getItems();

//    static InventorySystem of(DefaultedList<ItemStack> items) {
//        return () -> items;
//    }
//
//    static InventorySystem ofSize(int size) {
//        return of(DefaultedList.ofSize(size, ItemStack.EMPTY));
//    }

    @Override
    default int getInvSize() {
        return getItems().size();
    }

    @Override
    default boolean isInvEmpty() {
        for (int i = 0; i < getInvSize(); i++) {
            ItemStack stack = getInvStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    default ItemStack getInvStack(int slot) {
        return getItems().get(slot);
    }

    @Override
    default ItemStack takeInvStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    @Override
    default ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(getItems(), slot);
    }

    @Override
    default void setInvStack(int slot, ItemStack stack) {
        getItems().set(slot, stack);
        if (stack.getCount() > getInvMaxStackAmount()) {
            stack.setCount(getInvMaxStackAmount());
        }
    }

    @Override
    default void clear() {
        getItems().clear();
    }

//    @Override
//    default void markDirty() {
//        // Override if you want behavior.
//    }
    @Override
    default boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }


}
