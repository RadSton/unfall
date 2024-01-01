package io.radston12.reddefense.commands.map;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MapUtils {

    public static final int[] AVALABLE_COLORS = new int[] {0x7fb238, 0xf7e9a3, 0xc7c7c7, 0xff0000, 0xa0a0ff, 0xa7a7a7, 0x7c00, 0xffffff, 0xa4a8b8, 0x976d4d, 0x707070, 0x4040ff, 0x8f7748, 0xfffcf5, 0xd87f33, 0xb24cd8, 0x6699d8, 0xe5e533, 0x7fcc19, 0xf27fa5, 0x4c4c4c, 0x999999, 0x4c7f99, 0x7f3fb2, 0x334cb2, 0x664c33, 0x667f33, 0x993333, 0x191919, 0xfaee4d, 0x5cdbd5, 0x4a80ff, 0xd93a, 0x815631, 0x700200, 0xd1b1a1, 0x9f5224, 0x95576c, 0x706c8a, 0xba8524, 0x677535, 0xa04d4e, 0x392923, 0x876b62, 0x575c5c, 0x7a4958, 0x4c3e5c, 0x4c3223, 0x4c522a, 0x8e3c2e, 0x251610, 0xbd3031, 0x943f61, 0x5c191d, 0x167e86, 0x3a8e8c, 0x562c3e, 0x14b485, 0x646464, 0xd8af93, 0x7fa796};

    private static URL checkURL(String url) {
        try {
            return new URL(url).toURI().toURL();
        } catch (Exception e) {
            return null;
        }
    }

    private static BufferedImage getImage(String link) {
        URL url = checkURL(link);

        if (url == null) return null;

        try {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Minecarft Forge");
            connection.connect();
            return ImageIO.read(connection.getInputStream());
        } catch (Throwable e) {
            return null;
        }
    }

    public static int[] createMap(String path, int sizeX, int sizeY, ServerLevel level) {
        BufferedImage img = getImage(path);

        if(img == null) return new int[] { -2 };
        if(img.getWidth() != img.getHeight()) return new int[] { -1 };


        return renderToMapIds(img, sizeX, sizeY, level);
    }


    public static int[] renderToMapIds(BufferedImage image, int width, int height, ServerLevel level) {
        Image resizedImage = image.getScaledInstance(width * 128, height * 128, Image.SCALE_DEFAULT);
        BufferedImage resized = convertToBufferedImage(resizedImage);

        List<List<MapItemSavedData>> maps = new ArrayList<List<MapItemSavedData>>();

        for (int sizeY = 0; sizeY < height; sizeY++) {
            maps.add(new ArrayList<MapItemSavedData>());
            for (int sizeX = 0; sizeX < width; sizeX++) {
                MapItemSavedData data = MapItemSavedData.createFresh(0,0, (byte) 0, false, false, ResourceKey.create(Registries.DIMENSION, new ResourceLocation("unfall_img")));

                for (int x = 0; x < 128; x++) {
                    for (int y = 0; y < 128; y++) {

                        data.colors[x + (y * 128)] = (byte) (findClosestColorId(resized.getRGB(x + (sizeX * 128),y + (sizeY * 128))) << 2 | 255 & 3);

                    }
                }

                maps.get(sizeY).add(data);
            }
        }

        int[] store = new int[width * height];

        int a = 0;
        for (List<MapItemSavedData> datas: maps) {
            for(MapItemSavedData data : datas) {
                final int freeMap = level.getFreeMapId();
                level.setMapData(MapItem.makeKey(freeMap), data);
                store[a] = freeMap;
                a++;
            }
        }

        return store;
    }
/*
    public static List<ItemStack> toVanillaItems(CanvasImage image, ServerWorld world, String url) {
        var xSections = MathHelper.ceil(image.getWidth() / 128d);
        var ySections = MathHelper.ceil(image.getHeight() / 128d);

        var xDelta = (xSections * 128 - image.getWidth()) / 2;
        var yDelta = (ySections * 128 - image.getHeight()) / 2;

        var items = new ArrayList<ItemStack>();

        for (int ys = 0; ys < ySections; ys++) {
            for (int xs = 0; xs < xSections; xs++) {
                var id = world.getNextMapId();
                var state = MapState.of(0, 0, (byte) 0, false, false, RegistryKey.of(RegistryKeys.WORLD, new Identifier("image2map", "generated")));

                for (int xl = 0; xl < 128; xl++) {
                    for (int yl = 0; yl < 128; yl++) {
                        var x = xl + xs * 128 - xDelta;
                        var y = yl + ys * 128 - yDelta;

                        if (x >= 0 && y >= 0 && x < image.getWidth() && y < image.getHeight()) {
                            state.colors[xl + yl * 128] = image.getRaw(x, y);
                        }
                    }
                }

                world.putMapState(FilledMapItem.getMapName(id), state);

                var stack = new ItemStack(Items.FILLED_MAP);
                stack.getOrCreateNbt().putInt("map", id);
                var lore = new NbtList();
                lore.add(NbtString.of(Text.Serializer.toJson(Text.literal(xs + " / " + ys).formatted(Formatting.GRAY))));
                lore.add(NbtString.of(Text.Serializer.toJson(Text.literal(url))));
                stack.getOrCreateNbt().putInt("image2map:x", xs);
                stack.getOrCreateNbt().putInt("image2map:y", ys);
                stack.getOrCreateNbt().putInt("image2map:width", xSections);
                stack.getOrCreateNbt().putInt("image2map:height", ySections);
                stack.getOrCreateSubNbt("display").put("Lore", lore);
                items.add(stack);
            }
        }

        return items;
    }

    private static int mapColorToRGBColor(CanvasColor color) {
        var mcColor = color.getRgbColor();
        double[] mcColorVec = { (double) ColorHelper.Argb.getRed(mcColor), (double) ColorHelper.Argb.getGreen(mcColor), (double) ColorHelper.Argb.getBlue(mcColor) };
        double coeff = shadeCoeffs[color.getColor().id & 3];
        return ColorHelper.Argb.getArgb(0, (int) (mcColorVec[0] * coeff), (int) (mcColorVec[1] * coeff), (int) (mcColorVec[2] * coeff));
    }

    private static CanvasColor floydDither(int[][] pixels, int x, int y, int imageColor) {
        var closestColor = CanvasUtils.findClosestColorARGB(imageColor);
        var palletedColor = mapColorToRGBColor(closestColor);

        var errorR = ColorHelper.Argb.getRed(imageColor) - ColorHelper.Argb.getRed(palletedColor);
        var errorG = ColorHelper.Argb.getGreen(imageColor) - ColorHelper.Argb.getGreen(palletedColor);
        var errorB = ColorHelper.Argb.getBlue(imageColor) - ColorHelper.Argb.getBlue(palletedColor);
        if (pixels[0].length > x + 1) {
            pixels[y][x + 1] = applyError(pixels[y][x + 1], errorR, errorG, errorB, 7.0 / 16.0);
        }
        if (pixels.length > y + 1) {
            if (x > 0) {
                pixels[y + 1][x - 1] = applyError(pixels[y + 1][x - 1], errorR, errorG, errorB, 3.0 / 16.0);
            }
            pixels[y + 1][x] = applyError(pixels[y + 1][x], errorR, errorG, errorB, 5.0 / 16.0);
            if (pixels[0].length > x + 1) {
                pixels[y + 1][x + 1] = applyError(pixels[y + 1][x + 1], errorR, errorG, errorB, 1.0 / 16.0);
            }
        }

        return closestColor;
    }

    private static int applyError(int pixelColor, int errorR, int errorG, int errorB, double quantConst) {
        int pR = clamp( ColorHelper.Argb.getRed(pixelColor) + (int) ((double) errorR * quantConst), 0, 255);
        int pG = clamp(ColorHelper.Argb.getGreen(pixelColor) + (int) ((double) errorG * quantConst), 0, 255);
        int pB = clamp(ColorHelper.Argb.getBlue(pixelColor) + (int) ((double) errorB * quantConst), 0, 255);
        return ColorHelper.Argb.getArgb(ColorHelper.Argb.getAlpha(pixelColor), pR, pG, pB);
    }

    private static int clamp(int i, int min, int max) {
        if (min > max)
            throw new IllegalArgumentException("max value cannot be less than min value");
        if (i < min)
            return min;
        if (i > max)
            return max;
        return i;
    }

    private static int[][] convertPixelArray(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();

        int[][] result = new int[height][width];
        final int pixelLength = 4;
        for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
            int rgb = 0;
            rgb += ((int) pixels[pixel + 1] & 0xff); // blue
            rgb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
            rgb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
            result[row][col] = rgb;
            col++;
            if (col == width) {
                col = 0;
                row++;
            }
        }

        return result;
    }
*/
    private static BufferedImage convertToBufferedImage(Image image) {
        BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return newImage;
    }

    public static int findClosestColorId(int targetColor) {
        int minDifference = Integer.MAX_VALUE;
//        int closestColor = 0;
        int closestColorId = -1;

        int count = 0;

        for (int color : AVALABLE_COLORS) {
            int difference = calculateColorDifference(targetColor, color);
                count++;
            if (difference < minDifference) {
                minDifference = difference;
//                closestColor = color;
                closestColorId = count;
            }
        }

        return closestColorId;
    }

    private static int calculateColorDifference(int color1, int color2) {
        int red1 = (color1 >> 16) & 0xFF;
        int green1 = (color1 >> 8) & 0xFF;
        int blue1 = color1 & 0xFF;

        int red2 = (color2 >> 16) & 0xFF;
        int green2 = (color2 >> 8) & 0xFF;
        int blue2 = color2 & 0xFF;

        int redDiff = red1 - red2;
        int greenDiff = green1 - green2;
        int blueDiff = blue1 - blue2;

        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }

}
