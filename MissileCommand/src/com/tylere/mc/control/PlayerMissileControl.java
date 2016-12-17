/*
 * This class controls the missiles I spawn in, mostly movement controls
 * The FXGL library has a method for adding controls, this is what that refers to
 * It connects a class formatted as an  Abstract Control to the Entity (FXGL's object)
 */
package com.tylere.mc.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.IDComponent;
import javafx.geometry.Point2D;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class PlayerMissileControl extends AbstractControl {

    private Point2D velocity;
    private static int count = 1;
    private double id, x, y, rand;
    private IDComponent testID;

    public PlayerMissileControl(double targetX, double targetY) {
        double x = targetX;
        double y = targetY;
        velocity = new Point2D(0, 0);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        Point2D baseDirection = new Point2D(500, 500)
                .subtract(Entities.getPosition(entity).getValue())
                .normalize()
                .multiply(1000);
        
        velocity = velocity.add(baseDirection)
                .multiply(0.65);

        Entities.getPosition(entity).translate(velocity.multiply(d * 0.1f));

        // rotate
        if (velocity.getX() != 0 && velocity.getY() != 0) {
            Entities.getRotation(entity).rotateToVector(velocity);
        }
    }

}
