import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bulles {
    private double x, y;
    private double r;
    private double vy;

    public Bulles(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
        vy = Math.random() * (450 - 350) + 350;
    }

    public void update(double dt) {
        y -= dt * vy;
    }

    public void draw(GraphicsContext context) {
        context.setFill(Color.rgb(0, 0, 255, 0.4));
        context.fillOval(x, y, r, r);
    }
}