package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.AbstractManaCondensor;
import studio.robotmonkey.archcreatia.blocks.AbstractManaPipe;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.HashSet;

public abstract class AbstractManaPipeEntity extends BlockEntity implements Tickable, HasMana {
    private int maxMana;
    private int manaTotal = 0;
    private int tranferRate;
    private HashSet<HasMana> inputs = new HashSet<>();
    private HasMana output;
    protected final PropertyDelegate propertyDelegate;
    private boolean update = false;

    public AbstractManaPipeEntity(BlockEntityType<?> type, int maxMana, int transferRate) {
        super(type);
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return AbstractManaPipeEntity.this.maxMana;
                    case 1:
                        return AbstractManaPipeEntity.this.manaTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        AbstractManaPipeEntity.this.maxMana = value;
                    case 1:
                        AbstractManaPipeEntity.this.manaTotal = value;
                }

            }

            public int size() {
                return 2;
            }
        };
        this.maxMana = maxMana;
        this.tranferRate = transferRate;
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
            ((AbstractManaPipe)this.getWorld().getBlockState(this.getPos()).getBlock()).updateConnections(this.getWorld(), this.getPos(), this.getWorld().getBlockState(this.getPos()));
        }
        if(this.output != null) {
            //Todo makes this an actual amount?
            transferMana(this.output, this.tranferRate);
        }
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



}
