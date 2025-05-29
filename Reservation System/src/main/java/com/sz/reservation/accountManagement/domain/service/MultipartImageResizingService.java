package com.sz.reservation.accountManagement.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.InputStream;

public interface MultipartImageResizingService {
    Image resizeImage(String profilePictureOriginalName,InputStream profilePictureStream);
}
