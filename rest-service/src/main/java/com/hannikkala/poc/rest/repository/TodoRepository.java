package com.hannikkala.poc.rest.repository;

import com.hannikkala.poc.rest.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: bleed
 * Date: 25/02/16
 * Time: 14:54
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUsername(String username);
}
