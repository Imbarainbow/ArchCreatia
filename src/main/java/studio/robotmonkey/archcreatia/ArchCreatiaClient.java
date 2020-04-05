package studio.robotmonkey.archcreatia;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import studio.robotmonkey.archcreatia.blocks.blockEntities.TestEntityRenderer;
import studio.robotmonkey.archcreatia.containers.ManaCondensorContainer;
import studio.robotmonkey.archcreatia.containers.ManaFurnaceContainer;
import studio.robotmonkey.archcreatia.containers.ManaTankContainer;
import studio.robotmonkey.archcreatia.screens.ManaFurnaceScreen;
import studio.robotmonkey.archcreatia.screens.ManaTankScreen;
import studio.robotmonkey.archcreatia.screens.ManaCondensorScreen;

//import static studio.robotmonkey.archcreatia.ArchCreatia.MANA_BLOCK_ENTITY;
import static studio.robotmonkey.archcreatia.ArchCreatia.TEST_BLOCK_ENTITY;

public class ArchCreatiaClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(TEST_BLOCK_ENTITY, TestEntityRenderer::new);
//        BlockEntityRendererRegistry.INSTANCE.register(MANA_BLOCK_ENTITY, ManaBlockEntityRenderer::new);
        ScreenProviderRegistry.INSTANCE.<ManaFurnaceContainer>registerFactory(new Identifier("archcreatia", "mana_furnace"), (container) -> new ManaFurnaceScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("container.archcreatia.mana_furnace")));
        ScreenProviderRegistry.INSTANCE.<ManaTankContainer>registerFactory(new Identifier("archcreatia", "mana_tank"), (container) -> new ManaTankScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("container.archcreatia.mana_tank")));
        ScreenProviderRegistry.INSTANCE.<ManaCondensorContainer>registerFactory(new Identifier("archcreatia", "weak_mana_condensor"), (container) -> new ManaCondensorScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("container.archcreatia.weak_mana_condensor")));
        ScreenProviderRegistry.INSTANCE.<ManaCondensorContainer>registerFactory(new Identifier("archcreatia", "mana_condensor"), (container) -> new ManaCondensorScreen(container, MinecraftClient.getInstance().player.inventory, new TranslatableText("container.archcreatia.mana_condensor")));

        BlockRenderLayerMap.INSTANCE.putBlock(ArchCreatia.MANA_PIPE, RenderLayer.getTranslucent());
    }
}
