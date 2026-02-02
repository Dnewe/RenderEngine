package screen;

import config.Config;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Screen {
    private static Screen screen;

    private Color[][] pixels;
    private int screenWidth;
    private int screenHeight;
    private WritableImage img;
    private PixelWriter writer;
    private double resolutionRatio;
    private int resolutionWidth; 
    private int resolutionHeight;
    private Color[][] previousPixels;
    private GraphicsContext gc;

    private Screen(double screenWidth, double screenHeight, double resolutionRatio) {
        this.screenWidth = (int) screenWidth;
        this.screenHeight = (int) screenHeight;
        this.resolutionRatio = resolutionRatio;
        this.resolutionWidth = (int)(screenWidth * resolutionRatio);
        this.resolutionHeight = (int)(screenHeight * resolutionRatio);
        img = new WritableImage(this.screenWidth, this.screenHeight);
        writer = img.getPixelWriter();
        pixels = new Color[resolutionWidth][resolutionHeight];
        previousPixels = new Color[resolutionWidth][resolutionHeight];
        gc = AppIHM.getCanvas().getGraphicsContext2D();
        
        resetPixels();
    } 

    private Screen() {
        this(Config.DEFAULT_SCREEN_WIDTH, Config.DEFAULT_SCREEN_HEIGHT, Config.RESOLUTION_RATIO);
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


    public int getWidth() {
        return resolutionWidth;
    }

    public int getHeight() {
        return resolutionHeight;
    }

    public synchronized Color[][] getPixels() {
        return pixels;
    }


    public static void resizeDisplay(double screenWidth, double screenHeight) {
        screen = new Screen(screenWidth, screenHeight, Config.RESOLUTION_RATIO);
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
        for (int i=0; i< screenWidth; i+= 1) {
            int x = (int)(i*resolutionRatio)<= resolutionWidth-1? (int)(i*resolutionRatio) : resolutionWidth-1;
            for (int j=0; j< screenHeight; j+= 1) {
                int y = (int)(j*resolutionRatio)<= resolutionHeight-1?  (int)(j*resolutionRatio) : resolutionHeight-1;
                if (pixels[x][y] != previousPixels[x][y] || drawAll) {
                    writer.setColor(i, j, pixels[x][y]);
                    //previousPixels[x][y] = pixels[x][y];
                }
            }
        }
        Platform.runLater(() -> gc.drawImage(img, 0, 0));
    } 


    private void postProcess() {
            for (int x=0; x< pixels.length; x++) {
                for (int y=0; y< pixels[x].length; y++) {
                    double r=0;
                    double g=0;
                    double b=0;
                    for (int i=-1; i<2; i++) {
                        for (int j=-1; j<2; j++) {
                            double ratio = 1./9;
                            ratio *= i==0? 1 : 1 ;
                            ratio *= j==0? 1 : 1 ;
                            int posX = x+i;
                            int posY = y+i;
                            posX = posX <0 || posX>=pixels.length? x : posX;
                            posY = posY <0 || posY>=pixels[x].length? y : posY;
                            r += ratio*pixels[posX][posY].getRed();
                            g += ratio*pixels[posX][posY].getGreen();
                            b += ratio*pixels[posX][posY].getBlue();
                        }
                    }
                    r = r>1? 1:r;
                    g = g>1? 1:g;
                    b = b>1? 1:b;
                    pixels[x][y] = new Color(r,g,b,1);
                    //pixelWriter.setColor(x, y, pixels[x][y]);
                    previousPixels[x][y] = pixels[x][y];
                }
            }
    }

    public void draw() {
        
        //Platform.runLater(() -> {
        draw(false);
        //postProcess();
        //});
    }
}
