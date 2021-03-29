package com.imorning.qmc_decoder;

import com.imorning.qmcdecoder.QmcInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Decoder extends Thread {

    private final String input;
    private final String output;
    private final long length;

    public Decoder(String input, String output) {
        this.input = input;
        this.output = output;
        length = new File(input).length();
    }


    public String getInput() {
        return input;
    }

    private void decode() throws IOException {
        long pos = 0;
        try (InputStream in = new QmcInputStream(new FileInputStream(input));
             FileOutputStream out = new FileOutputStream(output)) {
            byte[] buffer = new byte[65536];
            for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                out.write(buffer, 0, read);
                pos += read;
                //setProgress((int) (pos * 100 / length));
            }
        }
        //setProgress(100);
    }

    private void cleanup() {
        try {
            File outFile = new File(output);
            if (outFile.isFile()) {
                outFile.delete();
            }
        } catch (Exception ignored) {
        }
    }

    protected void exe() {
        try {
            decode();
        } catch (Exception e) {
            cleanup();
        }
    }
}
