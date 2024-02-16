import javafx.scene.canvas.GraphicsContext;

public class Controleur {

    Jeu jeu;

    public Controleur() {
        jeu = new Jeu();
    }

    void draw(GraphicsContext context) {
        jeu.draw(context);
    }

    void update(double deltaTime) {
        jeu.update(deltaTime);
    }

    void positionCible(double x, double y){
        jeu.positionCible(x, y);
    }

    public void creerBalles() {
        jeu.creerBalles();
    }
    public void ajouterVie(){
        jeu.ajouterVie();
    }

    public void gameOver(){
        jeu.gameOver();
    }

    public void augenterNiveau(){
        jeu.augmenterNiveau();
    }
    public void augmenterScore(){
        jeu.augmenterScore();
    }
    public boolean changerScene(){
        return jeu.changerScene();

    }
    public int dernierScore(){
        return jeu.dernierScore();
    }


}
