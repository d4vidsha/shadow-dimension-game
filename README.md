# Project 2: Shadow Dimension Game

## Overview
This project implements level 0 and level 1 of Shadow Dimension, the game. This project utilises the Bagel library to draw the game window and handle user input. See the specifications for more details. The first commit of this project is imported from [project 1's repository](https://github.com/d4vidsha/swen20003-project-1/blob/master/README.md).

## Sources
- This project is part of the University of Melbourne subject [SWEN20003: Object Oriented Software Development 2022](https://handbook.unimelb.edu.au/2022/subjects/swen20003).
- The [Bagel library](https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/) is provided by the University of Melbourne.

## Requirements
- Run on a screen with a resolution of at least 1024x768.
- Game is intended to run on a 60Hz monitor.
- Java 11 or higher.

## How to run
Head to `ShadowDimension.java` and run the main method to launch the game.

## How to play
- Use the arrow keys to move the player.
- Use A to attack.
- Use K and L to decrease and increase [timescale](#timescale) respectively.

## Features of the game
### Timescale controls
The timescale determines the speed objects move in the game. It does not however affect the speed of the player. There are maximum and minimum values for the timescale, which can be found in the `ShadowDimension.java` file.
### W key
In development of the game, `W` is used to move the player to the next level without having to complete the current level. This is useful for testing the game.