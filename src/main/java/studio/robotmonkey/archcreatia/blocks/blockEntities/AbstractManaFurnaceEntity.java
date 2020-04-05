package studio.robotmonkey.archcreatia.blocks.blockEntities;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.container.PropertyDelegate;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ContainerPropertyUpdateS2CPacket;
import net.minecraft.recipe.*;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.system.CallbackI;
import studio.robotmonkey.archcreatia.ArchCreatia;
import studio.robotmonkey.archcreatia.ManaGeneration.AbstractManaRecipies;
import studio.robotmonkey.archcreatia.blocks.AbstractManaCondensor;
import studio.robotmonkey.archcreatia.blocks.AbstractManaFurnaceBlock;
import studio.robotmonkey.archcreatia.util.HasMana;

import java.util.*;

public abstract class AbstractManaFurnaceEntity extends LockableContainerBlockEntity implements SidedInventory, RecipeUnlocker, RecipeInputProvider, Tickable, HasMana {
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{2, 1};
    private static final int[] SIDE_SLOTS = new int[]{1};
    private HashSet<HasMana> inputs = new HashSet<>();
    private HasMana outPut;
    private boolean update = false;
    private int transferAmount = 30;

    protected DefaultedList<ItemStack> inventory;
    private int burnTime;
    private int fuelTime;
    private int cookTime;
    private int cookTimeTotal;
    protected final PropertyDelegate propertyDelegate;
    private final Map<Identifier, Integer> recipesUsed;
//    protected final RecipeType<? extends AbstractCookingRecipe> recipeType;
    HashMap<Item, AbstractManaRecipies> recipes;

    private float manaTotal = 0;
    private float maxMana;

