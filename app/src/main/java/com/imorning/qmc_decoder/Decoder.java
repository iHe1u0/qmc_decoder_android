package com.imorning.qmc_decoder;

import android.util.Log;

import com.imorning.qmcdecoder.QmcInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Decoder extends Thread {

    private static final String TAG = "Decoder";

    private final String input;
    private final String output;

    public Decoder(String input, String output) {
        this.input = input;
        this.output = output;
    }


    public String getOutputPath() {
        return output;
    }

    private void decode() throws IOException {
        try (InputStream in = new QmcInputStream(new FileInputStream(input));
             FileOutputStream out = new FileOutputStream(output)) {
            byte[] buffer = new byte[65536];
            for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                out.write(buffer, 0, read);
            }
        }
    }

    protected void execute() {
        new Thread(() -> {
            try {
                decode();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

}
