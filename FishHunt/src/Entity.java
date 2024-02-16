import javafx.scene.canvas.GraphicsContext;

public abstract class Entity {
    protected double largeur;
    protected double hauteur;
    protected double x, y;
    protected double vx;
    protected double vy;
    protected double ax, ay;
    protected int level;

    /**
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt) {
        vx += dt * ax;
        vy += dt * ay;
        x += dt * vx;
        y += dt * vy;

        // Force à rester dans les bornes de l'écran
        if (x + largeur > FishHunt.WIDTH || x < 0) {
            vx *= -1;
        }
        if (y + hauteur > FishHunt.HEIGHT || y < 0) {
            vy *= -1;
        }
        x = Math.min(x, FishHunt.WIDTH - largeur);
        x = Math.max(x, 0);
        y = Math.min(y, FishHunt.HEIGHT - hauteur);
        y = Math.max(y, 0);
    }

    public abstract void draw(GraphicsContext context);
}
