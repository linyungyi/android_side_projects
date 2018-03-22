package com.cht.android.music;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Activities extends ListActivity {

    private String[] mStrings = {
            "再次愛上<<小宇>>的盛夏情歌<<就站在這裡>>化身傳情小天使 徵求告白", 
            "Supermarket 超級4th 場。曹格飆唱會，邀你來飆歌！",
            "2009金曲大贏家，陳奕迅、梁文音、大嘴巴、神木與瞳，大方送iPod Touch", 
            "『娘家、真情滿天下，尚好聽台語主題曲！下載送獨家豪記限量福袋！』", 
            "阿密特張惠妹 週週送你千元禮券 阿密特主題派對入場資格搶先送",
            "暑假到了<<周蕙>>邀你去威秀看自己愛看的電影！",
            "言承旭 多出來的自由 送你Sony Walkman、唱KTV！"};
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use an existing ListAdapter that will map an array
        // of strings to TextViews
        
        // TODO: mStrings is from database!
        
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mStrings));
        getListView().setTextFilterEnabled(true);
}
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {   
        Intent aIntent = new Intent(this, ActivityDetails.class);
        aIntent.putExtra("position", position);
        this.startActivity(aIntent);
    }
}
