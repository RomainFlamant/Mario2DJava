
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
public class Program extends PApplet{
    
    static final int WINDOW_WIDTH = 1000;
    static final int WINDOW_HEIGHT = 600;
    static final float GRAVITY = -200; // acceleration due to gravity
    
    StartScreen st;
    World w;
    static public Mario m;
    Sound s;
    World wo;
    PlatformControler p;
    public static final String _level1 = "First"; 
    
    public void setup()
    {
        size(WINDOW_WIDTH,WINDOW_HEIGHT,P2D);
        //st = new StartScreen(this)
        m = new Mario(this,0,520);
        
        p = new PlatformControler(this);
        p.setup();
        w = new World(this);
        s = new Sound(this);
    }
    
    public void draw(){
        background(171,205,239);
        
        p.draw();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] passedArgs) {
        
        PApplet.main(new String[]{"Program"});
    }
    
}
