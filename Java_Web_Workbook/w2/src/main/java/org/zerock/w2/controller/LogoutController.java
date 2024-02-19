package org.zerock.w2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@WebServlet("/logout")
@Log4j2
public class LogoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("log out .....");

        HttpSession session = req.getSession();

        session.removeAttribute("loginInfo");
        session.invalidate();   //현재 세션을 무효화

        resp.sendRedirect("/");


    }
}
