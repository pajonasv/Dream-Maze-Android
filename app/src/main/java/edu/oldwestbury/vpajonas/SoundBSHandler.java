package edu.oldwestbury.vpajonas;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.util.Vector;

public class SoundBSHandler {
    private SoundPool soundPool;
    private MediaPlayer mediaPlayer;
    private Context context;
    private boolean paused;
    private final int MAX_STREAMS = 20;
    private Vector<SoundEffect> soundEffects;

    public SoundBSHandler(Context passedContext, int music){
        context = passedContext;
        createSoundPool();

        mediaPlayer = MediaPlayer.create(context,music);
        paused = false;


        AssetManager assetManager = context.getAssets();
        AssetFileDescriptor descriptor;

        soundEffects = new Vector<SoundEffect>();

    }
    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }

    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        soundPool = new SoundPool(MAX_STREAMS,AudioManager.STREAM_MUSIC,0);
    }

    public int playSound(int resource,int loop){
       int id = -1;
        for(int i = 0; i < soundEffects.size();i++){
            if(resource == soundEffects.elementAt(i).resource){
                id = soundEffects.elementAt(i).id;
                break;
            }
        }
        if(id == -1){
            return 0;
        }


        return soundPool.play(id,1.0f,1.0f,1, loop,1.0f);

    }

    public int loadSound(int resource){

        for(int i = 0; i < soundEffects.size();i++){
            if(resource == soundEffects.elementAt(i).resource){
                return soundEffects.elementAt(i).id;
            }
        }

        soundEffects.add(new SoundEffect(soundPool.load(context, resource,1),resource));

        return soundEffects.elementAt(soundEffects.size()-1).id;
    }

    public boolean isPlayingMusic(){
        return mediaPlayer.isPlaying();
    }
    public void playMusic(boolean looping){
        mediaPlayer.setLooping(looping);
        mediaPlayer.start();


    }

    public void pauseMusic(){
        mediaPlayer.pause();
        paused = true;
    }
    public boolean isPaused(){
        return paused;
    }
    public void unPause(){
        paused = false;
    }


    private class SoundEffect{
        public int resource;
        public int id;
        public SoundEffect(int idPassed, int resourcePassed){
            id = idPassed;
            resource = resourcePassed;

        }
    }





}
