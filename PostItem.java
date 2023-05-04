package ncu.im3069.demo.app;

import org.json.JSONObject;

	//貼文內留言
public class PostItem {

    // id，留言細項編號 */
    private int id;

    //** cm，留言物件 */
    private Comment cm;

    // quantity，留言數量 */
    private int quantity;
    
    private CommentHelper ch =  CommentHelper.getHelper();

    
    public PostItem(Comment cm) {
        this.cm = cm;
    }

    /**
     * 實例化（Instantiates）一個新的（new）OrderItem 物件<br>
     * 採用多載（overload）方法進行，此建構子用於修改訂單細項時
     *
     * @param order_product_id 訂單產品編號
     * @param order_id 會員密碼
     * @param product_id 產品編號
     * @param price 產品價格
     * @param quantity 產品數量
     */
    public PostItem(int post_comment_id, int post_id, int comment_id,  int quantity){
     
        this.id = post_comment_id;
        this.quantity = quantity;
        getCommentFromDB(comment_id);
        
    }

    //從 DB 中取得留言

    private void getCommentFromDB(int comment_id) {
        String id = String.valueOf(comment_id);
        this.cm = ch.getCommentById(id);
    }

    //取得留言 回傳留言
    public Comment getComment() {
        return this.cm;
    }

    
     //設定訂單細項編號
    public void setId(int id) {
        this.id = id;
    }

    //取得留言編號 @return int 回傳留言編號
    public int getId() {
        return this.id;
    }

     
    //取得留言數量 @return int 回傳產品數量
     
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * 取得產品細項資料
     *
     * @return JSONObject 回傳產品細項資料
     */
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("id", getId());
        data.put("comment", getComment().getData());
        data.put("quantity", getQuantity());

        return data;
    }
}