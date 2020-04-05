package studio.robotmonkey.archcreatia.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import studio.robotmonkey.archcreatia.blocks.blockEntities.AbstractManaPipeEntity;
import studio.robotmonkey.archcreatia.blocks.blockEntities.ManaPipeEntity;

import java.util.HashSet;

public interface HasMana {
    float getCurrentMana();
    float getMaxMana();
    public abstract boolean addMana(float manaCount);
    public abstract boolean removeMana(float manaCount);
    boolean transferMana(HasMana hasMana, int amount);
    public abstract void addInput(HasMana hasMana);
    public abstract void setOutput(HasMana hasMana);
    public abstract HashSet<HasMana> getInputs();
    default void fixInputs() {
        for(HasMana input: (this).getInputs()) {
            input.setOutput(null);
        }
    }

}
