/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rft8.controller;

import com.rft8.dao.DAO;
import com.rft8.model.Album;
import com.rft8.model.Artist;
import com.rft8.model.Genre;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(name = "addAlbum", urlPatterns = {"/addAlbum"})
public class addAlbum extends HttpServlet {

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
        String id = request.getParameter("id");
        String artist = request.getParameter("artist");
        String name = request.getParameter("name");
        String year = request.getParameter("year");
        String genre = request.getParameter("genre");
        Part part = request.getPart("pic");
        String fileName = part.getSubmittedFileName();
        String path = getServletContext().getRealPath("/img/album-img" + File.separator + fileName);
        InputStream is = part.getInputStream();
        if (id.isEmpty() || name.isEmpty() || year.isEmpty() || fileName.isEmpty()) {
            session.setAttribute("Amess", "Miss input!");
            response.sendRedirect("admin/add-album.jsp");
        } else {
            if (dao.checkIdAlbumExist(id)) {
                session.setAttribute("Amess", "Id exist!");
                response.sendRedirect("admin/add-album.jsp");
            } else {
                session.removeAttribute("Amess");
                uploadFile(is, path);
                dao.addAlbum(new Album(id, artist, name, year, genre, fileName));
                response.sendRedirect("admin/add-album.jsp");
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
