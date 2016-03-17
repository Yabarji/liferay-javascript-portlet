package com.hannikkala.poc.rest.controller;

import com.hannikkala.poc.rest.domain.Todo;
import com.hannikkala.poc.rest.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 25/02/16
 * Time: 12:36
 */
@Controller
@RequestMapping("/api/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Todo> listTodos() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return todoService.list(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Todo create(@RequestBody Todo todo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        todo.setUsername(username);
        return todoService.create(todo);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public Todo update(@RequestBody Todo todo) {
        return todoService.update(todo);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        todoService.delete(id);
    }
}
