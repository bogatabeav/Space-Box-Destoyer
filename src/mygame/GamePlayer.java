package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author Steven Alexander
 * Final Project
 * CMSC325
 * 
 * creates player control object
 * 
 */
public class GamePlayer {
    
    private BulletAppState bulletAppState;
    private Node rootNode;
    private AssetManager assetManager;
    private CharacterControl player;
    
    // default constructor
    public GamePlayer() {}
    
    // GamePlayer constructor
    public GamePlayer(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState, String name) {   
        
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 10f, 1);
        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0, -45, 0));
        bulletAppState.getPhysicsSpace().add(player);
    }
    
    // applies Vector3f direction for player character
    public void setWalkDirection(Vector3f walkdirection) {
        this.player.setWalkDirection(walkdirection);
    }
    
    // applies player jump ability
    public void jump() {
        this.player.jump();
    }
    
    // identifies specific location on player when called
    public Vector3f getPhysicsLocation() {
        return this.player.getPhysicsLocation();
    }
    
}