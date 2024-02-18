package org.zerock.jdbcex.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.jdbcex.dao.TodoDAO;
import org.zerock.jdbcex.domain.TodoVO;
import org.zerock.jdbcex.dto.TodoDTO;
import org.zerock.jdbcex.util.MapperUtil;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
public enum TodoService {
    INSTANCE;

    private TodoDAO todoDAO;
    private ModelMapper modelMapper;

    TodoService(){
        todoDAO = new TodoDAO();
        modelMapper = MapperUtil.INSTANCE.get();
    }

    public void register(TodoDTO todoDTO) throws Exception {
        TodoVO todoVO = modelMapper.map(todoDTO, TodoVO.class);
//        System.out.println("todoVO: " + todoVO);
        log.info(todoVO);
        todoDAO.insert(todoVO);

    }

    public List<TodoDTO> listAll() throws Exception {
        List<TodoVO> volist = todoDAO.selectAll();

        log.info("volist..................");
        log.info(volist);

        List<TodoDTO> dtolist = volist.stream()
                .map(vo -> modelMapper.map(vo, TodoDTO.class))
                .collect(Collectors.toList());

        return dtolist;

     }
}