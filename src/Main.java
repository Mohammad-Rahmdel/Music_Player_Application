import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

public class Main {

    public static void sleep(int time){
        try {
            Thread thread = new Thread();
            thread.sleep(time);
            System.exit(0);
        } catch (Exception e) {}
    }
    public static void main(String[] args) throws InterruptedException, InvalidDataException, IOException, UnsupportedTagException {

        GUI gui = new GUI();
        gui.show();

    }
}
