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

public class MainActivity extends AppCompatActivity {

    int activePlayer = 0; // 0 - X (Red), 1 - O (Green)
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2}; // 2 - Empty, 0 - X, 1 - O
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
        updateStatusText();
    }

    public void cellClicked(View view) {
        if (!gameActive) return;

        ImageView img = (ImageView) view;
        int tappedCell = Integer.parseInt(img.getTag().toString());

        if (gameState[tappedCell] != 2) return;

        gameState[tappedCell] = activePlayer;

        if (activePlayer == 0) {
            img.setImageResource(R.drawable.x_symbol);
            activePlayer = 1;
        } else {
            img.setImageResource(R.drawable.o_symbol);
            activePlayer = 0;
        }

        // Pop-up animation effect
        img.setScaleX(0f);
        img.setScaleY(0f);
        img.animate().scaleX(1f).scaleY(1f).setDuration(300).start();

        checkWinner();
        if (gameActive) {
            updateStatusText();
        }
    }

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

    public void checkWinner() {
        TextView status = findViewById(R.id.status);
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {

                gameActive = false;
                String winner = (gameState[winPosition[0]] == 0) ? "X" : "O";
                int winnerColor = (winner.equals("X")) ?
                        ContextCompat.getColor(this, R.color.x_color) :
                        ContextCompat.getColor(this, R.color.o_color);

                status.setText(getString(winner.equals("X") ? R.string.player_x_wins : R.string.player_o_wins));
                status.setTextColor(winnerColor);

                return;  // Exit loop early when a winner is found
            }
        }

        // Check for a draw
        if (!isMovesLeft()) {
            gameActive = false;
            status.setText(getString(R.string.draw_message));
            status.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray)); // Draw message in gray
        }
    }

    private boolean isMovesLeft() {
        for (int state : gameState) {
            if (state == 2) return true;
        }
        return false;
    }

    public void restartGame(View view) {
        gameActive = true;
        activePlayer = 0;
        Arrays.fill(gameState, 2); // Replaces the loop with Arrays.fill()

        TextView status = findViewById(R.id.status);
        status.setText(getString(R.string.player_x_turn));
        status.setTextColor(ContextCompat.getColor(this, R.color.x_color));

        int[][] cellIds = {
                {R.id.cell_00, R.id.cell_01, R.id.cell_02},
                {R.id.cell_10, R.id.cell_11, R.id.cell_12},
                {R.id.cell_20, R.id.cell_21, R.id.cell_22}
        };

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                ((ImageView) findViewById(cellIds[row][col])).setImageResource(0);
            }
        }
    }
}
