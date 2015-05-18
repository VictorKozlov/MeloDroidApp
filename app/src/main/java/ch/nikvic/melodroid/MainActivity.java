package ch.nikvic.melodroid;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

    private ArrayList<Song> songList;
    private ListView songView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songView = (ListView)findViewById(R.id.song_list);
        songList = new ArrayList<Song>();
        getSongList();

        //Sortiert die Songs alphabetisch
        Collections.sort(songList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdptr = new SongAdapter(this, songList);
        songView.setAdapter(songAdptr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // fügt Items zur ActionBar hinzu, wenn welche vorhanden sind
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicURI = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicURI, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            //fügt Songs der Liste hinzu
            do {
                long songID = musicCursor.getLong(idColumn);
                String songTitle = musicCursor.getString(titleColumn);
                String songArtist = musicCursor.getString(artistColumn);
                //Neues Song Objekt wird erstellt
                songList.add(new Song(songID, songTitle, songArtist));
            }
            while (musicCursor.moveToNext());
        }
    }
}
