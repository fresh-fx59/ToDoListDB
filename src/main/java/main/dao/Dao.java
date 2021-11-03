package main.dao;

import main.model.ToDo;

import java.util.List;

public interface Dao<T> {

    boolean updateToDo(int id, String description, boolean isDone);

    T get(int id);

    List<T> getAll();

    int add(ToDo toDo);

    boolean delete(int id);

    boolean deleteAll();
}