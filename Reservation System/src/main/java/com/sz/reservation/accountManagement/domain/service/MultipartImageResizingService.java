package com.sz.reservation.accountManagement.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

public interface MultipartImageResizingService {
    Image resizeImage(MultipartFile imageToResize);
}
