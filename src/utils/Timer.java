package utils;

public class Timer {
    private static int frameNumber = 0;
    private static double totalTime = 0;
    private static double colorRayTotalTime = 0;
    private static double lightRayTotalTime = 0;

    private long startTime;
    private long duration;

    public void start() {
        startTime = System.currentTimeMillis();
    }

    public void end() {
        duration = System.currentTimeMillis() - startTime;
    }

    public void addToTotal() {
        totalTime += duration;
    }

    public void addToColorRay() {
        colorRayTotalTime += duration;
    }

    public void addToLightRay() {
        lightRayTotalTime += duration;
    }

    public static void printTimes() {
        System.out.println("Number of frames : " + frameNumber);
        System.out.println("Average time per frame : " + (totalTime/frameNumber)/1000 + " (" + java.lang.Math.round(frameNumber/(totalTime/1000000))/1000. + " FPS)");
        System.out.println("Average colorRay time per frame : " + (colorRayTotalTime/frameNumber)/1000);
        System.out.println("Average lightRay time per frame : " + (lightRayTotalTime/frameNumber)/1000);
        System.out.println(" ");
    }


    public static void incrementFrameNumber() {
        frameNumber ++;
    }
}
