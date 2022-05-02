package com.cartoonize.service;

import java.awt.image.BufferedImage;

@FunctionalInterface
public interface ProcessImageFunction {

    BufferedImage process(BufferedImage image) throws Exception;

    default ProcessImageFunction thenCompose(ProcessImageFunction next) {
        return (BufferedImage first) -> next.process(process(first));
    }
}
