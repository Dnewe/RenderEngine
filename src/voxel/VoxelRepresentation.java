package voxel;

import javafx.scene.paint.Color;

public class VoxelRepresentation {
    
    // color
    private int red;
    private int green;
    private int blue;
    // properties
    private float transparency;
    private float reflexivity;
    private float lightIntensity;

    // Scale factor to quantize the floats
    private static final int FLOAT_SCALE_FACTOR = 1023;  // This allows for values in the range [-1023, 1023]
    private static final int LIGHTINTENSITY_SCALE_FACTOR = 1023; // this


    public VoxelRepresentation(Color color, float transparency, float reflexivity, float lightIntensity) {
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


    public long packToLong() {
        long packedVoxel = 0;

        // existence flag
        packedVoxel |= 1L <<62;
        // pack RGB int values in 8 bits each
        packedVoxel |= ((long) (red & 0xFF)) << 52;
        packedVoxel |= ((long) (green & 0xFF)) << 44;
        packedVoxel |= ((long) (blue & 0xFF)) << 36;
        // pack float values in 12 bits each
        packedVoxel |= ((long) ((int) (transparency*FLOAT_SCALE_FACTOR) & 0xFF)) <<28;
        packedVoxel |= ((long) ((int) (reflexivity*FLOAT_SCALE_FACTOR) & 0xFF)) <<20;
        packedVoxel |= ((long) ((int) (lightIntensity*FLOAT_SCALE_FACTOR) & 0xFFFFF));

        return packedVoxel;
    }


    public static VoxelRepresentation unpackVoxel(long packedVoxel) {
        //System.out.println((int) ((packedVoxel >> 52) & 0xFF) + " " + (int) ((packedVoxel >> 44) & 0xFF) + " " + (int) ((packedVoxel >> 36) & 0xFF));
        return new VoxelRepresentation(
            Color.rgb((int) ((packedVoxel >> 52) & 0xFF),
                      (int) ((packedVoxel >> 44) & 0xFF),
                      (int) ((packedVoxel >> 36) & 0xFF)),
            ((packedVoxel >> 28) & 0xFF) / (float) FLOAT_SCALE_FACTOR,
            ((packedVoxel >> 20) & 0xFF) / (float) FLOAT_SCALE_FACTOR,
            (packedVoxel & 0xFFFFF) / (float) LIGHTINTENSITY_SCALE_FACTOR
        );
    }
}
