package studio.robotmonkey.archcreatia.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import studio.robotmonkey.archcreatia.containers.AbstractManaCondensorContainer;
import studio.robotmonkey.archcreatia.containers.AbstractManaFurnaceContainer;
import studio.robotmonkey.archcreatia.containers.ManaCondensorContainer;
import studio.robotmonkey.archcreatia.containers.ManaFurnaceContainer;

public class ManaCondensorScreen extends ContainerScreen<ManaCondensorContainer> {

    // a path to gui texture, you may replace it with new Identifier(YourMod.MOD_ID, "textures/gui/container/your_container.png");
    private static final Identifier TEXTURE = new Identifier("archcreatia", "textures/gui/container/mana_condensor.png");

    public ManaCondensorScreen(ManaCondensorContainer container, PlayerInventory playerInventory, Text title) {
        super(container, playerInventory, title);
        this.containerHeight = 114 + 6 * 18;
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), this.containerWidth / 2F - 50F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, this.containerHeight / 2F - 39, 4210752);
    }
    int frameCount = 0;
    int renderFrame = 2;

    @Override

    protected void drawBackground(float delta, int mouseX, int mouseY) {
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.minecraft.getTextureManager().bindTexture(TEXTURE);
//        int i = (this.width - this.containerWidth) / 2;
//        int j = (this.height - this.containerHeight) / 2;
//        this.blit(i, j, 0, 0, this.containerWidth, 6 * 18 + 17);
//        this.blit(i, j + 6 * 18 + 17, 0, 126, this.containerWidth, 96);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(this.TEXTURE);
        int i = this.x;
        int j = this.y;
        this.blit(i, j, 0, 0, this.containerWidth, this.containerHeight);

//        if (((AbstractManaCondensorContainer)this.container).isBurning()) {
//            l = ((AbstractManaCondensorContainer)this.container).getFuelProgress();
//            this.blit(i + 40, j + 36 + 12 - l, 176, 12 - l, 14, l + 1);
//        }
//
//        System.out.println(this.container.getManaCount());
//        System.out.println(this.container.getMaxMana());
//        System.out.println(this.container.getManaPercent());

        float l = ((AbstractManaCondensorContainer)this.container).getCookProgress();
//        System.out.println(l);
        this.blit(i + 74, j + 34, 176, 14, (int)(l * 23), 16);
        int height = (int)((this.container.getManaPercent()) * 50F);
        this.blit(i + 43 , j + 67 - height, 176, 81 - height, 18, height);
        this.blit(i + 43 , j + 17, 176, 82, 18, 50);
    }
}