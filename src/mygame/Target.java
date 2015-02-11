package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import java.util.Random;
import jme3tools.optimize.GeometryBatchFactory;

/**
 *
 * @author Steven Alexander
 * Final Project
 * CMSC325
 * 
 * Target class creates Target objects and contains associated methods
 * 
 */
public class Target {
    
    //variables for shared assets
    private BulletAppState bulletAppState;
    private Node rootNode;
    private AssetManager assetManager;
    
    //random location limit variables
    private int xMin = -100;
    private int xMax = 100;
    private int yMin = 0;
    private int yMax = 10;
    private int zMin = -100;
    private int zMax = 100;
    
    // Target specific variables
    private Node boxes;
    private Node optimizedBoxes;
    int hitCount;
    private Material container_mat;
    private RigidBodyControl boxBody;
    private int type;
    private int score;
    
    // default constructor
    public Target() {}
    
    // target constructor
    public Target(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState, int targetType) {
        
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        
        float xloc = randFloat(xMin, xMax);
        float yloc = randFloat(yMin, yMax);
        float zloc = randFloat(zMin, zMax);
        
        ColorRGBA cyan = new ColorRGBA(0, 1, 1, 0.5f);
        ColorRGBA green = new ColorRGBA(0, 1, 0, 0.5f);
        ColorRGBA red = new ColorRGBA(1, 0, 0, 0.5f);
        ColorRGBA yellow = new ColorRGBA(1, 1, 0, 0.5f);
        
        Box box = new Box(3,3,3);

        // generates different target based on target type
        switch (targetType) {
            case 1:                  
                this.hitCount = 2; 
                this.score = 50;
                
                Geometry a1 = new Geometry("a1", box);
                Geometry a2 = a1.clone();
                Geometry a3 = a1.clone();
                Geometry a4 = a1.clone();
                Geometry a5 = a1.clone();
                a1.move(-6, -40, 0);
                a2.move(0, -40, 0);
                a3.move(6, -40, 0);
                a4.move(-6, -46, 0);
                a5.move(6, -46, 0);
                boxes = new Node("boxes");
                boxes.attachChild(a1);
                boxes.attachChild(a2);
                boxes.attachChild(a3);
                boxes.attachChild(a4);
                boxes.attachChild(a5);           
                container_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                container_mat.setColor("Color", cyan);
                container_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                boxes.setMaterial(container_mat);
                optimizedBoxes = GeometryBatchFactory.optimize(boxes, false);
                
                optimizedBoxes.setName("boxBatch1");
                
                this.boxBody = new RigidBodyControl(30f);
                optimizedBoxes.addControl(this.boxBody);
                this.boxBody.setPhysicsLocation(new Vector3f(xloc, yloc, zloc));
                this.boxBody.setFriction(0);                
                rootNode.attachChild(optimizedBoxes);
                bulletAppState.getPhysicsSpace().add(this.boxBody);
                
                break;
                
            case 2: 
                
                this.hitCount = 4; 
                this.score = 100;
                
                Geometry b1 = new Geometry("b1", box);
                Geometry b2 = b1.clone();
                Geometry b3 = b1.clone();
                Geometry b4 = b1.clone();
                Geometry b5 = b1.clone();
                Geometry b6 = b1.clone();
                Geometry b7 = b1.clone();
                
                b1.move(0, -40, 0);
                b2.move(-6, -40, 0);
                b3.move(6, -40, 0);
                b4.move(0, -40, 6);
                b5.move(0, -40, -6);
                b6.move(0, -46, 0);
                b7.move(0, -34, 0);
                
                boxes = new Node("boxes");
                
                boxes.attachChild(b1);
                boxes.attachChild(b2);
                boxes.attachChild(b3);
                boxes.attachChild(b4);
                boxes.attachChild(b5); 
                boxes.attachChild(b6);   
                boxes.attachChild(b7);   
                
                container_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                container_mat.setColor("Color", green);
                container_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                boxes.setMaterial(container_mat);
                optimizedBoxes = GeometryBatchFactory.optimize(boxes, false);
                optimizedBoxes.setName("boxBatch2");
                
                this.boxBody = new RigidBodyControl(20f);
                optimizedBoxes.addControl(this.boxBody);
                this.boxBody.setPhysicsLocation(new Vector3f(xloc, yloc, zloc));
                this.boxBody.setFriction(0);
                rootNode.attachChild(optimizedBoxes);
                
                bulletAppState.getPhysicsSpace().add(this.boxBody);

                break;
                
            case 3:   
                
                this.hitCount = 6; 
                this.score = 200;
                
                Geometry c1 = new Geometry("c1", box);
                Geometry c2 = c1.clone();
                Geometry c3 = c1.clone();
                Geometry c4 = c1.clone();
                Geometry c5 = c1.clone();

                c1.move(0, -40, 0);
                c2.move(-6, -34, 0);
                c3.move(6, -34, 0);
                c4.move(0, -34, 0);
                c5.move(0, -46, 0);

                boxes = new Node("boxes");
                boxes.attachChild(c1);
                boxes.attachChild(c2);
                boxes.attachChild(c3);
                boxes.attachChild(c4);
                boxes.attachChild(c5); 
   
                container_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                container_mat.setColor("Color", red);
                container_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                boxes.setMaterial(container_mat);
                optimizedBoxes = GeometryBatchFactory.optimize(boxes, false);
                optimizedBoxes.setName("boxBatch3");
                
                this.boxBody = new RigidBodyControl(10f);
                optimizedBoxes.addControl(this.boxBody);
                this.boxBody.setPhysicsLocation(new Vector3f(xloc, yloc, zloc));
                this.boxBody.setFriction(0);
                rootNode.attachChild(optimizedBoxes);
                
                bulletAppState.getPhysicsSpace().add(this.boxBody);
                
                break;
                
            case 4:    
                this.hitCount = 8; 
                this.score = 400;
                
                Geometry d1 = new Geometry("d1", box);
                Geometry d2 = d1.clone();
                Geometry d3 = d1.clone();
                Geometry d4 = d1.clone();
                Geometry d5 = d1.clone();

                d1.move(0, -40, 0);
                d2.move(0, -34, 0);
                d3.move(0, -46, 0);
                d4.move(6, -34, 0);
                d5.move(-6, -46, 0);

                boxes = new Node("boxes");
                boxes.attachChild(d1);
                boxes.attachChild(d2);
                boxes.attachChild(d3);
                boxes.attachChild(d4);
                boxes.attachChild(d5); 
   
                container_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                container_mat.setColor("Color", yellow);
                container_mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
                boxes.setMaterial(container_mat);
                optimizedBoxes = GeometryBatchFactory.optimize(boxes, false);
                optimizedBoxes.setName("boxBatch4");
                
                this.boxBody = new RigidBodyControl(5f);
                optimizedBoxes.addControl(this.boxBody);
                this.boxBody.setPhysicsLocation(new Vector3f(xloc, yloc, zloc));
                this.boxBody.setFriction(0);
                rootNode.attachChild(optimizedBoxes);
                bulletAppState.getPhysicsSpace().add(this.boxBody);

                break;  
        }
    }

    // score get method for type of target
    public int getScore() {
        int result = this.score;
        return result;
    }
    
    // hit count get method for type of target
    public int getHitCount() {
        int result = this.hitCount;
        return result;
    }
    
    // reduce target hit count set method for type of target
    public void setHitCount() {
        this.hitCount = hitCount-1;
    }

    // random min-to-max for target random respawn location
    private static float randFloat(float min, float max) {
        Random rand = new Random();
        float result = rand.nextFloat() * (max - min) + min;
        return result;
    }

}