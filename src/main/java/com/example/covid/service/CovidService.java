package com.example.covid.service;

import com.example.covid.model.TimeServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CovidService
{
    private final RestTemplate restTemplate;

    public String getQuarantineTime(int year, int month, int day)
    {
        try {
            TimeServerResponse timeServerResponse =
                    restTemplate.exchange("http://localhost:8080/api/v1/getTime",
                    HttpMethod.GET,
                    null,
                    TimeServerResponse.class)
                    .getBody();

            assert timeServerResponse != null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date currentDate = format.parse(timeServerResponse.getLocalTime());
            Date exposureDate = new Date (year, month, day);

            String daysLeftOfQuarantine = " ";
            String quarantineFinished = "Your quarantine is already finished!!!";
            String dateInvalid = "Sorry, your exposure date is invalid. It has not happened yet.";

            if (currentDate.before(exposureDate))
            {
                daysLeftOfQuarantine = dateInvalid + " Used the before method. Current Date = " + currentDate + " and Exposure Date is " + exposureDate;
            }
            else
            {
                long milliSecondsBetween = currentDate.getTime() - exposureDate.getTime();
                float daysBetween = TimeUnit.DAYS.convert(milliSecondsBetween, TimeUnit.MILLISECONDS);


                System.out.println(daysBetween);
                if (daysBetween < 0)
                {
                    daysLeftOfQuarantine = quarantineFinished;
                }
                else if (daysBetween > 14)
                {
                    daysLeftOfQuarantine = dateInvalid;
                }
                else
                {
                    daysLeftOfQuarantine = "You have " + daysBetween + " days left until you're done with quarantine. Good luck!";
                }
            }
            return daysLeftOfQuarantine;

        } catch (RestClientException | ParseException e) {
            //nothing
        }
        return null;
    }
}