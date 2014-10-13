
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

class AnimationSprite {
  PImage[] images;
  int imageCount;
  int frame = 0;
  PApplet engine; 
  
  AnimationSprite(PImage image[],int _imageCount, PApplet _engine) {
    engine = _engine;
    imageCount = _imageCount;
    images = image;
  }

  void display(float xpos, float ypos) {
    if (frame >=  imageCount)
        frame = 0;
    frame = (frame+1);
    engine.image(images[frame], xpos, ypos);
  }
  
  void display(float xpos, float ypos, int idImage) {
    frame = idImage;
    engine.image(images[frame], xpos, ypos);
  }
  
  int getWidth() {
    return images[0].width;
  }
}
