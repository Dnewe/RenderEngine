package world;

//import utils.Logger;
import voxel.VoxelArray;
import raytracing.Camera;


public class World {

    private static World world = new World();

    private VoxelArray voxelArray;

    public static Camera camera = new Camera(128.5, 18.5, 128.5, -25, 45, 90);

    private World() {
        voxelArray = WorldGeneration.generateWorld();
    }

    
    public static World getWorld() {
        return world;
    }


    public VoxelArray getVoxelArray() {
        return voxelArray;
    }

    public void tick() {
        camera.render();
        //camera.move();
        //Logger.info("ticked");
    }
}
