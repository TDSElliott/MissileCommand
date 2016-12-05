/*
 * Creates Entities
 */
package com.tylere.mc;

import com.tylere.mc.control.MissileControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class EntityFactory {
    public static Entity newMissile(double x, double y) {
        GameEntity missile = new GameEntity();
        missile.getTypeComponent().setValue(EntityType.PLAYER_MISSILE);
        missile.getPositionComponent().setValue(x, y);
        missile.getBoundingBoxComponent().addHitBox(new HitBox("BODY", BoundingShape.circle(5)));
        missile.getMainViewComponent().setView(new Circle(5, Color.BLUE));
        
        PhysicsComponent missilePhysics = new PhysicsComponent();
        missilePhysics.setBodyType(BodyType.DYNAMIC);
        
        FixtureDef def = new FixtureDef();
        def.setDensity(0.3f);
        def.setRestitution(1.0f);
        
        missilePhysics.setFixtureDef(def);
        missilePhysics.setOnPhysicsInitialized(() -> missilePhysics.setLinearVelocity(5 * 60, -5 * 60));
        
        missile.addComponent(missilePhysics);
        missile.addComponent(new CollidableComponent(true));
        missile.addControl(new MissileControl());
        
        return missile;
    }
}
