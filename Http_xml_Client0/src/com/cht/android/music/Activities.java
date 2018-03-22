package com.cht.android.music;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Activities extends ListActivity {

    private String[] mStrings = {
            "�A���R�W<<�p�t>>�����L���q<<�N���b�o��>>�ƨ��Ǳ��p�Ѩ� �x�D�i��", 
            "Supermarket �W��4th ���C����t�۷|�A�ܧA���t�q�I",
            "2009�����jĹ�a�A�������B��孵�B�j�L�ڡB����P���A�j��eiPod Touch", 
            "�y�Q�a�B�u�����ѤU�A�|�nť�x�y�D�D���I�U���e�W�a���O���q�ֳU�I�z", 
            "���K�S�i�f�f �g�g�e�A�d��§�� ���K�S�D�D����J�����m���e",
            "������F<<�P��>>�ܧA�h�¨q�ݦۤv�R�ݪ��q�v�I",
            "���Ӧ� �h�X�Ӫ��ۥ� �e�ASony Walkman�B��KTV�I"};
    
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
