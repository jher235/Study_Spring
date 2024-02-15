package org.zerock.w1.todo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.zerock.w1.todo.dto.TodoDTO;
import org.zerock.w1.todo.service.TodoService;

import java.io.IOException;

@WebServlet(name = "todoReadController", urlPatterns = "/todo/read")
public class TodoReadController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("/todo/read");

        // /todo/read?tno=123 이면 여기서 123을 가져오는 것. 문자열을 long으로 변환해서 저장
        Long tno = Long.parseLong(req.getParameter("tno"));

        TodoDTO dto = TodoService.INSTANCE.get(tno);

//        키를 "dto"로 저장해서 보내줌.
        req.setAttribute("dto", dto);

        req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req, resp);

    }
}
