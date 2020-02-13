/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
     * @return @throws BlueprintNotFoundException
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
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable("author") String author) {

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
    public ResponseEntity<?> getBlueprints(@PathVariable("author") String author, @PathVariable("name") String name) {

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
    
}

