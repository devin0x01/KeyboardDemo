package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import androidx.core.content.ContextCompat;

public class RSKeyboardView extends KeyboardView {
    private Drawable mBackground;
    private int mLabelTextSize;
    private int mKeyTextColor;
    private Paint mPaint;
    private Keyboard mKeyboardNumber;
    private Keyboard mKeyboardQwerty;
    private int[] mKeyBackgroundResourceTriple;
    private int keyboardMode;

    public static final int KEYBOARD_MODE_NUMBER = 1;
    public static final int KEYBOARD_MODE_QWERTY = 2;
    public static final int[] NUMBER_KEY_BACKGROUND = {
            R.drawable.selector_kb_number_keyboard_done,
            R.drawable.selector_kb_number_keyboard_action,
            R.drawable.selector_kb_number_keyboard_normal
    };
    public static final int[] QWERTY_KEY_BACKGROUND = {
            R.drawable.selector_kb_qwerty_keyboard_done,
            R.drawable.selector_kb_qwerty_keyboard_action,
            R.drawable.selector_kb_qwerty_keyboard_normal
    };

    public RSKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBackground = new ColorDrawable(0xFF000000);
        mLabelTextSize = 14;
        mKeyTextColor = 0xFF000000;
        mPaint = new Paint();
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);

        mKeyboardNumber = new Keyboard(context, R.xml.keyboard_number);
        mKeyboardQwerty = new Keyboard(context, R.xml.keyboard_qwerty);
        mKeyBackgroundResourceTriple = NUMBER_KEY_BACKGROUND;
        keyboardMode = KEYBOARD_MODE_NUMBER;

        int[] attrsArray = {
                android.R.attr.background,
                android.R.attr.labelTextSize,
                android.R.attr.keyTextColor
        };
        TypedArray a = context.obtainStyledAttributes(attrs, attrsArray);
        mBackground = a.getDrawable(0) != null? a.getDrawable(0) : mBackground;
        mLabelTextSize = a.getDimensionPixelSize(1, mLabelTextSize);
        mKeyTextColor = a.getColor(2, mKeyTextColor);
        a.recycle();

        setKeyboard(mKeyboardNumber);
        setEnabled(true);
        setPreviewEnabled(false);

        System.out.println("==========================" + this.getClass().getName());
    }

    public void toggleMode() {
        if (keyboardMode == KEYBOARD_MODE_QWERTY) {
            setKeyboard(mKeyboardNumber);
            keyboardMode = KEYBOARD_MODE_NUMBER;
            mKeyBackgroundResourceTriple = NUMBER_KEY_BACKGROUND;
        } else {
            setKeyboard(mKeyboardQwerty);
            keyboardMode = KEYBOARD_MODE_QWERTY;
            mKeyBackgroundResourceTriple = QWERTY_KEY_BACKGROUND;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        Keyboard keyboard = getKeyboard();
        if (keyboard == null) {
            return;
        }
        for (Keyboard.Key key : keyboard.getKeys()) {
            switch (key.codes[0]) {
                case Keyboard.KEYCODE_DONE:
                    drawKeyBackground(mKeyBackgroundResourceTriple[0], canvas, key);
                    drawText(canvas, key, Color.WHITE);
                    drawIcon(canvas, key);
                    break;
                case Keyboard.KEYCODE_MODE_CHANGE:
                case Keyboard.KEYCODE_DELETE:
                case Keyboard.KEYCODE_CANCEL:
                    drawKeyBackground(mKeyBackgroundResourceTriple[1], canvas, key);
                    drawText(canvas, key, mKeyTextColor);
                    drawIcon(canvas, key);
                    break;
                default:
                    drawKeyBackground(mKeyBackgroundResourceTriple[2], canvas, key);
                    drawText(canvas, key, mKeyTextColor);
                    drawIcon(canvas, key);
                    break;
            }
        }
    }

    private void drawKeyBackground(int drawableId, Canvas canvas, Keyboard.Key key) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableId);
        if (drawable == null) {
            return;
        }
        int[] drawableState = key.getCurrentDrawableState();
        if (key.codes[0] != 0) {
            drawable.setState(drawableState);
        }
        drawable.setBounds(
                key.x + getPaddingStart(),
                key.y + getPaddingTop(),
                key.x + getPaddingStart() + key.width,
                key.y + getPaddingTop() + key.height
        );
        drawable.draw(canvas);
    }

    private void drawText(Canvas canvas, Keyboard.Key key, int color) {
        if (key.label == null) {
            return;
        }
        mPaint.setColor(color);
        mPaint.setTextSize(mLabelTextSize);
        canvas.drawText(
                key.label.toString(),
                key.x + (key.width - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft(),
                key.y + (key.height - getPaddingTop() - getPaddingBottom()) / 2 + (mPaint.getTextSize() - mPaint.descent()) / 2 + getPaddingTop(),
                mPaint
        );
    }

    private void drawIcon(Canvas canvas, Keyboard.Key key) {
        if (key.icon == null) {
            return;
        }
        key.icon.setBounds(
                key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + getPaddingStart(),
                key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + getPaddingTop(),
                key.x + (key.width - key.icon.getIntrinsicWidth()) / 2 + key.icon.getIntrinsicWidth() + getPaddingStart(),
                key.y + (key.height - key.icon.getIntrinsicHeight()) / 2 + key.icon.getIntrinsicHeight() + getPaddingTop()
        );
        key.icon.draw(canvas);
    }
}
