import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Author: pasindi
 * Date: 1/8/25
 * Time: 9:19 AM
 * Description:
 */


@WebServlet(urlPatterns = "/InBuildDBCP")
public class InbuildDBCPServlet extends HttpServlet {

//    https://repo1.maven.org/maven2/javax/annotation/javax.annotation-api/1.3.2/
//    @Resource(name = "java:comp/env/jdbc/pool")
    @Resource(name = "java:comp/env/jdbc/pool")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        System.out.println("InbuildDBCP Servlet doGet");
        try {
            Connection connection=dataSource.getConnection();
            ResultSet resultSet=connection
                    .prepareStatement("SELECT * FROM customer")
                    .executeQuery();

            while (resultSet.next()){
                String id=resultSet.getString(1);
                String name=resultSet.getString(2);
                String address=resultSet.getString(3);

                System.out.println(id + " " + name + " " + address);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}