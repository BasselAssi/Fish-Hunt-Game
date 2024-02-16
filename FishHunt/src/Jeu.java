import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Jeu {
    public static final int WIDTH = FishHunt.WIDTH;
    public static final int HEIGHT = FishHunt.HEIGHT;

    private double compteurBulle;
    private double cibleX, cibleY;
    private double rayonBalles = 50;
    private int score = 0;
    private int tempScore;
    private int largeurPoisson;
    private int level;
    private int distance;
    private int scoreFinal;


    private boolean onClick = false;
    private boolean affichageLevel;
    private boolean GAMEOVER;
    private boolean departDroitePoissonNormaux;
    private boolean departDroitePoissonSpeciaux;
    private boolean scene;


    private double compteurPoissonNormaux;
    private double compteurPoissonNormauxSpeciaux;
    private double compteurLevel;
    private double compteurAffichageLevel;
    private double compteurGameover;

    private String gameOver;

    private ArrayList<Bulles> bubbles;
    private ArrayList<PoissonsNormaux> poissonsNormauxArray;
    private ArrayList<PoissonsSpeciaux> poissonsSpeciauxArray;
    private ArrayList<Image> vies;
    private ArrayList<String> levels;




    Cible cible;
    Balles balles;


    public Jeu() {
        bubbles = new ArrayList<>();
        cible = new Cible(cibleX, cibleY);
        balles = new Balles(cibleX, cibleY, rayonBalles);

        poissonsNormauxArray = new ArrayList<>();
        poissonsSpeciauxArray = new ArrayList<>();
        levels = new ArrayList<>();



        //Les 3 images des vies
        vies = new ArrayList<>();
        for(int i=0; i<3; i++) {
            vies.add(new Image("/00.png"));
        }
    }

    /**
     * update
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {

        cible.x = cibleX - cible.largeur / 2;
        cible.y = cibleY - cible.hauteur / 2;

        compteurLevel+=dt;

        /**
         *
         * Incremente les levels et s'occupe de leurs temps d'affichage
         *
         */
        //Le Level 1
        if ((int)compteurLevel == 1) {
            level=1;
            levels.add("Level "+ level);
            tempScore = 0;
        }

        //Pour les level 2 et plus
        if ((!(score==0)) && (score%5==0) && (levels.isEmpty())) {

            //Pour ne pas afficher le level sans arret lorsqu'on reste au meme niveau
            if (score != tempScore) {

                level++;
                levels.add("Level " + level);
                affichageLevel = true;
                tempScore = score;
            }

        }

        compteurAffichageLevel+=dt;

        //supprimer l'ancien level
        if ((int)compteurAffichageLevel>0 &&((int)compteurAffichageLevel %5==0) &&(!levels.isEmpty())){
                levels.clear();

                //Pour permettre au poissons de recommencer a se generer
                affichageLevel = false;
        }



        /**
         *
         * Affichage de "GAME OVER" lorsque tous nos vies sont perdues
         *
         */
        if (vies.isEmpty()){
            compteurGameover+=dt;

            gameOver = "GAME OVER";
            scoreFinal = score;

            if (compteurGameover>3) {
                GAMEOVER = true;
            }
        }

        /**
         * *
         * Genere les poissons normaux et speciaux
         *
         */
        if ((!affichageLevel) && (!GAMEOVER)) {

            //Poissons normaux
            if (((int)compteurPoissonNormaux!=0) && (int)compteurPoissonNormaux %4 == 0) {

                largeurPoisson = (int) randomNumber(120, 80); // genere un x

                poissonsNormauxArray.add(new PoissonsNormaux(genererDepartPoissonNormaux(),
                        randomNumber(0.8 * HEIGHT, 0.2 * HEIGHT), level,
                        largeurPoisson));
                compteurPoissonNormaux++;

            }

            //Poissons speciaux
            if (((int)compteurPoissonNormauxSpeciaux!=0) && ((int)compteurPoissonNormauxSpeciaux %6 == 0)&&(level >1)) {
                poissonsSpeciauxArray.add(new PoissonsSpeciaux(genererDepartPoissonSpeciaux(),
                        randomNumber(0.8 * HEIGHT, 0.2 * HEIGHT), level,largeurPoisson));
                compteurPoissonNormauxSpeciaux++;

            }
        }
        compteurPoissonNormaux += dt;
        compteurPoissonNormauxSpeciaux += dt;


        //efface les poisson qui ne sont plus necessaire.

        if (poissonsNormauxArray.size() > 2 ){
            poissonsNormauxArray.remove(0);
        }
        if (poissonsSpeciauxArray.size() > 2) {

            poissonsSpeciauxArray.remove(0);
        }




        //poisson normaux
        for (PoissonsNormaux p : poissonsNormauxArray) {
            if (departDroitePoissonNormaux) {
                if ((p.x + 120) < 0) {
                    enleverVie();
                    poissonsNormauxArray.remove(p);
                    break;
                }
            } else if (p.x > WIDTH) {
                enleverVie();
                poissonsNormauxArray.remove(p);
                break;
            }

            //Si le poisson est touche
            if (balles.intersects(p)) {
                score++;
                poissonsNormauxArray.clear();
                break;
            }
            p.update(dt);
        }

        // Poisson speciaux
        for (PoissonsSpeciaux p : poissonsSpeciauxArray) {
            if (departDroitePoissonSpeciaux) {
                if (p.x + 120 < 0) {
                    enleverVie();
                    poissonsSpeciauxArray.remove(p);
                    break;
                }
            } else if (p.x > WIDTH) {
                enleverVie();
                poissonsSpeciauxArray.remove(p);
                break;
            }
            //Si le poisson est touche
            if (balles.intersects(p)) {
                score++;
                poissonsSpeciauxArray.clear();
                break;
            }
            p.update(dt);
        }


        if (onClick) {
//            balles = new Balles(cibleX, cibleY, rayonBalles);
            rayonBalles = 50;
            onClick = false;
        } else {
            rayonBalles -= dt * 300;
            balles.setR(rayonBalles);
        }


        for (Bulles b : bubbles) {
            b.update(dt);
        }

        // incremente le temp ecouler
        compteurBulle += dt;

        if ((int) compteurBulle % 3 == 0) {
            removeBubbles();
            bulle();
        }

    }




    public boolean changerScene() {

        return GAMEOVER;
    }

    //Retourne le dernier score
    public int dernierScore(){
        return scoreFinal;
    }


    //Faire augmenter le score
    public void augmenterScore(){
        score++;
    }

    //Faire augmenter le niveau
    public void augmenterNiveau(){
        levels.clear();
        level++;
        levels.add("Level " +level);
    }

    //Faire perdre la patie
    public void gameOver(){
        GAMEOVER = true;
        gameOver = "GAME OVER";
    }

    //Ajouter une vie
    public void ajouterVie(){
        if (vies.size()<3){
            vies.add(new Image("/00.png"));
        }
    }

    //Retirer une vie
    public void enleverVie(){
        if (!vies.isEmpty()) {
            int lastElement = vies.size() - 1;
            vies.remove(lastElement);
        }
    }


    /**
     * construit les bulles
     */
    public void bulle() {

        int y = 480;

        for (int i = 0; i < 3; i++) {

            double position = Math.random() * (WIDTH);

            for (int j = 0; j < 5; j++) {

                double rayon = Math.random() * ((40 - 10) + 10);
                double distance = Math.random() * ((20 + 20) - 20);

                bubbles.add(new Bulles(position + distance, y, rayon));
            }
        }
    }


    public void removeBubbles() {
        /**
         * efface les bulles
         */
        bubbles.clear();
    }

    void positionCible(double x, double y) {
        cibleX = x;
        cibleY = y;
    }

    public void creerBalles() {
        balles.setX(cibleX);
        balles.setY(cibleY);
        balles.setR(rayonBalles);
        onClick = true;
    }

    /**
     * @param max maximum exclus
     * @param min minimum inclus
     * @return un nombre aleatoire generer entre max et min
     */
    public double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

    /**
     * genere un chiffre entre 0 et 1 aleatoirement
     *
     * @return un x à droite ou a gauche de l'ecran
     */
    /**
     * genere un chiffre entre 0 et 1 aleatoirement
     *
     * @return un x à droite ou a gauche de l'ecran
     */
    public int genererDepartPoissonNormaux() {
        if (randomNumber(2, 0) >= 1) {
            departDroitePoissonNormaux = false;
            return -largeurPoisson;
        } else {
            departDroitePoissonNormaux = true;
            return WIDTH + largeurPoisson;
        }
    }

    public int genererDepartPoissonSpeciaux() {
        if (randomNumber(2, 0) >= 1) {
            departDroitePoissonSpeciaux = false;
            return -largeurPoisson;
        } else {
            departDroitePoissonSpeciaux = true;
            return WIDTH + largeurPoisson;
        }
    }


    /**
     * Dessine
     *
     * @param context
     */
    public void draw(GraphicsContext context) {

        //on set la couleur du backround
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);

        //dessine nos bulles
        for (Bulles b : bubbles) {
            b.draw(context);
        }

        //Score
        context.setFill(Color.WHITE);
        context.setFont(new Font("Verdana", 25));
        context.fillText(Integer.toString(score), WIDTH/2 -10, 55);

        //Vies
        distance=0;
        for (Image v : vies) {
            context.drawImage(v,(WIDTH/2 -60 ) +distance ,80,30,30);
            distance+=40;
        }
        //Levels
        for (String s : levels) {
            context.setFont(new Font("Verdana", 50));
            context.fillText(s,WIDTH/2-90, HEIGHT/2);
        }

        //Game over
        context.setFill(Color.RED);
        context.setFont(new Font("Verdana", 70));
        context.fillText((gameOver), WIDTH/2 -200, HEIGHT/2);


        //Cible et balles
        cible.draw(context);
        balles.draw(context);

        //Poissons normaux
        for (PoissonsNormaux p : poissonsNormauxArray) {
            p.draw(context);
        }
        //Poissons speciaux
        for (PoissonsSpeciaux p : poissonsSpeciauxArray) {
            p.draw(context);
        }

    }

}
