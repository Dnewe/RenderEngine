package screen;

import utils.Logger;
import utils.Timer;
import world.World;

public class App {

    public void start() {
        Timer timer = new Timer();
        try {
            Logger.info("Starting app...");
            World world = World.getWorld();

            long averageTime = 0;
            timer.start();
            for (int i=0; i<2000; i++) {
                //long time = System.currentTimeMillis();

                world.tick();
                //long timeTaken = System.currentTimeMillis() - time;
                //averageTime = (averageTime*i + timeTaken)/(i+1);
                //System.out.println("frame : " + (i+1));
                //System.out.println("time taken : " +  (timeTaken)/1000.0);
                //System.out.println("avg time : " +  (averageTime/1000.0));
                Screen.getScreen().draw();
                
                Timer.incrementFrameNumber();
            }
            timer.end();
            timer.addToTotal(); 
            System.out.println("FPS : " + 1000.0/averageTime);
            Timer.printTimes();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

}
