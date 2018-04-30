package edu.duke.compsci290.fpx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Philipk on 29/04/18.
 */

public class NetId extends AppCompatActivity {
    Button idBtn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        idBtn=(Button) findViewById(R.id.btnId);

    }


}
