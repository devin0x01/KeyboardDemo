package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.SparseArray;

public class RSKeyboard {
    private RSKeyboardView mKeyboardView;
    private EditText mAttachedEditText;
    private OnKeyboardCancelListener onKeyBoardCancelListener;
    private OnKeyboardDoneListener onKeyboardOkListener;

    public interface OnKeyboardCancelListener {
        void onCancel();
    }

    public interface OnKeyboardDoneListener {
        void onDone();
    }

    public RSKeyboard(View rootView) {
        mKeyboardView = rootView.findViewById(R.id.keyboard_view);
    }

    public void attachTo(EditText editText) {
        mAttachedEditText = editText;
        mKeyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            private final SparseArray<String> code2StringMap = new SparseArray<>();
            {
                code2StringMap.put(-1001, "000");
                code2StringMap.put(-1002, "002");
                code2StringMap.put(-1003, "300");
                code2StringMap.put(-1004, "600");
                code2StringMap.put(-1005, "601");
            }

            @Override
            public void swipeRight() {}

            @Override
            public void onPress(int primaryCode) {}

            @Override
            public void onRelease(int primaryCode) {}

            @Override
            public void swipeLeft() {}

            @Override
            public void swipeUp() {}

            @Override
            public void swipeDown() {}

            @Override
            public void onText(CharSequence text) {}

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                if (mAttachedEditText != null) {
                    Editable editable = mAttachedEditText.getText();
                    int start = mAttachedEditText.getSelectionStart();
                    switch (primaryCode) {
                        case Keyboard.KEYCODE_DELETE:
                            if (keyCodes != null && keyCodes.length == 1) {
                                return;
                            }
                            if (editable != null && editable.length() > 0 && start > 0) {
                                editable.delete(start - 1, start);
                            }
                            break;
                        case Keyboard.KEYCODE_CANCEL:
                            hideKeyBoard();
                            if (onKeyBoardCancelListener != null) {
                                onKeyBoardCancelListener.onCancel();
                            }
                            mAttachedEditText.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
                            break;
                        case Keyboard.KEYCODE_DONE:
                            hideKeyBoard();
                            if (onKeyboardOkListener != null) {
                                onKeyboardOkListener.onDone();
                            }
                            mAttachedEditText.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
                            break;
                        case Keyboard.KEYCODE_MODE_CHANGE:
                            mKeyboardView.toggleMode();
                            mAttachedEditText.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
                            break;
                        default:
                            if (code2StringMap.indexOfKey(primaryCode) >= 0) {
                                editable.insert(start, code2StringMap.get(primaryCode));
                            } else {
                                editable.insert(start, String.valueOf((char) primaryCode));
                            }
                            mAttachedEditText.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
                            break;
                    }
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoard();
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, android.view.MotionEvent event) {
                editText.requestFocus();
                editText.requestFocusFromTouch();
                hideSystemSoftKeyboard(editText.getContext(), editText);
                return false;
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyBoard();
                }
            }
        });
    }

    public void showKeyBoard() {
        if (mKeyboardView.getVisibility() != View.VISIBLE) {
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyBoard() {
        if (mKeyboardView.getVisibility() == View.VISIBLE) {
            mKeyboardView.setVisibility(View.GONE);
        }
    }

    public static void hideSystemSoftKeyboard(Context context, EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            editText.setShowSoftInputOnFocus(false);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            try {
                Class<?> clazz = EditText.class;
                java.lang.reflect.Method setShowSoftInputOnFocus = clazz.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            editText.setInputType(android.text.InputType.TYPE_NULL);
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void setOnKeyboardDoneListener(OnKeyboardDoneListener listener) {
        this.onKeyboardOkListener = listener;
    }

    public void setOnKeyboardCancelListener(OnKeyboardCancelListener listener) {
        this.onKeyBoardCancelListener = listener;
    }
}
