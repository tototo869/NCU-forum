package ncu.im3069.demo.app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import ncu.im3069.demo.util.DBMgr;
;



public class PostItemHelper {
    
    private PostItemHelper() {
        
    }
    
    private static PostItemHelper oph;
    private Connection conn = null;
    private PreparedStatement pres = null;
    
    /**
     * 靜態方法<br>
     * 實作Singleton（單例模式），僅允許建立一個MemberHelper物件
     *
     * @return the helper 回傳MemberHelper物件
     */
    public static PostItemHelper getHelper() {
        /** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
        if(oph == null) oph = new PostItemHelper();
        
        return oph;
    }
    
    public JSONArray createByList(long post_id, List<PostItem> postcomment) {
        JSONArray jsa = new JSONArray();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        
        for(int i=0 ; i < postcomment.size() ; i++) {
            PostItem pi = postcomment.get(i);
            
            /** 取得所需之參數 */
            int comment_id = pi.getComment().getID();
            int quantity = pi.getQuantity();
            
            try {
                /** 取得資料庫之連線 */
                conn = DBMgr.getConnection();
                /** SQL指令 */
                String sql = "INSERT INTO missa.`order_product`(post_id, comment_id, quantity)"
                        + " VALUES(?, ?, ?)";
                
                /** 將參數回填至SQL指令當中 */
                pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pres.setLong(1, post_id);
                pres.setInt(2, comment_id);
                pres.setInt(3, quantity);
                
                /** 執行新增之SQL指令並記錄影響之行數 */
                pres.executeUpdate();
                
                /** 紀錄真實執行的SQL指令，並印出 **/
                exexcute_sql = pres.toString();
                System.out.println(exexcute_sql);
                
                ResultSet rs = pres.getGeneratedKeys();

                if (rs.next()) {
                    long id = rs.getLong(1);
                    jsa.put(id);
                }
            } catch (SQLException e) {
                /** 印出JDBC SQL指令錯誤 **/
                System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
            } catch (Exception e) {
                /** 若錯誤則印出錯誤訊息 */
                e.printStackTrace();
            } finally {
                /** 關閉連線並釋放所有資料庫相關之資源 **/
             DBMgr.close(pres, conn);
            }
        }
        
        return jsa;
    }
    
    public ArrayList<PostItem> getPostCommentByPostId(int post_id) {
        ArrayList<PostItem> result = new ArrayList<PostItem>();
        /** 記錄實際執行之SQL指令 */
        String exexcute_sql = "";
        ResultSet rs = null;
        PostItem op;
        
        try {
            /** 取得資料庫之連線 */
            conn = DBMgr.getConnection();
            /** SQL指令 */
            String sql = "SELECT * FROM fourm.`post_comment` WHERE post_comment.`post_id` = ?";
            
            /** 將參數回填至SQL指令當中 */
            pres = conn.prepareStatement(sql);
            pres.setInt(1, post_id);
            
            /** 執行新增之SQL指令並記錄影響之行數 */
            rs = pres.executeQuery();
            
            /** 紀錄真實執行的SQL指令，並印出 **/
            exexcute_sql = pres.toString();
            System.out.println(exexcute_sql);
            
            while(rs.next()) {
                /** 每執行一次迴圈表示有一筆資料 */
                
                /** 將 ResultSet 之資料取出 */
                int post_comment_id = rs.getInt("id");
                int comment_id = rs.getInt("comment_id");
                int quantity = rs.getInt("quantity");
                
                /** 將每一筆會員資料產生一名新Member物件 */
                op = new PostItem(post_comment_id, post_id, comment_id,quantity);
                /** 取出該名會員之資料並封裝至 JSONsonArray 內 */
                result.add(op);
            }
        } catch (SQLException e) {
            /** 印出JDBC SQL指令錯誤 **/
            System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            /** 若錯誤則印出錯誤訊息 */
            e.printStackTrace();
        } finally {
            /** 關閉連線並釋放所有資料庫相關之資源 **/
            DBMgr.close(pres, conn);
        }
        
        return result;
    }
}
