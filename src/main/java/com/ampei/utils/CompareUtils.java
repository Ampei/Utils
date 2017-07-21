package com.ampei.utils;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.PixelGrabber;
import java.io.File;

import javax.imageio.ImageIO;

public class CompareUtils {

	public enum Result {
		Matched, SizeMismatch, PixelMismatch
	};

	/**
	 * Compare 2 images based on dimensions
	 * 
	 * @param baseFile
	 * @param actualFile
	 * @return {@link Result}
	 */
	public static Result CompareImage(String baseFile, String actualFile) {

		Result compareResult = Result.PixelMismatch;
		Image baseImage = Toolkit.getDefaultToolkit().getImage(baseFile);
		Image actualImage = Toolkit.getDefaultToolkit().getImage(actualFile);
		try {
			PixelGrabber baseImageGrab = new PixelGrabber(baseImage, 0, 0, -1, -1, false);
			PixelGrabber actualImageGrab = new PixelGrabber(actualImage, 0, 0, -1, -1, false);

			int[] baseImageData = null;
			int[] actualImageData = null;

			if (baseImageGrab.grabPixels()) {
				int width = baseImageGrab.getWidth();
				int height = baseImageGrab.getHeight();
				baseImageData = new int[width * height];
				baseImageData = (int[]) baseImageGrab.getPixels();
			}

			if (actualImageGrab.grabPixels()) {
				int width = actualImageGrab.getWidth();
				int height = actualImageGrab.getHeight();
				actualImageData = new int[width * height];
				actualImageData = (int[]) actualImageGrab.getPixels();
			}

			System.out.println(baseImageGrab.getHeight() + "<>" + actualImageGrab.getHeight());
			System.out.println(baseImageGrab.getWidth() + "<>" + actualImageGrab.getWidth());

			if ((baseImageGrab.getHeight() != actualImageGrab.getHeight())
					|| (baseImageGrab.getWidth() != actualImageGrab.getWidth()))
				compareResult = Result.SizeMismatch;
			else if (java.util.Arrays.equals(baseImageData, actualImageData))
				compareResult = Result.Matched;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return compareResult;
	}

	/**
	 * Compare 2 files images and return % of similitude
	 * 
	 * @param fileA
	 * @param fileB
	 * @return {@link Float}
	 */
	public static float compareImage(File fileA, File fileB) {

		float percentage = 0;
		try {
			// take buffer data from both image files //
			BufferedImage biA = ImageIO.read(fileA);
			DataBuffer dbA = biA.getData().getDataBuffer();
			int sizeA = dbA.getSize();
			BufferedImage biB = ImageIO.read(fileB);
			DataBuffer dbB = biB.getData().getDataBuffer();
			int sizeB = dbB.getSize();
			int count = 0;
			// compare data-buffer objects //
			if (sizeA == sizeB) {
				for (int i = 0; i < sizeA; i++) {
					if (dbA.getElem(i) == dbB.getElem(i))
						count = count + 1;
				}
				percentage = (count * 100) / sizeA;
			} else {
				System.out.println("Both the images are not of same size");
			}

		} catch (Exception e) {
			System.out.println("Failed to compare image files ...");
		}
		return percentage;
	}

}
