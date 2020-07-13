/*
Basic java to SQL connection for inserting user, interaction, and recieve the 
list of user a specific user has interacted with

to be done:
Delete data every 15 days
connect to actual database
 */
package com.mycompany.contacttracing;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.sound.sampled.FloatControl;

/**
 *
 * @author Kelvin
 */
public class Query {

    private Connection conn;
    private int userID = 50;
    private int interactionID = 0;

    private static final String FIND_USER = "SELECT i.id2 AS otherID\n"
            + "FROM Interaction as i\n"
            + "WHERE i.id1 == ?";
    private PreparedStatement findUserStatement;
    private static final String INSERT_USER = "INSERT INTO ID VALUES (?, ?, ?, ?, ?)";
    private PreparedStatement insertUserStatement;
    private static final String INSERT_INTERACTION = "INSERT INTO INTERACTION VALUES (?, ?, ?, ?, ?)";
    private PreparedStatement insertInteractionStatement;
    private static final String CLEAR_TABLE = "DELETE FROM INTERACTION; DELE FROM ID;";
    private PreparedStatement clearTableStatement;

    public void openConnection() throws IOException, SQLException {

    }

    public void closeConnection() throws SQLException {

    }

    // Prepare SQL Statements
    public void prepareStatements() throws SQLException {
        findUserStatement = conn.prepareStatement(FIND_USER);
        insertUserStatement = conn.prepareStatement(INSERT_USER);
        insertInteractionStatement = conn.prepareStatement(INSERT_INTERACTION);

    }

    // insert Users' personal information
    public void insertUser(String fname, String lname, int phone, String address) {
        try {
            insertUserStatement.clearParameters();
            insertUserStatement.setInt(1, userID++);
            insertUserStatement.setString(2, fname);
            insertUserStatement.setString(3, lname);
            insertUserStatement.setInt(4, phone);
            insertUserStatement.setString(5, address);
            insertUserStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // insert an interaction
    // check conditons for month, dayOfMonth, id1, id2
    public void insertInteraction(int month, int dayOfMonth, int id1, int id2) {
        if (id1 >= userID || id2 >= userID) {
            System.out.println("Invalid ID");
            return;
        }
        if (month < 0 || month > 12) {
            System.out.println("Invalid Month");
            return;
        }
        if (dayOfMonth < 0 || dayOfMonth > 31) {
            System.out.println("Invalid Date");
            return;
        }
        try {
            insertInteractionStatement.clearParameters();
            insertInteractionStatement.setInt(1, interactionID++);
            insertInteractionStatement.setInt(2, month);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Returns the id of every user that has interacted with the given id
    // preconditon: id must be valid
    // postconditon: Returns an int array of all the users interated with
    public int [] findUser(int id) {
        if (id < 0 ) {
            System.out.println("Invalid User");
            return null;
        }
        int[] storage;
        int count = 0;
        try {
            findUserStatement.clearParameters();
            findUserStatement.setInt(1, id);
            ResultSet result = findUserStatement.executeQuery();
            storage = new int[result.getRow()];
            while (result.next()) {
                storage[count++] = result.getInt("otherID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return storage;
    }

}
