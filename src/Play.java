import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class Play extends Thread {


    private int offset;
    private AdvancedPlayer p;
    public Music music;

    private Clip clip;
    private int lastFrame;
    private Long currentFrame;
    String path;
    private final int BUFFER_SIZE = 128000;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;

    public Play(int offset, Music music){
        this.music = music;
        this.offset = offset;
    }


//    public Play(Music m){
//        this.music = m;
//    }


    public void run() {

//            try {
//
//                Mp3File temp1 = new Mp3File(music.getPath());
//                int sampleRate = temp1.getFrameCount();
//                long time = temp1.getLengthInSeconds();
//
//
//                FileInputStream file = new FileInputStream(music.getPath());
//                p = new AdvancedPlayer(file);
//                if(offset == 0)
//                    p.play();
//                else
//                    p.play((int) (offset*sampleRate/(time*1000)), 130000);
//
//            } catch (Exception e) {
//                System.out.println(e);
//            }
        try {
            this.path = music.getPath();
            if (path.contains(".mp3")) {

                Mp3File temp1 = new Mp3File(path);
                int sampleRate = temp1.getFrameCount();
                long time = temp1.getLengthInSeconds();

                FileInputStream file = new FileInputStream(music.getPath());
                p = new AdvancedPlayer(file);
                if (offset == 0)
                    p.play();
                else
                    p.play((int) (offset * sampleRate / (time * 1000)), 130000);
            } else if (path.contains(".wav")) {
                loadClip();
                if (offset ==0) {
                    try {
                        clip.start();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {

                    if (offset < clip.getFrameLength()) {
                        clip.setFramePosition(offset);
                    } else {
                        clip.setFramePosition(0);
                    }
                    clip.start();
                }

            }


        } catch (Exception e) {
            System.out.println(e);
        }



    }



    public void loadClip() {
        try {
            audioStream = AudioSystem.getAudioInputStream(music.getFile());
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
        try {
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

    }




    public int pause() {
//        int last = p.getPosition();
//        p.close();
//        return last + offset;

        int output = 0 ;
        if (path.contains(".mp3")) {
            int last = p.getPosition();
            p.close();
            output =  last + offset;

        } else
        if (path.contains(".wav")) {
            lastFrame = clip.getFramePosition();
            clip.stop();
            output= lastFrame;
        }
        return output;
    }

    public boolean getComplete(){
        return p.getComplete();
    }

    public int getPosition(){
        return p.getPosition();
    }

}