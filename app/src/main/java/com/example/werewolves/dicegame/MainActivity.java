package com.example.werewolves.dicegame;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.button;
import static android.R.attr.color;
import static android.R.attr.drawable;
import static com.example.werewolves.dicegame.RollingActivity.dicevalue;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button p1less,p1great,p2less,p2great,rolldice;
    TextView scoreOne,scoreTwo;
    static int lessPlayer,score1,score2;
    private int dicevalue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Buttons
        rolldice = findViewById(R.id.buttonRollDice);
        rolldice.setOnClickListener(this);
        p1less = findViewById((R.id.buttonPOneLess));
        p1less.setOnClickListener(this);
        p1great = (Button) findViewById(R.id.buttonPOneGreat);
        p1great.setOnClickListener(this);
        p2less = (Button) findViewById(R.id.buttonPTwoLess);
        p2less.setOnClickListener(this);
        p2great = (Button) findViewById(R.id.buttonPtwogreat);
        p2great.setOnClickListener(this);

        //TextFields
        scoreOne = findViewById(R.id.scoreOne);
        scoreTwo = findViewById(R.id.scoreTwo);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonRollDice:
//                Toast.makeText(getApplicationContext(),Integer.toString(lessPlayer),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this,RollingActivity.class);
                startActivityForResult(intent,1);
                break;

            case R.id.buttonPOneGreat:
                p2less.setBackgroundColor(Color.rgb(255,87,51));
                p2great.setBackgroundColor(Color.rgb(255,193,0));
                p1great.setBackgroundColor(Color.rgb(255,87,51));
                p1less.setBackgroundColor(Color.rgb(255,193,0));
                lessPlayer = 2;
                break;

            case R.id.buttonPOneLess:
                p2less.setBackgroundColor(Color.rgb(255,193,0));
                p2great.setBackgroundColor(Color.rgb(255,87,51));
                p1great.setBackgroundColor(Color.rgb(255,193,0));
                p1less.setBackgroundColor(Color.rgb(255,87,51));
                lessPlayer = 1;
                break;

            case R.id.buttonPtwogreat:
                p2less.setBackgroundColor(Color.rgb(255,193,0));
                p2great.setBackgroundColor(Color.rgb(255,87,51));
                p1great.setBackgroundColor(Color.rgb(255,193,0));
                p1less.setBackgroundColor(Color.rgb(255,87,51));
                lessPlayer = 1;
                break;

            case R.id.buttonPTwoLess:
                p2less.setBackgroundColor(Color.rgb(255,87,51));
                p2great.setBackgroundColor(Color.rgb(255,193,0));
                p1great.setBackgroundColor(Color.rgb(255,87,51));
                p1less.setBackgroundColor(Color.rgb(255,193,0));
                lessPlayer = 2;
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            dicevalue = data.getIntExtra("DICEVALUE",0);
            //Toast.makeText(this,Integer.toString(dicevalue),Toast.LENGTH_SHORT).show();
            checkWinner();
        }
    }

    private void checkWinner()
    {
        if (dicevalue <= 3)
        {
            switch (lessPlayer){
                case 1 : Toast.makeText(this,"Player 1 wins",Toast.LENGTH_SHORT).show();
                    score1 = score1+1;
                    scoreOne.setText(Integer.toString(score1));
                    break;
                case 2: Toast.makeText(this,"Player 2 wins",Toast.LENGTH_SHORT).show();
                    score2 = score2 +1;
                    scoreTwo.setText(Integer.toString(score2));
                    break;
                default:
                    break;
            }

        }
        else
        {
            switch (lessPlayer){
                case 1 : Toast.makeText(this,"Player 2 wins",Toast.LENGTH_SHORT).show();
                    score2 = score2 +1;
                    scoreTwo.setText(Integer.toString(score2));
                    break;
                case 2: Toast.makeText(this,"Player 1 wins",Toast.LENGTH_SHORT).show();
                    score1 = score1+1;
                    scoreOne.setText(Integer.toString(score1));
                    break;
                default:
                    break;
            }
        }
    }
}

