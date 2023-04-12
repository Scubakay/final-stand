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
        int sessionTicksLeft = SessionHandler.getSessionTicksLeft();
        if (sessionTicksLeft > 0) {
            drawCountdownText(matrixStack, ticksToFormattedTime(sessionTicksLeft));
        }
    }

    private static String ticksToFormattedTime(int ticks) {
        int milliseconds = ticks / 20 * 1000;
        return DurationFormatUtils.formatDuration(milliseconds, "HH:mm:ss").replace("00:","");
    }

    private static void drawCountdownText(MatrixStack matrixStack, String inputString) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;

        Text text = Text.literal(inputString).formatted(Formatting.BOLD, Formatting.GREEN);
        int x = client.getWindow().getScaledWidth() - 20 - renderer.getWidth(text);
        int y = client.getWindow().getScaledHeight() - 20;

        renderer.drawWithShadow(matrixStack, text, x, y, 0xffffff);
    }
}
