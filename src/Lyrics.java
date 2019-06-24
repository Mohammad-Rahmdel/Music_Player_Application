import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Lyrics {
    private int fontSize;
    private String fontName;

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }


    public static String readFileAsString(String fileName)throws Exception
    {
        String data = "";
        data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }



    public static String lyricCleaner(String path){
        String lyric = "";

        try {
            FileInputStream fstream = new FileInputStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

            while ((strLine = br.readLine()) != null)   {
                if (strLine.length() > 0) {
                    if (strLine.substring(0, 1).equals("[")) ;
                    {
                        int endIndex = strLine.indexOf("]");
                        strLine = strLine.substring(endIndex + 1, strLine.length());
                        lyric += (strLine + '\n');
                    }
                }

            }

            fstream.close();

        }
        catch (Exception e) {
            System.out.println("execption = " + e);
        }
        return lyric.trim();
    }

    public static void main(String[] args){
        String path = "./lyrics/Fall in Lie.lrc";
        lyricCleaner(path);

    }
}

