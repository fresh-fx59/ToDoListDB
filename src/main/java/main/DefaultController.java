package main;

import main.dao.ToDoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @Autowired
    ToDoDao toDoDao;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("toDoCount", toDoDao.getAll().size());
        model.addAttribute("toDos", toDoDao.getAll());
        return "index";
    }
}
