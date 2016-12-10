/*
 * The main missile command class. JavaFX convention is to have App at the end of the 
 * class name. The format for the package is 'com.name.gameInitials'
 */
package com.tylere.mc;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.EntityView;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.entity.RenderLayer;
import com.almasb.fxgl.entity.control.ProjectileControl;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.PhysicsWorld;
import com.almasb.fxgl.settings.GameSettings;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.gameutils.math.Vec2;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class MissileCommandApp extends GameApplication {

    private Point2D cursor = new Point2D(0, 0);

    @Override
    protected void initSettings(GameSettings gs) {
        // The settings code to generate the window and remove FXGL intro for dev
        gs.setWidth(600);
        gs.setHeight(600);
        gs.setTitle("Missile Command");
        gs.setVersion("0.1");
        gs.setIntroEnabled(false);
        gs.setMenuEnabled(false);
        gs.setProfilingEnabled(false);
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

        input.addAction(new UserAction("Spawn Anti-Ballastic") {
            @Override
            protected void onAction() {
                Input input = getInput();
                Point2D cursor = input.getMousePositionWorld();

                double mouseX = cursor.getX();
                double mouseY = cursor.getY();

                Entity explosion = EntityFactory.newExplosion(mouseX - 15, mouseY - 15);
                getGameWorld().addEntity(explosion);
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(250),
                        ae -> getGameWorld().removeEntity(explosion)));
                timeline.play();
            }
        }, MouseButton.PRIMARY);
        
        input.addAction(new UserAction("Spawn Box") {
            @Override
            protected void onActionBegin() {
                GameEntity box = createPhysicsEntity();

                // 3. set hit box (-es) to specify bounding shape
                box.getBoundingBoxComponent()
                        .addHitBox(new HitBox("Left", BoundingShape.box(40, 40)));
                box.getBoundingBoxComponent()
                        .addHitBox(new HitBox("Right", new Point2D(40, 0), BoundingShape.box(40, 40)));

                box.getMainViewComponent().setView(new Rectangle(80, 40, Color.BLUE));

                getGameWorld().addEntity(box);
            }
        }, MouseButton.SECONDARY);
        
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
        for (int x = 0; x < 6; x++) {
            Entity city = EntityFactory.newCity(100 * x + 25, getHeight() - 100);
            getGameWorld().addEntity(city);
        }

        ///// Spawning missiles at top of the screen to streak down /////
        getMasterTimer().runAtInterval(() -> {
            Entity axisMissile = EntityFactory.newMissile(Math.random() * getWidth() - 64, 100, false);

            Point2D direction = new Point2D(10, 10);

            getGameWorld().addEntity(axisMissile);
        }, Duration.millis(250));
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
    }

    @Override
    protected void initUI() {

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
    }

    @Override
    protected void onUpdate(double d) {
        Input input = getInput();
        Point2D cursor = input.getMousePositionWorld();
//        System.out.println("Position X: " + cursor.getX() + " Position Y: " + cursor.getY());

        int size = 20;
        double x = cursor.getX();
        double y = cursor.getY();
    }

    private GameEntity createPhysicsEntity() {
        PhysicsComponent physics = new PhysicsComponent();

        physics.setBodyType(BodyType.DYNAMIC);

        FixtureDef fd = new FixtureDef();
        fd.setDensity(0f);
        fd.setRestitution(0f);
        physics.setFixtureDef(fd);
        Vec2 force = new Vec2(0, 0);
        physics.applyBodyForceToCenter(force);

        return Entities.builder()
                .at(getInput().getMousePositionWorld())
                // 2. add physics component
                .with(physics)
                .build();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
