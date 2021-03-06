
import processing.core.PApplet;
import processing.core.PVector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Romain
 */

class Player {
  PVector position,velocity; // PVector contains two floats, x and y

  Boolean isOnGround; // used to keep track of whether the player is on the ground. useful for control and animation.
  Boolean facingRight; // used to keep track of which direction the player last moved in. used to flip player image.
  int animDelay; // countdown timer between animation updates
  int animFrame; // keeps track of which animation frame is currently shown for the player
  int coinsCollected; // a counter to keep a tally on how many coins the player has collected
  
  static final double JUMP_POWER = 11.0; // how hard the player jolts upward on jump
  static final double RUN_SPEED = 5.0; // force of player movement on ground, in pixels/cycle
  static final double AIR_RUN_SPEED = 2.0; // like run speed, but used for control while in the air
  static final double SLOWDOWN_PERC = 0.6; // friction from the ground. multiplied by the x speed each frame.
  static final double AIR_SLOWDOWN_PERC = 0.85; // resistance in the air, otherwise air control enables crazy speeds
  static final int RUN_ANIMATION_DELAY = 3; // how many game cycles pass between animation updates?
  static final double TRIVIAL_SPEED = 1.0; // if under this speed, the player is drawn as standing still
  
  private PApplet engine;
  private Keyboard theKeyboard;
  private PlatformControler p;
  private World2 w;
  
  Player(PApplet _engine, Keyboard _theKeyboard,PlatformControler _p, World2 w) { // constructor, gets called automatically when the Player instance is created
    isOnGround = false;
    facingRight = true;
    position = new PVector();
    velocity = new PVector();
    reset();
    engine = _engine;
    theKeyboard = _theKeyboard;
    p = _p;
    this.w = w;
  }
  
  void reset() {
    coinsCollected = 0;
    animDelay = 0;
    animFrame = 0;
    velocity.x = 0;
    velocity.y = 0;
  }
  
  void inputCheck() {
    // keyboard flags are set by keyPressed/keyReleased in the main .pde
    
    float speedHere = (float) (isOnGround ? RUN_SPEED : AIR_RUN_SPEED);
    float frictionHere = (float) (isOnGround ? SLOWDOWN_PERC : AIR_SLOWDOWN_PERC);
    
    
    
    if(theKeyboard.holdingLeft) {
      velocity.x -= speedHere;
    } else if(theKeyboard.holdingRight) {
      velocity.x += speedHere;
    } 
    velocity.x *= frictionHere; // causes player to constantly lose speed
    
    if(isOnGround) { // player can only jump if currently on the ground
      if(theKeyboard.holdingSpace || theKeyboard.holdingUp) { // either up arrow or space bar cause the player to jump
        p.sndJump.trigger(); // play sound
        velocity.y = (float) -JUMP_POWER; // adjust vertical speed
        isOnGround = false; // mark that the player has left the ground, i.e. cannot jump again for now
      }
    }
  }
  
