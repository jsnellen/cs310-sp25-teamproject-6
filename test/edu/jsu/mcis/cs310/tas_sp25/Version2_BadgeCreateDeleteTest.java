package edu.jsu.mcis.cs310.tas_sp25;

import edu.jsu.mcis.cs310.tas_sp25.dao.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Version2_BadgeCreateDeleteTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testCreateBadge1() {

        /* Create Badges */

        Badge b1 = new Badge("Bies, Bill X");

        /* Compare Badge to Expected Value */
        //System.out.println (b1); // checking by print() (debugging)
        assertEquals("#052B00DC (Bies, Bill X)", b1.toString());
        

    }
    
    @Test
    public void testCreateBadge2() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Badge Object */

        Badge b2 = new Badge("Smith, Daniel Q");
        
        /* Insert New Badge (delete first in case badge already exists) */
        
        badgeDAO.delete(b2.getId());
        boolean result = badgeDAO.create(b2);

        /* Compare Badge to Expected Value */
        
        assertEquals("#02AA8E86 (Smith, Daniel Q)", b2.toString());
        //System.out.println (b2); // debugging println
        
        /* Check Insertion Result */
        
        assertEquals(true, result);

    }
    
    @Test
    public void testDeleteBadge1() {
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create New Badge Object */

        Badge b = new Badge("Haney, Debra F");
        
        /* Insert New Badge (delete first in case badge already exists) */
        
        badgeDAO.delete(b.getId());
        badgeDAO.create(b);
        
        /* Delete New Badge */
        
        boolean result = badgeDAO.delete(b.getId());

        /* Compare Badge to Expected Value */
        
        assertEquals("#8EA649AD (Haney, Debra F)", b.toString());
        //System.out.println(b); // debugging println
        
        /* Check Deletion Result */
        
        assertEquals(true, result);

    }
       @Test
public void testUpdateBadge() {
    BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

    Badge original = badgeDAO.find("08D01475");
    assertNotNull(original);
    // System.out.println ("original badge: " + original);// debugging println

    Badge updated = new Badge("08D01475", "Updated Amie Littell");
    boolean success = badgeDAO.update(updated);
    assertTrue(success);

    Badge fetched = badgeDAO.find("08D01475");
    assertEquals("Updated Amie Littell", fetched.getDescription());
    // System.out.println ("updated badge: " + fetched);// debugging pritnln


    badgeDAO.update(original);

}
    
}
