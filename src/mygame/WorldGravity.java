package mygame;

import com.jme3.math.Vector3f;
import java.util.Random;

/**
 *
 * @author Steven Alexander
 * Final Project
 * CMSC325
 * 
 * WorldGravity class controls random gravity and contains get/set methods
 * 
 */
public class WorldGravity {

    private Vector3f worldGravity;
    
    // world gravity constructor
    public WorldGravity() {
        worldGravity = (new Vector3f(0, -9.81f, 0));
    }   
        
    // method generates random Vector3f for gravity
    public void randGravity() {
        Random rand = new Random();
        int result = rand.nextInt(5);

        switch (result) {
            case 0:
                setWorldGravity(new Vector3f(-3f, -7.81f, 0));
                break;
            case 1:
                setWorldGravity(new Vector3f(3f, -7.81f, 0));
                break;
            case 2:
                setWorldGravity(new Vector3f(0, -7.81f, -3f));
                break;
            case 3:
                setWorldGravity(new Vector3f(0, -7.81f, 3f));
                break;
            case 4:
                setWorldGravity(new Vector3f(-3, -7.81f, 3f));
                break;
            case 5:
                setWorldGravity(new Vector3f(3f, -7.81f, 3f));
                break;
        }
        
    }
    
    // gravity get method
    public Vector3f getWorldGravity() {
        return worldGravity;
    }
    
    // gravity set method
    public void setWorldGravity(Vector3f gravity) {
        worldGravity = gravity;
    }
    
}