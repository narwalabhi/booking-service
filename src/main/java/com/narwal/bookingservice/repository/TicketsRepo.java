package com.narwal.bookingservice.repository;

import com.narwal.bookingservice.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketsRepo extends MongoRepository<Ticket, String> {

    public Optional<Ticket> findByTicketId(String ticketId);

    public void deleteByTicketId(String ticketId);

    public void findTicketByPNR(String PNR);

}
