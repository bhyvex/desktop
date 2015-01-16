/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stacksync.desktop.test.db;

import com.stacksync.desktop.Constants;
import com.stacksync.desktop.config.ConfigNode;
import com.stacksync.desktop.config.Database;
import com.stacksync.desktop.util.FileUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 *
 * @author cotes
 */
public class TestItem {
    
    private static Database database;
    
    @BeforeClass
    public static void setUpClass() {
        
        File configFolder = new File("database_test");
        database = new Database(configFolder.getAbsolutePath());
        
        // Create and copy config file
        File configFile = prepareConfigFile();
        
        // Read config file and load database
        loadDatabase(configFile);
        
        database.getEntityManager();
    }
    
    public static File prepareConfigFile() {
        File configFolder = new File("database_test");
        configFolder.mkdir();
        
        File configFile = new File(configFolder.getAbsoluteFile() + File.separator + Constants.CONFIG_FILENAME);
        
        InputStream is = null;
        try {
            is = TestItem.class.getResourceAsStream(Constants.CONFIG_DEFAULT_FILENAME);

            FileUtil.writeFile(is, configFile);
        } catch (IOException e) {
            assert false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                assert false;
            }
        }
        
        return configFile;
    }
    
    public static void loadDatabase(File configFile){
        
        InputStream configStream = null;
        try {
            configStream = new FileInputStream(configFile);
            parseConfigFile(configStream);
        } catch (FileNotFoundException ex) {
            assert false;
        } finally {
            try {
                if (configStream != null) {
                    configStream.close();
                }
            } catch (IOException ex) { }
        }
    }
    
    public static void parseConfigFile(InputStream configStream) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(configStream);
            ConfigNode node = new ConfigNode(doc.getDocumentElement());

            database.load(node.findChildByName("database"));

        } catch (Exception e) {
            assert false;
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        File configFolder = new File("database_test");
        try {
            FileUtils.deleteDirectory(configFolder);
        } catch (IOException ex) { }
    }
    
    //@Before
    public void setUp() {
    }
    
    //@After
    public void tearDown() {
    }
    
    @Test
    public void test() {
        System.out.println("Hola!");
    }

    /*public static void main(String[] args) {
        TestItem test = new TestItem();
        TestItem.setUpClass();
        
        
        TestItem.tearDownClass();
    }*/
}