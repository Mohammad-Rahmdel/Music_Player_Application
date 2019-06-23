import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.FileInputStream;
import java.util.Scanner;

public class Play extends Thread {


    private int offset;
    private volatile boolean flag = true;

    public void stopRunning()
    {
        flag = false;
    }

    public Play(int offset){
        this.offset = offset;
    }

    public void run() {
      //  while (flag) {
            try {

//                for(int i = 0; i < 10000; i++ ) {
                    FileInputStream file = new FileInputStream("./Preach.mp3");
                    Player2 p = new Player2(file);
                    p.play(1000);
                    offset += 2;
//                }
                System.out.println("offset = " + offset);
//                Player playMP3 = new Player(file);
//                playMP3.play(10000);
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println("Stopped Running....");
        //}
    }

    public int getOffset(){
        return offset;
    }
}