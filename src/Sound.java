
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
    
    private Minim minimStandartLevelSound;
    private AudioPlayer playerStandartLevelSound;
    public void LaunchStandartLevelSound(){
        minimStandartLevelSound = new Minim(engine);
        playerStandartLevelSound = minimStandartLevelSound.loadFile("Ressource/Music/1-01-main-theme-overworld.mp3",2048);
        playerStandartLevelSound.loop();
    }
    
    public void StopStandartLevelSound()
    {
        if (playerStandartLevelSound != null)
            playerStandartLevelSound.mute();
        if (playerLaunchUnderWorldLevelSound != null)
            playerLaunchUnderWorldLevelSound.mute();
        if (playerLaunchCastleLevelSound != null)
            playerLaunchCastleLevelSound.mute();
        if (player != null)
            player.mute();
        if (minim != null)
            minim.dispose();
    }
    
    public void LaunchCloudLevelSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_starmancloud1.mp3",2048);
        player.loop();
    }
    
    private Minim minimLaunchUnderWorldLevelSound;
    private AudioPlayer playerLaunchUnderWorldLevelSound;
    public void LaunchUnderWorldLevelSound(){
        minimLaunchUnderWorldLevelSound = new Minim(engine);
        playerLaunchUnderWorldLevelSound = minimLaunchUnderWorldLevelSound.loadFile("Ressource/Music/1-02-underworld.mp3",2048);
        playerLaunchUnderWorldLevelSound.loop();
    }
    
    public void LaunchWaterLevelSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_water2.mp3",2048);
        player.loop();
    }
    
    
    private Minim minimLaunchCastleLevelSound;
    private AudioPlayer playerLaunchCastleLevelSound;
    public void LaunchCastleLevelSound(){
        minimLaunchCastleLevelSound = new Minim(engine);
        playerLaunchCastleLevelSound = minimLaunchCastleLevelSound.loadFile("Ressource/Music/1-04-castle.mp3",2048);
        playerLaunchCastleLevelSound.loop();
    }
    
    public void LaunchFlagSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/Music/smb_flag.mp3",2048);
        if (!player.isPlaying()){
            player.rewind();
        }
        player.play();
        if (!player.isPlaying()){
            player.rewind();
        }
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
        if (player != null){
        player.close();
        }
    }
    
    public void LaunchJumpSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/effectSound/smb_jump-small.mp3",2048);
        player.play();
    }
    
    public void LaunchWinSound(){
        System.out.println("Sound activated");
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/effectSound/smb_stage_clear.mp3",2048);
        if (!player.isPlaying()){
            player.rewind();
        }
        player.play();
        if (!player.isPlaying()){
            player.rewind();
        }
    }
    
    public void LaunchMarioDieSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/effectSound/smb_mariodie.mp3",2048);
        player.play();
    }
    
    public void LaunchGoombaDieSound(){
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/effectSound/smb_stomp.mp3",2048);
        player.play();
    }

    void LaunchCoinSound() {
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/effectSound/smb_coin.mp3",2048);
        player.play();
    }
    
    void LaunchBrickBlockChocSound() {
        minim = new Minim(engine);
        player = minim.loadFile("Ressource/effectSound/smb_bump.mp3",2048);
        player.play();
    }
}
