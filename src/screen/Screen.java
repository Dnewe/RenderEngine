package screen;

import config.Config;
import javafx.application.Platform;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;

public class Screen {
    private static Screen screen;

    private Color[][] pixels;
    private double screenWidth; 
    private double screenHeight;
    private Color[][] previousPixels;
    private PixelWriter pixelWriter;

    private Screen(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        pixels = new Color[(int)screenWidth][(int)screenHeight];
        previousPixels = new Color[(int)screenWidth][(int)screenHeight];
        pixelWriter = AppIHM.getCanvas().getGraphicsContext2D().getPixelWriter();
        
        resetPixels();
    }

    private Screen() {
        this(Config.DEFAULT_SCREEN_WIDTH, Config.DEFAULT_SCREEN_HEIGHT);
    }

    public static Screen getScreen() {
        if (screen == null) {
            screen = new Screen();
        }
        return screen;
    }


    private void resetPixels() {
        for (int i=0; i< pixels.length; i++) {
            for (int j=0; j< pixels[i].length; j++) {
                pixels[i][j] = Color.BLACK;
            }
        }
    }


    public double getWidth() {
        return screenWidth;
    }

    public double getHeight() {
        return screenHeight;
    }

    public synchronized Color[][] getPixels() {
        return pixels;
    }


    public static void resizeDisplay(double screenWidth, double screenHeight) {
        screen = new Screen(screenWidth, screenHeight);
        //screenWidth = AppIHM.canvas.getWidth();
        //screenHeight = AppIHM.canvas.getHeight();
        //pixels = new Color[(int)screenWidth][(int)screenHeight];
        //previousPixels = new Color[(int)screenWidth][(int)screenHeight];
        //resetPixels();
        //pixels = new Color[((int)AppIHM.canvas.getWidth())][(int)AppIHM.canvas.getHeight()];
        //draw(true);
        // NEEDS TO MERGE COLOR BEFORE CREATING NEW PIXELS
    }


    public void draw(boolean drawAll) {
        Platform.runLater(() -> {
            for (int i=0; i< pixels.length; i++) {
                for (int j=0; j< pixels[i].length; j++) {
                    if (pixels[i][j] != previousPixels[i][j] || drawAll) {
                        pixelWriter.setColor(i, j, pixels[i][j]);
                        previousPixels[i][j] = pixels[i][j];
                    }
                }
            }
        });
    } 

    public void draw() {
        draw(false);
    }
}
