package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BadgeFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindBadge1() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Badges from Database */

        Badge b1 = badgeDAO.find("12565C60");

        /* Compare to Expected Values */
        
        assertEquals("#12565C60 (Chapman, Joshua E)", b1.toString());

    }

    @Test
    public void testFindBadge2() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Badges from Database */

        Badge b2 = badgeDAO.find("08D01475");

        /* Compare to Expected Values */
        
        assertEquals("#08D01475 (Littell, Amie D)", b2.toString());

    }
    
    @Test
    public void testFindBadge3() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Retrieve Badges from Database */

        Badge b3 = badgeDAO.find("D2CC71D4");

        /* Compare to Expected Values */
        
        assertEquals("#D2CC71D4 (Lawson, Matthew J)", b3.toString());

    }

    @Test
public void testCreateBadge() {
    BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

   
    badgeDAO.delete("D2CC71D4");

       Badge newBadge = new Badge("D2CC71D4", "Lawson, Matthew J");

    boolean created = badgeDAO.create(newBadge);
    assertTrue(created);

    Badge fetched = badgeDAO.find("D2CC71D4");
    assertNotNull(fetched);
    assertEquals("#D2CC71D4 (Lawson, Matthew J)", fetched.toString());
}

    @Test
public void testUpdateBadge() {
    BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

    Badge original = badgeDAO.find("08D01475");
    assertNotNull(original);

    Badge updated = new Badge("08D01475", "Updated Amie Littell");
    boolean success = badgeDAO.update(updated);
    assertTrue(success);

    Badge fetched = badgeDAO.find("08D01475");
    assertEquals("Updated Amie Littell", fetched.getDescription());

    badgeDAO.update(original);

}

    @Test
public void testDeleteBadge() {
    BadgeDAO badgeDAO = daoFactory.getBadgeDAO();


    Badge original = badgeDAO.find("12565C60");
    assertNotNull(original);

    
    boolean deleted = badgeDAO.delete("12565C60");
    assertTrue(deleted);

    Badge fetched = badgeDAO.find("12565C60");
    assertNull(fetched);

    badgeDAO.create(original);
}
}
