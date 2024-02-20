package org.zerock.w2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import lombok.extern.java.Log;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/login")
@Log
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login GET.......");

        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login Post.........");

        String mid = req.getParameter("mid");
        String mpw = req.getParameter("mpw");

        String auto = req.getParameter("auto");

        boolean rememberMe = auto != null && auto.equals("on"); //체크 박스에서 전송된 값이 on인지 확인



        try{
            MemberDTO memberDTO = MemberService.INSTANCE.login(mid, mpw);

            if(rememberMe){
                String uuid = UUID.randomUUID().toString();

                MemberService.INSTANCE.updateUuid(mid, uuid);
                memberDTO.setUuid(uuid);

                Cookie rememberCookie = new Cookie("remember-me", uuid);
                rememberCookie.setMaxAge(60*60*24*7);   //유효기간 일주일
                rememberCookie.setPath("/");

                resp.addCookie(rememberCookie);
            }

            HttpSession session = req.getSession();
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect("/todo/list");

        } catch (Exception e) {
            resp.sendRedirect("/login?result=error");
        }


//        String str = mid+mpw;
//
//        HttpSession session =  req.getSession();
//
//        session.setAttribute("loginInfo", str);
//
//        resp.sendRedirect("/todo/list");


    }
}
