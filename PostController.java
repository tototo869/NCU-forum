package ncu.im3069.demo.controller;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;
import org.json.*;

import ncu.im3069.demo.app.*;
import ncu.im3069.tools.*;

import javax.servlet.annotation.WebServlet;

@WebServlet("/api/post.do")
public class PostController extends HttpServlet {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ch，PosttHelper 之物件與 Post 相關之資料庫方法（Sigleton） */
	private PostHelper ph = PostHelper.getHelper();

	/**
	 * 處理 Http Method 請求 POST 方法（新增資料）
	 *
	 * @param request  Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
	 * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		/** 若直接透過前端AJAX之data以key=value之字串方式進行傳遞參數，可以直接由此方法取回資料 */

		/** 取出經解析到 JSONObject 之 Request 參數 */
		String group = jso.getString("group");
		String title = jso.getString("title");
		String info = jso.getString("info");

		// int id, String user_name,String content, Timestamp create, Timestamp edited
		/** 建立一個新的貼文物件 */
		Post p = new Post(group, title, info);

		/** 透過 PostHelper 物件的 create() 方法新建一筆貼文至資料庫 */

		if (group.isEmpty() || title.isEmpty() || info.isEmpty()) {
			/** 以字串組出JSON格式之資料 */
			String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
			/** 透過JsonReader物件回傳到前端（以字串方式） */
			jsr.response(resp, response);
		} else {

			/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */
			JSONObject data = ph.create(p);
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "貼文新增成功！");
			resp.put("response", data);

			/** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */
			jsr.response(resp, response);
		}
	}

	/**
	 * 處理 Http Method 請求 GET 方法（新增資料）
	 *
	 * @param request  Servlet 請求之 HttpServletRequest 之 Request 物件（前端到後端）
	 * @param response Servlet 回傳之 HttpServletResponse 之 Response 物件（後端到前端）
	 * @throws ServletException the servlet exception
	 * @throws IOException      Signals that an I/O exception has occurred.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/** 透過 JsonReader 類別將 Request 之 JSON 格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);

		/** 取出經解析到 JsonReader 之 Request 參數 */
		String id_list = jsr.getParameter("id_list");
		
		JSONObject resp = new JSONObject();

		/** 新建一個 JSONObject 用於將回傳之資料進行封裝 */

		/** 判斷該字串是否存在，若存在代表要取回個別貼文之資料，否則代表要取回全部資料庫內貼文之資料 */
		if (!id_list.isEmpty()) {
			/** 透過 orderHelper 物件的 getByID() 方法自資料庫取回該筆貼文之資料，回傳之資料為 JSONObject 物件 */
	          JSONObject query = ph.getById(id_list);
	          resp.put("status", "200");
	          resp.put("message", "所有貼文之留言資料取得成功");
	          resp.put("response", query);
			jsr.response(resp, response);
		} else {
			/** 透過 PostorderHelper 物件之 getAll() 方法取回所有貼文之資料，回傳之資料為 JSONObject 物件 */
	          JSONObject query = ph.getAll();

	          resp.put("status", "200");
	          resp.put("message", "所有資料取得成功");
	          resp.put("response", query);
		}
		
		jsr.response(resp, response);

		/** 透過 JsonReader 物件回傳到前端（以 JSONObject 方式） */

	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();

		/** 取出經解析到JSONObject之Request參數 */
		int id = jso.getInt("id");

		/** 透過MemberHelper物件的deleteByID()方法至資料庫刪除該名會員，回傳之資料為JSONObject物件 */
		JSONObject query = ph.deleteByID(id);

		/** 新建一個JSONObject用於將回傳之資料進行封裝 */
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
		resp.put("message", "貼文移除成功！");
		resp.put("response", query);

		/** 透過JsonReader物件回傳到前端（以JSONObject方式） */
		jsr.response(resp, response);
	}
}
