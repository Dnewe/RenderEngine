package screen;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import config.Config;


public class AppIHM extends Application {
    private double screenWidth = Config.DEFAULT_SCREEN_WIDTH; //default width of screen (left = 0)
    private double screenHeight = Config.DEFAULT_SCREEN_HEIGHT; //default height of screen (top = 0)

    private static Canvas canvas;

    @Override
    public void start(Stage stage){
        App app = new App();

        canvas = new Canvas(); // display screen
        VBox.setVgrow(canvas, javafx.scene.layout.Priority.ALWAYS);   
        Pane pane = new Pane(canvas);
        Scene scene = new Scene(pane, screenWidth, screenHeight);

        Controller controller = new Controller(scene);

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.widthProperty().addListener((obs, oldVal, newVal) -> { // update canvas width
            Screen.resizeDisplay((double) newVal, (double) pane.getHeight());
        }); 
        pane.heightProperty().addListener((obs, oldVal, newVal) -> { // update canvas height
            Screen.resizeDisplay((double) pane.getWidth(), (double) newVal);
        }); 


        // Hide the cursor
        scene.setCursor(Cursor.NONE);

        // Handle key pressed events
        scene.setOnKeyPressed((KeyEvent event) -> {
            switch (event.getCode()) {
                case Z -> controller.handleKeyPressed("Z");
                case S -> controller.handleKeyPressed("S");
                case Q -> controller.handleKeyPressed("Q");
                case D -> controller.handleKeyPressed("D");
                case SPACE -> controller.handleKeyPressed("SPACE");
                case CONTROL -> controller.handleKeyPressed("CONTROL");
                case SHIFT -> controller.handleKeyPressed("SHIFT");
                case ESCAPE -> controller.handleKeyPressed("ESCAPE");
                default -> System.out.println("wrong");
            }
        });

        // Handle key released events
        scene.setOnKeyReleased((KeyEvent event) -> {
            switch (event.getCode()) {
                case Z -> controller.handleKeyReleased("Z");
                case S -> controller.handleKeyReleased("S");
                case Q -> controller.handleKeyReleased("Q");
                case D -> controller.handleKeyReleased("D");
                case SPACE -> controller.handleKeyReleased("SPACE");
                case CONTROL -> controller.handleKeyReleased("CONTROL");
                case SHIFT -> controller.handleKeyReleased("SHIFT");
                case ESCAPE -> controller.handleKeyReleased("ESCAPE");
                default -> System.out.println("wrong");
            }
        });

        // Mouse movement event
        pane.setOnMouseMoved((MouseEvent event) -> {
            controller.handleMouseMovement(event.getSceneX(), event.getSceneY());
        });
        pane.setOnMouseDragged((MouseEvent event) -> {
            controller.handleMouseMovement(event.getSceneX(), event.getSceneY());
        });
        
        // Start a game loop using AnimationTimer
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //double deltaTime = 0.016;  // Assuming 60fps
                controller.updateCamera();
            }
        };
        gameLoop.start();
        

        stage.setTitle("Render Engine");
        stage.setScene(scene);
        stage.show();

        Task<Void> worldRender = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                        app.start();
                    return null;
                }
            };
            Thread worldRenderThread = new Thread(worldRender);
            worldRenderThread.setDaemon(true);
            worldRenderThread.start();
        
    }


    public static void main(String[] args) throws Exception {     
        launch();
    }

    public static Canvas getCanvas() {
        return canvas;
    }
    
}
