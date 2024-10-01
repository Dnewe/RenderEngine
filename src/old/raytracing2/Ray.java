package old.raytracing2;

import java.util.ArrayList;

import old.VoxelRepresentation;

public abstract class Ray {
    protected int[][][][] voxels;
    protected ArrayList<int[]> lights;
    protected double epsilon = 1e-9; // Tolérance pour vérifier si la valeur est proche d'un entier
    // cartesian coordinates of starting point
    protected double x;
    protected double y;
    protected double z;
    // coordinates of ray
    protected double currentX;
    protected double currentY;
    protected double currentZ;
    // spherical coordinates
    protected double dirX;
    protected double dirY;
    protected double dirZ;
    protected double radius;
    protected double brightness;
    protected int timesReflected = 0;
    protected int timesMarched = 0;
    



    

    // raymarching towards initial direction
    protected void rayMarch() {
        timesMarched ++;

        double stepX = (nextInt(currentX, dirX) -x)/dirX;
        double stepY = (nextInt(currentY, dirY) -y)/dirY;
        double stepZ = (nextInt(currentZ, dirZ) -z)/dirZ;
        
        radius = Math.min(stepX, stepY);
        radius = Math.min(radius, stepZ);


        //radius += 0.01;

        currentX = (x + radius * dirX);
        currentY = (y + radius * dirY);
        currentZ = (z + radius * dirZ);
    }

    protected double nextInt(double value, double dir) {

        if (dir > 0) {
            if (Math.abs(value - Math.ceil(value)) < epsilon) {
                return value + 0.00001;
            } else {
                return Math.ceil(value);
            }
        } else {
            if (Math.abs(value - Math.floor(value)) < epsilon) {
                return value -0.00001 ;
            } else {
                return Math.floor(value);
            }
        }
    }


    // common test for Voxel
    protected VoxelRepresentation foundVoxel() {
        if (voxels[(int) Math.floor(currentX)][(int) Math.floor(currentY)][(int) Math.floor(currentZ)] != null) {
            return new VoxelRepresentation(voxels[(int) Math.floor(currentX)][(int) Math.floor(currentY)][(int) Math.floor(currentZ)]);
        } else {
            return null;
        }
        




        //List<Integer> currentPos = Arrays.asList((int) Math.floor(currentX+0.0000000), (int) Math.floor(currentY+0.0000000), (int) Math.floor(currentZ+0.0000000));
        //System.out.println("Ray at (" + currentX + ", " + currentY + ", " + currentZ + ")");
        /*if (currentPos.get(0) == x && currentPos.get(1) == y && currentPos.get(2) == z) {
            return null;
        } else if (voxels[(int) Math.floor(currentX)][(int) Math.floor(currentY)][(int) Math.floor(currentZ)] != null) {
            return voxels.get(currentPos);
        } else {
            return null;
        } */
    }
}
