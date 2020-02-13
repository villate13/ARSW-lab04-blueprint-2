/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintsFilter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author jmvillatei
 */
@Service
public class InMemoryBlueprintSubsamplingFilter implements BlueprintsFilter {

    @Override
    public Blueprint blueprintsFilter(Blueprint bp) {
        Blueprint bluep = bp;
        List<Point> points = new ArrayList<>();
        for (Integer x = 0; x < bp.getPoints().size(); x++) {
            if (x % 2 == 0) {
                System.out.println(x);
                points.add(bp.getPoints().get(x));
            }
        }
        bluep.setPoints(points);
        return bluep;
    }

}
