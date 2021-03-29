package com.imorning.qmcdecoder;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CancellationException;

public class DecodeWorker extends SwingWorker<Void, Void> {
    public String getInput() {
        return input;
    }

    private final String input;
    private final String output;
    private final long length;

    public DecodeWorker(String input, String output) {
        this.input = input;
        this.output = output;
        length = new File(input).length();
    }

    private void decode() throws IOException {
        setProgress(0);
        long pos = 0;

        try (InputStream in = new QmcInputStream(new FileInputStream(input));
             FileOutputStream out = new FileOutputStream(output)) {
            byte[] buffer = new byte[65536];
            for (int read = in.read(buffer); read != -1; read = in.read(buffer)) {
                out.write(buffer, 0, read);
                pos += read;
                setProgress((int) (pos * 100 / length));
                if (isCancelled()) {
                    throw new CancellationException();
                }
            }
        }
        setProgress(100);
    }

    private void cleanup() {
        try {
            File outFile = new File(output);
            if (outFile.isFile()) {
                //noinspection ResultOfMethodCallIgnored
                outFile.delete();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            decode();
        } catch (Exception e) {
            cleanup();
            throw e;
        }
        return null;
    }
}
