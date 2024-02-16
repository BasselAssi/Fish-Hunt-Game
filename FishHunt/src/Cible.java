import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


public class Cible extends Entity{
    private Image image;

    public Cible(double x, double y){
        this.x = x;
        this.y = y;
        this.largeur = 50;
        this.hauteur = 50;
        this.image = new Image("/cible.png");
    }

    @Override
    public void update(double dt) {
        super.update(dt);
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }
}
