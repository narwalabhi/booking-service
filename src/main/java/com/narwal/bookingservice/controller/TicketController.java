package com.narwal.bookingservice.controller;

import com.narwal.bookingservice.exception.ApiRequestException;
import com.narwal.bookingservice.exception.TicketNotFoundException;
import com.narwal.bookingservice.model.*;
import com.narwal.bookingservice.service.TicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/booking")
public class TicketController {

    @Value("${status.booked}")
    private String bookedStatus;

    @Value("${status.cancel}")
    private String cancelledStatus;

    @Value("${urls.get-trip-url}")
    private String getTripUrl;

    @Value("${urls.post-trip-schedule-url}")
    private String addTripSchedule;

    @Value("${urls.put-trip-schedule-url}")
    private String updateTripScheduleUrl;

    @Value("${urls.get-trip-schedule-url}")
    private String getTripScheduleUrl;

    @Value("${urls.get-train-url}")
    private String getTrainUrl;

    @Value("${codes.first-ac}")
    private String firstAcCode;

    @Value("${codes.second-ac}")
    private String secondAcCode;

    @Value(("${codes.third-ac}"))
    private String thirdAcSeats;

    @Value("${codes.first-class}")
    private String firstClass;

    @Value("${codes.chair-car}")
    private String chairCar;

    @Value("${codes.sleeper}")
    private String sleeper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TicketsService ticketsService;

