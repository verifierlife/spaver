package org.spaver.painting;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painting extends Canvas{
	static int maxn = 2000;
	static int nHeight, mWidth;
	static int[][] a = new int[maxn][maxn];
	// static int[][]pixels;
	static int[][] h = new int[maxn][maxn];
	static int[][] l = new int[maxn][maxn];
	static int[][] r = new int[maxn][maxn];
	static int x1;
	static int y1;
	static int x2;
	static int y2;
	static int rwidth;
	static int rheight;
	static final int FZ = 130;
	public static void main(String[] args) {
		System.out.println("Hello World!");
		
		String filePath = "./src/main/resources/ecnuBig.jpg";
		String filePath2 = "./src/main/resources/Jpg.jpg";
		String outPath = "./src/main/resources/Result.jpg";
		binaryImage(filePath, 100);
				
		removeNoise(0, 3);
		print();
		new Painting().findMaxBlackRect();// findMaxBlackRect();
		stickImage(filePath, filePath2, outPath);
	}
	
	/**
	 * @param filePath
	 * @param threshold
	 */
	public static void binaryImage(String filePath, double threshold) {
		try {
			BufferedImage image = ImageIO.read(new File(filePath));
			int minX = 0;
			int minY = 0;
			int width = image.getWidth();
			mWidth = width;
			int height = image.getHeight();
			nHeight = height;
			// pixels=new int[height][width];

			for (int i = minX; i < width; i++) {
				for (int j = minY; j < height; j++) {
					Object data = image.getRaster().getDataElements(i, j, null);
					int red = image.getColorModel().getRed(data);
					int blue = image.getColorModel().getBlue(data);
					int green = image.getColorModel().getGreen(data);
					if (red == 0 && green == 0 && blue == 0) {
						a[i + 1][j + 1] = 1;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param whiteThreshold
	 * @param blackThreshold
	 */
	public static void removeNoise(int whiteThreshold, int blackThreshold) {
		int i, j, nValue, nCount, m, n;
		int nWidth = Painting.maxn;
		int nHeight = Painting.maxn;
		// Remove noise in the up and down edge of the picture
		for (i = 0; i < nWidth; i++) {
			a[i][0] = 0;
			a[i][nHeight - 1] = 0;
		}

		// Remove noise in the left and right edge of the picture
		for (i = 0; i < nHeight; i++) {
			a[0][i] = 0;
			a[0][nWidth - 1] = 0;
		}

		// Remove noise according to the surrounding color
		// Traversing all the point, j -> y, i -> x
		for (i = 1; i < nWidth - 10; i++) {
			for (j = 1; j < nHeight - 10; j++) {
				nValue = a[i][j];
				// If the point is black
				if (nValue == 1 && whiteThreshold != 0) {
					nCount = 0;
					// Traversing the eight surrounding points, and counting the number of white
					// points
					for (m = j - 1; m <= j + 1; m++) {
						for (n = i - 1; n <= i + 1; n++) {
							if (a[n][m] == 0) {// The number of surrounding white points
								nCount++;
							}
						}
					}
					// If the number of white points is larger than the threshold of white points,
					// Transferring it to white point
					if (nCount >= whiteThreshold) {
						a[i][j] = 0;
					}
				} else { // If only one point is white and the surrounding points are black
					nCount = 0;
					for (m = j - 1; m <= j + 10; m++) {
						for (n = i - 1; n <= i + 10; n++) {
							if (a[n][m] == 1) {
								nCount++;
							}
						}
					}
					// If the number of black points is larger than the threshold of black
					// points,Transferring it to black point
					if (nCount >= blackThreshold) {
						a[i][j] = 1;
					}
				}
			}
		}
	}
	
	public static void print() {
		String filePath = "./src/main/resources/ecnu.jpg";
		try {
			BufferedImage image = ImageIO.read(new File(filePath));
			int width = image.getWidth();
			// m = width;
			System.out.println("The width is: "+width);
			int height = image.getHeight();
			// n = height;
			System.out.println("The height is: "+height);
			for (int i = 1; i < width; i++) {
				for (int j = 1; j < height; j++) {
					if (a[i][j] == 0) {
						Object data = image.getRaster().getDataElements(i - 1, j - 1, null);
						int red = image.getColorModel().getRed(data);
						int blue = image.getColorModel().getBlue(data);
						int green = image.getColorModel().getGreen(data);
						System.out.print("[ red " + red + " blue " + blue + " green " + green + " ]");
					}
				}
				System.out.println();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Finding the max black rectangle
	 */
	public void findMaxBlackRect() {
		//GraphicsContext g = getGraphicsContext2D();
		int ans = 0;
		// Initialization
		for (int i = 1; i <= mWidth; ++i) {
			l[0][i] = 1;
			r[0][i] = mWidth;
		}

		for (int i = 1; i <= nHeight; i++) {
			int maxl = 1, maxr = mWidth;

			for (int j = 1; j <= mWidth; j++) {
				if (a[i][j] == 0) {
					h[i][j] = 0;
					l[i][j] = 1;
					maxl = j + 1;
				} else {
					l[i][j] = Math.max(l[i - 1][j], maxl);
					h[i][j] = h[i - 1][j] + 1;
				}
			}

			for (int j = mWidth; j >= 1; j--) {
				if (a[i][j] == 0) {
					r[i][j] = mWidth;
					maxr = j - 1;
				} else {
					r[i][j] = Math.min(r[i - 1][j], maxr);
					int temp = ans;
					ans = Math.max(ans, (r[i][j] - l[i][j] + 1) * h[i][j]);
					if (temp != ans) {
						y1 = l[i][j];
						x1 = i - h[i][j] + 1;
						rwidth = r[i][j] - l[i][j] + 1;
						rheight = h[i][j];
						System.out.println(
								"i: " + i + ", j:" + j + ", l:" + l[i][j] + ", r:" + r[i][j] + ", h:" + h[i][j]);
					}
				}
			}
		}
		
		x1 = x1 - 1;
		y1 = y1 - 1;
		
//		g.setFill(Color.FUCHSIA);
//		g.fillOval(x1, y1, rwidth, rheight);
		
		System.out.println("x1: " + x1 + " y1: " + y1 + " width: " + rwidth + " height: " + rheight);
	}
	
	/**
	 * 
	 * @param bigImagePath
	 * @param smallImagePath
	 * @param outImagePath
	 */
	public static void stickImage(String bigImagePath, String smallImagePath, String outImagePath) {
		try {
			BufferedImage bigImage = ImageIO.read(new File(bigImagePath));
			BufferedImage smallImage = ImageIO.read(new File(smallImagePath));
			Graphics2D gh = bigImage.createGraphics();
			gh.drawImage(smallImage, null, x1, y1);
			gh.dispose();
			ImageIO.write(bigImage, "jpg", new File(outImagePath));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
