package old;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;
import objects.Voxel;
import raytracing.Camera;

public class World {
    private HashMap<List<Integer>, Voxel> voxels;
    private ArrayList<Voxel> lights;
    private Color[][] pixels;
    private Camera camera;

    public World(Color[][] pixels) {
        voxels = new HashMap<>();
        lights = new ArrayList<>();
        this.pixels = pixels;
    }

    public void generateWorld() {
        setCamera(-4.5, 4.2, 4.5, -20, -45, 100);

        int groundsize = 10;
        Color groundColor = Color.WHITE;
        // ground
        
        for (int i=-groundsize; i<groundsize; i++) {
            for (int j=-groundsize; j<groundsize; j++) {
                if ((i+j)%2 == 0) {
                    addMirrorVoxel(i, 0, j, groundColor, 0);
                } else {
                    addMirrorVoxel(i, 0, j, Color.GRAY,0); 
                }
            }
        }

        for (int i=0; i<groundsize; i++) {
            for (int j=-groundsize; j<groundsize; j++) {
                //addMirrorVoxel(groundsize-3, i, j, Color.GRAY , 0.8);
                //addMirrorVoxel(-(groundsize-3), i, j, Color.GRAY, 0.8);
                //addMirrorVoxel(j, i, -groundsize+3, groundColor, 0.9);
                //addMirrorVoxel(j, i, groundsize-3, groundColor, 0.9);
            }
        }
        
        //addWhiteVoxel(0, 0, 15);
        //addWhiteVoxel(0, 0, 5);
        //addWhiteVoxel(1, 1, 5);
        //addRedVoxel(-1, 0, 5);
        //addWhiteVoxel(0, 0, -5);
        addVoxel(-1, 1 , 0, Color.WHITE, 0, 0, 1);
        //addLightVoxel(4, 1, 0, 1);
        addLightVoxel(0, 6 ,5, 1);
        addWhiteVoxel(5, 7, 10);
        addWhiteVoxel(0, 1, -2);
        addWhiteVoxel(2, 1, 3);
        addMirrorVoxel(-3, 1, -3, Color.WHITE, 1);
        addMirrorVoxel(-3, 2, -3, Color.WHITE, 1);
        addTransparentVoxel(-3, 1, 3, Color.RED, 0.55);
        addTransparentVoxel(-3, 2, 3, Color.RED, 0.55);
        addWhiteVoxel(1, 1, -4);
        addMirrorVoxel(0, 2, -2, Color.WHITE, 0); 
        addWhiteVoxel(0, 2, 2);


        // blackhole
        //addBlackHole(-2, 1.5, 2, 1);
    }

    public void renderWorld() {   
        camera.rayTrace();
        camera.turnPhi(1.0/180.0 * Math.PI);
        camera.moveZ(-0.1);
    }

    // getters
    public Color[][] getPixels() {
        return pixels;
    }
    public HashMap<List<Integer>, Voxel> getVoxels() {
        return voxels;
    }
    public ArrayList<Voxel> getLights() {
        return lights;
    }


    // voxels
    public void addVoxel(int x, int y, int z, Color color, double reflexivity, double transparency, double lightIntensity) {
        Voxel voxel = new Voxel(x, y, z, color, reflexivity, transparency, lightIntensity);
        List<Integer> coordinates = Arrays.asList(x,y,z);
        voxels.put(coordinates, voxel);   
        if (lightIntensity > 0) {
            lights.add(voxel);
        }  
    }

    public void addMirrorVoxel(int x, int y, int z, Color color, double reflexivity) {
        Voxel voxel = new Voxel(x, y, z, color, reflexivity, 0, 0);
        List<Integer> coordinates = Arrays.asList(x,y,z);
        voxels.put(coordinates, voxel);    
    }

    public void addTransparentVoxel(int x, int y, int z, Color color, double transparency) {
        Voxel voxel = new Voxel(x, y, z, color, 0, transparency, 0);
        List<Integer> coordinates = Arrays.asList(x,y,z);
        voxels.put(coordinates, voxel);    
    }


    public void addLightVoxel(int x, int y, int z, double lightIntensity) {
        Voxel voxel = new Voxel(x, y, z, Color.WHITE, 0, 0, lightIntensity);
        List<Integer> coordinates = Arrays.asList(x,y,z);
        voxels.put(coordinates, voxel);
        lights.add(voxel);
    }

    public void addWhiteVoxel(int x, int y, int z) {
        Voxel voxel = new Voxel(x, y, z);
        List<Integer> coordinates = Arrays.asList(x,y,z);
        voxels.put(coordinates, voxel);
    }
    public void addRedVoxel(int x, int y, int z) {
        Voxel voxel = new Voxel(x, y, z, Color.RED);
        List<Integer> coordinates = Arrays.asList(x,y,z);
        voxels.put(coordinates, voxel);
    }

    public void setCamera(double x, double y, double z, double theta, double phi, double fov) {
        this.camera = new Camera(this, x, y, z, theta, phi, fov);
    }



}
