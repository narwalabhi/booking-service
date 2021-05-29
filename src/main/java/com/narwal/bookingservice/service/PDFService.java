package com.narwal.bookingservice.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.narwal.bookingservice.model.Passenger;
import com.narwal.bookingservice.model.Station;
import com.narwal.bookingservice.model.Ticket;
import com.narwal.bookingservice.model.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Service
public class PDFService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${urls.get-station}")
    String getStationUrl;

    public String getClass(String className) {
        switch (className) {
            case "FAC":
                return "1AC";
            case "SAC":
                return "2AC";
            case "TAC":
                return "3AC";
            default:
                return className;
        }
    }

    public void generatePdf(Ticket ticket, Train train) {
        Document document = new Document();

        Station fromStation = restTemplate.getForObject(getStationUrl + "/" + train.getFromStationCode(), Station.class);
        Station toStation = restTemplate.getForObject(getStationUrl + "/" + train.getToStationCode(), Station.class);


        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream("table.pdf"));
            document.open();

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 5f});


            Image logo = Image.getInstance("C:\\Users\\anarw\\OneDrive\\Desktop\\CG-Assignments\\RabbitMQ-Assingments\\booking-service\\src\\main\\resources\\static\\logo.png");
            logo.scaleAbsolute(80f, 80f);

            PdfPCell imgCell = new PdfPCell();
            imgCell.addElement(logo);
            imgCell.setBorder(Rectangle.NO_BORDER);


            PdfPCell textCell = new PdfPCell();
            textCell.addElement(new Paragraph("AN RAILWAYS e-Ticketing Service\nElectronic Reservation Slip"));
            textCell.setBorder(Rectangle.NO_BORDER);
            textCell.setHorizontalAlignment(Rectangle.LEFT);

            table.addCell(imgCell);
            table.addCell(textCell);

            PdfPTable journeyTable = new PdfPTable(4);
            journeyTable.setWidthPercentage(100);
            journeyTable.setSpacingBefore(20f);
            journeyTable.setSpacingAfter(20f);


            float[] columnWidths = {1f, 1f, 1f, 1f};
            journeyTable.setWidths(columnWidths);

            PdfPCell cell1 = new PdfPCell(new Paragraph("Train Name & NO : " + train.getName()));
            cell1.setBorderColor(BaseColor.BLUE);
            cell1.setPadding(10);
            cell1.setColspan(2);
            cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);


            PdfPCell cell2 = new PdfPCell(new Paragraph("PNR: " + ticket.getPNR()));
            cell2.setBorderColor(BaseColor.BLUE);
            cell2.setPadding(10);
            cell2.setColspan(2);
            cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell1Middle = new PdfPCell(new Paragraph("From: " + fromStation.getName()));
            cell1Middle.setBorderColor(BaseColor.BLUE);
            cell1Middle.setPadding(10);
            cell1Middle.setColspan(2);
            cell1Middle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell1Middle.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell2Middle = new PdfPCell(new Paragraph("To: " + toStation.getName()));
            cell2Middle.setBorderColor(BaseColor.BLUE);
            cell2Middle.setPadding(10);
            cell2Middle.setColspan(2);
            cell2Middle.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell2Middle.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell3Middle = new PdfPCell(new Paragraph("Class: " + getClass(String.valueOf(ticket.getSeats().keySet().toArray()[0]))));
            cell3Middle.setBorderColor(BaseColor.BLUE);
            cell3Middle.setPadding(10);
            cell3Middle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell3Middle.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell4Middle = new PdfPCell(new Paragraph("DOJ: " + ticket.getJourneyDate()));
            cell4Middle.setBorderColor(BaseColor.BLUE);
            cell4Middle.setPadding(10);
            cell4Middle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell4Middle.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell5Middle = new PdfPCell(new Paragraph("Arrival: " + train.getArrival()));
            cell5Middle.setBorderColor(BaseColor.BLUE);
            cell5Middle.setPadding(10);
            cell5Middle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell5Middle.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell cell6Middle = new PdfPCell(new Paragraph("Duration: " + getDuration(train.getDurationHrs(), train
            .getDurationMns())));
            cell6Middle.setBorderColor(BaseColor.BLUE);
            cell6Middle.setPadding(10);
            cell6Middle.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell6Middle.setVerticalAlignment(Element.ALIGN_MIDDLE);

            journeyTable.addCell(cell1);
            journeyTable.addCell(cell2);
            journeyTable.addCell(cell1Middle);
            journeyTable.addCell(cell2Middle);
            journeyTable.addCell(cell3Middle);
            journeyTable.addCell(cell4Middle);
            journeyTable.addCell(cell5Middle);
            journeyTable.addCell(cell6Middle);


            PdfPTable passengerTable = new PdfPTable(5);
            passengerTable.setWidthPercentage(100);
            passengerTable.setSpacingBefore(20f);
            passengerTable.setSpacingAfter(20f);


            float[] columnWidthsPassengerTable = {.5f, 1.5f, .5f, 1f, 1.5f};
            passengerTable.setWidths(columnWidthsPassengerTable);

            PdfPCell snoCell = new PdfPCell(new Paragraph("SNO"));
            snoCell.setBorderColor(BaseColor.BLUE);
            snoCell.setPadding(10);
            snoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            snoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell nameCell = new PdfPCell(new Paragraph("Name"));
            nameCell.setBorderColor(BaseColor.BLUE);
            nameCell.setPadding(10);
            nameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell ageCell = new PdfPCell(new Paragraph("Age"));
            ageCell.setBorderColor(BaseColor.BLUE);
            ageCell.setPadding(10);
            ageCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            ageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell genderCell = new PdfPCell(new Paragraph("Gender"));
            genderCell.setBorderColor(BaseColor.BLUE);
            genderCell.setPadding(10);
            genderCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            genderCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            PdfPCell seatCell = new PdfPCell(new Paragraph("Booking Status/Seat No"));
            seatCell.setBorderColor(BaseColor.BLUE);
            seatCell.setPadding(10);
            seatCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            seatCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            passengerTable.addCell(snoCell);
            passengerTable.addCell(nameCell);
            passengerTable.addCell(ageCell);
            passengerTable.addCell(genderCell);
            passengerTable.addCell(seatCell);

            List<Passenger> passengerList = ticket.getPassengers();


            for (int i = 0; i < passengerList.size(); i++){

                Passenger passenger = passengerList.get(i);

                PdfPCell snoDataCell = new PdfPCell(new Paragraph(String.valueOf(i+1)));
                snoDataCell.setBorderColor(BaseColor.BLUE);
                snoDataCell.setPadding(10);
                snoDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                snoDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell nameDataCell = new PdfPCell(new Paragraph(passenger.getName()));
                nameDataCell.setBorderColor(BaseColor.BLUE);
                nameDataCell.setPadding(10);
                nameDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nameDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell ageDataCell = new PdfPCell(new Paragraph(String.valueOf(passenger.getAge())));
                ageDataCell.setBorderColor(BaseColor.BLUE);
                ageDataCell.setPadding(10);
                ageDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                ageDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell genderDataCell = new PdfPCell(new Paragraph(passenger.getGender()));
                genderDataCell.setBorderColor(BaseColor.BLUE);
                genderDataCell.setPadding(10);
                genderDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                genderDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                PdfPCell seatDataCell = new PdfPCell(new Paragraph(getSeatText(ticket.getSeats(), ticket.getStatus() ,i)));
                seatDataCell.setBorderColor(BaseColor.BLUE);
                seatDataCell.setPadding(10);
                seatDataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                seatDataCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                passengerTable.addCell(snoDataCell);
                passengerTable.addCell(nameDataCell);
                passengerTable.addCell(ageDataCell);
                passengerTable.addCell(genderDataCell);
                passengerTable.addCell(seatDataCell);
            }




            document.add(table);
            document.add(new Paragraph("Ticket"));
            document.add(journeyTable);
            document.add(new Paragraph("Passengers"));
            document.add(passengerTable);


            document.close();
            pdfWriter.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getSeatText(HashMap<String, List<Integer>> seats, String status, int index) {
        String classCode = String.valueOf(seats.keySet().toArray()[0]);
        return status.toUpperCase() + "/" + getClass(classCode) + " " + seats.get(classCode).get(index);
    }

    private String getDuration(int durationHrs, int durationMns) {
        return durationHrs + "Hrs " + durationMns + "Mns";
    }

}
