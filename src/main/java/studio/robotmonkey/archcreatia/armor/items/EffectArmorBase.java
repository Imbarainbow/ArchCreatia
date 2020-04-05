package studio.robotmonkey.archcreatia.armor.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EffectArmorBase extends ArmorBase {
    private StatusEffect statusEffect;
    public EffectArmorBase(StatusEffect statusEffect, ArmorMaterial material, EquipmentSlot slot, Settings settings) {
        super(material, slot, settings);
        this.statusEffect = statusEffect;
    }

    public void setStatusEffect(StatusEffect statusEffect) {
        this.statusEffect = statusEffect;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (true) {
            if(stack.getItem() == this) {
                entity.getArmorItems().forEach((ItemStack armorPiece) -> {
                    if(armorPiece.getItem() == this) {
                        if(entity instanceof PlayerEntity) {
                            if(this.statusEffect != null) {
                                ((PlayerEntity) entity).addStatusEffect(new StatusEffectInstance(this.statusEffect));
                            }
                        }
                    }
                });
            }
        }
    }
}
