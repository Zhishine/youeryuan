
package com.youer.modal;
/**
 * 
 ******************************************
 * @author 寤栦箖娉�
 * @鏂囦欢鍚嶇О	:  ChatMsgEntity.java
 * @鍒涘缓鏃堕棿	: 2013-1-27 涓嬪崍02:33:33
 * @鏂囦欢鎻忚堪	: 娑堟伅瀹炰綋
 ******************************************
 */
public class MChatMsgEntity {

    private String name;

    private String date;

    private String text;

    private boolean isComMeg = true;

    private boolean isTimeShow =false;
    
    public boolean getTimeShow(){
    	return isTimeShow;
    }
    public void setTimeShow(boolean timeShow){
    	this.isTimeShow=timeShow;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
    	isComMeg = isComMsg;
    }

    public MChatMsgEntity() {
    }

    public MChatMsgEntity(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.isComMeg = isComMsg;
    }

}
