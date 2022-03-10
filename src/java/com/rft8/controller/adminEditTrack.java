/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rft8.controller;

import com.rft8.dao.DAO;
import com.rft8.model.Track;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author Administrator
 */
@MultipartConfig
@WebServlet(name = "adminEditTrack", urlPatterns = {"/adminEditTrack"})
public class adminEditTrack extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        DAO dao = new DAO();
        String trackFile = request.getParameter("trackFile");
        int id = Integer.parseInt(request.getParameter("id"));
        String albumId = request.getParameter("albumId");
        String name = request.getParameter("name");
        Part part = request.getPart("music");
        String fileName = part.getSubmittedFileName();
        String path = getServletContext().getRealPath("/audio" + File.separator + fileName);
        InputStream is = part.getInputStream();
        String artistId = request.getParameter("artistId");
        if (name.isEmpty()) {
            session.setAttribute("editTrackmess", "Miss input!");
            response.sendRedirect("admin/edit-track-form.jsp");
        } else {
            session.removeAttribute("editTrackmess");
            if (!fileName.isEmpty()) {
                uploadFile(is, path);
                dao.editTrack(new Track(id, albumId, name, fileName, artistId));
                response.sendRedirect("admin/edit-track.jsp");
            } else {
                dao.editTrack(new Track(id, albumId, name, trackFile, artistId));
                response.sendRedirect("admin/edit-track.jsp");
            }
        }
    }

    public void uploadFile(InputStream is, String path) {
        try {
            byte[] byt = new byte[(is.available())];
            is.read(byt);
            FileOutputStream fops = new FileOutputStream(path);
            fops.write(byt);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
