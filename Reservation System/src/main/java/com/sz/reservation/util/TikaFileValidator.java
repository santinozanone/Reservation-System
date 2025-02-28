package com.sz.reservation.util;

import com.sz.reservation.accountManagement.infrastructure.exception.FileReadingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.InputStream;

public class TikaFileValidator implements FileTypeValidator {
    private Logger logger = LogManager.getLogger(TikaFileValidator.class);
    @Override
    public MediaType getRealFileType(InputStream fileInputStream) {
        logger.debug("validating file input stream content");
        try (fileInputStream){
            Tika tika = new Tika();
            MediaType mediaType = MediaType.parseMediaType(tika.detect(fileInputStream));
            fileInputStream.close();
            return mediaType;
        } catch (IOException e) {
            logger.error("IOException when trying to read input stream from file");
            throw new FileReadingException("failed to read input stream from file ", e);
        }
    }
}
