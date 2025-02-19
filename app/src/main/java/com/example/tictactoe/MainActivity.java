package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    int activePlayer = 0; // 0 - X, 1 - O
    int[] gameState = {2,2,2,2,2,2,2,2,2}; // 2 - Empty, 0 - X, 1 - O
    int[][] winPositions = {
            {0,1,2}, {3,4,5}, {6,7,8}, // Rows
            {0,3,6}, {1,4,7}, {2,5,8}, // Columns
            {0,4,8}, {2,4,6}  // Diagonals
    };
    boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cellClicked(View view) {
        ImageView img = (ImageView) view;
        int tappedCell = Integer.parseInt(img.getTag().toString());

        if (!gameActive || gameState[tappedCell] != 2) return;

        gameState[tappedCell] = activePlayer;

        // Set the appropriate symbol
        if (activePlayer == 0) {
            img.setImageResource(R.drawable.x_symbol);
            activePlayer = 1;
        } else {
            img.setImageResource(R.drawable.o_symbol);
            activePlayer = 0;
        }

        // Set initial scale to 0 and animate to full size (pop-up effect)
        img.setScaleX(0f);
        img.setScaleY(0f);
        img.animate().scaleX(1f).scaleY(1f).setDuration(300).start();

        // Update the status text for the next player's turn
        TextView status = findViewById(R.id.status);
        if (gameActive) {
            status.setText("Player " + (activePlayer == 0 ? "X" : "O") + "'s Turn");
        }

        checkWinner();
    }

    public void checkWinner() {
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {

                gameActive = false;
                String winner = (gameState[winPosition[0]] == 0) ? "X" : "O";
                TextView status = findViewById(R.id.status);
                status.setText("Player " + winner + " Wins!");
                return;
            }
        }

        boolean draw = true;
        for (int state : gameState) {
            if (state == 2) draw = false;
        }

        if (draw) {
            gameActive = false;
            TextView status = findViewById(R.id.status);
            status.setText("It's a Draw!");
        }
    }

    public void restartGame(View view) {
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }

        ((TextView) findViewById(R.id.status)).setText("Player X's Turn");

        // Loop through actual IDs (cell_00, cell_01, ..., cell_22)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                String cellID = "cell_" + row + col;
                int resID = getResources().getIdentifier(cellID, "id", getPackageName());
                ((ImageView) findViewById(resID)).setImageResource(0);
            }
        }
    }
}
