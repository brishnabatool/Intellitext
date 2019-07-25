/***
 * Neuroph  http://neuroph.sourceforge.net
 * Copyright by Neuroph Project (C) 2008
 *
 * This file is part of Neuroph framework.
 *
 * Neuroph is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Neuroph is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Neuroph. If not, see <http://www.gnu.org/licenses/>.
 */
package com.smd.project.ahmad.intellignizer;

import android.graphics.Bitmap;
import android.graphics.Color;

//import java.awt.image.BufferedImage;

/**
 * This class is for cleaning the loaded BufferedImage
 * (due to the JPEG format imprecision)
 * @author Ivana Jovicic, Vladimir Kolarevic, Marko Ivanovic
 */
public class Cleaner {

    /**
     * This method cleans input image by replacing
     * all non black pixels with white pixels
     * @param Image - input image that will be cleaned
     * @return - cleaned input image as BufferedImage
     */
//    public BufferedImage blackAndWhiteCleaning(BufferedImage Image) {
//        BufferedImage bwImage = null;
//        for (int j = 0; j < Image.getHeight(); j++) {
//            for (int i = 0; i < Image.getWidth(); i++) {
//                if (Image.getRGB(i, j) != -16777216) {
//                    Image.setRGB(i, j, -1);
//                }
//            }
//        }
//        return bwImage = Image;
//    }

    /**
     * This method cleans input image by replacing all pixels with RGB values
     * from -4473925 (gray) to -1 (white) with white pixels and
     * from -4473925 (gray) to -16777216 (black) with black pixels
     * @param Image - input image that will be cleaned
     * @return - cleaned input image as BufferedImage
     */
//    public BufferedImage blackAndGrayCleaning(BufferedImage Image) {
//        BufferedImage bwImage = null;
//        for (int j = 0; j < Image.getHeight(); j++) {
//            for (int i = 0; i < Image.getWidth(); i++) {
//                if (Image.getRGB(i, j) > -4473925) {
//                    Image.setRGB(i, j, -1);
//                } else {
//                    Image.setRGB(i, j, -16777216);
//                }
//            }
//        }
//        return bwImage = Image;
//    }

    /**
     * This method cleans input image by replacing all pixels with RGB values
     * from -3092272 (light gray) to -1 (white) with white pixels and
     * from -3092272 (light gray) to -16777216 (black) with black pixels
     * @param Image - input image that will be cleaned
     * @return - cleaned input image as BufferedImage
     */
    public Bitmap blackAndLightGrayCleaning(Bitmap Image) {
//        Bitmap bwImage = null;
        Bitmap img=Bitmap.createBitmap(Image.getWidth(),Image.getHeight(), Image.getConfig());
        for (int j = 0; j < Image.getHeight(); j++) {
            for (int i = 0; i < Image.getWidth(); i++) {
                if (Image.getPixel(i, j) > Color.BLACK/2) {
                    img.setPixel(i,j,Color.WHITE);
                } else {
                    img.setPixel(i, j, (int)Color.BLACK);
                }
            }
        }
        return img;
    }

    /**
     * This method cleans input image by replacing all pixels with RGB values
     * from RGBcolor input (the input color) to -1 (white) with white pixels and
     * from RGBcolor input (the input color) to -16777216 (black) with black pixels
     * @param Image - input image that will be cleaned
     * @param RGBcolor - input RGB value of wanted color as reference for celaning
     * @return - cleaned input image as BufferedImage
     */
//    public BufferedImage ColorCleaning(BufferedImage Image, int RGBcolor) {
//        BufferedImage bwImage = null;
//        for (int j = 0; j < Image.getHeight(); j++) {
//            for (int i = 0; i < Image.getWidth(); i++) {
//                if (Image.getRGB(i, j) == RGBcolor) {
//                    Image.setRGB(i, j, -16777216);
//                } else {
//                    Image.setRGB(i, j, -1);
//                }
//            }
//        }
//        return bwImage = Image;
//    }
}
