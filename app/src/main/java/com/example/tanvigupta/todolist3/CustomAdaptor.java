package com.example.tanvigupta.todolist3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdaptor extends ArrayAdapter {

    ArrayList<Note> items;
    LayoutInflater inflater;
    int inflateCount=0;




    public CustomAdaptor(@NonNull Context context, ArrayList<Note> items ) {
        super(context, 0, items);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items=items;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output=convertView;
        if(output==null){
            inflateCount++;
            Log.d("CustomAdaptor","inflatecount="+inflateCount);
            output=inflater.inflate(R.layout.todo_row_layout,parent,false);
            ImageView img = output.findViewById(R.id.image);
            TextView titleview = output.findViewById(R.id.textview1);
            TextView descriptionview = output.findViewById(R.id.textview2);
            TextView dateview= output.findViewById(R.id.textview3);
            TextView timeview = output.findViewById(R.id.textview4);
            NoteViewHolder viewHolder=new NoteViewHolder();
            viewHolder.title=titleview;
            viewHolder.description=descriptionview;
            viewHolder.image=img;
            viewHolder.date=dateview;
            viewHolder.time=timeview;
            output.setTag(viewHolder);

        }

        NoteViewHolder viewHolder=(NoteViewHolder)output.getTag();
        Note note=items.get(position);
        viewHolder.image.setImageResource(R.drawable.notepapericon);
        viewHolder.title.setText(note.getTitle());
        viewHolder.description.setText(note.getDescription());
        viewHolder.date.setText(note.getDate());
        viewHolder.time.setText(note.getTime());
        return output;



    }


}
