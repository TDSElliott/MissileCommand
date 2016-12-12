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
        
        int rand = (int) (Math.random() * 9);
        switch (rand) {
            case 0: x = 40; y = 600;
            case 1: x = 156; y = 600;
            case 2: x = 243; y = 600;
            case 3: x = 330; y = 600;
            case 4: x = 400; y = 600;
            case 5: x = 471; y = 600;
            case 6: x = 558; y = 600;
            case 7: x = 645; y = 600;
            case 8: x = 754; y = 600;
        }
        System.out.println("rand generated: " + rand);
    }

    @Override
    public void onUpdate(Entity entity, double d) {
        
        
        System.out.println(rand + " " + x + " " + y);
        
        
        
        Point2D baseDirection = new Point2D(x, y)
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
