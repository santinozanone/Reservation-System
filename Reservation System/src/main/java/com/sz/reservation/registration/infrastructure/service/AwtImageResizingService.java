package com.sz.reservation.registration.infrastructure.service;

import com.sz.reservation.registration.application.useCase.ImageResizingService;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AwtImageResizingService implements ImageResizingService {
    @Override
    public Image resizeImage(int width, int height, BufferedImage imageToResize) {
        return imageToResize.getScaledInstance(width, height, Image.SCALE_DEFAULT);
    }
}
