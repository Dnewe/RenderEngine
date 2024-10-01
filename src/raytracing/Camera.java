package raytracing;

import config.Config;

public class Camera {

    // cartesian coordinates of the camera
    private double x;
    private double y;
    private double z;
    // spherical coordinates
    private double theta; // vertical angle (direction +Y -> y/x and 0 being +y)
    private double phi; // horizontal angle (direction +x -> +z and 0 being +x)
    // camera specific
    private RayTracer rayTracing;
    private double hFov;


    public Camera(double x, double y, double z, double thetaDegr, double phiDegr, double fov) {
        rayTracing = RayTracer.getRayTracer();
        this.x = x;
        this.y = y;
        this.z = z;
        this.theta = Math.toRadians(thetaDegr);
        this.phi = Math.toRadians(phiDegr);
        this.hFov = Math.toRadians(fov);
    }

    public Camera(double x, double y, double z) {
        this(x, y, z, 0, 45, 150);
    }

    // camera getters
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getZ() {
        return z;
    }
    public double getTheta() {
        return theta;
    }
    public double getPhi() {
        return phi;
    }
    public double getHFov() {
        return hFov;
    }


    // camera movement
    public void turnTheta(double value) {
        theta += value;
    }
    public void turnPhi(double value) {
        phi += value;
    }
    public void moveX(double value) {
        x += value;
    }
    public void moveY(double value) {
        y += value;
    }
    public void moveZ(double value) {
        z += value;
    }


    public void render() {
        rayTracing.run(this,Config.THREADS_NUM);
    }
    
    public void move() {
        /*
         * Move automatically between each frame
         */
    }
}
