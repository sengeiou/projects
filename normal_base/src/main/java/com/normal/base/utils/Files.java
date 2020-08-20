package com.normal.base.utils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.net.URL;
import java.util.Arrays;

public final class Files {

    static {
        System.setProperty("java.awt.headless", "false");
    }

    public static byte[] url2Bytes(URL url) {
        InputStream input = null;
        ByteArrayOutputStream out = null;
        try {
            input = url.openStream();
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                //igonre
            }
        }
    }


    public static void download(URL url, String fileFullPath) {
        FileOutputStream output = null;
        try {
            File file = new File(fileFullPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            output.write(url2Bytes(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static void ctrlC(File file) {

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        cb.setContents(new Transferable() {
            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.javaFileListFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.javaFileListFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                return Arrays.asList(file);
            }
        }, (clipboard, contents) -> {

        });
    }



}
