package cz.fi.muni.pa165.dao;

import cz.fi.muni.pa165.InMemoryDatabaseContext;
import cz.fi.muni.pa165.dao.interfaces.VehicleCategoryDao;
import cz.fi.muni.pa165.entity.VehicleCategory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by MBalicky on 23/10/2016.
 *
 * @author Michal Balický
 */

@ContextConfiguration(classes = InMemoryDatabaseContext.class)
public class VehicleCategoryDaoImplTest extends AbstractTransactionalTestNGSpringContextTests {

    @Inject
    private VehicleCategoryDao vehicleCategoryDao;

    private VehicleCategory vehicleCategory1;
    private VehicleCategory vehicleCategory2;

    @BeforeMethod
    public void setUp() {
        vehicleCategory1 = new VehicleCategory("Supply");
        vehicleCategoryDao.persist(vehicleCategory1);

        vehicleCategory2 = new VehicleCategory("Representational");
        vehicleCategoryDao.persist(vehicleCategory2);
    }

    @Test
    public void testFindById() {
        // Act
        VehicleCategory foundVehiclecCategory = vehicleCategoryDao.findById(vehicleCategory1.getId());

        // Assert
        Assert.assertEquals(foundVehiclecCategory, vehicleCategory1);
    }

    @Test
    public void testFindAll() {
        // Act
        List<VehicleCategory> foundVehicleCategories = vehicleCategoryDao.findAll();

        // Assert
        Assert.assertEquals(foundVehicleCategories.get(0), vehicleCategory1);
        Assert.assertEquals(foundVehicleCategories.get(1), vehicleCategory2);
    }

    @Test
    public void testPersist() {
        // Arrange
        int itemCountBefore = vehicleCategoryDao.findAll().size();

        // Act
        VehicleCategory vehicleCategory = new VehicleCategory("Facility Department");
        vehicleCategoryDao.persist(vehicleCategory);

        // Assert
        Assert.assertEquals(vehicleCategoryDao.findAll().size(), itemCountBefore + 1);
    }

    @Test
    public void testMerge() {
        // Arrange
        String name = "MadeUpName";
        VehicleCategory foundVehicleCategory = vehicleCategoryDao.findById(vehicleCategory1.getId());
        foundVehicleCategory.setName(name);

        // Act
        vehicleCategoryDao.merge(foundVehicleCategory);

        // Assert
        VehicleCategory foundAfterMergeVehicleCategory = vehicleCategoryDao.findById(vehicleCategory1.getId());
        Assert.assertEquals(foundAfterMergeVehicleCategory.getName(), name);
    }

    @Test
    public void testRemove() {
        // Arrange
        int itemCountBefore = vehicleCategoryDao.findAll().size();

        // Act
        vehicleCategoryDao.remove(vehicleCategory1);

        // Assert
        Assert.assertEquals(vehicleCategoryDao.findAll().size(), itemCountBefore - 1);
    }

    @Test
    public void testRemoveById() {
        // Arrange
        int itemCountBefore = vehicleCategoryDao.findAll().size();

        // Act
        vehicleCategoryDao.remove(vehicleCategory1);

        // Assert
        Assert.assertEquals(vehicleCategoryDao.findAll().size(), itemCountBefore - 1);
    }

    @Test(expectedExceptions = org.springframework.dao.DataAccessException.class)
    public void testNullName() {
        VehicleCategory vehicleCategoryNullName = new VehicleCategory(null);
        vehicleCategoryDao.persist(vehicleCategoryNullName);
    }

    @Test(expectedExceptions = org.springframework.dao.DataAccessException.class)
    public void testUniqueName() {
        VehicleCategory vehicleCategoryUniqueName1 = new VehicleCategory("Unique");
        VehicleCategory vehicleCategoryUniqueName2 = new VehicleCategory("Unique");
        vehicleCategoryDao.persist(vehicleCategoryUniqueName1);
        vehicleCategoryDao.persist(vehicleCategoryUniqueName2);
    }
}
