package main;

import main.dao.ToDoDao;
import main.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ToDoController {

    @Autowired
    ToDoDao toDoDao;

    //Создание дела
    //удаление дела
    //обновление дела
    //получение списка
    //удаление всего списка

    @GetMapping("/todos/")
    public List<ToDo> list() {
        return toDoDao.getAll();
    }

    @PostMapping("/todos/")
    public ResponseEntity<?> add(ToDo toDo) {
        toDoDao.add(toDo);
        return ResponseEntity.status(HttpStatus.CREATED).body("New ToDo successfully created!");
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {

        return toDoDao.delete(id) ? new ResponseEntity<>(HttpStatus.OK) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("ToDo number " +
                        id + " can't be deleted, because it doesn't exist.");
    }

    @DeleteMapping("/todos/")
    public ResponseEntity<?> deleteAll() {
        return toDoDao.deleteAll() ? new ResponseEntity<>("All ToDos was successfully deleted!",
                HttpStatus.OK) :
                ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                        "Can't delete all ToDos for unknown reason.");
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> get(@PathVariable int id) {
        return toDoDao.get(id) == null ?
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't get ToDo " +
                        id + " because it doesn't exist.") :
                new ResponseEntity<>(toDoDao.get(id), HttpStatus.OK);
    }

    @PutMapping("/todos/")
    public ResponseEntity<?> updateAll(List<ToDo> todos) {
        return null;
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateToDo(@PathVariable int id, String description, boolean isDone) {
        return toDoDao.updateToDo(id, description, isDone) ?
                new ResponseEntity<>("ToDo id = " + id + " was successfully updated!",
                HttpStatus.OK) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't UPDATE ToDo " +
                        id + " because it doesn't exist.");
    }
}
