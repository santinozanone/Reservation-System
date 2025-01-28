package com.sz.reservation.util;

import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface FileValidator {
    MediaType getRealFileType(InputStream fileInputStream) throws IOException;
}
