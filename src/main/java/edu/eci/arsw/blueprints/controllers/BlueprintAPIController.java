/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;

import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices bpServices;

    /**
     *
     * @return all blueprints 
     * @throws BlueprintNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllBlueprints() throws BlueprintNotFoundException {

        try {
            Map<String, Blueprint> blueprints = new HashMap();

            List<Blueprint> bpList = new ArrayList<>();
            bpList.addAll(bpServices.getAllBlueprints());

            for (Blueprint x : bpList) {
                blueprints.put(x.getName(), x);
            }

            String data = new Gson().toJson(blueprints);

            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("ERROR, >> No se han podido obtener los planos", HttpStatus.NOT_FOUND);
        }
    }   
    
    /**
     * 
     * @param author
     * @return all blueprints by Author
     */
    @RequestMapping(method = RequestMethod.GET, path = "{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable("author") String author) throws BlueprintNotFoundException {

        try {
            Map<String, Blueprint> blueprints = new HashMap();

            List<Blueprint> bpList = new ArrayList<>();
            bpList.addAll(bpServices.getBlueprintsByAuthor(author));

            for (Blueprint x : bpList) {
                blueprints.put(x.getName(), x);
            }
            

            String data = new Gson().toJson(blueprints);

            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("ERROR, >> No se han podido obtener los planos del autor: "+author, HttpStatus.NOT_FOUND);
        }
    }   
    
    /**
     * 
     * @param author
     * @param name
     * @return blueprint by author and by blueprint name
     */
    @RequestMapping(method = RequestMethod.GET, path = "{author}/{name}")
    public ResponseEntity<?> getBlueprints(@PathVariable("author") String author, @PathVariable("name") String name) throws BlueprintNotFoundException {

        try {
            Map<String, Blueprint> blueprints = new HashMap();
            
            blueprints.put(bpServices.getBlueprint(author, name).getName(),bpServices.getBlueprint(author, name));

            String data = new Gson().toJson(blueprints);

            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("ERROR, >> No se han podido obtener el plano "+name+" del autor: "+author, HttpStatus.NOT_FOUND);
        }
    }   
    
    /**
     * 
     * @param blueprint
     * @return status http servidor
     * @throws BlueprintPersistenceException 
     */
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addNewBlueprint(@RequestBody String bp) throws BlueprintPersistenceException {
        //Formato json {"bp5":{"author":"Isaza","points":[{"x":180,"y":181},{"x":190,"y":190}],"name":"bp5"}}
        try {
            //System.out.println("data"+bp);
            Type jsonToList = new TypeToken<Map<String, Blueprint>>() {}.getType();
            
            Map<String, Blueprint> data = new Gson().fromJson(bp, jsonToList);

            Object[] keys = data.keySet().toArray();
            
            //System.out.println("llave"+keys[0].toString());

            bpServices.addNewBlueprint(data.get(keys[0]));

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (BlueprintPersistenceException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("ERROR. >> No se ha podido añadir el blueprint", HttpStatus.FORBIDDEN);
        }
    }
    
    @RequestMapping(method = RequestMethod.PUT, path = "{author}/{name}")
    public ResponseEntity<?> updateABlueprint(@PathVariable("author") String author,
            @PathVariable("name") String name, @RequestBody String points) throws BlueprintNotFoundException {
        //Formato json {"1":[{"x":9,"y":9},{"x":10,"y":10}]}
        try {
            //System.out.println("puntos"+points);
            Blueprint selectBP = bpServices.getBlueprint(author, name);

            Type jsonToList = new TypeToken<Map<String, Point[]>>() {
            }.getType();
            Map<String, Point[]> data = new Gson().fromJson(points, jsonToList);

            Object[] keys = data.keySet().toArray();
            
            //System.out.println("llave"+keys[0].toString());

            selectBP.setPoints(Arrays.asList(data.get(keys[0])));

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("ERROR, >> No se ha podido actualizar el blueprint", HttpStatus.FORBIDDEN);
        }
    }
}

