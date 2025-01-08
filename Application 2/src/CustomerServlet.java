import DTO.CustomerDTO;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.json.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: pasindi
 * Date: 1/8/25
 * Time: 11:16 AM
 * Description:
 */


@WebServlet(urlPatterns = "/customerJSON")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("Do Get");

        try {
//            JsonObjectBuilder customer1= Json.createObjectBuilder();
//            customer1.add("id","C001");
//            customer1.add("name","pasindi");
//            customer1.add("address","galle");
//
//            JsonObjectBuilder customer2= Json.createObjectBuilder();
//            customer2.add("id","C001");
//            customer2.add("name","pasindi");
//            customer2.add("address","galle");
//
//            JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();
//            arrayBuilder.add(customer1.build());
//            arrayBuilder.add(customer2.build());

            Connection connection=dataSource.getConnection();
            ResultSet resultSet=connection.prepareStatement("SELECT * FROM customer").executeQuery();

            JsonArrayBuilder arrayBuilder=Json.createArrayBuilder();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);

                //create a single json object
                JsonObjectBuilder customer = Json.createObjectBuilder();
                customer.add("id", id);
                customer.add("name", name);
                customer.add("address", address);
                arrayBuilder.add(customer.build());
            }

            JsonObjectBuilder response =Json.createObjectBuilder();
            response.add("customers",arrayBuilder);
            response.add("status",HttpServletResponse.SC_OK);
            response.add("message","Success");

            JsonObject json= response.build();
            resp.setContentType("application/json");
            resp.getWriter().print(json);

        }catch (Exception e){
            JsonObjectBuilder response= Json.createObjectBuilder();
            response.add("data","");
            response.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.add("message",e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            JsonReader jsonReader=Json.createReader(req.getReader());
            JsonObject jsonObject=jsonReader.readObject();
//            String id = req.getParameter("id");
//            String name = req.getParameter("name");
//            String address = req.getParameter("address");
//
//            if (id == null || name == null || address == null) {
//                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
//                resp.getWriter().write("{\"error\": \"All fields are required\"}");
//                return;
//            }
//
            String id=jsonObject.getString("id");
            String name=jsonObject.getString("name");
            String address=jsonObject.getString("address");
            System.out.println(id + " " + name + " " + address);

            Connection connection = dataSource.getConnection();
            PreparedStatement pstm= connection.prepareStatement("INSERT INTO customer VALUES (?,?,?)");
            pstm.setString(1,id);
            pstm.setString(2,name);
            pstm.setString(3,address);
            pstm.executeUpdate();

//        save on database
//        save a proper response

            JsonObjectBuilder response= Json.createObjectBuilder();
            response.add("data","");
            response.add("status",HttpServletResponse.SC_CREATED);
            response.add("message","Success");

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());

        }catch (Exception e){
            JsonObjectBuilder response= Json.createObjectBuilder();
            response.add("data","");
            response.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.add("message",e.getMessage());

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());
        }
//        JsonObjectBuilder response= Json.createObjectBuilder();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            JsonReader jsonReader=Json.createReader(req.getReader());
            JsonObject jsonObject=jsonReader.readObject();

            String id=jsonObject.getString("id");
            String name=jsonObject.getString("name");
            String address=jsonObject.getString("address");

            Connection connection = dataSource.getConnection();
            CustomerDTO customer=findById(id);

            try {
                if (customer != null) {
                    PreparedStatement pstm = connection.prepareStatement("UPDATE customer SET name = ?, address = ? WHERE id = ?");
                    pstm.setString(1, name);
                    pstm.setString(2, address);
                    pstm.setString(3, id);
                    pstm.executeUpdate();
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    resp.getWriter().write("{\"error\": \"Customer not found\"}");
                }
                resp.getWriter().write("{\"success\": \"Customer updated successfully\"}");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            JsonObjectBuilder response= Json.createObjectBuilder();
            response.add("data","");
            response.add("status",HttpServletResponse.SC_CREATED);
            response.add("message","Success");

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());

        }catch (Exception e){
            JsonObjectBuilder response= Json.createObjectBuilder();
            response.add("data","");
            response.add("status",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.add("message",e.getMessage());

            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.setContentType("application/json");
            resp.getWriter().print(response.build());
        }
    }

    private CustomerDTO findById(String id) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM customer WHERE id = ?");
            pstm.setString(1, id);
            ResultSet resultSet = pstm.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString(2);
                String address = resultSet.getString(3);
                return new CustomerDTO(id, name, address);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        CustomerDTO customer = findById(id);

        if (customer == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("{\"error\": \"Customer not found\"}");
            return;
        }

        try {
            Connection connection=dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE FROM customer WHERE id = ?");
            pstm.setString(1, id);
            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                resp.getWriter().write("{\"success\": \"Customer deleted successfully\"}");
            }else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Failed to delete customer\"}");
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}