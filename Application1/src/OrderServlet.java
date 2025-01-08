import DTO.OrderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

/**
 * Author: pasindi
 * Date: 1/7/25
 * Time: 11:22 PM
 * Description:
 */

@WebServlet (urlPatterns = "/orders")
public class OrderServlet extends HttpServlet {
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


//    customer w select karanna danna
//    date eka auto enna danna
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        String customerId = req.getParameter("customerId");
        Date date = Date.valueOf(req.getParameter("date"));

        if (id == null || customerId == null || date == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"All fields are required\"}");
            return;
        }

        Connection connection =getConnection();

        try {
            PreparedStatement pstm=connection.prepareStatement("INSERT INTO orders VALUES (?,?,?)");
            pstm.setString(1,id);
            pstm.setString(2,customerId);
            pstm.setString(3, String.valueOf(date));
            pstm.executeUpdate();

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"success\": \"Order added successfully\"}");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id=req.getParameter("id");
        OrderDTO order = findById(id);

        if (order == null){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Order not found\"}");
            return;
        }

        Connection connection =getConnection();

        try{
            PreparedStatement pstm =connection.prepareStatement("DELETE FROM orders WHERE id = ?");
            pstm.setString(1,id);
            int rowsAffected =pstm.executeUpdate();

            if (rowsAffected > 0){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                resp.getWriter().write("{\"success\": \"Order deleted successfully\"}");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Failed to delete Order\"}");
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private OrderDTO findById(String id) {
        Connection connection= getConnection();

        try {
            PreparedStatement pstm=connection.prepareStatement("SELECT * FROM orders WHERE id = ?");
            pstm.setString(1,id);
            ResultSet resultSet=pstm.executeQuery();
            if (resultSet.next()){
                String customerId=resultSet.getString(2);
                Date date= Date.valueOf(resultSet.getString(3));
                return new OrderDTO(id,customerId,date);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
