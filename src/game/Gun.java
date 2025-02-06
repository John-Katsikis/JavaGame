package game;
import city.cs.engine.*;

public class Gun extends DynamicBody {

    private static final CircleShape gun = new CircleShape(0.5f);
    private BodyImage pistolImage = new BodyImage("data/scifiPistol.png");
    private static final CircleShape burstGun = new CircleShape(0.5f);
    private BodyImage burstGunImage = new BodyImage("data/burstGun.png");

    /**
     * Instantiates a new Gun, according to what level player picked, changes BodyImage.
     *
     * @param world    the world
     * @param timeline the timeline
     */
    public Gun(World world, String timeline) {
        super(world, gun);
        if (timeline=="Future") {
            this.addImage(pistolImage);
        }
        if(timeline=="Present") {
            this.addImage(new BodyImage("data/Glock18.png",4));
        }

        if(timeline=="Past"){
            this.addImage(new BodyImage("data/pastRevolver.png"));
        }
    }



}
