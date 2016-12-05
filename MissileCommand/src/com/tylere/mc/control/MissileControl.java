/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tylere.mc.control;

import com.almasb.ents.AbstractControl;
import com.almasb.ents.Entity;
import com.almasb.ents.component.Required;
import com.almasb.fxgl.entity.Entities;
import com.almasb.fxgl.entity.component.PositionComponent;
import com.almasb.fxgl.physics.PhysicsComponent;

/**
 *
 * @author Tyler
 */
public class MissileControl extends AbstractControl {

    private PhysicsComponent missile;
    
     @Override
    public void onAdded(Entity entity) {
        missile = entity.getComponentUnsafe(PhysicsComponent.class);
    }
    
    @Override
    public void onUpdate(Entity entity, double d) {
        
    }

}
