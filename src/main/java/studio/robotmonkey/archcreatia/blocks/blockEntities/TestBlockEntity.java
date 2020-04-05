package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.blocks.TestBlock;
import studio.robotmonkey.archcreatia.util.InventorySystem;

public class TestBlockEntity extends BlockEntity implements Inventory {

    private int number = 10;
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private ItemStack item = new ItemStack(Items.AIR, 1);

    public TestBlockEntity() {
        super(ArchCreatia.TEST_BLOCK_ENTITY);
    }

    public int getNumber() {
        return number;
    }

    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getInvSize() {
        return getItems().size();
    }


    @Override
    public boolean isInvEmpty() {
        for (int i = 0; i < getInvSize(); i++) {
            ItemStack stack = getInvStack(i);
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public ItemStack getItem() {
        return item;
    }
    public ItemStack getInvStack(int slot) {
        return item;
    }

    public ItemStack takeInvStack(int slot, int count) {
        ItemStack result = Inventories.splitStack(getItems(), slot, count);
        if (!result.isEmpty()) {
            markDirty();
        }
        return result;
    }

    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(getItems(), slot);

    }



    public void setInvStack(int slot, ItemStack stack) {
        item = stack;
        if (stack.getCount() > getInvMaxStackAmount()) {
            stack.setCount(getInvMaxStackAmount());
        }
        markDirty();
    }

    public void clear() {
        getItems().clear();
    }

    //    @Override
//    default void markDirty() {
//        // Override if you want behavior.
//    }

    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        Inventories.fromTag(tag,items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag,items);
        return super.toTag(tag);
    }





}
