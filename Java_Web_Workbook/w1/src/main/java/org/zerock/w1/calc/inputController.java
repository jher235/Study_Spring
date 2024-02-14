package org.zerock.w1.calc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//서블릿에 온 리퀘스트를 다른 곳으로 전달, 배포하는 객체
@WebServlet(name = "inputController", urlPatterns = "/calc/input")  //처리해야하는 경로 지정/calc/input
public class inputController extends HttpServlet {

    @Override   //get방식으로 들어오는 요청에 대해서만 처리
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("InputController...doGet...");

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/calc/input.jsp"); ///WEB-INF/calc/input.jsp목적지로 가는 중간 경로

        dispatcher.forward(req,resp);
    }



}

