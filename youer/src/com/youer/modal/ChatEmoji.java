package com.youer.modal;


/**
 * 
 ******************************************
 * @author 寤栦箖娉�
 * @鏂囦欢鍚嶇О	:  ChatEmoji.java
 * @鍒涘缓鏃堕棿	: 2013-1-27 涓嬪崍02:33:43
 * @鏂囦欢鎻忚堪	: 琛ㄦ儏瀵硅薄瀹炰綋
 ******************************************
 */
public class ChatEmoji {

    /** 琛ㄦ儏璧勬簮鍥剧墖瀵瑰簲鐨処D */
    private int id;

    /** 琛ㄦ儏璧勬簮瀵瑰簲鐨勬枃瀛楁弿杩�*/
    private String character;

    /** 琛ㄦ儏璧勬簮鐨勬枃浠跺悕 */
    private String faceName;

    /** 琛ㄦ儏璧勬簮鍥剧墖瀵瑰簲鐨処D */
    public int getId() {
        return id;
    }

    /** 琛ㄦ儏璧勬簮鍥剧墖瀵瑰簲鐨処D */
    public void setId(int id) {
        this.id=id;
    }

    /** 琛ㄦ儏璧勬簮瀵瑰簲鐨勬枃瀛楁弿杩�*/
    public String getCharacter() {
        return character;
    }

    /** 琛ㄦ儏璧勬簮瀵瑰簲鐨勬枃瀛楁弿杩�*/
    public void setCharacter(String character) {
        this.character=character;
    }

    /** 琛ㄦ儏璧勬簮鐨勬枃浠跺悕 */
    public String getFaceName() {
        return faceName;
    }

    /** 琛ㄦ儏璧勬簮鐨勬枃浠跺悕 */
    public void setFaceName(String faceName) {
        this.faceName=faceName;
    }
}
