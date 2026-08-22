package ycpk.sculkandjaw.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class DebugTool extends Item {
    private static final long COLOR_CYCLE = 2000L;
    private static final long SPACE_CYCLE = 2000L;
    private static final int SPACE_COUNT = -1;

    public DebugTool(Properties properties) {
        super(properties);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        long time = System.currentTimeMillis();
        String text = Component.translatable("item.ycpk.sculk_and_jaw_debug_tool").getString();
        int length = text.length();
        if (length == 0) {
            return Component.empty();
        }
        int G = length - 1; // 间隙数
        int S = (SPACE_COUNT < 0) ? G : SPACE_COUNT;
        if (S < 0) S = 0;
        float colorProgress = (time % COLOR_CYCLE) / (float) COLOR_CYCLE;
        int startHue = (int) (colorProgress * 360);
        int endHue = (startHue + 120) % 360;
        int startColor = Color.HSBtoRGB(startHue / 360.0f, 1.0f, 1.0f);
        int endColor = Color.HSBtoRGB(endHue / 360.0f, 1.0f, 1.0f);
        float spaceProgress = (time % SPACE_CYCLE) / (float) SPACE_CYCLE; // 0~1
        int totalSteps = 2 * (S + G);
        int t = Math.round(spaceProgress * totalSteps);
        t = Math.min(t, totalSteps);
        int leftSpaces = 0;
        int rightSpaces = 0;
        boolean[] gapFilled = new boolean[G];
        if (t <= S) {
            int filled = t;
            for (int i = G - filled; i < G; i++) gapFilled[i] = true;
            rightSpaces = S - t;
        }
        else if (t <= S + G) {
            int left = t - S;
            int remaining = S + G - t;
            for (int i = G - remaining; i < G; i++) gapFilled[i] = true;
            leftSpaces = left;
        }
        else if (t <= S + G + S) {
            int left = S - (t - (S + G));
            int filled = t - (S + G);
            for (int i = 0; i < filled; i++) gapFilled[i] = true;
            leftSpaces = left;
        }
        else {
            int right = t - (S + G + S);
            int filled = G - right;
            for (int i = 0; i < filled; i++) gapFilled[i] = true;
            rightSpaces = right;
        }
        MutableComponent result = Component.empty();
        for (int i = 0; i < leftSpaces; i++) {
            result.append(Component.literal(" ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA))));
        }
        for (int i = 0; i < length; i++) {
            float colorPos = (length == 1) ? 0 : (float) i / (length - 1);
            int r = (int) ((startColor >> 16 & 0xFF) * (1 - colorPos) + (endColor >> 16 & 0xFF) * colorPos);
            int g = (int) ((startColor >> 8 & 0xFF) * (1 - colorPos) + (endColor >> 8 & 0xFF) * colorPos);
            int b = (int) ((startColor & 0xFF) * (1 - colorPos) + (endColor & 0xFF) * colorPos);
            int charColor = (r << 16) | (g << 8) | b;
            result.append(Component.literal(String.valueOf(text.charAt(i))).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(charColor))));
            if (i < G && gapFilled[i]) {
                result.append(Component.literal(" ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA))));
            }
        }
        for (int i = 0; i < rightSpaces; i++) {
            result.append(Component.literal(" ").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xAAAAAA))));
        }
        return result;
    }
}
