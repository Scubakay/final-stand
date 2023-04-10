package scubakay.finalstand.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.time.DurationFormatUtils;
import scubakay.finalstand.event.callback.HotbarRenderCallback;
import scubakay.finalstand.event.handler.SessionHandler;

@Environment(EnvType.CLIENT)
public class CountdownOverlay implements HotbarRenderCallback {
    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        int y = 10;
        int sessionTicksLeft = SessionHandler.getSessionTicksLeft();
        if (sessionTicksLeft > 0) {
            drawCountdownText(matrixStack, y, "Time left: " + ticksToFormattedTime(sessionTicksLeft));
            y += 10;
        }
        int hunterTicksLeft = SessionHandler.getHunterTicksLeft();
        if (hunterTicksLeft > 0) {
            drawCountdownText(matrixStack, y, "Hunter selection: " + ticksToFormattedTime(hunterTicksLeft));
            y += 10;
        }
        int chestTicksLeft = SessionHandler.getChestTicksLeft();
        if (chestTicksLeft > 0) {
            drawCountdownText(matrixStack, y, "Treasure chest: " + ticksToFormattedTime(chestTicksLeft));
        }
    }

    private static String ticksToFormattedTime(int ticks) {
        int milliseconds = ticks / 20 * 1000;
        return DurationFormatUtils.formatDuration(milliseconds, "HH:mm:ss").replace("00:","");
    }

    private static void drawCountdownText(MatrixStack matrixStack, int y, String inputString) {
        int x = 10;
        Text text = Text.literal(inputString).formatted(Formatting.BOLD);
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        renderer.drawWithShadow(matrixStack, text, x, y, 0xffffff);
    }
}
