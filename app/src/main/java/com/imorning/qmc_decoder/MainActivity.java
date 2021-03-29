package com.imorning.qmc_decoder;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    static {
        System.loadLibrary("main");
    }

    private String in, out;

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File("/sdcard/test.qmc0");
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
        new Decoder(in, out).exe();
    }

    private boolean isQmcFile(File f) {
        if (!f.isFile()) {
            return false;
        }
        String path = f.getName();
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
                break;
            default:
                return false;
        }
        return true;
    }
}