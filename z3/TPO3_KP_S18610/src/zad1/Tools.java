/**
 *
 *  @author Kamiński Patryk S18610
 *
 */

package zad1;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Tools {

    public static Options createOptionsFromYaml(String fileName) {

        Yaml file = new Yaml();
        InputStream inputStream = null;

        try {
            inputStream = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = file.load(inputStream);

        Map<String, List<String>> clientsMap;

        clientsMap = (Map<String, List<String>>) map.get("clientsMap");

        return new Options(
                map.get("host").toString(),
                Integer.parseInt(map.get("port").toString()),
                Boolean.parseBoolean(map.get("concurMode").toString()),
                Boolean.parseBoolean(map.get("showSendRes").toString()),
                clientsMap
        );
    }
}
