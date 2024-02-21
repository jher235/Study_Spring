package org.zerock.springex.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.zerock.springex.dto.TodoDTO;

@Controller
@Log4j2
@RequestMapping("/todo")
public class TodoController {

    @RequestMapping("/list")
    public void list(){
        log.info("todo list.......");
    }

//    @RequestMapping(value = "/register", method = RequestMethod.GET)
//    public void register(){
//        log.info("todo register.......");
//    }

    @GetMapping("/register")
    public void registerGet(){
        log.info("GET todo register.......");
    }

    @PostMapping("/register")
    public void registerPost(TodoDTO dto){
        log.info("POST todo register.......");
        log.info("TodoDTO : "+ dto);
    }



}
