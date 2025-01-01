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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedPoints = sharedPreferences.getInt(PLAYER_POINTS_KEY, 0); // Valeur par défaut : 0
        TextView points = findViewById(R.id.points);
        points.setText(String.valueOf(savedPoints));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addPoint(View view) {
        TextView points = findViewById(R.id.points);
        int currentPoints = Integer.parseInt(points.getText().toString()) + 1;
        points.setText(String.valueOf(currentPoints));

        if (currentPoints % 100 == 0) {
            savePlayerPoints(currentPoints);
            Toast.makeText(this, "Palier atteint ! Points sauvegardés : " + currentPoints, Toast.LENGTH_SHORT).show();
        }
    }
    public void savePPoints(View view) {
        TextView points = findViewById(R.id.points);
        int currentPoints = Integer.parseInt(points.getText().toString());
        savePlayerPoints(Integer.parseInt(String.valueOf(currentPoints)));
        Toast.makeText(this, "Points sauvegardés : " + currentPoints, Toast.LENGTH_SHORT).show();
    }
    private void savePlayerPoints(int points) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PLAYER_POINTS_KEY, points);
        editor.apply();
    }
}
