package com.example.tanvigupta.todolist3;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    int inflateCount = 0;
    TODOitemClickListenener listener;

    public CustomAdaptor(@NonNull Context context, ArrayList<Note> items, LayoutInflater inflater,TODOitemClickListenener listenener) {
        super(context, 0, items);
        this.inflater = inflater;
        this.items = items;
        this.listener=listenener;
    }

    @NonNull @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output = convertView;
        if (output == null) {
            output = inflater.inflate(R.layout.todo_row_layout, parent, false);
        }
        ImageView img = output.findViewById(R.id.image);
        TextView titleview = output.findViewById(R.id.textview1);
        TextView descriptionview = output.findViewById(R.id.textview2);
        TextView dateview = output.findViewById(R.id.textview3);
        TextView timeview = output.findViewById(R.id.textview4);
        ImageView imageView=output.findViewById(R.id.deletetodo);


        final Note note = items.get(position);
        img.setImageResource(R.drawable.pencil);
        titleview.setText(note.getTitle());
        descriptionview.setText(note.getDescription());
        dateview.setText(note.getDate());
        timeview.setText(note.getTime());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.rowImageClicked(v,position,note.getId());
            }
        });








        return output;
    }
}
