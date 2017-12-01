package cz.stechy.clickbot.actions;

import cz.stechy.clickbot.IRobotController;
import cz.stechy.clickbot.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

public class CheckScreen implements Consumer<IRobotController> {

    private final String imagePath;
    private final BufferedImage originalImage;
    private final Point coordinates;
    private final int width;
    private final int height;

    public CheckScreen(String imagePath, Point coordinates, int width, int height) {
        BufferedImage tmp = null;
        try {
            tmp = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            tmp = null;
        } finally {
            this.originalImage = tmp;
        }
        this.coordinates = coordinates;
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
    }

    @Override
    public void accept(IRobotController robot) {
        try {
            final BufferedImage capture = robot.createScreenCapture(coordinates.x, coordinates.y, width, height);
            ByteArrayOutputStream baus = new ByteArrayOutputStream();
            ImageIO.write(capture, "png", baus);
            ByteArrayInputStream bais = new ByteArrayInputStream(baus.toByteArray());
            final BufferedImage image = ImageIO.read(bais);
            final float percentage = compareImage(originalImage, image);
            if (percentage < 50) {
                System.out.println("Obrazky se neshoduji, koncim");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Porovná dva obrázky a vrátí jejich podobnost vyjádřenou v procentech
     *
     * @param biA První obrázek
     * @param biB Druhý obrázek
     * @return Podobnost obrázků v procentech
     */
    public float compareImage(BufferedImage biA, BufferedImage biB) {
        float percentage = 0;
        try {
            // take buffer data from both image files //
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                    }

                }
                percentage = (count * 100) / sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println("Failed to compare image files ...");
            e.printStackTrace();
        }
        return percentage;
    }

    @Override
    public String toString() {
        return "CheckScreen: " + imagePath + " na souradnicich: " + coordinates;
    }
}
