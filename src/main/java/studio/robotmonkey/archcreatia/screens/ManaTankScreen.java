package studio.robotmonkey.archcreatia.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import studio.robotmonkey.archcreatia.containers.ManaFurnaceContainer;
import studio.robotmonkey.archcreatia.containers.ManaTankContainer;

public class ManaTankScreen extends ContainerScreen<ManaTankContainer> {

    // a path to gui texture, you may replace it with new Identifier(YourMod.MOD_ID, "textures/gui/container/your_container.png");
    private static final Identifier TEXTURE = new Identifier("archcreatia", "textures/gui/container/mana_tank.png");

    public ManaTankScreen(ManaTankContainer container, PlayerInventory playerInventory, Text title) {
        super(container, playerInventory, title);
        this.containerHeight = 114 + 6 * 18;
    }

    @Override
    protected void drawForeground(int mouseX, int mouseY) {
        this.font.draw(this.title.asFormattedString(), this.containerWidth / 2F - 25F, 6.0F, 4210752);
        this.font.draw(this.playerInventory.getDisplayName().asFormattedString(), 8.0F, this.containerHeight / 2F - 39, 4210752);
    }

    @Override

    protected void drawBackground(float delta, int mouseX, int mouseY) {
        int manaHeight = 0;
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
//
//        System.out.println(this.container.getManaCount());
//        System.out.println(this.container.getMaxMana());
//        System.out.println(this.container.getManaPercent());
        manaHeight = (int)((this.container.getManaPercent()) * 50F);
        //This draws the mana amount
        this.blit(i + 79 , j + 68 - manaHeight, 176, 81 - manaHeight, 18, manaHeight);
        //Draws the tank lines on top
        this.blit(i + 79 , j + 18, 176, 82, 18, 50);
    }
}