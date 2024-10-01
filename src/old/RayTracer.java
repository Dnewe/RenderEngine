package old;


import javafx.scene.paint.Color;
import screen.AppIHM;
import screen.Screen;
import world.Camera;
import voxel.VoxelArray;
import voxel.VoxelRepresentation;
import world.World;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RayTracer {

    private static RayTracer rayTracing;

    private VoxelArray voxelArray;
    private Color[][] pixels;
    private long[][][] voxels;
    private ArrayList<int[]> lights;

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
    private final double EPSILON = 1e-5;
    private double maxRadius = 128;
    private final Color VOIDCOLOR = Color.BLACK;
    private final double BRIGHTNESS_THRESHOLD = 0.001;  // (1/BRIGHTNESS_THRESHOLD)*100% of brightness
    private final int TILE_SIZE = 60; // Size of tiles to be processed by each thread


    private RayTracer() {
        pixels = Screen.getPixels();
        voxelArray = World.getWorld().getVoxelArray();
        voxels = voxelArray.getVoxels();
        lights = voxelArray.getLights();
    }

    public static RayTracer getRayTracer() {
        if (rayTracing == null) {
            rayTracing = new RayTracer();
        }
        return rayTracing;
    }

    public void run(Camera camera) {
        cameraX = camera.getX();
        cameraY = camera.getY();
        cameraZ = camera.getZ();
        theta = camera.getTheta();
        phi = camera.getPhi();
        scale = Math.tan(camera.getHFov() / 2);
        aspectRatio = Screen.getHeight() / Screen.getWidth();
        forward = new double[]{
            Math.cos(theta) * Math.cos(phi),
            Math.sin(theta),
            Math.cos(theta) * Math.sin(phi)
        };  
        right = new double[]{
            -Math.sin(phi),
            0,
            Math.cos(phi)
        };
        up = new double[]{
            -Math.sin(theta) * Math.cos(phi),
            Math.cos(theta),
            -Math.sin(theta) * Math.sin(phi)
        };

        for (int i=0; i<Screen.getWidth(); i++) {
            //System.out.println(i);
            for (int j=0; j<Screen.getHeight(); j++) {
                //System.out.println(j);
                double pixelCameraX = (2 * (i+0.5) / (double) AppIHM.screenWidth - 1) * scale;
                double pixelCameraY = (1 - 2 * (j + 0.5) / (double) AppIHM.screenHeight) * scale * aspectRatio;
                // mix ray direction + camera direction
                dirX = forward[0] + pixelCameraX * right[0] + pixelCameraY * up[0];
                dirY = forward[1] + pixelCameraX * right[1] + pixelCameraY * up[1];
                dirZ = forward[2] + pixelCameraX * right[2] + pixelCameraY * up[2];
                // normalize ray direction
                double length = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                dirX /= length;
                dirY /= length;
                dirZ /= length;

                pixels[i][j] = getColor(cameraX, cameraY, cameraZ);
            }
        }
    }

    public void runMultiProc(Camera camera, int numThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        cameraX = camera.getX();
        cameraY = camera.getY();
        cameraZ = camera.getZ();
        theta = camera.getTheta();
        phi = camera.getPhi();
        scale = Math.tan(camera.getHFov() / 2);
        aspectRatio = Screen.getHeight() / Screen.getWidth();
        forward = new double[]{Math.cos(theta) * Math.cos(phi), Math.sin(theta), Math.cos(theta) * Math.sin(phi)};  
        right = new double[]{-Math.sin(phi), 0, Math.cos(phi)};
        up = new double[]{-Math.sin(theta) * Math.cos(phi), Math.cos(theta), -Math.sin(theta) * Math.sin(phi)};
        for (int x = 0; x < Screen.getWidth(); x += TILE_SIZE) {
            for (int y = 0; y < Screen.getHeight(); y += TILE_SIZE) {
                int finalX = x;
                int finalY = y;
                
                // Submit a task for each tile
                executor.submit(() -> renderTile(finalX, finalY, TILE_SIZE, TILE_SIZE));
            }
        }

        executor.shutdown();  // Wait for tasks to complete
        while (!executor.isTerminated()) {}
    }


    private void renderTile(int startX, int startY, int tileWidth, int tileHeight) {
        for (int x = startX; x < startX + tileWidth; x++) {
            for (int y = startY; y < startY + tileHeight; y++) {
                double pixelCameraX = (2 * (x+0.5) / (double) AppIHM.screenWidth - 1) * scale;
                double pixelCameraY = (1 - 2 * (y + 0.5) / (double) AppIHM.screenHeight) * scale * aspectRatio;
                // mix ray direction + camera direction
                dirX = forward[0] + pixelCameraX * right[0] + pixelCameraY * up[0];
                dirY = forward[1] + pixelCameraX * right[1] + pixelCameraY * up[1];
                dirZ = forward[2] + pixelCameraX * right[2] + pixelCameraY * up[2];
                // normalize ray direction
                double length = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                dirX /= length;
                dirY /= length;
                dirZ /= length;

                pixels[x][y] = getColor(cameraX, cameraY, cameraZ);
            }
        }
    }


    private Color getColor(double originX, double originY, double originZ) {
        //System.out.println("begin color");
        //resetRayVariables(originX, originY, originZ);
        rayMarch();
        if (foundVoxel != null) {
            int red = foundVoxel.getRed();
            int green = foundVoxel.getGreen();
            int blue = foundVoxel.getBlue();
            //System.out.println(red + " " + green + " " + blue);
            double brightness = foundVoxel.getLightIntensity();
            double[] brightnessRGB =  getBrightness(currentX, currentY, currentZ);
            //brightness = brightness > 1 ? 1 : brightness;
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

    private VoxelRepresentation rayMarch() {
        VoxelRepresentation foundVoxel = null;
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


    private double[] getBrightness(double originX, double originY, double originZ) {
        double[] brightnessRGB = {0,0,0};

        for (int[] v : lights) {
            if (v[0] == originX && v[1] == originY && v[2] == originZ) {
                continue;
            }
            if (!isReachable(originX, originY, originZ, v[0], v[1], v[2], v[3])) {
                //System.out.println("skipped : not reachable");
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
        return brightnessRGB;
    }

    private void goToNextVoxelBox() {
        int boxSize = voxelArray.maxEmptyBoxSize(currentX,currentY,currentZ);
        //System.out.println(boxSize);
        // step candidates
        double stepX = Math.abs((currentX/boxSize - utils.Math.nextInt(currentX/boxSize, dirX))/dirX);
        double stepY = Math.abs((currentY/boxSize - utils.Math.nextInt(currentY/boxSize, dirY))/dirY);
        double stepZ = Math.abs((currentZ/boxSize - utils.Math.nextInt(currentZ/boxSize, dirZ))/dirZ);
        // choose smallest step
        double step = Math.min(stepX, stepY);
        step = Math.min(step, stepZ);
        step *= boxSize;
        step += EPSILON;
        radius += step;
        // move step length
        currentX = currentX + step*dirX;
        currentY = currentY + step*dirY;
        currentZ = currentZ + step*dirZ;
    }

    /*private void goToNextVoxel() {
        // step candidates
        stepX = Math.abs((currentX - nextInt(currentX, dirX))/dirX);
        stepY = Math.abs((currentY - nextInt(currentY, dirY))/dirY);
        stepZ = Math.abs((currentZ - nextInt(currentZ, dirZ))/dirZ);
        // choose smallest step
        step = Math.min(stepX, stepY);
        step = Math.min(step, stepZ);
        step += EPSILON;
        radius += step;
        // move step length
        currentX = currentX + step*dirX;
        currentY = currentY + step*dirY;
        currentZ = currentZ + step*dirZ;
    }

    private static double nextInt(double pos, double dir) {
        if (dir>0) {
            return Math.ceil(pos);
        } else {
            return Math.floor(pos);
        }
    }*/

    private boolean isReachable(double originX, double originY, double originZ, double lightX, double lightY, double lightZ, int lightIntensityInt) {
        return ((originX-lightX)*(originX-lightX) + (originY-lightY)*(originY-lightY) + (originZ-lightZ)*(originZ-lightZ) < 1/BRIGHTNESS_THRESHOLD*lightIntensityInt);
    }

    private void resetRayVariables(double originX, double originY, double originZ) {
        currentX = originX;
        currentY = originY;
        currentZ = originZ;
        foundVoxel = null;
        radius = 0;
    }

    private VoxelRepresentation currentVoxel() {
        long packedVoxel = voxels[(int)currentX][(int)currentY][(int)currentZ];
        if (packedVoxel != 0) {
            return VoxelRepresentation.unpackVoxel(packedVoxel);
        } else {
            return null;
        }
    }

}
