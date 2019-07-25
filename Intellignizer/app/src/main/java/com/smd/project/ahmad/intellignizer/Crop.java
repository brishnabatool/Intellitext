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

//import java.awt.image.BufferedImage;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * This class is for croping the input image. Image is croped by
 * the black pixels as border mark
 * @author Ivana Jovicic, Vladimir Kolarevic, Marko Ivanovic
 */
public class Crop {

    /**
     * This method reads the image until it reads the first black pixel
     * by height and then returns that value
     * @param Img - input image that will be read
     * @return - returns the value of height when conditions are true
     */
    public int lockup(Bitmap Img){

        for (int j = 0; j < Img.getHeight(); j++) {
             for (int i = 0; i < Img.getWidth(); i++) {
                    if(Img.getPixel(i, j)== Color.BLACK){
                    if(j==0){
                      return j;
                    }
                    return j-1;
                }
            }
        }
        return 0;
    }

    /**
     * This method reads the input image from the input from
     * start pixel height (y1) until it reads the first next row
     * where all pixel are white by height and return that value
     * @param Img - input image that will be read
     * @param y1 - input start height pixel of image
     * @return - returns the value of height when conditions are true
     */
    public int lockdown(Bitmap Img, int y1) {

        for (int j = y1 + 1; j < Img.getHeight(); j++) {
            int counterWhite = 0;
            for (int i = 0; i < Img.getWidth(); i++) {
                if (Img.getPixel(i, j) == Color.WHITE) {
                    counterWhite++;
                }
            }
            if (counterWhite == Img.getWidth()) {
                //this is a chek for dots over the letters i and j
                //so they wont be missread as dots
                if(j>(Img.getHeight()/2)){
                    return j;
                }
            }
            if (j == Img.getHeight() - 1) {
                return j + 1;
            }
        }
        return 0;
    }

    /**
     * This method crop the input image and return is as a BufferedImage
     * @param imageToCrop - input image that will be cropped
     * @return - return cropped image of input as BufferedImage
     */
    public Bitmap crop(Bitmap imageToCrop) {

        int y1 = lockup(imageToCrop);
        int y2 = lockdown(imageToCrop, y1);
        int x1 = 0;
        int x2 = imageToCrop.getWidth();
        return Bitmap.createBitmap(imageToCrop,x1,y1,x2-x1,y2-y1);
    }
}
