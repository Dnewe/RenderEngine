package raytracing;


import javafx.scene.paint.Color;
import screen.Screen;
//import utils.Timer;
import voxel.VoxelArray;
import voxel.VoxelRepresentation;
import world.World;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RayTracer {

    private static RayTracer rayTracer;
    private ExecutorService executor;

    private Screen screen;
    private VoxelArray voxelArray;
    private Color[][] pixels;
    private long[][][] voxels;
    private double[][][][] voxelsBrightnessCache;
    private ArrayList<int[]> lights;

    // timer
    //private Timer timer = new Timer();

    
    // camera variables
    private double cameraX;
    private double cameraY;
    private double cameraZ;
    private double theta;
    private double phi;
    private double scale;
    private double aspectRatio;
    private double[] forward;
    private double[] right;
    private double[] up;
    // CONSTANT
    private final double EPSILON = 1e-8;
    private double maxRadius = 128;
    private final Color VOIDCOLOR = Color.BLACK;
    private final double BRIGHTNESS_THRESHOLD = 0.001;  // (1/BRIGHTNESS_THRESHOLD)*100% of brightness
    private final int TILE_SIZE = 30; // Size of tiles to be processed by each thread


    private RayTracer() {
        screen = Screen.getScreen();
        pixels = screen.getPixels();
        voxelArray = World.getWorld().getVoxelArray();
        voxels = voxelArray.getVoxels();
        voxelsBrightnessCache = new double[voxels.length][voxels[0].length][voxels[0][0].length][];
        lights = voxelArray.getLights();
        executor = Executors.newFixedThreadPool(6);
    }

    public static RayTracer getRayTracer() {
        if (rayTracer == null) {
            rayTracer = new RayTracer();
        }
        return rayTracer;
    }


    public void run(Camera camera, int numThreads) {
        screen = Screen.getScreen();
        pixels = screen.getPixels();
        List<Future<?>> futures = new ArrayList<>();

        cameraX = camera.getX();
        cameraY = camera.getY();
        cameraZ = camera.getZ();
        theta = camera.getTheta();
        phi = camera.getPhi();
        scale = Math.tan(camera.getHFov() / 2);
        aspectRatio = (double) screen.getHeight() / screen.getWidth();
        forward = new double[]{Math.cos(theta) * Math.cos(phi), Math.sin(theta), Math.cos(theta) * Math.sin(phi)};  
        right = new double[]{-Math.sin(phi), 0, Math.cos(phi)};
        up = new double[]{-Math.sin(theta) * Math.cos(phi), Math.cos(theta), -Math.sin(theta) * Math.sin(phi)};
        int x=0;
        int y=0;
        for (x=0; x < screen.getWidth() - TILE_SIZE+1; x += TILE_SIZE) {
            for (y=0; y < (int)screen.getHeight()- TILE_SIZE+1; y += TILE_SIZE) {
                int finalX = x;
                int finalY = y;
                
                Future<?> future = executor.submit(() -> new TileRenderer(finalX, finalY, TILE_SIZE, TILE_SIZE).renderTile());
                futures.add(future);
            }
        }

        
        int remainingTileSizeX = (screen.getWidth()-(x));
        int remainingTileSizeY = (screen.getHeight()-(y));
        if (remainingTileSizeX>0) {
            for (y=0; y < screen.getHeight()- TILE_SIZE+1; y += TILE_SIZE) {
                int finalX = x;
                int finalY = y;

                Future<?> future = executor.submit(() -> new TileRenderer(finalX, finalY, remainingTileSizeX, TILE_SIZE).renderTile());
                futures.add(future);
            }
        } 
        if (remainingTileSizeY>0) {
            for (x=0; x < screen.getWidth() - TILE_SIZE+1; x += TILE_SIZE) {
                int finalX = x;
                int finalY = y;

                Future<?> future = executor.submit(() -> new TileRenderer(finalX, finalY, TILE_SIZE, remainingTileSizeY).renderTile());
                futures.add(future);
            }
        }
        if (remainingTileSizeX>0 && remainingTileSizeY>0) {
            int finalX = x;
            int finalY = y;
            Future<?> future = executor.submit(() -> new TileRenderer(finalX, finalY, remainingTileSizeX, remainingTileSizeY).renderTile());
            futures.add(future);
        }


        // wait for tasks to finish
        //System.out.println("tile size :" + TILE_SIZE + " width" + screen.getWidth() + " height" + screen.getHeight() + " , rX" + remainingTileSizeX + "  rY" + remainingTileSizeY);
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    class TileRenderer {
        // tile variable
        private int startX;
        private int startY;
        private int tileWidth;
        private int tileHeight;
        // ray variables
        private VoxelRepresentation foundVoxel;
        private double currentX;
        private double currentY;
        private double currentZ;
        private double dirX;
        private double dirY;
        private double dirZ;
        private double stepX;
        private double stepY;
        private double stepZ;
        private double step;
        private double radius;

        private TileRenderer(int startX, int startY, int tileWidth, int tileHeight) {
            this.startX = startX;
            this.startY = startY;
            this.tileWidth = tileWidth;
            this.tileHeight = tileHeight;
        }

        private void renderTile() {
            for (int i = startX; i < startX + tileWidth; i++) {
                for (int j = startY; j < startY + tileHeight; j++) {
                    double pixelCameraX = (2 * (i+0.5) / (double) Screen.getScreen().getWidth() - 1) * scale;
                    double pixelCameraY = (1 - 2 * (j + 0.5) / (double) Screen.getScreen().getHeight()) * scale * aspectRatio;
                    // mix ray direction + camera direction
                    dirX = forward[0] + pixelCameraX * right[0] + pixelCameraY * up[0];
                    dirY = forward[1] + pixelCameraX * right[1] + pixelCameraY * up[1];
                    dirZ = forward[2] + pixelCameraX * right[2] + pixelCameraY * up[2];
                    // normalize ray direction
                    double length = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                    dirX /= length;
                    dirY /= length;
                    dirZ /= length;

                    //timer.start();
                    pixels[i][j] = getColor();
                    //timer.end();
                    //timer.addToColorRay();
                }
            }
        }


        private Color getColor() {
            resetRayVariables(cameraX, cameraY, cameraZ);
            rayMarch();

            if (foundVoxel != null) {
                int red = foundVoxel.getRed();
                int green = foundVoxel.getGreen();
                int blue = foundVoxel.getBlue();
                double brightness = foundVoxel.getLightIntensity();
                //timer.start();
                double[] brightnessRGB = getBrightness(currentX, currentY, currentZ);
                //timer.end();
                //timer.addToLightRay();
                red = (int)(red*(brightness+brightnessRGB[0]));
                green = (int)(green*(brightness+brightnessRGB[1]));
                blue = (int)(blue*(brightness+brightnessRGB[2]));
                red = red>255? 255 : red;
                green = green>255? 255 : green;
                blue = blue>255? 255 : blue;
                return Color.rgb(red, green, blue);
            } else {
                return VOIDCOLOR;
            }
        }


        private double[] getBrightness(double originX, double originY, double originZ) {
            double[] brightnessRGB = {0,0,0};

            if (voxelsBrightnessCache[(int)originX][(int)originY][(int)originZ] != null) {
                //return voxelsBrightnessCache[(int)originX][(int)originY][(int)originZ];
            }
    
            for (int[] v : lights) {
                if (v[0] == originX && v[1] == originY && v[2] == originZ) {
                    continue;
                }
                if (!isReachable(originX, originY, originZ, v[0], v[1], v[2], v[3])) {
                    continue;
                }
    
                if (true) {
                    resetRayVariables(originX, originY, originZ);
                    // dir to light's location v
                    dirX = v[0]+0.5 - currentX;
                    dirY = v[1]+0.5 - currentY;
                    dirZ = v[2]+0.5 - currentZ;
                    // normalize ray direction
                    double length = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                    dirX /= length;
                    dirY /= length;
                    dirZ /= length;
                    // move one small step to prevent passing through voxels
                    currentX += 0.0001*dirX;
                    currentY += 0.0001*dirY;
                    currentZ += 0.0001*dirZ;
                    radius += 0.0001;
                    foundVoxel = currentVoxel();
                    while (foundVoxel == null) {
                        if (radius > maxRadius || currentX>254 || currentY>254 || currentZ>254 || currentX<8 || currentY<3 || currentZ<8) {
                            break;
                        }
                        rayMarch();
                        foundVoxel = currentVoxel();
                    }
                    if (foundVoxel != null) {
                        brightnessRGB[0] += foundVoxel.getRed()/255. * foundVoxel.getLightIntensity()/(radius*radius);
                        brightnessRGB[1] += foundVoxel.getGreen()/255. * foundVoxel.getLightIntensity()/(radius*radius);
                        brightnessRGB[2] += foundVoxel.getBlue()/255. * foundVoxel.getLightIntensity()/(radius*radius);
                    }
                }
            }
            voxelsBrightnessCache[(int)originX][(int)originY][(int)originZ] = brightnessRGB;
            return brightnessRGB;
        }


        private VoxelRepresentation rayMarch() {
            while (foundVoxel == null) {
                //System.out.println(radius + "  ; " + step);
                goToNextVoxelBox();
                if (radius > maxRadius || currentX>254 || currentY>254 || currentZ>254 || currentX<8 || currentY<3 || currentZ<8) {
                    return null;
                }
                
                foundVoxel = currentVoxel();
            }
            return foundVoxel;
        }


        private void goToNextVoxelBox() {
            int boxSize = voxelArray.maxEmptyBoxSize(currentX,currentY,currentZ);
            // step candidates
            stepX = Math.abs((currentX/boxSize - utils.Math.nextInt(currentX/boxSize, dirX))/dirX);
            stepY = Math.abs((currentY/boxSize - utils.Math.nextInt(currentY/boxSize, dirY))/dirY);
            stepZ = Math.abs((currentZ/boxSize - utils.Math.nextInt(currentZ/boxSize, dirZ))/dirZ);
            // choose smallest step
            step = Math.min(stepX, stepY);
            step = Math.min(step, stepZ);
            step *= boxSize;
            step += EPSILON;
            radius += step;
            // move step length
            currentX = currentX + step*dirX;
            currentY = currentY + step*dirY;
            currentZ = currentZ + step*dirZ;
        }


        private VoxelRepresentation currentVoxel() {
            long packedVoxel = voxels[(int)currentX][(int)currentY][(int)currentZ];
            if (packedVoxel != 0) {
                return VoxelRepresentation.unpackVoxel(packedVoxel);
            } else {
                return null;
            }
        }


        private void resetRayVariables(double originX, double originY, double originZ) {
            currentX = originX;
            currentY = originY;
            currentZ = originZ;
            foundVoxel = null;
            radius = 0;
        }
    }


    private boolean isReachable(double originX, double originY, double originZ, double lightX, double lightY, double lightZ, int lightIntensityInt) {
        return ((originX-lightX)*(originX-lightX) + (originY-lightY)*(originY-lightY) + (originZ-lightZ)*(originZ-lightZ) < 1/BRIGHTNESS_THRESHOLD*lightIntensityInt);
    }   
}
