package zad1;

import org.json.JSONObject;

public class Weather {

    String weatherDesc;
    double temp;
    double humidity;
    double pressure;
    double wind;
    String city;
    String country;

    public Weather(String string, String string2, String string3) {

        city = new String(string2);
        country = new String(string3);
        JSONObject json = new JSONObject(string);

        //DESCRIPTION
        String tmp = json.get("weather").toString();
        weatherDesc = tmp.toString().substring(tmp.indexOf("\"description\":\"") + 15);
        weatherDesc = weatherDesc.substring(0, weatherDesc.indexOf("\""));

        //TEMPRATURE
        tmp = json.get("main").toString();
        tmp = tmp.substring(8, tmp.indexOf(','));
        temp = Double.parseDouble(tmp);

        //HUMIDITY
        tmp = json.get("main").toString();
        tmp = tmp.substring(tmp.indexOf("\"humidity\":") + 11);
        tmp = tmp.substring(0, tmp.indexOf(","));
        humidity = Double.parseDouble(tmp);

        //PRESSURE
        tmp = json.get("main").toString();
        tmp = tmp.substring(tmp.indexOf("\"pressure\":") + 11);
        tmp = tmp.substring(0, tmp.indexOf(","));
        pressure = Double.parseDouble(tmp);

        //WIND
        tmp = json.get("wind").toString();
        tmp = tmp.substring(tmp.indexOf("\"speed\":") + 8);
        tmp = tmp.replaceAll("[}]", ",");
        tmp = tmp.substring(0, tmp.indexOf(","));
        wind = Double.parseDouble(tmp);

    }

    public String getHTML() {
        return "<html>Weather for " + city + ", " + country + ":" +
                "<br>Description: " + weatherDesc +
                "<br>Temprature: " + temp + " °C" +
                "<br>Humidity: " + humidity + "%" +
                "<br>Pressure: " + pressure + " hPa" +
                "<br>Wind speed: " + wind + " m/s</html>";

    }

    public String toString() {
        return "Description: " + weatherDesc +
                "\nTemprature: " + temp + " °C\n" +
                "Humidity: " + humidity + "%" +
                "\nPressure: " + pressure + " hPa" +
                "\nWind speed: " + wind + " m/s";

    }

}

