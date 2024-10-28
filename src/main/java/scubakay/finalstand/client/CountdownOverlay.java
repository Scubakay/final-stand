package scubakay.finalstand.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.time.DurationFormatUtils;
import scubakay.finalstand.event.callback.HotbarRenderCallback;

@Environment(EnvType.CLIENT)
public class CountdownOverlay implements HotbarRenderCallback {
    private static int sessionTicksLeft = -1;

    public static void setSessionTicksLeft(int ticks) {
        sessionTicksLeft = ticks;
    }

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        if (sessionTicksLeft > 0) {
            drawCountdownText(context, ticksToFormattedTime(sessionTicksLeft));
        }
    }

    private static String ticksToFormattedTime(int ticks) {
        int milliseconds = ticks / 20 * 1000;
        String time = DurationFormatUtils.formatDuration(milliseconds, "HH:mm:ss");
        // Remove leading 00:
        while(time.startsWith("00:")) {
            time = time.replace("00:", "");
        }
        // Remove leading 0, but not if it is 0
        time = time.replaceFirst ("^0*", "");
        if (time.isEmpty()) {
            time = "0";
        }
        return time;
    }

    private static void drawCountdownText(DrawContext context, String inputString) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;

        Text text = Text.literal(inputString).formatted(Formatting.BOLD, Formatting.GREEN);
        int x = client.getWindow().getScaledWidth() - 20 - renderer.getWidth(text);
        int y = client.getWindow().getScaledHeight() - 20;

        context.drawTextWithShadow(renderer, text, x, y, 0xffffff);
    }
}
