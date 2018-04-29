package edu.duke.compsci290.fpx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Philipk on 29/04/18.
 */

public class Starter_Activity  extends AppCompatActivity{
    Button btnSignUp;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //final Context mContext=this.getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);



        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnSignIn = (Button) findViewById(R.id.btnSingIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                setBtnSignIn();

            }

        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                setBtnSignUp();

            }

        });




    }

    private void setBtnSignIn(){
        Intent mIntent = new Intent(Starter_Activity.this, SignInActivity.class);
        startActivity(mIntent);

    }
    private void  setBtnSignUp(){
        Intent mIntent = new Intent(Starter_Activity.this, SignUpActivity.class);
        startActivity(mIntent);

    }



}
