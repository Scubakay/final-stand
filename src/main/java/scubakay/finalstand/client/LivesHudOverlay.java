package scubakay.finalstand.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import scubakay.finalstand.FinalStand;
import scubakay.finalstand.event.callback.HotbarRenderCallback;
import scubakay.finalstand.util.IAbstractClientPlayerEntityMixin;

/**
 * Draws HUD overlay for lives
 */
public class LivesHudOverlay implements HotbarRenderCallback {
    private static final Identifier LIFE_GREEN = Identifier.of(FinalStand.MOD_ID, "textures/lives/life-green.png");
    private static final Identifier LIFE_YELLOW = Identifier.of(FinalStand.MOD_ID, "textures/lives/life-yellow.png");
    private static final Identifier LIFE_RED = Identifier.of(FinalStand.MOD_ID, "textures/lives/life-red.png");

    private static int lives = -1;

    public static void setLives(int amount) {
        lives = amount;
    }

    public static int getLives() {
        return lives;
    }

    @Override
    public void onHudRender(DrawContext context) {
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

        if (lives > 0) {
            drawHeart(context, x, y, lives);
            drawAmount(context, x, y, lives);
        }
    }

    private static void drawAmount(DrawContext context, int x, int y, int lives) {
        Text text = Text.literal("" + lives).formatted(Formatting.BOLD);
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int width = renderer.getWidth(text) / 2;
        context.drawTextWithShadow(renderer, text, x - width, y - 56, 0xffffff);
    }

    private static void drawHeart(DrawContext context, int x, int y, int lives) {
        int xOffset = -12;
        int yOffset = -64;

        Identifier texture;
        if (lives > 2) {
            texture = LIFE_GREEN;
        } else if (lives < 2) {
            texture = LIFE_RED;
        } else {
            texture = LIFE_YELLOW;
        }

        context.drawTexture(RenderLayer::getGuiTextured, texture, x + xOffset, y + yOffset, 0, 0, 24, 24, 24, 24);
    }
}
