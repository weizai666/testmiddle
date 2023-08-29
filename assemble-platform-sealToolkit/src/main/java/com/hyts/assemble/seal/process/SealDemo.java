package com.hyts.assemble.seal.process;

import com.hyts.assemble.seal.process.bean.HorizonWordBean;
import com.hyts.assemble.seal.process.bean.QuaterTextBean;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class  SealDemo {
	/**
	 * 是否显示内图
	 */
	private boolean isShowPic = false;
	
	/**
	 * 上弦文内容
	 */
	private final static String aboveText = "章丘农商银行";
	/**
	 * 下弦文内容
	 */
	private final static String underText = "对账专用章";
	/**
	 * 生成图像中心点X坐标
	 */
	final int CENTERX = CANS_WIDTH / 2;
	/**
	 * 生成图像中心点Y坐标
	 */
	final int CENTERY = CANS_HEIGHT / 2;
	
	private Graphics2D g2;
	
	private FontRenderContext context;
	
	/**
	 * 生成图片的宽度
	 */
	private final static int CANS_WIDTH = 560;
	/**
	 * 生成图片的长度
	 */
	private final static int CANS_HEIGHT = 560;
	//***********************外框*********************************
	/**
	 *  外边框画笔宽度
	 */
	private final static int penWidth = 8;
	
	/**
	 * 平行于X轴半轴长度
	 */
	private final static int shape_w = 240;
	/**
	 * 平行于Y轴半轴长度
	 */
	private final static int shape_h = 200;
	//***********************内图*********************************
	/**
	 * 内图显示内容
	 */
	private final static String innerPic = "★";
	
	/**
	 * 内图显示大小
	 */
	private final static int innerPicSize = 120;
	
	/**
	 * 内图显示内容下移位置
	 */
	int innerPicOffset = 20;
	
	
	/**
	 * 椭圆电子图章
	 * @param lists 横文
	 * @param aboveBean	上弦文
	 * @param underBean 下弦文
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public BufferedImage ellipseDeal(List<QuaterTextBean> lists, HorizonWordBean aboveBean, HorizonWordBean underBean) throws FileNotFoundException, IOException {

		BufferedImage image = new BufferedImage(CANS_WIDTH, CANS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		// 初始化画布
		initCanvas(image);
		
		// 外框
		drawCycle();

		// 内图
		if (isShowPic){
			drawInnerPic();
		}
		
		// 横向文
		drawQuater(lists);
		// 上弦文
		drawHorizonWord(aboveBean);
		// 下弦文
		drawHorizonWord(underBean);

		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g2.dispose();
		return image;
	}
	/**
	 * 矩形电子图章
	 * @param lists
	 * @return
	 */
	public BufferedImage squareDeal(List<QuaterTextBean> lists){
		BufferedImage image = new BufferedImage(CANS_WIDTH, CANS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		initCanvas(image);
		// 外框
		drawSquare();
		
		// 内图
		if (isShowPic){
			drawInnerPic();
		}
		
		// 横向文
		drawQuater(lists);
		
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g2.dispose();
		return image;
	}
	
	public BufferedImage drawDiamondSeal(List<QuaterTextBean> lists){
		BufferedImage image = new BufferedImage(CANS_WIDTH, CANS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		initCanvas(image);
		// 外框
		drawDiamond();
		
		// 内图
		if (isShowPic){
			drawInnerPic();
		}
		
		// 横向文
		drawQuater(lists);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g2.dispose();
		return image;
	}

	public BufferedImage drawTriangleSeal(List<QuaterTextBean> lists){
		BufferedImage image = new BufferedImage(CANS_WIDTH, CANS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		initCanvas(image);
		// 外框
		drawTriangle();
		
		// 内图
		if (isShowPic){
			drawInnerPic();
		}
		
		// 横向文
		drawQuater(lists);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g2.dispose();
		return image;
	}
	
	private void initCanvas(BufferedImage image) {
		g2 = image.createGraphics();
		// 绘制画布
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, CANS_WIDTH, CANS_HEIGHT);
		// 设置背景透明
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
		// 设置画笔颜色
		g2.setColor(Color.RED);
		// 抗锯齿
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		context = g2.getFontRenderContext();
	}

	private void drawInnerPic() {
		Font innerPicFont = new Font("宋体", Font.PLAIN, innerPicSize);
		Rectangle2D bounds = innerPicFont.getStringBounds(innerPic, context);
		// 抗锯齿
		g2.setFont(innerPicFont);
		g2.drawString(innerPic, (float) (CENTERX - bounds.getWidth() / 2), (float)(CENTERY + innerPicOffset - 10));
	}

	/**
	 * 
	 * @param penWidth
	 * @param w
	 * @param h
	 */
	private void drawCycle() {
		/*********** draw circle *************/
		g2.setPaint(Color.red);
		g2.setStroke(new BasicStroke(penWidth));// 设置画笔的粗度
		Ellipse2D circle = new Ellipse2D.Double((CANS_WIDTH - shape_w * 2) / 2, (CANS_HEIGHT - shape_h * 2) / 2, shape_w * 2,
				shape_h * 2);
		g2.draw(circle);
	}

	private void drawSquare(){
		g2.setPaint(Color.red);
		g2.setStroke(new BasicStroke(penWidth));
		Rectangle2D square = new Rectangle2D.Double(CENTERX - shape_w, CENTERY - shape_h, shape_w*2, shape_h*2);
		g2.draw(square);
	}
	
	private void drawDiamond() {
		g2.setPaint(Color.red);
		g2.setStroke(new BasicStroke(penWidth));
		Polygon p = new Polygon();
		p.addPoint(CENTERX, CENTERY - shape_h);
		p.addPoint(CENTERX - shape_w, CENTERY);
		p.addPoint(CENTERX, CENTERY + shape_h);
		p.addPoint(CENTERX + shape_w, CENTERY);
		g2.draw(p);
	}
	
	private void drawTriangle() {
		g2.setPaint(Color.red);
		g2.setStroke(new BasicStroke(penWidth));
		
		Polygon p = new Polygon();
		p.addPoint(CENTERX, (int) (CENTERY - Math.floor(4 * shape_h / 3)));
		p.addPoint((CENTERX - shape_w), (int) (CENTERY + Math.floor(2 * shape_h / 3)));
		p.addPoint(CENTERX + shape_w, (int) (CENTERY + Math.floor(2 * shape_h / 3)));
		
		g2.draw(p);
	}
	
	/**
	 * 横向文
	 */
	private void drawQuater(List<QuaterTextBean> quaterTextBeans) {
		double heightOffSet = 0;
		for (int i = 0; i < quaterTextBeans.size(); i++) {
			QuaterTextBean bean = quaterTextBeans.get(i);
			String quaterText = bean.getQuaterText();
			if (null != bean) {
				// 字体
				Font font;
				if (bean.isBold()){
					font = new Font(bean.getFont(), Font.BOLD, bean.getFontSize());
				}else{
					font = new Font(bean.getFont(), Font.PLAIN, bean.getFontSize());
				}
				g2.setFont(font);
				Rectangle2D hbounds = font.getStringBounds(quaterText, context);
				heightOffSet += (bean.getHeightOffset() * 1 + hbounds.getHeight() * 1);
				double w = hbounds.getWidth();
				String[] quaterTexts = quaterText.split("", 0);
				String[] texts = new String[quaterTexts.length - 1];
				System.arraycopy(quaterTexts, 1, texts, 0, quaterTexts.length - 1);
				int textLen = texts.length;
				
				double n = w/textLen;
				for(int j = 0; j < textLen; j++){
					g2.drawString(texts[j], (float) (CENTERX - (w + bean.getTextOffset() * (textLen - 1)) / 2 + j * (n + bean.getTextOffset())),
							(float) (CENTERY + hbounds.getHeight() / 2 + heightOffSet));
				}
			}
		}
	}

	/**
	 * 上下弦文
	 * 
	 */
	private void drawHorizonWord(HorizonWordBean bean) {
		
		String text = bean.getText();
		String[] texts = text.split("");
		String[] messages = new String[texts.length-1];
        System.arraycopy(texts,1,messages,0,texts.length-1);
		// 输入的字数
		int ilength = messages.length;
		Font f;
		if (bean.isBold()){
			f = new Font(bean.getFont(), Font.BOLD, bean.getFontSize());
		}else{
			f = new Font(bean.getFont(), Font.PLAIN, bean.getFontSize());
		}
		Rectangle2D textbounds = f.getStringBounds(text, context);
		double textOffset;
		if (bean.isAbove()){
			textOffset = bean.getOffset() + textbounds.getHeight();
		}else{
			textOffset = bean.getOffset() + penWidth;
		}
		double w = shape_w - textOffset;
		double h = shape_h - textOffset;
		// 字符宽度＝字符串长度/字符数
		double size = (textbounds.getWidth() / ilength);
		double k = size / (h * 2);
		// 半个字的弧度
		double wordArc = Math.asin(k);
		// 字与字之间夹角弧度
		double offset;
		if (ilength != 1) {
			offset = (bean.getAngle() / 180 * Math.PI - 2 * wordArc * ilength) / (ilength - 1);
		} else {
			offset = 0;
		}
		double mid = Math.floor(ilength / 2);
		double x = 0, y = 0;
		double angle = 0;

		if (ilength % 2 == 0) {// 文字个数为偶数
			for (int i = 0; i < ilength; i++) {
				// 上弦文
				if (bean.isAbove()) {
					angle = (i - mid) * (offset + 2 * wordArc) + offset / 2;
					x = CENTERX + w * Math.sin(angle);
					y = CENTERY - h * Math.cos(angle);
				} else {
					angle = (mid - 1 - i) * (offset + 2 * wordArc) + offset / 2;
					x = CENTERX + w * Math.sin(angle);
					y = CENTERY + h * Math.cos(angle);
				}
				AffineTransform transform = AffineTransform.getRotateInstance((2 * i - 2 * mid + 1) * (offset / 2 + wordArc));
				Font f2 = f.deriveFont(transform);
				g2.setFont(f2);
				g2.drawString(messages[i], (float) x, (float) y);

			}
		} else {
			for (int i = 0; i < ilength; i++) {
				if (bean.isAbove()) {
					angle = (i - mid) * (offset + 2 * wordArc) - wordArc;
					x = CENTERX + w * Math.sin(angle);
					y = CENTERY - h * Math.cos(angle);
				} else {
					angle = (mid - i) * (offset + wordArc * 2) - wordArc;
					x = CENTERX + w * Math.sin(angle);
					y = CENTERY + h * Math.cos(angle);
				}
				AffineTransform transform = AffineTransform.getRotateInstance((i - mid) * (offset + 2 * wordArc));
				Font f2 = f.deriveFont(transform);
				g2.setFont(f2);
				g2.drawString(messages[i], (float) x, (float) y);
			}
		}

	}
	/**
	 * 椭圆章测试
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void ellipseDealText() throws FileNotFoundException, IOException{
		SealDemo demo = new SealDemo();
		// 横文
		List<QuaterTextBean> lists = new ArrayList<QuaterTextBean>();
		QuaterTextBean text1 = new QuaterTextBean("法人行名", -60);
		text1.setFontSize(27);
		text1.setBold(false);
		text1.setTextOffset(0);;
		lists.add(text1);
		QuaterTextBean text2 = new QuaterTextBean("组织机构", 15);
		text2.setFontSize(25);
		text1.setBold(false);
		text1.setTextOffset(0);;
		lists.add(text2);
		// 上弦文
		HorizonWordBean aboveBean = new HorizonWordBean(aboveText, true);
		aboveBean.setBold(true);
		aboveBean.setFontSize(25);
		aboveBean.setAngle(145);
		aboveBean.setOffset(15);
		aboveBean.setFont("仿宋");
		HorizonWordBean underBean = new HorizonWordBean(underText, false);
		underBean.setFontSize(20);
		underBean.setBold(false);
		underBean.setAngle(90);
		underBean.setOffset(10);
		BufferedImage image = demo.ellipseDeal(lists, aboveBean, underBean);
		ImageIO.write(image, "png", new File("E:\\icon4.png"));
	}
	/**
	 * 矩形章测试
	 * @throws IOException
	 */
	public void squareDealTest() throws IOException{
		// 横文
		SealDemo demo = new SealDemo();
		List<QuaterTextBean> lists = new ArrayList<QuaterTextBean>();
		QuaterTextBean text1 = new QuaterTextBean("法人行名", -60);
		text1.setFontSize(27);
		text1.setBold(false);
		text1.setTextOffset(0);;
		lists.add(text1);
		QuaterTextBean text2 = new QuaterTextBean("组织机构", 15);
		text2.setFontSize(25);
		text1.setBold(false);
		text1.setTextOffset(0);
		lists.add(text2);
		BufferedImage image = demo.squareDeal(lists);
		ImageIO.write(image, "png", new File("E:\\icon5.png"));
	}
	/**
	 * 菱形章测试
	 * @throws IOException
	 */
	public void diamondTest() throws IOException{
		SealDemo demo = new SealDemo();
		List<QuaterTextBean> lists = new ArrayList<QuaterTextBean>();
		QuaterTextBean text1 = new QuaterTextBean("法人行名", -60);
		text1.setFontSize(27);
		text1.setBold(false);
		text1.setTextOffset(0);;
		lists.add(text1);
		QuaterTextBean text2 = new QuaterTextBean("组织机构", 15);
		text2.setFontSize(25);
		text1.setBold(false);
		text1.setTextOffset(0);
		lists.add(text2);
		BufferedImage image = demo.drawDiamondSeal(lists);
		ImageIO.write(image, "png", new File("E:\\icon5.png"));
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
			long l = System.currentTimeMillis();
			triangleDealTest();
			System.out.println(System.currentTimeMillis() - l);
	}
	
	/**
	 * 三角形章测试
	 * @throws IOException
	 */
	private static void triangleDealTest() throws IOException {
		SealDemo demo = new SealDemo();
		List<QuaterTextBean> lists = new ArrayList<QuaterTextBean>();
		QuaterTextBean text1 = new QuaterTextBean("法人行名", -60);
		text1.setFontSize(27);
		text1.setBold(false);
		text1.setTextOffset(0);;
		lists.add(text1);
		QuaterTextBean text2 = new QuaterTextBean("组织机构", 15);
		text2.setFontSize(25);
		text1.setBold(false);
		text1.setTextOffset(0);
		lists.add(text2);
		BufferedImage image = demo.drawTriangleSeal(lists);
		ImageIO.write(image, "png", new File("./icon6.png"));
	}

}
