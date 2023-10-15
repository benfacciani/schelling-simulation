// Virginia Tech Honor Code Pledge:
//
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of those
// who do.
// -- Ben Facciani benfacc
import student.micro.*;
import static org.assertj.core.api.Assertions.*;
import student.media.*;
import student.util.Random;
import java.awt.Color;
// -------------------------------------------------------------------------
/**
 *  Test class for all of the methods in SchellingSimulation
 *
 *  @author Ben Facciani benfacc
 *  @version 2023.10.01
 */
public class SchellingSimulationTest
    extends TestCase
{
    //~ Fields ................................................................
    private SchellingSimulation simulation;

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Creates a new SchellingSimulationTest test object.
     */
    public SchellingSimulationTest()
    {
        // The constructor is usually empty in unit tests, since it runs
        // once for the whole class, not once for each test method.
        // Per-test initialization should be placed in setUp() instead.
    }


    //~ Methods ...............................................................

    // ----------------------------------------------------------
    /**
     * Sets up the test fixture.
     * Called before every test case method.
     * Creates new SchellingSimulation with 2x2 grid
     */
    public void setUp()
    {
        simulation = new SchellingSimulation(2, 2);
    }
    /**
     * Set up test fixture for when we need a 3x3 grid
     */
    private void setUp3x3()
    {
        simulation = new SchellingSimulation(3, 3);
    }


    // ----------------------------------------------------------
    /*# Insert your own test methods here */
    /**
     * Test our getter and setter methods for redLine()
     * Assuring that our method is setting and returning correctly
     */
    public void testGetSetRedLine()
    {
        simulation.setRedLine(3);
        assertThat(simulation.getRedLine()).isEqualTo(3);
    }
    /**
     * Test our getter and setter methods for satisfactionThreshold()
     * Assuring that our method is setting and returning correctly
     */
    public void testGetSetSatisfactionThreshold()
    {
        simulation.setSatisfactionThreshold(0.3);
        assertThat(simulation.getSatisfactionThreshold()).isEqualTo(0.3);
    }
    /**
     * Test for populate() method with specific values I wish my 
     * program will choose
     */
    public void testPopulate(double orange, double blue)
    {
        //Pass in double values
        simulation.populate(blue, orange);
        
        
         /*
         rand val < blue, set to blue
         rand bal < blue + orange set to orange
         else set to white*/
         
        
        
         
         /* For this example, we will assume blue will be placed at (0,0)
         and assume that orange will be placed at (1,0) That means pixel
         (0,1) and (1,1) will be white (empty)
         
         /*assertThat(simulation.getPixel(0, 0).
        getColor()).isEqualTo(Color.BLUE);
        assertThat(simulation.getPixel(1, 0).
        getColor()).isEqualTo(Color.ORANGE);
        assertThat(simulation.getPixel(0, 1).
        getColor()).isEqualTo(Color.WHITE);
        assertThat(simulation.getPixel(1, 1).
        getColor()).isEqualTo(Color.WHITE);*/
    }
    /**
     * Test for testAreSameColor() method with specific values I wish my 
     * program will choose
     */
    public void testAreSameColor()
    {
        /* For this simulation, we are going to assume pixel at cordinate
         0,0 and 0,1 are blue, and 1,0 is orange */
        simulation.getPixel(0, 0).setColor(Color.BLUE);
        simulation.getPixel(0, 1).setColor(Color.BLUE);
        simulation.getPixel(1, 0).setColor(Color.ORANGE);
        
        //Store values
        Pixel bluePixel1 = simulation.getPixel(0, 0);
        Pixel bluePixel2 = simulation.getPixel(0, 1);
        Pixel orangePixel = simulation.getPixel(1, 0);
        
        //Test two blue pixels; should return true
        boolean trueResult = simulation.areSameColor(bluePixel1, bluePixel1);
        assertThat(trueResult).isTrue();
        //Test a blue and orange pixel, should return false
        boolean falseResult = simulation.areSameColor(bluePixel1, orangePixel);
        assertThat(falseResult).isFalse();
    }
    /**
     * Test for isEmpty() method with specific values I wish my 
     * program will choose
     */
    public void testIsEmpty()
    {
        /* For this simulation, we are going to assume the pixel at 0,0 
        is white and 0,1 is blue */
        simulation.getPixel(0, 0).setColor(Color.WHITE);
        simulation.getPixel(0, 1).setColor(Color.BLUE);
        
        //Store values
        Pixel whitePixel = simulation.getPixel(0, 0);
        Pixel bluePixel = simulation.getPixel(0, 1);
        
        //Tests a white pixel, should return true
        boolean trueResult = simulation.isEmpty(whitePixel);
        assertThat(trueResult).isTrue();
        
        //Tests a blue pixel; should return false
        boolean falseResult = simulation.isEmpty(bluePixel);
        assertThat(falseResult).isFalse();
    }
    /**
     * Test for isSatisfied() method with specific values I wish my 
     * program will choose
     */
    public void testIsSatisfied()
    {
        //Create a new 3x3 grid
        setUp3x3();
        /*
         * We are going to fix this 3x3 grid to my liking get boolean result
         * of a pixel where it is/isn't satisfied
         */
        simulation.getPixel(0, 0).setColor(Color.BLUE);
        simulation.getPixel(0, 1).setColor(Color.BLUE);
        simulation.getPixel(0, 2).setColor(Color.BLUE);
        simulation.getPixel(1, 0).setColor(Color.ORANGE);
        simulation.getPixel(1, 2).setColor(Color.BLUE);
        simulation.getPixel(2, 0).setColor(Color.BLUE);
        simulation.getPixel(1, 1).setColor(Color.ORANGE);
        simulation.getPixel(2, 1).setColor(Color.ORANGE);
        simulation.getPixel(2, 2).setColor(Color.ORANGE);
        /*
         * Here is what our 3x3 grid looks like
        B B B
        O O B
        B O O
        */
       // Confirms bottom left pixel is not satisified.
        Pixel bottomLeft = simulation.getPixel(2, 0);
        boolean bottomLeftSatisfied = simulation.
            isSatisfied(bottomLeft, Color.BLUE);
        assertThat(bottomLeftSatisfied).isFalse();
        //Confirms top right pixel is satisfied.
        Pixel topRight = simulation.getPixel(0, 2);
        boolean topRightSatisfied = simulation.
            isSatisfied(topRight, Color.BLUE);
        assertThat(topRightSatisfied).isTrue();
    }
    public void testMaybeRelocate() {
    // Setup a 5x5 grid for this test
    simulation = new SchellingSimulation(5, 5);
    simulation.setRedLine(5);  // Set red line to allow relocation anywhere within the grid

    // Create a specific grid where every pixel is blue
    for (int x = 0; x < 5; x++) {
        for (int y = 0; y < 5; y++) {
            simulation.getPixel(x, y).setColor(Color.BLUE);
        }
    }

    // Set one pixel to white to allow for relocation
    simulation.getPixel(0, 0).setColor(Color.WHITE);

    // Set one pixel to orange, which will be unsatisfied
    Pixel unsatisfiedPixel = simulation.getPixel(2, 2);
    unsatisfiedPixel.setColor(Color.ORANGE);

    // Attempt to relocate the unsatisfied orange pixel
    boolean relocated = simulation.maybeRelocate(unsatisfiedPixel);

    // Assert that the pixel was relocated
    assertThat(relocated).isTrue();

    // Assert that the original location is now white (empty)
    assertThat(simulation.getPixel(2, 2).getColor()).isEqualTo(Color.WHITE);

    // Find the new location of the relocated orange pixel
    boolean found = false;
    for (int x = 0; x < 5; x++) {
        for (int y = 0; y < 5; y++) {
            Pixel pixel = simulation.getPixel(x, y);
            if (pixel.getColor().equals(Color.ORANGE)) {
                found = true;
                assertThat(x).isNotEqualTo(2);
                assertThat(y).isNotEqualTo(2);
                return;  // Exit the test method as soon as the relocated pixel is found
            }
        }
    }
    // This will only be reached if the orange pixel was not found
    assertThat(found).isTrue();
}


 }