    //TODO Add dynamic userID
    //TODO move the code into a service
    //TODO Add PNR Logic
    @PostMapping("/book")
    public ResponseEntity<Ticket> createTicket(@RequestBody BookTicketRequest bookTicketRequest) throws ParseException {
        System.out.println(getTripUrl + "/" + bookTicketRequest.getTripId());
        System.out.println(bookTicketRequest);
        Trip trip = restTemplate.getForObject(getTripUrl + "/" + bookTicketRequest.getTripId(), Trip.class);
        System.out.println(trip);
        if (trip != null) {
            TripSchedule tripScheduleData = restTemplate.getForObject(getTripScheduleUrl + "/" + bookTicketRequest.getTripId() + "/" + bookTicketRequest.getTripDate(), TripSchedule.class);
            System.out.println("TripSchedule " + tripScheduleData);
            Train train = restTemplate.getForObject(getTrainUrl + "/" + bookTicketRequest.getTrainId(), Train.class);
            if (tripScheduleData == null) {
                System.out.println("Train " + train);
                if (train != null) {
                    tripScheduleData = new TripSchedule();
                    tripScheduleData.setTripId(bookTicketRequest.getTripId());
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    tripScheduleData.setTripDate(LocalDate.parse(bookTicketRequest.getTripDate(), dateTimeFormatter));
                    System.out.println(tripScheduleData.getTripDate());
                    tripScheduleData.setFirstAcAvailableSeats(train.getFirstAcSeats());
                    tripScheduleData.setSecondAcAvailableSeats(train.getSecondAcSeats());
                    tripScheduleData.setThirdAcAvailableSeats(train.getThirdAcSeats());
                    tripScheduleData.setChairCarAcAvailableSeats(train.getChairCarSeats());
                    tripScheduleData.setFirstClassAcAvailableSeats(train.getFirstClassSeats());
                    tripScheduleData.setSleeperAvailableSeats(train.getSleeperSeats());
                    tripScheduleData = restTemplate.exchange(addTripSchedule, HttpMethod.POST, new HttpEntity<TripSchedule>(tripScheduleData), TripSchedule.class).getBody();
                } else throw new ApiRequestException("Invalid trainId");
            }
            Ticket ticket = new Ticket();
            HashMap<String, Integer> seatsReq = bookTicketRequest.getSeats();
            HashMap<String, List<Integer>> assignedSeats = new HashMap<>();
            if (seatsReq.containsKey(firstAcCode)) {
                if (tripScheduleData.getFirstAcAvailableSeats() >= seatsReq.get(firstAcCode)) {
                    for (int i = 1; i <= seatsReq.get(firstAcCode); i++) {
                        if (assignedSeats.containsKey(firstAcCode)) {
                            assignedSeats.get(firstAcCode).add(train.getFirstAcSeats() - tripScheduleData.getFirstAcAvailableSeats() + i);
                        } else {
                            assignedSeats.put(firstAcCode, new ArrayList<>(List.of(train.getFirstAcSeats() - tripScheduleData.getFirstAcAvailableSeats() + i)));
                        }
                    }
                    tripScheduleData.setFirstAcAvailableSeats(tripScheduleData.getFirstAcAvailableSeats() - seatsReq.get(firstAcCode));
                }
            }
            if (seatsReq.containsKey(secondAcCode)) {
                if (tripScheduleData.getSecondAcAvailableSeats() >= seatsReq.get(secondAcCode)) {
                    for (int i = 1; i <= seatsReq.get(secondAcCode); i++) {
                        if (assignedSeats.containsKey(secondAcCode)) {
                            assignedSeats.get(secondAcCode).add(train.getSecondAcSeats() - tripScheduleData.getSecondAcAvailableSeats() + i);
                        } else {
                            assignedSeats.put(secondAcCode, new ArrayList<>(List.of(train.getSecondAcSeats() - tripScheduleData.getSecondAcAvailableSeats() + i)));
                        }
                    }
                    tripScheduleData.setSecondAcAvailableSeats(tripScheduleData.getSecondAcAvailableSeats() - seatsReq.get(secondAcCode));
                }
            }
            if (seatsReq.containsKey(thirdAcSeats)) {
                if (tripScheduleData.getThirdAcAvailableSeats() >= seatsReq.get(thirdAcSeats)) {
                    for (int i = 1; i <= seatsReq.get(thirdAcSeats); i++) {
                        if (assignedSeats.containsKey(thirdAcSeats)) {
                            assignedSeats.get(thirdAcSeats).add(train.getThirdAcSeats() - tripScheduleData.getThirdAcAvailableSeats() + i);
                        } else {
                            assignedSeats.put(thirdAcSeats, new ArrayList<>(List.of(train.getThirdAcSeats() - tripScheduleData.getThirdAcAvailableSeats() + i)));
                        }
                    }
                    tripScheduleData.setThirdAcAvailableSeats(tripScheduleData.getThirdAcAvailableSeats() - seatsReq.get(thirdAcSeats));
                }
            }
            if (seatsReq.containsKey(firstClass)) {
                if (tripScheduleData.getFirstClassAcAvailableSeats() >= seatsReq.get(firstClass)) {
                    for (int i = 1; i <= seatsReq.get(firstClass); i++) {
                        if (assignedSeats.containsKey(firstClass)) {
                            assignedSeats.get(firstClass).add(train.getFirstClassSeats() - tripScheduleData.getFirstClassAcAvailableSeats() + i);
                        } else {
                            assignedSeats.put(firstClass, new ArrayList<>(List.of(train.getFirstClassSeats() - tripScheduleData.getFirstClassAcAvailableSeats() + i)));
                        }
                    }
                    tripScheduleData.setFirstClassAcAvailableSeats(tripScheduleData.getFirstClassAcAvailableSeats() - seatsReq.get(firstClass));
                }
            }
            if (seatsReq.containsKey(chairCar)) {
                if (tripScheduleData.getChairCarAcAvailableSeats() >= seatsReq.get(chairCar)) {
                    for (int i = 1; i <= seatsReq.get(chairCar); i++) {
                        if (assignedSeats.containsKey(chairCar)) {
                            assignedSeats.get(chairCar).add(train.getChairCarSeats() - tripScheduleData.getChairCarAcAvailableSeats() + i);
                        } else {
                            assignedSeats.put(chairCar, new ArrayList<>(List.of(train.getChairCarSeats() - tripScheduleData.getChairCarAcAvailableSeats() + i)));
                        }
                    }
                    tripScheduleData.setChairCarAcAvailableSeats(tripScheduleData.getChairCarAcAvailableSeats() - seatsReq.get(chairCar));
                }
            }
            if (seatsReq.containsKey(sleeper)) {
                if (tripScheduleData.getSleeperAvailableSeats() >= seatsReq.get(sleeper)) {
                    for (int i = 1; i <= seatsReq.get(sleeper); i++) {
                        if (assignedSeats.containsKey(sleeper)) {
                            assignedSeats.get(sleeper).add(train.getSleeperSeats() - tripScheduleData.getSleeperAvailableSeats() + i);
                        } else {
                            assignedSeats.put(sleeper, Arrays.asList(train.getSleeperSeats() - tripScheduleData.getSleeperAvailableSeats() + i));
                        }
                    }
                    tripScheduleData.setSleeperAvailableSeats(tripScheduleData.getSleeperAvailableSeats() - seatsReq.get(sleeper));
                }
            }
            ticket.setCancellable(false);
            ticket.setJourneyDate(tripScheduleData.getTripDate());
            ticket.setTripScheduleId(tripScheduleData.getId());
            ticket.setUserId("anarwal@gmail.com");
            // PNR = Tain No. + JourneyDate + first seat number + class Code
            String PNR = train.getNumber()+tripScheduleData.getTripDate().toString().replaceAll("-","")+assignedSeats.keySet().toArray()[0] + assignedSeats.values().stream().findFirst().get().get(0);
            System.out.println(PNR);
            ticket.setPNR(PNR);
            ticket.setSeats(assignedSeats);
            ticket.setPassengers(bookTicketRequest.getPassengers());
            ticket.setStatus(bookedStatus);
            System.out.println(tripScheduleData);
            restTemplate.exchange(updateTripScheduleUrl + "/" + tripScheduleData.getId(), HttpMethod.PUT, new HttpEntity<TripSchedule>(tripScheduleData), TripSchedule.class);
            return ResponseEntity.ok(ticketsService.createTicket(ticket));
        } else throw new ApiRequestException("Invalid tripID");
    }

