package BL;
import BL.Exceptions.MapApiHandlerException;
import BL.Exceptions.ReportGeneratorException;
import BL.Exceptions.TourListManagerException;
import BL.Exceptions.TourLogManagerException;
import DATATYPES.Tour;
import DATATYPES.TourLog;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.*;

public class ReportGenerator
{
    TourListHandler tourListHandler;
    TourLogHandler tourLogHandler;
    MapApiHandler httpHandler;
    Logger log;

    public ReportGenerator() throws ReportGeneratorException {
        try {
            tourListHandler = new TourListHandler();
            tourLogHandler = new TourLogHandler();
            httpHandler = new MapApiHandler();
            log = LogManager.getLogger(ReportGenerator.class);
        } catch (TourListManagerException | TourLogManagerException | MapApiHandlerException e) {
            throw new ReportGeneratorException("could not get necessary Tour DataHandlers for Exporting",e);
        }

    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws Exception {
        BufferedImage resultImage =
        Thumbnails.of(originalImage)
                .size(targetWidth, targetHeight)
                .outputFormat("JPEG")
                .outputQuality(1)
                .asBufferedImage();
        return resultImage;
    }

    public void generateTourReport(Tour tour) throws ReportGeneratorException {

        Document document = new Document();
        Tour tourDetails= tour;
        String tourName = tour.getTourName();
        try {
            List<TourLog> tourLogList = tourLogHandler.getAllTourLogs(tourName);
            log.debug("Collected all Tour Attributes and TourLogs for PDF Exporting");
            PdfWriter.getInstance(document, new FileOutputStream("TourReport_"+tourName+".pdf"));
            document.open();

            Font chapterFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, Font.BOLD);
            Font subChapterFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
            Font paragraphFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);

            Chunk tourLogs = new Chunk("TourLogs for "+tourName, chapterFont);
            Chunk tourAttributes = new Chunk(tourName+" Tour Attributes", chapterFont);

            document.add(new Chapter(new Paragraph(tourAttributes),1));
            document.add(new Paragraph("Tour Description: "+tourDetails.getTourDescription(),paragraphFont));
            document.add(new Paragraph("Tour Distance: "+tourDetails.getTourDistance(),paragraphFont));
            document.add(new Paragraph("Route Information: "+tourDetails.getRouteInformation(),paragraphFont));
            document.add(new Paragraph("Tour Route",subChapterFont));
            document.add(new Paragraph("Route From: "+tourDetails.getTourFrom(),paragraphFont));
            document.add(new Paragraph("Route to: "+tourDetails.getTourTo(),paragraphFont));
            document.add(new Paragraph(" "));
            log.debug("Single Export : added all TourInfos to document");

            BufferedImage image = ImageIO.read(new File("src/main/resources/View/pictures/"+tourName+".jpg"));
            image = resizeImage(image,520,350);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            document.add(new Jpeg(bytes));
            log.debug("Tour Report generation : added TourImage from FileSystem to document");

            int logCounter=0;
            document.add(new Chapter(new Paragraph(tourLogs),2));
            document.add(new Paragraph(" "));
            for (TourLog tourLog : tourLogList) {
                logCounter++;
                document.add(new Paragraph("Tour Log "+logCounter, subChapterFont));
                document.add(new Paragraph("Log Author: " + tourLog.getAuthor(), paragraphFont));
                document.add(new Paragraph("Log Report: " + tourLog.getLogReport(), paragraphFont));
                document.add(new Paragraph("Log Traveled Distance: " + tourLog.getTraveledDistance(), paragraphFont));
                document.add(new Paragraph("Log Duration: " + tourLog.getDuration(), paragraphFont));
                document.add(new Paragraph("Log Date: " + tourLog.getTimestamp().substring(0,10), paragraphFont));
                document.add(new Paragraph("Log Rating: " + tourLog.getRating(), paragraphFont));
                document.add(new Paragraph("Log Average Speed: " + tourLog.getAvgSpeed(), paragraphFont));
                document.add(new Paragraph("Log Special Remarks: " + tourLog.getRemarks(), paragraphFont));
                document.add(new Paragraph("Log Joule expended: " + tourLog.getJoule(), paragraphFont));
                document.add(new Paragraph("Log Weather: " + tourLog.getWeather(), paragraphFont));
                document.add(new Paragraph(" "));
            }
            log.debug("Single Export : added all TourLogs to document");

        } catch (TourListManagerException | DocumentException | TourLogManagerException | FileNotFoundException e) {
            throw new ReportGeneratorException("Could not get Tour Attributes",e);
        } catch (IOException e) {
            throw new ReportGeneratorException("Could not open Image File",e);
        } catch (Exception e) {
            throw new ReportGeneratorException("Could not resize Image",e);
        }

