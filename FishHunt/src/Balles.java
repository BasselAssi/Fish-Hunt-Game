import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balles extends Entity {

    private double r;

    public Balles(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * Met à jour la position et la vitesse de la balle
     *
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {
        super.update(dt);
    }

    public void setR(double r) {
        this.r = r;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    /**
     * intersect un poisson ou non
     */
    public boolean intersects(PoissonsNormaux other) {

        if (r > 0 && r < 10) {
            return !(
                    x + r < other.x
                            || other.x + other.largeur < this.x
                            || y + hauteur < other.y
                            || other.y + other.hauteur < this.y);
        }else {
            return false;
        }
    }

    public boolean intersects(PoissonsSpeciaux other) {

        if (r > 0 && r < 10) {
            return !(
                    x + r < other.x
                            || other.x + other.largeur < this.x
                            || y + hauteur < other.y
                            || other.y + other.hauteur < this.y);
        }else {
            return false;
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        context.setFill(Color.BLACK);
        context.fillOval(x - r, y - r, r * 2, r * 2);
    }

}
