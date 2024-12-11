package com.example.doodle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DoodleView doodleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleView = findViewById(R.id.doodleView);

        // Clear Button
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(view -> doodleView.clearCanvas());

        // Brush Size Button
        Button brushSizeButton = findViewById(R.id.brushSizeButton);
        brushSizeButton.setOnClickListener(view -> showBrushSizeDialog());

        // Color Picker Button
        Button colorPickerButton = findViewById(R.id.colorPickerButton);
        colorPickerButton.setOnClickListener(view -> showColorPickerDialog());

        // Opacity Button
        Button opacityButton = findViewById(R.id.opacityButton);
        opacityButton.setOnClickListener(view -> showOpacityDialog());

        // Undo Button
        Button undoButton = findViewById(R.id.undoButton);
        undoButton.setOnClickListener(view -> doodleView.undo());

        // Redo Button
        Button redoButton = findViewById(R.id.redoButton);
        redoButton.setOnClickListener(view -> doodleView.redo());
    }

    private void showBrushSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Brush Size");

        String[] sizes = {"Small", "Medium", "Large"};
        builder.setItems(sizes, (dialog, which) -> {
            switch (which) {
                case 0:
                    doodleView.setBrushSize(10); // Small
                    break;
                case 1:
                    doodleView.setBrushSize(20); // Medium
                    break;
                case 2:
                    doodleView.setBrushSize(30); // Large
                    break;
            }
        });

        builder.create().show();
    }

    private void showColorPickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Color");

        String[] colors = {"Red", "Blue", "Green", "Pink"};
        builder.setItems(colors, (dialog, which) -> {
            switch (which) {
                case 0:
                    doodleView.setBrushColor(getResources().getColor(android.R.color.holo_red_light));
                    break;
                case 1:
                    doodleView.setBrushColor(getResources().getColor(android.R.color.holo_blue_light));
                    break;
                case 2:
                    doodleView.setBrushColor(getResources().getColor(android.R.color.holo_green_light));
                    break;
                case 3:
                    doodleView.setBrushColor(0xFFFFC0CB); // Pink
                    break;
            }
        });

        builder.create().show();
    }

    private void showOpacityDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adjust Opacity");

        SeekBar opacitySeekBar = new SeekBar(this);
        opacitySeekBar.setMax(255);
        opacitySeekBar.setProgress(doodleView.getBrushOpacity());

        builder.setView(opacitySeekBar);
        builder.setPositiveButton("OK", (dialog, which) -> {
            int alpha = opacitySeekBar.getProgress();
            doodleView.setBrushOpacity(alpha);
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }
}
