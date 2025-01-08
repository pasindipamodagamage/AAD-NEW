import DTO.OrderDetailDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

/**
 * Author: pasindi
 * Date: 1/8/25
 * Time: 7:04 AM
 * Description:
 */

@WebServlet(urlPatterns = "/orderDetail")
public class OrderDetailServlet extends HttpServlet {
    private Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Shop",
                    "root",
                    "Ijse@123");
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String itemCode = req.getParameter("customerId");
        int qty = Integer.parseInt(req.getParameter("qty"));
        double unitPrice= Double.parseDouble(req.getParameter("price"));

        if (orderId == null || itemCode == null || qty == 0 || unitPrice == 0.0){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"All fields are required\"}");
            return;
        }

        Connection connection =getConnection();

        try {
            PreparedStatement pstm=connection.prepareStatement("INSERT INTO order_detail VALUES (?,?,?,?)");
            pstm.setString(1,orderId);
            pstm.setString(2,itemCode);
            pstm.setString(3, String.valueOf(qty));
            pstm.setString(4, String.valueOf(unitPrice));
            pstm.executeUpdate();

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"success\": \"Order details added successfully\"}");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");
        String itemCode = req.getParameter("customerId");
        int qty = Integer.parseInt(req.getParameter("qty"));
        double unitPrice= Double.parseDouble(req.getParameter("price"));

        if (orderId == null || itemCode == null || qty == 0 || unitPrice == 0.0){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"All fields are required\"}");
            return;
        }

        Connection connection=getConnection();
        OrderDetailDTO orderDetail=findById(orderId);

        try {
            if (orderDetail != null) {
                PreparedStatement pstm = connection.prepareStatement("UPDATE order_detail SET itemCode = ?, qty = ?, price = ? WHERE orderId = ?");
                pstm.setString(1, itemCode);
                pstm.setInt(2, qty);
                pstm.setDouble(3, unitPrice);
                pstm.setString(4, orderId);
                pstm.executeUpdate();

                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Order Detail not found\"}");
            }
            resp.getWriter().write("{\"success\": \"Order Detail updated successfully\"}");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private OrderDetailDTO findById(String orderId) {
        Connection connection= getConnection();

        try {
            PreparedStatement pstm=connection.prepareStatement("SELECT * FROM order_detail WHERE orderId = ?");
            pstm.setString(1,orderId);
            ResultSet resultSet=pstm.executeQuery();
            if (resultSet.next()){
                String itemCode=resultSet.getString(2);
                int qty = Integer.parseInt(resultSet.getString(3));
                double unitPrice= Double.parseDouble(resultSet.getString(4));
                return new OrderDetailDTO(orderId,itemCode,qty,unitPrice);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
