package screen;

import raytracing.Camera;
import world.World;
import java.lang.Math;
import java.awt.Robot;

import javafx.scene.Cursor;
import javafx.scene.Scene;

public class Controller {

    private Camera camera;
    private Scene scene;
    private Robot robot;

    // mouse movement
    private final double defaultRotateSpeed = 0.1;
    private double previousMouseX = -1;
    private double previousMouseY = -1;
    private double dynamicOffsetX = 0.;
    private double dynamicOffsetY = 0.8;
    private boolean paused = false;

    // keyboard movement
    private static final double defaultStepSpeed = 0.1;
    private boolean moveForward, moveBackward, moveLeft, moveRight, moveUp, moveDown;
    private double stepSpeed = defaultStepSpeed;

    

    public Controller(Scene scene) {
        this.scene = scene;  
        try {
        this.robot = new Robot();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setCamera() {
        
        if (World.camera!=null) {
            this.camera = World.camera;
        }
    }

    public void handleKeyPressed(String key) {
        switch (key) {
            case "Z" -> moveForward = true;
            case "S" -> moveBackward = true;
            case "Q" -> moveLeft = true;
            case "D" -> moveRight = true;
            case "SPACE" -> moveUp = true;
            case "CONTROL" -> moveDown = true;
            case "SHIFT" -> stepSpeed = 2*defaultStepSpeed;
            case "ESCAPE" -> togglePause();
        }
    }

    public void handleKeyReleased(String key) {
        switch (key) {
            case "Z" -> moveForward = false;
            case "S" -> moveBackward = false;
            case "Q" -> moveLeft = false;
            case "D" -> moveRight = false;
            case "SPACE" -> moveUp = false;
            case "CONTROL" -> moveDown = false;
            case "SHIFT" -> stepSpeed = defaultStepSpeed;
        }
    }
    
    private void togglePause() {
        paused = !paused;
        centerMouse();
    }

    public void handleMouseMovement(double mouseX, double mouseY) {
        if (camera==null) {setCamera();}
        if (paused) {return;}

        double sceneCenterX = scene.getWidth() / 2.;
        double sceneCenterY = scene.getHeight() / 2.;

        /*if (previousMouseX != -1 && previousMouseY != -1) {
            dynamicOffsetX = sceneCenterX - previousMouseX;
            dynamicOffsetY = sceneCenterY - previousMouseY;
        }*/

        mouseX += dynamicOffsetX; 
        mouseY += dynamicOffsetY; 

        //System.out.println( dynamicOffsetX +  "  scene: "  + dynamicOffsetY);

        //System.out.println( sceneCenterX +  "  scene: "  + sceneCenterY);

        //System.out.println( mouseX +  "  mouse: "  + mouseY);

        double deltaX = mouseX - sceneCenterX;
        double deltaY = mouseY - sceneCenterY;
        rotate(deltaX, deltaY);

        centerMouse();

        previousMouseX = mouseX;
        previousMouseY = mouseY;     
    }

    public void updateCamera() {
        if (paused) {
            scene.setCursor(Cursor.DEFAULT); 
            return;
        } else {
            //scene.setCursor(Cursor.NONE); 
            if (moveForward) {moveForward();}
            if (moveBackward) {moveBackward();}
            if (moveLeft) {moveLeft();}
            if (moveRight) {moveRight();}
            if (moveUp) {moveUp();}
            if (moveDown) {moveDown();}
        }
        
    }

    private void rotate(double deltaX, double deltaY) {
        camera.turnPhi(Math.toRadians(deltaX*defaultRotateSpeed));
        camera.turnTheta(Math.toRadians(-deltaY*defaultRotateSpeed));
    }

    private void moveForward() {
        if (camera==null) {setCamera();}
        camera.moveX(Math.cos(camera.getPhi())*stepSpeed);
        camera.moveZ(Math.sin(camera.getPhi())*stepSpeed);
    }

    private void moveRight() {
        if (camera==null) {setCamera();}
        camera.moveX(Math.cos(camera.getPhi()+Math.PI/2)*stepSpeed);
        camera.moveZ(Math.sin(camera.getPhi()+Math.PI/2)*stepSpeed);
    }

    private void moveLeft() {
        if (camera==null) {setCamera();}
        camera.moveX(Math.cos(camera.getPhi()-Math.PI/2)*stepSpeed);
        camera.moveZ(Math.sin(camera.getPhi()-Math.PI/2)*stepSpeed);
    }

    private void moveBackward() {
        if (camera==null) {setCamera();}
        camera.moveX(Math.cos(camera.getPhi()+Math.PI)*stepSpeed);
        camera.moveZ(Math.sin(camera.getPhi()+Math.PI)*stepSpeed);
    }

    private void moveUp() {
        if (camera==null) {setCamera();}
        camera.moveY(stepSpeed);
    }

    private void moveDown() {
        if (camera==null) {setCamera();}
        camera.moveY(-stepSpeed);
    }


    // Method to center the mouse on the screen or window
    public void centerMouse() {
        double centerX = scene.getWindow().getX() + scene.getX() + scene.getWidth() / 2;
        double centerY = scene.getWindow().getY() + scene.getY() + scene.getHeight() / 2;

        previousMouseX = centerX - scene.getWindow().getX();
        previousMouseY = centerY - scene.getWindow().getY();

        robot.mouseMove((int) centerX, (int) centerY);
    }
}
