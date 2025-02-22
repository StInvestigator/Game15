package com.example.game15;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    LocalTime startTime;
    Game15 game = new Game15();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startTime = LocalTime.now();
        }
        CreateButtons();
    }

    private void CreateButtons() {
        GridLayout grid = findViewById(R.id.grid_id);
        grid.removeAllViews();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int val = game.getValue(i, j);
                if (val == 0) continue;

                Button btn = new Button(this);
                btn.setText(Integer.toString(val));
                btn.setHeight(300);

                GridLayout.Spec row = GridLayout.spec(i);
                GridLayout.Spec col = GridLayout.spec(j);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(row, col);
                params.setMargins(10, 10, 10, 10);

                grid.addView(btn, params);

                btn.setOnClickListener(v -> {
                    Game15.Coord coord = game.go(val);
                    if (coord.isValid()) {
                        GridLayout.Spec row2 = GridLayout.spec(coord.x);
                        GridLayout.Spec col2 = GridLayout.spec(coord.y);
                        GridLayout.LayoutParams params2 = new GridLayout.LayoutParams(row2, col2);
                        params2.setMargins(10, 10, 10, 10);
                        btn.setLayoutParams(params2);

                        if (game.isWin()) {
                            TextView tv = findViewById(R.id.tv_win);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                long seconds = Duration.between(startTime, LocalTime.now()).toSeconds();
                                tv.setText("You won! Time - " + seconds / 60 + ":" + seconds % 60);
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                long minutes = Duration.between(startTime, LocalTime.now()).toMinutes();
                                tv.setText("You won! Time - " + minutes + " minute(s)");
                            }
                            tv.setVisibility(VISIBLE);
                        }
                    }
                });
            }
        }
    }

    public void restart(View view) {
        TextView tv = findViewById(R.id.tv_win);
        tv.setVisibility(INVISIBLE);

        game.shuffle();
        CreateButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.info){
            openInfo();
        }
        else if(item.getItemId()==R.id.settings){
            Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openInfo() {
        Intent intent = new Intent(getBaseContext(), SecondActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            long seconds = Duration.between(startTime, LocalTime.now()).toSeconds();
            intent.putExtra("time", seconds / 60 + ":" + seconds % 60);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            long minutes = Duration.between(startTime, LocalTime.now()).toMinutes();
            intent.putExtra("time", minutes + " minute(s)");
        }
        intent.putExtra("turns", game.getTurnsCount());

        startActivity(intent);
    }
}