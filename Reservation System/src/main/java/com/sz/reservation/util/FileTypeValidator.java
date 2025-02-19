package com.sz.reservation.util;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public interface FileTypeValidator {
    MediaType getRealFileType(InputStream fileInputStream);
}
