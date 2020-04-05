package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import studio.robotmonkey.archcreatia.blocks.AbstractManaPipe;
import studio.robotmonkey.archcreatia.blocks.AbstractManaTankBlock;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractManaTankEntity extends LockableContainerBlockEntity implements Tickable, HasMana {
    private int maxMana;
    private int manaTotal;
    private HashSet<HasMana> inputs = new HashSet<>();
    private HasMana output;
    protected final PropertyDelegate propertyDelegate;
    private boolean update = false;

    protected AbstractManaTankEntity(BlockEntityType<?> blockEntityType, int maxMana) {
        super(blockEntityType);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return AbstractManaTankEntity.this.maxMana;
                    case 1:
                        return AbstractManaTankEntity.this.manaTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        AbstractManaTankEntity.this.maxMana = value;
                    case 1:
                        AbstractManaTankEntity.this.manaTotal = value;
                }

            }

            public int size() {
                return 2;
            }
        };
        this.maxMana = maxMana;
        this.manaTotal = 0;
    }

    public void addInput(HasMana input) {
        this.inputs.add(input);
    }

    public void setOutput(HasMana output) {
        this.output = output;
    }

    @Override
    public HashSet<HasMana> getInputs() {
        return this.inputs;
    }

    @Override
    public void tick() {
        if(update) {
            update = false;
            ((AbstractManaTankBlock)this.getWorld().getBlockState(this.getPos()).getBlock()).updateConnections(this.getWorld(), this.getPos(), this.getWorld().getBlockState(this.getPos()));
        }
        if(this.manaTotal > this.maxMana - 1) {
            this.manaTotal = 0;
        }
        if(this.output != null && this.manaTotal > 0) {
            //Todo makes this an actual amount?
            transferMana(this.output, 100);
        }
    }

    public void setManaTotal(int amount) {
        this.manaTotal = amount;
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
        if(getCurrentMana() + manaCount > getMaxMana()) {
            return false;
        } else {
            this.manaTotal += manaCount;
            return true;
        }
    }

    @Override
    public boolean removeMana(float manaCount) {
        if(getCurrentMana() - manaTotal < 0) {
            return false;
        } else {
            return addMana(-manaCount);
        }

    }

    @Override
    public boolean transferMana(HasMana hasMana, int amount) {
        if(this.manaTotal - amount >= 0 && hasMana.addMana(amount)) {
            return removeMana(amount);
        } else {
            return false;
        }
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.maxMana = tag.getInt("maxMana");
        this.manaTotal = tag.getInt("totalMana");
        this.update = tag.getBoolean("hasOutput");

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putInt("maxMana", this.maxMana);
        tag.putInt("totalMana", this.manaTotal);
        tag.putBoolean("hasOutput", this.output != null);

        return tag;
    }


    @Override
    public int getInvSize() {
        return 0;
    }

    @Override
    public boolean isInvEmpty() {
        return true;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return null;
    }

    @Override
    public ItemStack takeInvStack(int slot, int amount) {
        return null;
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        return null;
    }

    @Override
    public void setInvStack(int slot, ItemStack stack) {
        return;
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        return;
    }


}
