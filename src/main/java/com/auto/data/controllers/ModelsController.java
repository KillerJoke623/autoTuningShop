package com.auto.data.controllers;

import com.auto.data.repositories.ManufacturersRepository;
import com.auto.data.repositories.ModelRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.auto.data.models.Model;

import java.util.List;

@RestController
public class ModelsController {


    private final ManufacturersRepository manufacturersRepository;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    public ModelsController(ManufacturersRepository manufacturersRepository) {
        this.manufacturersRepository = manufacturersRepository;
    }

    @GetMapping("/api/models/manufacturer/{manufacturerId}")
    public String getModelsByManufacturer(@PathVariable Integer manufacturerId) throws JSONException {
        // Get the models for the specified manufacturer.
        List<Model> models = manufacturersRepository.findById(manufacturerId).get().getModels();

        JSONArray jsonArray = new JSONArray();
        // Create a JSON object.
        JSONObject jsonObject = new JSONObject();

        for (Model model : models) {
            jsonArray.put(
                    new JSONObject()
                            .put("model_id", model.getModel_id())
                            .put("model_name", model.getModel_name())
            );

        }



        // Add the models property to the JSON object.
        jsonObject.put("models", jsonArray);

        // Convert the JSON object to a string.
        String jsonString = gson.toJson(jsonArray);


        // Return the models.
        return jsonString;
    }
}
