package org.zerock.w1.todo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.zerock.w1.todo.dto.TodoDTO;
import org.zerock.w1.todo.service.TodoService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "todoListController", urlPatterns = "/todo/list")
public class TodoListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("/todo/list");

        List<TodoDTO> dtoList = TodoService.INSTANCE.getList(); //서비스에서 리스트를 받아옴

        req.setAttribute("list", dtoList);  //key("list"), value(dtoList)로 HttpServletRequest에 데이터를 보관하도록 하여 JSP에서 사용할 수 있게끔 함

        req.getRequestDispatcher("/WEB-INF/todo/list.jsp").forward(req,resp);
    }
}
