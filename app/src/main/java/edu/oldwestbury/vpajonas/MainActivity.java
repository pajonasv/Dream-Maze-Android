package edu.oldwestbury.vpajonas;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;



@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class MainActivity extends AppCompatActivity {
    protected GameEnvironment gameEnvironment;
    protected CustomView customView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //creating customView and gameEnvironment
        gameEnvironment = new GameEnvironment(this);
        customView = new CustomView(this,gameEnvironment);
        setContentView(customView);
        gameEnvironment.setCustomView(customView);

    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        customView.setFlags();

        gameEnvironment.getDungeon().getSoundBSHandler().unPause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameEnvironment.getDungeon().getSoundBSHandler().pauseMusic();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


}

