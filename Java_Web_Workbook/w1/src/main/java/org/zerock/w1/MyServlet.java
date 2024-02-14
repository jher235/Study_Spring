package org.zerock.w1;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="myServlet", urlPatterns = "/my")  //http://localhost:8080/my로 접속하면 hi가 출력됨.
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        resp.setContentType("text/html");//브라우저가 데이터를 어떻게 해석할 것인지

        PrintWriter writer = resp.getWriter();

        writer.println("<h1>hi</h1>");  //브라우저에 전달하는 내용

        writer.close();
    }
}
