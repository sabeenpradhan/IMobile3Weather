package com.sabeen.weather;

import com.sabeen.weather.model.AllWeatherDTO;

/**
 *Interface for getting onPostExecute result of Weather Async Call
 * Created by sabeen on 6/14/16.
 */

public interface AsyncResponseInterface {

    void processFinish(AllWeatherDTO allWeatherDTO);
}
