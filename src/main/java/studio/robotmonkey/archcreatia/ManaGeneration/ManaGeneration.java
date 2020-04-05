package studio.robotmonkey.archcreatia.ManaGeneration;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.HashMap;

public class ManaGeneration {
    private HashMap<Item, AbstractManaRecipies> MANA_GENERATION = new HashMap<>();
    public ManaGeneration() {
        createManaMap();
    }
    public void createManaMap() {
        MANA_GENERATION = new HashMap<>();
        MANA_GENERATION.put(Items.OAK_LEAVES, new AbstractManaRecipies(new ItemStack(Items.OAK_LEAVES), 10, 10));
        MANA_GENERATION.put(Items.OAK_LOG, new AbstractManaRecipies(new ItemStack(Items.OAK_LOG), 10, 10));
    }

    public HashMap<Item, AbstractManaRecipies> getManaMap() {
        return MANA_GENERATION;
    }

}
