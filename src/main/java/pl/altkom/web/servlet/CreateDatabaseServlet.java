package pl.altkom.web.servlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabaseServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request,
                        HttpServletResponse response) {

        String dataSource = getServletContext().getInitParameter("dataSource");
        InitialContext initCtx;
        DataSource ds;
        Connection conn = null;
        Statement stmt = null;
        try {
            initCtx = new InitialContext();
            Context context = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) context.lookup(dataSource);
            conn = ds.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate("CREATE TABLE Users (id INTEGER PRIMARY KEY, name TEXT)");
            stmt.executeUpdate("INSERT INTO Users VALUES(1, 'pawel')");
            stmt.executeUpdate("INSERT INTO Users VALUES(2, 'bogdan')");
            PrintWriter pw = response.getWriter();
            pw.println("<HTML><HEAD>");
            pw.println("<TITLE>Hello</TITLE>");
            pw.println("</HEAD><BODY>");
            pw.println("<H3>Database created and filled!</H3>");
            pw.println("</BODY></HTML>");
        } catch (NamingException | SQLException | IOException e) {
            try {
                e.printStackTrace();
                PrintWriter pw = response.getWriter();
                pw.println("<HTML><HEAD>");
                pw.println("<TITLE>Hello</TITLE>");
                pw.println("</HEAD><BODY>");
                pw.println("<H3>ERROR! " + e.getMessage() + "</H3>");
                pw.println("</BODY></HTML>");
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }
}