package old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;
import old.objects.Voxel;
import old.raytracing2.ColorRay;
import screen.AppIHM;
import screen.Screen;
import world.World;

public class Camera {
    public void render2() {
        ColorRay ray;
        Color resultingColor;
        double aspectRatio = (double) AppIHM.screenWidth / AppIHM.screenHeight;
        double scale = Math.tan(hFov / 2);
        double dirX;
        double dirY;
        double dirZ;
        double length;

        // camera vectors
        double[] forward = {
            Math.cos(theta) * Math.cos(phi),
            Math.sin(theta),
            Math.cos(theta) * Math.sin(phi)
        };
        
        double[] right = {
            -Math.sin(phi),
            0,
            Math.cos(phi)
        };

        double[] up = {
            -Math.sin(theta) * Math.cos(phi),
            Math.cos(theta),
            -Math.sin(theta) * Math.sin(phi)
        };


        for (int i=0; i<AppIHM.screenWidth; i++) {
            for (int j=0; j<AppIHM.screenHeight; j++) {
                //System.out.println(j);
                double pixelCameraX = (2 * (i+0.5) / (double) AppIHM.screenWidth - 1) * scale * aspectRatio;
                double pixelCameraY = (2 * (j + 0.5) / (double) AppIHM.screenHeight - 1) * scale;
                //dirZ = 1; // axe Z points behind the camera

                // mix ray direction + camera direction
                dirX = forward[0] + pixelCameraX * right[0] + pixelCameraY * up[0];
                dirY = forward[1] + pixelCameraX * right[1] + pixelCameraY * up[1];
                dirZ = forward[2] + pixelCameraX * right[2] + pixelCameraY * up[2];
                // normalize ray direction
                length = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                dirX /= length;
                dirY /= length;
                dirZ /= length;
                
                ray = new ColorRay(this.x, this.y, this.z, dirX, dirY, dirZ, 0);
                //ray = new Ray(voxels, x, y, z, theta + (j+0.5)*(vFov/AppIHM.screenHeight)-vFov/2, phi + (i+0.5)*(hFov/AppIHM.screenWidth) - hFov/2);
                resultingColor = ray.getColor();
                Screen.getPixels()[i][j] = resultingColor;
                
            }
            //System.out.println(i);
        }
    }
    
    // cartesian coordinates of the camera
    private double x;
    private double y;
    private double z;
    // spherical coordinates
    private double thetaCamera; // vertical angle (direction +Y -> y/x and 0 being +y)
    private double phiCamera; // horizontal angle (direction +x -> +y and 0 being +x)

    // camera specific
    private World world;
    private double hFov;
    private Color[][] pixels;
    private HashMap<List<Integer>, Voxel> voxels;
    private ArrayList<Voxel> lights;

    // camera movement
    public void turnTheta(double value) {
        thetaCamera += value;
    }
    public void turnPhi(double value) {
        phiCamera += value;
    }
    public void moveX(double value) {
        x += value;
    }
    public void moveY(double value) {
        y += value;
    }
    public void moveZ(double value) {
        z += value;
    }

    // Camera set looking +X horizontally
    public Camera(World world, double x, double y, double z, double fov) {
        this(world, x, y, z, 0, 0, fov);
    }

    public Camera(World world, double x, double y, double z, double theta, double phi, double fov) {
        this.world = world;
        this.pixels = Screen.getPixels();
        this.voxels = world.getVoxels();
        this.lights = world.getLights();
        this.x = x;
        this.y = y;
        this.z = z;
        this.thetaCamera = Math.toRadians(theta);
        this.phiCamera = Math.toRadians(phi);
        this.hFov = Math.toRadians(fov);
    }

    public void rayTrace() {
        ColorRay ray;
        Color resultingColor;
        double aspectRatio = (double) AppIHM.screenWidth / AppIHM.screenHeight;
        double scale = Math.tan(hFov / 2);
        double dirX;
        double dirY;
        double dirZ;
        double length;

        // camera vectors
        double[] forward = {
            Math.cos(thetaCamera) * Math.cos(phiCamera),
            Math.sin(thetaCamera),
            Math.cos(thetaCamera) * Math.sin(phiCamera)
        };
        
        double[] right = {
            -Math.sin(phiCamera),
            0,
            Math.cos(phiCamera)
        };

        double[] up = {
            -Math.sin(thetaCamera) * Math.cos(phiCamera),
            Math.cos(thetaCamera),
            -Math.sin(thetaCamera) * Math.sin(phiCamera)
        };


        for (int i=0; i<AppIHM.screenWidth; i++) {
            for (int j=0; j<AppIHM.screenHeight; j++) {
                //System.out.println(j);
                double pixelCameraX = (2 * (i+0.5) / (double) AppIHM.screenWidth - 1) * scale * aspectRatio;
                double pixelCameraY = (1 - 2 * (j + 0.5) / (double) AppIHM.screenHeight) * scale;
                //dirZ = 1; // axe Z points behind the camera

                // mix ray direction + camera direction
                dirX = forward[0] + pixelCameraX * right[0] + pixelCameraY * up[0];
                dirY = forward[1] + pixelCameraX * right[1] + pixelCameraY * up[1];
                dirZ = forward[2] + pixelCameraX * right[2] + pixelCameraY * up[2];
                // normalize ray direction
                length = Math.sqrt(dirX*dirX + dirY*dirY + dirZ*dirZ);
                dirX /= length;
                dirY /= length;
                dirZ /= length;
                
                ray = new ColorRay(this.x, this.y, this.z, dirX, dirY, dirZ, 0);
                //ray = new Ray(voxels, x, y, z, thetaCamera + (j+0.5)*(vFov/AppIHM.screenHeight)-vFov/2, phiCamera + (i+0.5)*(hFov/AppIHM.screenWidth) - hFov/2);
                resultingColor = ray.getColor();
                pixels[i][j] = resultingColor;
                
            }
            //System.out.println(i);
        }
    }
}
