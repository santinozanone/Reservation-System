package com.sz.reservation.registration.application.useCase;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface ImageResizingService {
    Image resizeImage(int width, int height, BufferedImage imageToResize);
}
