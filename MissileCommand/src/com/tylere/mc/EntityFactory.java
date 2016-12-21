/*
 * Creates Entities
 */
package com.tylere.mc;

import com.tylere.mc.control.EnemyMissileControl;
import com.almasb.ents.Entity;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.ServiceType;
import com.almasb.fxgl.asset.AssetLoader;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.component.CollidableComponent;
import com.almasb.fxgl.entity.component.IDComponent;
import com.almasb.fxgl.entity.control.ExpireCleanControl;
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
import com.tylere.mc.control.PlayerMissileControl;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class EntityFactory {

    private static AssetLoader assetLoader;
    private GameEntity missile;
    public static int ammoL = 5, ammoR = 5, ammoC = 5;

    static {
        assetLoader = FXGL.getService(ServiceType.ASSET_LOADER);
    }

    public static Entity newMissile(double x, double y, boolean isPlayer) {
        // <editor-fold desc="This is my enemy missile code.">
        if (!isPlayer) { // Baddie
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
                case 0:
                    missile.addControl(new Control00());
                case 1:
                    missile.addControl(new Control01());
                case 2:
                    missile.addControl(new Control02());
                case 3:
                    missile.addControl(new Control03());
                case 4:
                    missile.addControl(new Control04());
                case 5:
                    missile.addControl(new Control05());
                case 6:
                    missile.addControl(new Control06());
                case 7:
                    missile.addControl(new Control07());
                case 8:
                    missile.addControl(new Control08());
            }

            // Adds the actual control class
            missile.addControl(new EnemyMissileControl());

            int id = 42;
            return missile;
            // </editor-fold>
        } else { // Goodie
            // This beautiful simplification of previous code is a collaboration with Mack
            int spawn = 0;
            boolean hasFired = false;
            double towerL = 50, towerC = 400, towerR = 750;
            double distanceL = Math.abs(towerL - x),
                    distanceC = Math.abs(towerC - x),
                    distanceR = Math.abs(towerR - x);
   
            if (ammoL != 0 || ammoC != 0 || ammoR != 0) {
                if (ammoL != 0) {
                    spawn = 0;
                    ammoL--;
                    hasFired = true;
                } else {
                    spawn = 1;
                    ammoC--;
                    MissileCommandApp.ammoC = new SimpleIntegerProperty(ammoC - 1);
                    hasFired = true;
                }
                if (ammoC != 0 && distanceC < distanceL) {
                    spawn = 1;
                    ammoC--;
                    MissileCommandApp.ammoC = new SimpleIntegerProperty(ammoC - 1);
                    ammoL++;
                    hasFired = true;
                }
                if ((ammoR != 0 && distanceR < distanceC) || (ammoR != 0 && ammoC == 0 && ammoL == 0)) {
                    spawn = 2;
                    
                    System.out.println(ammoC);
                    ammoR--;
                    ammoC++;

                    hasFired = true;
                }
            }

            GameEntity missile = new GameEntity();

            missile.getTypeComponent().setValue(EntityType.PLAYER_MISSILE);

            if (hasFired) {
                missile.getPositionComponent().setValue(50 + 350 * spawn, 600);
            } else {
                missile.addControl(new ExpireCleanControl(Duration.ZERO));
            }

            missile.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("missile.png")), true);

            // Adds the actual control class
            missile.addControl(new PlayerMissileControl(x, y));

            System.out.println();
            return missile;

//            if (ammoL > 0 && ammoC > 0 && ammoR > 0) {
//                // This decides the tower to fire from depending on the target location
//                if (x <= 266 && ammoL > 0) {
//                    ammoL--;
//                    missile.getPositionComponent().setValue(50, 600);
//                } else if (x > 266 && x < 532 && ammoC > 0) {
//                    ammoC--;
//                    missile.getPositionComponent().setValue(400, 600);
//                } else if (x >= 532 && ammoR > 0) {
//                    ammoR--;
//                    missile.getPositionComponent().setValue(750, 600);
//                } else {
//                    // This should never happen, just incase I change in future
//                }
//            missile.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("missile.png")), true);
//
//            // Adds the actual control class
//            missile.addControl(new PlayerMissileControl(x, y));
//
//            return missile;
//        }else {
//                return missile;
//            }
        }
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

        // -12 and -13 to center the explosion in the cursor/missile's location
        explosion.getPositionComponent().setValue(x - 12, y - 13);

        // Gif for the explosion, so no animation
        explosion.getMainViewComponent().setView(new EntityView(assetLoader.loadTexture("explosionTwo.gif")), true);

        // For physics
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

    // This is a varargs parameter, it can take in any number of integers
    // and will assign them to the ammo array
    public static void setAmmo(int... ammo) {
        int x = ammo.length;
        if (x == 1) {
            ammoL = ammo[0];
        } else if (x > 1 && x < 3) {
            ammoL = ammo[0];
            ammoC = ammo[1];
        } else if (x >= 3) {
            ammoL = ammo[0];
            ammoC = ammo[1];
            ammoR = ammo[2];
        }
    }

}
