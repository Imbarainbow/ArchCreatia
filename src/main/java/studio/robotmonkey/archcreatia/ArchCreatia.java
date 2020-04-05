package studio.robotmonkey.archcreatia;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.container.ContainerListener;
import net.minecraft.container.ContainerType;
import net.minecraft.container.FurnaceContainer;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import studio.robotmonkey.archcreatia.ManaGeneration.ManaGeneration;
import studio.robotmonkey.archcreatia.armor.items.ManaEnhancedBoots;
import studio.robotmonkey.archcreatia.armor.items.ManaEnhancedChestplate;
import studio.robotmonkey.archcreatia.armor.items.ManaEnhancedHelmet;
import studio.robotmonkey.archcreatia.armor.items.ManaEnhancedLeggings;
import studio.robotmonkey.archcreatia.blocks.*;
import studio.robotmonkey.archcreatia.blocks.blockEntities.*;
import studio.robotmonkey.archcreatia.containers.ManaFurnaceContainer;
import studio.robotmonkey.archcreatia.items.ImpureIchor;
import studio.robotmonkey.archcreatia.items.ManaCrystal;
import studio.robotmonkey.archcreatia.items.ManaEnhancedIron;
import studio.robotmonkey.archcreatia.items.PipeWrench;


public class ArchCreatia implements ModInitializer {

	//Creative Inventory Tabs
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(
			new Identifier("archcreatia", "archcreatia_items"))
			.icon(() -> new ItemStack(ArchCreatia.IMPURE_ICHOR))
			.build();

	public static final ItemGroup MACHINE_GROUP = FabricItemGroupBuilder.create(
			new Identifier("archcreatia", "archcreatia_machines"))
			.icon(() -> new ItemStack(ArchCreatia.WEAK_MANA_CONDENSOR))
			.build();

	public static final ItemGroup ARMOR_GROUP = FabricItemGroupBuilder.create(
			new Identifier("archcreatia", "archcreatia_armor"))
			.icon(() -> new ItemStack(ArchCreatia.MANA_CHEST))
			.build();


	//Item Instances
	public static final PipeWrench PIPE_WRENCH = new PipeWrench(new Item.Settings().group(ArchCreatia.ITEM_GROUP).maxCount(1));
	public static final ImpureIchor IMPURE_ICHOR = new ImpureIchor(new Item.Settings().group(ArchCreatia.ITEM_GROUP));
	public static final ManaCrystal MANA_CRYSTAL = new ManaCrystal(new Item.Settings().group(ArchCreatia.ITEM_GROUP));
	public static final ManaEnhancedIron MANA_ENHANCED_IRON = new ManaEnhancedIron(new Item.Settings().group(ArchCreatia.ITEM_GROUP));

	//Block instances
//	public static final TestBlock TEST_BLOCK = new TestBlock(FabricBlockSettings.of(Material.METAL).strength(1f, 3f).build());
//	public static final ManaBlockTest MANA_BLOCK = new ManaBlockTest(FabricBlockSettings.of(Material.METAL).build());
	public static final ManaFurnace MANA_FURNACE = new ManaFurnace(FabricBlockSettings.of(Material.METAL).strength(1f, 3f).build());
	public static final ManaTank MANA_TANK = new ManaTank(FabricBlockSettings.of(Material.METAL).strength(1f, 3f).build());
	public static final ManaPipe MANA_PIPE = new ManaPipe(FabricBlockSettings.of(Material.METAL).strength(0.8f, 2f).build());
	public static final WeakManaCondensor WEAK_MANA_CONDENSOR = new WeakManaCondensor(FabricBlockSettings.of(Material.METAL).strength(1f, 3f).build());
	public static final ManaCondensor MANA_CONDENSOR = new ManaCondensor(FabricBlockSettings.of(Material.METAL).strength(1f, 3f).build());

	//Block Entities
	public static BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY;
//	public static BlockEntityType<ManaBlockEntity> MANA_BLOCK_ENTITY;
	public static BlockEntityType<ManaFurnaceEntity> MANA_FURNACE_ENTITY;
	public static BlockEntityType<ManaTankEntity> MANA_TANK_ENTITY;
	public static BlockEntityType<ManaPipeEntity> MANA_PIPE_ENTITY;
	public static BlockEntityType<WeakManaCondensorEntity> WEAK_MANA_CONDENSOR_ENTITY;
	public static BlockEntityType<ManaCondensorEntity> MANA_CONDENSOR_ENTITY;

	//Recipes
	public static ManaGeneration MANA_RECIPES = new ManaGeneration();

	//Containers
//	public static final ContainerType<ManaFurnaceContainer> MANA_FURNACE_CONTAINER;

	//Armor
	//Armor Registry
	public static final ManaEnhancedHelmet MANA_HELMET = new ManaEnhancedHelmet((new Item.Settings().group(ArchCreatia.ARMOR_GROUP)));
	public static final ManaEnhancedChestplate MANA_CHEST = new ManaEnhancedChestplate((new Item.Settings().group(ArchCreatia.ARMOR_GROUP)));
	public static final ManaEnhancedLeggings MANA_LEGS = new ManaEnhancedLeggings((new Item.Settings().group(ArchCreatia.ARMOR_GROUP)));
	public static final ManaEnhancedBoots MANA_BOOTS = new ManaEnhancedBoots((new Item.Settings().group(ArchCreatia.ARMOR_GROUP)));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		System.out.println("ArchCreatia is welcoming magic to your world!");

