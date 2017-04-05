package com.beini.ui.fragment.music.utils;

import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.beini.base.BaseApplication;

import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.support.model.PersonWithRole;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.Movie;
import org.fourthline.cling.support.model.item.MusicTrack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by beini on 2017/2/20.
 */

public class MediaResourceDao {
    private static String storageDir = "";

    static {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public static List<Item> getAudioList(String serverUrl, String parentId) {
        List<Item> items = new ArrayList<>();

        //Query all track,add to items
        Cursor c = BaseApplication.getInstance().getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.TITLE);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String displayName = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            if(displayName.contains(".aac")){
                c.moveToNext();
                continue;
            }
            String creator = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

            String data = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            //Remove SDCard path
            data = data.replaceFirst(storageDir, "");
            //Replace file name by "id.ext"
            String fileName = data.substring(data.lastIndexOf(File.separator));
            String ext = fileName.substring(fileName.lastIndexOf("."));
            data = data.replace(fileName, File.separator + id + ext);

            String mimeType = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
            long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
            long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
            //Get duration string
            String durationStr = ModelUtil.toTimeString(duration);

            //Compose audio url
            String url = serverUrl + File.separator + "audio" + File.separator + data;
            Res res = new Res(mimeType, size, durationStr, null, url);

            items.add(new MusicTrack(String.valueOf(id), parentId, title, creator, album, new PersonWithRole(creator), res));

            c.moveToNext();
        }

        return items;
    }

    public static List<Item> getVideoList(String serverUrl, String parentId) {
        List<Item> items = new ArrayList<>();

        Cursor c = BaseApplication.getInstance().getContentResolver()
                .query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.TITLE);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(c.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
            String creator = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));

            String data = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            //Remove SDCard path
            data = data.replaceFirst(storageDir, "");
            //Replace file name by "id.ext"
            String fileName = data.substring(data.lastIndexOf(File.separator));
            String ext = fileName.substring(fileName.lastIndexOf("."));
            data = data.replace(fileName, File.separator + id + ext);

            String mimeType = c.getString(c.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
            long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            long duration = c.getLong(c.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
            //Get duration string
            String durationStr = ModelUtil.toTimeString(duration);

            //Compose audio url
            String url = serverUrl + File.separator + "video" + File.separator + data;
            Res res = new Res(mimeType, size, durationStr, null, url);

            items.add(new Movie(String.valueOf(id), parentId, title, creator, res));

            c.moveToNext();
        }

        return items;
    }
}
