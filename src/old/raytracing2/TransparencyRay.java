package old.raytracing2;

import javafx.scene.paint.Color;
import old.VoxelRepresentation;
import world.World;

public class TransparencyRay extends Ray {
    private double currentTransparency;


    public TransparencyRay(double x, double y, double z, double dirX, double dirY, double dirZ, int timesReflected, double currentTransparency) {
        this.voxels = World.getWorld().getVoxels();
        this.lights = World.getWorld().getLights();
        this.x = x;
        this.y = y;
        this.z = z;
        this.currentX = x;
        this.currentY = y;
        this.currentZ = z;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.radius = 0; // i.e. initially at starting point
        this.brightness = 0;
        this.timesReflected = timesReflected;
        this.currentTransparency = currentTransparency;
    }

    public Color getColor() {
        Color resultingColor = rayMarching();
        return resultingColor;
    }

    private Color rayMarching() {
        VoxelRepresentation voxel = null;
        while (radius < 20 && voxel == null && timesMarched <1000) {
            rayMarch();
            /*for (BlackHole b : blackHoles) {
                if (Math.abs(currentX - b.getX()) <1.5 && Math.abs(currentY - b.getY()) <1.5 && Math.abs(currentZ - b.getZ()) <1.5) {
                    return Color.BLACK;
                }
            }*/
            voxel = foundVoxel();   
            if (voxel != null && currentTransparency == voxel.getTransparency()) {
                voxel = null;
            } 
            //System.out.println(radius);
        }
        if (voxel != null) {
            // base color
            double red = voxel.getRed();
            double green = voxel.getGreen();
            double blue = voxel.getBlue();
            // lighting
            for (int[] v : lights) {
                if (v[0] != (int) currentX && v[1] != (int) currentY && v[2] != (int) currentZ) {
                    double dirXlight = v[0]+0.5 - currentX;
                    double dirYlight = v[1]+0.5 - currentY;
                    double dirZlight = v[2]+0.5 - currentZ;
                    // normalize ray direction
                    double length = Math.sqrt(dirXlight*dirXlight + dirYlight*dirYlight + dirZlight*dirZlight);
                    dirXlight /= length;
                    dirYlight /= length;
                    dirZlight /= length;
                    LightRay lightRay = new LightRay(currentX, currentY, currentZ, dirXlight, dirYlight, dirZlight);
                    brightness += lightRay.getBrightness();
                    
                } else {
                    brightness += voxel.getLightIntensity();
                }
            }
            brightness = brightness >1? 1 : brightness;    
            
            // reflection
            double reflexivity = voxel.getReflexivity();
            if (reflexivity !=0 && timesReflected <10) {
                double dirXreflexion = dirX;
                double dirYreflexion = dirY;
                double dirZreflexion = dirZ;
                if (Math.abs(currentY - Math.round(currentY)) < 0.0001) {
                    dirYreflexion = - dirY;
                } else if (Math.abs(currentX - Math.round(currentX)) < 0.0001) {
                    dirXreflexion = -dirX;
                } else if (Math.abs(currentZ - Math.round(currentZ)) < 0.0001) {
                    dirZreflexion = -dirZ;
                }
                ColorRay reflectionRay = new ColorRay(currentX, currentY, currentZ, dirXreflexion, dirYreflexion, dirZreflexion, timesReflected+1);
                Color reflectionColor = reflectionRay.getColor();
                brightness = (1-reflexivity)*brightness + reflexivity*0.9;
                red = (reflexivity * reflectionColor.getRed() + (1-reflexivity) * red);
                green = (reflexivity * reflectionColor.getGreen() + (1-reflexivity) * green);
                blue = (reflexivity * reflectionColor.getBlue() + (1-reflexivity) * blue);
            }

            // transparency

            double transparency = voxel.getTransparency();
            
            if (transparency !=0) {
                TransparencyRay transparencyRay = new TransparencyRay(currentX, currentY,currentZ, dirX, dirY, dirZ, timesReflected, transparency);
                Color transparencyColor = transparencyRay.getColor();
                red = (transparency * transparencyColor.getRed() + (1-transparency) * red);
                green = (transparency * transparencyColor.getGreen() + (1-transparency) * green);
                blue = (transparency * transparencyColor.getBlue() + (1-transparency) * blue);
            }

            return Color.rgb((int) (red*brightness), (int) (green*brightness), (int) (blue*brightness));      
        } else {
            return Color.BLACK;
        }
    }
}
