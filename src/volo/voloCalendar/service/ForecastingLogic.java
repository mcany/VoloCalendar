package volo.voloCalendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import volo.voloCalendar.dao.OrderDayStatisticsDAO;
import volo.voloCalendar.dao.StoreDAO;
import volo.voloCalendar.entity.OrderDayStatistics;
import volo.voloCalendar.entity.Store;
import volo.voloCalendar.externalModel.Orders;
import volo.voloCalendar.util.Settings;
import volo.voloCalendar.viewModel.forecasting.ManualForecasting;
import volo.voloCalendar.util.UtilMethods;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Emin Guliyev on 25/01/2015.
 */
@Service
public class ForecastingLogic {
    @Autowired
    public StoreDAO storeDAO;
    @Autowired
    public OrderDayStatisticsDAO orderDayStatisticsDAO;

    public static void main(String[] args) {
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("file:D:/projects/VoloCalendar/web/WEB-INF/config/applicationContext.xml");
        ForecastingLogic forecastingLogic = context.getBean(ForecastingLogic.class);

        java.sql.Date lowerBound = new java.sql.Date(2013 - 1900, 1, 1);
        forecastingLogic.updateDatabase(lowerBound);
        int all = forecastingLogic.deleteOutliers(2, lowerBound);
        System.out.println("Deleted: " + all);
        forecastingLogic.calculateForecasting(lowerBound);
    }

    //rest
    public ManualForecasting getManualForecasting() {
        Store store = storeDAO.findById("manualForecasting");
        if (store == null) {
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
    public void setManualForecasting(ManualForecasting manualForecasting) {
        try {
            String manualForecastingString = UtilMethods.convertObjectToJson(manualForecasting);
            Store store = new Store("manualForecasting", manualForecastingString);
            storeDAO.save(store);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateDatabase(java.util.Date lowerBound) {
        Date[] dateArray;
        try {
            //TODO 5: comment next uncomment after next
            dateArray = getOrderDatesFromFile();
            //dateArray = getOrderDatesByRest(lowerBound);
        } catch (RestClientException ex) {
            ex.printStackTrace();
            return false;
        }

        orderDayStatisticsDAO.deleteAll();

        if (dateArray.length > 0) {
            OrderDayStatistics orderDayStatistics = new OrderDayStatistics(dateArray[0]);
            for (Date date : dateArray) {
                if (!UtilMethods.getSqlDate(date).equals(orderDayStatistics.getDate())) {
                    orderDayStatisticsDAO.save(orderDayStatistics);
                    orderDayStatistics = new OrderDayStatistics(date);
                }
                orderDayStatistics.addStatistics(date);
            }

            orderDayStatisticsDAO.save(orderDayStatistics);
        }
        return true;
    }

    private Date[] getOrderDatesFromFile() {
        try {
            ArrayList<Date> dates = new ArrayList<Date>();
            FileReader fr = new FileReader(Settings.testDataFilePath);
            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Date date = (Date) formatter.parse(sCurrentLine);
                dates.add(date);
            }
            return dates.toArray(new Date[dates.size()]);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //TODO 6: Order filtering, get only orders up to lowerBound
    private Date[] getOrderDatesByRest(Date lowerBound) throws RestClientException {
        Date[] dateArray;
        HttpEntity<Object> httpEntity = UtilMethods.getAuthenticatedObjectHttpEntity(null);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Orders> entity = restTemplate.exchange(
                "http://staging.volo.de/orders", HttpMethod.GET,
                httpEntity, Orders.class);
        dateArray = entity.getBody().convertToSortedDateArray();
        return dateArray;
    }

    public int deleteOutliers(double sigma, java.sql.Date lowerBound) {
        int deletedOutliers = 0;

        List<Object[]> avgAndStddevsList = orderDayStatisticsDAO.getAverageAndStandardDeviationsWeek(lowerBound);

        for (Object[] avgAndStddevs : avgAndStddevsList) {
            int count = orderDayStatisticsDAO.deleteOutlierDays(((BigDecimal) avgAndStddevs[1]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[2]).doubleValue(), ((BigDecimal) avgAndStddevs[3]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[4]).doubleValue(), ((BigDecimal) avgAndStddevs[5]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[6]).doubleValue(), ((BigDecimal) avgAndStddevs[7]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[8]).doubleValue(), ((BigDecimal) avgAndStddevs[9]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[10]).doubleValue(), ((BigDecimal) avgAndStddevs[11]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[12]).doubleValue(), ((BigDecimal) avgAndStddevs[13]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[14]).doubleValue(), ((BigDecimal) avgAndStddevs[15]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[16]).doubleValue(), ((BigDecimal) avgAndStddevs[17]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[18]).doubleValue(), ((BigDecimal) avgAndStddevs[19]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[20]).doubleValue(), ((BigDecimal) avgAndStddevs[21]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[22]).doubleValue(), ((BigDecimal) avgAndStddevs[23]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[24]).doubleValue(), ((BigDecimal) avgAndStddevs[25]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[26]).doubleValue(), ((BigDecimal) avgAndStddevs[27]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[28]).doubleValue(), ((BigDecimal) avgAndStddevs[29]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[30]).doubleValue(), ((BigDecimal) avgAndStddevs[31]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[32]).doubleValue(), ((BigDecimal) avgAndStddevs[33]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[34]).doubleValue(), ((BigDecimal) avgAndStddevs[35]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[36]).doubleValue(), ((BigDecimal) avgAndStddevs[37]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[38]).doubleValue(), ((BigDecimal) avgAndStddevs[39]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[40]).doubleValue(), ((BigDecimal) avgAndStddevs[41]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[42]).doubleValue(), ((BigDecimal) avgAndStddevs[43]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[44]).doubleValue(), ((BigDecimal) avgAndStddevs[45]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[46]).doubleValue(), ((BigDecimal) avgAndStddevs[47]).doubleValue()
                    , ((BigDecimal) avgAndStddevs[48]).doubleValue(), (short) (int) (Integer) avgAndStddevs[0], sigma, lowerBound);
            deletedOutliers += count;
        }
        return deletedOutliers;
    }

    public void calculateForecasting(java.sql.Date lowerBound) {
        ManualForecasting manualForecasting = new ManualForecasting();
        List<Object[]> averagesList = orderDayStatisticsDAO.getWeekForecastingByDateLowerBound(lowerBound);
        for (Object[] averages : averagesList) {
            for (int i = 0; i < 24; i++) {
                manualForecasting.getDays()[((short) (Short) averages[0]) - 1][i].setCount((int) Math.round((double) (Double) averages[i + 1]));
            }
        }
        setManualForecasting(manualForecasting);
    }
}
