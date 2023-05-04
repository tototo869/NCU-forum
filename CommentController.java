package ncu.im3069.demo.controller;



import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.*;
import ncu.im3069.tools.JsonReader;

@WebServlet("/api/comment.do")
public class CommentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CommentHelper ch =  CommentHelper.getHelper();
	private MemberHelper mh = MemberHelper.getHelper();

    public CommentController() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id_list = jsr.getParameter("id_list");

        JSONObject resp = new JSONObject();
        /** 判斷該字串是否存在，若存在代表要取回購物車內產品之資料，否則代表要取回全部資料庫內產品之資料 */
        if (!id_list.isEmpty()) {
          JSONObject query = ch.getById(id_list);
          resp.put("status", "200");
          resp.put("message", "所有貼文之留言資料取得成功");
          resp.put("response", query);
        }
        else {
          JSONObject query = ch.getAll();

          resp.put("status", "200");
          resp.put("message", "所有資料取得成功");
          resp.put("response", query);
        }

        jsr.response(resp, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        /** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */
        String id = jsr.getParameter("id");
        
        /** 取出經解析到 JSONObject 之 Request 參數 */
        Member m = mh.getMemberByID(id);
        String content = jso.getString("content");//
        String user_name = jso.getString("user_name");
        String title = jso.getString("title");

        //int id, String user_name,String content, Timestamp create, Timestamp edited
        /** 建立一個新的貼文物件 */
        Comment c = new Comment(title,user_name,m, content);

        JSONObject data = ch.create(c);
        /** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "留言新增成功！");
        resp.put("response", data);

        /** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
        jsr.response(resp, response);
	}
	
    /**
     * 處理Http Method請求DELETE方法（刪除）
     *
     * @param request Servlet請求之HttpServletRequest之Request物件（前端到後端）
     * @param response Servlet回傳之HttpServletResponse之Response物件（後端到前端）
     * @throws ServletException the servlet exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        /** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        int id = jso.getInt("id");
        
        /** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
        JSONObject query = ch.deleteByID(id);
        
        /** 新建一個JSONObject用於將回傳之資料進行封裝 */
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "留言移除成功！");
        resp.put("response", query);

        /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
        jsr.response(resp, response);
    }

}
