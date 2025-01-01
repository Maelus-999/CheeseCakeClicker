package fr.maelus.cheesecakeclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "PlayerPrefs";
    private static final String PLAYER_POINTS_KEY = "PlayerPoints";
    private static final String POINTS_PER_CLICK_KEY = "PointsPerClick";
    private int pointsPerClick = 1;
    private int lastSavedHundred = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        int savedPoints = sharedPreferences.getInt(PLAYER_POINTS_KEY, 0);
        pointsPerClick = sharedPreferences.getInt(POINTS_PER_CLICK_KEY, 1);
        lastSavedHundred = sharedPreferences.getInt("LastSavedHundred", 0);

        TextView points = findViewById(R.id.points);
        points.setText(String.valueOf(savedPoints));

        TextView pointsPerClickTxt = findViewById(R.id.costPointPerEachClickTxt);
        pointsPerClickTxt.setText(String.valueOf(50 * pointsPerClick));



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addPoint(View view) {
        TextView points = findViewById(R.id.points);
        int currentPoints = Integer.parseInt(points.getText().toString()) + pointsPerClick;
        points.setText(String.valueOf(currentPoints));

        int requiredCheesecakesForNextCheckpoint = 100 * pointsPerClick;

        if (currentPoints >= requiredCheesecakesForNextCheckpoint + (lastSavedHundred * 100)) {
            saveGameState(currentPoints);
            lastSavedHundred = currentPoints / 100;
                Toast.makeText(this, getString(R.string.lvlPass) +  " " + getString(R.string.savedPoints) + " " + currentPoints, Toast.LENGTH_SHORT).show();
        }
    }

    public void incrementPointsPerClick(View view) {
        TextView points = findViewById(R.id.points);
        int currentPoints = Integer.parseInt(points.getText().toString());

        int costToUpgrade = 100 * pointsPerClick;

        if (currentPoints >= costToUpgrade) {
            currentPoints -= costToUpgrade;
            pointsPerClick += 1;

            TextView pointsPerClickTxt = findViewById(R.id.costPointPerEachClickTxt);
            pointsPerClickTxt.setText(String.valueOf(100 * pointsPerClick));

            saveGameState(currentPoints);
            points.setText(String.valueOf(currentPoints));
            Toast.makeText(this, getString(R.string.PointPerClickUpgraded) + " "+ pointsPerClick + " ! " + getString(R.string.YouLoose) + " " + costToUpgrade + " " + getString(R.string.points), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.NotEnoughPoints), Toast.LENGTH_SHORT).show();
        }
    }

    public void savePPoints(View view) {
        TextView points = findViewById(R.id.points);
        int currentPoints = Integer.parseInt(points.getText().toString());
        int currentHundred = currentPoints / 100;
        lastSavedHundred = currentHundred;
        saveGameState(currentPoints);
        Toast.makeText(this, getString(R.string.savedPoints) + " " + currentPoints, Toast.LENGTH_SHORT).show();
    }

    private void saveGameState(int points) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(PLAYER_POINTS_KEY, points);
        editor.putInt(POINTS_PER_CLICK_KEY, pointsPerClick);
        editor.putInt("LastSavedHundred", lastSavedHundred);

        editor.apply();
    }
}
