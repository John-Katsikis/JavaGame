package game;
import city.cs.engine.*;

public class collectablePickup implements CollisionListener{

    Player player1;

    /**
     * Instantiates a new Collectable pickup and according to what object it is, it executes different behaviors
     *
     * @param p, the Player
     */
    public collectablePickup(Player p){
        this.player1 = p;
    }

    @Override
    public void collide(CollisionEvent e) { //checks to see what item player collided with
        Body otherBody = e.getOtherBody();
        switch (otherBody.getClass().getSimpleName()) {
            case "Gun":
                player1.enableFire(true); //allows player to fire
                e.getOtherBody().destroy();
                break;
            case "HealthPickup": //increases player health by 20
                player1.setPlayerHealth(20);
                System.out.println(player1.getPlayerHealth());
                e.getOtherBody().destroy();
            case "Throwable":
                e.getOtherBody().destroy();
            default:
                break;
        }
    }


}
