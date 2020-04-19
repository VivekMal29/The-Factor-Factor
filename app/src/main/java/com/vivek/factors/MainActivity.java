package com.vivek.factors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
   //Declaring Various things required.
    Vibrator vibrator;
    Button go;
    Button option1 ;
    Button option2 ;
    Button option3 ;
    EditText editText ;
    TextView msg;
    TextView wrong;
    TextView textScore;
    TextView textHigh;
    TextView countTime;
    TextView Number;
    CountDownTimer countDownTimer;



    View viewB;
                                                          //ArrayList which contain options.
    ArrayList optionArray = new ArrayList();
                                                            //Arraylist which contain factors of particular number.
    ArrayList<Integer> factor = new ArrayList<>();

    // location variable which tells about tag(option) of correct answer.
    public int location;
    public int score=0;
    int highScore;
    int counter = 10;
    boolean timerRunning=false;
    public int number;
    //function for animation
    public static Animation blinkAnim() {

        // Configure your animation properties here

        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(10);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }


    //Function that random number
    public int getRandomNum(int num){
        Random random = new Random();
        return random.nextInt(num);
    }

    //Function That checks if a number is factor of particular number or  not
    public boolean ifFactor(int n){
        boolean b=false;
        int j;
        j = factor.size();
        for(int i=0;i<j;i++){
            if(n ==factor.get(i)){
                b=true;
            }
        }
        return b;
    }

    //funtion ThAT RETURNS random numbers less a particular number
    public int RandomOption1(int number){
        int ran = getRandomNum(number);
        while (ifFactor(ran)||ran==0){
            ran=getRandomNum(number);
        }
        return ran;
    }

    //Countdown timer which starts when we press the Go button
    public void startTimer(){
        Log.d("timerAfterstarttime",Integer.toString(counter));
        countDownTimer = new CountDownTimer(counter*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countTime.setText(String.valueOf(counter));
                countTime.startAnimation(blinkAnim());
               counter--;
            }

            @Override
            public void onFinish() {
                timerRunning=false;
                countTime.setText("Game Over");
                countTime.clearAnimation();
                Number.setText("");
                score=0;
                editText.setEnabled(true);
                viewB.setBackgroundColor(getResources().getColor(R.color.Red));
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(200);
                }
                counter=10;
                timerRunning=false;
                option1.setEnabled(false);
                option2.setEnabled(false);
                option3.setEnabled(false);
                wrong.setText("Correct Answer is " + optionArray.get(location));
                msg.setText("!!WRONG :(!!");
                score=0;

            }
        }.start();
        timerRunning = true;
    }
    // Stopping the timer
    public void stopTimer(){
        countDownTimer.cancel();
        timerRunning=false;
    }
    // It is called when any option is clicked
    public  void optionClicked(View view){
        editText.setEnabled(true);
                                                             //If answer is correct
        if(view.getTag().equals(Integer.toString(location))){
            msg.setText("!!CORRECT : )!!");
            countTime.setText("Well Done");
            counter=10;
            score++;
                                       //Updating High score
            if(score>=highScore){
                highScore=score;
                textHigh.setText("HighScore: "+highScore);
            }
            viewB.setBackgroundColor(getResources().getColor(R.color.Green));    //setting background color
            countTime.clearAnimation();
            stopTimer();
        }
                                                              //If answer is wrong
        else{
            msg.setText("!!WRONG :(!!");
            countTime.clearAnimation();
            countTime.setText("Game Over");
            counter=10;
            wrong.setText("Correct Answer is " + optionArray.get(location));
            score=0;
            viewB.setBackgroundColor(getResources().getColor(R.color.Red));
                                                  //making vibration when answer is wrong
            if(Build.VERSION.SDK_INT>=26){
                vibrator.vibrate(VibrationEffect.createOneShot(200,VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else{
                vibrator.vibrate(200);
            }
            stopTimer();
        }
        textScore.setText("score :"+score);                                                   //setting score when answered
        SharedPreferences shrd = getSharedPreferences("highestScore",MODE_PRIVATE);     //saving data
        SharedPreferences.Editor editor = shrd.edit();
        editor.putInt("highscore",highScore);
        editor.commit();
        Number.setText("");
        option1.setEnabled(false);               //Disabling the options
        option2.setEnabled(false);
        option3.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
         option3 = findViewById(R.id.option3);
         go = findViewById(R.id.getFactor);
        editText = findViewById(R.id.editText);
        msg = findViewById(R.id.messageView);
        wrong = findViewById(R.id.ifWrong);
        textScore = findViewById(R.id.score);
        textHigh = findViewById(R.id.highScore);
        constraintLayout = findViewById(R.id.consLayout);
        countTime = findViewById(R.id.countTime);
        Number = findViewById(R.id.number);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        textScore.setText("score :"+ score);
        viewB = this.getWindow().getDecorView();

        editText.addTextChangedListener(textWatcher);        //Used to disable editText when not in use
        if(editText.getText().toString().isEmpty()){
            go.setEnabled(false);
            option1.setEnabled(false);
            option2.setEnabled(false);
            option3.setEnabled(false);
        }
        else{
            go.setEnabled(true);
        }

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setEnabled(false);
                if(!editText.getText().toString().isEmpty()) {

                    option1.setEnabled(true);
                    option2.setEnabled(true);
                    option3.setEnabled(true);
                    viewB.setBackgroundColor(getResources().getColor(R.color.White));

                    startTimer();
                                                     //Providing Random Location to Correct Answer
                    int locationOfCorrectAns = getRandomNum(3);
                    number = Integer.parseInt(editText.getText().toString());    //obtaining number editText
                    location = locationOfCorrectAns;

                    factor.clear();   //emptying ArrayList before adding something

                                             //adding stuffs to factors(ArrayList)
                    if (number >= 3) {
                        for (int i = 1; i <= number; i++) {
                            if (number % i == 0) {
                                factor.add(i);
                            }
                        }
                                                      //Adding Option to Question
                        for (int i = 0; i < 3; i++) {
                            if (i == locationOfCorrectAns) {
                                optionArray.add(i, factor.get(getRandomNum(factor.size())));
                            } else {
                                optionArray.add(i, RandomOption1(number));
                            }
                        }
                                                                                     //Setting text to the option
                        option1.setText(Integer.toString((Integer) optionArray.get(0)));
                        option2.setText(Integer.toString((Integer) optionArray.get(1)));
                        option3.setText(Integer.toString((Integer) optionArray.get(2)));
                        //Clearing message after every Question
                        msg.setText("");
                        wrong.setText("");
                    } else {                                              //Code executed when number is less than 3
                        wrong.setText("Enter number greater than 3");
                        option1.setText("A.");
                        option2.setText("B.");
                        option3.setText("C.");
                        option1.setEnabled(false);
                        option2.setEnabled(false);
                        option3.setEnabled(false);
                        countDownTimer.cancel();
                        timerRunning=false;
                        Number.setText("");
                        msg.setText("");
                        editText.setEnabled(true);
                        countTime.setText("");

                    }

                    Number.setText(editText.getText().toString());
                    editText.getText().clear();
                }
                else{
                    msg.setText("please enter a number");
                }


            }
        });


        SharedPreferences getShared = getSharedPreferences("highestScore",MODE_PRIVATE);  //HighScore to memory of system
        highScore = getShared.getInt("highscore",0);
        textHigh.setText("HighScore: "+highScore);


    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = editText.getText().toString();
            go.setEnabled(!text.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {   //saving data before destroying an activity
        super.onSaveInstanceState(outState);
        outState.putInt("score",score);
        outState.putInt("counter",counter);
        outState.putBoolean("timerRunning",timerRunning);
        outState.putString("number",Number.getText().toString());
        outState.putString("cOut",countTime.getText().toString());
        outState.putString("msg",msg.getText().toString());
        outState.putString("wrong",wrong.getText().toString());
        outState.putString("option1",option1.getText().toString());
        outState.putString("option2",option2.getText().toString());
        outState.putString("option3",option3.getText().toString());
        outState.putIntegerArrayList("optionArray",optionArray);


        if(timerRunning) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {   //REstoring data when screen is rotated or restarted
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getInt("score");
        Number.setText(savedInstanceState.getString("number"));
        counter = savedInstanceState.getInt("counter");
        msg.setText(savedInstanceState.getString("msg"));
        wrong.setText(savedInstanceState.getString("wrong"));
        option1.setText(savedInstanceState.getString("option1"));
        option2.setText(savedInstanceState.getString("option2"));
        option3.setText(savedInstanceState.getString("option3"));
        optionArray = savedInstanceState.getIntegerArrayList("optionArray");
        Log.d("timerAfterOnRestoreBe",Integer.toString(counter));
        timerRunning = savedInstanceState.getBoolean("timerRunning");
        textScore.setText("score :"+score);
        if(msg.getText().toString()=="!!CORRECT : )!!"){
            viewB.setBackgroundColor(getResources().getColor(R.color.Green));
        }
        if(msg.getText().toString()=="!!WRONG :(!!"){
            viewB.setBackgroundColor(getResources().getColor(R.color.Red));
        }

        if(timerRunning){
            Log.d("timerAfterOnRestore",Integer.toString(counter));
           startTimer();
            option1.setEnabled(true);
            option2.setEnabled(true);
            option3.setEnabled(true);
        }
        else{
            countTime.setText(savedInstanceState.getString("cOut"));
        }


    }
}