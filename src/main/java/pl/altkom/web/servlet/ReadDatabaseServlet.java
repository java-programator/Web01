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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReadDatabaseServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest request,
                        HttpServletResponse response) {

        String dataSource = getServletContext().getInitParameter("dataSource");
        InitialContext initCtx;
        DataSource ds;
        Connection conn = null;
        Statement stmt = null;
        ResultSet resultSet = null;
        try {
            initCtx = new InitialContext();
            Context context = (Context) initCtx.lookup("java:comp/env");
            ds = (DataSource) context.lookup(dataSource);
            conn = ds.getConnection();
            stmt = conn.createStatement();
            StringBuilder sb = new StringBuilder("<TR><TH>ID</TH><TH>Name</TH></TR>\n");

            resultSet = stmt.executeQuery("SELECT * FROM Users");
            while (resultSet.next()) {
                sb.append("<TR>");
                sb.append("<TD>");
                sb.append(resultSet.getInt(1));
                sb.append("</TD>");
                sb.append("<TD>");
                sb.append(resultSet.getString(2));
                sb.append("</TD>");
                sb.append("</TR>\n");
            }

            PrintWriter pw = response.getWriter();
            pw.println("<HTML><HEAD>");
            pw.println("<TITLE>Hello</TITLE>");
            pw.println("</HEAD><BODY>");
            pw.println("<H3>Database content</H3>");
            pw.println("<TABLE>");
            pw.println(sb.toString());
            pw.println("</TABLE>");
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
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        }
    }
}