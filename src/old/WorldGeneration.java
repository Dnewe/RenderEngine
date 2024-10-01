package world;

import utils.Logger;
import java.util.ArrayList;

import voxel.VoxelRepresentation;
import voxel.VoxelArray;

import javafx.scene.paint.Color;

public class WorldGeneration {

    private static int[] worldSize = {256,256,256};  // x,y,z

    private static long[][][] voxels;
    private static ArrayList<int[]> lights = new ArrayList<>();

    // ground properties
    private static int groundHeight = 4;
    private static VoxelRepresentation groundVoxel = new VoxelRepresentation(Color.WHITE, 0,0,0);
    private static VoxelRepresentation groundVoxel2 = new VoxelRepresentation(Color.LIGHTGRAY, 0,0,0);
    private static VoxelRepresentation lightVoxel = new VoxelRepresentation(Color.WHITE, 0, 0, 16);
    private static VoxelRepresentation sun = new VoxelRepresentation(Color.rgb(255, 240, 200), 0, 0, 100);
   
    
    public static long[][][] generateWorld() {
        Logger.info("Generating world...");
        voxels = new long[worldSize[0]][worldSize[1]][worldSize[2]];

        generateCheckerGround();
        generateChicken(135, 5, 135);
        setVoxel(sun, 128, groundHeight+15, 128);
        //fillVoxel(new VoxelRepresentation(Color.RED), 135, groundHeight+1, 135, 135, groundHeight+2, 135);
        //fillVoxel(lightVoxel, 140, 16, 130, 140, 16, 130);
        fillVoxel(lightVoxel, 128, 16, 139, 128, 16, 139);
        //fillVoxel(lightVoxel, 132, 9, 125, 132, 9, 125);
        //fillVoxel(lightVoxel, 131, 20, 131, 131, 20, 131);
        //fillVoxel(lightVoxel, 133, 4, 123, 123, 7, 133);
        //fillVoxel(redVoxel, 133, 4, 133, 133, 7, 133);
        //fillVoxel(lightVoxel, 123, 4, 133, 133, 7, 123);
        //fillVoxel(lightVoxel, 128, 10, 128, 128, 10, 128);

        Logger.info("World generated.");
        return voxels;
    }

    public static ArrayList<int[]> getGeneratedLights() {
        Logger.info("Generated " + lights.size() + " light.");
        return lights;
    }

    private static void generateCheckerGround() {
        Logger.info("Generating ground...");
        for (int x= 0 ; x< worldSize[0]; x++) {
            for (int z= 0 ; z< worldSize[2]; z++) {
                if ((x+z)%2 ==0) {
                    addVoxel(groundVoxel, x, groundHeight, z);
                } else {
                    addVoxel(groundVoxel2, x, groundHeight, z);
                }
            }
        }
    }

    private static void generateGround() {
        Logger.info("Generating ground...");
        for (int x= worldSize[0]/2-worldSize[0] ; x< worldSize[0]/2; x++) {
            for (int z= worldSize[2]/2-worldSize[2] ; z< worldSize[2]/2; z++) {
                addVoxel(groundVoxel, x, groundHeight, z);
            }
        }
    }


    private static void generateChicken(int x, int y, int z) {
        VoxelRepresentation orangeVoxel = new VoxelRepresentation(Color.ORANGE);
        VoxelRepresentation whiteVoxel = new VoxelRepresentation(Color.WHITE);
        VoxelRepresentation redVoxel = new VoxelRepresentation(Color.RED);
        VoxelRepresentation blackVoxel = new VoxelRepresentation(Color.BLACK);

        // right feet
        fillVoxel(orangeVoxel, x, y, z, x+1, y, z);
        fillVoxel(orangeVoxel, x, y, z+2, x+1, y, z+2);
        fillVoxel(orangeVoxel, x+2, y, z, x+4, y, z+2);
        fillVoxel(orangeVoxel, x+3, y+1, z+1, x+3, y+2, z+1);
        // left feet
        fillVoxel(orangeVoxel, x, y, z+5, x+1, y, z+5);
        fillVoxel(orangeVoxel, x, y, z+7, x+1, y, z+7);
        fillVoxel(orangeVoxel, x+2, y, z+5, x+4, y, z+7);
        fillVoxel(orangeVoxel, x+3, y+1, z+6, x+3, y+2, z+6);
        // body
        fillVoxel(whiteVoxel, x, y+3, z+1, x+4, y+11, z+6);
        fillVoxel(whiteVoxel, x+5, y+3, z+1, x+7, y+6, z+6);
        fillVoxel(whiteVoxel, x+8, y+3, z+2, x+8, y+6, z+5);
        // wings
        fillVoxel(whiteVoxel, x+2, y+3, z, x+6, y+5, z);
        fillVoxel(whiteVoxel, x+2, y+3, z+7, x+6, y+5, z+7);
        // head
        fillVoxel(orangeVoxel, x-2, y+8, z+3, x-1, y+9, z+4); // beak
        fillVoxel(redVoxel, x-1, y+7, z+3, x-1, y+7, z+4);
        fillVoxel(blackVoxel, x+1, y+10, z+1, x+1, y+10, z+1); // right eye
        fillVoxel(blackVoxel, x+1, y+10, z+6, x+1, y+10, z+6); // left eye
        fillVoxel(redVoxel, x, y+12, z+3, x+3, y+13, z+4);
    }


    private static void fillVoxel(VoxelRepresentation voxel, int x1, int y1, int z1, int x2, int y2, int z2) {
        for (int x=x1; x<=x2; x++) {
            for (int y=y1; y<=y2; y++) {
                for (int z=z1; z<=z2; z++) {
                    addVoxel(voxel, x, y, z);
                }
            }
        }
    }

    private static void setVoxel(VoxelRepresentation voxel, int x, int y, int z) {
        addVoxel(voxel, x, y, z);
    }

    private static void addVoxel(VoxelRepresentation voxel, int x, int y, int z) {
        voxels[x][y][z] = voxel.packToLong();
        if (voxel.getLightIntensity()>0) {
            int[] t = {x,y,z,(int) Math.ceil(voxel.getLightIntensity())}; 
            lights.add(t);
        }
    }
}
