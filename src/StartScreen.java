
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
public class StartScreen {

    private PApplet engine;
    private World w;
    private Mario m;
    private Sound s;
    
    public StartScreen(PApplet _engine) {
        engine = _engine;
    }
    
    public void PlayGame()
    {
        w = new World(engine);
        w.initBasicLevel();
        m = new Mario(engine,0,520);
        //s = new Sound(engine);
    }

    void StartGame() {
        PlayGame();
    }
    
}
