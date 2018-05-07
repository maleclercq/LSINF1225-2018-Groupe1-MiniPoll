package com.example.matthieu.minipoll.New_poll;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.matthieu.minipoll.R;
import com.example.matthieu.minipoll.Utilisateur;

public class PopUpReponseFinaleActivity extends AppCompatActivity {

    String repFinale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_reponse_finale);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7), (int)(height*.3));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        Intent i = getIntent();
        this.repFinale = (String) i.getSerializableExtra("str");

        TextView tv=findViewById(R.id.reponse);
        tv.setText(repFinale);
    }

    public void Retour(View v){
        finish();
    }
}
