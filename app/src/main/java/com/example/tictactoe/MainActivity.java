package com.example.tictactoe;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Arrays;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Emulator: Pixel 4 API 33
public class MainActivity extends AppCompatActivity {


    int activePlayer = 0;

    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};

    // Winning positions (Rows, Columns, and Diagonals)
    int[][] winPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
            {0, 4, 8}, {2, 4, 6}  // Diagonals
    };


    boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set the initial status text
        updateStatusText();
    }

    // Handles cell clicks during the game
    public void cellClicked(View view) {
        if (!gameActive) return; // Ignore clicks if the game is over

        ImageView img = (ImageView) view;
        int tappedCell = Integer.parseInt(img.getTag().toString());

        // Ignore already occupied cells
        if (gameState[tappedCell] != 2) return;

        // Update game state
        gameState[tappedCell] = activePlayer;

        // Set appropriate symbol image based on the player
        if (activePlayer == 0) {
            img.setImageResource(R.drawable.x_symbol);
            activePlayer = 1;
        } else {
            img.setImageResource(R.drawable.o_symbol);
            activePlayer = 0;
        }

        // Pop-up animation effect for symbols
        img.setScaleX(0f);
        img.setScaleY(0f);
        img.animate().scaleX(1f).scaleY(1f).setDuration(300).start();

        // Check for a winner
        checkWinner();

        // Update status text only if the game is still active
        if (gameActive) {
            updateStatusText();
        }
    }

    // Updates the status text whose turn it is
    private void updateStatusText() {
        TextView status = findViewById(R.id.status);
        int newColor;

        if (activePlayer == 0) {
            status.setText(getString(R.string.player_x_turn));
            newColor = ContextCompat.getColor(this, R.color.x_color);
        } else {
            status.setText(getString(R.string.player_o_turn));
            newColor = ContextCompat.getColor(this, R.color.o_color);
        }

        // Smooth color transition animation
        ObjectAnimator colorAnim = ObjectAnimator.ofObject(
                status, "textColor", new ArgbEvaluator(),
                status.getCurrentTextColor(), newColor
        );
        colorAnim.setDuration(500);
        colorAnim.start();
    }

    // Checks if a player has won the game
    public void checkWinner() {
        TextView status = findViewById(R.id.status);

        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {

                gameActive = false; // End the game

                // Determine the winner
                String winner = (gameState[winPosition[0]] == 0) ? "X" : "O";
                int winnerColor = (winner.equals("X")) ?
                        ContextCompat.getColor(this, R.color.x_color) :
                        ContextCompat.getColor(this, R.color.o_color);

                // Update status text with winner message
                status.setText(getString(winner.equals("X") ? R.string.player_x_wins : R.string.player_o_wins));
                status.setTextColor(winnerColor);

                return;  // Exit loop early once a winner is found
            }
        }

        // If no winner, check for a draw
        if (!isMovesLeft()) {
            gameActive = false;
            status.setText(getString(R.string.draw_message));
            status.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray)); // Gray color for draw
        }
    }

    // Checks if there are any empty cells left
    private boolean isMovesLeft() {
        for (int state : gameState) {
            if (state == 2) return true; // If any cell is empty, moves are still left
        }
        return false;
    }

    // Restarts the game
    public void restartGame(View view) {
        gameActive = true;
        activePlayer = 0;
        Arrays.fill(gameState, 2); // Reset game state to empty cells

        // Reset status text to player X's turn
        TextView status = findViewById(R.id.status);
        status.setText(getString(R.string.player_x_turn));
        status.setTextColor(ContextCompat.getColor(this, R.color.x_color));

        // Reset all image views to empty
        int[][] cellIds = {
                {R.id.cell_00, R.id.cell_01, R.id.cell_02},
                {R.id.cell_10, R.id.cell_11, R.id.cell_12},
                {R.id.cell_20, R.id.cell_21, R.id.cell_22}
        };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                ((ImageView) findViewById(cellIds[row][col])).setImageResource(0); // Clear images
            }
        }
    }
}
