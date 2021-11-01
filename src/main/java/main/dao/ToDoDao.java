package main.dao;

import main.model.ToDo;
import main.model.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ToDoDao implements Dao<ToDo> {

    @Autowired
    ToDoRepository toDoRepository;

    @Override
    public boolean updateToDo(int id, String description, boolean isDone) {
        if (toDoRepository.existsById(id)) {
            ToDo toDo = new ToDo();
            toDo.setId(id);
            toDo.setDescription(description);
            toDo.setIsDone(isDone);
            toDoRepository.save(toDo);
            return true;
        }
        return false;
    }

    @Override
    public ToDo get(int id) {
        if (!toDoRepository.existsById(id)) {
            return null;
        }
        return toDoRepository.findById(id).get();
    }

    @Override
    public boolean deleteAll() {
        toDoRepository.deleteAll();
        if (toDoRepository.count() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        if (toDoRepository.existsById(id)) {
            toDoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void add(ToDo toDo) {
        toDoRepository.save(toDo);
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> toDos = new ArrayList<>();
        Iterable<ToDo> toDoIterable = toDoRepository.findAll();
        for (ToDo toDo : toDoIterable) {
            toDos.add(toDo);
        }
        return toDos;
    }
}
