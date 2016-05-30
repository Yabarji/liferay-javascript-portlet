package com.hannikkala.poc.rest.service;

import com.hannikkala.poc.rest.domain.Todo;
import com.hannikkala.poc.rest.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 25/02/16
 * Time: 14:55
 */
@Transactional
@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> list(String username) {
        return todoRepository.findByUsername(username);
    }

    public Todo create(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Todo todo) {
        return todoRepository.save(todo);
    }

    public void delete(Long todo) {
        todoRepository.delete(todo);
    }
}
