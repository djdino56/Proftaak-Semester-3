/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wotf.game.classes.Items;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author chaos
 */
public class Nuke extends MissileLauncher {

    /* Sprite clusterbomb_sprite = new Sprite(new Texture(Gdx.files.internal("clusterbomb.png")));
    
    String nm = "Clusterbomb";
    int pw = 10;
    int rad= 30;
    int damage = 30;*/
    public Nuke(String nm, int pw, int rad, int damage, Sprite weaponSprite, Sprite bulletSprite) {
        super(nm, pw, rad, damage, weaponSprite, bulletSprite);
    }

    /**
     * trigger the activation method of the object
     *
     * @param position position from where it is fired
     * @param mousePos pisition where it fires too
     * @param Wind wind affection
     * @param grav gravity affection
     */
    @Override
    public void activate(Vector2 position, Vector2 mousePos, Vector2 Wind, double grav) {
        super.activate(position, mousePos, Wind, grav);
    }
}
