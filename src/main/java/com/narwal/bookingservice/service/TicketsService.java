package com.narwal.bookingservice.service;

import com.narwal.bookingservice.model.Ticket;
import com.narwal.bookingservice.repository.TicketsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class TicketsService {

    @Autowired
    private TicketsRepo ticketsRepo;

    public Ticket createTicket(Ticket ticket){
       return ticketsRepo.save(ticket);
    }

    public Ticket updateTicket(String ticketId, Ticket ticket){
        Optional<Ticket> ticketData = ticketsRepo.findByTicketId(ticketId);
        if(ticketData.isPresent()){
            ticketsRepo.save(ticket);
        }
        return ticket;
    }

    public void deleteTicket(String ticketId){
        ticketsRepo.deleteByTicketId(ticketId);
    }

    public Ticket getTicketByTicketId(String ticketId){
        Optional<Ticket> ticketData = ticketsRepo.findByTicketId(ticketId);
        return ticketData.orElse(null);
    }

}
