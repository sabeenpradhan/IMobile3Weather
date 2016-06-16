package com.imobile3.weather;

import com.imobile3.weather.model.AllWeatherDTO;

/**
 *Interface for getting onPostExecute result of Weather Async Call
 * Created by sabeen on 6/14/16.
 */

public interface AsyncResponseInterface {

    void processFinish(AllWeatherDTO allWeatherDTO);
}
