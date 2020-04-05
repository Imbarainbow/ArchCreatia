package studio.robotmonkey.archcreatia.blocks.blockEntities;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.Container;
import net.minecraft.container.FurnaceContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.ManaGeneration.ManaGeneration;
import studio.robotmonkey.archcreatia.containers.ManaFurnaceContainer;

public class ManaFurnaceEntity extends AbstractManaFurnaceEntity {
    public ManaFurnaceEntity() {
        super(ArchCreatia.MANA_FURNACE_ENTITY, ArchCreatia.MANA_RECIPES.getManaMap(), 5001);
    }

    protected Text getContainerName() {
        return new TranslatableText("container.archcreatia.mana_furnace");
    }

    @Override
    public Container createContainer(int i, PlayerInventory playerInventory) {
        return new ManaFurnaceContainer(i, playerInventory, this, this.propertyDelegate);
    }
}
