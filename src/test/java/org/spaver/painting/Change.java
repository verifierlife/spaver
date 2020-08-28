package org.spaver.painting;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Change {
	public static void convert(String path, String outFile) throws IOException {

		// 将背景色变�?�明
		try {
			BufferedImage image = ImageIO.read(new File(path));
			ImageIcon imageIcon = new ImageIcon(image);
			int w = imageIcon.getIconWidth();
			int h = imageIcon.getIconHeight();
			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
			g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
			int alpha = 0;
			for (int i = bufferedImage.getMinX(); i < w; i++) {
				for (int j = bufferedImage.getMinY(); j < h; j++) {
					int rgb = bufferedImage.getRGB(i, j);
					// 以背景色左上角最小像素做参�?�系
					int RGB = bufferedImage.getRGB(bufferedImage.getMinX(), bufferedImage.getMinY());
					int r = (rgb & 0xff0000) >> 16;
					int g = (rgb & 0xff00) >> 8;
					int b = (rgb & 0xff);
					int R = (RGB & 0xff0000) >> 16;
					int G = (RGB & 0xff00) >> 8;
					int B = (RGB & 0xff);
					// a为色差范围�?�，渐变色边缘处理，数�?�需要具体测试，50左右的效果比较可�?
					int a = 45;
					if (Math.abs(R - r) < a && Math.abs(G - g) < a && Math.abs(B - b) < a) {
						alpha = 0;
					} else
						alpha = 255;
					rgb = (alpha << 24) | (rgb & 0x00ffffff);
					bufferedImage.setRGB(i, j, rgb);
				}
			}
			g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
			ImageIO.write(bufferedImage, "png", new File(outFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void convert2(String imagePath, String outFile) throws IOException {
		// 指定某种单色为�?�明
		try {
			BufferedImage image = ImageIO.read(new File(imagePath));
			ImageIcon imageIcon = new ImageIcon(image);
			int w = imageIcon.getIconWidth();
			int h = imageIcon.getIconHeight();
			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
			g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
			int alpha = 0;
			for (int i = bufferedImage.getMinX(); i < w; i++) {
				for (int j = bufferedImage.getMinY(); j < h; j++) {
					int rgb = bufferedImage.getRGB(i, j);
					// 以白色为�?
					int RGB = Color.WHITE.getRGB();
					int r = (rgb & 0xff0000) >> 16;
					int g = (rgb & 0xff00) >> 8;
					int b = (rgb & 0xff);
					int R = (RGB & 0xff0000) >> 16;
					int G = (RGB & 0xff00) >> 8;
					int B = (RGB & 0xff);
					if (Math.abs(R - r) < 15 && Math.abs(G - g) < 15 && Math.abs(B - b) < 15) {
						alpha = 0;
					} else
						alpha = 255;
					rgb = (alpha << 24) | (rgb & 0x00ffffff);
					bufferedImage.setRGB(i, j, rgb);
				}
			}
			g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
			ImageIO.write(bufferedImage, "png", new File(outFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {

		try {
			convert("./ecnu.png", "./test.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			convert2("./ecnu.png", "./test2.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			convert3("./ecnu.png", "./test3.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void convert3(String imagePath, String outFile) throws IOException {

		// 指定某种颜色替换成另�?�?
		BufferedImage image = null;
		image = ImageIO.read(new File(imagePath));
		int w = image.getWidth();
		int h = image.getHeight();
		int minx = image.getMinTileX();
		int miny = image.getMinTileY();
		for (int i = minx; i < w; i++) {
			for (int j = miny; j < h; j++) {
				int rgb = image.getRGB(i, j);
				// 以黑色为�?
				int RGB = Color.BLACK.getRGB();
				int r = (rgb & 0xff0000) >> 16;
				int g = (rgb & 0xff00) >> 8;
				int b = (rgb & 0xff);
				int R = (RGB & 0xff0000) >> 16;
				int G = (RGB & 0xff00) >> 8;
				int B = (RGB & 0xff);
				if (Math.abs(R - r) < 75 && Math.abs(G - g) < 75 && Math.abs(B - b) < 75) {
					// 0xff0000是红色的十六进制代码
					image.setRGB(i, j, 0xff0000);
				}
			}
			ImageIO.write(image, "png", new File(outFile));
		}
	}

}
