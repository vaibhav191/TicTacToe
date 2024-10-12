# Tic-Tac-Toe Game with AI and Bluetooth Multiplayer

This repository contains a fully implemented Tic-Tac-Toe game for Android/iOS, developed as part of the CSE 535 Mobile Computing course at Arizona State University. The game features an AI opponent and Bluetooth multiplayer capabilities, allowing for both single-player and local multiplayer experiences.

## Features

### Single Player Modes
- **Easy Mode**: The AI makes random moves, providing a relaxed gameplay experience.
- **Medium Mode**: The AI makes random moves 50% of the time and optimal moves 50% of the time, offering a balanced challenge.
- **Hard Mode**: The AI uses the Minimax algorithm with alpha-beta pruning to make optimal moves, ensuring a challenging experience for players.

### Multiplayer Modes
- **Bluetooth Multiplayer**: Connect two devices via Bluetooth to play against another human player, with game state synchronization using JSON.
- **Local Player vs Player**: Two players can play on the same device, taking turns to make their moves.

### Data Persistence
Game results, including the winner and difficulty mode, are stored on the device using Realm. Realm provides a modern, efficient way to handle data persistence, offering features like live objects and automatic updates.

### User Interface
The app features a clean and intuitive UI with separate screens for gameplay, settings, and past games.

## Screens

- **Game Screen**: The main interface where the game is played, allowing users to compete against the AI or another human.
- **Settings Screen**: Adjust game settings, including difficulty levels and multiplayer options.
- **Past Games Screen**: View a history of past games, including date, winner, and difficulty mode.

## Technical Details

- **Programming Language**: Developed in Kotlin for Android using Jetpack Compose.
- **AI Algorithm**: Implements the Minimax algorithm with alpha-beta pruning for efficient decision-making.
- **Data Storage**: Utilizes Realm for storing game history and settings, providing a seamless and efficient data management experience.
- **Bluetooth Connectivity**: Establishes connections between devices for multiplayer gameplay, using JSON for data exchange.

## Setup Instructions

1. **Clone the Repository**:
bash git clone <git clone https://github.com/vaibhav191/Project2.git>


2. **Open in Android Studio/Xcode**: Import the project into your preferred IDE.

3. **Build and Run**: Follow the instructions in the README to build and run the application on your device or emulator.

## Usage

- Run the application on an emulator or physical device.
- Follow the on-screen instructions to use the app.
- **Bluetooth Setup**: Ensure Bluetooth is enabled on your devices for multiplayer gameplay.

## Contact

For any questions or feedback, please contact [vshriva5@asu.edu](mailto:vshriva5@asu.edu).
