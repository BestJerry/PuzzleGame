package com.example.puzzlegame.Util;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jerry on 16-9-10.
 */
public class ImageSplitterUtil {

    /*传入bitmap，切成piece*piece块，返回list<imagepiece>*/
    public static List<ImagePiece> splitImage(Bitmap bitmap, int piece) {
        List<ImagePiece> imagePieces = new ArrayList<>();
        //制成正方形
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int piecewidth = Math.min(width, height) / piece;

        for (int i = 0; i < piece; i++) {

            for (int j = 0; j < piece; j++) {

                ImagePiece imagePiece = new ImagePiece();
                imagePiece.setIndex(j + i * piece);

                int x = j * piecewidth;
                int y = i * piecewidth;

                imagePiece.setBitmap(Bitmap.createBitmap(bitmap, x, y, piecewidth, piecewidth));
                imagePieces.add(imagePiece);

            }
        }
        return imagePieces;

    }
}
