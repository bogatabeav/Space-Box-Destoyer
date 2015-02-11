package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.StripBox;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

/**
 *
 * @author Steven Alexander
 * Final Project
 * CMSC325
 * 
 * Contains world stage objects, as well as cosmetic additions
 * 
 */
public class Stage {
    
    private BulletAppState bulletAppState;
    private Node rootNode;
    private AssetManager assetManager;
    
    // default stage constructor
    public Stage() {}
    
    // stage constructor
    public Stage(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        
        // creates playfield physics container box
        StripBox container = new StripBox(200f, 50f, 200f);
        Geometry geo_container = new Geometry("container", container);
        Material container_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        container_mat.setColor("Color", new ColorRGBA(0,0,0,0f));
        container_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        geo_container.setMaterial(container_mat);
        geo_container.setQueueBucket(RenderQueue.Bucket.Transparent);       
        RigidBodyControl container_phy = new RigidBodyControl(0);
        geo_container.addControl(container_phy);
        rootNode.attachChild(geo_container);
        bulletAppState.getPhysicsSpace().add(container_phy);
        
        // creates visible playfield platform
        Quad floor = new Quad(400,400);
        Geometry geo_floor = new Geometry("floor", floor);
        geo_floor.rotate(-1.57f,0,0);
        Material floor_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        floor_mat.setTexture("ColorMap", assetManager.loadTexture("Textures/Tron.jpg"));
        geo_floor.setMaterial(floor_mat);
        geo_floor.setLocalTranslation(new Vector3f(-200,-50,200));
        rootNode.attachChild(geo_floor);
        
        // creates space background skybox
        Texture north = assetManager.loadTexture("Textures/jajspace2_front.jpg");
        Texture south = assetManager.loadTexture("Textures/jajspace2_back.jpg");
        Texture east = assetManager.loadTexture("Textures/jajspace2_right.jpg");
        Texture west = assetManager.loadTexture("Textures/jajspace2_left.jpg");
        Texture up = assetManager.loadTexture("Textures/jajspace2_top.jpg");
        Spatial skyBox = SkyFactory.createSky(assetManager, west, east, north, south, up, up);
        rootNode.attachChild(skyBox);
        
    }
    
}
