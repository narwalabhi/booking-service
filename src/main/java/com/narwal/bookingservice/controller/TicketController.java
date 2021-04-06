package com.narwal.bookingservice.controller;

import com.narwal.bookingservice.model.*;
import com.narwal.bookingservice.service.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Value("${urls.get-trip-url}")
    private String getTripUrl;

    @Value("${urls.get-trip-schedule-url}")
    private String getTripScheduleUrl;

    @Value("${urls.get-train-url}")
    private String getTrainUrl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TicketsService ticketsService;

    @PostMapping("/book")
    public Ticket createTicket(@RequestBody BookTicketRequest bookTicketRequest){
        System.out.println(getTripUrl + "/" + bookTicketRequest.getTripId());
        Trip trip = restTemplate.getForObject(getTripUrl + "/" + bookTicketRequest.getTripId(), Trip.class);
        System.out.println(trip);
        if(trip != null){
            TripSchedule tripScheduleData = restTemplate.getForObject(getTripScheduleUrl + "/" + bookTicketRequest.getTripId(), TripSchedule.class);
            if(tripScheduleData == null){
                Train train = restTemplate.getForObject(getTrainUrl + "/" + bookTicketRequest.getTrainId(), Train.class);
                tripScheduleData = new TripSchedule();
                tripScheduleData.setTripId(bookTicketRequest.getTripId());
                tripScheduleData.setTripDate(bookTicketRequest.getTripDate());
                tripScheduleData.setFirstAcAvailableSeats(train.getFirstAcSeats());
                tripScheduleData.setSecondAcAvailableSeats(train.getSecondAcSeats());
                tripScheduleData.setThirdAcAvailableSeats(train.getThirdAcSeats());
                tripScheduleData.setChairCarAcAvailableSeats(train.getChairCarSeats());
                tripScheduleData.setFirstClassAcAvailableSeats(train.getFirstClassSeats());
                tripScheduleData.setSleeperAvailableSeats(train.getSleeperSeats());
            }
            Ticket ticket = new Ticket();
            tripScheduleData.setFirstAcAvailableSeats(tripScheduleData.getFirstAcAvailableSeats()- bookTicketRequest.getFirstAcSeats());
            tripScheduleData.setSecondAcAvailableSeats(tripScheduleData.getSecondAcAvailableSeats()- bookTicketRequest.getSecondAcSeats());
            tripScheduleData.setThirdAcAvailableSeats(tripScheduleData.getThirdAcAvailableSeats()-bookTicketRequest.getThirdAcSeats());
            tripScheduleData.setFirstClassAcAvailableSeats(tripScheduleData.getFirstClassAcAvailableSeats()-bookTicketRequest.getFirstAcSeats());
            tripScheduleData.setChairCarAcAvailableSeats(tripScheduleData.getChairCarAcAvailableSeats()- bookTicketRequest.getChairCarSeats());
            tripScheduleData.setSleeperAvailableSeats(tripScheduleData.getSleeperAvailableSeats()- bookTicketRequest.getSleeperSeats());
            ticket.setCancellable(false);
            ticket.setJourneyDate(tripScheduleData.getTripDate());
            ticket.setTripScheduleId(tripScheduleData.getId());
            ticket.setUserId("anarwal@gmail.com");
            ticket.setPNR("21312");
            ticket.setPassengers(bookTicketRequest.getPassengers());
            return ticketsService.createTicket(ticket);
        }
        return null;
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
