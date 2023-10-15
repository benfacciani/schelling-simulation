// Virginia Tech Honor Code Pledge:
//
// As a Hokie, I will conduct myself with honor and integrity at all times.
// I will not lie, cheat, or steal, nor will I accept the actions of those
// who do.
// -- Ben Facciani benfacc
import student.media.*;
import student.util.Random;
import java.awt.Color;
import student.micro.*;
import java.util.ArrayList;
import java.util.List;
//-------------------------------------------------------------------------
/**
 *  SchellingSimulation class that holds all attributes
 *  and methods regarding our simulation.
 *
 *  @author Ben Facciani benfacc
 *  @version 2023.10.01
 */
public class SchellingSimulation
    extends Picture
{
    // Satisfaction %; Horizdontal dividing line
    private double satisfactionThreshold;
    private int redLine;

    //~ Constructor ...........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a newly created SchellingSimulation object.
     * @param width the width of grid
     * @param height the height of grid
     */
    public SchellingSimulation(int width, int height)
    {
        // Specify world arguments and restrictions
        super(width, height);
        satisfactionThreshold = 0.3;
        redLine = 0;
    }
    //~ Methods ...............................................................

    /**
     * A getter method that returns
     * the satisfaction threshold (a double).
     * @return a double that represents satisfaction threshold
     */
    public double getSatisfactionThreshold()
    {
        return satisfactionThreshold;
    }
    /**
     * A setter method that takes a double
     * parameter and changes the satisfaction
     * threshold to the specified value.
     * @param newThreshold the threshold to be set
     */
    public void setSatisfactionThreshold(double newThreshold)
    {
        this.satisfactionThreshold = newThreshold;
    }
    /**
     * A getter method that returns the
     * redline value (an int).
     * @return the redLine marker
     */
    public int getRedLine()
    {
        return redLine;
    }
    /**
     * A setter method that takes an integer parameter
     * and changes the redline to the specified value.
     * @param newRedLine the new redLine set by the method
     */
    public void setRedLine(int newRedLine)
    {
        this.redLine = newRedLine;
    }
    /**
     * Takes two parameters between 0.0 - 1.0 that represent %'s.
     * We will use the parameters to "paint" the image with said 
     * represented %'s. First pixel is blue, second is orange.
     * Say we were to have areSameColor(0.3, 0.4). We would then
     * have 30% blue, 40% orange, 30% white / empty
     * @param blue % of x pixels set to blue
     * @param orange % of o pixels set to orange
     * 
     */
    public void populate(double blue, double orange)
    {
        //Construct num generator
        Random generator = new Random();
        
        //Nested loop; go thro each pixel in image
        for (int x = 0; x < getWidth(); x++)
        {
            for (int y = 0; y < getHeight(); y++)
            {   // get random value
                double randVal = generator.nextDouble();                
                //Conditions that make pixel blue, orange, or white
                if (randVal < blue)
                {
                    getPixel(x, y).setColor(Color.BLUE);
                }
                else if (randVal < blue + orange)
                {
                    getPixel(x, y).setColor(Color.ORANGE);
                }
                else
                {
                    getPixel(x, y).setColor(Color.WHITE);
                }
            }
        }
    }

    /**
     * Takes two pixel objects and returns boolean value indicating
     * whether the two pixels have the same color
     * 
     * @param pixel1 the first pixel object
     * @param pixel2 the second pixel object
     * @return the indicated boolean value
     */
    public boolean areSameColor(Pixel pixel1, Pixel pixel2)
    {
        //return true / false indicating same color or not
        return pixel1.getColor().equals(pixel2.getColor());
    }
    /**
     * Returns true if pixel is white (aka empty)
     * @param pixel the pixel to test if empty
     * @return the indicated boolean value
     */
    public boolean isEmpty(Pixel pixel)
    {
        //return true / false if color white
        return pixel.getColor().equals(Color.WHITE);
    }
    /**
     * Return true / false based on if pixel would
     * be satisfied at a given location
     * @param pixel the pixel object to test
     * @param color the color value the pixel has
     * @return the indicated boolean value
         */
    public boolean isSatisfied(Pixel pixel, Color color)
    {
        int x = pixel.getX();
        int y = pixel.getY();
    
        int sameColorCount = 0; 
        int totalNeighbors = 0;  
    
        // Loop to check all neighbors
        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                // Skip checking the pixel itself
                if (i == 0 && j == 0)
                {
                    continue;
                }
  
                // Check boundaries
                if (x + i >= 0 && x + i < getWidth() &&
                    y + j >= 0 && y + j < getHeight())
                {
                    Pixel neighbor = getPixel(x + i, y + j);
    
                    // Check if neighbor is not empty
                    if (!isEmpty(neighbor))
                    {
                        totalNeighbors++;
    
                        if (neighbor.getColor().equals(color))
                        {
                            sameColorCount++;
                        }
                    }
                }
            }
        }
    
        // If there are no neighbors, the pixel is satisfied
        if (totalNeighbors == 0)
        {
            return true;
        }
    
        // Calculate the proportion of same-color neighbors
        double proportion = (double) sameColorCount / totalNeighbors;
    
        // Return true if the proportion meets or exceeds
        //the satisfaction threshold
        return proportion >= satisfactionThreshold;
    }

    /**
     * Relocate pixel depending on multiple factors
     * @param pixel pixel instance checking to be relocated
     * @return the indicated boolean value
     */
    public boolean maybeRelocate(Pixel pixel)
    {
        // create random generator instance, max attempts, and og color
        Random generator = new Random();
        int maxAttempts = getWidth() * getHeight();
        Color originalColor = pixel.getColor();
        
        
        // loop thro each cord
        for (int attempt = 0; attempt < maxAttempts; attempt++)
        {
            // get new x and y cords, put in var for new location
            int newX = generator.nextInt(getWidth());
            int newY = generator.nextInt(getHeight());
            Pixel newLocation = getPixel(newX, newY);
            
            // swap colors and return bools
            if (isEmpty(newLocation) && isSatisfied(newLocation, originalColor))
            {
                pixel.setColor(Color.WHITE);
                newLocation.setColor(originalColor);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Loop through all elements, relocate if needed
     * @return an int representing amount of successful moves
     */
    public int cycleAgents()
    {
        // loop thro each pixel, if empty or satisfied, continue
        // if pixel not satisfied, relocate(). If relocate succesful, ++ int
        int succesfulRelocations = 0;
        for (int x = 0; x < getWidth(); x++)
        {
            for (int y = 0; y < getHeight(); y++)
            {
                Pixel currLocation = getPixel(x, y);
                Color currColor = currLocation.getColor();
                if (isEmpty(currLocation) || 
                    isSatisfied(currLocation, currColor))
                {
                    continue;
                }
                else
                {
                    boolean relocated = maybeRelocate(currLocation);
                    if (!relocated)
                    {
                        continue;
                    }
                    else
                    {
                        succesfulRelocations++;
                    }
                }
            }
        }
        return succesfulRelocations;
    }
    
}
