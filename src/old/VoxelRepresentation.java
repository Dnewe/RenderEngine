package old;

import javafx.scene.paint.Color;

public class VoxelRepresentation {

    // one voxel is represented by a int list : [R,G,B, transparency (*), reflexivity (*), lightIntensity (*)]  (*): [-128,+127] -128=0% ; +127=100%

    private int red;
    private int green;
    private int blue;
    private double transparency;
    private double reflexivity;
    private double lightIntensity;

    public VoxelRepresentation(int[] voxelintList) {
        red= (int) voxelintList[0];
        green= (int) voxelintList[1];
        blue= (int) voxelintList[2];
        transparency= (double) voxelintList[3] /100;
        reflexivity= (double) voxelintList[4] /100;
        lightIntensity= (double) voxelintList[5] /100;
    } 

    public VoxelRepresentation(Color color, double transparency, double reflexivity, double lightIntensity) {
        this.red = (int) (color.getRed() * 255);
        this.green = (int) (color.getGreen() * 255);
        this.blue = (int) (color.getBlue() * 255);
        this.transparency = transparency;
        this.reflexivity = reflexivity;
        this.lightIntensity = lightIntensity;
    }
    

    public VoxelRepresentation(Color color) {
        this(color, 1, 0, 0);
    }

    public int getRed() {
        return red;
    }
    public int getGreen() {
        return green;
    }
    public int getBlue() {
        return blue;
    }
    public double getTransparency() {
        return transparency;
    }
    public double getReflexivity() {
        return reflexivity;
    }
    public double getLightIntensity() {
        return lightIntensity;
    }


    public int[] toIntList() {
        int[] voxel = new int[6];
        voxel[0] = (int) (red);
        voxel[1] = (int) (green);
        voxel[2] = (int) (blue);
        voxel[3] = (int) (transparency*100);
        voxel[4] = (int) (reflexivity*100);
        voxel[5] = (int) (lightIntensity*100);
        return voxel;
    }
}
