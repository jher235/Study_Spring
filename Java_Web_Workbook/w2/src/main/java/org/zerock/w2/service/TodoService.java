package org.zerock.w2.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.zerock.w2.dao.TodoDAO;
import org.zerock.w2.domain.TodoVO;
import org.zerock.w2.dto.TodoDTO;
import org.zerock.w2.util.MapperUtil;

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

     public TodoDTO get(Long tno) throws Exception {
        log.info("tno : "+ tno);
        TodoVO todoVO = todoDAO.selectOne(tno);
        TodoDTO dto = modelMapper.map(todoVO, TodoDTO.class);
        return dto;
     }

     public void remove(Long tno) throws Exception {
        log.info("tno : " + tno);
        todoDAO.deleteOne(tno);
     }

     public void modify(TodoDTO todoDTO) throws Exception {
        log.info("todoDTO : " + todoDTO);
        TodoVO vo = modelMapper.map(todoDTO, TodoVO.class);
        todoDAO.updateOne(vo);

     }


}
