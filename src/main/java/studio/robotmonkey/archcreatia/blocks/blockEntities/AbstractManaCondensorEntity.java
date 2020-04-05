package studio.robotmonkey.archcreatia.blocks.blockEntities;

import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.ManaGeneration.AbstractManaRecipies;
import studio.robotmonkey.archcreatia.blocks.AbstractManaCondensor;
import studio.robotmonkey.archcreatia.util.DirectionHelper;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.*;

public abstract class AbstractManaCondensorEntity extends LockableContainerBlockEntity implements SidedInventory, Tickable, HasMana {
    private static final int[] BOTTOM_SLOTS = new int[]{0};
    private HashSet<HasMana> inputs = new HashSet<>();
    private HasMana outPut;
    private boolean isProcessing = false;
    protected DefaultedList<ItemStack> inventory;
    private int manaUsage;
    private int cookTime;
    private int cookTimeTotal;
    protected final PropertyDelegate propertyDelegate;
    private float manaTotal = 0;
    private float maxMana;
    private Item product;
    private boolean update = false;

    protected AbstractManaCondensorEntity(BlockEntityType<?> blockEntityType, float maxMana, int cookTimeTotal, int manaUsage, Item product) {
        super(blockEntityType);
        this.inventory = DefaultedList.ofSize(1, ItemStack.EMPTY);
        this.maxMana = maxMana;
        this.cookTimeTotal = cookTimeTotal;
        this.manaUsage = manaUsage;
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch (index) {
                    case 0:
                        return AbstractManaCondensorEntity.this.cookTime;
                    case 1:
                        return AbstractManaCondensorEntity.this.cookTimeTotal;
                    case 2:
                        return (int) AbstractManaCondensorEntity.this.maxMana;
                    case 3:
                        return (int) AbstractManaCondensorEntity.this.manaTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0:
                        AbstractManaCondensorEntity.this.cookTime = value;
                        break;
                    case 1:
                        AbstractManaCondensorEntity.this.cookTimeTotal = value;
                    case 2:
                        AbstractManaCondensorEntity.this.maxMana = value;
                    case 3:
                        AbstractManaCondensorEntity.this.manaTotal = value;
                }

            }

            public int size() {
                return 4;
            }
        };
        this.product = product;

    }

    @Override
    public float getCurrentMana() {
        return this.manaTotal;
    }
    @Override
    public float getMaxMana() {
        return this.maxMana - 1;
    }

    @Override
    public boolean addMana(float manaCount) {
        if (this.manaTotal + manaCount > this.maxMana - 1) {
            return false;
        } else {
            this.manaTotal += manaCount;
            return true;
        }
    }

    @Override
    public boolean removeMana(float manaCount) {
        if (this.manaTotal - manaCount < 0) {
            return false;
        } else {
            return addMana(-manaCount);
        }
    }

    @Override
    public boolean transferMana(HasMana hasMana, int amount) {
        if (this.manaTotal - amount >= 0 && hasMana.addMana(amount)) {
            return removeMana(amount);
        } else {
            return false;
        }
    }

    public void addInput(HasMana hasMana) {
        inputs.add(hasMana);
    }
    public void setOutput(HasMana hasMana) {
        this.outPut = hasMana;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        this.cookTime = tag.getShort("CookTime");
        this.cookTimeTotal = tag.getShort("CookTimeTotal");
        this.isProcessing = tag.getBoolean("processing");
        this.maxMana = tag.getFloat("maxMana");
        this.manaTotal = tag.getFloat("totalMana");
        update = tag.getBoolean("hasOutput");

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putShort("CookTime", (short) this.cookTime);
        tag.putShort("CookTimeTotal", (short) this.cookTimeTotal);
        tag.putFloat("maxMana", this.maxMana);
        tag.putFloat("totalMana", this.manaTotal);
        tag.putBoolean("hasOutput", false);
        if(this.outPut != null) {
            tag.putBoolean("hasOutput", true);
        }
        tag.putBoolean("processing", this.isProcessing);

        return tag;
    }

    public void tick() {
        if(update) {
            update = false;
            ((AbstractManaCondensor)this.getWorld().getBlockState(this.getPos()).getBlock()).updateConnections(this.getWorld(), this.getPos(), this.getWorld().getBlockState(this.getPos()));
        }
        boolean bl2 = false;
        if (this.manaTotal > this.maxMana - 1 && this.world.isClient) {
            this.manaTotal = 0;
        }

        if (this.outPut != null && this.manaTotal > 0 && !this.world.isClient && this.manaTotal <= this.getMaxMana()) {
            transferMana(this.outPut, 30);
        }
        if(this.inventory.get(0).getItem() == this.product) {
            if (this.inventory.get(0).getCount() == this.inventory.get(0).getMaxCount()) {
                return;
            }
        }
        if(this.inventory.get(0).getItem() != this.product && !(this.inventory.get(0).getItem() == Items.AIR)) {
//            System.out.println(this.inventory.get(0));
            return;
        }

        if (!this.world.isClient) {
            if(!this.isProcessing) {
                if(removeMana(manaUsage)) {
                    cookTime = 0;
                    isProcessing = true;
                }
            } else {
                bl2 = true;
                this.cookTime++;
                if(this.cookTime == this.cookTimeTotal) {
                    cookTime = 0;
                    if(this.inventory.get(0).isItemEqual(ItemStack.EMPTY) || this.inventory.get(0).getItem() == Items.AIR) {
                        ItemStack item = new ItemStack(this.product);
//                        System.out.println("new");
                        this.inventory.set(0, item);
                    } else {
                        if(this.inventory.get(0).getMaxCount() > this.inventory.get(0).getCount() && this.inventory.get(0).getItem() == this.product) {
//                            System.out.println("Inc");
                            this.inventory.get(0).increment(1);
                        }
                    }
                    this.isProcessing = false;
                    this.markDirty();
                }
            }
        }

    }

    @Override
    public HashSet<HasMana> getInputs() {
        return this.inputs;
    }

    protected boolean canAcceptRecipeOutput(AbstractManaRecipies recipe) {
        if (this.manaTotal + recipe.getManaOutput() < maxMana - 1) {
            return true;
        } else {
            return false;
        }
    }

    //    protected boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe) {
