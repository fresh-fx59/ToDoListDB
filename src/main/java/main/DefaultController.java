package main;

import main.dao.ToDoDao;
import main.model.ToDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
public class DefaultController {

    @Autowired
    ToDoDao toDoDao;

    @RequestMapping("/")
    public String index(Model model) {
        int toDoCount;
        List<ToDo> toDos = new ArrayList<>();
        if (isNull(toDoDao.getAll())) {
            toDoCount = 0;
            toDos.add(new ToDo("There is no ToDos"));
        } else {
            toDoCount = toDoDao.getAll().size();
            toDos.addAll(toDoDao.getAll());
        }
        model.addAttribute("toDoCount", toDoCount);
        model.addAttribute("toDos", toDos);
        return "index";
    }
}
