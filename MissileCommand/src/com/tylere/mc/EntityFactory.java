/*
 * Creates Entities
 */
package com.tylere.mc;

import com.tylere.mc.control.MissileControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.RotationComponent;
import com.almasb.fxgl.entity.control.ProjectileControl;
import com.almasb.fxgl.physics.PhysicsComponent;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.jbox2d.dynamics.BodyType;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class EntityFactory {

    private static AssetLoader assetLoader;
    private GameEntity missle;

    static {
        assetLoader = FXGL.getService(ServiceType.ASSET_LOADER);
    }

    public static Entity newMissile(double x, double y, boolean isPlayer) {
        GameEntity missile = new GameEntity();
        missile.getTypeComponent().setValue(EntityType.ENEMY_MISSILE);
        missile.getPositionComponent().setValue(x, y);
        missile.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("missile.png")), true);

        missile.addComponent(new CollidableComponent(true));

        Point2D direction = new Point2D(0, 1);
        Point2D test = new Point2D(Math.toRadians(1000), Math.toRadians(90));

//        PhysicsComponent physics = new PhysicsComponent();
//
//        physics.setBodyType(BodyType.DYNAMIC);
//        
//        physics.setLinearVelocity(1, 1);
//    
//        missile.addControl(new ProjectileControl(test, 10 * 6));
        missile.addControl(new MissileControl());
        
        return missile;
    }

    public void setOnPhysicsInitialized() {
        PhysicsComponent physics = new PhysicsComponent();
        System.out.println("test");
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setLinearVelocity(1, 1);

//        missile.addComponent(physics);
        System.out.println("test2");
    }

    public static Entity newCity(double x, double y) {
        GameEntity city = new GameEntity();
        city.getTypeComponent().setValue(EntityType.CITY);
        city.getPositionComponent().setValue(x, y);
        city.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("city.png")), true);
        city.addComponent(new CollidableComponent(true));

        return city;
    }

    public static Entity newExplosion(double x, double y) {
        GameEntity explosion = new GameEntity();
        explosion.getTypeComponent().setValue(EntityType.EXPLOSION);
        explosion.getPositionComponent().setValue(x, y);
        explosion.getMainViewComponent().setView(new Circle(15, Color.ORANGE), true);
        explosion.addComponent(new CollidableComponent(true));

        return explosion;
    }
}
