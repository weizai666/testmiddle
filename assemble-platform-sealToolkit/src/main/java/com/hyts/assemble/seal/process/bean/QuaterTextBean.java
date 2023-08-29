package com.hyts.assemble.seal.process.bean;
/**
 * 印章内横文对象
 * @author wangfubin
 *
 */
public class QuaterTextBean {
	/**
	 *  横文内容
	 */
	private String text = "";
	/**
	 * 字体大小
	 */
	private int fontSize = 15;
	/**
	 * 是否加粗
	 */
	private boolean isBold = false;
	/**
	 * 字体
	 */
	private String font = "宋体";
	/**
	 * 字间距
	 */
	private double textOffset = 6;
	/**
	 * 文字下移
	 */
	private double heightOffset = 0;
	
	
	
	public QuaterTextBean(String quaterText,double heightOffset) {
		super();
		this.text = quaterText;
		this.heightOffset = heightOffset;
	}
	public QuaterTextBean(String quaterText) {
		super();
		this.text = quaterText;
	}

	public QuaterTextBean() {
		super();
	}
	
	public String getQuaterText() {
		return text;
	}
	public void setQuaterText(String quaterText) {
		this.text = quaterText;
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public boolean isBold() {
		return isBold;
	}
	public void setBold(boolean isBold) {
		this.isBold = isBold;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public double getTextOffset() {
		return textOffset;
	}
	public void setTextOffset(double textOffset) {
		this.textOffset = textOffset;
	}
	public double getHeightOffset() {
		return heightOffset;
	}
	public void setHeightOffset(double heightOffset) {
		this.heightOffset = heightOffset;
	}
	
}
