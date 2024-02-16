import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Random;

public class PoissonsNormaux extends Entity {
    Image image;
    private boolean coteDroit;
    // pour savoir si le poisson est generer du coter droit


    public PoissonsNormaux(double x, double y, int level, double taille) {
        this.x = x;
        this.y = y;
        this.level = level;
        this.largeur = taille;
        this.hauteur = taille;

        verificationCote();
        vx = 100 * Math.pow(level, 1/3) + 200;
        vy = Math.random() * (200 - 100) + 100;
        ay -= 100;

        Image[] imageArray = new Image[8];
        int picNumber = 0;
        for (int i = 0; i < imageArray.length; i++) {
            imageArray[i] = new Image("0" + picNumber + ".png");
            picNumber++;
        }
        int indexImage = (int) (Math.random() * 8);

        if (coteDroit) {
            image =
                    ImageHelpers.flop(ImageHelpers.colorize(imageArray[indexImage],
                            Color.rgb(r, g, b)));
        } else {
            image = ImageHelpers.colorize(imageArray[indexImage], Color.rgb(r, g, b));
        }

    }

    /**
     * verifie le poisson est generer de quelle coté.
     */
    public void verificationCote() {
        if (x > FishHunt.WIDTH) {
            coteDroit = true;
        }
    }

    @Override
    public void update(double dt) {

        if (coteDroit) {
            x -= dt * vx;
        } else {
            x += dt * vx;
        }
        y -= dt * vy;
        vy += dt * ay;
    }

    Random random = new Random();
    int r = random.nextInt(255);
    int g = random.nextInt(255);
    int b = random.nextInt(255);

    @Override
    public void draw(GraphicsContext context) {

        context.drawImage(image, x, y, largeur, hauteur);
    }
}
