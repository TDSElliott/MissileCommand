/*
 * The main missile command class. JavaFX convention is to have App at the end of the 
 * class name. The format for the package is 'com.name.gameInitials'
 */
package com.tylere.mc;

import com.almasb.ents.Entity;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.GameEntity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputMapping;
import com.almasb.fxgl.input.InputModifier;
import com.almasb.fxgl.input.Trigger;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.scene.Viewport;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.settings.UserProfile;
import com.almasb.fxgl.time.UpdateEvent;
import java.util.Map;
import javafx.animation.PathTransition;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author Tyler D.S. Elliott
 */
public class MissileCommandApp extends GameApplication {

    private GameEntity square;
    private Point2D cursor = new Point2D(0, 0);

    @Override
    protected void initInput() {

    }

    @Override
    protected void initAssets() {

    }

    @Override
    protected void initGame() {
        
        initMissile();
        
        square = Entities.builder()
                .at(300, 300)
                .viewFromNode(new Rectangle(20, 20, Color.BLUE))
                .buildAndAttach(getGameWorld());

        Image image = new Image("/assets/ui/cursors/crosshair.png");

        Point2D dimensions = new Point2D(image.getWidth() / 2, image.getHeight() / 2);

        getGameScene().setCursor("crosshair.png", dimensions);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, 0);
    }

    @Override
    protected void initUI() {
        Text text = getUIFactory().newText("Missile Command", Color.BLUE, 48);
        
        QuadCurve curve = new QuadCurve(-10, 0, getWidth() /2, getHeight(), getWidth() + 100, -20);
        
        PathTransition transition = new PathTransition(Duration.seconds(4), curve, text);
        transition.setOnFinished(e -> {
            getGameScene().removeUINode(text);
    });
        
    getGameScene().addUINode(text);
    transition.play();
    
    }

    @Override
    protected void onUpdate(double d) {
        Input input = getInput();
        Point2D cursor = input.getMousePositionWorld();
        System.out.println("Position X: " + cursor.getX() + " Position Y: " + cursor.getY());

        square.removeFromWorld();
        int size = 20;
        double x = cursor.getX();
        double y = cursor.getY();

        square = Entities.builder()
                .at(cursor.getX() - size / 2, cursor.getY() - size / 2)
                .viewFromNode(new Rectangle(size, size, Color.BLUE))
                .buildAndAttach(getGameWorld());
    }

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
    
    
    private void initMissile() {
        getGameWorld().addEntity(EntityFactory.newMissile(250.0, 250.0));
    }
    

    public static void main(String[] args) {
        launch(args);
    }

}
