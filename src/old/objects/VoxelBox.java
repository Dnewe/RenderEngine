package old.objects;

public class VoxelBox {
    /** position +x +y -z*/
    private Object bottomFrontRight; 
    /** position +x -y -z*/
    private Object bottomFrontLeft;
    /** position -x +y -z*/
    private Object bottomRearRight;
    /** position -x -y -z*/
    private Object bottomRearLeft;
    /** position +x +y +z*/
    private Object topFrontRight;
    /** position +x -y +z*/
    private Object topFrontLeft;
    /** position -x +y +z*/
    private Object topRearRight;
    /** position -x -y +z*/
    private Object topRearLeft;

    // box coordinates relative to other box of the same size
    private int x;
    private int y;
    private int z;
    // size of the box
    private int size;

    /**
     * 
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param size size of the side of the cube in voxel
     */
    public VoxelBox(int x, int y, int z, int size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    // specific methods
    public boolean isEmpty() {
        if (bottomFrontRight != null || bottomFrontLeft != null || bottomRearRight != null || bottomRearLeft != null || topFrontRight != null || topFrontLeft != null || topRearRight != null || topRearLeft != null) {
            return false;
        }
        return true;
    }

    public void addVoxel(Voxel voxel, int x, int y, int z) {
        if (size < 10) {
            
        } else {

        }
    }

    // getters
    public Object getBottomFrontRight() {
        return bottomFrontRight;
    }
    public Object getBottomFrontLeft() {
        return bottomFrontLeft;
    }
    public Object getBottomRearRight() {
        return bottomRearRight;
    }
    public Object getBottomRearLeft() {
        return bottomRearLeft;
    }
    public Object getTopFrontRight() {
        return topFrontRight;
    }
    public Object getTopFrontLeft() {
        return topFrontLeft;
    }
    public Object getTopRearRight() {
        return topRearRight;
    }
    public Object getTopRearLeft() {
        return topRearLeft;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
    public int getSize() {
        return size;
    }
    // setters
    public void setBottomFrontRight(Object bottomFrontRight) {
        this.bottomFrontRight = bottomFrontRight;
    }
    public void setBottomFrontLeft(Object bottomFrontLeft) {
        this.bottomFrontLeft = bottomFrontLeft;
    }
    public void setBottomRearRight(Object bottomRearRight) {
        this.bottomRearRight = bottomRearRight;
    }
    public void setBottomRearLeft(Object bottomRearLeft) {
        this.bottomRearLeft = bottomRearLeft;
    }
    public void setTopFrontRight(Object topFrontRight) {
        this.topFrontRight = topFrontRight;
    }
    public void setTopFrontLeft(Object topFrontLeft) {
        this.topFrontLeft = topFrontLeft;
    }
    public void setTopRearRight(Object topRearRight) {
        this.topRearRight = topRearRight;
    }
    public void setTopRearLeft(Object topRearLeft) {
        this.topRearLeft = topRearLeft;
    }

}
