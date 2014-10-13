
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
public class World 
{

    private PApplet engine;
    
    public World(PApplet _engine)
    {
        engine = _engine;
        initBasicLevel();
    }
    public World(PApplet _engine,int LevelType)
    {
        engine = _engine;
        switch(LevelType) {
        
            case 1:
                initBasicLevel();
            break;
            case 2:
                initHunderWorldLevel();
            break;
            case 3:
                initWaterWorldLevel();
            break;
            default:
                return;
        }
    }
    
    public void initBasicLevel()
    {
        engine.pushMatrix();
            PImage imageSprite1 = engine.loadImage("Ressource/img/spriteGround.gif");
            PImage breakingBrick = imageSprite1.get(0, 0, 16, 16);
            breakingBrick.resize(25, 25);
            CreateBloc(breakingBrick, 25, 25, 340, 2, 0, 550);
        engine.popMatrix();
        //Sound s = new Sound(engine);
        //s.LaunchStandartLevelSound();
        //_engine.rect(10, 10, 10, 10);
    }
    
    public void CreateBloc(PImage p, int sizeBlockX, int sizeBlockY , int NumberBlockCol, int NumberBlockRow, int X, int Y)
    {
        int TempNumbRow = 0;
        int TempY = 0;
        while (NumberBlockCol > 0) {
            TempNumbRow = NumberBlockRow;
            TempY = Y;
            while (TempNumbRow > 0) {
            engine.image(p, X, TempY);
            TempY = TempY + sizeBlockY;
            TempNumbRow--;
            }
            X = X + sizeBlockX;
            NumberBlockCol--;
        }
    }
    
    public void initHunderWorldLevel()
    {
    
    }
    
    public void initWaterWorldLevel()
    {
    
    }
    
}
