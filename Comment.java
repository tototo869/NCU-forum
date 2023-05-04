package ncu.im3069.demo.app;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.*;

public class Comment {
	private int id;
 
    private Member m;

    private String content;
    /** create，訂單創建時間 */
    private Timestamp create;
    
    private String user_name;
    
    private String title;
 
    private  MemberHelper mh = MemberHelper.getHelper();

    /**
     * 實例化（Instantiates）一個新的（new）comment 物件<br>
     * 採用多載（overload）方法進行，此建構子用於新增產品時
     */
    public Comment(String user_name,String title,Member m, String content) {
    	this.title = title;
    	this.user_name = user_name;
    	this.m = m;
    	this.content = content;
    	this.create = Timestamp.valueOf(LocalDateTime.now());
    }

    /**
     * 實例化（Instantiates）一個新的（new）Product 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改產品時
     *
     */
    public Comment(int id, int user_id, String title,String user_name, String content, Timestamp create) {
    	this.id = id;
    	this.title = title;
    	this.user_name = user_name;
    	this.content = content;
    	this.create = create;
    	getMemberFromDB(user_id);
    }
 
    /**
     * 從 DB 中取得產品
     */
    private void getMemberFromDB(int user_id) {
        String id = String.valueOf(user_id);
        this.m = mh.getMemberByID(id);
    }

    /**
     * 取得產品編號
     *
     * @return int 回傳產品編號
     */
    public int getID() {
    	return this.id;
    }

    /**
     * 取得留言使用者名稱
     *
     * @return String 回傳使用者名稱
     */
    public Member getMember() {
    	return this.m;
    }

    /**
     * 取得
     *
     * @return String 回傳留言內容
     */
    public String getContent() {
    	return this.content;
	}

  /**
     * 取得留言創建時間
     *
     * @return Timestamp 回傳訂單創建時間
     */
    public Timestamp getCreateTime() {
        return this.create;
    }
 
    
    public String getuser_name() {
    	return this.user_name;
    }
    
    public String getTitle() {
    	return this.title;
    }
    /**
     * 取得產品資訊
     *
     * @return JSONObject 回傳產品資訊
     */
    public JSONObject getData() {
        /** 透過JSONObject將該項產品所需之資料全部進行封裝*/
        JSONObject jso = new JSONObject();
        jso.put("id", getID());
        jso.put("member", getMember().getData());
        jso.put("content", getContent());
        jso.put("create", getCreateTime());
        jso.put("user_name", getuser_name());
        jso.put("title", getTitle());

        return jso;
    }
 
 
}
