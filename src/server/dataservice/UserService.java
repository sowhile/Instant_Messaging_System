package server.dataservice;

import server.dao.UserDAO;
import server.domain.User;

/**
 * @author sowhile
 * <p>
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();

    public Object getUser(String userName) {
        String sql = "SELECT * FROM users WHERE user = ?";
        return userDAO.querySingle(sql, User.class, userName);
    }

    public String getPassword(String user) {
        String sql = "SELECT password FROM users WHERE user = ?";
        return (String) userDAO.queryScalar(sql, user);
    }
}
