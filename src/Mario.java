import java.util.Timer;
import java.util.TimerTask;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 *
 * @author Romain
 */
public class Mario {
    public static PImage MarioStandbye = null;
    public static PImage MarioJump = null;
    public static PImage MarioDie = null;
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
    public static PImage[] imgMarioRunRight = new PImage[5];
    public static PImage[] imgMarioRunLeft = new PImage[5];
    public static final double JUMP_POWER = 8.0; // how hard the player jolts upward on jump
    public static final double RUN_SPEED = 3.0; // force of player movement on ground, in pixels/cycle
    public static final double AIR_RUN_SPEED = 1.0; // like run speed, but used for control while in the air
    public static final double SLOWDOWN_PERC = 0.6; // friction from the ground. multiplied by the x speed each frame.
    public static final double AIR_SLOWDOWN_PERC = 0.85; // resistance in the air, otherwise air control enables crazy speeds
    public static final int RUN_ANIMATION_DELAY = 3; // how many game cycles pass between animation updates?
    public static final double TRIVIAL_SPEED = 1.0; // if under this speed, the player is drawn as standing still
    public PVector position,velocity; // PVector contains two floats
    public Boolean isOnGround; // used to keep track of whether the player is on the ground. useful for control and animation.
    public Boolean facingRight; // used to keep track of which direction the player last moved in. used to flip player image.
    public int animDelay; // countdown timer between animation updates
    public int animFrame; // keeps track of which animation frame is currently shown for the player
    public int coinsCollected; // a counter to keep a tally on how many coins the player has collected
    public int height;
    
    private PApplet engine = null;
    private Keyboard keyboard = null;
    private Program p = null;
    private Sound s = null;
    private final World w;
    private int MarioDeath = 0;
    private int MarioWin = 0;
    private boolean coinsAnimationActive;
    private float coinPositionX;
    private float coinPositionY;
    private int temp = 0;
    private boolean coinsAnimationTimerPassed = false;
    
    public Mario(PApplet _engine, World _w, Keyboard k, Program _p, Sound _s){
        engine = _engine;
        w = _w;
        s = _s;
        p = _p;
        keyboard = k;
        isOnGround = false;
        facingRight = true;
        position = new PVector();
        velocity = new PVector();
        MarioWin = 0;
        initImage();
        height = MarioStandbye.height;
        reset();
    }
    
    public void reset() {
        coinsCollected = 0;
        animDelay = 0;
        animFrame = 0;
        velocity.x = 0;
        velocity.y = 0;
    }
    
    public void inputCheck() {
    if (MarioDeath == 1){
        velocity.x = 0;
    }
    else if(MarioWin == 1){
        float speedHere = (float) (isOnGround ? RUN_SPEED : AIR_RUN_SPEED);
        float frictionHere = (float) (isOnGround ? SLOWDOWN_PERC : AIR_SLOWDOWN_PERC);
        velocity.x = (float)0.0;
        //velocity.y = (float) JUMP_POWER-2;
        if (temp == 0){
        position.x = position.x + 10;
        temp++;
        }
        if (position.y > 170){
            velocity.x = 6;
        }
        
    } 
    else{
        float speedHere = (float) (isOnGround ? RUN_SPEED : AIR_RUN_SPEED);
        float frictionHere = (float) (isOnGround ? SLOWDOWN_PERC : AIR_SLOWDOWN_PERC);


        if(keyboard.holdingLeft) {
          velocity.x -= speedHere;
        } else if(keyboard.holdingRight) {
          velocity.x += speedHere;
        } 
        velocity.x *= frictionHere; // causes player to constantly lose speed

        if(isOnGround) { // player can only jump if currently on the ground
          if(keyboard.holdingSpace || keyboard.holdingUp) { // either up arrow or space bar cause the player to jump
            s.LaunchJumpSound();
            velocity.y = (float) -JUMP_POWER; // adjust vertical speed
            isOnGround = false; // mark that the player has left the ground, i.e. cannot jump again for now
          }
        }
    }
  }
    