		//Item Registry

		Registry.register(Registry.ITEM, new Identifier("archcreatia", "pipe_wrench"), PIPE_WRENCH);
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "impure_ichor"), IMPURE_ICHOR);
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_crystal"), MANA_CRYSTAL);
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_enhanced_iron"), MANA_ENHANCED_IRON);

		//Armor Registry
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_enhanced_helmet"), MANA_HELMET);
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_enhanced_chestplate"), MANA_CHEST);
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_enhanced_leggings"), MANA_LEGS);
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_enhanced_boots"), MANA_BOOTS);



		//Block Items
//		Registry.register(Registry.ITEM, new Identifier("archcreatia", "test_block"), new BlockItem(TEST_BLOCK, new Item.Settings().group(ArchCreatia.MACHINE_GROUP)));
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_furnace"), new BlockItem(MANA_FURNACE, new Item.Settings().group(ArchCreatia.MACHINE_GROUP)));
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_tank"), new BlockItem(MANA_TANK, new Item.Settings().group(ArchCreatia.MACHINE_GROUP)));
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_pipe"), new BlockItem(MANA_PIPE, new Item.Settings().group(ArchCreatia.MACHINE_GROUP)));
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "weak_mana_condensor"), new BlockItem(WEAK_MANA_CONDENSOR, new Item.Settings().group(ArchCreatia.MACHINE_GROUP)));
		Registry.register(Registry.ITEM, new Identifier("archcreatia", "mana_condensor"), new BlockItem(MANA_CONDENSOR, new Item.Settings().group(ArchCreatia.MACHINE_GROUP)));

		//Block Registry
//		Registry.register(Registry.BLOCK, new Identifier("archcreatia", "test_block"), TEST_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier("archcreatia", "mana_furnace"), MANA_FURNACE);
		Registry.register(Registry.BLOCK, new Identifier("archcreatia", "mana_tank"), MANA_TANK);
		Registry.register(Registry.BLOCK, new Identifier("archcreatia", "mana_pipe"), MANA_PIPE);
		Registry.register(Registry.BLOCK, new Identifier("archcreatia", "weak_mana_condensor"), WEAK_MANA_CONDENSOR);
		Registry.register(Registry.BLOCK, new Identifier("archcreatia", "mana_condensor"), MANA_CONDENSOR);

		//Block Entity Registry
//		TEST_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:test_block_entity", BlockEntityType.Builder.create(TestBlockEntity::new, TEST_BLOCK).build(null));
//		MANA_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:mana_block_entity", BlockEntityType.Builder.create(ManaBlockEntity::new, MANA_BLOCK).build(null));
		MANA_FURNACE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:mana_furnace_entity", BlockEntityType.Builder.create(ManaFurnaceEntity::new, MANA_FURNACE).build(null));
		MANA_TANK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:mana_tank_entity", BlockEntityType.Builder.create(ManaTankEntity::new, MANA_TANK).build(null));
		MANA_PIPE_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:mana_pipe_entity", BlockEntityType.Builder.create(ManaPipeEntity::new, MANA_PIPE).build(null));
		WEAK_MANA_CONDENSOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:weak_mana_condensor_entity", BlockEntityType.Builder.create(WeakManaCondensorEntity::new, WEAK_MANA_CONDENSOR).build(null));
		MANA_CONDENSOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "archcreatia:mana_condensor_entity", BlockEntityType.Builder.create(ManaCondensorEntity::new, MANA_CONDENSOR).build(null));

//		//Container Registry
//		MANA_FURNACE_CONTAINER = Registry.register(Registry.CONTAINER, "archcreatia:mana_furnace_container", )
		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("archcreatia", "mana_furnace"), (syncId, identifier, player, buf) -> {
			final BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			return((ManaFurnaceEntity) blockEntity).createContainer(syncId, player.inventory);
		});
//		ContainerProviderRegistry.INSTANCE.registerFactory(ExampleBlock.ID, (syncId, id, player, buf) -> new ExampleBlockController(syncId, player.inventory, BlockContext.create(player.world, buf.readBlockPos())));

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("archcreatia", "mana_tank"), (syncId, identifier, player, buf) -> {
			final BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			return((ManaTankEntity) blockEntity).createContainer(syncId, player.inventory);
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("archcreatia", "weak_mana_condensor"), (syncId, identifier, player, buf) -> {
			final BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			return((WeakManaCondensorEntity) blockEntity).createContainer(syncId, player.inventory);
		});

		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("archcreatia", "mana_condensor"), (syncId, identifier, player, buf) -> {
			final BlockEntity blockEntity = player.world.getBlockEntity(buf.readBlockPos());
			return((ManaCondensorEntity) blockEntity).createContainer(syncId, player.inventory);
		});

		System.out.println("ArchCreatia's magic is here!");
	}
}
