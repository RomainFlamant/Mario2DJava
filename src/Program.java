
import java.util.Timer;
import java.util.TimerTask;
import processing.core.PApplet;
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
public class Program extends PApplet{
    
    static final int WINDOW_WIDTH = 800;
    static final int WINDOW_HEIGHT = 224;
    static final float GRAVITY = -200; // acceleration due to gravity
    static private int MarioDie = 0;
    static private int MarioWin = 0;
    static public int MarioPoint = 0;
    static private final int CONST_MarioTimer = 180;
    static private int MarioTimer = CONST_MarioTimer;
    static private String RefreshMatrix = "off";
    static public float cameraOffsetX;
    static public World w;
    static public Mario m;
    public Timer timer;
    public Sound s;
    private Keyboard k;
    public static String level = "1";
    public long timeToWait = 9000;
    public long lastTime;
    
    static public void gameover() {
        if (MarioDie == 0){
            w.gameover();
            m.MarioDeath();     
            MarioDie = 1;
            MarioPoint = 0;
            initTimer();
            TimerRefresh(4);
        }
    } 
    
    static public void gameWin() {
        if (MarioWin == 0){
            w.gameWin();
            m.MarioLive();     
            MarioWin = 1;
            TimerRefresh(6);
            initTimer();
            System.out.println("game win switch");
            switch(level) { // check which tile type it is
                case "1":
                  level = "2";
                  break;
                case "2":
                  level = "3";
                  break;
                case "3":
                  level = "1";
                  break;    
                default:
                  break;
            }
            System.out.println(level);
        }
    } 
    
    static public void initTimer(){
        MarioTimer = CONST_MarioTimer;
    }
    
    public void setup()
    {
        if (timer != null){
            timer.cancel();
        }
        frameRate(60);
        s = new Sound(this);
        size(WINDOW_WIDTH,WINDOW_HEIGHT,P2D);
        k = new Keyboard(this);
        w = new World((PApplet)this, s, k, (Program)this);
        switch(level) { // check which tile type it is
          case "1":
            w.initLevel1();
            break;
          case "2":
            w.initLevel2();
            break;
          case "3":
            w.initLevel3();
            break;    
          default:
            break;
        }
        w.setup();
        m = w.getMario();
        cameraOffsetX = (float) 0.0;
        w.reload();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (MarioTimer != 0 && MarioDie == 0)
                    MarioTimer--;
            }
          }, 1000, 1000);
    }
    
    static public void TimerRefresh(int time){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                RefreshMatrix = "on";
            }
          }, time*1000);
    }
    
    
    public void draw(){
        background(107,136,255);
        textFont(createFont("Terminal", 20), 25);
        pushMatrix(); // lets us easily undo the upcoming translate call
        translate(-cameraOffsetX, (float) 0.0); 
        w.render();
        
        if (m != null){
            updateCameraPosition();
            m.inputCheck();
            m.move();
            m.draw();
        }


        if(MarioWin == 1)
        {
            if( RefreshMatrix == "on" ){
                setup();
                w.RefreshEnemy();
                MarioWin = 0;
                RefreshMatrix = "off";
            }
        }
        if(MarioDie == 1)
        {
            if( RefreshMatrix == "on" ){
                setup();
                frameCount = 0;
                MarioDie = 0;
                RefreshMatrix = "off";
            }
        }
        
        if (MarioTimer == 0){
            gameover();
            initTimer();
        }
        popMatrix();
        text( "MARIO", 10, 20);
        text( "TIME", WINDOW_WIDTH-150, 20);
        text( MarioPoint, 10, 40);
        text( MarioTimer, WINDOW_WIDTH-150, 40);
        PImage imageSprite = loadImage("Ressource/img/spriteSource3.gif");
        PImage coins = imageSprite.get(425, 161, 14, 18);
        image(coins, 200, 15);
        text( "x " + m.coinsCollected, 216, 30);
    }
    
    @Override
    public void keyPressed(){
        super.keyPressed();
        k.pressKey(key, keyCode);
    }
    
    void updateCameraPosition() {
        if (m != null){
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
