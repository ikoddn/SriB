package no.srib.sribapp.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.srib.sribapp.controller.exception.InvalidParameterException;

/**
 * Servlet implementation class PodcastStream
 */
@WebServlet("/PodcastStream")
public class PodcastStream extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        BufferedInputStream bufferStream = null;
        ServletOutputStream outStream = null;

        String fileName = req.getParameter("file");

        try {
            if (fileName == null || fileName.equals("")) {
                throw new InvalidParameterException();
            }

            File mp3 = new File(getServletContext().getRealPath("/")
                    + "podcast", fileName);

            FileInputStream inputStream = new FileInputStream(mp3);
            bufferStream = new BufferedInputStream(inputStream);
            
            resp.setContentType("audio/mpeg");
            resp.setContentLength((int) mp3.length());
            
            outStream = resp.getOutputStream();
            int readByte = bufferStream.read();
            
            while (readByte != -1) {
                outStream.write(readByte);
                readByte = bufferStream.read();
            }
        } catch (InvalidParameterException e) {
            // TODO Treat exceptions, this will currently display an Apache error page
            throw new ServletException(e);
        } finally {
            if (outStream != null) {
                outStream.close();
            }

            if (bufferStream != null) {
                bufferStream.close();
            }
        }
    }
}
