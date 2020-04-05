package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.HashSet;

public class ManaBlockEntity extends BlockEntity implements HasMana {

    private float currentMana = 0;
    private static float maxMana = 100;

    public ManaBlockEntity() {
        super(ArchCreatia.MANA_FURNACE_ENTITY);
    }

    @Override
    public float getCurrentMana() {
        return this.currentMana;
    }

    @Override
    public boolean addMana(float manaCount) {
        if(currentMana + manaCount < maxMana) {
           return true;
        }
        return false;
    }

    @Override
    public boolean removeMana(float manaCount) {
        if(getCurrentMana() - manaCount > 0) {
            return addMana(-manaCount);
        }
        return false;
    }

    @Override
    public boolean transferMana(HasMana hasMana, int amount) {
        return false;
    }

    @Override
    public void addInput(HasMana hasMana) {

    }

    @Override
    public void setOutput(HasMana hasMana) {

    }

    @Override
    public HashSet<HasMana> getInputs() {
        return null;
    }

    @Override
    public void fixInputs() {

    }


    @Override
    public float getMaxMana() {
        return this.maxMana;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        currentMana = tag.getFloat("manaCount");

    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);

        tag.putFloat("manaCount", currentMana);
        return tag;
    }
}
