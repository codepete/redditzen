package com.peterung.redditzen.ui.messages;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.peterung.redditzen.R;
import com.peterung.redditzen.data.db.schematic.MessageColumns;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.Instant;
import org.threeten.bp.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagesAdapter extends CursorAdapter {
    public static final String[] PROJECTION = new String[] {
            MessageColumns._ID,
            MessageColumns.CREATED,
            MessageColumns.UNREAD,
            MessageColumns.AUTHOR,
            MessageColumns.SUBJECT,
            MessageColumns.BODY
    };

    public MessagesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        v.setTag(new ViewHolder(v));
        return v;
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = (ViewHolder) view.getTag();
        final String author = cursor.getString(cursor.getColumnIndex(MessageColumns.AUTHOR));
        vh.author.setText(author);

        final String subject = cursor.getString(cursor.getColumnIndex(MessageColumns.SUBJECT));
        vh.subject.setText(subject);

        final String body = cursor.getString(cursor.getColumnIndex(MessageColumns.BODY));
        vh.body.setText(body);

        final String created = cursor.getString(cursor.getColumnIndex(MessageColumns.CREATED));
        DateTimeFormatter parser = DateTimeFormatter.ISO_INSTANT;
        Date date = DateTimeUtils.toDate(Instant.from(parser.parse(created)));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        vh.created.setText(simpleDateFormat.format(date));

        final boolean unread = cursor.getInt(cursor.getColumnIndex(MessageColumns.UNREAD)) == 1;

        vh.author.setTypeface(null, unread ? Typeface.BOLD : Typeface.NORMAL);
        vh.subject.setTypeface(null, unread ? Typeface.BOLD : Typeface.NORMAL);
        vh.body.setTypeface(null, unread ? Typeface.BOLD : Typeface.NORMAL);
        vh.created.setTypeface(null, unread ? Typeface.BOLD : Typeface.NORMAL);
    }

    static class ViewHolder {

        @BindView(R.id.author) TextView author;
        @BindView(R.id.created) TextView created;
        @BindView(R.id.subject) TextView subject;
        @BindView(R.id.body) TextView body;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
