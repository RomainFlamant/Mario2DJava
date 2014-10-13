
import processing.core.PApplet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Romain
 */
class Keyboard {
  // used to track keyboard input
  public Boolean holdingUp;
  public Boolean holdingRight;
  public Boolean holdingLeft;
  public Boolean holdingSpace;
  private PApplet engine;
  
  Keyboard(PApplet _engine) {
    holdingUp = false;
    holdingRight = false;
    holdingLeft = false;
    holdingSpace = false;
    engine = _engine;
  }
  
  /* The way that Processing, and many programming languages/environments, deals with keys is
   * treating them like events (something can happen the moment it goes down, or when it goes up).
   * Because we want to treat them like buttons - checking "is it held down right now?" - we need to
   * use those pressed and released events to update some true/false values that we can check elsewhere.
   */

  void pressKey(int key,int keyCode) {
    //if(key == 'r') { // never will be held down, so no Boolean needed to track it
    //  if(p.gameWon()) { // if the game has been won...
    //    p.resetGame(); // then R key resets it
    //  }
    //}
   
    if (keyCode == engine.UP) {
      holdingUp = true;
    }
    if (keyCode == engine.LEFT) {
      holdingLeft = true;
    }
    if (keyCode == engine.RIGHT) {
      holdingRight = true;
    }
    if (key == ' ') {
      holdingSpace = true;
    }
  }
  void releaseKey() {
    
    holdingUp = false;
    holdingLeft = false;
    holdingRight = false;
    holdingSpace = false;
    
  }
}
