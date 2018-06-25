package com.rsm.configuration;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.rsm.report.Report;
import com.rsm.user.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmailReportAssigned(Report report) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        UserDetails employee = report.getEmployee().getDetails();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(report.getCustomer().getDetails().getEmail());
            helper.setSubject("Status zgłoszenia o tytule " + report.getTitle() + " zmieniony");
            helper.setText("Poniższe zgłoszenie zostało przydzielone do diagnozy technikowi. Wraz z końcem diagnozy " +
                    "otrzymają Państwo kolejnego maila z informacją i fakturą.\nTytuł: " + report.getTitle() +
                    "\nOpis:" + report.getDescription() + "\nDane kontaktowe technika: \nImie: " + employee.getFirstName() +
            "\nNazwisko: " + employee.getLastName() + "\nTelefon: " + employee.getPhone() + "\nEmail: " + employee.getEmail());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }

    public void sendEmailReportFinished(Report report) {
        MimeMessage mail = javaMailSender.createMimeMessage();
        UserDetails employee = report.getEmployee().getDetails();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(report.getCustomer().getDetails().getEmail());
            helper.setSubject("Zgłoszenie o tytule " + report.getTitle() + " zostało rozpatrzone");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 10, 10, 10, 10);
            try {
                PdfWriter.getInstance(document, baos);
                document.open();

                PdfPTable table = new PdfPTable(2);
                table.setWidths(new float[] { 1, 4 });
                table.addCell(new PdfPCell(new Paragraph("Tytul zgloszenia")));
                table.addCell(new PdfPCell(new Paragraph(report.getTitle())));
                table.addCell(new PdfPCell(new Paragraph("Opis zgloszenia")));
                table.addCell(new PdfPCell(new Paragraph(report.getDescription())));
                table.addCell(new PdfPCell(new Paragraph("Diagnoza")));
                table.addCell(new PdfPCell(new Paragraph(report.getDiagnosis())));
                table.addCell(new PdfPCell(new Paragraph("Pracownik")));
                table.addCell(new PdfPCell(new Paragraph(report.getEmployee().getDetails().getFirstName() + " " +
                report.getEmployee().getDetails().getLastName())));
                table.addCell(new PdfPCell(new Paragraph("Kontakt telefoniczny")));
                table.addCell(new PdfPCell(new Paragraph(report.getEmployee().getDetails().getPhone())));
                table.addCell(new PdfPCell(new Paragraph("Wycena")));
                table.addCell(new PdfPCell(new Paragraph(report.getPricing() + " zlotych")));
                table.addCell(new PdfPCell(new Paragraph("Termin zaplaty")));
                LocalDate dateNextTwoWeeks = LocalDate.now().plus(2, ChronoUnit.WEEKS);
                table.addCell(new PdfPCell(new Paragraph(dateNextTwoWeeks.toString())));

                document.add(table);
                document.close();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("Diagnoza dla poniższego zgłoszenia została zakończona. " +
                    "W załączniku dostępna jest faktura, którą należy uregulować w ciągu dwóch tygodni.\nTytuł: " + report.getTitle() +
                    "\nOpis:" + report.getDescription() + "\n\nDiagnoza:\n" + report.getDiagnosis() + "\nDane kontaktowe technika: \nImie: " + employee.getFirstName() +
                    "\nNazwisko: " + employee.getLastName() + "\nTelefon: " + employee.getPhone() + "\nEmail: " + employee.getEmail());

            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            messageBodyPart2.setDataHandler(new DataHandler(new ByteArrayDataSource(baos.toByteArray(),"application/pdf")));
            messageBodyPart2.setFileName("Diagnoza-"+ report.getTitle() + ".pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            multipart.addBodyPart(messageBodyPart2);

            mail.setContent(multipart);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(mail);
    }
}
