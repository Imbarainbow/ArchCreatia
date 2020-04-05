package studio.robotmonkey.archcreatia.armor.items;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffects;
import studio.robotmonkey.archcreatia.armor.materials.ArchMateria;

public class ManaEnhancedChestplate extends EffectArmorBase {
    public ManaEnhancedChestplate(Settings settings) {
        super(null, ArchMateria.ManaEnhancedIron, EquipmentSlot.CHEST, settings);
    }
}
