# INFO1113 - 2024 S2 Tasks and Assignments

This repository contains most of the assignments from the INFO1113 course in the 2024 S2 semester. The most challenging tasks were implementing the **Minesweeper** and **Inkball** games in Java.

## Project Overview

### Minesweeper
Minesweeper is a classic puzzle game where players clear a grid while avoiding hidden mines. Key features of this implementation include:
- **Dynamic Grid Generation**: The game grid is dynamically generated based on the difficulty level.
- **Mine Distribution**: Mines are randomly placed, ensuring the first click is always safe.
- **Timer and Win/Loss Detection**: A timer tracks gameplay, and the game determines win/loss conditions in real-time.
- **Recursive Reveal Mechanism**: Safe areas are revealed automatically upon clicking empty cells.

### Inkball
Inkball is a physics-based puzzle game where players draw lines to guide balls into target zones. Key features include:
- **Physics Engine**: Simulates gravity, collisions, and ball reflections.
- **Level Design**: Multiple levels with increasing difficulty.
- **Dynamic Interactions**: Allows players to draw lines in real-time to adjust strategies.
- **Smooth Animations**: Includes fluid animations and responsive visual feedback.

## Requirements

To run these projects, you need the following:

1. **Java 8**  
   Ensure you have Java 8 or higher installed on your machine. You can download it from [Oracle Java Downloads](https://www.oracle.com/java/technologies/javase-downloads.html).

2. **Gradle**  
   The project uses Gradle as the build tool. Download and set it up from [Gradle Install Guide](https://gradle.org/install/). The installation is straightforward with instructions provided on the official website.

## How to Run

Clone the repository to your local machine:
   ```bash
   git clone https://github.com/Dragolone/INFO1113-Java-.git
   cd INFO1113-Java-
   gradle build
   gradle run
