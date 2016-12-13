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
import com.almasb.fxgl.entity.component.PositionComponent;
import com.tylere.mc.EntityFactory;
import javafx.geometry.Point2D;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class MissileControl extends AbstractControl {

    private Point2D velocity;
    private static int count = 1;
    private int id, x, y, rand;
    private IDComponent testID;

    public MissileControl() {
        velocity = new Point2D(0, 0);
        id = count++;
        IDComponent testID = new IDComponent("trail", 5);
        
        
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        
        Point2D baseDirection = new Point2D(40, 600)
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
        
//        PositionComponent location = entity.getComponentUnsafe(PositionComponent.class);
//        
//        Entity trail = EntityFactory.newTrail(location.getX(), location.getY(), id);
//        
//        FXGL.getApp().getGameWorld().addEntity(trail);
//        System.out.println(testID.getFullID());
//        
//        if (trail.getComponent(IDComponent.class).equals(testID)) {
//            System.out.println("Hooah!");
//        }

    }
   
    
    public int getID() {
        return id;
    }
}
