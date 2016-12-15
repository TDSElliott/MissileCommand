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
public class Control04 extends AbstractControl {

    @Override
    public void onUpdate(Entity entity, double d) {
    }

}
