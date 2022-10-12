package io.automation.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static io.restassured.RestAssured.given;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public interface Api {

    Logger LOGGER = LoggerFactory.getLogger(Api.class);

    default <T> T doPOSTUntilDone(RequestSpecification spec, Class<T> clazz) {
        return doPOSTUntilDone(spec).as(clazz);
    }

    default <T> T getUntilDone(RequestSpecification spec, Class<T> clazz) {
        return getUntilDone(spec).as(clazz);
    }

    default Response doPOSTUntilDone(RequestSpecification spec) {
        spec = spec.log().all();
        Response response = spec.post().then().log().all().extract().response();
        try {
            StopWatch timer = new StopWatch();
            timer.start();
            while (timer.getTime() < MILLISECONDS.convert(Duration.ofSeconds(20))) {
                if (!(response.getStatusCode() == 201)) {
                    sleep(MILLISECONDS.convert(Duration.ofSeconds(5)));
                    LOGGER.warn("Request hasn't been succeed - {} - {}", response.getStatusCode(), response.getBody().asString());
                    LOGGER.warn("Request is running again");
                    response = spec.post()
                            .then().log().all()
                            .extract().response();
                } else {
                    break;
                }
            }
            timer.stop();
        } catch (InterruptedException e) {
            LOGGER.warn("Timer has been finished");
            currentThread().interrupt();
        }
        return response;
    }

    default Response getUntilDone(RequestSpecification spec) {
        spec = spec.log().all();
        Response response = spec.get().then().log().all().extract().response();
        try {
            StopWatch timer = new StopWatch();
            timer.start();
            while (timer.getTime() < MILLISECONDS.convert(Duration.ofSeconds(20))) {
                if (!(response.getStatusCode() == 200)) {
                    sleep(MILLISECONDS.convert(Duration.ofSeconds(5)));
                    LOGGER.warn("Request hasn't been succeed - {} - {}", response.getStatusCode(), response.getBody().asString());
                    LOGGER.warn("Request is running again");
                    response = spec.post()
                            .then().log().all()
                            .extract().response();
                } else {
                    break;
                }
            }
            timer.stop();
        } catch (InterruptedException e) {
            LOGGER.warn("Timer has been finished");
            currentThread().interrupt();
        }
        return response;
    }

    default RequestSpecification getRequestSpecification() {
        return given().filter(new AllureRestAssured());
    }
}