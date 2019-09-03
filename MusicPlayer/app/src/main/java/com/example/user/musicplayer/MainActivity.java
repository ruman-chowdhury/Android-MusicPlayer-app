package com.example.user.musicplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView Lv;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Lv= (ListView) findViewById(R.id.lvPlaylist);

        final ArrayList<File> mySongs=findSongs(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];
        //loop for reading all name from arraylist "mysongs"
        for (int i=0;i<mySongs.size();i++){
            toast(mySongs.get(i).getName());

            items[i]=mySongs.get(i).getName().replace(".mp3","").replace(".wav",""); //add name into items and replace extension with null
        }

        //adapter for showing custom listview
        ArrayAdapter<String> adp = new ArrayAdapter<>(getApplicationContext(), R.layout.songs_layout, R.id.textView, items);
        Lv.setAdapter(adp);

        //by clicking in the songs name which activity will open
        Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("Pos",i).putExtra("songList",mySongs));
            }
        });

    }

    //create arraylist with datatype "File"
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<>(); //creating object 'al'

        File[] files = root.listFiles(); //store every file(doc,pdf,mp3 etc) in "files" from root directory/sdcard

        //foreach loop for store every single file into "singleFile"
        for(File singleFile:files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){

                al.addAll(findSongs(singleFile)); //recursive findsongs() method,get another many arraylist from sub folder/directory
            }
            else{
                if (singleFile.getName().endsWith(".mp3")){
                    al.add(singleFile);
                }
            }
        }

        return al;
    }

    //method for showing songs name as toast while adding into list
    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