        document.close();

    }

    public void generateTourSummaryReport() throws ReportGeneratorException {

        Document document = new Document();

        log.debug("Retrieved all Tour Infos and TourLogs for PDF Exporting");
        try {
            List<Tour> tours = tourListHandler.getAllToursAttributes();
            PdfWriter.getInstance(document, new FileOutputStream("TourReport_AllTours.pdf"));
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 22, Font.BOLD);
            Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
            Font subChapterFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
            Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
            int chapterCounter=1;

            for (Tour tour : tours){
                Chunk tourAttributes = new Chunk(tour.getTourName()+" Tour Attributes", chapterFont);
                document.add(new Chapter(new Paragraph(tourAttributes),chapterCounter));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Tour Attributes",subChapterFont));
                document.add(new Paragraph("Tour Description: "+tour.getTourDescription(),paragraphFont));
                document.add(new Paragraph("Tour Distance: "+tour.getTourDistance(),paragraphFont));
                document.add(new Paragraph("Route Information: "+tour.getRouteInformation(),paragraphFont));
                document.add(new Paragraph("Tour Route",subChapterFont));
                document.add(new Paragraph("Tour Route From: "+tour.getTourFrom(),paragraphFont));
                document.add(new Paragraph("Tour Route to: "+tour.getTourTo(),paragraphFont));
                document.add(new Paragraph(" "));
                log.debug("Export all : Added all TourInfos for Tour in Document");

                HashMap<String,String> averages = calculateStatsTourLogs(tour.getTourName());
                document.add(new Paragraph("Tour Logs Summary",subChapterFont));
                document.add(new Paragraph("Tour Logs Average Speed: "+averages.get("average Speed"),paragraphFont));
                document.add(new Paragraph("Tour Logs Average Distance: "+averages.get("average Distance"),paragraphFont));
                document.add(new Paragraph("Tour Logs Average Duration: "+averages.get("average Duration"),paragraphFont));
                document.add(new Paragraph("Tour Logs Average Joule: "+averages.get("average Joule"),paragraphFont));
                document.add(new Paragraph("Tour Logs Average Rating: "+averages.get("average Rating"),paragraphFont));
                log.debug("Export all : Added all Averages for all TourLogs of current Tour in Document");

                chapterCounter++;

            }
        } catch (DocumentException e) {
            throw new ReportGeneratorException("couldn't create PDF Document",e);
        } catch (FileNotFoundException e) {
            throw new ReportGeneratorException("couldn't generate File for Report",e);
        } catch (TourListManagerException e) {
            throw new ReportGeneratorException("couldn't get Tours for Report generation",e);
        }

        document.close();

    }

    public HashMap<String,String> calculateStatsTourLogs(String tourName) throws ReportGeneratorException {
        List<TourLog> tourLogList = null;
        try {
            tourLogList = tourLogHandler.getAllTourLogs(tourName);
            Double tourLogsTotalSpeed=0.0;
            Double tourLogsTotalDistance=0.0;
            Double tourLogsTotalDuration=0.0;
            Double tourLogsTotalJoule=0.0;
            int tourLogsTotalRatings=0;
            int tourLogsCount = 0;

            for (TourLog tourLog: tourLogList){
                tourLogsTotalSpeed += tourLog.getAvgSpeed();
                tourLogsTotalDistance += tourLog.getTraveledDistance();
                tourLogsTotalDuration += tourLog.getDuration();
                tourLogsTotalJoule += tourLog.getJoule();
                tourLogsTotalRatings += tourLog.getRating();
                tourLogsCount ++;
            }

            HashMap<String,String> averages = new HashMap<>();
            if(tourLogsCount != 0) {
                averages.put("average Speed", "" + tourLogsTotalSpeed / tourLogsCount);
                averages.put("average Distance",""+tourLogsTotalDistance / tourLogsCount);
                averages.put("average Duration",""+tourLogsTotalDuration / tourLogsCount);
                averages.put("average Joule",""+tourLogsTotalJoule / tourLogsCount);
                averages.put("average Rating",""+ (tourLogsTotalRatings + tourLogsCount - 1) / tourLogsCount);
                log.debug("All Tours Report -> Calculated all Averages for all TourLogs of current Tour in Document");
            }

            return averages;
        } catch (TourLogManagerException e) {
            throw new ReportGeneratorException("could not get TourLogs data for Exporting",e);
        }
    }


}
