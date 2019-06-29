import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLException;


public class TopSongs extends Thread{

    private String response = "";

    public TopSongs(){
    }

    public String getResponse(){
        return this.response;
    }
    public void run(){
        try {
            Document doc = Jsoup.connect("https://www.billboard.com/charts/hot-100").userAgent("Mozilla/67.0.4").get();
            Elements music = doc.select("div.chart-list-item__title");
            Elements singer = doc.select("div.chart-list-item__artist");


            for(int i = 0; i < 10; i++){

                String result = (i+1) + ") Title: " + music.get(i).getElementsByTag("span").first().text() + "\n" ;
                for (int j = 3; j > i/10; j--)
                    result += " ";
                result += "Artist: " + singer.get(i).getAllElements().first().text();

                response += result + "\n \n";
            }

        } catch (UnknownHostException e) {
            response = "NO INTERNET ACCESS";
        } catch (SocketTimeoutException | SSLException e){
            response = "Please activate your VPN";
        } catch (IOException e){
//            System.out.println(e);
        }

//        System.out.println("res = " + response );
    }

}