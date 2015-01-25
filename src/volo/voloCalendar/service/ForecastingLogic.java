package volo.voloCalendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import volo.voloCalendar.dao.StoreDAO;
import volo.voloCalendar.entity.Store;
import volo.voloCalendar.model.ManualForecasting;
import volo.voloCalendar.util.UtilMethods;

import java.io.IOException;

/**
 * Created by Emin Guliyev on 25/01/2015.
 */
@Service
public class ForecastingLogic {
    @Autowired
    public StoreDAO storeDAO;

    //rest
    public  ManualForecasting getManualForecasting() {
        Store store = storeDAO.findById("manualForecasting");
        if (store == null){
            return new ManualForecasting();
        }
        String manualForecastingString = store.getData();
        ManualForecasting manualForecasting = null;
        try {
            manualForecasting = UtilMethods.convertJsonToObject(ManualForecasting.class, manualForecastingString);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return manualForecasting;
    }

    //rest
    public  void setManualForecasting(ManualForecasting manualForecasting) {
        try {
            String manualForecastingString = UtilMethods.convertObjectToJson(manualForecasting);
            Store store = new Store("manualForecasting", manualForecastingString);
            storeDAO.save(store);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
