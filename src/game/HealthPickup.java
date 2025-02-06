package game;

import city.cs.engine.*;

public class HealthPickup extends DynamicBody {

    private static final BoxShape med = new BoxShape(1,1); // Declare med as a static field
    private final BodyImage medkitImage = new BodyImage("data/MedKit.gif");

    /**
     * Instantiates a new Health pickup.
     *
     * @param w the w
     */

    public HealthPickup(World w) {
        super(w, med); // Use the med shape declared as a static field
        this.addImage(medkitImage);
    }


}
