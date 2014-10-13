import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Romain
 */
public class Mario {
    
    public static int MarioPositionX = 0;
    public static int MarioPositionY = 0;
    public static PImage MarioStandbye = null;
    public static PImage MarioJump = null;
    public static PImage MarioRunLeft1 = null;
    public static PImage MarioRunLeft2 = null;
    public static PImage MarioRunLeft3 = null;
    public static PImage MarioRunLeft4 = null;
    public static PImage MarioRunLeft5 = null;
    public static PImage MarioRunRight1 = null;
    public static PImage MarioRunRight2 = null;
    public static PImage MarioRunRight3 = null;
    public static PImage MarioRunRight4 = null;
    public static PImage MarioRunRight5 = null;
    public static AnimationSprite AnimMarioRunRight = null;
    public static AnimationSprite AnimMarioRunLeft = null;
    public static PImage[] imgMarioRunRight = new PImage[5];
    public static PImage[] imgMarioRunLeft = new PImage[5];
    
    private PApplet engine = null;
    
    public Mario(PApplet _engine, int _MarioPositionX, int _MarioPositionY)
    {
        engine = _engine;
        MarioPositionX = _MarioPositionX;
        MarioPositionY = _MarioPositionY;
        initImage();
        
        AnimMarioRunRight = new AnimationSprite(imgMarioRunRight,4, engine);
        AnimMarioRunLeft = new AnimationSprite(imgMarioRunLeft,4, engine);
    }
    
    public void initImage()
    {
        int XMarioSize = 24;
        int YMarioSize = 28;
        MarioStandbye = engine.loadImage("Ressource/img/spriteSource3.gif").get(23, 507, 14, 18);
        MarioStandbye.resize(XMarioSize, YMarioSize);
        MarioJump = engine.loadImage("Ressource/img/spriteSource3.gif").get(140, 507, 14, 18);
        MarioJump.resize(XMarioSize, YMarioSize);
        MarioRunRight1 = engine.loadImage("Ressource/img/spriteSource3.gif").get(67, 507, 14, 18);
        MarioRunRight1.resize(XMarioSize, YMarioSize);
        MarioRunRight2 = engine.loadImage("Ressource/img/spriteSource3.gif").get(85, 507, 14, 18);
        MarioRunRight2.resize(XMarioSize, YMarioSize);
        MarioRunRight3 = engine.loadImage("Ressource/img/spriteSource3.gif").get(100, 507, 14, 18);
        MarioRunRight3.resize(XMarioSize, YMarioSize);
        MarioRunRight4 = engine.loadImage("Ressource/img/spriteSource3.gif").get(118, 507, 14, 18);
        MarioRunRight4.resize(XMarioSize, YMarioSize);
        
        MarioRunLeft1 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(600, 507, 14, 18);
        MarioRunLeft1.resize(XMarioSize, YMarioSize);
        MarioRunLeft2 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(583, 507, 14, 18);
        MarioRunLeft2.resize(XMarioSize, YMarioSize);
        MarioRunLeft3 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(566, 507, 14, 18);
        MarioRunLeft3.resize(XMarioSize, YMarioSize);
        MarioRunLeft4 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(547, 507, 14, 18);
        MarioRunLeft4.resize(XMarioSize, YMarioSize);
        
        imgMarioRunRight[0] = MarioRunRight2;
        imgMarioRunRight[1] = MarioRunRight3;
        imgMarioRunRight[2] = MarioRunRight4;
        imgMarioRunRight[3] = MarioRunRight3;
        imgMarioRunRight[4] = MarioRunRight2;
        
        imgMarioRunLeft[0] = MarioRunLeft2;
        imgMarioRunLeft[1] = MarioRunLeft3;
        imgMarioRunLeft[2] = MarioRunLeft4;
        imgMarioRunLeft[3] = MarioRunLeft3;
        imgMarioRunLeft[4] = MarioRunLeft2;
    }
    
    public void RestoreMoveVariable()
    {
        FirstAnimRunLeft = 0;
        FirstAnimRunRight = 0;
    }
    
    public void draw()
    {
        if (engine.keyPressed)
        {
            keyPressed(engine.keyEvent);
            /*if (engine.keyPressed(engine.keyEvent) == engine.UP){
                if (engine.keyCode == engine.LEFT){
                    AnimUP(MarioPositionX,MarioPositionY);
                    AnimRunLeft(MarioPositionX,MarioPositionY);
                }
                else
                    AnimUP(MarioPositionX,MarioPositionY);
            }
            else if (engine.keyCode == engine.DOWN){
                AnimDOWN(MarioPositionX,MarioPositionY);
            }
            if (engine.keyCode == engine.LEFT){
                AnimRunLeft(MarioPositionX,MarioPositionY);
            }
            else if (engine.keyCode == engine.RIGHT){
                AnimRunRight(MarioPositionX,MarioPositionY);
            }*/
            //else
                //engine.image(MarioStandbye, MarioPositionX, MarioPositionY);
        }
        else
            engine.image(MarioStandbye, MarioPositionX, MarioPositionY);
    }
    
    private void keyPressed(KeyEvent keyEvent)
    {
            if (engine.keyCode == engine.UP){
                if (engine.keyCode == engine.LEFT){
                    AnimUP(MarioPositionX,MarioPositionY);
                    AnimRunLeft(MarioPositionX,MarioPositionY);
                }
                else
                    AnimUP(MarioPositionX,MarioPositionY);
            }
            else if (engine.keyCode == engine.DOWN){
                AnimDOWN(MarioPositionX,MarioPositionY);
            }
            if (engine.keyCode == engine.LEFT){
                AnimRunLeft(MarioPositionX,MarioPositionY);
            }
            else if (engine.keyCode == engine.RIGHT){
                AnimRunRight(MarioPositionX,MarioPositionY);
            }
        
    }
    
    public int FirstAnimRunRight = 0;
    public void AnimRunRight(int x, int y)
    {
        if (engine.frameCount % 8 == 0){
            
        
            if (FirstAnimRunRight == 0){
            engine.image(MarioRunRight1, x, y);
            FirstAnimRunRight++;
            return;
            }
            AnimMarioRunRight.display(x, y);
        }
        else
            AnimMarioRunRight.display(x, y, AnimMarioRunRight.frame);
        MarioPositionX = MarioPositionX + 3;
    }
    
    public int FirstAnimRunLeft = 0;
    public void AnimRunLeft(int x, int y)
    {
        if (FirstAnimRunRight == 0){
            engine.image(MarioRunLeft1, x, y);
            FirstAnimRunLeft++;
        }
        if (engine.frameCount % 8 == 0)
            AnimMarioRunLeft.display(x, y);
        else
            AnimMarioRunLeft.display(x, y, AnimMarioRunLeft.frame);
        if (MarioPositionX < 3)
            MarioPositionX = 0;
        else
            MarioPositionX = MarioPositionX - 3;
    }

    private void AnimUP(int x, int y) {
        engine.image(MarioJump, x, y);
        MarioPositionY = MarioPositionY - 3;
    }
    
    private void AnimDOWN(int x, int y) {
        engine.image(MarioJump, x, y);
        MarioPositionY = MarioPositionY + 3;
    }

    int getPositionX() {
        return MarioPositionX;
    }

    int getPositionY() {
        return MarioPositionY;
    }

    
    
}
