import com.mpatric.mp3agic.*;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class Music extends JPanel{

    private int rating;
    private String title = "";
    private Lyrics lyrics;
    private String artist = "";
    private String album = "";
    private String path = "";
    private long time;
    private String genre = "";
    private byte[] albumImageData;
    private boolean recentlyPlayed;
    private int numberOfPlays;
    private ArrayList<Star>starsButtons;
    private File f;
    public int offset = 0;
    private long msTime;


    public String getTitle() {
        return title;
    }

    public byte[] getAlbumImageData() {
        return albumImageData;
    }

    public boolean isRecentlyPlayed() {
        return recentlyPlayed;
    }

    public int getNumberOfPlays() {
        return numberOfPlays;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTime() {
        long min = time / 60;
        long sec = time % 60;
        if(sec < 10)
            return min + ":0" + sec;
        return min + ":" + sec;
    }

    public void setRecentlyPlayed(boolean recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public void setNumberOfPlays(int numberOfPlays) {
        this.numberOfPlays = numberOfPlays;
    }


    public JLabel getImage() throws InvalidDataException, IOException, UnsupportedTagException {
        if(!(GUI.nowPlaying == null))
        {
            String imagePath = GUI.nowPlaying.getPath();
            Mp3File mp3file = new Mp3File(imagePath);
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            albumImageData = id3v2Tag.getAlbumImage();
            JLabel jLabel = new JLabel();
            ImageIcon imageIcon;
            if (albumImageData != null){
                Image image = ImageIO.read(new ByteArrayInputStream(albumImageData));
                image = image.getScaledInstance(300, 190, Image.SCALE_SMOOTH);
                imageIcon = new ImageIcon(image);
                jLabel.setIcon(imageIcon);
            }
            return jLabel;
        }
        return null;
    }

    public JLabel getDefaultImage(){
        JLabel jLabel = new JLabel();
        ImageIcon imageIcon;

        File imgPath = new File("./default.png");

        try {
            this.albumImageData = Files.readAllBytes(imgPath.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (albumImageData != null){
            Image image = null;
            try {
                image = ImageIO.read(new ByteArrayInputStream(albumImageData));
            } catch (IOException e) {
                e.printStackTrace();
            }
            image = image.getScaledInstance(300, 190, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(image);
            jLabel.setIcon(imageIcon);
        }
        return jLabel;
    }


    public Music(String dir) throws InvalidDataException, IOException, UnsupportedTagException ,ReadOnlyFileException,
            IOException, LineUnavailableException, CannotReadException, TagException, InvalidAudioFrameException {


        this.path = dir;
        extractMetaData(dir);
        makeMusicPanel();
    }

    private void extractMetaData(String dir) throws InvalidDataException, UnsupportedTagException, IOException, ReadOnlyFileException,
            IOException, LineUnavailableException, CannotReadException, TagException, InvalidAudioFrameException {
        this.path = dir;

        if (path.endsWith(".mp3")) {
            Mp3File mp3file = new Mp3File(dir);

            time = mp3file.getLengthInSeconds();
            msTime = mp3file.getLengthInMilliseconds();

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

                if (this.artist.equals(""))
                    this.artist = id3v2Tag.getArtist();
                if (this.title.equals(""))
                    this.title = id3v2Tag.getTitle();
                if (this.album.equals(""))
                    this.album = id3v2Tag.getAlbum();
                if (this.genre.equals("") || this.genre.toLowerCase().equals("unknown"))
                    this.genre = id3v2Tag.getGenreDescription();


                albumImageData = id3v2Tag.getAlbumImage();
            }
        }
        else{
            this.f = new File(dir);
            AudioFile audioFile = AudioFileIO.read(new File(dir));
            this.artist = audioFile.getTag().getFirst(FieldKey.ARTIST);
            this.title = audioFile.getTag().getFirst(FieldKey.TITLE);
            this.album = audioFile.getTag().getFirst(FieldKey.ALBUM);
            this.genre = audioFile.getTag().getFirst(FieldKey.GENRE);

            Tag tag = audioFile.getTag();
            this.time = audioFile.getAudioHeader().getTrackLength() ;

            if (this.artist.equals("")){
                String[] s = this.path.split("/");
                String x = s[s.length - 1];
                this.artist = x.substring(0, x.length()-4);
            }
            if (this.title.equals("")){
                String[] s = this.path.split("/");
                String x = s[s.length - 1];
                this.title = x.substring(0, x.length()-4);
            }
            if (this.genre.equals("")){
             setGenre("unknown");
            }
        }

    }


    private void makeMusicPanel(){
        this.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GUI.getMode2().equals("play")){
                    GUI.p.pause();
                    GUI.nowPlaying = Music.this;
                    GUI.p = new Play(0, Music.this);
                    GUI.p.start();
                }
                else {
                    GUI.nowPlaying = Music.this;
                    GUI.p = new Play(0, Music.this);
                    GUI.p.start();
                    GUI.makePlay();
                }

                recentlyPlayed = true;
                Border border = BorderFactory.createLineBorder(Color.BLUE, 1);
                for (Music m:GUI.songs) {
                    m.setBorder(null);
                }
                Music.this.setBorder(border);
            }
        });
        this.setLayout(new FlowLayout());
        this.setMinimumSize(new Dimension(700, 40));
        this.setMaximumSize(new Dimension(700, 40));

        JLabel[] labels = new JLabel[4];

        for (int i = 0; i < 4; i++) {
            labels[i] = new JLabel();
            labels[i].setPreferredSize(new Dimension(100, 40));
            labels[i].setHorizontalAlignment(SwingConstants.CENTER);
        }

        labels[0].setText(getTitle());
        labels[1].setText(getTime());
        labels[2].setText(getArtist());
        labels[3].setText(getGenre());


        labels[3].addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // comment the below if statement
                String[] unknowns = {"unknown", "other"};
                if (Arrays.asList(unknowns).contains(getGenre().toLowerCase())) {
                    GenreClassification g = new GenreClassification(getPath(),genre, labels[3]);
                    g.start();
                }
            }
        });
        JPanel hold = new JPanel();
        hold.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hold.setPreferredSize(new Dimension(400, 40));
        for (int j = 0; j < 4; j++) {
            hold.add(labels[j]);
        }
        JPanel stars = new JPanel();
        stars.setPreferredSize(new Dimension(300, 40));
        stars.setLayout(new FlowLayout(FlowLayout.CENTER));
        stars.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 50));

        starsButtons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Star button = new Star();
            starsButtons.add(button);
            stars.add(button);
        }
        this.add(hold);
        this.add(stars);
    }
    public void setStarsButtons(){
        for (int i = 0; i < rating; i++) {
            starsButtons.get(i).setMode(2);
        }
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public void setRating(){
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if(starsButtons.get(i).getMode() == 2){
                count++;
            }
        }
        setRating(count);
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

    public int getRating() {
        return rating;
    }

    public Lyrics getLyrics() {
        return lyrics;
    }

    public String getArtist() {
        return artist;
    }

    public File getFile() {
        return f;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public long getMsTime(){
        return this.msTime;
    }
    public static void saveMusics(ArrayList<Music> musics, ArrayList<Playlist>playlists){
        ArrayList<MusicInfo>musicInfos1 = new ArrayList<>();

        ArrayList<ArrayList<MusicInfo>> playlistInfos = new ArrayList<>();

        for (Music d:musics) {
            d.setRating();
            MusicInfo musicInfo = new MusicInfo(d);
            musicInfos1.add(musicInfo);
        }
        for (Playlist p:playlists) {
            ArrayList<MusicInfo>musicInfos2 = new ArrayList<>();
            for (Music m:p.getMusics()) {
                m.setRating();
                MusicInfo musicInfo = new MusicInfo(m);
                musicInfos2.add(musicInfo);
            }
            playlistInfos.add(musicInfos2);
        }

        try{
            FileOutputStream fileOut = new FileOutputStream("songs.info");
            ObjectOutputStream oos = new ObjectOutputStream (fileOut);
            oos.writeObject(musicInfos1);
            oos.close();
            fileOut.close();

        }catch(Exception e){
            System.err.println(e.getMessage());
        }

        int k = 1;
        for (ArrayList a:playlistInfos) {
            try{
                FileOutputStream fileOut = new FileOutputStream("./playLists/playlist" + k + ".info");
                ObjectOutputStream oos = new ObjectOutputStream (fileOut);
                oos.writeObject(a);
                oos.close();
                fileOut.close();

            }catch(Exception e){
                System.err.println(e.getMessage());
            }
            k++;
        }
    }
}