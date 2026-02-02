package config;

public abstract class Config {

    // Controller config
    public static double STEP_SPEED;
    static final double ROTATE_SPEED = 0.1;


    // Screen config
    public static final double DEFAULT_SCREEN_WIDTH = 1080; // screen width on launch
    public static final double DEFAULT_SCREEN_HEIGHT = 720; // screen height on launch
    public static final double RESOLUTION_RATIO = 1; // resolution ratio of rendered pixels


    // World generation config
    public static final int[] WORLD_SIZE = {256,256,256};


    // Render config
    public static final int MAX_VOXEL_BOX = 64; // controls the size of the largest voxel box used to skip empty spaces (must be >0)


    // Hardware config
    public static final int THREADS_NUM = 8;


    // Misc config
    public static final boolean DETAILED_LOGS = true; // shows warnings and error details if enabled
}
