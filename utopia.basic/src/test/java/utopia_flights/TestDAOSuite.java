package utopia_flights;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({TestDeleteAddDAO.class, TestReadDAO.class, TestUpdateDAO.class})
public class TestDAOSuite {

}
