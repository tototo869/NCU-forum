package ncu.im3069.demo.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import ncu.im3069.demo.app.Member;
import ncu.im3069.demo.app.LoginHelper;
import ncu.im3069.tools.JsonReader;

/**
 * Servlet implementation class LoginController
 */
@WebServlet("/api/login.do")
public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /** lh，LoginHelper之物件與Login相關之資料庫方法（Sigleton） */
    private LoginHelper lh =  LoginHelper.getHelper();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		/** 透過JsonReader類別將Request之JSON格式資料解析並取回 */
        JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        /** 取出經解析到JSONObject之Request參數 */
        String email = jso.getString("email");
        String password = jso.getString("password");
        
        /** 建立一個新的會員物件 */
        Member m = new Member(email, password);
        
        /** 後端檢查是否有欄位為空值，若有則回傳錯誤訊息 */
        if(email.isEmpty() || password.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        
        /** 透過LoginHelper物件的checkemailexist()檢查是否有該會員電子郵件信箱*/
        else if (!lh.checkemailexist(m)) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'email不存在\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        
        else if (lh.checkemailexist(m)) {
            if (lh.checklogin(m)) {
                /** 透過MemberHelper物件的create()方法新建一個會員至資料庫 */
                JSONObject data = lh.getLoginUser(m);
                
            	 /** 新建一個JSONObject用於將回傳之資料進行封裝 */
                JSONObject resp = new JSONObject();
                resp.put("status", "200");
                resp.put("message", "登入成功!");
                resp.put("response", data);
                
                /** 透過JsonReader物件回傳到前端（以JSONObject方式） */
                jsr.response(resp, response);
            }
            
            else {
            	/** 以字串組出JSON格式之資料 */
                String resp = "{\"status\": \'400\', \"message\": \'密碼錯誤\', \'response\': \'\'}";
                /** 透過JsonReader物件回傳到前端（以字串方式） */
                jsr.response(resp, response);
            }
                       
        } 
            
        else {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'帳號密碼錯誤，請重新輸入！\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
	}

}

