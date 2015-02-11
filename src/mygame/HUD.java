package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

/**
 *
 * @author Steven Alexander
 * Final Project
 * CMSC325
 * 
 * HUD class creates game HUD actions as well as statistics variables and methods
 * 
 * 
 */
public class HUD {

    private Node guiNode;
    private AssetManager assetManager;
    private AppSettings settings;
    private BitmapFont guiFont;
    private BitmapText timePlayed;
    private BitmapText score;
    private BitmapText accuracy;
    private BitmapText shotsfired;
    private BitmapText killcount;
    private BitmapText shotshit;
    private int gameTime = 0;
    private int shotsFired = 0;
    private int shotsHit = 0;
    private float shotAccuracy = 100;
    private int points = 0;
    private int killCount = 0;
    private int fontSize = 20;
    private ColorRGBA fontColor = new ColorRGBA(0, 0, 0, 0.5f);

    // default constructor
    public HUD() {}
    
    // HUD constructor
    public HUD(AssetManager assetManager, Node guiNode, AppSettings settings, BitmapFont guiFont) {
        this.guiNode = guiNode;
        this.assetManager = assetManager;
        this.settings = settings;
        this.guiFont = guiFont;

        createHUD();
        createText();
    }

    // creates basic HUD settings
    public void createHUD() {
        Picture pic = new Picture("HUD Picture");
        pic.setImage(assetManager, "Interface/HUD.png", true);
        pic.setWidth(settings.getWidth());
        pic.setHeight(settings.getHeight());
        pic.setPosition(settings.getWidth(), settings.getHeight());
        pic.setLocalTranslation(0, 0, 0);
        guiNode.attachChild(pic);

    }

    // creates HUD text overlay
    public void createText() {
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

        // Creates Time Played Text
        timePlayed = new BitmapText(guiFont, false);
        timePlayed.setSize(fontSize);
        timePlayed.setText("Time: " + gameTime + " seconds");
        timePlayed.setLocalTranslation(40, 720, 0);
        timePlayed.setColor(fontColor);
        guiNode.attachChild(timePlayed);

        // Creates Score Text
        score = new BitmapText(guiFont, false);
        score.setSize(fontSize);
        score.setText("Score: " + points);
        score.setLocalTranslation(1160, 720, 0);
        score.setColor(fontColor);
        guiNode.attachChild(score);

        // Creates Bullet Fired Text
        shotsfired = new BitmapText(guiFont, false);
        shotsfired.setSize(fontSize);
        shotsfired.setText("Bullets Fired: " + shotsFired);
        shotsfired.setLocalTranslation(1160, 670, 0);
        shotsfired.setColor(fontColor);
        guiNode.attachChild(shotsfired);

        // Creates Bullets Hit
        shotshit = new BitmapText(guiFont, false);
        shotshit.setSize(fontSize);
        shotshit.setText("Target Hits: " + shotsHit);
        shotshit.setLocalTranslation(1160, 620, 0);
        shotshit.setColor(fontColor);
        guiNode.attachChild(shotshit);

        // Creates Targets Destroyed Text
        killcount = new BitmapText(guiFont, false);
        killcount.setSize(fontSize);
        killcount.setText("Targets Killed: " + killCount);
        killcount.setLocalTranslation(1160, 570, 0);
        killcount.setColor(fontColor);
        guiNode.attachChild(killcount);

        // Creates Accuracy Text
        accuracy = new BitmapText(guiFont, false);
        accuracy.setSize(fontSize);
        accuracy.setText("Accuracy: " + shotAccuracy + "%");
        accuracy.setLocalTranslation(1160, 520, 0);
        accuracy.setColor(fontColor);
        guiNode.attachChild(accuracy);

    }

    // counts game seconds based on Main timer, without main timer reset
    public void updateTime() {
        gameTime++;
        timePlayed.setText("Time: " + gameTime + " seconds");
        if (gameTime >= 100) {
            timePlayed.setColor(new ColorRGBA(1, 0, 0, 0.5f));
        } else {
            timePlayed.setColor(fontColor);
        }
    }

    // game time get method
    public int getTime() {
        return gameTime;
    }

    // bullet count set method
    public void setBulletCount() {
        shotsFired++;
        shotAccuracy = Math.round(((float) shotsHit / (float) shotsFired) * 100);
        accuracy.setText("Accuracy: " + shotAccuracy + "%");
        score.setText("Score: " + points);
        shotshit.setText("Target Hits: " + shotsHit);
        shotsfired.setText("Bullets Fired: " + shotsFired);
        killcount.setText("Targets Killed: " + killCount);

    }

    // shots hit set method
    public void updateShotsHit() {
        shotsHit++;
        shotAccuracy = Math.round(((float) shotsHit / (float) shotsFired) * 100);
        accuracy.setText("Accuracy: " + shotAccuracy + "%");
        shotshit.setText("Target Hits: " + shotsHit);
        shotsfired.setText("Bullets Fired: " + shotsFired);
        score.setText("Score: " + points);
        killcount.setText("Targets Killed: " + killCount);
    }

    // reset all HUD statistics method (for game reset)
    public void resetHUD() {
        this.gameTime = 0;
        this.shotsFired = 0;
        this.shotsHit = 0;
        this.shotAccuracy = 100;
        this.points = 0;
        this.killCount = 0;
        accuracy.setText("Accuracy: " + shotAccuracy + "%");
        shotsfired.setText("Bullets Fired: " + shotsFired);
        shotshit.setText("Target Hits: " + shotsHit);
        score.setText("Score: " + points);
        killcount.setText("Targets Killed: " + killCount);
    }

    // player score set method
    public void setPoints(int num) {
        points = num;
    }

    // player score get method
    public int getPoints() {
        return points;
    }

    // bullets fired get method
    public int getBulletsFired() {
        return shotsFired;
    }

    // accuracy get method
    public float getAccuracy() {
        return shotAccuracy;
    }

    // targets destroyed get method
    public int getKillCount() {
        return killCount;
    }

    // targets destroyed set method
    public void setKillCount() {
        killCount = killCount + 1;
    }
}