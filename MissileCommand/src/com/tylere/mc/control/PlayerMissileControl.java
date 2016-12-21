/*
 * This class controls the missiles I spawn in, mostly movement controls
 * The FXGL library has a method for adding controls, this is what that refers to
 * It connects a class formatted as an  Abstract Control to the Entity (FXGL's object)
 */
package com.tylere.mc.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.IDComponent;
import com.tylere.mc.EntityFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.util.Duration;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class PlayerMissileControl extends AbstractControl {

    private Point2D velocity;
    private double x, y, rand;
    private IDComponent testID;

    public PlayerMissileControl(double targetX, double targetY) {
        x = targetX;
        y = targetY;

        velocity = new Point2D(0, 0);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        Point2D baseDirection = new Point2D(x, y)
                .subtract(Entities.getPosition(entity).getValue())
                .normalize()
                .multiply(1000);

//        System.out.println(baseDirection.multiply(0.0001).getX() + " " + baseDirection.getY());
//        System.out.println("The x and y targets: " + x + " " + y);
        
    int posX = (int) Entities.getPosition(entity).getValue().getX();
    int posY = (int) Entities.getPosition(entity).getValue().getY();    
//     && (posY <(int) x + 5 && posY > (int) x - 5)
    if ((posX <(int) x + 5 && posX > (int) x - 5)) {
//            System.out.println("Target reached sir!");
            FXGL.getApp().getGameWorld().removeEntity(entity);
             Entity explosion = EntityFactory.newExplosion(x, y);
             FXGL.getApp().getGameWorld().addEntity(explosion);
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(250),
                        ae -> FXGL.getApp().getGameWorld().removeEntity(explosion)));
                timeline.play();
    }

//    System.out.println("Info: " + Entities.getPosition(entity).getValue().getX() + " " + Entities.getPosition(entity).getValue().getY());
    
    velocity  = velocity.add(baseDirection)
            .multiply(0.65);

    Entities.getPosition (entity)
             .translate(velocity.multiply(d * 0.3f));

        // rotate
        if (velocity.getX () 
        != 0 && velocity.getY() != 0) {
            Entities.getRotation(entity).rotateToVector(velocity);
    }
}

}
