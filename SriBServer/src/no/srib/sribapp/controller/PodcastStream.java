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
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        String fileName = "pipershut.mp3";

        try {
            stream = response.getOutputStream();

            // File[] roots = File.listRoots();
            // File dir = new File(roots[0], "podcast");
            // File mp3 = new File(dir, fileName);

            File mp3 = new File(getServletContext().getRealPath("/")
                    + "podcast", fileName);

            response.setContentType("audio/mpeg");
            response.addHeader("Content-Disposition", "attachment; filename="
                    + fileName);

            response.setContentLength((int) mp3.length());

            FileInputStream input = new FileInputStream(mp3);
            buf = new BufferedInputStream(input);
            int readBytes = 0;

            while ((readBytes = buf.read()) != -1) {
                stream.write(readBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }

            if (buf != null) {
                buf.close();
            }
        }
    }
}
