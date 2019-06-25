import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;




public class LyricDownloader {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String search_artist = "https://www.lyricsfreak.com/search.php?q=";

    private String title;
    private String artist;

    public LyricDownloader(String artist, String title){
        this.title = title;
        this.artist = artist;
    }

    public String downloader() throws IOException{
        String lyric = "Lyrics downloaded from www.lyricsfreak.com \n \n" ;
        lyric += "Title: " + title + '\n';
        lyric += "Artist: " + artist + "\n \n";

        title = title.toLowerCase();
        artist = artist.toLowerCase();

        artist = artist.replace(" ", "+");
        title = title.replace(" ", "+");

        String search_response;
        search_response = sendGET(search_artist + artist);


        String find = "href=\"/" + artist.substring(0, 1) + '/' + artist + '/' + title;

        int firstIndex = search_response.indexOf(find);
        int lastindex = firstIndex + find.length();


        while(true){
            if (search_response.substring(lastindex, lastindex + 1).equals("\""))
                break;
            else
                lastindex++;
        }

        String request_page = search_response.substring(firstIndex, lastindex);

        request_page = "https://www.lyricsfreak.com" + request_page.substring(6, request_page.length());

        String response_page = sendGET(request_page);

        find = "lyrictxt js-lyrics js-share-text-content";

        firstIndex = response_page.indexOf(find);
        lastindex = firstIndex + find.length();
        while(true){

            if (response_page.substring(lastindex, lastindex + 3).equals("div"))
                break;
            else
                lastindex++;
        }


        String parse = response_page.substring(firstIndex, lastindex);
        firstIndex = parse.indexOf(">");
        parse = parse.substring(firstIndex + 1, parse.length() - 2);
        parse = parse.trim();

        parse = parse.replace("<br />", "");

        parse = parse.replace("&#039;", "'");

        lyric += parse;

        System.out.println(lyric);

        return lyric;

    }

    private String sendGET(String address) throws IOException {

        String res = "";

        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2,TLSv1.3");
        URL obj = new URL(address);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {

//                System.out.println(inputLine);

                response.append(inputLine + '\n');
            }
            in.close();

            // print result
//            System.out.println(response.toString());

            res = response.toString();
        } else {
            System.out.println("GET request not worked");
        }

        return res;
    }


    public static void main(String[] args) throws IOException {

        LyricDownloader lyricDownloader = new LyricDownloader("coldplay", "a sky full of stars");

        String ss = lyricDownloader.downloader();
        System.out.println(ss);
    }

}


// TODO : finding exceptions (no internet access + lyric not found)
// TODO : saving text 