    @PutMapping("/update/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable String ticketId, @RequestBody Ticket ticket) {
        Optional<Ticket> ticketOptional = ticketsService.updateTicket(ticketId, ticket);
        if (ticketOptional.isPresent()){
            return ResponseEntity.ok(ticketOptional.get());
        }
        throw new TicketNotFoundException("Ticket with ticketId " + ticketId + " was not found.");
    }


    @PutMapping("/cancel/{PNR}")
    public ResponseEntity<Ticket> cancelTicket(@PathVariable String PNR) {
        Optional<Ticket> ticket = ticketsService.getTicketByPNR(PNR);
        if (ticket.isPresent()) {
            Ticket ticketData = ticket.get();
            HashMap<String, List<Integer>> seats = ticketData.getSeats();
            System.out.println(seats);
            TripSchedule tripSchedule = restTemplate.getForObject(getTripScheduleUrl + "/" + ticketData.getTripScheduleId(), TripSchedule.class);
            if (tripSchedule != null) {
                if (seats.containsKey(firstAcCode)) {
                    System.out.println(firstAcCode);
                    tripSchedule.setFirstAcAvailableSeats(tripSchedule.getFirstAcAvailableSeats() + seats.get(firstAcCode).size());
                }
                if (seats.containsKey(secondAcCode)) {
                    tripSchedule.setSecondAcAvailableSeats(tripSchedule.getSecondAcAvailableSeats() + seats.get(secondAcCode).size());
                }
                if (seats.containsKey(thirdAcSeats)) {
                    tripSchedule.setThirdAcAvailableSeats(tripSchedule.getThirdAcAvailableSeats() + seats.get(thirdAcSeats).size());
                }
                if (seats.containsKey(firstClass)) {
                    tripSchedule.setFirstClassAcAvailableSeats(tripSchedule.getFirstClassAcAvailableSeats() + seats.get(firstClass).size());
                }
                if (seats.containsKey(chairCar)) {
                    tripSchedule.setChairCarAcAvailableSeats(tripSchedule.getChairCarAcAvailableSeats() + seats.get(chairCar).size());
                }
                if (seats.containsKey(sleeper)) {
                    tripSchedule.setSleeperAvailableSeats(tripSchedule.getSleeperAvailableSeats() + seats.get(sleeper).size());
                }
                restTemplate.exchange(updateTripScheduleUrl + "/" + tripSchedule.getId(), HttpMethod.PUT, new HttpEntity<TripSchedule>(tripSchedule), TripSchedule.class);
            }
            System.out.println(tripSchedule);
            ticketData.setStatus(cancelledStatus);
            System.out.println(ticketData);
            return ResponseEntity.ok(ticketsService.updateTicketByPNR(PNR, ticketData));
        }else throw new TicketNotFoundException("Ticket with PNR " + PNR + " was not found");
    }

    @GetMapping("/get/{PNR}")
    public ResponseEntity<Ticket> getTicket(@PathVariable String PNR) {
        Optional<Ticket> ticket = ticketsService.getTicketByPNR(PNR);
        if(ticket.isPresent()){
            return ResponseEntity.ok(ticket.get());
        }else throw new TicketNotFoundException("Ticket with PNR " + PNR + " was not found");
    }

    //TODO Add update query for all tickets with tripScheduleId param
    //TODO Change return type from list to a POJO containing List<Ticket> for further changes
    @PutMapping("/cancel-all/{tripScheduleID}")
    public List<Ticket> cancelAllTicketsByTripScheduleId(@PathVariable String tripScheduleID) {
        return ticketsService.getTicketsByTripScheduleId(tripScheduleID);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable String id){
        Optional<Ticket> ticket = ticketsService.getTicketByTicketId(id);
        if (ticket.isPresent()){
            return ResponseEntity.ok(ticket.get());
        }else throw new TicketNotFoundException("Ticket with id " + id + " was not found");
    }

}
