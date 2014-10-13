
import ddf.minim.AudioPlayer;
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Romain
 */
public class PlatformControler {
    
private PApplet engine;

    public PlatformControler(PApplet _engine) {
        engine = _engine;
    }



// these next 2 lines are used for sound
Minim minim;

// for storing and referencing animation frames for the player character
PImage guy_stand, guy_run1, guy_run2;

// music and sound effects
AudioPlayer music; // AudioPlayer uses less memory. Better for music.
public AudioSample sndJump, sndCoin; // AudioSample plays more respnosively. Better for sound effects.

// we use this to track how far the camera has scrolled left or right
float cameraOffsetX;

Keyboard theKeyboard = new Keyboard(engine);
World2 theWorld = new World2(engine);
Player thePlayer = new Player(engine, theKeyboard,this , theWorld);


PFont font;

// we use these for keeping track of how long player has played
int gameStartTimeSec,gameCurrentTimeSec;

// by adding this to the player's y velocity every frame, we get gravity
final double GRAVITY_POWER = 0.5; // try making it higher or lower!

void setup() { // called automatically when the program starts
  engine.size(600,480); // how large the window/screen is for the game
  
  font = engine.loadFont("Ressource/test/SansSerif-20.vlw");

  guy_stand = engine.loadImage("Ressource/test/guy.png");
  guy_run1 = engine.loadImage("Ressource/test/run1.png");
  guy_run2 = engine.loadImage("Ressource/test/run2.png");
  
  cameraOffsetX = (float) 0.0;
  
  minim = new Minim(this);
  music = minim.loadFile("Ressource/test/PinballSpring.mp3", 1024);
  music.loop();
  int buffersize = 256;
  sndJump = minim.loadSample("Ressource/test/jump.wav", buffersize);
  sndCoin = minim.loadSample("Ressource/test/coin.wav", buffersize);
  
  engine.frameRate(24); // this means draw() will be called 24 times per second
  
  resetGame(); // sets up player, game level, and timer
}

void resetGame() {
  // This function copies start_Grid into worldGrid, putting coins back
  // multiple levels could be supported by copying in a different start grid
  
  thePlayer.reset(); // reset the coins collected number, etc.
  
  theWorld.reload(thePlayer); // reset world map

  // reset timer in corner
  gameCurrentTimeSec = gameStartTimeSec = engine.millis()/1000; // dividing by 1000 to turn milliseconds into seconds
}

Boolean gameWon() { // checks whether all coins in the level have been collected
  return (thePlayer.coinsCollected == theWorld.coinsInStage);
}

void outlinedText(String sayThis, float atX, float atY) {
  engine.textFont(font); // use the font we loaded
  engine.fill(0); // white for the upcoming text, drawn in each direction to make outline
  engine.text(sayThis, atX-1,atY);
  engine.text(sayThis, atX+1,atY);
  engine.text(sayThis, atX,atY-1);
  engine.text(sayThis, atX,atY+1);
  engine.fill(255); // white for this next text, in the middle
  engine.text(sayThis, atX,atY);
}

void updateCameraPosition() {
  int rightEdge = World2.GRID_UNITS_WIDE*World2.GRID_UNIT_SIZE-engine.width;
  // the left side of the camera view should never go right of the above number
  // think of it as "total width of the game world" (World.GRID_UNITS_WIDE*World.GRID_UNIT_SIZE)
  // minus "width of the screen/window" (width)
  
  cameraOffsetX = thePlayer.position.x-engine.width/2;
  if(cameraOffsetX < 0) {
    cameraOffsetX = 0;
  }
  
  if(cameraOffsetX > rightEdge) {
    cameraOffsetX = rightEdge;
  }
}

void draw() { // called automatically, 24 times per second because of setup()'s call to frameRate(24)
  engine.pushMatrix(); // lets us easily undo the upcoming translate call
  engine.translate(-cameraOffsetX, (float) 0.0); // affects all upcoming graphics calls, until popMatrix

  updateCameraPosition();

  theWorld.render();
    
  thePlayer.inputCheck();
  thePlayer.move();
  thePlayer.draw();
  
  engine.popMatrix(); // undoes the translate function from earlier in draw()
  
  if(engine.focused == false) { // does the window currently not have keyboard focus?
    engine.textAlign(engine.CENTER);
    outlinedText("Click this area to play.\n\nUse arrows to move.\nSpacebar to jump.",engine.width/2, engine.height-90);
  } else {
    engine.textAlign(engine.LEFT); 
    outlinedText("Coins:"+thePlayer.coinsCollected +"/"+theWorld.coinsInStage,8, engine.height-10);
    
    engine.textAlign(engine.RIGHT);
    if(gameWon() == false) { // stop updating timer after player finishes
      gameCurrentTimeSec = engine.millis()/1000; // dividing by 1000 to turn milliseconds into seconds
    }
    int minutes = (gameCurrentTimeSec-gameStartTimeSec)/60;
    int seconds = (gameCurrentTimeSec-gameStartTimeSec)%60;
    if(seconds < 10) { // pad the "0" into the tens position
      outlinedText(minutes +":0"+seconds,engine.width-8, engine.height-10);
    } else {
      outlinedText(minutes +":"+seconds,engine.width-8, engine.height-10);
    }
    
    engine.textAlign(engine.CENTER); // center align the text
    outlinedText("Music by Kevin MacLeod, Code by Chris DeLeon",engine.width/2, 25);
    if(gameWon()) {
      outlinedText("All Coins Collected!\nPress R to Reset.",engine.width/2, engine.height/2-12);
    }
  }
}

void keyPressed() {
  theKeyboard.pressKey(engine.key,engine.keyCode);
}

void keyReleased() {
  //theKeyboard.releaseKey(engine.key,engine.keyCode);
}

void stop() { // automatically called when program exits. here we'll stop and unload sounds.
  music.close();
  sndJump.close();
  sndCoin.close();
 
  minim.stop();

  engine.stop(); // tells program to continue doing its normal ending activity
}
    
}


