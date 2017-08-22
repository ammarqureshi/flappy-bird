package com.maq.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

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
    Texture gameOver;
    Texture playButton;
    Texture ground;
    float gap = 550;
    float maxOffset;
    int numberOfTubes = 4;
    float tubeDistance;
    float[] tubeX = new float[numberOfTubes];
    float[] offset = new float[numberOfTubes];
    float tubeVelocity = 4;
    Circle circle;
    Rectangle[] topRectangles;
    Rectangle[] bottomRectangles;
    ShapeRenderer shapeRenderer;
    Random rand;
    int score = 0;
    int consecutiveTubes = 0;
    BitmapFont font;


	//instantiate objects
    //texture = image
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        gameOver = new Texture("gameOver.png");

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        playButton = new Texture("playButton.png");
        ground = new Texture("ground.png");


        //declare the maximum offset of the tubes so they don't get off the screen
        maxOffset = Gdx.graphics.getHeight()/2 - gap - 100;
        tubeDistance = Gdx.graphics.getWidth()/2  + 100;
        rand = new Random();

        circle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().scale(8);
        topRectangles = new Rectangle[numberOfTubes];
        bottomRectangles = new Rectangle[numberOfTubes];

        initialize();

        // tubeX[0] = Gdx.graphics.getWidth() /2 - (topTube.getWidth() /2 + Gdx.graphics.getHeight() );

        //shape renderer is similar to batch, but renders shapes instead of textures
        //need to do collision detection on shapes
        shapeRenderer = new ShapeRenderer();
    }


    public void initialize(){
        //initial position of the bird is in the middle
        birdY = Gdx.graphics.getHeight()/2;

        for(int i =0;i<numberOfTubes;i++){

            offset[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()  - gap - 150);

            //first tube starts at the right of the screen
            tubeX[i] = Gdx.graphics.getWidth() /2 - topTube.getWidth() /2   + Gdx.graphics.getWidth()+ i * tubeDistance;

            topRectangles[i] = new Rectangle();
            bottomRectangles[i] = new Rectangle();
        }
    }


    public void reset(){
        consecutiveTubes = 0;
        score = 0;
        velocity =0;
    }

    //the render method is a continuous loop
	@Override
	public void render () {


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //create a gravity system

        //if the game has started, then the bird drops down by velocity
        if(gameState == 1) {

            //when the tube is on the left hand side of the screen, increment score

            //simple way of keeping track of the score would be to increment the score each time a tube passes by,
            //this means there wasn't a collision.
            if(tubeX[consecutiveTubes] < Gdx.graphics.getWidth() /2){

                score++;
                consecutiveTubes++;
                consecutiveTubes%=numberOfTubes;
                Gdx.app.log("score", String.valueOf(score));

            }

            //check if the user has clicked the screen, in which case start freefal
            if(Gdx.input.justTouched()){
                Gdx.app.log("screen", "touched");

                //move up the bird if the screen has just been touched
                velocity = -15;

            }

            for(int i =0;i<numberOfTubes;i++) {

                //check if the tube has gone off the edge of the screen
                if(tubeX[i] < -topTube.getWidth()){

                    //shift to the right, reuse the same tubes
                    tubeX[i] = numberOfTubes * tubeDistance;

                    //new tube offsets
                    offset[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()  - gap - 200);

                }
                else {
                    tubeX[i] -= 4;
                }


                //y coordinates of bottom left
                float topTubeY = Gdx.graphics.getHeight() / 2 + gap / 2 + offset[i];
                float bottomTubeY =  Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight()  + offset[i] + ground.getHeight() ;

                batch.draw(topTube, tubeX[i], topTubeY);
                batch.draw(bottomTube, tubeX[i],bottomTubeY);

                topRectangles[i].set(tubeX[i],topTubeY, topTube.getWidth(), topTube.getHeight());
                bottomRectangles[i].set(tubeX[i],bottomTubeY,bottomTube.getWidth(), bottomTube.getHeight());




            }



            //so the bird doesn't fall off from the screen
            if(birdY >0 || velocity <0 ) {
                velocity++;
                birdY -= velocity;
            }
            else{
                gameState = 2;
            }



        }
        else if(gameState == 0 && !Gdx.input.justTouched()){

            batch.draw(playButton, Gdx.graphics.getWidth()/2 - playButton.getWidth()/2, Gdx.graphics.getHeight()/2 - playButton.getHeight()/2);


        }
        else if(gameState ==0 && Gdx.input.justTouched()){
            gameState = 1;
        }
        else{

            batch.draw(gameOver,Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2);

            if(Gdx.input.justTouched()){
                gameState = 1;
                initialize();
                reset();

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


        //#comment out shapeRenderer code for debugging purposes, to see the cicles and the rectangles in red

       // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
       // shapeRenderer.setColor(Color.RED);

        //set a new location for the circle
        circle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth() /2);

        font.draw(batch, String.valueOf(score),0,Gdx.graphics.getHeight() - 20);
        //render the shape
        //shapeRenderer.circle(circle.x, circle.y, circle.radius);

        for(int i=0;i<numberOfTubes;i++) {
        //    shapeRenderer.rect(topRectangles[i].x, topRectangles[i].y, topTube.getWidth(), topTube.getHeight());
        //    shapeRenderer.rect(bottomRectangles[i].x, bottomRectangles[i].y, bottomTube.getWidth(), bottomTube.getHeight());


            //check if the circle(the bird) has collided with either the top or bottom tubes

            if(Intersector.overlaps(circle, topRectangles[i]) || Intersector.overlaps(circle, bottomRectangles[i])){
                Gdx.app.log("collision", "detected");
                gameState = 2;


                //play again
                if(Gdx.input.justTouched()){
                    gameState = 1;
                    //reset the position of the bird, and the tubes

                }

            }


        }
        //shapeRenderer.end();
        batch.draw(ground, 0,0,Gdx.graphics.getWidth(), ground.getHeight());

        batch.end();

    }
	

}