    protected AbstractManaFurnaceEntity(BlockEntityType<?> blockEntityType, HashMap<Item, AbstractManaRecipies> recipies, float maxMana) {
        super(blockEntityType);
        this.inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);
        this.maxMana = maxMana;
        this.propertyDelegate = new PropertyDelegate() {
            public int get(int index) {
                switch(index) {
                    case 0:
                        return AbstractManaFurnaceEntity.this.burnTime;
                    case 1:
                        return AbstractManaFurnaceEntity.this.fuelTime;
                    case 2:
                        return AbstractManaFurnaceEntity.this.cookTime;
                    case 3:
                        return AbstractManaFurnaceEntity.this.cookTimeTotal;
                    case 4:
                        return (int)AbstractManaFurnaceEntity.this.maxMana;
                    case 5:
                        return (int)AbstractManaFurnaceEntity.this.manaTotal;
                    default:
                        return 0;
                }
            }

            public void set(int index, int value) {
                switch(index) {
                    case 0:
                        AbstractManaFurnaceEntity.this.burnTime = value;
                        break;
                    case 1:
                        AbstractManaFurnaceEntity.this.fuelTime = value;
                        break;
                    case 2:
                        AbstractManaFurnaceEntity.this.cookTime = value;
                        break;
                    case 3:
                        AbstractManaFurnaceEntity.this.cookTimeTotal = value;
                    case 4:
                        AbstractManaFurnaceEntity.this.maxMana = value;
                    case 5:
                        AbstractManaFurnaceEntity.this.manaTotal = value;
                }

            }

            public int size() {
                return 6;
            }
        };
        this.recipesUsed = Maps.newHashMap();
        this.recipes = recipes;

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
        if(this.manaTotal + manaCount > this.maxMana - 1) {
            return false;
        } else {
            this.manaTotal += manaCount;
            return true;
        }
    }

    @Override
    public boolean removeMana(float manaCount) {
        if(this.manaTotal - manaCount < 0) {
            return false;
        } else {
            return addMana(-manaCount);
        }
    }

    @Override
    public boolean transferMana(HasMana hasMana, int amount) {
        if(hasMana != null) {
            if (this.manaTotal - amount >= 0 && hasMana.addMana(amount)) {
                return removeMana(amount);
            } else {
                return false;
            }
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

    public static HashMap<Item, AbstractManaRecipies> createManaMap() {
        HashMap<Item, AbstractManaRecipies> MANA_GENERATION = new HashMap<>();
        MANA_GENERATION.put(Items.OAK_LEAVES, new AbstractManaRecipies(new ItemStack(Items.OAK_LEAVES), 5, 100));
        MANA_GENERATION.put(Items.OAK_LOG, new AbstractManaRecipies(new ItemStack(Items.OAK_LOG), 5, 100));
        MANA_GENERATION.put(ArchCreatia.IMPURE_ICHOR, new AbstractManaRecipies(new ItemStack(ArchCreatia.IMPURE_ICHOR), 75, 20));
        MANA_GENERATION.put(ArchCreatia.MANA_CRYSTAL, new AbstractManaRecipies(new ItemStack(ArchCreatia.MANA_CRYSTAL), 100, 30));

        return MANA_GENERATION;
    }

    public static Map<Item, Integer> createFuelTimeMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        addFuel(map, (ItemConvertible) Items.LAVA_BUCKET, 20000);
        addFuel(map, (ItemConvertible) Blocks.COAL_BLOCK, 16000);
        addFuel(map, (ItemConvertible)Items.BLAZE_ROD, 2400);
        addFuel(map, (ItemConvertible)Items.COAL, 1600);
        addFuel(map, (ItemConvertible)Items.CHARCOAL, 1600);
        addFuel(map, (Tag) ItemTags.LOGS, 300);
        addFuel(map, (Tag)ItemTags.PLANKS, 300);
        addFuel(map, (Tag)ItemTags.WOODEN_STAIRS, 300);
        addFuel(map, (Tag)ItemTags.WOODEN_SLABS, 150);
        addFuel(map, (Tag)ItemTags.WOODEN_TRAPDOORS, 300);
        addFuel(map, (Tag)ItemTags.WOODEN_PRESSURE_PLATES, 300);
        addFuel(map, (ItemConvertible)Blocks.OAK_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.BIRCH_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.SPRUCE_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.JUNGLE_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.DARK_OAK_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.ACACIA_FENCE, 300);
        addFuel(map, (ItemConvertible)Blocks.OAK_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.BIRCH_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.SPRUCE_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.JUNGLE_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.DARK_OAK_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.ACACIA_FENCE_GATE, 300);
        addFuel(map, (ItemConvertible)Blocks.NOTE_BLOCK, 300);
        addFuel(map, (ItemConvertible)Blocks.BOOKSHELF, 300);
        addFuel(map, (ItemConvertible)Blocks.LECTERN, 300);
        addFuel(map, (ItemConvertible)Blocks.JUKEBOX, 300);
        addFuel(map, (ItemConvertible)Blocks.CHEST, 300);
        addFuel(map, (ItemConvertible)Blocks.TRAPPED_CHEST, 300);
        addFuel(map, (ItemConvertible)Blocks.CRAFTING_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.DAYLIGHT_DETECTOR, 300);
        addFuel(map, (Tag)ItemTags.BANNERS, 300);
        addFuel(map, (ItemConvertible)Items.BOW, 300);
        addFuel(map, (ItemConvertible)Items.FISHING_ROD, 300);
        addFuel(map, (ItemConvertible)Blocks.LADDER, 300);
        addFuel(map, (Tag)ItemTags.SIGNS, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_SHOVEL, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_SWORD, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_HOE, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_AXE, 200);
        addFuel(map, (ItemConvertible)Items.WOODEN_PICKAXE, 200);
        addFuel(map, (Tag)ItemTags.WOODEN_DOORS, 200);
        addFuel(map, (Tag)ItemTags.BOATS, 1200);
        addFuel(map, (Tag)ItemTags.WOOL, 100);
        addFuel(map, (Tag)ItemTags.WOODEN_BUTTONS, 100);
        addFuel(map, (ItemConvertible)Items.STICK, 100);
        addFuel(map, (Tag)ItemTags.SAPLINGS, 100);
        addFuel(map, (ItemConvertible)Items.BOWL, 100);
        addFuel(map, (Tag)ItemTags.CARPETS, 67);
        addFuel(map, (ItemConvertible)Blocks.DRIED_KELP_BLOCK, 4001);
        addFuel(map, (ItemConvertible)Items.CROSSBOW, 300);
        addFuel(map, (ItemConvertible)Blocks.BAMBOO, 50);
        addFuel(map, (ItemConvertible)Blocks.DEAD_BUSH, 100);
        addFuel(map, (ItemConvertible)Blocks.SCAFFOLDING, 400);
        addFuel(map, (ItemConvertible)Blocks.LOOM, 300);
        addFuel(map, (ItemConvertible)Blocks.BARREL, 300);
        addFuel(map, (ItemConvertible)Blocks.CARTOGRAPHY_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.FLETCHING_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.SMITHING_TABLE, 300);
        addFuel(map, (ItemConvertible)Blocks.COMPOSTER, 300);
        return map;
    }

    private static void addFuel(Map<Item, Integer> fuelTimes, Tag<Item> tag, int fuelTime) {
        Iterator var3 = tag.values().iterator();

        while(var3.hasNext()) {
            Item item = (Item)var3.next();
            fuelTimes.put(item, fuelTime);
        }

    }

    private static void addFuel(Map<Item, Integer> fuelTimes, ItemConvertible item, int fuelTime) {
        fuelTimes.put(item.asItem(), fuelTime);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.inventory = DefaultedList.ofSize(this.getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, this.inventory);
        this.burnTime = tag.getShort("BurnTime");
        this.cookTime = tag.getShort("CookTime");
        this.cookTimeTotal = tag.getShort("CookTimeTotal");
        this.fuelTime = this.getFuelTime((ItemStack)this.inventory.get(1));
        int i = tag.getShort("RecipesUsedSize");

        for(int j = 0; j < i; ++j) {
            Identifier identifier = new Identifier(tag.getString("RecipeLocation" + j));
            int k = tag.getInt("RecipeAmount" + j);
            this.recipesUsed.put(identifier, k);
        }

        this.maxMana = tag.getFloat("maxMana");
        this.manaTotal = tag.getFloat("totalMana");
        this.update = tag.getBoolean("hasOuput");

    }

    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putShort("BurnTime", (short)this.burnTime);
        tag.putShort("CookTime", (short)this.cookTime);
        tag.putShort("CookTimeTotal", (short)this.cookTimeTotal);
        tag.putFloat("maxMana", this.maxMana);
        tag.putFloat("totalMana", this.manaTotal);

        Inventories.toTag(tag, this.inventory);
        tag.putShort("RecipesUsedSize", (short)this.recipesUsed.size());
        int i = 0;

        for(Iterator var3 = this.recipesUsed.entrySet().iterator(); var3.hasNext(); ++i) {
            Map.Entry<Identifier, Integer> entry = (Map.Entry)var3.next();
            tag.putString("RecipeLocation" + i, ((Identifier)entry.getKey()).toString());
            tag.putInt("RecipeAmount" + i, (Integer)entry.getValue());
        }

        tag.putBoolean("hasOutput", outPut != null);

        return tag;
    }

    public void tick() {
        if(update) {
            update = false;
            ((AbstractManaFurnaceBlock)this.getWorld().getBlockState(this.getPos()).getBlock()).updateConnections(this.getWorld(), this.getPos(), this.getWorld().getBlockState(this.getPos()));
        }

        if(this.world.isClient) {
            return;
        }

        boolean bl = this.isBurning();
        boolean bl2 = false;

        //TODO readd client check here, this should be only on server
        if(this.manaTotal > this.maxMana - 1) {
            this.manaTotal = 0;
        }
        //TODO fix this
//        if(this.outPut != null && this.manaTotal > 0 && !this.world.isClient && this.manaTotal <= this.getMaxMana()) {
//            if(this.manaTotal != 0 && !transferMana(this.outPut, 30) && this.outPut != null /* && this.manaTotal != getMaxMana()*/) {
//                System.out.println("HEre");
//                return;
//            }
//        }
        if(this.outPut != null && getCurrentMana() >= this.transferAmount && !this.world.isClient) {
            transferMana(this.outPut, this.transferAmount);
        }

        if(this.manaTotal >= getMaxMana() && !this.world.isClient) {
//            System.out.println("HEre2");
            return;
        }
        if (this.isBurning()) {
            --this.burnTime;
        }
        //TODO FIX BUG WHERE ONCE FULL IT STILL ADDS NEW FUEL

        if (!this.world.isClient) {
            ItemStack itemStack = this.inventory.get(1);
            if (!this.isBurning() && (itemStack.isEmpty() || ((ItemStack)this.inventory.get(0)).isEmpty())) {
                if (this.cookTime > 0) {
                    this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
                }
            } else {
                AbstractManaRecipies recipe = createManaMap().getOrDefault(this.inventory.get(0).getItem(), AbstractManaRecipies.defaultRecipe);
//                Recipe<?> recipe = (Recipe)this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).orElse(null);
                if (!this.isBurning() && this.canAcceptRecipeOutput(recipe) && this.inventory.get(0).getItem() == recipe.getInputAsItem()) {
                    this.burnTime = this.getFuelTime(itemStack);
                    this.fuelTime = this.burnTime;
                    if (this.isBurning()) {
                        bl2 = true;
                        if (!itemStack.isEmpty()) {
                            Item item = itemStack.getItem();
                            itemStack.decrement(1);
                            if (itemStack.isEmpty()) {
                                Item item2 = item.getRecipeRemainder();
                                this.inventory.set(1, item2 == null ? ItemStack.EMPTY : new ItemStack(item2));
                            }
                        }
                    }
                }

                if (this.isBurning() && !this.world.isClient) {
                    if(!this.canAcceptRecipeOutput(recipe)) {
//                        System.out.println("HEr3");
                        return;
                    }
                    if(this.cookTime == 0 && !this.world.isClient) {
                        this.craftRecipe(recipe);
                        this.markDirty();
                    }
//                    TODO make this increase by a specific amount for specific items
                    if (this.manaTotal + recipe.getManaOutput() < getMaxMana()) {
                        if(!addMana(recipe.getManaOutput())) {
//                            System.out.println("HEre4");
                            return;
                        }
//                        this.manaTotal += recipe.getManaOutput();
                    } else {
                        return;
                    }
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = this.getCookTime();
                        bl2 = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            }

            if (bl != this.isBurning()) {
                bl2 = true;
                this.world.setBlockState(this.pos, (BlockState)this.world.getBlockState(this.pos).with(AbstractManaFurnaceBlock.LIT, this.isBurning()), 3);
            }
        }

        if(bl2) {
            this.markDirty();
        }

    }

    @Override
    public HashSet<HasMana> getInputs() {
        return this.inputs;
    }

    protected boolean canAcceptRecipeOutput(AbstractManaRecipies recipe) {
        if(this.manaTotal + recipe.getManaOutput() <= getMaxMana()) {
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
        ItemStack itemStack = (ItemStack)this.inventory.get(0);
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

    protected int getFuelTime(ItemStack fuel) {
        if (fuel.isEmpty()) {
            return 0;
        } else {
            Item item = fuel.getItem();
            return (Integer)createFuelTimeMap().getOrDefault(item, 0);
        }
    }

    protected int getCookTime() {
        return createManaMap().getOrDefault(this.inventory.get(0).getItem(), AbstractManaRecipies.defaultRecipe).getCookingTime();
        //return (Integer)this.world.getRecipeManager().getFirstMatch(this.recipeType, this, this.world).map(AbstractCookingRecipe::getCookTime).orElse(200);
    }

    public static boolean canUseAsFuel(ItemStack stack) {
        return createFuelTimeMap().containsKey(stack.getItem());
    }

    public int[] getInvAvailableSlots(Direction side) {
        if (side == Direction.DOWN) {
            return BOTTOM_SLOTS;
        } else {
            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
        }
    }

    public boolean canInsertInvStack(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValidInvStack(slot, stack);
    }

    public boolean canExtractInvStack(int slot, ItemStack stack, Direction dir) {
        if (dir == Direction.DOWN && slot == 1) {
            Item item = stack.getItem();
            if (item != Items.WATER_BUCKET && item != Items.BUCKET) {
                return false;
            }
        }

        return true;
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

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getInvStack(int slot) {
        return (ItemStack)this.inventory.get(slot);
    }

    public ItemStack takeInvStack(int slot, int amount) {
        return Inventories.splitStack(this.inventory, slot, amount);
    }

    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(this.inventory, slot);
    }

    public void setInvStack(int slot, ItemStack stack) {
        ItemStack itemStack = (ItemStack)this.inventory.get(slot);
        boolean bl = !stack.isEmpty() && stack.isItemEqualIgnoreDamage(itemStack) && ItemStack.areTagsEqual(stack, itemStack);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getInvMaxStackAmount()) {
            stack.setCount(this.getInvMaxStackAmount());
        }
    //TODO FIX
        if (slot == 0 && !bl) {
            this.cookTimeTotal = this.getCookTime();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    public boolean canPlayerUseInv(PlayerEntity player) {
        if (this.world.getBlockEntity(this.pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    public boolean isValidInvStack(int slot, ItemStack stack) {
        if (slot == 2) {
            return false;
        } else if (slot != 1) {
            return true;
        } else {
            ItemStack itemStack = (ItemStack)this.inventory.get(1);
            return canUseAsFuel(stack) || stack.getItem() == Items.BUCKET && itemStack.getItem() != Items.BUCKET;
        }
    }

    public void clear() {
        this.inventory.clear();
    }

    public void setLastRecipe(@Nullable Recipe<?> recipe) {
        if (recipe != null) {
            this.recipesUsed.compute(recipe.getId(), (identifier, integer) -> {
                return 1 + (integer == null ? 0 : integer);
            });
        }

    }

    @Nullable
    public Recipe<?> getLastRecipe() {
        return null;
    }

    public void unlockLastRecipe(PlayerEntity player) {
    }

    public void dropExperience(PlayerEntity player) {
        List<Recipe<?>> list = Lists.newArrayList();
        Iterator var3 = this.recipesUsed.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<Identifier, Integer> entry = (Map.Entry)var3.next();
            player.world.getRecipeManager().get((Identifier)entry.getKey()).ifPresent((recipe) -> {
                list.add(recipe);
                dropExperience(player, (Integer)entry.getValue(), ((AbstractCookingRecipe)recipe).getExperience());
            });
        }

        player.unlockRecipes(list);
        this.recipesUsed.clear();
    }

    private static void dropExperience(PlayerEntity player, int totalExperience, float experienceFraction) {
        int i;
        if (experienceFraction == 0.0F) {
            totalExperience = 0;
        } else if (experienceFraction < 1.0F) {
            i = MathHelper.floor((float)totalExperience * experienceFraction);
            if (i < MathHelper.ceil((float)totalExperience * experienceFraction) && Math.random() < (double)((float)totalExperience * experienceFraction - (float)i)) {
                ++i;
            }

            totalExperience = i;
        }

        while(totalExperience > 0) {
            i = ExperienceOrbEntity.roundToOrbSize(totalExperience);
            totalExperience -= i;
            player.world.spawnEntity(new ExperienceOrbEntity(player.world, player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, i));
        }

    }

    public void provideRecipeInputs(RecipeFinder recipeFinder) {
        Iterator var2 = this.inventory.iterator();

        while(var2.hasNext()) {
            ItemStack itemStack = (ItemStack)var2.next();
            recipeFinder.addItem(itemStack);
        }

    }

}
