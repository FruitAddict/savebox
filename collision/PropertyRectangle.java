package com.project2k15.logic.collision;

import com.badlogic.gdx.math.Rectangle;
import com.project2k15.logic.entities.abstracted.Entity;

/**
 * Custom Rectangle, contains type for collision identification and owner
 */
public class PropertyRectangle extends Rectangle {
    private Entity owner;
    private int type;
    private int linkID = -1; //Additional info, can really be anything. Used to identify portal links atm

    /**
     * Set of 4 constructors to use in different situations.
     */

    public PropertyRectangle(float x, float y, float width, float height, int type) {
        super(x, y, width, height);
        this.type = type;
    }

    public PropertyRectangle(float x, float y, float width, float height, int type, int linkID) {
        super(x, y, width, height);
        this.type = type;
        this.linkID = linkID;
    }

    public PropertyRectangle(float x, float y, float width, float height, Entity owner, int type) {
        super(x, y, width, height);
        this.owner = owner;
        this.type = type;
    }

    public PropertyRectangle(float x, float y, float width, float height, Entity owner, int type, int linkID) {
        super(x, y, width, height);
        this.owner = owner;
        this.type = type;
        this.linkID = linkID;
    }

    public PropertyRectangle(Rectangle rec, int type) {
        super(rec);
        this.type = type;
    }

    public PropertyRectangle(int type) {
        super();
        this.type = type;
    }

    private PropertyRectangle() {
    }

    public int getType() {
        return type;
    }

    public int getLinkID(){
        return linkID;
    }

    public Entity getOwner() {
        return owner;
    }
}
