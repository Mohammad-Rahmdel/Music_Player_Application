import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GenreClassification extends Thread {

    private String path;
    private String genre = "";

    public GenreClassification(String path){
        this.path = path;
    }

    public String getGenre(){
        return this.genre;
    }

    public void run(){
        String cmd = "python3 ";
        cmd += "./GenreClassification/predict_example.py ";
        cmd += path;

        Runtime run = Runtime.getRuntime();

        Process pr = null;
        try {
            pr = run.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            pr.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));

        String line = "";

        while (true) {
            try {
                if (!((line=buf.readLine())!=null)) break;
            } catch (IOException e) {
                System.out.println(e);
            }
            genre = line.substring(15);
        }
    }

    public static void main(String[] args){
        String path = "./Preach.mp3";
        GenreClassification g = new GenreClassification(path);
        g.start();
        while (g.isAlive()){}
        System.out.println(g.getGenre());
    }
}
