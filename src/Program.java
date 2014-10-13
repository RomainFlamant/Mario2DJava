
import processing.core.PApplet;
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
public class Program extends PApplet{
    
    static final int WINDOW_WIDTH = 1000;
    static final int WINDOW_HEIGHT = 600;
    static final float GRAVITY = -200; // acceleration due to gravity

    public void gamevover() {
        size(WINDOW_WIDTH,WINDOW_HEIGHT,P2D);
        //st = new StartScreen(this)
        w = new World(this);
        w.initBasicLevel();
        k = new Keyboard(this);
        m = new Mario(this,w,k,this);
        
        //p = new PlatformControler(this);
        //p.setup();
        w.reload(m); 
    }
    
    World w;
    static public Mario m;
    Sound s;
    Keyboard k;
    public static final String _level1 = "First"; 
    
    public void setup()
    {
        size(WINDOW_WIDTH,WINDOW_HEIGHT,P2D);
        //st = new StartScreen(this)
        w = new World(this);
        w.initBasicLevel();
        k = new Keyboard(this);
        m = new Mario(this,w,k,this);
        
        //p = new PlatformControler(this);
        //p.setup();
        w.reload(m); 
        
    }
    
    public void draw(){
        background(171,205,239);
        w.initBasicLevel();
        m.draw();
        m.inputCheck();
        m.move();
        
        //m.reset(); // reset the coins collected number, etc.
  
        // reset world map

  
        
    }
    
    @Override
    public void keyPressed(){
        super.keyPressed();
        k.pressKey(key, keyCode);
    }
    

    @Override
    public void keyReleased() {
        super.keyReleased();
        k.releaseKey();
    }
  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] passedArgs) {
        
        PApplet.main(new String[]{"Program"});
    }
    
}
