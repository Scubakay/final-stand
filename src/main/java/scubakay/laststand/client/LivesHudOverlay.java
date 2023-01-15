package scubakay.laststand.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import scubakay.laststand.LastStand;
import scubakay.laststand.util.IEntityDataSaver;

/**
 * Draws HUD overlay for lives
 */
public class LivesHudOverlay implements HudRenderCallback {
    private static final Identifier LIFE = new Identifier(LastStand.MOD_ID, "textures/lives/life.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if(client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;
        }
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, LIFE);

        for(int i = 0; i < 10; i++) {
            if(((IEntityDataSaver) MinecraftClient.getInstance().player).getPersistentData().getInt("lives") > i) {
                DrawableHelper.drawTexture(matrixStack, x - 94 + (i * 9), y - 54, x, x, 12, 12, 12, 12);
            } else {
                break;
            }
        }
    }
}
