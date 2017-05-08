package hello;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Hashtable;
import javax.naming.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.jdbc.datasource.DataSourceUtils;
import java.util.Random;




public class DBConnector {
	
	public String parseSessId(String sessId) {
		sessId = sessId.trim();
		String [] pair = sessId.split("=");
		if (pair[0].equals("sessId")){
			return pair[1];
		}
		return "Not SessId";
	}
	@SuppressWarnings("finally")
	public String auth(String sessId) {
		sessId = parseSessId(sessId);
		// Create a new application context. this processes the Spring config
		ApplicationContext ctx =
		    new ClassPathXmlApplicationContext("authServer.xml");
		// Retrieve the data source from the application context
		    DataSource ds = (DataSource) ctx.getBean("dataSource");
		// Open a database connection using Spring's DataSourceUtils
		Connection c = DataSourceUtils.getConnection(ds);
	    String res = "";
		try {
		    // retrieve a list of three random cities
			PreparedStatement pick = c.prepareStatement("use users");
			pick.executeQuery();
		    PreparedStatement ps = c.prepareStatement(
		    		"select * from sessions where token=?"
		    		);
		    ps.setString(1, sessId);
		    ResultSet rs = ps.executeQuery();
		    while(rs.next()) {
		        res += rs.getString("username");
		        res+= rs.getString("token");
		        res+= rs.getString("exp_date");

		    }
		   
		} catch (SQLException ex) {
		    // something has failed and we print a stack trace to analyse the error
		    ex.printStackTrace();
		    // ignore failure closing connection
		    try { c.close(); } catch (SQLException e) { }
		    DataSourceUtils.releaseConnection(c, ds);
		    try {
		    	((ConfigurableApplicationContext)ctx).close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "Broke";

		} finally {
		    // properly release our connection
		    DataSourceUtils.releaseConnection(c, ds);
		    try {
		    	((ConfigurableApplicationContext)ctx).close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if (!res.equals("")){
		    	return "Valid Session Token";
		    }
		    else {
		    	return "Invalid Session Token" + "|sessId = "+sessId;
		    }
		}
	}
	
	
	public String [] parseLogin(String body){
		body = body.trim();
		String [] p = body.split("&");
		String [] res =  {"1","2"};
		if (p.length == 2){
			String []f = p[0].split("=");
			if (f[0].equals("user")){
				res[0] = f[1];
			}
			String []l = p[1].split("=");
			if (l[0].equals("pass")){
				res[1] = l[1];
			}
		}	
		return res;
	}
	
	@SuppressWarnings("finally")
	public String login(String body){
		
		String [] userPass = parseLogin(body);

		/**
		 * Demonstrates how to create an initial context to an LDAP server
		 * using simple authentication.
		 *
		 * usage: java Simple
		 */
		    // Set up environment for creating initial context
	    Hashtable<String, String> env = new Hashtable(11);
	    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	    env.put(Context.PROVIDER_URL, "ldaps://ldap.rit.edu/");

	    env.put(Context.SECURITY_AUTHENTICATION, "simple");
	    env.put(Context.SECURITY_PRINCIPAL, "uid="+userPass[0]+", ou=people, dc=rit,dc=edu");
	    env.put(Context.SECURITY_CREDENTIALS, userPass[1]);

	    try {
	        // Create initial context
	        DirContext ctx = new InitialDirContext(env);

	    ///System.out.println(ctx.lookup("ou=people"));

	        // do something useful with ctx	

	        // Close the context when we're done
	        ctx.close();
	    } catch (NamingException e) {
	    	e.printStackTrace();
	    	return "LDAP Auth Failed";
	    }   
		 

		Random ihatejava = new Random();
		String sessId = "";
		for(int i=0; i<10;i++){
			Integer curr = new Integer(ihatejava.nextInt(10));
			sessId = sessId +curr.toString();
		}

		
		ApplicationContext ctx =
			    new ClassPathXmlApplicationContext("authServer.xml");
			// Retrieve the data source from the application context
			    DataSource ds = (DataSource) ctx.getBean("dataSource");
			// Open a database connection using Spring's DataSourceUtils
			Connection c = DataSourceUtils.getConnection(ds);
		    String res = "";
			try {
			    // retrieve a list of three random cities
				PreparedStatement pick = c.prepareStatement("use users");
				pick.executeQuery();
				
				PreparedStatement checkUser = c.prepareStatement("select * from settings where username = ?");
			    checkUser.setString(1, userPass[0]);
			    ResultSet valid = checkUser.executeQuery();
			    
			    if (valid.isBeforeFirst()){
									
				    PreparedStatement ps = c.prepareStatement("select * from sessions where username=?");
				    ps.setString(1, userPass[0]);
				    ResultSet rs = ps.executeQuery();
				    if (!rs.isBeforeFirst() ) {    
				    	PreparedStatement ps1 = c.prepareStatement("insert into sessions (username,token,exp_date) values (?,?,?)");
					    ps1.setString(1, userPass[0]);
					    ps1.setString(2, sessId);
					    ps1.setString(3, "2017-05-18");
					    int rs1 = ps1.executeUpdate();
					   
				    }
				    else {
								
					    PreparedStatement ps1 = c.prepareStatement(
					    		"update sessions set token=? where username=?"
					    		);
					    ps1.setString(1, sessId);
					    ps1.setString(2, userPass[0]);
					    int rs1 = ps1.executeUpdate();
				    }
			    } else {
			    	res = "No such User";
			    }
				   
			} catch (SQLException ex) {
			    // something has failed and we print a stack trace to analyse the error
			    ex.printStackTrace();
			    // ignore failure closing connection
			    try { c.close(); } catch (SQLException e) { }
			    DataSourceUtils.releaseConnection(c, ds);
			    try {
			    	((ConfigurableApplicationContext)ctx).close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "Failed to login";

			} finally {
			    // properly release our connection
			    DataSourceUtils.releaseConnection(c, ds);
			    try {
			    	((ConfigurableApplicationContext)ctx).close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (res.equals("")){
				return "sessId="+sessId;
			}
			else {
				return res;
			}
	}
	
	
	public String [] parseSignup(String body) {
		body = body.trim();
		String [] person = new String[5];
		//USE WHITELIST
		//String [] whiteList = {"id","user","screen","pass"};
		String []params = body.split("&");
		for (int x=0; x<params.length; x++){
			person[x] = params[x].split("=")[1];
		}
		return person;
	}
	
	@SuppressWarnings("finally")
	public String signup(String body){
		String person[] = parseSignup(body);
		ApplicationContext ctx =
			    new ClassPathXmlApplicationContext("authServer.xml");
			// Retrieve the data source from the application context
			    DataSource ds = (DataSource) ctx.getBean("dataSource");
			// Open a database connection using Spring's DataSourceUtils
			Connection c = DataSourceUtils.getConnection(ds);
		    String res = "";
			try {
			    // retrieve a list of three random cities
				PreparedStatement pick = c.prepareStatement("use users");
				pick.executeQuery();
			   /* PreparedStatement ps = c.prepareStatement(
			    		"Select id from settings where username=?");
			    ps.setString(1, person[1]);
			    ps.setString(2, person[2]);
			    ResultSet rs = ps.executeQuery();
			    if (!rs.isBeforeFirst() ) {   */ 
			    	PreparedStatement ps1 = c.prepareStatement("insert into settings (username,displayname,email,image) values(?,?,?,?)");
			    	//for (int x=0;x<person.length;x++){
			    		ps1.setString(1,person[0]);
			    		ps1.setString(2,person[1]);
			    		ps1.setString(3,person[2]);
			    		ps1.setString(4,person[3]);
			    		
			    	//}
			    	int update = ps1.executeUpdate();
			    	res = new Integer(update).toString();
			    	return "User SignedUp";
			   /* }
			    else {
			    	res = "Username Taken";
			    }*/
			    
			} catch (SQLException ex) {
			    // something has failed and we print a stack trace to analyse the error
			    ex.printStackTrace();
			    // ignore failure closing connection
			    try { c.close(); } catch (SQLException e) { }
			    DataSourceUtils.releaseConnection(c, ds);
			    try {
			    	((ConfigurableApplicationContext)ctx).close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return ex.toString();

			} finally {
			    // properly release our connection
			    DataSourceUtils.releaseConnection(c, ds);
			    try {
			    	((ConfigurableApplicationContext)ctx).close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   return "User SignedUp";
			}
	
	}
}