    public void initImage(){
        MarioStandbye = engine.loadImage("Ressource/img/spriteSource3.gif").get(23, 507, 14, 18);
        MarioJump = engine.loadImage("Ressource/img/spriteSource3.gif").get(140, 507, 14, 18);
        MarioDie = engine.loadImage("Ressource/img/spriteSource3.gif").get(44, 507, 14, 18);
        MarioRunRight1 = engine.loadImage("Ressource/img/spriteSource3.gif").get(67, 507, 14, 18);
        MarioRunRight2 = engine.loadImage("Ressource/img/spriteSource3.gif").get(85, 507, 14, 18);
        MarioRunRight3 = engine.loadImage("Ressource/img/spriteSource3.gif").get(100, 507, 14, 18);
        MarioRunRight4 = engine.loadImage("Ressource/img/spriteSource3.gif").get(118, 507, 14, 18);
        
        MarioRunLeft1 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(600, 507, 14, 18);
        MarioRunLeft2 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(583, 507, 14, 18);
        MarioRunLeft3 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(566, 507, 14, 18);
        MarioRunLeft4 = engine.loadImage("Ressource/img/spriteSource3left.gif").get(547, 507, 14, 18);
        
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

    public void draw() {
        if (MarioDeath == 0){
            int guyWidth = MarioStandbye.width;
            int guyHeight = MarioStandbye.height;

            if(velocity.x<-TRIVIAL_SPEED) {
              facingRight = false;
            } else if(velocity.x>TRIVIAL_SPEED) {
              facingRight = true;
            }

            engine.pushMatrix(); // lets us compound/accumulate translate/scale/rotate calls, then undo them all at once
            engine.translate(position.x,position.y);
            if(facingRight==false) {
              engine.scale(-1,1); // flip horizontally by scaling horizontally by -100%
            }
            engine.translate(-guyWidth/2,-guyHeight); // drawing images centered on character's feet

            int wallProbeDistance = (int)(guyWidth*0.3);
            int ceilingProbeDistance = (int)(guyHeight*0.95);
            PVector leftSideHigh,rightSideHigh,leftSideLow,rightSideLow,topSide;
            leftSideHigh = new PVector();
            rightSideHigh = new PVector();
            leftSideLow = new PVector();
            rightSideLow = new PVector();
            topSide = new PVector();

            // update wall probes
            leftSideHigh.x = leftSideLow.x = position.x - wallProbeDistance; // left edge of player
            rightSideHigh.x = rightSideLow.x = position.x + wallProbeDistance; // right edge of player
            leftSideLow.y = rightSideLow.y = (float) (position.y-0.2*guyHeight); // shin high
            leftSideHigh.y = rightSideHigh.y = (float) (position.y-0.8*guyHeight); // shoulder high

            topSide.x = position.x; // center of player
            topSide.y = position.y-ceilingProbeDistance; // top of guy

            if(isOnGround==false) { // falling or jumping
              engine.image(MarioJump, 0,0); // this running pose looks pretty good while in the air
            } else if(engine.abs(velocity.x)<TRIVIAL_SPEED) { // not moving fast, i.e. standing
              engine.image(MarioStandbye, 0,0);
            } 
            else if (w.worldSquareAt(topSide)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(leftSideHigh)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(leftSideLow)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(rightSideHigh)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(rightSideLow)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(position)==World.TILE_KILLBLOCK){
                engine.image(MarioDie, 0, 0);
            }
            else if (w.worldSquareAt(topSide)==World.TILE_WIN ||
                 w.worldSquareAt(leftSideHigh)==World.TILE_WIN ||
                 w.worldSquareAt(leftSideLow)==World.TILE_WIN ||
                 w.worldSquareAt(rightSideHigh)==World.TILE_WIN ||
                 w.worldSquareAt(rightSideLow)==World.TILE_WIN ||
                 w.worldSquareAt(position)==World.TILE_WIN){
                engine.image(MarioDie, 0, 0);
            }
            else { // running. Animate.
              if(animDelay--<0) {
                animDelay=RUN_ANIMATION_DELAY;
                if(animFrame==0) {
                  animFrame=1;
                } else {
                  animFrame=0;
                }
              }

              if(animFrame==0) {
                engine.image(MarioStandbye, 0,0);
              } else {
                engine.image(MarioRunRight2, 0,0);
              }
            }

            engine.popMatrix();
        }
        else
        {
            int guyWidth = MarioStandbye.width;
            int guyHeight = MarioStandbye.height;

            if(velocity.x<-TRIVIAL_SPEED) {
              facingRight = false;
            } else if(velocity.x>TRIVIAL_SPEED) {
              facingRight = true;
            }

            engine.pushMatrix(); // lets us compound/accumulate translate/scale/rotate calls, then undo them all at once
            engine.translate(position.x,position.y);
            if(facingRight==false) {
              engine.scale(-1,1); // flip horizontally by scaling horizontally by -100%
            }
            engine.translate(-guyWidth/2,-guyHeight); // drawing images centered on character's feet

            int wallProbeDistance = (int)(guyWidth*0.3);
            int ceilingProbeDistance = (int)(guyHeight*0.95);
            PVector leftSideHigh,rightSideHigh,leftSideLow,rightSideLow,topSide;
            leftSideHigh = new PVector();
            rightSideHigh = new PVector();
            leftSideLow = new PVector();
            rightSideLow = new PVector();
            topSide = new PVector();

            // update wall probes
            leftSideHigh.x = leftSideLow.x = position.x - wallProbeDistance; // left edge of player
            rightSideHigh.x = rightSideLow.x = position.x + wallProbeDistance; // right edge of player
            leftSideLow.y = rightSideLow.y = (float) (position.y-0.2*guyHeight); // shin high
            leftSideHigh.y = rightSideHigh.y = (float) (position.y-0.8*guyHeight); // shoulder high

            topSide.x = position.x; // center of player
            topSide.y = position.y-ceilingProbeDistance; // top of guy

            engine.image(MarioDie, 0, 0);

            engine.popMatrix();
        }
    }
    
    public void move() {
    position.add(velocity);
    
    checkForWallBumping();
    
    checkForCoinGetting();
    
    checkForFalling();
  }
    
    public void checkForWallBumping() {
    int guyWidth = MarioStandbye.width; // think of image size of player standing as the player's physical size
    int guyHeight = MarioStandbye.height;
    int wallProbeDistance = (int)(guyWidth-6);
    int ceilingProbeDistance = (int)(guyHeight-2);
    
    PVector leftSideHigh,rightSideHigh,leftSideLow,rightSideLow,topSide;
    leftSideHigh = new PVector();
    rightSideHigh = new PVector();
    leftSideLow = new PVector();
    rightSideLow = new PVector();
    topSide = new PVector();

    leftSideHigh.x = leftSideLow.x = position.x - wallProbeDistance; // left edge of player
    rightSideHigh.x = rightSideLow.x = position.x + wallProbeDistance; // right edge of player
    leftSideLow.y = rightSideLow.y = (float) (position.y-0.2*guyHeight); // shin high
    leftSideHigh.y = rightSideHigh.y = (float) (position.y-0.8*guyHeight); // shoulder high

    topSide.x = position.x; // center of player
    topSide.y = position.y-ceilingProbeDistance; // top of guy

    if (MarioDeath == 0){
        
        if( w.worldSquareAt(topSide)==World.TILE_KILLBLOCK ||
             w.worldSquareAt(leftSideHigh)==World.TILE_KILLBLOCK ||
             w.worldSquareAt(leftSideLow)==World.TILE_KILLBLOCK ||
             w.worldSquareAt(rightSideHigh)==World.TILE_KILLBLOCK ||
             w.worldSquareAt(rightSideLow)==World.TILE_KILLBLOCK ||
             w.worldSquareAt(position)==World.TILE_KILLBLOCK) {
            Program.gameover();
          return; // any other possible collisions would be irrelevant, exit function now
        }

        // if any edge of the player is inside a red killblock, reset the round
        if( w.worldSquareAt(topSide)==World.TILE_WIN ||
             w.worldSquareAt(leftSideHigh)==World.TILE_WIN ||
             w.worldSquareAt(leftSideLow)==World.TILE_WIN ||
             w.worldSquareAt(rightSideHigh)==World.TILE_WIN ||
             w.worldSquareAt(rightSideLow)==World.TILE_WIN ||
             w.worldSquareAt(position)==World.TILE_WIN) {
            Program.gameWin();
            MarioWin();
          return; // any other possible collisions would be irrelevant, exit function now
        }
    
        if(w.worldSquareAt(topSide)==World.TILE_SOLID 
                || w.worldSquareAt(topSide)==World.TILE_EMPTY_BLOCK 
                || w.worldSquareAt(topSide)==World.TILE_SOLID_BONUS 
                || w.worldSquareAt(topSide)==World.TILE_SOLID_BlockBlock 
                || w.worldSquareAt(topSide)==World.TILE_SOLID_BrickBlock
                || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_BOTTOM
                || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_TOP
                || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_BOTTOM
                || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_TOP
                || w.worldSquareAt(topSide)==World.TILE_SOLID_BONUS_EMPTY) {
          if(w.worldSquareAt(position)==World.TILE_SOLID) {
            position.sub(velocity);
            velocity.x=(float) 0.0;
            velocity.y=(float) 0.0;
          } else {
            position.y = w.bottomOfSquare(topSide)+ceilingProbeDistance;
            if(velocity.y < 0) {
              velocity.y = (float) 0.0;
            }
          }
        }

        if ( w.worldSquareAt(topSide)==World.TILE_SOLID_BONUS){
            coinsAnimationActive = true;
            coinPositionX = topSide.x;
            coinPositionY = topSide.y;
            s.LaunchCoinSound();
            coinsCollected++;
            Program.MarioPoint += 50;
            w.setSquareAtToThis(topSide, World.TILE_SOLID_BONUS_EMPTY);
        }
        
        if (!coinsAnimationTimerPassed && coinsAnimationActive)
        {
          engine.image(w.coins, coinPositionX-8, coinPositionY-30);
          TimerDieDisparition((float)0.5);
        }
        else if (coinsAnimationTimerPassed){
          coinsAnimationActive = false;
          coinsAnimationTimerPassed = false;
        }
    
        if(  w.worldSquareAt(leftSideLow)==World.TILE_SOLID || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BONUS 
                || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BlockBlock 
                || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BrickBlock
                || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_BOTTOM
                || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_TOP
                || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
                || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_TOP
                || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BONUS_EMPTY) {
          position.x = w.rightOfSquare(leftSideLow)+wallProbeDistance;
          if(velocity.x < 0) {
            velocity.x = (float) 0.0;
          }
        }
   
        if( w.worldSquareAt(leftSideHigh)==World.TILE_SOLID || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BONUS 
                || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BlockBlock 
                || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BrickBlock
                || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
                || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_TOP
                || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
                || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_TOP
                || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BONUS_EMPTY) {
          position.x = w.rightOfSquare(leftSideHigh)+wallProbeDistance;
          if(velocity.x < 0) {
            velocity.x = (float) 0.0;
          }
        }

        if( w.worldSquareAt(rightSideLow)==World.TILE_SOLID || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BONUS 
                || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BlockBlock 
                || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BrickBlock
                || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_BOTTOM
                || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_TOP
                || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
                || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_TOP
                || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BONUS_EMPTY) {
          position.x = w.leftOfSquare(rightSideLow)-wallProbeDistance;
          if(velocity.x > 0) {
            velocity.x = (float) 0.0;
          }
        }
   
        if(w.worldSquareAt(rightSideHigh)==World.TILE_SOLID || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BONUS 
                || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BlockBlock 
                || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BrickBlock
                || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
                || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_LEFT_TOP
                || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
                || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_RIGHT_TOP
                || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BONUS_EMPTY) {
          position.x = w.leftOfSquare(rightSideHigh)-wallProbeDistance;
          if(velocity.x > 0) {
            velocity.x = (float) 0.0;
          }
        }
    }
    else
    {
    }
  }

    public void TimerDieDisparition(float time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                coinsAnimationTimerPassed = true;
            }
          }, (long)(time*1000));
    }
  
    public void checkForCoinGetting() {
    PVector centerOfPlayer;
    centerOfPlayer = new PVector(position.x,position.y-MarioStandbye.height/2);

    if(w.worldSquareAt(centerOfPlayer)==World.TILE_COIN) {
      w.setSquareAtToThis(centerOfPlayer, World.TILE_EMPTY);
      coinsCollected++;
      s.LaunchCoinSound();
    }
  }

    public void checkForFalling() {
    // If we're standing on an empty or coin tile, we're not standing on anything. Fall!
    if(w.worldSquareAt(position)==World.TILE_EMPTY 
            || w.worldSquareAt(position)==World.TILE_COIN
            || w.worldSquareAt(position)==World.TILE_WIN) {
       isOnGround=false;
    }
    
    if (MarioDeath == 0){
    if(isOnGround==false) { // not on ground?    
      if(w.worldSquareAt(position)==World.TILE_SOLID || w.worldSquareAt(position)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(position)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(position)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(position)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(position)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(position)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(position)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(position)==World.TILE_SOLID_BONUS_EMPTY) { // landed on solid square?
        isOnGround = true;
        position.y = w.topOfSquare(position);
        velocity.y = (float) 0.0;
      } else { // fall
        velocity.y += World.GRAVITY_POWER;
      }
    }
    }else
    {
        velocity.y += World.GRAVITY_POWER;
    }
  }

    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public void MarioDeath() {
        velocity.y -= JUMP_POWER;
        MarioDeath = 1;
    }
    
    public void MarioWin(){
        MarioWin = 1;
    }

    public void MarioLive() {
        MarioDeath = 0;
    }
}
