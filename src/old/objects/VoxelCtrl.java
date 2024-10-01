package old.objects;


public class VoxelCtrl {
    private VoxelBox bottomFrontRight;
    private VoxelBox bottomFrontLeft;
    private VoxelBox bottomRearRight;
    private VoxelBox bottomRearLeft;
    private VoxelBox topFrontRight;
    private VoxelBox topFrontLeft;
    private VoxelBox topRearRight;
    private VoxelBox topRearLeft;

    private static int maxSize = 256;


    public VoxelCtrl() {
        bottomFrontRight = new VoxelBox(1, 1, -1, maxSize);
        bottomFrontLeft = new VoxelBox(1, -1, -1, maxSize);
        bottomRearRight = new VoxelBox(-1, 1, -1, maxSize);
        bottomRearLeft = new VoxelBox(-1, -1, -1, maxSize);
        topFrontRight = new VoxelBox(1, 1, 1, maxSize);
        topFrontLeft = new VoxelBox(1, -1, 1, maxSize);
        topRearRight = new VoxelBox(-1, 1, 1, maxSize);
        topRearLeft = new VoxelBox(-1, -1, 1, maxSize);
    }

    public void addVoxel(Voxel voxel, int x, int y, int z) {
    
        
    }

    // getters
    public VoxelBox getBottomFrontRight() {
        return bottomFrontRight;
    }
    public VoxelBox getBottomFrontLeft() {
        return bottomFrontLeft;
    }
    public VoxelBox getBottomRearRight() {
        return bottomRearRight;
    }
    public VoxelBox getBottomRearLeft() {
        return bottomRearLeft;
    }
    public VoxelBox getTopFrontRight() {
        return topFrontRight;
    }
    public VoxelBox getTopFrontLeft() {
        return topFrontLeft;
    }
    public VoxelBox getTopRearRight() {
        return topRearRight;
    }
    public VoxelBox getTopRearLeft() {
        return topRearLeft;
    }

    // setters
    public void setBottomFrontRight(VoxelBox bottomFrontRight) {
        this.bottomFrontRight = bottomFrontRight;
    }
    public void setBottomFrontLeft(VoxelBox bottomFrontLeft) {
        this.bottomFrontLeft = bottomFrontLeft;
    }
    public void setBottomRearRight(VoxelBox bottomRearRight) {
        this.bottomRearRight = bottomRearRight;
    }
    public void setBottomRearLeft(VoxelBox bottomRearLeft) {
        this.bottomRearLeft = bottomRearLeft;
    }
    public void setTopFrontRight(VoxelBox topFrontRight) {
        this.topFrontRight = topFrontRight;
    }
    public void setTopFrontLeft(VoxelBox topFrontLeft) {
        this.topFrontLeft = topFrontLeft;
    }
    public void setTopRearRight(VoxelBox topRearRight) {
        this.topRearRight = topRearRight;
    }
    public void setTopRearLeft(VoxelBox topRearLeft) {
        this.topRearLeft = topRearLeft;
    }
    
    
    
}
