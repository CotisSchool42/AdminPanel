package ru.controllers;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.models.Client;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.service.ClientsService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * addAttribute - В ваш шаблонизатор вы передаете model в которой находятся данные необходимые для отображения
 * на веб-страничке. model.addAttribute("some text", someObject); это добавление в передаваемую модель данных,
 * в вашем случае someObject, которые потом будут доступны по ключу который вы указываете как параметр String,
 * в вашем случае это "some text"
 */

@Controller
@RequestMapping
public class ClientsController {
    private final ClientsService clientsService;


    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @PatchMapping({"/index/checkbox/{id}"})
    public String checkbox(@PathVariable("id") int id) {
        this.clientsService.checkbox(id);
        return "redirect:/index";
    }

    @DeleteMapping({"/index/{id}"})
    public String delete(@PathVariable("id") int id) {
        this.clientsService.delete(id);
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String listClients(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(12);

        Page<Client> bookPage = clientsService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("pageSize", pageSize);
        }
        return "clients/index";
    }

    @GetMapping("/index/{id}/edit")
    public String editClient(@PathVariable("id") int id, Model model) {
        model.addAttribute("editClients", clientsService.editClient(id));
        return "clients/edit";
    }

    @PatchMapping("/index/{id}")
    public String updateClient(@ModelAttribute("editClient") Client client,
                               @PathVariable("id") int id) {
        clientsService.updateClient(id, client);
        return "redirect:{id}/edit";
    }
}