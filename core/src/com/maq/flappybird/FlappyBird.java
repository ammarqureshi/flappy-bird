package com.maq.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	//drawing tool to draw the images on to the screen, like a pencil
	SpriteBatch batch;

	//texture  = image
	Texture background;
	Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    int gameState = 0;
    Texture bottomTube;
    Texture topTube;
    float gap = 400;
    float maxOffset;
    float offset;
    float tubeX;
    float tubeVelocity = 4;
    Random rand;


	//instantiate objects
    //texture = image
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        //initial position of the bird is in the middle
        birdY = Gdx.graphics.getHeight()/2;

        //declare the maximum offset of the tubes so they don't get off the screen
        maxOffset = Gdx.graphics.getHeight()/2 - gap - 100;

        rand = new Random();

        tubeX = Gdx.graphics.getWidth() /2;
    }

    //the render method is a continuous loop
	@Override
	public void render () {


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //create a gravity system

        //if the game has started, then the bird drops down by velocity
        if(gameState != 0) {

            //check if the user has clicked the screen, in which case start freefal
            if(Gdx.input.justTouched()){
                Gdx.app.log("screen", "touched");

                //move up the bird if the screen has just been touched
                velocity = -15;
                offset = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()  - gap - 200);
                tubeX = Gdx.graphics.getWidth() /2;

            }


            tubeX -=4;

            batch.draw(topTube,tubeX,   Gdx.graphics.getHeight()/2 + gap/2 + offset);
            batch.draw(bottomTube,tubeX , Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight() + offset);



            //so the bird doesn't fall off from the screen
            if(birdY >0 || velocity <0 ) {
                velocity++;
                birdY -= velocity;
            }



        }
        else{


            if(Gdx.input.justTouched()){
                Gdx.app.log("screen", "touched");
                gameState = 1;
            }

        }


        if(flapState == 0){
            flapState = 1;
        }
        else{
            flapState = 0;
        }

        //recalculate depending on the game state

        batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - (birds[flapState].getWidth() / 2), birdY);
        batch.end();

    }
	

}