  void checkForWallBumping() {
    int guyWidth = p.guy_stand.width; // think of image size of player standing as the player's physical size
    int guyHeight = p.guy_stand.height;
    int wallProbeDistance = (int)(guyWidth*0.3);
    int ceilingProbeDistance = (int)(guyHeight*0.95);
    
    /* Because of how we draw the player, "position" is the center of the feet/bottom
     * To detect and handle wall/ceiling collisions, we create 5 additional positions:
     * leftSideHigh - left of center, at shoulder/head level
     * leftSideLow - left of center, at shin level
     * rightSideHigh - right of center, at shoulder/head level
     * rightSideLow - right of center, at shin level
     * topSide - horizontal center, at tip of head
     * These 6 points - 5 plus the original at the bottom/center - are all that we need
     * to check in order to make sure the player can't move through blocks in the world.
     * This works because the block sizes (World.GRID_UNIT_SIZE) aren't small enough to
     * fit between the cracks of those collision points checked.
     */
    
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

    // if any edge of the player is inside a red killblock, reset the round
    if( w.worldSquareAt(topSide)==World2.TILE_KILLBLOCK ||
         w.worldSquareAt(leftSideHigh)==World2.TILE_KILLBLOCK ||
         w.worldSquareAt(leftSideLow)==World2.TILE_KILLBLOCK ||
         w.worldSquareAt(rightSideHigh)==World2.TILE_KILLBLOCK ||
         w.worldSquareAt(rightSideLow)==World2.TILE_KILLBLOCK ||
         w.worldSquareAt(position)==World2.TILE_KILLBLOCK) {
      p.resetGame();
      return; // any other possible collisions would be irrelevant, exit function now
    }
    
    // the following conditionals just check for collisions with each bump probe
    // depending upon which probe has collided, we push the player back the opposite direction
    
    if( w.worldSquareAt(topSide)==World2.TILE_SOLID) {
      if(w.worldSquareAt(position)==World2.TILE_SOLID) {
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
    
    if( w.worldSquareAt(leftSideLow)==World2.TILE_SOLID) {
      position.x = w.rightOfSquare(leftSideLow)+wallProbeDistance;
      if(velocity.x < 0) {
        velocity.x = (float) 0.0;
      }
    }
   
    if( w.worldSquareAt(leftSideHigh)==World2.TILE_SOLID) {
      position.x = w.rightOfSquare(leftSideHigh)+wallProbeDistance;
      if(velocity.x < 0) {
        velocity.x = (float) 0.0;
      }
    }
   
    if( w.worldSquareAt(rightSideLow)==World2.TILE_SOLID) {
      position.x = w.leftOfSquare(rightSideLow)-wallProbeDistance;
      if(velocity.x > 0) {
        velocity.x = (float) 0.0;
      }
    }
   
    if( w.worldSquareAt(rightSideHigh)==World2.TILE_SOLID) {
      position.x = w.leftOfSquare(rightSideHigh)-wallProbeDistance;
      if(velocity.x > 0) {
        velocity.x = (float) 0.0;
      }
    }
  }

  void checkForCoinGetting() {
    PVector centerOfPlayer;
    // we use this to check for coin overlap in center of player
    // (remember that "position" is keeping track of bottom center of feet)
    centerOfPlayer = new PVector(position.x,position.y-p.guy_stand.height/2);

    if(w.worldSquareAt(centerOfPlayer)==World2.TILE_COIN) {
      w.setSquareAtToThis(centerOfPlayer, World2.TILE_EMPTY);
      p.sndCoin.trigger();
      coinsCollected++;
    }
  }

  void checkForFalling() {
    // If we're standing on an empty or coin tile, we're not standing on anything. Fall!
    if(w.worldSquareAt(position)==World2.TILE_EMPTY ||
       w.worldSquareAt(position)==World2.TILE_COIN) {
       isOnGround=false;
    }
    
    if(isOnGround==false) { // not on ground?    
      if(w.worldSquareAt(position)==World2.TILE_SOLID) { // landed on solid square?
        isOnGround = true;
        position.y = w.topOfSquare(position);
        velocity.y = (float) 0.0;
      } else { // fall
        velocity.y += p.GRAVITY_POWER;
      }
    }
  }

  void move() {
    position.add(velocity);
    
    checkForWallBumping();
    
    checkForCoinGetting();
    
    checkForFalling();
  }
  
  void draw() {
    int guyWidth = p.guy_stand.width;
    int guyHeight = p.guy_stand.height;
    
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

    if(isOnGround==false) { // falling or jumping
      engine.image(p.guy_run1, 0,0); // this running pose looks pretty good while in the air
    } else if(engine.abs(velocity.x)<TRIVIAL_SPEED) { // not moving fast, i.e. standing
      engine.image(p.guy_stand, 0,0);
    } else { // running. Animate.
      if(animDelay--<0) {
        animDelay=RUN_ANIMATION_DELAY;
        if(animFrame==0) {
          animFrame=1;
        } else {
          animFrame=0;
        }
      }
      
      if(animFrame==0) {
        engine.image(p.guy_run1, 0,0);
      } else {
        engine.image(p.guy_run2, 0,0);
      }
    }
    
    engine.popMatrix(); // undoes all translate/scale/rotate calls since the pushMatrix earlier in this function
  }
}