//        if (!((ItemStack)this.inventory.get(0)).isEmpty() && recipe != null) {
//            ItemStack itemStack = recipe.getOutput();
//            if (itemStack.isEmpty()) {
//                return false;
//            } else {
//                ItemStack itemStack2 = (ItemStack)this.inventory.get(2);
//                if (itemStack2.isEmpty()) {
//                    return true;
//                } else if (!itemStack2.isItemEqualIgnoreDamage(itemStack)) {
//                    return false;
//                } else if (itemStack2.getCount() < this.getInvMaxStackAmount() && itemStack2.getCount() < itemStack2.getMaxCount()) {
//                    return true;
//                } else {
//                    return itemStack2.getCount() < itemStack.getMaxCount();
//                }
//            }
//        } else {
//            return false;
//        }
//    }
    private void craftRecipe(AbstractManaRecipies recipe) {
        ItemStack itemStack = (ItemStack) this.inventory.get(0);
        if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
            itemStack.decrement(1);
        }
    }
//    private void craftRecipe(@Nullable Recipe<?> recipe) {
//        if (recipe != null && this.canAcceptRecipeOutput(recipe)) {
//            ItemStack itemStack = (ItemStack)this.inventory.get(0);
//            ItemStack itemStack2 = recipe.getOutput();
//            ItemStack itemStack3 = (ItemStack)this.inventory.get(2);
//            if (itemStack3.isEmpty()) {
//                this.inventory.set(2, itemStack2.copy());
//            } else if (itemStack3.getItem() == itemStack2.getItem()) {
//                itemStack3.increment(1);
//            }
//
//            if (!this.world.isClient) {
//                this.setLastRecipe(recipe);
//            }
//
//            if (itemStack.getItem() == Blocks.WET_SPONGE.asItem() && !((ItemStack)this.inventory.get(1)).isEmpty() && ((ItemStack)this.inventory.get(1)).getItem() == Items.BUCKET) {
//                this.inventory.set(1, new ItemStack(Items.WATER_BUCKET));
//            }
//
//            itemStack.decrement(1);
//        }
//    }


    protected int getCookTime() {
        return this.cookTimeTotal;
        //return (Integer)this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }


    public int[] getInvAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        } else {
            return new int[]{};
        }
    }

    public boolean canInsertInvStack(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValidInvStack(slot, stack);
    }

    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 0) {
            Item item = stack.getItem();
            return true;
        }
        return false;
    }

    public int getInvSize() {
        return this.inventory.size();
    }

    public boolean isInvEmpty() {
        Iterator var1 = this.inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack) var1.next();
        } while (itemStack.isEmpty());

        return false;
    }

    public ItemStack getInvStack(int slot) {
        return (ItemStack) this.inventory.get(slot);
    }

    public ItemStack takeInvStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    public void setInvStack(int slot, ItemStack stack) {
        ItemStack itemStack = (ItemStack) this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getInvMaxStackAmount()) {
            stack.setCount(this.getInvMaxStackAmount());
        }

        //TODO FIX
        if (slot == 0 && !bl) {
            this.cookTimeTotal = this.getCookTime();
//            this.cookTime = 0;
            this.markDirty();
        }

    }

    public boolean canPlayerUseInv(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isValidInvStack(int slot, ItemStack stack) {
        if (slot == 0 && stack.getItem() == this.product) {
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        this.inventory.clear();
    }

}