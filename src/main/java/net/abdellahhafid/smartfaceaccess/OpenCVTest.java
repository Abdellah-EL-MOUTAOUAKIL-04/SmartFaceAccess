package net.abdellahhafid.smartfaceaccess;

import net.abdellahhafid.smartfaceaccess.Dao.SingletonConnectionDB;
import org.opencv.core.Core;

public class OpenCVTest {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("OpenCV loaded successfully");
        //Test DB connection
        System.out.println(SingletonConnectionDB.getConnection());
    }
}
