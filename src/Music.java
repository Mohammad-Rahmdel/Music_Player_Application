import com.mpatric.mp3agic.*;

import java.io.*;

public class Music {
    private int rating;
    private String title = "";
    private Lyrics lyrics;
    private String artist = "";
    private String album = "";
    private String path;
    private String genre = "";
    private long time;
    private byte[] albumImageData;

    public Music(String dir) throws IOException, InvalidDataException, UnsupportedTagException {

        Mp3File mp3file = new Mp3File("./Bird.mp3");

        time = mp3file.getLengthInSeconds();

        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            this.artist = id3v1Tag.getArtist();
            this.title = id3v1Tag.getTitle();
            this.album = id3v1Tag.getAlbum();
            this.genre = id3v1Tag.getGenreDescription();
//            System.out.println(id3v1Tag.getGenreDescription());
        }


        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();

            if (this.artist == "")
                this.artist = id3v2Tag.getArtist();
            if (this.title == "")
                this.title = id3v2Tag.getTitle();
            if (this.album == "")
                this.album = id3v2Tag.getAlbum();
            if (this.genre == "" || this.genre.toLowerCase()=="unknown")
                this.genre = id3v2Tag.getGenreDescription();

//            System.out.println(id3v2Tag.getGenreDescription());

            albumImageData = id3v2Tag.getAlbumImage();

        }



//        if (mp3file.hasId3v2Tag()) {
//            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
//            byte[] imageData = id3v2Tag.getAlbumImage();
//            if (imageData != null) {
//                String mimeType = id3v2Tag.getAlbumImageMimeType();
//                // Write image to file - can determine appropriate file extension from the mime type
//                RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
//                file.write(data);
//                file.close();
//            }
//        }


        System.out.println("Title = " + title);
        System.out.println("Album = " + album);
        System.out.println("Time = " + time);
        System.out.println("Artist = " + artist);
        System.out.println("Genre = " + genre);

    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public void setLyrics(Lyrics lyrics) {
        this.lyrics = lyrics;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public Lyrics getLyrics() {
        return lyrics;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public String getGenre(){
        return genre;
    }

    public long getTime(){
        return time;
    }
}
