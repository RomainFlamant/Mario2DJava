
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
    static final int WINDOW_HEIGHT = 224;
    static final float GRAVITY = -200; // acceleration due to gravity
    public float cameraOffsetX;

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
        cameraOffsetX = (float) 0.0;
        //p = new PlatformControler(this);
        //p.setup();
        w.reload(m); 
        
    }
    
    public void draw(){
        background(171,205,239);
        pushMatrix(); // lets us easily undo the upcoming translate call
        translate(-cameraOffsetX, (float) 0.0); // affects all upcoming graphics calls, until popMatrix

        updateCameraPosition();

        w.initBasicLevel();

        m.inputCheck();
        m.move();
        m.draw();

        popMatrix();
    }
    
    @Override
    public void keyPressed(){
        super.keyPressed();
        k.pressKey(key, keyCode);
    }
    
    void updateCameraPosition() {
        int rightEdge = World.GRID_UNITS_WIDE*World.GRID_UNIT_SIZE-width;
        // the left side of the camera view should never go right of the above number
        // think of it as "total width of the game world" (World.GRID_UNITS_WIDE*World.GRID_UNIT_SIZE)
        // minus "width of the screen/window" (width)

        cameraOffsetX = m.position.x-width/2;
        if(cameraOffsetX < 0) {
          cameraOffsetX = 0;
        }

        if(cameraOffsetX > rightEdge) {
          cameraOffsetX = rightEdge;
        }
    }
    

    @Override
    public void keyReleased() {
        super.keyReleased();
        k.releaseKey(this.keyCode);
    }
  
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] passedArgs) {
        
        PApplet.main(new String[]{"Program"});
    }
    
}
