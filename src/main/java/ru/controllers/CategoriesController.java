package ru.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.dao.CategoriesDao;
import ru.models.Category;

import javax.validation.Valid;

@RequestMapping("/categories")
@Controller
public class CategoriesController {
    private CategoriesDao categoriesDAO;

    @Autowired
    public void setCategoriesDAO(CategoriesDao categoriesDAO)
    {
        this.categoriesDAO = categoriesDAO;
    }

    @GetMapping
    public  String index(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoriesDAO.index());
        return "categories/index";
    }

    /** modelAttribute делает три вещи: 1) Создание нового объекта.
     *                                  2) Добавление значений в поля объекта из HTML формы
     *                                  3) Добавление созданного объекта в модель.
     *
     * В примере ниже, мы принимаем созданную ранее модель, записываем в базу данных полученную информацию из
     * представления и делаем редирект страницы для отображения измененных данных*/

    @PostMapping
    public String addCategory(@ModelAttribute("category") @Valid Category category,
                              BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("categories", categoriesDAO.index());
            return "categories/index";
        }
        categoriesDAO.addCategory(category);
        return "redirect:categories";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") int id) {
        categoriesDAO.deleteCategory(id);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editCategory(Model model, @PathVariable("id") int id) {
        model.addAttribute("editCategory", categoriesDAO.editCategory(id));
        return "categories/edit";
    }

    @PatchMapping("/{id}")
    public String updateCategory(@ModelAttribute("editCategory") Category updateCategory,
                                 @PathVariable("id") int id) {
        categoriesDAO.updateCategory(id, updateCategory);
        return "redirect:{id}/edit";
    }
}