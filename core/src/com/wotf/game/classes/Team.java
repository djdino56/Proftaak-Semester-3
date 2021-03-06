package com.wotf.game.classes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.wotf.game.classes.Items.Item;
import com.wotf.game.GameStage;
import static com.wotf.game.classes.GameSettings.WEAPONS_ARMORY;
import java.io.Serializable;

/**
 * Team contains data that represent a team Contains a list of players, list of
 * units, a list of items including ammo, a name and a team colour
 */
public class Team implements Serializable {

    private String name;
    // This is used for RMI because the Color of GDX can't be serialized. But we need them both
    private String colorName;
    private transient Color color;
    private Player player;
    private transient List<Unit> units;
    private transient Map<Item, Integer> items; // The integer represents the ammo remaining
    private Unit activeUnit;

    /**
     * Constructor of Team, Initialize lists and set active unit index to zero.
     *
     * @param name Name of the team
     * @param color Colour of the team
     */
    public Team(String name, Color color) {
        items = new HashMap<>();
        for (Item i : WEAPONS_ARMORY) {
            items.put(i, 99);
        }
        //items.put(WEAPONS_ARMORY.get(0), 99);

        this.name = name;
        this.color = color;

        //Instantiating list of items
        units = new ArrayList<>();
    }

    /**
     * Helper method called when team wasn't initalized by this application
     */
    public void intializeForClient() {
        items = new HashMap<>();
        for (Item i : WEAPONS_ARMORY) {
            items.put(i, 99);
        }
        units = new ArrayList<>();
    }

    /**
     * Sets the player associated with this team
     * @param player Player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * @return Returns the player associated with this team
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the name of the colour associated with this team
     * @param colorName Name of the colour to set
     */
    public void setColorname(String colorName) {
        this.colorName = colorName;
    }

    /**
     * Returns the name of the colour associated with this team
     * Used because Color for serialization
     * @return Name of the colour of this team
     */
    public String getColorname() {
        return colorName;
    }

    /**
     * @return the team name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the team
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return color of team
     */
    public Color getColor() {
        return color;
    }

    /**
     * set the color of team
     *
     * @param color color of team
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return all the units of team
     */
    public List<Unit> getUnits() {
        return Collections.unmodifiableList(units);
    }

    /**
     * Instantiates units list
     */
    public void makeUnitList() {
        //Instantiating list of items
        units = new ArrayList<>();
    }

    /**
     * @return active unit by active unit index
     */
    public Unit getActiveUnit() {
        return activeUnit;
    }

    /**
     * @param index index of player
     * @return unit by index
     */
    public Unit getUnit(int index) {
        return units.get(index);
    }

    /**
     * Add a unit to the team
     *
     * @param name name of the unit
     * @param health health of the unit
     */
    public void addUnit(String name, int health) {
        units.add(new Unit(name, health, this));
    }

    /**
     * When unit is killed (health is zero or lower), remove the actor and unit
     * from team
     *
     * @param unit to be removed
     */
    public void removeUnit(Unit unit) {
        if (unit != null && units.contains(unit)) {
            GameStage gameStage = (GameStage) unit.getStage();
            for (Actor actor : gameStage.getActors()) {
                if (actor == unit) {
                    actor.remove();
                    units.remove(unit);
                }
            }
        }
    }

    /**
     * Removes all the units from the team.
     *
     */
    public void removeAllUnits() {
        units.clear();
    }

    /**
     * Begin turn for team If its the first time for the team and there's no
     * active unit set it to the first After that get next active unit
     */
    public void beginTurn() {
        if (activeUnit == null) {
            activeUnit = units.get(0);
        } else {
            setNextActiveUnit();
        }
    }

    /**
     * end turn for team When unit has lower or equal than 0 health, remove the
     * unit from the team
     */
    public void endTurn() {
        List<Unit> unitsToRemove = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.getHealth() <= 0) {
                unitsToRemove.add(unit);
            }
        }
        
        for (int i = 0; i < unitsToRemove.size(); i++) {
            removeUnit(unitsToRemove.get(i));
        }
    }

    /**
     * Set the next active unit in the team
     */
    public void setNextActiveUnit() {
        if (!units.isEmpty()) {
            int activeUnitIndex = units.indexOf(activeUnit);

            // Change the active unit index if its not at the end of the list
            if (activeUnitIndex < (units.size() - 1)) {
                activeUnitIndex++;
            } else {
                activeUnitIndex = 0;
            }
            
            activeUnit = units.get(activeUnitIndex);
        } else {
            activeUnit = null;
        }
    }

    /**
     * Set active unit
     *
     * @param unit unit to set as active
     */
    public void setActiveUnit(Unit unit) {
        this.activeUnit = unit;
    }
    
    /**
     * At the end of a turn, check which units should be removed.
     */
    public void removeUnitsToBeRemoved() {
        List<Unit> unitsToRemove = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.getHealth() <= 0) {
                unitsToRemove.add(unit);
            }
        }
        
        for (int i = 0; i < unitsToRemove.size(); i++) {
            removeUnit(unitsToRemove.get(i));
        }
    }

    /**
     * select an item that is found inside the teamlist
     *
     * @param item item that needs to be selected
     * @return the selected item
     */
    public boolean selectItem(Item item) {
        return containsKey(item) != null;
    }

    /**
     * Check if the item exists inside the team's item list
     *
     * @param item item to check for
     * @return return the item that is found inside the team
     */
    public Item containsKey(Item item) {
        Item result = null;
        for (Entry<Item, Integer> entry : items.entrySet()) {
            if ((entry.getKey().getName().equals(item.getName()))) {
                result = entry.getKey();
                break;
            }
        }
        return result;
    }

    /**
     * Decrease the item amount for the selected item
     *
     * @param item selected
     * @param amount to increase
     */
    public void increaseItemAmount(Item item, int amount) {
        items.put(item, amount);
    }

    /**
     * Decrease the item amount for the selected item
     *
     * @param item to decrease ammo of
     * @param amount to decrease
     */
    public void decreaseItemAmount(Item item, int amount) {
        if (items.containsKey(item)) {

            if (items.get(item) > 0) {
                increaseItemAmount(item, (items.get(item) - 1));
            }
            if (items.get(item) == 0 || items.get(item) < 0) {
                // not implemented, do nothing
            }
        }
    }

    /**
     * Check if item contains an amount
     *
     * @param item to check for
     * @return amount of item with matching key
     */
    public int containsItemAmount(Item item) {
        if (items.containsKey(item)) {
            return items.get(item);
        }
        
        return 0;
    }

    /**
     * @return String representing the name and color of team
     */
    @Override
    public String toString() {
        return getName();
    }
}
