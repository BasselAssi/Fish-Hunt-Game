//Bassel Assi

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FishHunt extends Application {
    public static final int WIDTH = 640, HEIGHT = 480;
    private Stage primaryStage;
    ListView<String> list;
    ArrayList<String> scores;
    public int scoreFinal;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        primaryStage.setScene(creerSceneA());

        primaryStage.show();
    }


    private Scene creerSceneA() {
        Pane root = new Pane();
        Scene sceneA = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        Image logo = new Image("/logo.png");
        ImageView imageView = new ImageView(logo);

        // Définis la largeur/hauteur du ImageView
        imageView.setFitWidth(400);

        imageView.setPreserveRatio(true);
        imageView.setX(WIDTH / 2 - 200);
        imageView.setY(HEIGHT / 8);

        root.getChildren().add(canvas);
        GraphicsContext context = canvas.getGraphicsContext2D();

        Button nouvellePartie = new Button("Nouvelle partie!");
        Button meilleursScore = new Button("Meilleurs scores");


        nouvellePartie.setLayoutY(HEIGHT - 120);
        meilleursScore.setLayoutY(HEIGHT - 85);
        nouvellePartie.setLayoutX(WIDTH - 380);
        meilleursScore.setLayoutX(WIDTH - 380);

        nouvellePartie.setOnAction((e) -> {
            primaryStage.setScene(creerSceneB());
        });
        meilleursScore.setOnAction((e) -> {
            try {
                primaryStage.setScene(creerSceneC());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);


        root.getChildren().addAll(imageView, nouvellePartie, meilleursScore);

        sceneA.setOnKeyPressed(t -> {
            KeyCode key = t.getCode();

            if (key == KeyCode.ESCAPE) {
                Platform.exit();
            }
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("Fish Hunt");
        primaryStage.getIcons().add(new Image("/logo.png"));
        primaryStage.setScene(sceneA);
        primaryStage.show();

        return sceneA;
    }

    private Scene creerSceneB() {
        Pane root = new Pane();
        Scene sceneB = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);

        root.getChildren().add(canvas);
        GraphicsContext context = canvas.getGraphicsContext2D();

        Controleur controleur = new Controleur();

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;


            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }
                double deltaTime = (now - lastTime) * 1e-9;



                context.clearRect(0, 0, WIDTH, HEIGHT);
                controleur.update(deltaTime);
                controleur.draw(context);

                scoreFinal = controleur.dernierScore();


                if (controleur.changerScene()) {

                        try {
                            primaryStage.setScene(creerSceneC());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        this.stop();
                    }

                lastTime = now;
            }




        };
        timer.start();

        //Differentes touches pour le debug
        sceneB.setOnKeyPressed(t -> {
            KeyCode key = t.getCode();

            if (key == KeyCode.ESCAPE){
                Platform.exit();
            }
            if (key == KeyCode.J){
                controleur.augmenterScore();
            }
            if (key == KeyCode.K) {
                controleur.ajouterVie();
            }
            if (key == KeyCode.L) {
                controleur.gameOver();
            }
            if (key == KeyCode.H) {
                controleur.augenterNiveau();
            }



        });


        root.setOnMouseMoved((event) -> {
            controleur.positionCible(event.getX(), event.getY());
        });
        root.setOnMouseClicked(event -> {
            controleur.creerBalles();
        });

        return sceneB;
    }

    private Scene creerSceneC() throws IOException {
        VBox root = new VBox();
        Scene sceneC = new Scene(root, WIDTH, HEIGHT);
        Text titre = new Text("Meilleurs Scores");
        titre.setFont(Font.font(30));
        root.getChildren().add(titre);
        root.getChildren().add(new Separator());

        HBox buttonGroup = new HBox();

        Button menu = new Button("Menu");
        Button ajouter = new Button("Ajouter!");






        scores = new ArrayList<>();
        list = new ListView<>();

        // determine the high score

        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "MeilleursScores.txt"));
            String line = reader.readLine();
            while (line != null)                 // read the score file line by line
            {
                try {
                    int score = Integer.parseInt(line.trim());   // parse each line as an int
                    if (score > scoreFinal)                       // and keep track of the largest
                    {
                        scoreFinal = score;
                    }
                } catch (NumberFormatException e1) {
                    // ignore invalid scores
                    //System.err.println("ignoring invalid score: " + line);
                }
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException ex) {
            System.err.println("ERROR reading scores from file");
        }

        buttonGroup.getChildren().setAll(menu, ajouter);

            buttonGroup.setAlignment(Pos.CENTER);

            root.getChildren().add(buttonGroup);
            root.getChildren().add(new Separator());


            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);


            sceneC.setOnKeyPressed(t -> {
                KeyCode key = t.getCode();

                if (key == KeyCode.ESCAPE) {
                    Platform.exit();
                }
            });

            menu.setOnAction((e) -> {
                primaryStage.setScene(creerSceneA());
            });

            primaryStage.setScene(sceneC);
            primaryStage.show();



        return sceneC;


    }



        public static void main (String[]args){
            FishHunt.launch(args);

        }
    }
