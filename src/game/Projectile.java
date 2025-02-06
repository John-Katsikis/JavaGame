package game;

import city.cs.engine.*;
import org.jbox2d.common.Vec2;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import javax.swing.*;


public class Projectile extends DynamicBody {

    private static final CircleShape projectile = new CircleShape(0.5f);
    private BodyImage projectileFuture = new BodyImage("data/newLazer.gif");
    private BodyImage projectilePresent = new BodyImage("data/newPresentBullet.gif");
    private BodyImage projectilePast = new BodyImage("data/pastBullet.gif");
     public int originalEnemyHealth = 100;
    public int enemyHealth = 100;
    mapGenerator currentLevel;

    /**
     * Instantiates a new Projectile.
     *
     * @param world the world
     * @param Pos   the pos
     */
    public Projectile(World world, Vec2 Pos) {
        super(world, projectile);
        this.setBullet(true);
        this.setPosition(Pos);
        addCollisionListener(new ProjectileListener());
        addCollisionListener(new WallListener());
        this.addImage(projectileFuture);
    }

    /**
     * Instantiates a new Projectile.
     *
     * @param world the world
     * @param Pos   the pos
     * @param t     the t
     */
    public Projectile(World world, Vec2 Pos, String t) {
        super(world, projectile);
        this.setBullet(true);
        this.setPosition(Pos);
        addCollisionListener(new ProjectileListener());
        addCollisionListener(new WallListener());
        if(t=="Past") {
            this.addImage(projectilePast);
        }
        if(t=="Present"){
            this.addImage(projectilePresent);
        }
        if(t=="Future"){
            this.addImage(projectileFuture);
        }
    }

    private class ProjectileListener implements CollisionListener {
        @Override
        public void collide(CollisionEvent e) {
            switch (e.getOtherBody().getClass().getSimpleName()) { //Will either destroy enemy if health is 0, or destroy both projectiles
                //that collide together
                case "Enemy":
                    destroy();
                    if (enemyHealth > 0) {
                        enemyHealth = enemyHealth - 20;
                    } else {
                        e.getOtherBody().destroy();
                        System.out.println("Enemy eliminated");
                    }
                    break;
                case "Projectile":
                    e.getOtherBody().destroy();
                    destroy();
                    break;
                default:
                    break;
            }
        }
    }

    private class WallListener implements CollisionListener {
        @Override
        public void collide(CollisionEvent e) {
            if (e.getOtherBody() instanceof StaticBody) {
                destroy();
            }
        }
    }

}