package com.example.covid.service;

import com.example.covid.model.TimeServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

            LocalDate currentDate = LocalDate.parse(timeServerResponse.getLocalTime());
            LocalDate exposureDate = LocalDate.of(year, month, day);

            String daysLeftOfQuarantine = " ";
            String quarantineFinished = "Your quarantine is already finished!!!";
            String dateInvalid = "Sorry, your exposure date is invalid. It has not happened yet.";

            if (currentDate.compareTo(exposureDate) < 0)
            {
                daysLeftOfQuarantine = dateInvalid;
            }
            else
            {
                long daysBetween = ChronoUnit.DAYS.between(exposureDate, currentDate);
                long daysLeft = 14 - daysBetween;

                if (daysLeft <= 0) {
                    daysLeftOfQuarantine = quarantineFinished;
                } else
                {
                    if (daysLeft == 1)
                    {
                        daysLeftOfQuarantine = "You have " + daysLeft + " day left until you're done with quarantine. Good luck!";
                    } else {
                        daysLeftOfQuarantine = "You have " + daysLeft + " days left until you're done with quarantine. Good luck!";
                    }
                }
            }
            return daysLeftOfQuarantine;

        } catch (RestClientException e) {
            //nothing
        }
        return null;
    }
}