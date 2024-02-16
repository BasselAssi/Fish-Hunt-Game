import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PoissonsSpeciaux extends Entity {
    Image image;
    private boolean poissonEtoile;
    private boolean coteDroit;
    private boolean recule;

    public PoissonsSpeciaux(double x, double y,int level, double taille) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.largeur = taille;
        this.hauteur = taille;

        verificationCote();

        vx = (100 * Math.pow(level, 1 / 3)) + 200;
        vy = 50;

        Image[] imageArray = new Image[2];
        imageArray[0] = new Image("star.png");
        imageArray[1] = new Image("crabe.png");
        int quellePoisson = (int) (Math.random() * 2);
        poissonEtoile = quellePoisson == 0;

        if (coteDroit) {
            image = ImageHelpers.flop(imageArray[quellePoisson]);
        } else {
            image = imageArray[quellePoisson];
        }
    }

    public void verificationCote() {
        if (x > FishHunt.WIDTH) {
            coteDroit = true;
        }
    }

    double compteurEtoile = 500;

    @Override
    public void update(double dt) {
        compteurEtoile -= dt * 1000;

        if (coteDroit) {
            if (poissonEtoile) {
                x -= dt * vx;       // si l' ETOILE apparait du cote droit

            } else {
                x -= dt * vx;       // si le CRABE apparait du cote droit
            }

        } else {

            if (poissonEtoile) {
                x += dt * vx;       // si l'ETOILE aparait du cote gauche

            } else {
                x += dt * vx;       // si le CRABE apparait du cote gauche
            }
        }

        if (poissonEtoile) {
            y += dt * vy;
            if (compteurEtoile < 0) {
                vy *= -1;
                compteurEtoile = 500;
            }
        } else {
            if (compteurEtoile < 0 && !recule) {
                vx = 100 * Math.pow(level, 1 / 3) + 200;
                vx *= -1.3;
                recule = true;
                compteurEtoile = 250;
            }

            if (compteurEtoile < 0 && recule) {
                vx *= -1;
                recule = false;
                compteurEtoile = 500;
            }
        }
    }

    @Override
    public void draw(GraphicsContext context) {
        context.drawImage(image, x, y, largeur, hauteur);
    }
}
