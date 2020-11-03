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
import java.util.Date;

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
            //
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(timeServerResponse.getLocalTime());
            //
            /*LocalDateTime date = LocalDateTime.parse(timeServerResponse.getLocalTime()); */
            int currentDay = date.getDay();
            int currentMonth = date.getMonth();
            int currentYear = date.getYear();

            String daysLeftOfQuarantine = " ";
            String quarantineFinished = "Your quarantine is already finished!!!";
            String dateInvalid = "Sorry, your exposure date is invalid. It has not happened yet.";
            ////
            Date currentDate = new Date (currentYear, currentMonth, currentDay);
            Date exposureDate = new Date (year, month, day);

            if (currentDate.before(exposureDate))
            {
                daysLeftOfQuarantine = dateInvalid;
            }
            else
            {
                long daysBetween = currentDate.getTime() - exposureDate.getTime();
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