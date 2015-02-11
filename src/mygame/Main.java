package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;

/**
 *
 * @author Steven Alexander
 * Final Project
 * CMSC325
 * 
 * Main program file controls all game objects
 * 
 */
public class Main extends SimpleApplication implements ActionListener, PhysicsCollisionListener {

    public BulletAppState bulletAppState;
    private Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    public WorldGravity currentGravity = new WorldGravity();
    private static int gravityCount;
    private HUD gameHUD;
    private Stage gameStage;
    private GamePlayer gamePlayer;
    private SimpleApplication app;
    private RigidBodyControl bullet_phy;
    private Geometry bullet_geo;
    private Target target1, target2, target3, target4;
    private int endTime = 120;
    private AudioNode ambient;
    private AudioNode audio_gun;
    private AudioNode audio_hit;

    // app settings
    public static void main(String[] args) {
        Main app = new Main();
        AppSettings settings = new AppSettings(true);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        settings.setWidth(1366);
        settings.setHeight(768);
        settings.setTitle("Space Box Destroyer");
        app.setSettings(settings);
        app.setShowSettings(false);
        app.setPauseOnLostFocus(true);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        // start intro JOptionPane
        String startMessage = "\nWelcome to Space Box Destoyer. \n\nYou have 120 seconds to destroy as many of the space boxes as your can. \n\nClick OK when you are ready to begin.\n\n";
        JOptionPane.showMessageDialog(null, startMessage);

        // Set up collision physics with increased accuracy
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setAccuracy(1f / 100f);

        // Hide screen stats
        setDisplayStatView(false);
        setDisplayFps(false);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f)); // background color
        flyCam.setMoveSpeed(50); // player move speed

        setUpKeys(); // keybindings

        initInputs(); // Input Listeners

        gameHUD = new HUD(assetManager, guiNode, settings, guiFont); // creates HUD

        gameStage = new Stage(assetManager, rootNode, bulletAppState); // creates world stage

        gamePlayer = new GamePlayer(assetManager, rootNode, bulletAppState, "testPlayer"); // creates new player stage

        // creates target objects
        target1 = new Target(assetManager, rootNode, bulletAppState, 1);
        target2 = new Target(assetManager, rootNode, bulletAppState, 2);
        target3 = new Target(assetManager, rootNode, bulletAppState, 3);
        target4 = new Target(assetManager, rootNode, bulletAppState, 4);

        // Collision listener
        bulletAppState.getPhysicsSpace().addCollisionListener(this);

        // play an ambient sound
        ambient = new AudioNode(assetManager, "Sounds/Radar 1.wav", false);
        ambient.setLooping(true);
        ambient.setVolume(0.2f);
        rootNode.attachChild(ambient);
        ambient.play();

        // play a shooting sound
        audio_gun = new AudioNode(assetManager, "Sounds/Photon2.wav", false);
        audio_gun.setLooping(false);
        audio_gun.setVolume(0.5f);
        rootNode.attachChild(audio_gun);

    }

    @Override
    public void simpleUpdate(float tpf) {

        bulletAppState.update(tpf);

        // updates player control movement
        Vector3f camDir = cam.getDirection().clone().multLocal(0.6f);
        Vector3f camLeft = cam.getLeft().clone().multLocal(0.4f);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        gamePlayer.setWalkDirection(walkDirection);
        cam.setLocation(gamePlayer.getPhysicsLocation());

        // per second updates
        if (getTimer().getTimeInSeconds() >= 1 && gameHUD.getTime() < endTime) {

            gameHUD.updateTime();
            getTimer().reset();
            gravityCount++;
            
            // random gravity update
            if (gravityCount >= 6) {

                currentGravity.randGravity();
                bulletAppState.getPhysicsSpace().setGravity(currentGravity.getWorldGravity());
                gravityCount = 0;

            }
        }
        
        // stops game at endTime variable
        if (gameHUD.getTime() >= endTime) {
            endState();
        }
    }

    // displays game results, gives option for replay or exit
    private void endState() {

        // stops game
        bulletAppState.setEnabled(false);
        ambient.stop();
        audio_gun.setVolume(0f);
        inputManager.setCursorVisible(true);

        // Screen result stats display
        String screenResults = "Your time is up.  Here are your results:"
                + "\n"
                + "\n Time Played: 120 seconds"
                + "\n Score: " + gameHUD.getPoints()
                + "\n Bullets Fired: " + gameHUD.getBulletsFired()
                + "\n Accuracy: " + gameHUD.getAccuracy() + "%"
                + "\n Targets Destroyed: " + gameHUD.getKillCount()
                + "\n"
                + "\n Would you like to try again?";

        // file result stats write
        String fileResults = "\r\n Time Played: 120 seconds"
                + "\r\n Score: " + gameHUD.getPoints()
                + "\r\n Bullets Fired: " + gameHUD.getBulletsFired()
                + "\r\n Accuracy: " + gameHUD.getAccuracy() + "%"
                + "\r\n Targets Destroyed: " + gameHUD.getKillCount();
        
        // JOptionPane YES/NO
        int endDialog = JOptionPane.showConfirmDialog(null, screenResults, "Game Over", JOptionPane.YES_NO_OPTION);

        // Restarts game on YES
        if (endDialog == JOptionPane.YES_OPTION) {
            bulletAppState.setEnabled(true);
            ambient.play();
            audio_gun.setVolume(0.5f);
            inputManager.setCursorVisible(false);
            gameHUD.resetHUD();
        
        //  Writes game stats to gameScores.txt and stops program on NO
        } else {

            try {
                File file = new File("gameScores.txt");

                if (!file.exists()) {
                    file.createNewFile();
                }
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();

                FileWriter in = new FileWriter(file.getName(), true);
                BufferedWriter out = new BufferedWriter(in);
                out.write("\r\n");
                out.write(dateFormat.format(cal.getTime()));
                out.write(fileResults);
                out.close();
            } catch (Exception e) {
                System.err.println("Error Writing to File.");
            }
            stop();
        }

    }

    // Sets up player keybindings
    private void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");

    }
    
    // action listener for player keybindings
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            left = value;
        } else if (binding.equals("Right")) {
            right = value;
        } else if (binding.equals("Up")) {
            up = value;
        } else if (binding.equals("Down")) {
            down = value;
        } else if (binding.equals("Jump")) {
            gamePlayer.jump();
        }
    }

    // sets up bullet firing keybindings
    public void initInputs() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "shoot");
    }
    
    // action listener for bullets firing 
    public ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("shoot") && !keyPressed) {
                makeBullet();
            }
        }
    };

    // creates bullet object
    public void makeBullet() {

        Sphere p = new Sphere(32, 32, 0.5f, true, false);
        Material bullet_mat;
        bullet_mat = new Material(this.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        bullet_mat.setColor("Color", new ColorRGBA(1f, 0f, 1f, 0.5f));
        bullet_geo = new Geometry("bullet", p);
        bullet_geo.setMaterial(bullet_mat);
        rootNode.attachChild(bullet_geo);

        bullet_phy = new RigidBodyControl(0.001f);
        bullet_geo.addControl(bullet_phy);
        bullet_phy.setFriction(0.0f);
        bullet_phy.setGravity(new Vector3f(0, 9.81f, 0));
        bullet_phy.setLinearVelocity(cam.getDirection(walkDirection).mult(100));
        bullet_phy.setPhysicsLocation(cam.getLocation().add(cam.getDirection()));
        bulletAppState.getPhysicsSpace().add(bullet_phy);
        
        if (gameHUD.getTime() < endTime) {
            gameHUD.setBulletCount();
        }
        
        audio_gun.playInstance();
    }

    // bullet/target collision creates sound
    private void hitTargetEffect(Vector3f collisionPoint) {

        // play a shooting sound
        audio_hit = new AudioNode(assetManager, "Sounds/Lighthits.wav", false);
        audio_hit.setLooping(false);
        audio_hit.setVolume(0.5f);
        rootNode.attachChild(audio_hit);
        audio_hit.playInstance();

    }

    // series of conditionals guiding collision behavior for different types of targets
    public void collision(PhysicsCollisionEvent event) {

        if (event.getNodeA() == null || event.getNodeB() == null) {
        } else {

            if ("bullet".equals(event.getNodeA().getName())) {

                if ("boxBatch1".equals(event.getNodeB().getName())) {

                    hitTargetEffect(event.getPositionWorldOnB());

                    if (target1.getHitCount() >= 2) {
                        // update stats
                        target1.setHitCount();
                        gameHUD.updateShotsHit();
                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target1.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeB().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeB());
                        // create new target
                        target1 = new Target(assetManager, rootNode, bulletAppState, 1);
                    }
                } else if ("boxBatch2".equals(event.getNodeB().getName())) {

                    hitTargetEffect(event.getPositionWorldOnB());

                    if (target2.getHitCount() >= 2) {
                        // update stats
                        target2.setHitCount();
                        gameHUD.updateShotsHit();
                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target2.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeB().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeB());
                        // create new target
                        target2 = new Target(assetManager, rootNode, bulletAppState, 2);
                    }
                } else if ("boxBatch3".equals(event.getNodeB().getName())) {

                    hitTargetEffect(event.getPositionWorldOnB());

                    if (target3.getHitCount() >= 2) {
                        // update stats
                        target3.setHitCount();
                        gameHUD.updateShotsHit();
                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target3.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeB().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeB());
                        // create new target
                        target3 = new Target(assetManager, rootNode, bulletAppState, 3);
                    }
                } else if ("boxBatch4".equals(event.getNodeB().getName())) {

                    hitTargetEffect(event.getPositionWorldOnB());

                    if (target4.getHitCount() >= 2) {
                        // update stats
                        target4.setHitCount();
                        gameHUD.updateShotsHit();
                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target2.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeB().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeB());
                        // create new target
                        target4 = new Target(assetManager, rootNode, bulletAppState, 4);
                    }
                }
                rootNode.detachChildNamed(event.getNodeA().getName());
                bulletAppState.getPhysicsSpace().remove(event.getNodeA());

            } else if ("bullet".equals(event.getNodeB().getName())) {

                if ("boxBatch1".equals(event.getNodeA().getName())) {

                    hitTargetEffect(event.getPositionWorldOnA());

                    if (target1.getHitCount() >= 2) {
                        // update stats
                        target1.setHitCount();
                        gameHUD.updateShotsHit();

                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target1.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeA().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeA());
                        // create new target
                        target1 = new Target(assetManager, rootNode, bulletAppState, 1);
                    }
                } else if ("boxBatch2".equals(event.getNodeA().getName())) {

                    hitTargetEffect(event.getPositionWorldOnA());

                    if (target2.getHitCount() >= 2) {
                        // update stats
                        target2.setHitCount();
                        gameHUD.updateShotsHit();

                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target2.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeA().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeA());
                        // create new target
                        target2 = new Target(assetManager, rootNode, bulletAppState, 2);
                    }
                } else if ("boxBatch3".equals(event.getNodeA().getName())) {

                    hitTargetEffect(event.getPositionWorldOnA());

                    if (target3.getHitCount() >= 2) {
                        // update stats
                        target3.setHitCount();
                        gameHUD.updateShotsHit();
                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target3.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeA().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeA());
                        // create new target
                        target3 = new Target(assetManager, rootNode, bulletAppState, 3);
                    }
                } else if ("boxBatch4".equals(event.getNodeA().getName())) {

                    hitTargetEffect(event.getPositionWorldOnA());

                    if (target4.getHitCount() >= 2) {
                        // update stats
                        target4.setHitCount();
                        gameHUD.updateShotsHit();
                    } else {
                        // Update stats
                        gameHUD.setPoints(gameHUD.getPoints() + target2.getScore());
                        gameHUD.setKillCount();
                        gameHUD.updateShotsHit();
                        // remove from stage 
                        rootNode.detachChildNamed(event.getNodeA().getName());
                        bulletAppState.getPhysicsSpace().remove(event.getNodeA());
                        // create new target
                        target4 = new Target(assetManager, rootNode, bulletAppState, 4);
                    }
                }
                rootNode.detachChildNamed(event.getNodeB().getName());
                bulletAppState.getPhysicsSpace().remove(event.getNodeB());

            }
        }
    }
}