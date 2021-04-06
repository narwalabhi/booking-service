package com.narwal.bookingservice.controller;

import com.narwal.bookingservice.model.Ticket;
import com.narwal.bookingservice.service.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketsService ticketsService;

    @PostMapping("/add")
    public Ticket createTicket(@RequestBody Ticket ticket){
        return ticketsService.createTicket(ticket);
    }

    @PutMapping("/update/{ticketId}")
    public Ticket updateTicket(@PathVariable String ticketId, @RequestBody Ticket ticket){
        return ticketsService.updateTicket(ticketId, ticket);
    }

    @DeleteMapping("/delete/{ticketId}")
    public void deleteTicket(@PathVariable String ticketId){
        ticketsService.deleteTicket(ticketId);
    }

    @GetMapping("/get/{ticketId}")
    public Ticket getTicket(@PathVariable String ticketId){
        return ticketsService.getTicketByTicketId(ticketId);
    }

}
