
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Romain
 */

import ddf.minim.AudioPlayer;
import ddf.minim.*;
import processing.core.PApplet;


public class Sound{

    private PApplet engine;
    private Minim minim;
    private AudioPlayer player;
    
    public Sound(PApplet _engine) 
    {
        engine = _engine;
    }
    
    public void LaunchStandartLevelSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_over.mp3",2048);
        player.loop();
    }
    
    public void LaunchCloudLevelSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_starmancloud1.mp3",2048);
        player.loop();
    }
    
    public void LaunchUnderWorldLevelSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_undrwrld.mp3",2048);
        player.loop();
    }
    
    public void LaunchWaterLevelSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_water2.mp3",2048);
        player.loop();
    }
    
    public void LaunchFlagSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_flag.mp3",2048);
        player.play();
    }
    
    public void LaunchGameOverSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_gameover.mp3",2048);
        player.loop();
    }
    
    public void LaunchEndSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_end.mp3",2048);
        player.loop();
    }
    
    public void StopSound(){
        player.close();
        minim.stop();
        player = null;
        minim = null;
    }
}
