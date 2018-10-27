# game-development
The project is created in Java Object Oriented Programing. Tank War game is multiplayer game and the Lazarus Game is level based game. The project is based on two games and to learn how to reuse the code. The first one is Tank War Game. The second game is Lazarus Game, which reuses some of the codes from Tank War Game.

## Overview
This program is written in Java, using the Net Beans 8.2 and JDK 1.8. The purpose of this assignment is to provide us with the opportunity to work in a team with one other student to create an object oriented solution for a first game, that can then be re-used for a second game. While doing this, we will be exposed to GUI creation and graphics in Java.

## How to Use the Project
• Open the project in Net Beans 8.2 for best result
• The user must clone the repo and set up as a project with existing source code. Then set up the working directory by right clicking on the newly created project Set Configuration
-> Run -> Working Directory
• Then select the cloned repo and continue
• The user must use the MyGame.TankGame and
lazarusgame.LazarusGame in order to execute the fully
functioning games.
• Afterwards, the program should compile and execute using
the play button and it will prompt the user click on the start game.

## Class Diagrams

## Implementations

 # TANK WAR GAME:

Package name: resource
This package contains the files used by the java program for sound and images. It has also a file with the map of the walls and the starting position of the tanks. 

Package name: tank
This package contains the java code.
Classes:
GameStart.java:  It contains the “main” and creates objects of the SoundEffects.java class to call methods that plays background music continuously or just once. It also creates object of the ControlPanel.java class.

SoundEffects.java: It has two methods: ones that plays music continuously and another that plays sound just once.
GamePanel.java:  This class extends JPanel and implements Runnable. It has methods to get the tank position, to do animation and the paintComponent method which draws the background image, including the mini map. 

ControlPanel.java:  This class extends GamePanel.java and implements KeyListener. It has key released, key pressed and run methods. The run method processes the keyboard inputs that moves the tank and does the animation. 

GameAnimation.java: It has methods for the animation of the bullets and explosion. 

GameFrame.java: This class sets the title in the game frame,  gets an object of the type GamePanel.java as an argument, has add, pack, setVisible, and  instantiates an object of type thread  and then calls a thread start method. 
ImageObject.java: This class has methods to get and set the image location and dimension
Wall.java: This class extends the ImageObject.java class. It draws the breakable and unbreakable walls. 

TankBullet.java: This class extends the GameAnimation.java class. It draws the bullet, checks collision of the bullets with the walls and collision the bullets with tank. 

Tank.java:  This class extends ImageObject.java. It loads the tank image, sets the tank movements , checks collision of the tank and walls and tank with tank. 
Player.java:  It has methods to display health, lives and points. 

Icon.java: It has methods to load the images of the sprites. 

 # LAZARUS GAME:

Package name: resource

This package contains the files used by the java program for sound and images. It has also a file contains the map of the walls, the stop button location and Lazarus initial location. 

Package name:  Lazarus

This package contains the java code.

Class Description:

SoundEffects.java : This class has methods to play sounds continuously or just once.

KeyControl.java : This class extends KeyAdapter and will get the keyboard actions, which then is passed to EventNotifier.

EventNotifier.java : This class implements the Observable class. It will notify the observer whenever collisions and keyboard events occur.

GameObject.java: This class has the data fields and behaviors that are shared among the objects of the game, such as position and speed. It contains update, draw, get, set methods.

Box.java: This class extends GameObject and implements Observer. This class has data fields for the position, speed, kind of box, and the box situation (dropping or not). There are get methods, method to detect the collision event, draw and update methods. 

StopButton.java: This class extends GameObject and implements Observer. The stop button is used to stop the boxes from falling and then advancing the level to the next level. It has setters and update methods.

Lazarus.java:  This class extends GameObject and implements Observer. It has methods related to the position and movements of Lazarus. It has get, set, collision event identification, update, and draw methods.

GameWorld.java: This class extends JApplet and implements Runnable. It has methods to convert image files in buffered images, as well as, start, run, get and set methods. 

LazarusWorld.java: This class extends GameWorld.java. It loads the  background, the sprites images, and checks the collisions between boxes, Lazarus, wall, and stop buttons.

GameStart.java: This class instantiates objects of the SoundEffects.java and LazarusWorld.java classes, as well as sets up the frame for the game. It has the “main” method that initializes the game.


## Assumptions
• Tank Wars: For Tank Game we assume that we don’t have to make the graphic looks exactly the same as instructor's example. So we let the health bar and life dots to go with the tank instead of showing them at the bottom of the screen.
• Lazarus Game: For Lazarus Game, we assume that we created multiple levels, so we only did multiple levels and made the map looks exactly the same as the example shown on instruction. Also, when we tried to reuse the objects we created for Tank Wars, we assume that we can simply duplicate the objects and make modifications in order to make it work for our second game.

## Goals of Games
The goal of Tank (the game) is to have multiplayer game. The two tanks move using key controls provided in read me file on the GitHub for the Tank Game. We were not able to get the split screen but we managed to get all the other requirements for the game.
The goal of Lazarus (the game) is to get the character, Lazarus, out of the pit. Boxes are dropped on Lazarus, but once Lazarus hits the stop button the machine that drops boxes is stopped. If a box falls on Lazarus, the game is lost. Lazarus must climb on the boxes to get to the stop button. The kinds of boxes are, from lightest to heaviest: cardboard, wood, stone and metal. If a heavier box falls on a lighter one, the lighter one is crushed. The left and right arrow keys control the movement of Lazarus. There are three levels for the game.

## Conclusion
I have tried to reuse the objects from the Tank Game, and we have an opportunity to examine the reusability of object-oriented programming. Another important thing about this assignment is we have experienced about how to code in a team, and what pair programming is.
One of the toughest projects we have done in our academic career so far.
This is one of the largest learning curves from a project. There was a lot to keep track of before even trying to make the program start running at all.
This was a bit refreshing to use images and frames.
We are still learning to import properly because we feel like if we do, then we are doing something wrong, but it is actually the opposite.
It was difficult to make some work around and a lot of grinding to figure out what was a more optimal solution.
A lot of teamwork and a lot of logical discussing of the code were needed to get through this term project.

## Lessons Learned
• To reuse code. Had more practice with GitHub. 
• Challenges: To work with images and frames.

## References
• Helpful source: Airstrike and internet tutorials, such as www.tutorialspoint.com
