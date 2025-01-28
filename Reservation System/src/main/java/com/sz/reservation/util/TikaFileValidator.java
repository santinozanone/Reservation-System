package com.sz.reservation.util;

import org.apache.tika.Tika;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public class TikaFileValidator implements FileValidator{

    @Override
    public MediaType getRealFileType(InputStream fileInputStream) throws IOException {
        Tika tika = new Tika();
        MediaType mediaType = MediaType.parseMediaType(tika.detect(fileInputStream));
        fileInputStream.close();
        return mediaType;
    }
}
