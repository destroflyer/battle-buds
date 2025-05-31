package com.destroflyer.battlebuds.client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileAssets {

    public static String ROOT;

    public static BufferedImage getImage(String filePath) {
        try {
            return ImageIO.read(new File(ROOT + filePath));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean exists(String filePath) {
        return new File(ROOT + filePath).exists();
    }
}
