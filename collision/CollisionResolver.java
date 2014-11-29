package com.project2k15.logic.collision;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.project2k15.logic.collision.PropertyRectangle;
import com.project2k15.logic.collision.RectangleTypes;
import com.project2k15.logic.entities.abstracted.MovableObject;

/**
 * Created by FruitAddict on 2014-11-17.
 */
public class CollisionResolver implements RectangleTypes {

    public static boolean[] getCollisionTable(Array<PropertyRectangle> collisionRects, MovableObject obj, int type) {
        /**
         * This method checks for physical collisions
         * between this entity's collision rectangles and those passed as arguement.
         * returns a boolean table in the form [right,top,left,bottom], each indicating
         * if collision on this side happened. Used only for terrain collisions atm
         *
         */
        boolean[] result = new boolean[4];
        for (int i = 0; i < 4; i++) {
            result[i] = false;
        }
        for (PropertyRectangle cR : collisionRects) {
            if (cR.getType() == type) {
                Rectangle intersection = new Rectangle();
                Intersector.intersectRectangles(obj.getCollisionRectangle(), cR, intersection);
                if (intersection.width > 0 || intersection.height > 0) {
                    if (intersection.x > obj.getCollisionRectangle().x) {
                        //Intersects with right side
                        result[0] = true;
                    }
                    if (intersection.y > obj.getCollisionRectangle().y) {
                        //Intersects with top side
                        result[1] = true;
                    }
                    if (intersection.x + intersection.width < obj.getCollisionRectangle().x + obj.getCollisionRectangle().width) {
                        //Intersects with left side
                        result[2] = true;
                    }
                    if (intersection.y + intersection.height < obj.getCollisionRectangle().y + obj.getCollisionRectangle().height) {
                        //Intersects with bottom side
                        result[3] = true;
                    }
                }
            }
        }
        return result;
    }

    public static boolean resolveCollisionsTerrain(float delta, Array<PropertyRectangle> checkRectangles, MovableObject obj) {
        /**
         * Terrain Collision resolver method, takes delta time (time between frames) and array of rectangles to check
         * collisions with. Scales the velocity with delta time, copies the position of the player, moves the player
         * by the scaled velocity and then
         * determines how to resolve all the possible collision cases if any happened (by setting the players position to the
         * old position.)
         */
        Vector2 newVeloc = obj.getVelocity().cpy().scl(delta);
        Vector2 oldPosition = obj.getPosition().cpy();
        obj.getPosition().add(newVeloc);
        obj.getCollisionRectangle().setPosition(obj.getPosition().x, obj.getPosition().y);
        boolean[] collisions = getCollisionTable(checkRectangles, obj, TERRAIN_REC);

        boolean returnValue = false;
        for (int i = 0; i < collisions.length; i++) {
            if (collisions[i]) {
                returnValue = true;
            }
        }

        if (collisions[0] && collisions[1]) {
            //right-top
            if (obj.getVelocity().x > 0 && obj.getVelocity().y < 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y > 0 && obj.getVelocity().x < 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }
        } else if (collisions[0] && collisions[3]) {
            //right-bottom
            if (obj.getVelocity().x > 0 && obj.getVelocity().y > 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y < 0 && obj.getVelocity().x < 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }

        } else if (collisions[2] && collisions[1]) {
            //left-top
            if (obj.getVelocity().x < 0 && obj.getVelocity().y < 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y > 0 && obj.getVelocity().x > 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }
        } else if (collisions[2] && collisions[3]) {
            //left-bottom
            if (obj.getVelocity().x < 0 && obj.getVelocity().y > 0) {
                obj.getVelocity().x = 0;
                obj.getPosition().x = oldPosition.x;
            } else if (obj.getVelocity().y < 0 && obj.getVelocity().x > 0) {
                obj.getVelocity().y = 0;
                obj.getPosition().y = oldPosition.y;
            } else {
                obj.getVelocity().x = 0;
                obj.getVelocity().y = 0;
                obj.setPosition(oldPosition);
            }

        } else {
            if (collisions[0]) {
                //right
                obj.getPosition().x = oldPosition.x;
                obj.getVelocity().x = 0;
            }
            if (collisions[1]) {
                //top
                obj.getPosition().y = oldPosition.y;
                obj.getVelocity().y = 0;
            }
            if (collisions[2]) {
                //left
                obj.getPosition().x = oldPosition.x;
                obj.getVelocity().x = 0;
            }
            if (collisions[3]) {
                //bottom
                obj.getPosition().y = oldPosition.y;
                obj.getVelocity().y = 0;
            }
        }
        obj.getVelocity().scl(obj.getClamping());

        return returnValue;
    }

    public static PropertyRectangle resolveCollisionsByType(Array<PropertyRectangle> checkRectangles, MovableObject obj, int... types) {
        /**
         * Simple collision checking, returns rectangle it collided with if its type matches one provided in the arguments
         */
        for (PropertyRectangle rec : checkRectangles) {
            if (rec.getOwner() != obj && obj.getCollisionRectangle().overlaps(rec)) {
                for(int i : types) {
                    if(rec.getType() == i && rec != obj.getCollisionRectangle()) {
                        return rec;
                    }
                }
            }
        }
        return null;
    }

    public static boolean resolveCollisionSimple(Array<PropertyRectangle> checkRectangles, PropertyRectangle object){
        for(PropertyRectangle rec: checkRectangles){
            if(rec.overlaps(object)){
                return true;
            }
        }
        return false;
    }
}
