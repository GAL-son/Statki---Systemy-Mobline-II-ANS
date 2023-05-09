package com.battleships;

import static com.battleships.GameActivity.getFieldId;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.battleships.model.client.Game;
import com.battleships.model.client.Move;
import com.battleships.model.client.board.Field;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class SetShipsActivity extends AppCompatActivity {
    Game game_this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);

        Context context = getApplicationContext();
        createBoard(context);

        Button exitButton = findViewById(R.id.buttonExit);
        Button readyButton = findViewById(R.id.buttonReady);
        ImageView ship1x = findViewById(R.id.imageViewShip1x);
        ImageView ship2x = findViewById(R.id.imageViewShip2x);
        ImageView ship3x = findViewById(R.id.imageViewShip3x);
        ImageView ship4x = findViewById(R.id.imageViewShip4x);

        //game creation

        try {
            Game game=new Game(1,0);
            game_this=game;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToMainMenu();
            }
        });

        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameActivity(game_this);
            }
        });
    }

    private void goBackToMainMenu(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void goToGameActivity(Game game){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game",game_this);
        startActivity(intent);
    }

    public void createBoard(Context context) {
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        int imageResourceField = R.drawable.field;
        int imageResourceFieldWithoutShip = R.drawable.field_without_ship;

        for (int i = 0; i < 10; i++) {
            TableRow tableRow = new TableRow(context);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));

            for (int j = 0; j < 10; j++) {
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f
                ));

                imageView.setImageResource(imageResourceField);

                int marginInPixels = 2; // W pikselach
                float density = getResources().getDisplayMetrics().density;
                int marginInDp = (int) (marginInPixels / density);

                TableRow.LayoutParams layoutParams = (TableRow.LayoutParams) imageView.getLayoutParams();
                layoutParams.setMargins(marginInDp, marginInDp, marginInDp, marginInDp);
                imageView.setLayoutParams(layoutParams);

                // Nadanie unikalnego id dla każdego ImageView
                imageView.setId(i * 10 + j);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageView pom;
                        ImageView clickedImageView = (ImageView) v;
                       // clickedImageView.setImageResource(imageResourceFieldWithoutShip);
                        int pos = clickedImageView.getId();
                        Integer[] posId = new Integer[0];
                        posId = getFieldId(pos);
                        //
                        //ilosc statków
                        ArrayList<Integer> a=Game.histogram(game_this.getPlayer1().shipsSizes);
                        a=Game.histogram(game_this.getPlayer1().shipsSizes);
                        TextView x4=findViewById(R.id.textViewEnemy4xShips);
                        TextView x3=findViewById(R.id.textViewEnemy3xShips);
                        TextView x2=findViewById(R.id.textViewEnemy2xShips);
                        TextView x1=findViewById(R.id.textViewEnemy1xShips);

                        x1.setText(String.valueOf(a.get(0))+"x");
                        x2.setText(String.valueOf(a.get(1))+"x");
                        x3.setText(String.valueOf(a.get(2))+"x");
                        x4.setText(String.valueOf(a.get(3))+"x");

                        //
                        Snackbar.make(tableLayout, "Clicked on field " + String.valueOf(posId[0]) + ", " + String.valueOf(posId[1]), Snackbar.LENGTH_SHORT).show();
                        try {

                            game_this.place_ship(new Move(posId[0],posId[1],0),1,1);
                            for(int x=0;x<10;x++)
                            {
                                for(int y=0;y<10;y++)
                                {
                                            if (((Field)(game_this.getPlayer1().getPlayerBard().fields.get(y).get(x))).isOccupied())
                                            {
                                                pom=findViewById(10*x+y);
                                                pom.setImageResource(imageResourceFieldWithoutShip);
                                            }
                                }
                            }
                            a=Game.histogram(game_this.getPlayer1().shipsSizes);
                            {
                                x1.setText(String.valueOf(a.get(0))+"x");
                                x2.setText(String.valueOf(a.get(1))+"x");
                                x3.setText(String.valueOf(a.get(2))+"x");
                                x4.setText(String.valueOf(a.get(3))+"x");
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


                tableRow.addView(imageView);
            }
            tableLayout.addView(tableRow);
        }
    }
}