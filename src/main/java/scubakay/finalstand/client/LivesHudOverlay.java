package scubakay.finalstand.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.event.callback.HotbarRenderCallback;
import scubakay.finalstand.util.IAbstractClientPlayerEntityMixin;
import scubakay.finalstand.util.IEntityDataSaver;

/**
 * Draws HUD overlay for lives
 */
public class LivesHudOverlay implements HotbarRenderCallback {
    private static final Identifier LIFE_GREEN = new Identifier(FinalStand.MOD_ID, "textures/lives/life-green.png");
    private static final Identifier LIFE_YELLOW = new Identifier(FinalStand.MOD_ID, "textures/lives/life-yellow.png");
    private static final Identifier LIFE_RED = new Identifier(FinalStand.MOD_ID, "textures/lives/life-red.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null || !((IAbstractClientPlayerEntityMixin) player).fs_isSurvival()){
            return;
        }

        int x = 0;
        int y = 0;
        MinecraftClient client = MinecraftClient.getInstance();
        if(client != null) {
            int width = client.getWindow().getScaledWidth();
            int height = client.getWindow().getScaledHeight();

            x = width / 2;
            y = height;
        }

        int lives = ((IEntityDataSaver) player).fs_getPersistentData().getInt("lives");
        if (lives > 0) {
            drawHeart(matrixStack, x, y, lives);
            drawAmount(matrixStack, x, y, lives);
        }
    }

    private static void drawAmount(MatrixStack matrixStack, int x, int y, int lives) {
        Text text = Text.literal("" + lives).formatted(Formatting.BOLD);
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int width = renderer.getWidth(text) / 2;
        renderer.drawWithShadow(matrixStack, text, x - width, y - 56, 0xffffff);
    }

    private static void drawHeart(MatrixStack matrixStack, int x, int y, int lives) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (lives > 2) {
            RenderSystem.setShaderTexture(0, LIFE_GREEN);
        } else if (lives < 2) {
            RenderSystem.setShaderTexture(0, LIFE_RED);
        } else {
            RenderSystem.setShaderTexture(0, LIFE_YELLOW);
        }
        int zOffset = -90;
        DrawableHelper.drawTexture(matrixStack, x - 12, y - 64, zOffset, 0, 0, 24, 24, 24, 24);
    }
}
