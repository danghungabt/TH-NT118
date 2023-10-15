package vn.uit.nt118.thb1changeicon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CountDownActivity extends AppCompatActivity {
    private EditText hoursEditText, minutesEditText, secondsEditText;

    TextView countDownText;
    Intent intent;
    PendingIntent pendingIntent;

    private Button start, pause, end, reset;
    private CountDownTimer timer;

    int startTime;

    int hoursLeft, minutesLeft, secondsLeft;
    TextView hoursLeftText, minutesLeftText, secondsLeftText;

    int totalSecondsLeft;

    boolean isPaused = false;

    private void finishTimer(String message) {
        countDownText.setText(message);
        start.setEnabled(true);
        pause.setEnabled(false);
        end.setEnabled(false);
        reset.setEnabled(false);

        setEnableEdt(true);

        try {
            // Kích hoạt PendingIntent
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

    }

    private void endTimer(String message) {
        countDownText.setText(message);
        start.setEnabled(true);
        pause.setEnabled(false);
        end.setEnabled(false);

        setEnableEdt(true);
    }

    private void setupEditTexts() {
        hoursEditText = findViewById(R.id.hours);
        minutesEditText = findViewById(R.id.minutes);
        secondsEditText = findViewById(R.id.seconds);

        hoursEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    minutesEditText.requestFocus();
                }
            }
        });

        minutesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    secondsEditText.requestFocus();
                }
            }
        });
    }

    private void updateTimeRemaining(long millisUntilFinished) {
        totalSecondsLeft = (int) millisUntilFinished / 1000;
        hoursLeft = totalSecondsLeft / 3600;
        minutesLeft = (totalSecondsLeft % 3600) / 60;
        secondsLeft = totalSecondsLeft % 60;
        hoursLeftText.setText(String.format("%02d", hoursLeft));
        minutesLeftText.setText(String.format("%02d", minutesLeft));
        secondsLeftText.setText(String.format("%02d", secondsLeft));
        countDownText.setText("Count down in progress");
    }

    private void setupButtons() {
        start = findViewById(R.id.start_button);
        reset = findViewById(R.id.reset_button);
        pause = findViewById(R.id.pause_button);
        end = findViewById(R.id.end_button);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = 0;
                startTime += Integer.parseInt(secondsEditText.getText().toString()) * 1000;
                startTime += Integer.parseInt(minutesEditText.getText().toString()) * 60 * 1000;
                startTime += Integer.parseInt(hoursEditText.getText().toString()) * 60 * 60 * 1000;

                start.setEnabled(false);
                reset.setEnabled(true);
                pause.setEnabled(true);
                end.setEnabled(true);

                setEnableEdt(false);

                timer = new CountDownTimer(startTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        updateTimeRemaining(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        finishTimer("Count down complete");
                    }
                }.start();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setEnabled(false);
                reset.setEnabled(true);
                pause.setEnabled(true);
                pause.setText("Pause");
                isPaused = false;
                end.setEnabled(true);

                timer.cancel();
                timer = new CountDownTimer(startTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        updateTimeRemaining(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        finishTimer("Count down complete");
                    }
                }.start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPaused = !isPaused;
                if (isPaused) {
                    pause.setText("Resume");
                    timer.cancel();
                    countDownText.setText("Count down paused");
                } else {
                    pause.setText("Pause");
                    timer = new CountDownTimer(totalSecondsLeft * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            updateTimeRemaining(millisUntilFinished);
                        }

                        @Override
                        public void onFinish() {
                            finishTimer("Count down complete");
                        }
                    }.start();
                }

            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                endTimer("Count down cancelled");
            }
        });

    }

    private void setEnableEdt(boolean enableEdt) {
        hoursEditText.setEnabled(enableEdt);
        minutesEditText.setEnabled(enableEdt);
        secondsEditText.setEnabled(enableEdt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_down_timer);

        intent = new Intent(this, DialogActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        setupEditTexts();

        countDownText = findViewById(R.id.countDownText);
        hoursLeftText = findViewById(R.id.hoursLeftText);
        minutesLeftText = findViewById(R.id.minutesLeftText);
        secondsLeftText = findViewById(R.id.secondsLeftText);

        setupButtons();
    }
}