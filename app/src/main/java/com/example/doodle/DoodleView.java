package com.example.doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DoodleView extends View {
    private Paint paint;
    private Path path;
    private List<Path> paths;
    private List<Paint> paints;
    private Stack<Path> undoStack;
    private Stack<Paint> undoPaintStack;
    private Stack<Path> redoStack;
    private Stack<Paint> redoPaintStack;
    private int currentOpacity = 255;

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xFFFFC0CB); // Default color is pink
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setAlpha(currentOpacity);

        path = new Path();
        paths = new ArrayList<>();
        paints = new ArrayList<>();
        undoStack = new Stack<>();
        undoPaintStack = new Stack<>();
        redoStack = new Stack<>();
        redoPaintStack = new Stack<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                paths.add(path);
                paints.add(new Paint(paint));
                undoStack.push(path);
                undoPaintStack.push(new Paint(paint));

                redoStack.clear();
                redoPaintStack.clear();
                path = new Path();
                invalidate();
                break;
        }
        return true;
    }

    public void clearCanvas() {
        paths.clear();
        paints.clear();
        undoStack.clear();
        undoPaintStack.clear();
        redoStack.clear();
        redoPaintStack.clear();
        path.reset();
        invalidate();
    }

    public void setBrushSize(float size) {
        paint.setStrokeWidth(size);
    }

    public void setBrushColor(int color) {
        paint.setColor(color);
    }

    public void setBrushOpacity(int alpha) {
        currentOpacity = alpha;
        paint.setAlpha(alpha);
    }

    public int getBrushOpacity() {
        return currentOpacity;
    }

    public void undo() {
        if (!paths.isEmpty()) {
            Path undonePath = paths.remove(paths.size() - 1);
            Paint undonePaint = paints.remove(paints.size() - 1);

            redoStack.push(undonePath);
            redoPaintStack.push(undonePaint);

            invalidate();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Path redonePath = redoStack.pop();
            Paint redonePaint = redoPaintStack.pop();

            paths.add(redonePath);
            paints.add(redonePaint);

            invalidate();
        }
    }
}

