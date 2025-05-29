package com.sz.reservation.util;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public interface FileTypeValidator {

    /**
     * This method DOES NOT close the input stream, if the stream supports it, its marked and reset
     * @param fileInputStream
     * @return The real media type of the stream
     */
    MediaType getRealFileType(InputStream fileInputStream);
}
