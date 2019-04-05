package com.tecno.udemy.fernando.sqliterealmdemo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecno.udemy.fernando.sqliterealmdemo.R;
import com.tecno.udemy.fernando.sqliterealmdemo.model.Board;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoardAdapter extends BaseAdapter {

    Context context;
    List<Board> boardList;
    int layout;

    public BoardAdapter(Context context, List<Board> boardList, int layout) {
        this.context = context;
        this.boardList = boardList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return boardList.size();
    }

    @Override
    public Board getItem(int position) {
        return boardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return boardList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){

            convertView = LayoutInflater.from(context).inflate(layout, null);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.textViewBoardTitle);
            holder.notes = convertView.findViewById(R.id.textViewBoardNotes);
            holder.createAt = convertView.findViewById(R.id.textViewBoardDate);
            convertView.setTag(holder);

        }else
            holder = (ViewHolder) convertView.getTag();

        Board currentBoard = boardList.get(position);

        int numNotes = currentBoard.getNotes().size();
        String textForNotes = (numNotes == 1) ? numNotes + " Note" : numNotes + " Notes";

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String createAt = df.format(currentBoard.getCreatedAt());

        holder.title.setText(currentBoard.getTitle());
        holder.createAt.setText(createAt);
        holder.notes.setText(textForNotes);

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView notes;
        TextView createAt;
    }
}
