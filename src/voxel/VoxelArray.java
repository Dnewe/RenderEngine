package voxel;

import java.util.Map;

import java.util.HashMap;
import java.util.ArrayList;

public class VoxelArray {
    private long[][][] voxels;
    private Map<Integer,boolean[][][]> voxelBoxes;
    private ArrayList<int[]> lights = new ArrayList<>();

    private final int MAX_VOXEL_BOX = 64; // must be >0


    public VoxelArray(int[] worldSize) {
        voxels = new long[worldSize[0]][worldSize[1]][worldSize[2]];
        // init voxelBoxes
        voxelBoxes = new HashMap<Integer,boolean[][][]>();
        for (int i=2; i<MAX_VOXEL_BOX+1; i*=2) {
            voxelBoxes.put(i, new boolean[worldSize[0]/i+1][worldSize[1]/i+1][worldSize[2]/i+1]);
        }
    }

    public long[][][] getVoxels() {
        return voxels;
    }
    public ArrayList<int[]> getLights() {
        return lights;
    }


    public void addVoxel(VoxelRepresentation voxel, int x, int y, int z) {
        // add to voxels
        voxels[x][y][z] = voxel.packToLong();
        // add to lights
        if (voxel.getLightIntensity() >0) {
            int[] t = {x,y,z,(int) java.lang.Math.ceil(voxel.getLightIntensity())}; 
            lights.add(t);  
        }
        // add to voxelBoxes
        for (int i=2; i<MAX_VOXEL_BOX+1; i*=2) {
            voxelBoxes.get(i)[x/i][y/i][z/i] = true;
        }
    }


    public int maxEmptyBoxSize(double x, double y, double z) {
        int i;

        for (i=2; i<MAX_VOXEL_BOX+1; i*=2) {
            if (voxelBoxes.get(i)[(int)(x/i)][(int)(y/i)][(int)(z/i)]) {
                break;
            }
        }
        return i/2;
    }
}
