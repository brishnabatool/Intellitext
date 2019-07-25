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
//import javax.swing.ImageIcon;
//import org.neuroph.easyneurons.ocr.tcrneuroph.neuroph.RecognizeImage;
//import org.neuroph.easyneurons.ocr.tcrneuroph.view.GUI;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import org.neuroph.core.NeuralNetwork;

/**
 * This class is for extracting the letters from scanned image and preparing
 * them individually (croping and resizing) for recognizing.
 * The recognized letter is written in JTextArea
 * @author Ivana Jovicic, Vladimir Kolarevic, Marko Ivanovic
 */
public class ImageRender {

    private int y1 = 0;//up locked coordinate
    private int y2 = 0;//down locked coordinate
    private int x1 = 0;//left locked coordinate
    private int x2 = 0;//right locked coordinate
    public static Bitmap Img = null;
    private boolean end;//end of picture
    private boolean endRow;//end of current reading row
    String text;
    //private GUI gui;//owner of this class
    RecognizeImage ri;

    public ImageRender(Context context,Bitmap img,NeuralNetwork nnet) {

        //object of class RecognizeImage that will recognize images as letters
        ri = new RecognizeImage(context,nnet);
        Img=img;
//        this.gui = gui;
    }

    /**
     * This method reads the image until it reads the first black pixel
     * by height and sets (changes) y1 on that value
     * @return - return true when true when y1 value is changed and
     * false when y1 value is not changed
     */
    public boolean lockingUp() {
//Img.setPixel(0,0,Color.BLACK);
        for (int j = y2; j < Img.getHeight(); j++) {
            for (int i = x1; i < Img.getWidth(); i++) {
                if (Img.getPixel(i, j) == Color.BLACK) {
                    if (j == 0) {
                        y1 = j;
                        return true;
                    }
                    y1 = j;
                    return true;
                }
            }
        }
        end = true;//sets this value if there are no more black pixels
        return false;
    }

    /**
     * This method reads the image until it reads the first next row
     * where all pixel are white by height and sets (changes) y2 on that value
     * @return - return true when true when y2 value is changed and
     * false when y2 value is not changed
     */
    public boolean lockingDown() {

        for (int j = y1 + 1; j < Img.getHeight(); j++) {
            //counter for number of white pixels in row
            int counterWhite = 0;
            for (int i = x1; i < Img.getWidth(); i++) {
                if (Img.getPixel(i, j) == Color.WHITE) {
                    counterWhite++;
                }
            }
            if (counterWhite == Img.getWidth()) {
                y2 = j;
                return true;
            }
            if (j == Img.getHeight() - 1) {
                y2 = j;
                //sets this value if this method reaches the end of image
                end = true;
                return true;
            }
        }
        return false;
    }

    /**
     * This method reads the image until it reads the first black pixel
     * by width and sets (changes) x1 on that value. It also
     * writes in JTextArea " " and \n if conditions are true
     * @return - return true when true when x1 value is changed and
     * false when x1 value is not changed
     */
    public boolean lockingLeft() {
        //counter for white pixels between the letters
        int spaceCounter = 0;
        for (int i = x2; i < Img.getWidth(); i++) {
            for (int j = y1; j <= y2; j++) {
                if (Img.getPixel(i, j) == Color.BLACK) {
                    if(i==0){
                       x1 = i;
                    return true; 
                    }
                    x1 = i;
                    return true;
                }
            }
            spaceCounter++;
            if (spaceCounter == 3) {
                //add space
                text=text+" ";
                spaceCounter = 0;
            }
        }
        endRow = true;
        //add endline
        text=text+"\n";
        return false;
    }

    /**
     * This method reads the image until it reads the first next row
     * where all pixel are white by width between y1 and y2. It also
     * writes in JTextArea \n if conditions are true
     * and sets (changes) x2 on that value
     * @return - return true when true when x2 value is changed and
     * false when x2 value is not changed
     */
    public boolean lockingRight() {
        for (int i = x1 + 1; i < Img.getWidth(); i++) {
            int counteWhite = 0;
            for (int j = y1; j <= y2; j++) {
                if (Img.getPixel(i, j) == Color.WHITE) {
                    counteWhite++;
                }
            }
            int row = y2 - y1;
            if (counteWhite == row+1) {
                x2 = i;
                return true;
            }
            if (i == Img.getWidth() - 1) {
                x2 = i;
                endRow = true;
                //add endline
                text=text+"\n";
                return true;
            }
        }
        return false;
    }


    /**
     * This method returns subimage type BufferedImage
     * by x1,x2,y1,y2 values
     */
    public Bitmap cutting() {
//        return Bitmap.getSubimage(x1, y1, x2 - x1, y2 - y1);
        return (Bitmap.createBitmap(Img, x1, y1, x2 - x1, y2 - y1));
    }

    /**
     * This method cuts, crops and recognizes images as letter
     * and writes them in JTextArea when all lock methods return true
     */
    public String rendering() {
        
        //object of class Crop that crops the input image
        text="";
        Crop c = new Crop();
        while (end == false) {
            endRow = false;
            boolean up = lockingUp();
            boolean down = false;
            if (up == true) {
                down = lockingDown();
                if (down == true) {
                    while (endRow == false) {
                        boolean left = false;
                        boolean right = false;
                        left = lockingLeft();
                        if (left == true) {
                            right = lockingRight();
                            if (right == true) {
                                //text.concat(ri.recognize(c.crop(cutting())));
                                text=text+ri.recognize(cutting());
                                //gui.setjTextArea1(letter);
                            }
                        }
                    }
                    x1 = 0;
                    x2 = 0;
                }
            }
        }
        y1 = 0;
        y2 = 0;
        end = false;

        return text;
//        gui.getjButton2().setEnabled(true);
//        gui.getjLabel2().setIcon(new ImageIcon(getClass().getResource("/org/neuroph/easyneurons/ocr/resources/images/comment24x24.png")));
//        gui.getjLabel2().setText("Recognizing done!");
//        System.out.println("Recognizing done!");
    }
}
