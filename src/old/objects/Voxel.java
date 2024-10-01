package old.objects;

import javafx.scene.paint.Color;

public class Voxel {
    // cartesian coordinates
    private int x;
    private int y;
    private int z;
    // voxel specific
    private Color color;
    private double reflexivity;
    private double transparency;
    private double lightIntensity;

    // white voxel
    public Voxel(int x, int y, int z) {
        this(x,y,z,Color.WHITE,0,0,0);
    }

    // colored voxel
    public Voxel(int x, int y, int z, Color color) {
        this(x,y,z,color,0,0,0);
    }

    // colored voxel with explicit reflexivity
    public Voxel(int x, int y, int z, Color color, double reflexivity, double transparency, double lightIntensity) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.reflexivity = reflexivity;
        this.transparency = transparency;
        this.lightIntensity = lightIntensity;
    }

    // getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }
    public Color getColor() {
        return color;
    }
    public double getReflexivity() {
        return reflexivity;
    }
    public double getTransparency() {
        return transparency;
    }
    public double getLightIntensity() {
        return lightIntensity;
    }
    // setters
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setZ(int z) {
        this.z = z;
    }
}
