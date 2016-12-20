/*
 * The main missile command class. JavaFX convention is to have App at the end of the 
 * class name. The format for the package is 'com.name.gameInitials'
 */
package com.tylere.mc;

import com.almasb.ents.Entity; // Entities are the objects that the library can control
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.component.MainViewComponent;
import com.almasb.fxgl.entity.component.TypeComponent;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import com.almasb.fxgl.texture.Texture;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class MissileCommandApp extends GameApplication {

    private Point2D cursor = new Point2D(0, 0);
    private double mouseX, mouseY;
    private int missileCountdown;
    private IntegerProperty citiesDestroyed;

    @Override
    protected void initSettings(GameSettings gs) {
        // The settings code to generate the window and remove FXGL intro for dev
        gs.setWidth(800);
        gs.setHeight(700);
        gs.setTitle("Missile Command");
        gs.setVersion("0.3");
        gs.setIntroEnabled(false);
        gs.setMenuEnabled(false);
        gs.setProfilingEnabled(true);
        gs.setCloseConfirmation(false);
        gs.setApplicationMode(ApplicationMode.DEVELOPER);
        ///////////// This concludes the window generation section /////////////
    }

    @Override
    protected void initInput() {
        // Mouse click to spawn circle that represents friendly missile
        Input input = getInput();
        Point2D cursor = input.getMousePositionWorld();

        double mouseX = cursor.getX();
        double mouseY = cursor.getY();

        input.addAction(new UserAction("Spawn Anti-Ballistic") {
            @Override
            protected void onAction() {
                if (missileCountdown > 15) {
//                EntityFactory.setAmmo(0, 0, 0);
                Input input = getInput();
                Point2D cursor = input.getMousePositionWorld();

                double mouseX = cursor.getX();
                double mouseY = cursor.getY();

                Entity friendlyMissile = EntityFactory.newMissile(mouseX - 15, mouseY - 15, true);
                getGameWorld().addEntity(friendlyMissile);
                missileCountdown = 0;
                } else {
                    // ignore
                }
            }
        }, MouseButton.PRIMARY);
    }

    @Override
    protected void initAssets() {

    }

    @Override
    protected void initGame() {
        // Trying to load a background image
        Pane bg = new Pane();

        Rectangle rectangle = new Rectangle(1000, 1000, Color.BLACK);

        bg.getChildren().add(rectangle);

        Entities.builder()
                .viewFromNode(new EntityView(bg, RenderLayer.BACKGROUND))
                .buildAndAttach(getGameWorld());

        // Cursor image
        Image image = new Image("/assets/ui/cursors/crosshair.png");

        // You need to use a Point2D to set the cursor image position, to center / 2
        Point2D dimensions = new Point2D(image.getWidth() / 2, image.getHeight() / 2);

        // Set the cursor and its proper dimensions
        getGameScene().setCursor("crosshair.png", dimensions);

        // Cities spawning
        for (int x = 1; x <= 6; x++) {
            if (x < 4) {
                Entity city = EntityFactory.newCity(100 * x + 0, getHeight() - 100);
                getGameWorld().addEntity(city);
            } else {
                Entity city = EntityFactory.newCity(100 * x + 50, getHeight() - 100);
                getGameWorld().addEntity(city);
            }
        }

        // Missile Silo spawning
        for (int x = 0; x < 3; x++) {
            Entity silo = EntityFactory.newSilo(360 * x + 25, getHeight() - 120);
            getGameWorld().addEntity(silo);
        }

        ///// Spawning missiles at top of the screen to streak down /////
        getMasterTimer().runAtInterval(() -> {
            Entity axisMissile = EntityFactory.newMissile(Math.random() * getWidth() - 64, 0, false);

            Point2D direction = new Point2D(10, 10);

            getGameWorld().addEntity(axisMissile);
        }, Duration.millis(1000));

        
    }

    @Override
    protected void initPhysics() {
        // Easier to type multiple times
        PhysicsWorld physicsWorld = getPhysicsWorld();

        // We don't need no stinking gravity
        physicsWorld.setGravity(0, 0);

        // Setting collisions between explosions and warheads
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.EXPLOSION, EntityType.ENEMY_MISSILE) {
            @Override
            protected void onCollisionBegin(Entity explosion, Entity missile) {
                missile.removeFromWorld();
            }
        });

        // Setting collisions between cities and warheads
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.CITY, EntityType.ENEMY_MISSILE) {
            @Override
            protected void onCollisionBegin(Entity city, Entity missile) {
                missile.removeFromWorld();
                citiesDestroyed.set(citiesDestroyed.get() - 1);

                city.getComponentUnsafe(TypeComponent.class).setValue(EntityType.DESTROYED_CITY);
                city.getComponentUnsafe(MainViewComponent.class).setView(new EntityView(getAssetLoader().loadTexture("cityDE.png")));
                Point2D explosionLocation = Entities.getPosition(missile).getValue();

                Entity explosion = EntityFactory.newExplosion(explosionLocation.getX(), explosionLocation.getY());
                // Re-implement if I get this working in the future
//                explosion.getComponentUnsafe(MainViewComponent.class).setView(new EntityView(getAssetLoader().loadTexture("explosionSmoke.gif")));

                getGameWorld().addEntity(explosion);
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(250),
                        ae -> getGameWorld().removeEntity(explosion)));
                timeline.play();

                System.out.println("Your city has been nuked!");
            }
        });

        // Setting collisions between destroyed cities and warheads
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.DESTROYED_CITY, EntityType.ENEMY_MISSILE) {
            @Override
            protected void onCollisionBegin(Entity city, Entity missile) {
                missile.removeFromWorld();
                Point2D explosionLocation = Entities.getPosition(missile).getValue();

                Entity explosion = EntityFactory.newExplosion(explosionLocation.getX(), explosionLocation.getY());
                getGameWorld().addEntity(explosion);
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(250),
                        ae -> getGameWorld().removeEntity(explosion)));
                timeline.play();

                System.out.println("Your city has been nuked!");
            }
        });

        // Setting collisions between silos and warheads
        physicsWorld.addCollisionHandler(new CollisionHandler(EntityType.SILO, EntityType.ENEMY_MISSILE) {
            @Override
            protected void onCollisionBegin(Entity silo, Entity missile) {
                missile.removeFromWorld();

                Point2D explosionLocation = Entities.getPosition(missile).getValue();

                Entity explosion = EntityFactory.newExplosion(explosionLocation.getX(), explosionLocation.getY());
                getGameWorld().addEntity(explosion);
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(250),
                        ae -> getGameWorld().removeEntity(explosion)));
                timeline.play();

                System.out.println("Your missile silo has been nuked!");
            }
        });
    }

    @Override
    protected void initUI() {
        // This code moves the game title across the screen, purely aesthetic
        ////////////////////////// Curve code beings /////////////////////////
        Text text = getUIFactory().newText("Missile Command", Color.BLUE, 48);

        QuadCurve curve = new QuadCurve(-10, 0, getWidth() / 2, getHeight(), getWidth() + 100, -20);

        PathTransition transition = new PathTransition(Duration.seconds(4), curve, text);
        transition.setOnFinished(e -> {
            getGameScene().removeUINode(text);
        });

        getGameScene().addUINode(text);
        transition.play();
        /////////////////////////// Curve code ends ///////////////////////////

        this.citiesDestroyed = new SimpleIntegerProperty(6);
        Text cityCounter = getUIFactory().newText("", Color.WHITE, 20);
        cityCounter.setTranslateX(550);
        cityCounter.setTranslateY(100);
        cityCounter.textProperty().bind(citiesDestroyed.asString("Cities Remaining: %d"));
        getGameScene().addUINode(cityCounter);

        Texture texture = getAssetLoader().loadTexture("bg.jpg");

        EntityView bg = new EntityView(texture);

        getGameScene().addGameView(bg);

    }

    @Override
    protected void onUpdate(double d) {
        Input input = getInput();
        Point2D cursor = input.getMousePositionWorld();

        int size = 20;
        double x = cursor.getX();
        double y = cursor.getY();
        missileCountdown++;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
