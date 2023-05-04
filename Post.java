package ncu.im3069.demo.app;



import org.json.*;




public class Post {

    //id，貼文編號
    private int id;
    //看板分類
    private String group;
    
    private String title;

    //content，貼文內容
    private String info;
        
    // ph，PostHelper 之物件與 Post 相關之資料庫方法（Sigleton） */
    
    private PostHelper ph = PostHelper.getHelper();



    //創建貼文
    public Post(String group,String title,String info) {
    	this.group = group;
    	this.title = title;
    	this.info = info;
    }

    
    //修改貼文建構子

    public Post(int id, String group, String title, String info) {
        this.id = id;
        this.group = group;
        this.title = title;
        this.info = info;
    }
    


    //取得貼文編號
    public int getId() {
        return this.id;
    }

    public String getGroup() {
    	return this.group;
    }
    
    public String getTitle() {
    	return this.title;
    }
    
    //取得貼文內容
    public String getInfo() {
        return this.info;
    }




    //取得該名會員所有資料 @return the data 取得該名會員之所有資料並封裝於JSONObject物件內



    /**
     * 更新會員資料
     *
     * @return the JSON object 回傳SQL更新之結果與相關封裝之資料
     */
    public JSONObject update() {
        /** 新建一個JSONObject用以儲存更新後之資料 */
        JSONObject data = new JSONObject();
        
        /** 檢查該名會員是否已經在資料庫 */
        if(this.id != 0) {
            /** 透過PostHelper物件，更新目前之會員資料置資料庫中 */
            data = ph.update(this);
        }
        
        return data;
    }


    //取得貼文基本資料
    public JSONObject getPostData() {
        JSONObject jso = new JSONObject();
        jso.put("id", getId());
        jso.put("title", getTitle());
        jso.put("group", getGroup());
        jso.put("info", getInfo());

        return jso;
    }
}
    

    //取得貼文所有資訊
 


