package com.savonikyurii.beatifulkrivbas.GeolocationAPI.AdditionalClasses;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses.BasicUser;
import com.savonikyurii.beatifulkrivbas.GeolocationAPI.AbstractClasses.GeoPosition;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/*Клас серіалізації даних SerializableController*/
public class SerializableController {
    /*Поля класу*/
    private File file;
    /*Конструктори*/
    public SerializableController(File file) {
        this.file = file;
    }
    /*Метод серіалізації Користувача*/
    public <T extends BasicUser> void WriteUser(T user){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath())))
        {
            oos.writeObject(user);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /*Метод серіалізації списку Користувачів*/
    public <T extends BasicUser> void WriteUsers(ArrayList<T> users){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath())))
        {
            oos.writeObject(users);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /*Метод серіалізації місця*/
    public <T extends GeoPosition> void WritePlace(T place){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath())))
        {
            oos.writeObject(place);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /*Метод серіалізації спику місць*/
    public <T extends GeoPosition> void WritePlaces(ArrayList<T> place){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath())))
        {
            oos.writeObject(place);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    /*Метод зчитування користувача з файлу даних*/
    public <T extends BasicUser> T ReadUser(Class<T> tClass){
        T obj = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath())))
        {
            obj=(T)ois.readObject();
        }
        catch(Exception ex){
            return obj;
        }
        return obj;
    }
    /*Метод зчитування списку користувачів з файлу даних*/
    public <T extends BasicUser> ArrayList<T> ReadUsers(Class<T> tClass){
        ArrayList<T> obj = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath())))
        {
            obj=(ArrayList<T>) ois.readObject();
        }
        catch(Exception ex){
            return new ArrayList<>();
        }
        return obj;
    }
    /*Метод зчитування місця з файлу даних*/
    public <T extends GeoPosition> T ReadPlace(Class<T> clazz){
        T obj = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath())))
        {
            obj=(T)ois.readObject();
        }
        catch(Exception ex){
            return obj;
        }
        return obj;
    }
    /*Метод зчитування списка місць з файлу даних*/
    public <T extends GeoPosition> ArrayList<T> ReadPlaces(){
        ArrayList<T> obj = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath())))
        {
            obj=(ArrayList<T>) ois.readObject();
        }
        catch(Exception ex){
            return new ArrayList<>();
        }
        return obj;
    }
    /*Метод призначений для серіалізації будь-якого об'єкту*/
    public <T> void WriteObject(T obj){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file.getAbsolutePath())))//створюємо файл
        { oos.writeObject(obj); } //запис об'єкту
        catch(Exception ex){ System.out.println(ex.getMessage()); } // в разі помилки виводимо помилку
    }
    /*Метод зчитування будь-якого об'єкту з указанням к якому саме типо потрібно привести зчитаний об'єкт*/
    public <T> T ReadObject(Class<T> tClass){
        T obj = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.getAbsolutePath()))) // відкриваємо
        {obj=(T) ois.readObject(); } // зчитуємо дані
        catch(Exception ex){return null;} // в разі помилки повертаємо null
        return obj;//повертаємо зчитані дані
    }
}
