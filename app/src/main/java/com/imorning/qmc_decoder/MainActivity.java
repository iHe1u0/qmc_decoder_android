package com.imorning.qmc_decoder;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity.Song";

    static {
        System.loadLibrary("main");
    }

    private final String qmcPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/qqmusic/song/";
    private String in, out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView qmcListView = findViewById(R.id.songList);
        List<Map<String, Object>> list = new ArrayList<>();
        File file = new File(qmcPath);
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "error in path:" + qmcPath, Toast.LENGTH_LONG).show();
            return;
        }
        if (Objects.requireNonNull(file.list()).length > 0) {
            for (File allFile : Objects.requireNonNull(file.listFiles())) {
                if (isQmcFile(allFile)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("path", allFile.getAbsolutePath());
                    map.put("name", allFile.getName());
                    list.add(map);
                }
            }
        }
        if (list.size() == 0) {
            Toast.makeText(getApplicationContext(), "未扫描出qq音乐下载文件～", Toast.LENGTH_LONG).show();
        }
        SimpleAdapter sa = new SimpleAdapter(MainActivity.this, list, R.layout.file_item, new String[]{"name"}, new int[]{R.id.file_item_tv});
        qmcListView.setAdapter(sa);
        qmcListView.setOnItemClickListener((parent, view, position, id) -> {
            File qmcFile = new File(Objects.requireNonNull(list.get(position).get("path")).toString());
            decode(qmcFile);
        });
    }

    private boolean isQmcFile(File unknown_file) {
        //Log.d(TAG, unknown_file.getAbsolutePath());
        if (!unknown_file.isFile()) {
            return false;
        }
        String path = unknown_file.getName();
        int index = path.lastIndexOf(".");
        if (index == -1) {
            return false;
        }
        String ext = path.substring(index).toLowerCase();
        switch (ext) {
            case ".qmc0":
            case ".qmc3":
            case ".qmcogg":
            case ".qmcflac":
                return true;
            default:
                return false;
        }
    }

    private void decode(File file) {
        String filePath = file.getAbsolutePath();
        try {
            if (!file.exists() || !file.isFile()) {
                System.err.printf("\"%s\" is not a file%n", filePath);
            }
            String inputName;
            String inputExt;
            {
                if (!file.getName().contains(".")) {
                    System.err.printf("\"%s\" - no extension name found%n", filePath);
                }
                int index = filePath.lastIndexOf(".");
                inputName = filePath.substring(0, index);
                inputExt = filePath.substring(index).toLowerCase();
            }
            in = filePath;
            switch (inputExt) {
                case ".qmc0":
                case ".qmc3":
                    out = inputName + ".mp3";
                    break;
                case ".qmcogg":
                    out = inputName + ".ogg";
                    break;
                case ".qmcflac":
                    out = inputName + ".flac";
                    break;
            }
        } catch (Exception ex) {
            System.err.printf("\"%s\" error: %s%n", filePath, ex.getMessage());
            ex.printStackTrace();
        }
        Decoder decoder = new Decoder(in, out);
        decoder.exe();
        Toast.makeText(getApplicationContext(), String.format("已保存在\n%s", decoder.getOutputPath()), Toast.LENGTH_LONG).show();
    }
}