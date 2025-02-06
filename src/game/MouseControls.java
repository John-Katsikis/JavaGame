package game;

import org.jbox2d.common.Vec2;

import java.awt.event.*;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseMotionListener;



public class MouseControls extends MouseAdapter {


    Player player1;
    GameView view;
    public static Vec2 mouseCoords;


    /**
     * Instantiates a new Mouse controller.
     *
     * @param p the p
     * @param v the v
     */
    public MouseControls(Player p, GameView v) {
        this.player1 = p;
        this.view = v;

    }

    /**
     * Update player.
     *
     * @param player the player
     */
    public void updatePlayer(Player player){
        player1 = player;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       mouseCoords = view.viewToWorld(e.getPoint()); //gets the coordinates of the last point the player clicked on
      if(player1.isFireEnabled()) {
          if (player1.amountOfBulletsShot() > 0) {
              player1.bulletGotShot();
              player1.fire();
          }
      }
      if(player1.isCanTeleport()){
          player1.teleport(new Vec2(mouseCoords));
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

    }

    @Override
    public void mouseEntered(MouseEvent e){
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e){

    }

}