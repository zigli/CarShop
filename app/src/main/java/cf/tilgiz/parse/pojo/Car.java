package cf.tilgiz.parse.pojo;

import java.util.Arrays;

public class Car {
    private int id;
    private int car_id;
    private transient String imageUrl;
    private String name;
    private int year;
    private int mileage;
    private int price;
    private transient byte[] imageBytes;

    public Car(int id, int car_id, String imageUrl, String name, int year, int mileage, int price, byte[] imageBytes) {
        this.id = id;
        this.car_id = car_id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.year = year;
        this.mileage = mileage;
        this.price = price;
        this.imageBytes = imageBytes;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id: " + id +
                ", car_id: " + car_id +
                ", name: '" + name + '\'' +
                ", year: " + year +
                ", mileage: " + mileage +
                ", price: " + price +
                '}';
    }
}
