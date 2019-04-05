package com.tecno.udemy.fernando.sqliterealmdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecno.udemy.fernando.sqliterealmdemo.R;
import com.tecno.udemy.fernando.sqliterealmdemo.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private List<Note> listNotes;
    private Context context;
    private int layout;

    public NoteAdapter(List<Note> listNotes, Context context, int layout) {
        this.listNotes = listNotes;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return listNotes.size();
    }

    @Override
    public Note getItem(int position) {
        return listNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listNotes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if(convertView == null){

            convertView = LayoutInflater.from(context).inflate(layout, null);

            holder = new ViewHolder();
            holder.textViewDescription = convertView.findViewById(R.id.textViewNoteDescription);
            holder.textViewDate = convertView.findViewById(R.id.textViewNoteCreatedAt);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        Note currentNote = listNotes.get(position);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatDate = df.format(currentNote.getCreatedAt());

        holder.textViewDescription.setText(currentNote.getDescription());
        holder.textViewDate.setText(formatDate);

        return convertView;
    }

    private class ViewHolder{
        TextView textViewDescription;
        TextView textViewDate;
    }

}
