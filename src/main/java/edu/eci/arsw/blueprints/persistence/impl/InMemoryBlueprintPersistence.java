/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 */
@Service
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        /*Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("_authorname_", "_bpname_ ",pts);
        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);*/
        
        Point[] pts1 = new Point[]{new Point(140, 140), new Point(115, 115)};
        Point[] pts2 = new Point[]{new Point(150, 150), new Point(116, 116)};
        Point[] pts3 = new Point[]{new Point(160, 160), new Point(117, 117)};
        Point[] pts4 = new Point[]{new Point(170, 170), new Point(116, 116)};

        Blueprint bp1 = new Blueprint("Villate", "bpProduc1", pts1);
        Blueprint bp2 = new Blueprint("Villate", "bpProduc2", pts2);
        Blueprint bp3 = new Blueprint("Juan", "bpProduc3", pts3);
        Blueprint bp4 = new Blueprint("Manuel", "bpProduc4", pts4);

        blueprints.put(new Tuple<>(bp1.getAuthor(), bp1.getName()), bp1);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
        blueprints.put(new Tuple<>(bp4.getAuthor(), bp4.getName()), bp4);
        
    }    
    
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> bluep = new HashSet<>();
        for (Tuple<String, String> x : blueprints.keySet()) {
            if (author.equals(x.getElem1())) {
                bluep.add(blueprints.get(x));
            }
        }
        return bluep;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        Set<Blueprint> bluep = new HashSet<>();
        for(Tuple<String, String> x : blueprints.keySet()){
            bluep.add(blueprints.get(x));
        }
        return bluep;
    }

    
    
}
