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
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.entity.control.OffscreenCleanControl;
import com.tylere.mc.control.Control00;
import com.tylere.mc.control.Control01;
import com.tylere.mc.control.Control02;
import com.tylere.mc.control.Control03;
import com.tylere.mc.control.Control04;
import com.tylere.mc.control.Control05;
import com.tylere.mc.control.Control06;
import com.tylere.mc.control.Control07;
import com.tylere.mc.control.Control08;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class EntityFactory {

    private static AssetLoader assetLoader;
    private GameEntity missile;

    static {
        assetLoader = FXGL.getService(ServiceType.ASSET_LOADER);
    }

    public static Entity newMissile(double x, double y, boolean isPlayer) {
        GameEntity missile = new GameEntity();
        missile.getTypeComponent().setValue(EntityType.ENEMY_MISSILE);
        missile.getPositionComponent().setValue(x, y);
        missile.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("missile.png")), true);

        // Components
        missile.addComponent(new CollidableComponent(true));
        
        // Controls
        missile.addControl(new OffscreenCleanControl());
        
        int rand = (int) (Math.random() * 9);

        // Pls just ignore this
        switch (rand) {
            case 0: missile.addControl(new Control00());
            case 1: missile.addControl(new Control01());
            case 2: missile.addControl(new Control02());
            case 3: missile.addControl(new Control03());
            case 4: missile.addControl(new Control04());
            case 5: missile.addControl(new Control05());
            case 6: missile.addControl(new Control06());
            case 7: missile.addControl(new Control07());
            case 8: missile.addControl(new Control08());
        }
        
        // Adds the actual control class
        missile.addControl(new MissileControl());

        int id = 42;
        return missile;
    }

    public static Entity newCity(double x, double y) {
        GameEntity city = new GameEntity();
        city.getTypeComponent().setValue(EntityType.CITY);
        city.getPositionComponent().setValue(x, y);
        city.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("city.png")), true);
        city.addComponent(new CollidableComponent(true));

        return city;
    }
    
    public static Entity newSilo(double x, double y) {
        GameEntity silo = new GameEntity();
        silo.getTypeComponent().setValue(EntityType.SILO);
        silo.getPositionComponent().setValue(x, y);
        silo.getMainViewComponent().setView(new EntityView(new Rectangle(30, 100, Color.RED)), true);
        silo.addComponent(new CollidableComponent(true));

        return silo;
    }

    public static Entity newExplosion(double x, double y) {
        GameEntity explosion = new GameEntity();
        explosion.getTypeComponent().setValue(EntityType.EXPLOSION);
        explosion.getPositionComponent().setValue(x, y);
        explosion.getMainViewComponent().setView(new Circle(15, Color.ORANGE), true);
        explosion.addComponent(new CollidableComponent(true));

        return explosion;
    }
    
    public static Entity newTrail(double x, double y, int id) {
        GameEntity trail = new GameEntity();
        trail.getTypeComponent().setValue(EntityType.TRAIL);        
        trail.getPositionComponent().setValue(x, y);
        trail.getMainViewComponent().setView(new Rectangle(1, 1, Color.RED));
        trail.addComponent(new CollidableComponent(true));
        
        trail.addComponent(new IDComponent("trail", id));
       
        return trail;
    }
}
