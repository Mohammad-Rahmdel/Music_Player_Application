import com.mpatric.mp3agic.Mp3File;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

public class Play extends Thread {


    private int offset;
    private AdvancedPlayer p;


    public Play(){
        this.offset = 0;
    }

    public Play(int offset){
        this.offset = offset;
    }

    public void run() {

            try {

                Mp3File temp1 = new Mp3File("./Preach.mp3");
                int sampleRate = temp1.getFrameCount();
                long time = temp1.getLengthInSeconds();



                FileInputStream file = new FileInputStream("./Preach.mp3");
                p = new AdvancedPlayer(file);
                if(offset == 0)
                    p.play();
                else
                    p.play((int) (offset*sampleRate/(time*1000)), 130000);

            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Stopped Running....");

    }

    public int pause() {
        int last = p.getPosition();
        p.close();
        return last + offset;
    }

}