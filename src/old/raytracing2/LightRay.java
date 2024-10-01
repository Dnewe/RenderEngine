package old.raytracing2;


import old.VoxelRepresentation;
import world.World;

public class LightRay extends Ray {

    public LightRay(double x, double y, double z, double dirX, double dirY, double dirZ) {
        this.voxels = World.getWorld().getVoxels();
        this.lights = World.getWorld().getLights();
        this.x = x;
        this.y = y;
        this.z = z;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.radius = 0.001; // i.e. initially at starting point
        this.currentX = (x + radius * dirX);
        this.currentY = (y + radius * dirY);
        this.currentZ = (z + radius * dirZ);
    }

    public double getBrightness() {
        VoxelRepresentation voxel = foundVoxel();
        if (voxel != null) {
            return voxel.getLightIntensity();
        }
        double resultingBrightness = rayMarching();
        return resultingBrightness;
    }

    private double rayMarching() {
        VoxelRepresentation voxel = null;
        while (radius < 20 && voxel == null && timesMarched <1000) {
            rayMarch();
            voxel = foundVoxel();        
            //System.out.println(radius);
        }
        if (voxel != null) {
            //System.out.println(voxel.getX() + ", " + voxel.getY() + ", " + voxel.getZ() + ": " + voxel.getLightIntensity());
            return voxel.getLightIntensity()/radius;
            
        } else {
            return 0;
        }
    }
}
