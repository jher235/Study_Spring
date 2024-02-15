package org.zerock.w1.calc;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "calcController", urlPatterns = "/calc/makeResult")     //url패턴을 /calc/makeResult로 설정해줌
public class CalcController extends HttpServlet {

    //브라우저에서 post방식으로 호출하는 경우에만 호출이 가능하게 된다.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String num1 = req.getParameter("num1");
        String num2 = req.getParameter("num2");

        System.out.printf(" num1: %s", num1);
        System.out.printf(" num2: %s", num2);
        resp.sendRedirect("/index");

    }
}
