package com.hyts.assemble.seal.process.bean;

/**
 * 上下弦文对象
 * @author wangfubin
 *
 */
public class HorizonWordBean {
	/**
	 * 上下弦文内容
	 */
	private String text="";
	/**
	 * 文字大小
	 * 测试发现字体大小小于26显示不正常。
	 */
	private int fontSize = 26;
	/**
	 * 占用角度
	 */
	private double angle = 160;
	/**
	 * 是否加粗
	 */
	private boolean isBold = true;
	/**
	 * 字体
	 */
	private String font = "宋体";
	/**
	 * 文字内移
	 */
	private double offset = 0;
	/**
	 * 是否上下弦文
	 */
	private boolean isAbove;
	
	public HorizonWordBean() {
		super();
	}
	
	
	public HorizonWordBean(String text,boolean isAbove) {
		super();
		this.isAbove = isAbove;
		this.setText(text);
	}


	public String getText() {
		return text;
	}
	public void setText(String text) {
		if(!isAbove){
			this.text = new StringBuffer(text).reverse().toString();
		}else{
			this.text = text;
		}
	}
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	public double getAngle() {
		return angle;
	}
	public void setAngle(double angle) {
		this.angle = angle;
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
	public double getOffset() {
		return offset;
	}
	public void setOffset(double offset) {
		this.offset = offset;
	}
	public boolean isAbove() {
		return isAbove;
	}
	public void setAbove(boolean isAbove) {
		this.isAbove = isAbove;
	}
	
	
}
