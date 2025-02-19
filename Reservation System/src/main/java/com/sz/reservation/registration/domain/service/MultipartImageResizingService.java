package com.sz.reservation.registration.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface MultipartImageResizingService {
    Image resizeImage(MultipartFile imageToResize);
}
