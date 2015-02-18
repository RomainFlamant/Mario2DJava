import java.util.Timer;
import java.util.TimerTask;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Goomba {
    
    public static PImage goombaDie = null;
    public static PImage goombaMove1 = null;
    public static PImage goombaMove2 = null;
    public static AnimationSprite AnimGoombaMove = null;
    public String RunDirection = "left";
    public PVector position,velocity;
    public Boolean isOnGround;
    public int animDelay;
    public int animFrame;
    public static final double JUMP_POWER = 8.0;
    public static final double RUN_SPEED = 1.5;
    public static final double AIR_RUN_SPEED = 1.0;
    public static final double SLOWDOWN_PERC = 0.6;
    public static final double AIR_SLOWDOWN_PERC = 0.85;
    public static final int RUN_ANIMATION_DELAY = 3;
    public static final double TRIVIAL_SPEED = 1.0;
    private PApplet engine = null;
    private Program p = null;
    private Sound s = null;
    private Mario m = null;
    private int GoombaDie = 0;
    private int CallTimer = 0;
    private boolean GoombaVisibility = true;
    private final World w;
    public float realyXPosition = (float)0.0;
    public float realyYPosition = (float)0.0;
    public float posx = (float)0.0;
    public float posy = (float)0.0;
    public Boolean facingRight = false;
    private boolean active = false;
    
    public Goomba(PApplet _engine, World _w, Program p, Sound _s, Mario _m)
    {
        engine = _engine;
        w = _w;
        s = _s;
        m = _m;
        this.p = p;
        isOnGround = false;
        position = new PVector();
        position.x = 0;
        position.y = 0;
        velocity = new PVector();
        initImage();
    }
    
    public void setVisible(){
        GoombaVisibility = true;
    }
    
    public void TimerDieDisparition(int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                GoombaVisibility = false;
            }
          }, time*1000);
    }
  
    public void initImage()
    {
        goombaDie = engine.loadImage("Ressource/img/spriteSource3.gif").get(228, 893, 18, 18);
        goombaMove1 = engine.loadImage("Ressource/img/spriteSource3.gif").get(186 ,893, 18, 18);
        goombaMove2 = engine.loadImage("Ressource/img/spriteSource3.gif").get(208, 893, 18, 18);
    }
    
    void inputCheck() {
        if (  (Program.cameraOffsetX + engine.width) > position.x - 10){
            active = true;
        }
        if (active == true && "left".equals(RunDirection)){
            velocity.x -= RUN_SPEED;
            velocity.x *= SLOWDOWN_PERC;
        }
        else if (active == true)
        {
            velocity.x += RUN_SPEED;
            velocity.x *= SLOWDOWN_PERC;
        }
    }
    
    public void reset() {
        animDelay = 0;
        animFrame = 0;
        velocity.x = 0;
        velocity.y = 0;
    }

    public void draw() {
        int guyWidth = goombaMove1.width;
        int guyHeight = goombaMove1.height;
         
        engine.pushMatrix(); 
        if (GoombaDie == 0 && GoombaVisibility == true){
            if(velocity.x<-TRIVIAL_SPEED) {
                facingRight = false;
            } else if(velocity.x>TRIVIAL_SPEED) {
                facingRight = true;
            }

            // lets us compound/accumulate translate/scale/rotate calls, then undo them all at once
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

            
            if (w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_TOP   
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_TOP
            ||w.worldSquareAt(topSide)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BlockBlock){
                System.out.println(RunDirection);
            }
            
            if (w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_TOP){
                System.out.println(RunDirection);
            }
            
            if(engine.abs(velocity.x)<TRIVIAL_SPEED) { // not moving fast, i.e. standing
              engine.image(goombaMove1, 0,0);
            } 
            else if (w.worldSquareAt(topSide)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(leftSideHigh)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(leftSideLow)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(rightSideHigh)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(rightSideLow)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(position)==World.TILE_KILLBLOCK){
                engine.image(goombaDie, 0, 0);
            }
            else { // running. Animate.
                if(animDelay--<0) {
                    animDelay=RUN_ANIMATION_DELAY;
                    if(animFrame==0) {
                        animFrame=1;
                    } 
                    else {
                        animFrame=0;
                    }
                }

                if(animFrame==0) {
                    engine.image(goombaMove1, 0,0);
                } 
                else {
                    engine.image(goombaMove2, 0,0);
                }
            }
        }// Goomba Die
        else if (GoombaDie == 1 && GoombaVisibility == true){
            velocity.x = 0;
            velocity.y = 0;

            // lets us compound/accumulate translate/scale/rotate calls, then undo them all at once
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

            if(engine.abs(velocity.x)<TRIVIAL_SPEED) { // not moving fast, i.e. standing
              engine.image(goombaDie, 0,0);
            } 
            else if (w.worldSquareAt(topSide)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(leftSideHigh)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(leftSideLow)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(rightSideHigh)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(rightSideLow)==World.TILE_KILLBLOCK ||
                 w.worldSquareAt(position)==World.TILE_KILLBLOCK){
                engine.image(goombaDie, 0, 0);
            }
            else { // running. Animate.
                if(animDelay--<0) {
                    animDelay=RUN_ANIMATION_DELAY;
                    if(animFrame==0) {
                        animFrame=1;
                    } 
                    else {
                        animFrame=0;
                    }
                }

                if(animFrame==0) {
                    engine.image(goombaDie, 0,0);
                } 
                else {
                    engine.image(goombaDie, 0,0);
                }
            }
            if (CallTimer == 0){
                TimerDieDisparition(1);
                CallTimer = 1;
            }
        }
        else
        {
            //position.x = position.y = -999999999;
        }
        engine.popMatrix();
    }
    
    void move() {
        position.add(velocity);
        checkForWallBumping();
        checkForFalling();
  }
    
  void checkForWallBumping() {
    float speedHere = (float) (isOnGround ? RUN_SPEED : AIR_RUN_SPEED);
    float frictionHere = (float) (isOnGround ? SLOWDOWN_PERC : AIR_SLOWDOWN_PERC);
    int guyWidth = goombaMove1.width; // think of image size of player standing as the player's physical size
    int guyHeight = goombaMove1.height;
    int wallProbeDistance = (int)(guyWidth-8);
    int ceilingProbeDistance = (int)(guyHeight-2);
    
    // used as probes to detect running into walls, ceiling
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
    if(m.getPositionX() > (position.x - 10) 
            && m.getPositionX() < (position.x + 10) 
            && m.getPositionY() < position.y - 2
            && m.getPositionY() > position.y - 20
            && GoombaDie == 0
            && m.velocity.y > (-m.JUMP_POWER + 1)){
        GoombaDie = 1;
        Sound s = new Sound(engine);
        s.LaunchGoombaDieSound();
        Program.MarioPoint += 300;
    }
    
    if(m.getPositionX() > (position.x - 5) 
            && m.getPositionX() < (position.x + 5) 
            && position.y == m.getPositionY() 
            && GoombaDie == 0){
        Program.gameover();
    }

    if (w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(topSide)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(topSide)==World.TILE_EMPTY_BLOCK
            || w.worldSquareAt(leftSideHigh)==World.TILE_EMPTY_BLOCK
            || w.worldSquareAt(leftSideLow)==World.TILE_EMPTY_BLOCK){
                RunDirection = "right";
            }
            
            if (w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_TOP
            || w.worldSquareAt(topSide)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BlockBlock
            || w.worldSquareAt(topSide)==World.TILE_EMPTY_BLOCK
            || w.worldSquareAt(rightSideHigh)==World.TILE_EMPTY_BLOCK
            || w.worldSquareAt(rightSideLow)==World.TILE_EMPTY_BLOCK){
                RunDirection = "left";
            }
    
    // if any edge of the player is inside a red killblock, reset the round
    if( w.worldSquareAt(topSide)==World.TILE_KILLBLOCK ||
         w.worldSquareAt(leftSideHigh)==World.TILE_KILLBLOCK ||
         w.worldSquareAt(leftSideLow)==World.TILE_KILLBLOCK ||
         w.worldSquareAt(rightSideHigh)==World.TILE_KILLBLOCK ||
         w.worldSquareAt(rightSideLow)==World.TILE_KILLBLOCK ||
         w.worldSquareAt(position)==World.TILE_KILLBLOCK) {
        position.y = 999999;
        position.x = 999999;
      return; 
    }
    
    // the following conditionals just check for collisions with each bump probe
    // depending upon which probe has collided, we push the player back the opposite direction
    
    if(w.worldSquareAt(topSide)==World.TILE_SOLID || w.worldSquareAt(topSide)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(topSide)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(topSide)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(topSide)==World.TILE_PIPE_RIGHT_TOP) {
    }
    
    if ( w.worldSquareAt(topSide)==World.TILE_SOLID_BrickBlock){
        s.LaunchBrickBlockChocSound();
    }
    
    if(  w.worldSquareAt(leftSideLow)==World.TILE_SOLID || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(leftSideLow)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(leftSideLow)==World.TILE_PIPE_RIGHT_TOP) {
      position.x = w.rightOfSquare(leftSideLow)+wallProbeDistance;
    }
   
    if( w.worldSquareAt(leftSideHigh)==World.TILE_SOLID || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(leftSideHigh)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(leftSideHigh)==World.TILE_PIPE_RIGHT_TOP) {
      position.x = w.rightOfSquare(leftSideHigh)+wallProbeDistance;
    }
   
    if( w.worldSquareAt(rightSideLow)==World.TILE_SOLID || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(rightSideLow)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(rightSideLow)==World.TILE_PIPE_RIGHT_TOP) {
      position.x = w.leftOfSquare(rightSideLow)-wallProbeDistance;
      
    }
   
    if(w.worldSquareAt(rightSideHigh)==World.TILE_SOLID || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(rightSideHigh)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(rightSideHigh)==World.TILE_PIPE_RIGHT_TOP) {
      //position.x = w.leftOfSquare(rightSideHigh)-wallProbeDistance;
      
    }
  }

  void checkForFalling() {
    // If we're standing on an empty or coin tile, we're not standing on anything. Fall!
    if(w.worldSquareAt(position)==World.TILE_EMPTY ||
       w.worldSquareAt(position)==World.TILE_COIN) {
       isOnGround=false;
    }
    
    if(isOnGround==false) { // not on ground?    
      if(w.worldSquareAt(position)==World.TILE_SOLID || w.worldSquareAt(position)==World.TILE_SOLID_BONUS 
            || w.worldSquareAt(position)==World.TILE_SOLID_BlockBlock 
            || w.worldSquareAt(position)==World.TILE_SOLID_BrickBlock
            || w.worldSquareAt(position)==World.TILE_PIPE_LEFT_BOTTOM
            || w.worldSquareAt(position)==World.TILE_PIPE_LEFT_TOP
            || w.worldSquareAt(position)==World.TILE_PIPE_RIGHT_BOTTOM
            || w.worldSquareAt(position)==World.TILE_PIPE_RIGHT_TOP) { // landed on solid square?
        isOnGround = true;
        position.y = w.topOfSquare(position);
        velocity.y = (float) 0.0;
      } else { // fall
        velocity.y += World.GRAVITY_POWER;
      }
    }
  }
    

    float getPositionX() {
        return position.x;
    }

    float getPositionY() {
        return position.y;
    }
    
}
