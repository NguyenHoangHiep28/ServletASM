package com.example.ServletHibernate;

import com.example.ServletHibernate.dao.AccountDao;
import com.example.ServletHibernate.dao.ProductDao;
import com.example.ServletHibernate.model.Account;
import com.example.ServletHibernate.model.Product;
import com.google.common.hash.Hashing;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "ProductServlet", value = "/")

public class ProductServlet extends HttpServlet {
    private String message;

    private ProductDao productDao;
    private AccountDao accountDao;


    public void init() {
        productDao = new ProductDao();
        accountDao = new AccountDao();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getServletPath();
        System.out.println(action);
        switch (action) {
            case "/login":
                RequestDispatcher loginDispatcher = request.getRequestDispatcher("index.jsp");
                loginDispatcher.forward(request, response);
                break;
            case "/register":
                RequestDispatcher registerDispatcher = request.getRequestDispatcher("register.jsp");
                registerDispatcher.forward(request, response);
                break;
            case "/list":
                if (checkAuthorize(request, response)) {
                    getAll(request, response);
                } else {
                    redirectToLogin(request, response);
                }
                break;
            case "/new":
                if (checkAuthorize(request, response)) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("add-product.jsp");
                    dispatcher.forward(request, response);
                } else {
                    redirectToLogin(request, response);
                }
                break;
            case "/edit":
                if (checkAuthorize(request, response)) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Product productExist = productDao.getProduct(id);
                    RequestDispatcher dispat = request.getRequestDispatcher("add-product.jsp");
                    request.setAttribute("product", productExist);
                    dispat.forward(request, response);
                } else {
                    redirectToLogin(request, response);
                }
                break;
            case "/delete":
                if (checkAuthorize(request, response)) {
                    int delId = Integer.parseInt(request.getParameter("id"));
                    try {
                        deleteUser(request, response);
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                } else {
                    redirectToLogin(request, response);
                }

                break;
            case "/logout":
                if (checkAuthorize(request, response)) {
                    request.getSession().invalidate();
                    response.sendRedirect(request.getContextPath() + "/login");
                } else {
                    redirectToLogin(request, response);
                }

                break;
            default:
                if (checkAuthorize(request, response)) {
                    getAll(request, response);
                } else {
                    redirectToLogin(request, response);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        System.out.println(action);
        switch (action) {
            case "/login":
                login(req, resp);
                break;
            case "/register":
                register(req, resp);
                break;
            case "/insert":
                try {
                    insertUser(req, resp);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                break;
            case "/update":
                try {
                    updateUser(req, resp);
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                break;
        }
    }

    private void getAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Product> list = productDao.getAllProducts();
        req.setAttribute("listProduct", list);
        RequestDispatcher dispatcher = req.getRequestDispatcher("product-list.jsp");
        dispatcher.forward(req, resp);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String madeIn = request.getParameter("madeIn");
        String price = request.getParameter("price");
        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setBrand(brand);
        newProduct.setMadeIn(madeIn);
        newProduct.setPrice(Float.parseFloat(price));
        productDao.saveProduct(newProduct);
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String madeIn = request.getParameter("madeIn");
        String price = request.getParameter("price");
        Product newProduct = new Product();
        newProduct.setId(id);
        newProduct.setName(name);
        newProduct.setBrand(brand);
        newProduct.setMadeIn(madeIn);
        newProduct.setPrice(Float.parseFloat(price));
        productDao.updateProduct(newProduct);
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        productDao.deleteProduct(id);
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username != null && password != null) {
            String encryptedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
            List<Account> accounts = accountDao.login(username, encryptedPassword);
            if (accounts != null && accounts.size() > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect(request.getContextPath() + "/list");
            } else {
                out.append("Sorry, username or password does not match!");
            }
        }
        out.close();
    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPass = request.getParameter("confirm-password");
        if (username.length() >= 5) {
            if (accountDao.findUserByUsername(username) == null || accountDao.findUserByUsername(username).size() == 0) {
                if (password.equals(repeatPass)) {
                    String encryptedPassword = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
                    Account account = new Account(username, encryptedPassword);
                    accountDao.register(account);
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    response.sendRedirect(request.getContextPath() + "/list");
                } else {
                    out.append("Sorry, password confirm does not match!");
                }
            } else {
                out.append("Username " + username + "is existing. Please choose another username!");
            }
        } else {
            out.append("Password must be from 5 characters upwards");
        }
        out.close();
    }

    private boolean checkAuthorize(HttpServletRequest request, HttpServletResponse response) {
        return request.getSession().getAttribute("username") != null;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/login");
    }

    public void destroy() {
    }
}