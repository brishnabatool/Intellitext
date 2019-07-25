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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

//import org.mortennobel.imagescaling.ResampleOp;
//import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
//import org.neuroph.contrib.imgrec.ImageRecognitionPlugin;
//import org.neuroph.contrib.imgrec.image.Dimension;
//import org.neuroph.contrib.imgrec.image.Image;
//import org.neuroph.contrib.imgrec.image.ImageFactory;
//import org.neuroph.contrib.imgrec.ColorMode;

import org.neuroph.imgrec.ImageRecognitionPlugin;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.imgrec.ImageUtilities;
import org.neuroph.imgrec.image.Dimension;
import org.neuroph.imgrec.image.Image;
import org.neuroph.imgrec.image.ImageFactory;

/**
 *
 * @author Ivana Jovicic, Vladimir Kolarevic, Marko Ivanovic
 */
public class RecognizeImage {

    private ImageRecognitionPlugin imageRecognition;
    Context context;



    public RecognizeImage(Context context,NeuralNetwork nnet){
//        showDialog(LOADING_DATA_DIALOG);
        // load neural network in separate thread with stack size = 32000
        this.context=context;
        imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(ImageRecognitionPlugin.class);

        //Neural network that is used for recognition
        //nnet = NeuralNetwork.load(this.getClass().getResourceAsStream("/java/res/raw/nreza.nnet"));

    }


    private String getAnswer(HashMap<String, Double> output) {
        double highest = 0;
        String answer = "";
        for (Map.Entry<String, Double> entry : output.entrySet()) {
            if (entry.getValue() > highest) {
                highest = entry.getValue();
                answer = entry.getKey();
            }
        }

        return answer;
    }


    /**
     * This method recognizes the input image with defined NeuralNetwork
     * and return the letter as char with the biggest probability
     * @param image - the input image type BufferedImage to be recognized
     * @return - returns char value taht is recognized
     */
    public String recognize(Bitmap image) {
        Bitmap b = image;
        //Cleaner cleaner=new Cleaner();
//        Bitmap a = Bitmap.createScaledBitmap(b, 5, 5, false);
        Bitmap a = Bitmap.createScaledBitmap(b, 10, 10, true);

        Cleaner c = new Cleaner();
        a=c.blackAndLightGrayCleaning(a);

//        ColorMode m = ColorMode.FULL_COLOR;
//        Dimension d = new Dimension(40, 40);
//        ImageRecognitionPlugin irp = new ImageRecognitionPlugin(d , m);
//        ImageRecognitionPlugin imageRecognition = (ImageRecognitionPlugin) nnet.getPlugin(irp.getClass());
        //convert bitmap to file
        File filesDir = context.getFilesDir();
        File imageFile = new File(filesDir, "imageName" + ".jpg");

//        Matrix m = new Matrix();
//        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, 5, 5), Matrix.ScaleToFit.CENTER);
//        Bitmap c =  Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);

        OutputStream os;
        String ch="";
        try {
            os = new FileOutputStream(imageFile);
            a.compress(Bitmap.CompressFormat.JPEG, 100, os);

            os.flush();
            os.close();

            Image img=ImageFactory.getImage(imageFile);

            HashMap<String, Double> output = imageRecognition.recognizeImage(img);
//          System.out.println(output.toString());
//            HashMap<String, Neuron> n = imageRecognition.getMaxOutput();
            ch=getAnswer(output);
            //String that will return the recognized character
//            ch = n.toString().substring(1, 2);
//          System.out.println(ch);

        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }


        return ch;
    }
}
