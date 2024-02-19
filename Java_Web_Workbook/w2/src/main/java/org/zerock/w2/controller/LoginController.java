package org.zerock.w2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;
import org.zerock.w2.dto.MemberDTO;
import org.zerock.w2.service.MemberService;

import java.io.IOException;

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

        try{
            MemberDTO memberDTO = MemberService.INSTANCE.login(mid, mpw);
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
