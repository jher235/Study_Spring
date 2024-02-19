package org.zerock.w2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.service.TodoService;

import java.io.IOException;

@Log4j2
@WebServlet(name = "todoReadController", urlPatterns = "/todo/read")
public class TodoReadController extends HttpServlet {

    private TodoService todoService = TodoService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
//            Long tno = Long.valueOf(req.getParameter("tno"));
            Long tno = Long.parseLong(req.getParameter("tno"));

            TodoDTO todoDTO =  todoService.get(tno);

            //모델 담기
            req.setAttribute("dto",todoDTO );

            //쿠키 찾기
            Cookie viewTodoCookie = findCookie(req.getCookies(), "viewTodos");
            String todoListStr = viewTodoCookie.getValue();
            boolean exist = false;

            if(todoListStr != null && todoListStr.indexOf(tno+"-") >= 0){
                exist = true;
            }

            log.info("exist : "+ exist);

            if(!exist){
                todoListStr += tno+"-";
                viewTodoCookie.setValue(todoListStr);
                viewTodoCookie.setPath("/");
                viewTodoCookie.setMaxAge(60*60*24);
                resp.addCookie(viewTodoCookie);
            }


            req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req,resp);


        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServletException("read error");
        }
    }

    //쿠키 모음들 (Cookie[] cookies)로 부터 cookieName에 해당하는 이름의 쿠키가 있는지를 찾는 메소드
    private Cookie findCookie(Cookie[] cookies, String cookieName){

        Cookie targetCookie = null;

        if(cookies!=null && cookies.length>0){
            for(Cookie ck : cookies){
                if(ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                }
            }
        }

        //만약 타겟 쿠키를 못찾으면 내가 새로 만들어 줌
        if(targetCookie ==null){
            targetCookie = new Cookie(cookieName,"");
            targetCookie.setPath("/");
            targetCookie.setMaxAge(60*60*24); //24시간 유지
        }

        return targetCookie;

    }
}
